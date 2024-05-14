
package hd.produce.security.cn;

import hd.source.location.LocationManager;
import hd.utils.cn.Config;
import hd.utils.cn.ExitListener;
import hd.utils.cn.LogUtil;
import hd.utils.cn.StrictModeWrapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;

public class HdCnApp extends Application implements ExitListener {
    
    public static String VERSION_NAME = "";
    public static int VERSION_CODE = 0;

    public static final String TAG = "HdCnApp";

    private static boolean strictModeAvailable;

    private Handler mCurrentHandler;

    private static HdCnApp mHdCnApp;
    
    private List<Activity> activityList = null;
    

	public HdCnApp() {
		activityList = new LinkedList<Activity>();
	}
    public static HdCnApp getInstance(){
    	if(mHdCnApp == null){
    	    mHdCnApp = new HdCnApp();
    	}
    	return mHdCnApp;
    }
    public void addActivity(Activity activity){
    	 if(activityList!=null && activityList.size()>0){
             if(!activityList.contains(activity)){
                 activityList.add(activity);
             }
         }else{
             activityList.add(activity);
         }
    }
    public void exit(){
    	 if(activityList!=null && activityList.size()>0){
             for(Activity activity:activityList){
                 activity.finish();
             }
         }
         System.exit(0);
    }
    public void clearActivity(){
    	 if(activityList!=null && activityList.size()>0){
             for(Activity activity:activityList){
                 activity.finish();
             }
             activityList = new ArrayList<Activity>();
         }


    }

    /**
     * use the StrictModeWrapper to see if we are running on Android 2.3 or
     * higher and StrictMode is available
     */
    static {
        try {
            // StrictModeWrapper.checkAvailable();
            strictModeAvailable = true;
        } catch (Throwable throwable) {
            strictModeAvailable = false;
        }
    }

    @Override
    public void onCreate() {
        getVersionInfo();
        listener = (BDLocationSDKRegister) LocationManager.newInstance();
        listener.initBDSDK(this);

        super.onCreate();
        mHdCnApp = this;
        if (strictModeAvailable) {
            // check if android:debuggable is set to true
            int applicationFlags = getApplicationInfo().flags;
            if ((applicationFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                StrictModeWrapper.init();
                // StrictModeWrapper.enableDefaults();
            }
        }
        String logPath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            logPath = Config.LOG_PATH;
        } else {
            logPath = getApplication().getCacheDir().getAbsolutePath();
        }
        LogUtil.init(TAG, logPath, Config.LogFlag);
        Config.initConfig(mHdCnApp);
//        LogUtil.catchError(this);
        initData();

    }

    private BDLocationSDKRegister listener;

    public interface BDLocationSDKRegister {
        public void initBDSDK(Context context);
    }

    public static HdCnApp getApplication() {
        return mHdCnApp;
    }

    private void initData() {
    }

    /**
    public void exit() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    */

    public Handler getCurrentHandler() {
        return mCurrentHandler;
    }

    public void setCurrentHandler(Handler currentHandler) {
        this.mCurrentHandler = currentHandler;
    }
    
    private void getVersionInfo() {
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            VERSION_NAME = pinfo.versionName;
            VERSION_CODE = pinfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
