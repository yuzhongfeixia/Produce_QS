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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import java.lang.ref.WeakReference;

import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;

/**
 * This class wraps up completing some arbitrary long running work when loading
 * a bitmap to an ImageView. It handles things like using a memory and disk
 * cache, running the work in a background thread and setting a placeholder
 * image.
 */
public abstract class ImageWorker {

    private static final int FADE_IN_TIME = 200;

    protected ImageCache mImageCache;

    private Bitmap mLoadingBitmap;

    private boolean mFadeInBitmap = false;

    private boolean mExitTasksEarly = false;

    protected boolean mPauseWork = false;

    private final Object mPauseWorkLock = new Object();

    protected Resources mResources;

    protected Context mContext;

    private static final int MESSAGE_CLEAR = 0;

    private static final int MESSAGE_INIT_DISK_CACHE = 1;

    private static final int MESSAGE_FLUSH = 2;

    private static final int MESSAGE_CLOSE = 3;

    protected ImageWorker(Context context) {
        mResources = context.getResources();
        mContext = context;
    }

    /**
     * Load an image specified by the data parameter into an ImageView (override
     * {@link hd.source.cn.ImageWorker#processBitmap(Object, BitmapParams)} to define the processing
     * logic). A memory and disk cache will be used if an {@link ImageCache} has
     * been set using {@link hd.source.cn.ImageWorker#setImageCache(ImageCache)}. If the
     * image is found in the memory cache, it is set immediately, otherwise an
     * {@link AsyncTask} will be created to asynchronously load the bitmap.
     *
     * @param data      The URL of the image to download.
     * @param imageView The ImageView to bind the downloaded image to.
     */
    public boolean loadImage(Object data, ImageView imageView, BitmapParams params) {
        if (data == null) {
            return false;
        }

        Bitmap bitmap = null;

        if (mImageCache != null) {
            bitmap = mImageCache.getBitmapFromMemCache(String.valueOf(data));
        }
        // LogUtil.i("loadImage:" + imageView + "," + data);
        if (bitmap != null) {
            // Bitmap found in memory cache
            resizeImageView(imageView, params, bitmap);
            // LogUtil.i("setImageBitmap - " + imageView + "," + bitmap);
            imageView.setImageBitmap(bitmap);
            return true;
        } else if (cancelPotentialWork(data, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, params);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources, mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            if (params.needResize) {
                LayoutParams layoutParams = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(layoutParams);
            }

            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
        }
        return false;
    }

    private void resizeImageView(ImageView imageView, BitmapParams params, Bitmap bitmap) {
        if (params.needResize) {
            boolean showAsSquare = true;
            int padding = BitmapParams.PADDIND_LIST_MSG;
            if (params.mType == BitmapParams.LOAD_TYPE_CONTENT_MSG) {
                padding = BitmapParams.PADDIND_CONTENT_MSG;
            } else if (params.mType == BitmapParams.LOAD_TYPE_CONTENT_TRACE) {
                padding = BitmapParams.PADDIND_CONTENT_TRACE;
                showAsSquare = false;
            } else if (params.mType == BitmapParams.LOAD_IMAGE_SHOW) {
                padding = 0;
                showAsSquare = false;
            } else if (params.mType == BitmapParams.LOAD_TYPE_LIST_MSG_EXO) {
                padding = BitmapParams.PADDIND_LIST_MSG_EXO;
            } else if (params.mType == BitmapParams.LOAD_TYPE_LIST_MSG_SJ) {
                padding = BitmapParams.PADDIND_LIST_MSG_SJ;
            }
            int width = Utils.getScreenWidth() - Utils.dip2px(null, padding);
            int height = width;
            if (!showAsSquare) {
                float rate = bitmap.getHeight() / (float) bitmap.getWidth();
                height = (int) (width * rate);
            } else {
                imageView.setScaleType(ScaleType.CENTER_CROP);
            }
            LayoutParams layoutParams = new LayoutParams(width,
                    height);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            imageView.setLayoutParams(layoutParams);
            ((RelativeLayout) imageView.getParent()).getChildAt(0).setLayoutParams(layoutParams);
        }
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is
     * running.
     *
     * @param bitmap
     */
    public void setLoadingImage(Bitmap bitmap) {
        mLoadingBitmap = bitmap;
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is
     * running.
     *
     * @param resId
     */
    public void setLoadingImage(int resId) {
        mLoadingBitmap = BitmapFactory.decodeResource(mResources, resId);
    }

    /**
     * Adds an {@link ImageCache} to this worker in the background (to prevent
     * disk access on UI thread).
     *
     * @param imageCache
     */
    public void addImageCache(ImageCache imageCache) {
        setImageCache(imageCache);
    }

    /**
     * Sets the {@link ImageCache} object to use with this ImageWorker. Usually
     * you will not need to call this directly, instead use
     * {@link hd.source.cn.ImageWorker#addImageCache} which will create and add the
     * {@link ImageCache} object in a background thread (to ensure no disk
     * access on the main/UI thread).
     *
     * @param imageCache
     */
    public void setImageCache(ImageCache imageCache) {
        mImageCache = imageCache;
    }

    /**
     * If set to true, the image will fade-in once it has been loaded by the
     * background thread.
     */
    public void setImageFadeIn(boolean fadeIn) {
        mFadeInBitmap = fadeIn;
    }

    public void setExitTasksEarly(boolean exitTasksEarly) {
        mExitTasksEarly = exitTasksEarly;
    }

    /**
     * Subclasses should override this to define any processing or work that
     * must happen to produce the final bitmap. This will be executed in a
     * background thread and be long running. For example, you could resize a
     * large bitmap here, or pull down an image from the network.
     *
     * @param data The data to identify which image to process, as provided by
     *             {@link hd.source.cn.ImageWorker#loadImage(Object, android.widget.ImageView, BitmapParams)}
     * @return The processed bitmap
     */
    protected abstract Bitmap processBitmap(Object data, BitmapParams params);

    /**
     * Cancels any pending work attached to the provided ImageView.
     *
     * @param imageView
     */
    public static void cancelWork(ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            bitmapWorkerTask.cancel(true);
            if (LogUtil.LOG_MODE) {
                final Object bitmapData = bitmapWorkerTask.data;
                LogUtil.d("cancelWork - cancelled work for " + bitmapData);
            }
        }
    }

    /**
     * Returns true if the current work has been canceled or if there was no
     * work in progress on this image view. Returns false if the work in
     * progress deals with the same data. The work is not stopped in that case.
     */
    public static boolean cancelPotentialWork(Object data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final Object bitmapData = bitmapWorkerTask.data;
            if (bitmapData == null || !bitmapData.equals(data)) {
                bitmapWorkerTask.cancel(true);
                // LogUtil.d("cancelPotentialWork - cancelled work for " +
                // data);
            } else {
                // The same work is already in progress.
                return false;
            }
        }
        return true;
    }

    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active work task (if any) associated with
     * this imageView. null if there is no such task.
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * The actual AsyncTask that will asynchronously process the image.
     */
    private class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {
        private Object data;

