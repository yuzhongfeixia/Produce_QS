
package hd.source.task;

import android.content.Context;
import android.widget.ImageView;

import hd.produce.security.cn.HdCnApp;
import hd.source.cn.BitmapParams;
import hd.source.cn.ImageCache;
import hd.source.cn.ImageCache.ImageCacheParams;
import hd.source.cn.ImageFetcher;
import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;

public class CacheController {

    private static CacheController mInstance;

    private static final String IMAGE_CACHE_DIR = "images";

    private ImageFetcher mImageFetcher;

    private ImageCache mImageCache;

    private boolean mHasInitCahce = false;

    private CacheController() {
    }

    public static CacheController getInstance() {
        if (null == mInstance) {
            synchronized (CacheController.class) {
                if (null == mInstance) {
                    mInstance = new CacheController();
                }
            }
        }
        return mInstance;
    }

    private synchronized void initImageCache() {
        if (mHasInitCahce) {
            return;
        }
        Context context = HdCnApp.getApplication();
        ImageCacheParams cacheParams = new ImageCacheParams(context, IMAGE_CACHE_DIR);

        // Set memory cache to 25% of mem class
        cacheParams.setMemCacheSizePercent(0.12f);

        // The ImageFetcher takes care of loading images into our ImageView
        // children asynchronously
        mImageFetcher = new ImageFetcher(context);
        mImageCache = new ImageCache(context, cacheParams);
        mImageFetcher.addImageCache(mImageCache);
        mHasInitCahce = true;
        LogUtil.i("initImageCache end");
    }

    // -----------------------------------------------
    public void pasuseWork(boolean pause) {
        if (mHasInitCahce) {
            mImageFetcher.setPauseWork(pause);
        }
    }

    public void onResume() {
        if (mHasInitCahce) {
            mImageFetcher.setExitTasksEarly(false);
        }
    }

    public void onPause() {
        if (mHasInitCahce) {
            mImageFetcher.setExitTasksEarly(true);
            mImageFetcher.flushCache();
        }
    }

    public void closeCache() {
        if (null != mImageFetcher) {
            mImageFetcher.closeCache();
            mImageFetcher = null;
        }
        // if (null != mDownloadCache) {
        // mDownloadCache = null;
        // }
        mHasInitCahce = false;
    }

    public void clearCache() {
        if (null != mImageCache) {
            mImageCache.clearCache();
        }
        if (null != mImageFetcher) {
            mImageFetcher.clearCache();
        }
        // if (null != mDownloadCache) {
        // mDownloadCache.clearCache();
        // }
    }

    public void clearMemCache() {
        if (null != mImageCache) {
            mImageCache.clearMemCache();
        }
    }

    public boolean loadImage(ImageView imageView, String url, int type) {
        if (null == imageView) {
            return false;
        }
        initImageCache();
        if (!Utils.isEmpty(url)) {
            return mImageFetcher.loadImage(url, imageView, new BitmapParams(type));
        }
        return false;
    }

    public boolean loadImage(ImageView imageView, String url, int type, int width, int height) {
        if (null == imageView) {
            return false;
        }
        initImageCache();
        if (!Utils.isEmpty(url)) {
            LogUtil.i(url);
            return mImageFetcher.loadImage(url, imageView, new BitmapParams(type, width, height));
        }
        return false;
    }

    // public boolean loadImage(ImageView imageView, String url, int type,int
    // width,int height) {
    // if (null == imageView) {
    // return false;
    // }
    // initImageCache();
    // if (!Utils.isEmpty(url)) {
    // return mImageFetcher.loadImage(url, imageView, new
    // BitmapParams(type,width,height));
    // }
    // return false;
    // }

    // public boolean loadImage(View mUserBg, String url, int type, ScaleType
    // fitXy) {
    // if (null == mUserBg) {
    // return false;
    // }
    // initImageCache();
    // if (!Utils.isEmpty(url)) {
    // return mImageFetcher.loadImage(url, (ImageView) mUserBg, new
    // BitmapParams(type), fitXy);
    // }
    // return false;
    //
    // }

    public boolean loadImage(ImageView imageView, String url, int type, boolean resize) {
        if (null == imageView) {
            return false;
        }
        initImageCache();
        if (!Utils.isEmpty(url)) {
            return mImageFetcher.loadImage(url, imageView, new BitmapParams(type, resize));
        }
        return false;
    }

    public String getImagePath(String data) {
        if (mImageFetcher != null) {
            String path = mImageFetcher.getFilePath(data);
            if (Utils.isEmpty(path) && null != mImageCache) {
                path = mImageCache.getFilePath(data);
            }
            return path;
        }
        return null;
    }

}
