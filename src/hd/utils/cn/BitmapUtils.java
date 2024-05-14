package hd.utils.cn;

import hd.produce.security.cn.HdCnApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class BitmapUtils {

    /**
     * 取得Bitmap
     * 
     * @param imgPath
     * @return
     */
    public static Bitmap getBitmap(String imgPath) {

        Bitmap bitmap = null;
        InputStream is = null;
        File file = null;
        try {
            file = new File(imgPath);
            if (file.exists()) {
                is = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            LogUtil.e("Error in BitmapUtils.getBitmap(file)", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                    LogUtil.e("Error in BitmapUtils.getBitmap(file)", ignored);
                }
            }
        }
        return bitmap;
    }

    /**
     * 取得Bitmap
     * 
     * @param uri
     * @return
     */
    public static Bitmap getBitmap(Uri uri) {

        Bitmap bitmap = null;
        InputStream is = null;
        try {

            is = getInputStream(uri);

            bitmap = BitmapFactory.decodeStream(is);

        } catch (IOException e) {
            LogUtil.e("Error in BitmapUtils.getBitmap(url)", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                    LogUtil.e("Error in BitmapUtils.getBitmap(url)", ignored);
                }
            }
        }
        return bitmap;

    }

    /**
     * 取得Url对应的流
     * 
     * @param mUri
     * @return
     * @throws IOException
     */
    private static InputStream getInputStream(Uri mUri) throws IOException {

        try {
            if (mUri.getScheme().equals("file")) {
                return new java.io.FileInputStream(mUri.getPath());
            } else {
                return HdCnApp.getApplication().getApplicationContext().getContentResolver().openInputStream(mUri);
            }
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 采用Bitmap处理图片 防止OOM
     * 
     * @param imagePath
     * @return
     */
    public static Bitmap decodeFile(String imagePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, opts);
        // 默认分辨率
        opts.inSampleSize = BitmapUtils.computeSampleSize(opts, -1, 400 * 400);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, opts);
    }

    /**
     * 重新计算大小
     * 
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
