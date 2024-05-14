
package hd.utils.cn;

import android.annotation.SuppressLint;
import android.os.StrictMode;

@SuppressLint("NewApi")
public class StrictModeWrapper {
    static {
        try {
            Class.forName("android.os.StrictMode", true, Thread.currentThread()
                    .getContextClassLoader());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void checkAvailable() {
    }

    public static void enableDefaults() {
        StrictMode.enableDefaults();
    }

    public static void init() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                .detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                .penaltyLog().penaltyDeath().build());
    }
}
