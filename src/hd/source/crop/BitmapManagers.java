
package hd.source.crop;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

public class BitmapManagers {
    int pixR = 0;

    int pixG = 0;

    int pixB = 0;

    int pixColor = 0;

    int newR = 0;

    int newG = 0;

    int newB = 0;

    private static BitmapManagers bitmapManager = null;

    private BitmapManagers() {
    }

    public static BitmapManagers getInstance() {
        if (bitmapManager == null) {
            bitmapManager = new BitmapManagers();
        }
        return bitmapManager;
    }

    /**
     * @param bmp
     * @return
     */
    public Bitmap getReminiscenceBitmap(Bitmap bmp) {

        long start = System.currentTimeMillis();
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG,
                        newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Log.d("lzc", "reminiscence used time=" + (end - start));
        return bitmap;
    }

    /**
     * 图片锟今化ｏ拷锟斤拷锟斤拷锟斤拷斯锟戒换锟斤拷
     *
     * @param bmp
     * @return
     */
    public Bitmap getSharpenBitmap(Bitmap bmp) {
        long start = System.currentTimeMillis();
        // 锟斤拷锟斤拷锟斤拷斯锟斤拷锟斤拷
        int[] laplacian = new int[]{
                -1, -1, -1, -1, 9, -1, -1, -1, -1
        };

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int idx = 0;
        float alpha = 0.3F;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + n) * width + k + m];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Log.d("lzc", "sharpen used time=" + (end - start));
        return bitmap;
    }

    public Bitmap getReliefBitmap(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                pixColor = pixels[pos + 1];
                newR = Color.red(pixColor) - pixR + 127;
                newG = Color.green(pixColor) - pixG + 127;
                newB = Color.blue(pixColor) - pixB + 127;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 锟斤拷片效锟斤拷
     *
     * @param bmp
     * @return
     */
    public Bitmap getNegativesBitmap(Bitmap bmp) {
        // RGBA锟斤拷锟斤拷锟街�
        final int MAX_VALUE = 255;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = MAX_VALUE - pixR;
                newG = MAX_VALUE - pixG;
                newB = MAX_VALUE - pixB;

                newR = Math.min(MAX_VALUE, Math.max(0, newR));
                newG = Math.min(MAX_VALUE, Math.max(0, newG));
                newB = Math.min(MAX_VALUE, Math.max(0, newB));

                pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public Bitmap getIlluminationBitmap(Bitmap bmp) {

        final int width = bmp.getWidth();
        final int height = bmp.getHeight();

        Bitmap bitmap = (Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));

        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY);

        final float strength = 150F; // 锟斤拷锟斤拷强锟斤拷 100~150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;

        // bmp.recycle();
        // bmp = null;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {

                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = pixR;
                newG = pixG;
                newB = pixB;

                // 锟斤拷锟姐当前锟姐到锟斤拷锟斤拷锟斤拷锟侥的撅拷锟诫，平锟斤拷锟斤拷锟较碉拷锟斤拷锟斤拷锟斤拷锟街拷锟侥撅拷锟斤拷 v
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
                if (distance < radius * radius) {
                    // 锟斤拷锟秸撅拷锟斤拷锟叫★拷锟斤拷锟斤拷锟斤拷拥墓锟斤拷锟街�
                    int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 锟斤拷锟斤拷效锟斤拷
     *
     * @param bmp
     * @param x   锟斤拷锟斤拷锟斤拷锟侥碉拷锟斤拷bmp锟叫碉拷x锟斤拷锟�
     * @param y   锟斤拷锟斤拷锟斤拷锟侥碉拷锟斤拷bmp锟叫碉拷y锟斤拷锟�
     * @param r   锟斤拷锟轿的半径
     * @return
     */
    public Bitmap getHaloBitmap(Bitmap bmp, int x, int y, float r) {
        long start = System.currentTimeMillis();
        // 锟斤拷斯锟斤拷锟斤拷
        int[] gauss = new int[]{
                1, 2, 1, 2, 4, 2, 1, 2, 1
        };

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int delta = 18; // 值越小图片锟斤拷越锟斤拷锟斤拷越锟斤拷锟斤拷越锟斤拷

        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                int distance = (int) (Math.pow(k - x, 2) + Math.pow(i - y, 2));
                // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷牡锟斤拷锟侥ｏ拷锟斤拷锟�
                if (distance > r * r) {
                    for (int m = -1; m <= 1; m++) {
                        for (int n = -1; n <= 1; n++) {
                            pixColor = pixels[(i + m) * width + k + n];
                            pixR = Color.red(pixColor);
                            pixG = Color.green(pixColor);
                            pixB = Color.blue(pixColor);

                            newR = newR + (int) (pixR * gauss[idx]);
                            newG = newG + (int) (pixG * gauss[idx]);
                            newB = newB + (int) (pixB * gauss[idx]);
                            idx++;
                        }
                    }

                    newR /= delta;
                    newG /= delta;
                    newB /= delta;

                    newR = Math.min(255, Math.max(0, newR));
                    newG = Math.min(255, Math.max(0, newG));
                    newB = Math.min(255, Math.max(0, newB));

                    pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                    newR = 0;
                    newG = 0;
                    newB = 0;
                }
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Log.d("lzc", "Halo used time=" + (end - start));
        return bitmap;
    }

    /**
     * 锟斤拷效锟斤拷
     *
     * @author 锟斤拷瑟Boy
     */
    @SuppressWarnings("null")
    public Bitmap getFeatherBitmap(Bitmap bmp) {

        float Size = 0.5f;

        int pos = 0;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int[] pixels = new int[width * height];
        int ratio = width > height ? height * 32768 / width : width * 32768 / height;

        int cx = width >> 1;
        int cy = height >> 1;
        int max = cx * cx + cy * cy;
        int min = (int) (max * (1 - Size));
        int diff = max - min;

        // int R, G, B;
        for (int i = 0; i < height - 1; i++) {
            for (int k = 0; k < width - 1; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = pixR;
                newG = pixG;
                newB = pixB;

                // Calculate distance to center and adapt aspect ratio
                int dx = cx - i;
                int dy = cy - k;
                if (width > height) {
                    dx = (dx * ratio) >> 15;
                } else {
                    dy = (dy * ratio) >> 15;
                }
                int distSq = dx * dx + dy * dy;
                float v = ((float) distSq / diff) * 255;
                newR = (int) (newR + (v));
                newG = (int) (newG + (v));
                newB = (int) (newB + (v));
                newR = (newR > 255 ? 255 : (newR < 0 ? 0 : newR));
                newG = (newG > 255 ? 255 : (newG < 0 ? 0 : newG));
                newB = (newB > 255 ? 255 : (newB < 0 ? 0 : newB));
                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;

    }

    /** */
    /**
     * 锟斤拷图片锟斤拷锟皆诧拷锟�
     *
     * @param bitmap 锟斤拷要锟睫改碉拷图片
     * @param pixels 圆锟角的伙拷锟斤拷
     * @return 圆锟斤拷图片
     */
    public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap
                .createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    // 锟酵伙拷效锟斤拷锟斤拷
    public Bitmap changeToOil(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int color = 0;
        int radio = 0;

        Random random = new Random();
        int iModel = 4;
        int i = width - iModel;

        while (i > 1) {
            int j = height - iModel;
            while (j > 1) {
                int iPos = random.nextInt(100000000) % iModel;
                color = bitmap.getPixel(i + iPos, j + iPos);
                returnBitmap.setPixel(i, j, color);
                j--;
            }
            i--;
        }

        return returnBitmap;
    }

    // 锟睫猴拷锟叫э拷锟斤拷锟�
    public Bitmap changeToNeon(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int colorArray[] = new int[width * height];
        int r, g, b;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                r = (colorArray[index] >> 16) & 0xff;
                g = (colorArray[index] >> 8) & 0xff;
                b = colorArray[index] & 0xff;
                colorArray[index] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }

        boolean[][] mask = null;
        Paint grayMatrix[] = new Paint[256];

        // Init gray matrix
        int outlineCase = 1;
        double rand = Math.random();
        if (rand > 0.33 && rand < 0.66) {
            outlineCase = 2;
        } else if (rand > 0.66) {
            outlineCase = 3;
        }
        for (int i = 255; i >= 0; i--) {
            Paint p = new Paint();
            int red = i, green = i, blue = i;
            if (i > 127) {
                switch (outlineCase) {
                    case 1:
                        red = 255 - i;
                        break;

                    case 2:
                        green = 255 - i;
                        break;

                    case 3:
                        blue = 255 - i;
                        break;
                }
            }
            p.setColor(Color.rgb(red, green, blue));
            grayMatrix[255 - i] = p;
        }

        int[][] luminance = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (mask != null && !mask[x][y]) {
                    continue;
                }
                luminance[x][y] = (int) luminance(
                        (colorArray[((y * width + x))] & 0x00FF0000) >>> 16, (colorArray[((y
                        * width + x))] & 0x0000FF00) >>> 8,
                        colorArray[((y * width + x))] & 0x000000FF);
            }
        }

        int grayX, grayY;
        int magnitude;
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                if (mask != null && !mask[x][y]) {
                    continue;
                }

                grayX = -luminance[x - 1][y - 1] + luminance[x - 1][y - 1 + 2] - 2
                        * luminance[x - 1 + 1][y - 1] + 2 * luminance[x - 1 + 1][y - 1 + 2]
                        - luminance[x - 1 + 2][y - 1] + luminance[x - 1 + 2][y - 1 + 2];

                grayY = luminance[x - 1][y - 1] + 2 * luminance[x - 1][y - 1 + 1]
                        + luminance[x - 1][y - 1 + 2] - luminance[x - 1 + 2][y - 1] - 2
                        * luminance[x - 1 + 2][y - 1 + 1] - luminance[x - 1 + 2][y - 1 + 2];

                // Magnitudes sum
                magnitude = 255 - truncate(Math.abs(grayX) + Math.abs(grayY));
                Paint grayscaleColor = grayMatrix[magnitude];

                // Apply the color into a new image
                returnBitmap.setPixel(x, y, grayscaleColor.getColor());
            }
        }

        return returnBitmap;
    }

    private int luminance(int r, int g, int b) {
        return (int) ((0.299 * r) + (0.58 * g) + (0.11 * b));
    }

    /**
     * Sets the RGB between 0 and 255
     *
     * @param a
     * @return
     */
    private int truncate(int a) {
        if (a < 0)
            return 0;
        else if (a > 255)
            return 255;
        else
            return a;
    }

    // 锟斤拷锟斤拷效锟斤拷锟斤拷
    public Bitmap changeToTV(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int colorArray[] = new int[width * height];
        int r, g, b;
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                r = (colorArray[index] >> 16) & 0xff;
                g = (colorArray[index] >> 8) & 0xff;
                b = colorArray[index] & 0xff;
                colorArray[index] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y += 3) {

                r = 0;
                g = 0;
                b = 0;

                for (int w = 0; w < 3; w++) {
                    if (y + w < height) {
                        r += ((colorArray[(((y + w) * width + x))] & 0x00FF0000) >>> 16) / 2;
                        // r += (imageIn.getRComponent(x, y+w))/2;
                        g += ((colorArray[(((y + w) * width + x))] & 0x0000FF00) >>> 8) / 2;
                        // g += (imageIn.getGComponent(x, y+w))/2;
                        b += (colorArray[(((y + w) * width + x))] & 0x000000FF) / 2;
                        // b += (imageIn.getBComponent(x, y+w))/2;
                    }
                }
                r = getValidInterval(r);
                g = getValidInterval(g);
                b = getValidInterval(b);

                for (int w = 0; w < 3; w++) {
                    if (y + w < height) {
                        if (w == 0) {
                            colorArray[(((y + w) * width + x))] = (255 << 24) + (r << 16)
                                    + (0 << 8) + 0;
                            returnBitmap.setPixel(x, y + w, colorArray[(((y + w) * width + x))]);
                            // returnBitmap.setPixelColour(x,y+w,r,0,0);
                        } else if (w == 1) {
                            colorArray[(((y + w) * width + x))] = (255 << 24) + (0 << 16)
                                    + (g << 8) + 0;
                            returnBitmap.setPixel(x, y + w, colorArray[(((y + w) * width + x))]);
                            // imageIn.setPixelColour(x,y+w,0,g,0);
                        } else if (w == 2) {
                            colorArray[(((y + w) * width + x))] = (255 << 24) + (0 << 16)
                                    + (0 << 8) + b;
                            returnBitmap.setPixel(x, y + w, colorArray[(((y + w) * width + x))]);
                            // imageIn.setPixelColour(x,y+w,0,0,b);
                        }
                    }
                }
            }
        }

        return returnBitmap;
    }

    /**
     * method to calculate an appropriate interval for flicker lines
     *
     * @param a_value
     * @return
     */
    private static int getValidInterval(int a_value) {
        if (a_value < 0)
            return 0;
        if (a_value > 255)
            return 255;
        return a_value;
    }

    public static Bitmap doHighlightImage(Bitmap src) {

        // create new bitmap, which will be painted and becomes result image

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth() + 96, src.getHeight() + 96,
                Bitmap.Config.ARGB_8888);

        // setup canvas for painting

        Canvas canvas = new Canvas(bmOut);

        // setup default color

        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // create a blur paint for capturing alpha

        Paint ptBlur = new Paint();

        ptBlur.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));

        int[] offsetXY = new int[2];

        // capture alpha into a bitmap

        Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);

        // create a color paint

        Paint ptAlphaColor = new Paint();

        ptAlphaColor.setColor(0xFFFFFFFF);

        // paint color for captured alpha region (bitmap)

        canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);

        // free memory

        bmAlpha.recycle();

        // paint the image source

        canvas.drawBitmap(src, 0, 0, null);

        // return out final image

        return bmOut;

    }

    public static Bitmap doInvert(Bitmap src) {

        // create new bitmap with the same settings as source bitmap

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        // color info

        int A, R, G, B;

        int pixelColor;

        // image size

        int height = src.getHeight();

        int width = src.getWidth();

        // scan through every pixel

        for (int y = 0; y < height; y++)

        {

            for (int x = 0; x < width; x++)

            {

                // get one pixel

                pixelColor = src.getPixel(x, y);

                // saving alpha channel

                A = Color.alpha(pixelColor);

                // inverting byte for each R/G/B channel

                R = 255 - Color.red(pixelColor);

                G = 255 - Color.green(pixelColor);

                B = 255 - Color.blue(pixelColor);

                // set newly-inverted pixel to output image

                bmOut.setPixel(x, y, Color.argb(A, R, G, B));

            }

        }

        // return final bitmap

        return bmOut;

    }

    public static Bitmap doGreyscale(Bitmap src) {

        // constant factors

        final double GS_RED = 0.299;

        final double GS_GREEN = 0.587;

        final double GS_BLUE = 0.114;

        // create output bitmap

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        // pixel information

        int A, R, G, B;

        int pixel;

        // get image size

        int width = src.getWidth();

        int height = src.getHeight();

        // scan through every single pixel

        for (int x = 0; x < width; ++x) {

            for (int y = 0; y < height; ++y) {

                // get one pixel color

                pixel = src.getPixel(x, y);

                // retrieve color of all channels

                A = Color.alpha(pixel);

                R = Color.red(pixel);

                G = Color.green(pixel);

                B = Color.blue(pixel);

                // take conversion up to one single value

                R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);

                // set new pixel color to output bitmap

                bmOut.setPixel(x, y, Color.argb(A, R, G, B));

            }

        }

        // return final image

        return bmOut;

    }

    public static Bitmap doGamma(Bitmap src, double red, double green, double blue) {

        // create output image

        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        // get image size

        int width = src.getWidth();

        int height = src.getHeight();

        // color information

        int A, R, G, B;

        int pixel;

        // constant value curve

        final int MAX_SIZE = 256;

        final double MAX_VALUE_DBL = 255.0;

        final int MAX_VALUE_INT = 255;

        final double REVERSE = 1.0;

        // gamma arrays

        int[] gammaR = new int[MAX_SIZE];

        int[] gammaG = new int[MAX_SIZE];

        int[] gammaB = new int[MAX_SIZE];

        // btn_setting values for every gamma channels

        for (int i = 0; i < MAX_SIZE; ++i) {

            gammaR[i] = (int) Math.min(MAX_VALUE_INT,

                    (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));

            gammaG[i] = (int) Math.min(MAX_VALUE_INT,

                    (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));

            gammaB[i] = (int) Math.min(MAX_VALUE_INT,

                    (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));

        }

        // apply gamma table

        for (int x = 0; x < width; ++x) {

            for (int y = 0; y < height; ++y) {

                // get pixel color

                pixel = src.getPixel(x, y);

                A = Color.alpha(pixel);

                // look up gamma

                R = gammaR[Color.red(pixel)];

                G = gammaG[Color.green(pixel)];

                B = gammaB[Color.blue(pixel)];

                // set new color to output bitmap

                bmOut.setPixel(x, y, Color.argb(A, R, G, B));

            }

        }

        // return final image

        return bmOut;

    }

}
