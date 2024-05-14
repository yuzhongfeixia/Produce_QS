
package hd.source.cn;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;

public class ImageCacheUtils {
    // 获得圆角图片的方法
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (roundPx >= 1.0f || roundPx < 0.001f) {
            return bitmap;
        }
        Bitmap output = null;
        try {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            roundPx = bitmap.getWidth() * roundPx;
            LogUtil.w("roundPx:" + roundPx);

            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (OutOfMemoryError e) {
            LogUtil.e("OutOfMemoryError:" + e);
        } catch (Exception e) {
            LogUtil.e("Exception:" + e);
        } finally {
            if (null != output) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
            } else {
                return bitmap;
            }
        }
        return output;
    }

    public static final int MAX_WIDTH = 1500;

    public static class ResizeParams {
        public String path;

        public int sampleSize;

        public boolean hasNew;

        public Rect resizeRect(Rect rect) {
            if (null != rect) {
                if (sampleSize == 1) {
                    return rect;
                }
                return new Rect(rect.left / sampleSize, rect.top / sampleSize, rect.right
                        / sampleSize, rect.bottom / sampleSize);
            }
            return null;
        }
    }

    public static ResizeParams resizeImageSize(String picfile, String newfile) {
        if (!Utils.isExist(picfile)) {
            LogUtil.i("file not exist:" + picfile);
            return null;
        }
        FileInputStream input = null;
        ResizeParams params = new ResizeParams();
        params.path = picfile;
        params.sampleSize = 1;
        try {
            input = new FileInputStream(picfile);
            final BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, opts);
            closeStream(input);

            if (opts.outHeight <= 0 || opts.outWidth <= 0) {
                LogUtil.i("file not valid bitmap file!");
                return null;
            }

            int rate = 0;
            for (int i = 0; ; i++) {
                if ((opts.outWidth >> i <= MAX_WIDTH) && (opts.outHeight >> i <= MAX_WIDTH)) {
                    rate = i;
                    break;
                }
            }
            opts.inSampleSize = (int) Math.pow(2, rate);
            opts.inJustDecodeBounds = false;
            LogUtil.i("opts.outWidth:" + opts.outWidth + ",opts.outHeight:" + opts.outHeight
                    + ",opts.inSampleSize:" + opts.inSampleSize);

            if (opts.inSampleSize == 1) {
                params.path = picfile;
                params.sampleSize = 1;
                return params;
            }

            Bitmap bmp = null;
            final int MAX_TRIAL = 3;
            for (int i = 0; i < MAX_TRIAL; ++i) {
                try {
                    input = new FileInputStream(picfile);
                    bmp = BitmapFactory.decodeStream(input, null, opts);
                    closeStream(input);
                    break;
                } catch (OutOfMemoryError e) {
                    LogUtil.e("OutOfMemoryError:" + opts.inSampleSize);
                    opts.inSampleSize *= 2;
                }
            }

            if (bmp == null) {
                LogUtil.e("Bitmap decode error!");
                return params;
            }
            Utils.makesureFileExist(newfile);
            final FileOutputStream output = new FileOutputStream(newfile);
            bmp.compress(ImageCache.DEFAULT_COMPRESS_FORMAT, ImageCache.DEFAULT_COMPRESS_QUALITY,
                    output);
            params.hasNew = true;
            params.path = newfile;
            params.sampleSize = opts.inSampleSize;
            closeStream(output);
            bmp.recycle();
            return params;
        } catch (Exception e) {
            LogUtil.e("Exception:" + e.getMessage());
        } finally {
            closeStream(input);
        }
        return null;
    }

    public static boolean closeStream(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
                return true;
            }
        } catch (IOException e) {
            LogUtil.e(e + "");
        }
        return false;
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use
        // external cache dir
        // otherwise use internal cache dir
        try {
            boolean hasSd = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
            String cachePath = null;
            if (hasSd || !isExternalStorageRemovable()) {
                cachePath = getExternalCacheDir(context).getPath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
            return new File(cachePath + File.separator + uniqueName);
        } catch (Exception e) {
            LogUtil.e("getDiskCacheDir:" + e);
            return null;
        }
    }

    public static boolean sdCardValid() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !isExternalStorageRemovable()) {
            return true;
        }
        return false;
    }

    /**
     * A hashing method that changes a string (like a URL) into a hash suitable
     * for using as a disk filename.
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * Get the size in bytes of a bitmap.
     *
     * @param bitmap
     * @return size in bytes
     */
    @TargetApi(12)
    public static int getBitmapSize(Bitmap bitmap) {
        if (Utils.hasHoneycombMR1()) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    @TargetApi(9)
    public static boolean isExternalStorageRemovable() {
        if (Utils.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        File file = null;
        if (Utils.hasFroyo()) {
            file = context.getExternalCacheDir();
        }
        if (null != file) {
            return file;
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "android/data/" + context.getPackageName() + "/cache";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @TargetApi(9)
    public static long getUsableSpace(File path) {
        if (Utils.hasGingerbread()) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    private static final String TAG = "IMAGE_CACHE";

    /**
     * Find and return an existing ImageCache stored in a {@link hd.source.cn.ImageCacheUtils.RetainFragment}
     * , if not found a new one is created using the supplied params and saved
     * to a {@link hd.source.cn.ImageCacheUtils.RetainFragment}.
     *
     * @param fragmentManager The fragment manager to use when dealing with the
     *                        retained fragment.
     * @param cacheParams     The cache parameters to use if creating the ImageCache
     * @return An existing retained ImageCache object or a new one if one did
     * not exist
     */
    public static ImageCache findOrCreateCache(FragmentManager fragmentManager,
                                               ImageCache.ImageCacheParams cacheParams) {

        // Search for, or create an instance of the non-UI RetainFragment
        final RetainFragment mRetainFragment = ImageCacheUtils
                .findOrCreateRetainFragment(fragmentManager);

        // See if we already have an ImageCache stored in RetainFragment
        ImageCache imageCache = (ImageCache) mRetainFragment.getObject();

        // No existing ImageCache, create one and store it in RetainFragment
        if (imageCache == null) {
            imageCache = new ImageCache(mRetainFragment.getActivity(), cacheParams);
            mRetainFragment.setObject(imageCache);
        }

        return imageCache;
    }

    /**
     * Locate an existing instance of this Fragment or if not found, create and
     * add it using FragmentManager.
     *
     * @param fm The FragmentManager manager to use.
     * @return The existing instance of the Fragment or the new instance if just
     * created.
     */
    public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
        // Check to see if we have retained the worker fragment.
        RetainFragment mRetainFragment = (RetainFragment) fm.findFragmentByTag(TAG);

        // If not retained (or first time running), we need to create and add
        // it.
        if (mRetainFragment == null) {
            mRetainFragment = new RetainFragment();
            fm.beginTransaction().add(mRetainFragment, TAG).commitAllowingStateLoss();
        }

        return mRetainFragment;
    }

    /**
     * A simple non-UI Fragment that stores a single Object and is retained over
     * configuration changes. It will be used to retain the ImageCache object.
     */
    public static class RetainFragment extends Fragment {
        private Object mObject;

        /**
         * Empty constructor as per the Fragment documentation
         */
        public RetainFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure this Fragment is retained over a configuration change
            setRetainInstance(true);
        }

        /**
         * Store a single object in this Fragment.
         *
         * @param object The object to store
         */
        public void setObject(Object object) {
            mObject = object;
        }

        /**
         * Get the stored object.
         *
         * @return The stored object
         */
        public Object getObject() {
            return mObject;
        }
    }
}
