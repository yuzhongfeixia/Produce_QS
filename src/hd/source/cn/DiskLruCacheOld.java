/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hd.source.cn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import hd.utils.cn.LogUtil;

public class DiskLruCacheOld {
    private static final String TAG = "yyt";

    private static final String CACHE_FILENAME_PREFIX = "cache_";

    private static final int MAX_REMOVALS = 8;

    private static final int INITIAL_CAPACITY = 32;

    private static final float LOAD_FACTOR = 0.75f;

    public static final int IO_BUFFER_SIZE = 8 * 1024;

    private final File mCacheDir;

    private int cacheSize = 0;

    private int cacheByteSize = 0;

    private final int maxCacheItemSize = 64; // 64 item default

    private long maxCacheByteSize = 1024 * 1024 * 20;

    private final Map<String, String> mLinkedHashMap = Collections
            .synchronizedMap(new LinkedHashMap<String, String>(INITIAL_CAPACITY, LOAD_FACTOR, true));

    private static final FilenameFilter cacheFileFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            LogUtil.i("accept:" + filename);
            return filename.startsWith(CACHE_FILENAME_PREFIX);
        }
    };

    public static DiskLruCacheOld openCache(Context context, File cacheDir, long maxByteSize) {
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        if (cacheDir.isDirectory() && cacheDir.canWrite()
                && ImageCacheUtils.getUsableSpace(cacheDir) > maxByteSize) {
            return new DiskLruCacheOld(cacheDir, maxByteSize);
        }

        return null;
    }

    private DiskLruCacheOld(File cacheDir, long maxByteSize) {
        mCacheDir = cacheDir;
        maxCacheByteSize = maxByteSize;
    }

    public void put(String key, Bitmap data) {
        synchronized (mLinkedHashMap) {
            if (mLinkedHashMap.get(key) == null) {
                try {
                    final String file = createFilePath(mCacheDir, key);
                    LogUtil.i("put 0:" + file);
                    if (writeBitmapToFile(data, file)) {
                        put(key, file);
                        flushCache();
                    }
                } catch (final FileNotFoundException e) {
                    Log.e(TAG, "Error in put: " + e.getMessage());
                } catch (final IOException e) {
                    Log.e(TAG, "Error in put: " + e.getMessage());
                }
            }
        }
    }

    public void put(String key, byte[] binaryData) {
        synchronized (mLinkedHashMap) {
            if (mLinkedHashMap.get(key) == null) {
                try {
                    final String file = createFilePath(mCacheDir, key);
                    LogUtil.i("put 1:" + file);
                    if (writeBinaryToFile(binaryData, file)) {
                        put(key, file);
                        flushCache();
                    }
                } catch (final FileNotFoundException e) {
                    Log.e(TAG, "Error in put: " + e.getMessage());
                } catch (final IOException e) {
                    Log.e(TAG, "Error in put: " + e.getMessage());
                }
            }
        }
    }

    private void put(String key, String file) {
        mLinkedHashMap.put(key, file);
        cacheSize = mLinkedHashMap.size();
        cacheByteSize += new File(file).length();
    }

    private void flushCache() {
        Entry<String, String> eldestEntry;
        File eldestFile;
        long eldestFileSize;
        int count = 0;

        while (count < MAX_REMOVALS
                && (cacheSize > maxCacheItemSize || cacheByteSize > maxCacheByteSize)) {
            eldestEntry = mLinkedHashMap.entrySet().iterator().next();
            if (!eldestEntry.getValue().contains("cache_save_")) {
                eldestFile = new File(eldestEntry.getValue());
                eldestFileSize = eldestFile.length();
                mLinkedHashMap.remove(eldestEntry.getKey());
                eldestFile.delete();
                cacheSize = mLinkedHashMap.size();
                cacheByteSize -= eldestFileSize;
                count++;
                LogUtil.d("flushCache - Removed cache file, " + eldestFile + ", " + eldestFileSize);
            }
        }
    }

    public String getFilePath(String key) {
        synchronized (mLinkedHashMap) {
            final String file = mLinkedHashMap.get(key);
            return file;
        }
    }

    public Bitmap get(String key) {
        synchronized (mLinkedHashMap) {
            final String file = mLinkedHashMap.get(key);
            if (file != null) {
                LogUtil.d("Disk cache hit");
                return BitmapFactory.decodeFile(file);
            } else {
                final String existingFile = createFilePath(mCacheDir, key);
                if (new File(existingFile).exists()) {
                    put(key, existingFile);
                    LogUtil.d("Disk cache hit (existing file)");
                    return BitmapFactory.decodeFile(existingFile);
                }
            }
            return null;
        }
    }

    public boolean containsKey(String key) {
        if (mLinkedHashMap.containsKey(key)) {
            return true;
        }

        final String existingFile = createFilePath(mCacheDir, key);
        if (new File(existingFile).exists()) {
            put(key, existingFile);
            return true;
        }
        return false;
    }

    /**
     * Removes all disk cache entries from this instance cache dir
     */
    public void clearCache(boolean force) {
        DiskLruCacheOld.clearCache(mCacheDir, force);
    }

    public static void clearCache(File cacheDir, boolean force) {
        final File[] files = cacheDir.listFiles(cacheFileFilter);
        if (null == files) {
            return;
        }
        if (files.length < 100 && !force) {
            return;
        }
        LogUtil.d("---------clearCache------------");
        for (int i = 0; i < files.length; i++) {
            if (!files[i].getPath().contains("cache_save_")) {
                files[i].delete();
            }
        }
    }

    private static String createFilePath(File cacheDir, String key) {
        try {
            return cacheDir.getAbsolutePath()
                    + File.separator
                    + CACHE_FILENAME_PREFIX
                    + URLEncoder.encode(key.replace("*", "").replace(":", "").replace("/", "")
                    .replace(".", "_"), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            Log.e(TAG, "createFilePath - " + e);
        }

        return null;
    }

    public String createFilePath(String key) {
        return createFilePath(mCacheDir, key);
    }

    private boolean writeBitmapToFile(Bitmap bitmap, String file) throws IOException,
            FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file), IO_BUFFER_SIZE);
            return bitmap.compress(ImageCache.DEFAULT_COMPRESS_FORMAT,
                    ImageCache.DEFAULT_COMPRESS_QUALITY, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private boolean writeBinaryToFile(byte[] binaryData, String file) throws IOException,
            FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(binaryData, 0, binaryData.length);
            out.flush();
            return true;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
