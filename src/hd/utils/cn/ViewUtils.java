package hd.utils.cn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import hd.source.cn.ImageCache;
import hd.source.cn.ImageCacheUtils;
import hd.source.cn.ImageResizer;

public class ViewUtils {

    // -------------startActivityForResult request code ----------------
    public static final int CAPTURE_GALLERY = 10;

    public static final int CAPTURE_PIC = 11;

    public static final int CROP_GALLERY = 12;

    public static final int CROP_CAPTURE = 13;

    public static final int REQUEST_PERSON = 14;

    public static final int REQ_MSG_CONTENT = 15;

    public static final int GET_GPS = 16;

    public static final int REQ_GUIDE = 17;

    public static final int REQUEST_EDIT = 18;

    public static final int START_UPLOAD = 19;

    public static final int REQ_NEWS_CONTENT = 20;

    public static final int REQ_IMAGE_LIST = 21;

    public static final int CROP_IMAGE = 30;

    public static final int LIVE_WATCH = 31;

    public static final int REQ_PERSON_SHIP = 32;

    public static final int REQ_IMG_GALLERY = 33;

    public static final int REQ_CHAT_DETAIL = 34;

    public static final int REQ_SEARCH_FANS = 35;

    public static final int REQ_TOPIC_LIST = 36;

    // --------------------------------------
    public static void setTextView(View view, String content) {
        if (null != view) {
            if (view instanceof TextView) {
                TextView tView = (TextView) view;
                if (null == content) {
                    tView.setText("");
                } else {
                    tView.setText(content.trim());
                }
            } else if (view instanceof EditText) {
                EditText tView = (EditText) view;
                if (null == content) {
                    tView.setText("");
                } else {
                    tView.setText(content.trim());
                }
            }
        }
    }

    public static void setBackgroud(View view, int res) {
        if (null != view) {
            view.setBackgroundResource(res);
        }
    }

    public static int parseInt(Integer src) {
        if (null == src) {
            return 0;
        }
        return (int) src;
    }

    public static boolean parseBool(Boolean src) {
        if (null == src) {
            return false;
        }
        return src;
    }

    public static long parseLong(Long src) {
        if (null == src) {
            return 0;
        }
        return src;
    }

    public static void setTextView(View view, Integer content) {
        if (null != view) {
            if (view instanceof TextView) {
                TextView tView = (TextView) view;
                if (null == content) {
                    tView.setText("0");
                } else {
                    tView.setText(content.toString());
                }
            } else if (view instanceof EditText) {
                EditText tView = (EditText) view;
                if (null == content) {
                    tView.setText("0");
                } else {
                    tView.setText(content.toString());
                }
            }
        }
    }

    public static void setClickListener(View view, OnClickListener listener) {
        if (null != view) {
            view.setOnClickListener(listener);
        }
    }

    public static void setViewState(View view, int show_state) {
        if (null != view) {
            if (show_state == View.VISIBLE || show_state == View.GONE
                    || show_state == View.INVISIBLE) {
                view.setVisibility(show_state);
            }
        }
    }

    public static void shakeInHorizon(View view, float range) {
        TranslateAnimation animation = new TranslateAnimation(-range, 0, 0, 0);
        animation.setDuration(100);
        animation.setRepeatCount(2);
        animation.setRepeatMode(Animation.REVERSE);
        view.setAnimation(animation);
        animation.start();
    }

    public static void TranslateHorizonR(View view, float range) {
        TranslateAnimation animation = new TranslateAnimation(0, range, 0, 0);
        animation.setDuration(100);
        view.setAnimation(animation);
        animation.start();
    }

    public static void TranslateHorizonL(View view, float range) {
        TranslateAnimation animation = new TranslateAnimation(0, -range, 0, 0);
        animation.setDuration(100);
        view.setAnimation(animation);
        animation.start();
    }

