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
import android.graphics.Bitmap.CompressFormat;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;


/**
 * This class holds our bitmap caches (memory and disk).
 */
public class ImageCache {
    private static final String TAG = "yyt";

    // Default memory cache size in kilobytes
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5;

    // Default disk cache size in bytes
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 20;

    // Compression settings when writing images to disk cache
    public static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;

    public static final int DEFAULT_COMPRESS_QUALITY = 75;

    // Constants to easily toggle various caches
    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;

    private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;

    private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;

    private static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = true;

    private DiskLruCacheOld mDiskCache;

    private LruCache<String, Bitmap> mMemoryCache;

    private ImageCacheParams mCacheParams;

    private ThreadPoolExecutor threadPool;

    private Context mContext;

    /**
     * Creating a new ImageCache object using the specified parameters.
     *
     * @param cacheParams The cache parameters to use to initialize the cache
     */
    public ImageCache(Context context, ImageCacheParams cacheParams) {
        init(cacheParams);
        mContext = context;
    }

    /**
     * Creating a new ImageCache object using the default parameters.
     *
     * @param context    The context to use
     * @param uniqueName A unique name that will be appended to the cache
     *                   directory
     */
    public ImageCache(Context context, String uniqueName) {
        init(new ImageCacheParams(context, uniqueName));
        mContext = context;
    }

    /**
     * Initialize the cache, providing all parameters.
     *
     * @param cacheParams The cache parameters to initialize the cache
     */
    private void init(ImageCacheParams cacheParams) {
        mCacheParams = cacheParams;

        // Set up memory cache
        if (mCacheParams.memoryCacheEnabled) {
            if (LogUtil.LOG_MODE) {
                Log.d(TAG, "Memory cache created (size = " + mCacheParams.memCacheSize + ")");
            }
            mMemoryCache = new LruCache<String, Bitmap>(mCacheParams.memCacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    final int bitmapSize = ImageCacheUtils.getBitmapSize(bitmap) / 1024;
                    return bitmapSize == 0 ? 1 : bitmapSize;
                }

                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue,
                                            Bitmap newValue) {
                    // LogUtil.i("hard cache is full , entryRemoved");
                }
            };
        }

        // Set up disk cache
        if (cacheParams.diskCacheEnabled) {
            mDiskCache = DiskLruCacheOld.openCache(mContext, mCacheParams.diskCacheDir,
                    cacheParams.diskCacheSize);
        }

        threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    /**
     * Adds a bitmap to both memory and disk cache.
     *
     * @param data   Unique identifier for the bitmap to store
     * @param bitmap The bitmap to store
     */
    public void addBitmapToCache(String data, Bitmap bitmap) {
        if (data == null || bitmap == null) {
            return;
        }

        // Add to memory cache
        if (mMemoryCache != null && mMemoryCache.get(data) == null) {
            mMemoryCache.put(data, bitmap);
        }

        threadPool.execute(new AddCacheRequest(data, bitmap));
    }

    class AddCacheRequest implements Runnable {
        private String mData;

        private Bitmap mBitmap;

        public AddCacheRequest(String data, Bitmap bitmap) {
            mData = data;
            mBitmap = bitmap;
        }

        @Override
        public void run() {
            if (Utils.isEmpty(mData) || null == mBitmap || mBitmap.isRecycled()) {
                return;
            }
            try {
                // Add to disk cache
                if (mDiskCache != null && !mDiskCache.containsKey(mData)) {
                    mDiskCache.put(mData, mBitmap);
                }
            } catch (Exception e) {
                LogUtil.e("addBitmapToCache - " + e);
            }
        }
    }

    /**
     * Get from memory cache.
     *
     * @param data Unique identifier for which item to get
     * @return The bitmap if found in cache, null otherwise
     */
    public Bitmap getBitmapFromMemCache(String data) {
        if (mMemoryCache != null) {
            final Bitmap memBitmap = mMemoryCache.get(data);
            if (memBitmap != null && !memBitmap.isRecycled()) {
                LogUtil.i("Memory cache hit");
                return memBitmap;
            }
        }
        return null;
    }

    public String getFilePath(String key) {
        if (null != mDiskCache) {
            return mDiskCache.getFilePath(key);
        }
        return null;
    }

    /**
     * Get from disk cache.
     *
     * @param data Unique identifier for which item to get
     * @return The bitmap if found in cache, null otherwise
     */
    public Bitmap getBitmapFromDiskCache(String data) {
        LogUtil.i("mDiskLruCache:" + mDiskCache);
        try {
            if (mDiskCache != null) {
                return mDiskCache.get(data);
            }
        } catch (final OutOfMemoryError e) {
            LogUtil.e("getBitmapFromDiskCache - " + e);
        }
        return null;
    }

    public void clearMemCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
            if (LogUtil.LOG_MODE) {
                Log.d(TAG, "Memory cache cleared");
            }
        }
    }

    /**
     * Clears both the memory and disk cache associated with this ImageCache
     * object. Note that this includes disk access so this should not be
     * executed on the main/UI thread.
     */
    public void clearCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
            if (LogUtil.LOG_MODE) {
                Log.d(TAG, "Memory cache cleared");
            }
        }
        if (null != mDiskCache) {
            mDiskCache.clearCache(false);
        }
    }

    /**
     * A holder class that contains cache parameters.
     */
    public static class ImageCacheParams {
        public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;

        public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;

        public File diskCacheDir;

        public CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;

        public int compressQuality = DEFAULT_COMPRESS_QUALITY;

        public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;

        public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;

        public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;

        public boolean initDiskCacheOnCreate = DEFAULT_INIT_DISK_CACHE_ON_CREATE;

        public ImageCacheParams(Context context, String uniqueName) {
            diskCacheDir = ImageCacheUtils.getDiskCacheDir(context, uniqueName);
        }

        public ImageCacheParams(File diskCacheDir) {
            this.diskCacheDir = diskCacheDir;
        }

        public void setMemCacheSizePercent(float percent) {
            if (percent < 0.05f || percent > 0.8f) {
                throw new IllegalArgumentException("setMemCacheSizePercent - percent must be "
                        + "between 0.05 and 0.8 (inclusive)");
            }
            long maxMem = Runtime.getRuntime().maxMemory();
            memCacheSize = Math.round(percent * maxMem / 1024);
            LogUtil.i("memCacheSize:" + memCacheSize + ",maxMem:" + maxMem);
        }
    }
}
