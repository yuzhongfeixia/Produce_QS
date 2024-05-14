
package hd.utils.cn;

import android.content.Context;
import android.os.Environment;

public class Config {

    public Config() {
        // TODO Auto-generated constructor stub
    }

    public static boolean LogFlag = true;

    public static final String LOG_PATH = Environment.getExternalStorageDirectory() + "/HDCN/Log/";

    /**
     * call this method first when app launch
     *
     * @param context
     */
    public static void initConfig(Context context) {

    }

}