    public static void shakeInVertical(View view, float range) {
        TranslateAnimation animation = new TranslateAnimation(0, 0, -range, 0);
        animation.setDuration(100);
        animation.setRepeatCount(2);
        animation.setRepeatMode(Animation.REVERSE);
        view.setAnimation(animation);
        animation.start();
    }

    public static void startAnimation(View view, Animation animation) {
        if (null != animation && null != view) {
            view.startAnimation(animation);
        }
    }

    public static Bitmap getBitmapByResId(Context context, int id) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPurgeable = true;
            opt.inInputShareable = true;

            InputStream is = context.getResources().openRawResource(id);
            bitmap = BitmapFactory.decodeStream(is, null, opt);
        } catch (OutOfMemoryError e) {
            LogUtil.e("getBitmapByResId OutOfMemoryError.");
        } catch (Exception e) {
            LogUtil.e("getBitmapByResId Exception:" + e.getMessage());
        }
        return bitmap;
    }

    private static int MAX_SIZE = 1080;

    public static boolean checkValidPic(String path) {
        if (path.toLowerCase().endsWith(".bmp") || path.toLowerCase().endsWith(".gif")) {
            return false;
        }
        return true;
    }

    public static String convertToValidPic(String path) {
        MAX_SIZE = Utils.getScreenWidth() * 2;
        LogUtil.i("convertToValidPic path:" + path);
        LogUtil.i("MAX_SIZE:" + MAX_SIZE);
        if (Utils.isFile(path)) {
            if (path.toLowerCase().endsWith(".bmp")) {
                Bitmap bitmap = null;
                try {
                    bitmap = ImageResizer.decodeSampledBitmapFromFile(path, MAX_SIZE, MAX_SIZE);
                } catch (OutOfMemoryError e) {
                    return path;
                }
                String newPath = path.substring(0, path.length() - 4) + "_temp.jpg";
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(newPath);
                    if (outputStream != null) {
                        bitmap.compress(ImageCache.DEFAULT_COMPRESS_FORMAT,
                                ImageCache.DEFAULT_COMPRESS_QUALITY, outputStream);
                    }
                } catch (IOException ex) {
                    LogUtil.e("Cannot open file: " + newPath + ", error:" + ex.getMessage());
                } finally {
                    if (null != outputStream) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            LogUtil.e("IOException: " + e.getMessage());
                        }
                        outputStream = null;
                    }
                }
                return newPath;
            } else {
                return path;
            }
        }
        return null;
    }

    public static void destroyContentView(View v) {
        unbindDrawables(v);
        System.gc();
    }

    private static void unbindDrawables(View view) {
        try {
            if (view instanceof AdapterView) {
                ((AdapterView) view).setOnItemClickListener(null);
            } else {
                view.setOnClickListener(null);
            }
        } catch (Throwable mayHappen) {
            LogUtil.e(mayHappen.getMessage());
        }
        try {
            view.setOnCreateContextMenuListener(null);
        } catch (Throwable mayHappen) {
            LogUtil.e(mayHappen.getMessage());
        }
        try {
            view.setOnFocusChangeListener(null);
        } catch (Throwable mayHappen) {
            LogUtil.e(mayHappen.getMessage());
        }
        try {
            view.setOnKeyListener(null);
        } catch (Throwable mayHappen) {
            LogUtil.e(mayHappen.getMessage());
        }
        try {
            view.setOnLongClickListener(null);
        } catch (Throwable mayHappen) {
        }
        try {
            // set background to null
            if (view != null) {
                Drawable d = view.getBackground();
                if (d != null)
                    d.setCallback(null);
                if (view instanceof ImageView) {
                    ImageView imageView = (ImageView) view;
                    d = imageView.getDrawable();
                    if (d != null)
                        d.setCallback(null);
                    imageView.setImageDrawable(null);
                    imageView.setBackgroundDrawable(null);
                }
                // destroy webview
                if (view instanceof WebView) {
                    ((WebView) view).destroyDrawingCache();
                    ((WebView) view).destroy();
                }
                if (view instanceof ViewGroup) {
                    unbindViewGroupReferences((ViewGroup) view);
                }
            }
        } catch (Throwable mayHappen) {
            LogUtil.e(mayHappen.getMessage());
        }
    }

    private static void unbindViewGroupReferences(ViewGroup viewGroup) {
        int nrOfChildren = viewGroup.getChildCount();
        for (int i = 0; i < nrOfChildren; i++) {
            View view = viewGroup.getChildAt(i);
            unbindDrawables(view);
            if (view instanceof ViewGroup) {
                unbindViewGroupReferences((ViewGroup) view);
            }
        }
        try {
            if (viewGroup instanceof ListView) {
                ((ListView) viewGroup).removeAllViewsInLayout();
            } else if (viewGroup instanceof GridView) {
                ((GridView) viewGroup).removeAllViewsInLayout();
            } else {
                viewGroup.removeAllViews();
            }
        } catch (Throwable mayHappen) {
            // AdapterViews, ListViews and potentially other ViewGroups don't
            // support the removeAllViews operation
            LogUtil.e(mayHappen.getMessage());
        }
    }

    public static boolean checkMain(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfo = manager.getRunningTasks(10);
        if (taskInfo.size() > 0) {
            LogUtil.d("baseActivity: " + taskInfo.get(0).baseActivity.getClassName());
            LogUtil.d("num: " + taskInfo.get(0).numActivities);
            String homePath = context.getPackageName() + ".activity.HomeActivity";
            if (!homePath.equals(taskInfo.get(0).baseActivity.getClassName())
                    && 1 == taskInfo.get(0).numActivities) {
                return false;
            }
        } else {
            LogUtil.i("RunningTaskInfo:taskInfo.size()==0");
        }
        return true;
    }

    public static boolean checkCpuInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("abi: ").append(Build.CPU_ABI).append("\n");
        if (new File("/proc/cpuinfo").exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                String aLine;
                while ((aLine = br.readLine()) != null) {
                    sb.append(aLine + "\n");
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                LogUtil.i(e.getMessage());
            }
        }
        String cpuInfo = sb.toString();
        if (!Utils.isEmpty(cpuInfo) && cpuInfo.contains("ARMv7") && cpuInfo.contains("neon")) {
            return true;
        }
        return false;
    }

    public static String generBirth(int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear + 1;
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        if (monthOfYear < 10) {
            builder.append("-0");
        } else {
            builder.append("-");
        }
        builder.append(monthOfYear);
        if (dayOfMonth < 10) {
            builder.append("-0");
        } else {
            builder.append("-");
        }
        builder.append(dayOfMonth);
        return builder.toString();
    }

    private static long Min_Size = 1024 * 1024 * 2;

    public static boolean checkHasSpace() {
        if (ImageCacheUtils.sdCardValid()) {
            if (ImageCacheUtils.getUsableSpace(Environment.getExternalStorageDirectory()) > Min_Size) {
                return true;
            }
        }
        return false;
    }

    public static String getPathFromUri(ContentResolver resolver, Uri uri) {
        if (uri != null && uri.getScheme() != null) {
            if (uri.getScheme().equals("content")) {
                Cursor actualimagecursor = null;
                try {
                    String[] proj = {
                            MediaStore.Images.Media.DATA
                    };
                    actualimagecursor = resolver.query(uri, proj, null, null, null);

                    int actual_image_column_index = actualimagecursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    if (actualimagecursor.moveToFirst()) {
                        return actualimagecursor.getString(actual_image_column_index);
                    }
                } catch (Exception e) {
                    LogUtil.d(e.toString());
                } finally {
                    if (null != actualimagecursor) {
                        actualimagecursor.close();
                    }
                }
            } else if (uri.getScheme().equals("file")) {
                return uri.getPath();
            }
            return uri.getPath();
        }
        return null;
    }
}