        private BitmapParams mParams;

        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView, BitmapParams params) {
            // LogUtil.i("new BitmapWorkerTask" + imageView);
            mParams = params;
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        /**
         * Background processing.
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            // LogUtil.d("doInBackground - starting work");

            data = params[0];
            final String dataString = String.valueOf(data);
            Bitmap bitmap = null;

            // Wait here if work is paused and the task is not cancelled
            synchronized (mPauseWorkLock) {
                while (mPauseWork && !isCancelled()) {
                    try {
                        mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }

            if (mImageCache != null && !isCancelled() && getAttachedImageView() != null
                    && !mExitTasksEarly) {
                bitmap = mImageCache.getBitmapFromDiskCache(dataString);
            }
            // LogUtil.i("getBitmapFromDiskCache:" + bitmap);

            if (bitmap == null && !isCancelled() && getAttachedImageView() != null
                    && !mExitTasksEarly) {
                bitmap = processBitmap(params[0], mParams);
            }
            // LogUtil.i("processBitmap:" + bitmap);

            if (null != bitmap) {
                bitmap = ImageCacheUtils.getRoundedCornerBitmap(bitmap, mParams.round);
            }

            if (bitmap != null && mImageCache != null) {
                mImageCache.addBitmapToCache(dataString, bitmap);
            }

            // LogUtil.d("doInBackground - finished work:" + bitmap);
            return bitmap;
        }

        /**
         * Once the image is processed, associates it to the imageView
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // if cancel was called on this task or the "exit early" flag is set
            // then we're done
            if (isCancelled() || mExitTasksEarly) {
                bitmap = null;
            }

            final ImageView imageView = getAttachedImageView();
            // LogUtil.d("onPostExecute:" + imageView + "," + bitmap);
            if (bitmap != null && imageView != null) {
                // LogUtil.d("onPostExecute - setting bitmap");
                resizeImageView(imageView, mParams, bitmap);
                setImageBitmap(imageView, bitmap);
            }
        }

        @Override
        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
            synchronized (mPauseWorkLock) {
                mPauseWorkLock.notifyAll();
            }
        }

        /**
         * Returns the ImageView associated with this task as long as the
         * ImageView's task still points to this task as well. Returns null
         * otherwise.
         */
        private ImageView getAttachedImageView() {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }

            return null;
        }
    }

    /**
     * A custom Drawable that will be attached to the imageView while the work
     * is in progress. Contains a reference to the actual worker task, so that
     * it can be stopped if a new binding is required, and makes sure that only
     * the last started worker process can bind its result, independently of the
     * finish order.
     */
    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     * Called when the processing is complete and the final bitmap should be set
     * on the ImageView.
     *
     * @param imageView
     * @param bitmap
     */
    private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        // LogUtil.i("setImageBitmap - " + imageView + "," + bitmap);
        if (mFadeInBitmap) {
            final TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                    new ColorDrawable(android.R.color.transparent),
                    new BitmapDrawable(mResources, bitmap)
            });
            imageView.setImageDrawable(td);
            td.startTransition(FADE_IN_TIME);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void setPauseWork(boolean pauseWork) {
        synchronized (mPauseWorkLock) {
            mPauseWork = pauseWork;
            if (!mPauseWork) {
                mPauseWorkLock.notifyAll();
            }
        }
    }

    protected class CacheAsyncTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            switch ((Integer) params[0]) {
                case MESSAGE_CLEAR:
                    clearCacheInternal();
                    break;
                case MESSAGE_INIT_DISK_CACHE:
                    initDiskCacheInternal();
                    break;
                case MESSAGE_FLUSH:
                    flushCacheInternal();
                    break;
                case MESSAGE_CLOSE:
                    break;
            }
            return null;
        }
    }

    protected void initDiskCacheInternal() {
    }

    protected void clearCacheInternal() {
        if (mImageCache != null) {
            mImageCache.clearCache();
        }
    }

    protected void flushCacheInternal() {

    }

    protected void clearMemCache() {
        if (mImageCache != null) {
            mImageCache.clearMemCache();
        }
    }

    public void clearCache() {
        new CacheAsyncTask().execute(MESSAGE_CLEAR);
    }

    public void flushCache() {
        new CacheAsyncTask().execute(MESSAGE_FLUSH);
    }

    public void closeCache() {
        if (mImageCache != null) {
            mImageCache = null;
        }
    }
}
