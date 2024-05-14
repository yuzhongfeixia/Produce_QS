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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;

/**
 * A simple subclass of {@link ImageResizer} that fetches and resizes images
 * fetched from a URL.
 */
public class ImageFetcher extends ImageWorker {
    private static final String TAG = "HDCN";

    private static final int HTTP_CACHE_SIZE = 5 * 1024 * 1024; // 10MB

    public static final String HTTP_CACHE_DIR = "http";

    private DiskLruCacheOld mHttpCache;

    /**
     * Initialize providing a target image width and height for the processing
     * images.
     *
     * @param context
     */
    public ImageFetcher(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        checkConnection(context);
    }

    /**
     * Simple network connection check.
     *
     * @param context
     */
    private void checkConnection(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            Log.e(TAG, "checkConnection - no connection found");
        }
    }

    public void clearCache() {
        if (null != mHttpCache) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    mHttpCache.clearCache(true);
                }
            }).start();
        }
    }

    public String getFilePath(String key) {
        if (null != mHttpCache) {
            return mHttpCache.getFilePath(key);
        }
        return null;
    }

    /**
     * The main process method, which will be called by the ImageWorker in the
     * AsyncTask background thread.
     *
     * @param data The data to load the bitmap, in this case, a regular http URL
     * @return The downloaded and resized bitmap
     */
    private Bitmap processBitmap(String data, BitmapParams params) {
        FileDescriptor fileDescriptor = null;
        FileInputStream fileInputStream = null;

        if (null == mHttpCache) {
            final File cacheDir = ImageCacheUtils.getDiskCacheDir(mContext, HTTP_CACHE_DIR);
            mHttpCache = DiskLruCacheOld.openCache(mContext, cacheDir, HTTP_CACHE_SIZE);
        }

        if (null == mHttpCache) {
            LogUtil.i("no mHttpCache");
            return null;
        }

        LogUtil.i("processBitmap:" + data);
        final File cacheFile = new File(mHttpCache.createFilePath(data));
        try {
            if (!mHttpCache.containsKey(data)) {
                if (Utils.hasGingerbread()) {
                    downloadUrlToStream2(data, new FileOutputStream(cacheFile));
                } else {
                    downloadUrlToStream(data, new FileOutputStream(cacheFile));
                }
            } else {
                LogUtil.i("downloadBitmap - found in http cache - " + data);
            }
            if (null != cacheFile && cacheFile.exists()) {
                fileInputStream = new FileInputStream(cacheFile.getAbsolutePath());
                fileDescriptor = fileInputStream.getFD();
            }
        } catch (IOException e) {
            Log.e(TAG, "processBitmap - " + e);
        } catch (IllegalStateException e) {
            Log.e(TAG, "processBitmap - " + e);
        } finally {
            if (fileDescriptor == null && fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                }
            }
        }

        Bitmap bitmap = null;
        int scale = (int) (cacheFile.length() / (1024.0 * 1024.0)) + 1;
        LogUtil.i("cacheFile size:" + cacheFile.length() + "," + scale);
        LogUtil.i("params.targetW:" + params.targetW + "," + params.targetH);
        if (fileDescriptor != null) {
            try {
                bitmap = ImageResizer.decodeSampledBitmapFromDescriptor(fileDescriptor,
                        params.targetW / scale, params.targetH / scale);
            } catch (OutOfMemoryError e) {
                clearMemCache();
                System.gc();
                try {
                    scale = scale + 1;
                    bitmap = ImageResizer.decodeSampledBitmapFromDescriptor(fileDescriptor,
                            params.targetW / scale, params.targetH / scale);
                } catch (OutOfMemoryError e_in) {
                    LogUtil.i("OutOfMemoryError");
                } catch (Exception e_in) {
                    LogUtil.i("Exception:" + e_in);
                }
            } catch (Exception e) {
                LogUtil.i("Exception:" + e);
            }
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
            }
        }
        return bitmap;
    }

    @Override
    protected Bitmap processBitmap(Object data, BitmapParams params) {
        return processBitmap(String.valueOf(data), params);
    }

    private boolean downloadUrlToStream(String urlString, FileOutputStream outputStream) {
        disableConnectionReuseIfNecessary();
        HttpEntity entity = null;
        InputStream inputStream = null;

        try {
            LogUtil.i("downloadUrlToStream start - " + urlString);
            HttpGet httpRequest = new HttpGet(urlString);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpRequest);
            inputStream = response.getEntity().getContent();

            int readBytes = 0;
            int receivedLength = 0;
            byte[] sBuffer = new byte[8192];

            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                outputStream.write(sBuffer, 0, readBytes);
                receivedLength += readBytes;
            }
            outputStream.flush();
            LogUtil.i("receivedLength - " + receivedLength + ", url - " + urlString);
            return true;
        } catch (final IOException e) {
            LogUtil.e("Error in downloadBitmap - " + e);
        } catch (Exception e) {
            LogUtil.e("Error in downloadBitmap - " + e);
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    LogUtil.i("IOException:" + e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    LogUtil.e("Error in downloadBitmap 0 - " + e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (final IOException e) {
                    LogUtil.e("Error in downloadBitmap 1 - " + e);
                }
            }
        }
        return false;
    }

    private boolean downloadUrlToStream2(String urlString, FileOutputStream outputStream) {
        disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            LogUtil.i("downloadUrlToStream start - " + urlString);
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();

            int readBytes = 0;
            int receivedLength = 0;
            byte[] sBuffer = new byte[8192];

            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                outputStream.write(sBuffer, 0, readBytes);
                receivedLength += readBytes;
            }
            outputStream.flush();
            LogUtil.i("receivedLength - " + receivedLength + ", url - " + urlString);
            return true;
        } catch (final IOException e) {
            LogUtil.e("Error in downloadBitmap - " + e);
        } catch (Exception e) {
            LogUtil.e("Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    LogUtil.e("Error in downloadBitmap 0 - " + e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (final IOException e) {
                    LogUtil.e("Error in downloadBitmap 1 - " + e);
                }
            }
        }
        return false;
    }

    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
}
