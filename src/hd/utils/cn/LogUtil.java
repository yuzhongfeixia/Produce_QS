
package hd.utils.cn;

import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class LogUtil {

    private static final boolean DETAIL_ENABLE = true;

    private static String LOGPATH = "";

    public static String TAG = "HDCN";

    public static boolean LOG_MODE = true;

    private static final int LOGFILE_MAX_SIZE = 1 * 1024 * 1024;

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private LogUtil() {
    }

    public static void init(String tag, String logPath, boolean logMode) {
        TAG = tag;
        LOG_MODE = logMode;
        LOGPATH = logPath;
    }

    public static void catchError(final ExitListener exitApp) {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            // 给主线程设置一个处理运行时异常的handler
            public void uncaughtException(Thread thread, final Throwable ex) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                if (LOG_MODE) {
                    StringBuilder sb = new StringBuilder("");
                    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                    String date = formatter.format(curDate);
                    sb.append("\n");
                    sb.append(date + "     ");
                    sb.append("Version code is ");
                    sb.append(Build.VERSION.SDK_INT + "     ");// 设备的Android版本号
                    sb.append("Model is ");
                    sb.append(Build.MODEL + "\n");// 设备型号
                    sb.append("error:" + sw.toString());
                    copyToFile(sb.toString());
                }
                exitApp.exit();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    private static void copyToFile(String msg) {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            checkLogFile();
            File dir = new File(LOGPATH);
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].length() < LOGFILE_MAX_SIZE) {
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(files[i], true);
                        out.write(msg.getBytes());
                        out.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public static void LogMessage(String tag, String message) {
        if (LOG_MODE) {
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                checkLogFile();
                File dir = new File(LOGPATH);
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].length() < LOGFILE_MAX_SIZE) {
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(files[i], true);
                            StringBuilder sb1 = new StringBuilder();
                            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                            String date = formatter.format(curDate);
                            sb1.append("\n");
                            sb1.append(date);
                            sb1.append("     ");
                            sb1.append("D     ");
                            sb1.append(tag);
                            sb1.append("     ");
                            sb1.append(message);
                            out.write(sb1.toString().getBytes());
                            out.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private static void checkLogFile() {
        File dir = new File(LOGPATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File[] files = dir.listFiles();
        int size = files.length;

        if (size == 0) {
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String date = formatter.format(curDate);
            File file = new File(LOGPATH + date + ".log");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (size == 1) {
            if (files[0].length() >= LOGFILE_MAX_SIZE) {
                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                String date = formatter.format(curDate);
                try {
                    File file = new File(LOGPATH + date + ".log");
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (size == 2) {
            if (files[0].lastModified() > files[1].lastModified()) {
                if (files[0].length() >= LOGFILE_MAX_SIZE) {
                    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                    String date = formatter.format(curDate);
                    try {
                        File file = new File(LOGPATH + date + ".log");
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    files[1].delete();
                }
            } else if (files[0].lastModified() < files[1].lastModified()) {
                if (files[1].length() >= LOGFILE_MAX_SIZE) {
                    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                    String date = formatter.format(curDate);
                    try {
                        File file = new File(LOGPATH + date + ".log");
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    files[0].delete();
                }
            }
        }
    }

    private static String buildMsg(String msg) {
        StringBuilder buffer = new StringBuilder();
        if (DETAIL_ENABLE) {
            final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
            buffer.append("[ ");
            buffer.append(Thread.currentThread().getName());
            buffer.append(": ");
            buffer.append(stackTraceElement.getFileName());
            buffer.append(": ");
            buffer.append(stackTraceElement.getLineNumber());
            buffer.append(": ");
            buffer.append(stackTraceElement.getMethodName());
            buffer.append("() ] _____ ");
        }
        buffer.append(msg);
        return buffer.toString();
    }

    public static void v(String msg) {
        if (LOG_MODE) {
            Log.v(TAG, buildMsg(msg));
        }
    }

    public static void d(String msg) {
        if (LOG_MODE) {
            Log.d(TAG, buildMsg(msg));
        }
    }

    public static void i(String msg) {
        if (LOG_MODE) {
            Log.i(TAG, buildMsg(msg));
        }
    }

    public static void w(String msg) {
        if (LOG_MODE) {
            Log.w(TAG, buildMsg(msg));
        }
    }

    public static void w(String msg, Exception e) {
        if (LOG_MODE) {
            Log.w(TAG, buildMsg(msg), e);
        }
    }

    public static void e(String msg) {
        if (LOG_MODE) {
            Log.e(TAG, buildMsg(msg));
        }
    }

    public static void e(String msg, Exception e) {
        if (LOG_MODE) {
            Log.e(TAG, buildMsg(msg), e);
        }
    }

    public static void v(String tag, String msg) {
        if (LOG_MODE) {
            Log.v(tag, buildMsg(msg));
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_MODE) {
            Log.d(tag, buildMsg(msg));
        }
    }

    public static void i(String tag, String msg) {
        if (LOG_MODE) {
            Log.i(tag, buildMsg(msg));
        }
    }

    public static void w(String tag, String msg) {
        if (LOG_MODE) {
            Log.w(tag, buildMsg(msg));
        }
    }

    public static void w(String tag, String msg, Exception e) {
        if (LOG_MODE) {
            Log.w(tag, buildMsg(msg), e);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_MODE) {
            Log.e(tag, buildMsg(msg));
        }
    }

    public static void e(String tag, String msg, Exception e) {
        if (LOG_MODE) {
            Log.e(tag, buildMsg(msg), e);
        }
    }
}
