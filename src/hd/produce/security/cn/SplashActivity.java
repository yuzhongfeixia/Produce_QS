
package hd.produce.security.cn;

import com.google.gson.Gson;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;

import hd.produce.security.cn.data.MonitoringPadEntity;
import hd.produce.security.cn.db.DatabaseManager;
import hd.produce.security.cn.db.SQLManager;
import hd.source.task.DataManager;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.source.task.WebServiceUtil;
import hd.utils.cn.Constants;
import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;

/**
 * @author Administrator 启动页
 */
public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";

    private static final int MSG_INTENT_HOME = 1;

    private static final int MSG_INTENT_LOGIN = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INTENT_HOME:
                    sendIntent(ProjectListActivity.class);
                    break;
                case MSG_INTENT_LOGIN:
                    sendIntent(LoginActivity.class);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        HdCnApp.getApplication().addActivity(this);
//        DatabaseManager.getInstance().deleteDatabase();
//        TaskHelper.getAllMonitoringSite(this, new ITaskListener() {
//            @Override
//            public void onTaskFinish(int result, int request, Object entity) {
//                try {
//                    WebServiceUtil.getAllMonitoringSite((SoapObject)entity);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
        // 初始化数据库
        SQLManager.getInstance();
//        SQLManager.getInstance().execSQL("ALTER TABLE TB_SAMPLING_INFO ADD COLUMN samplingTime TIMESTAMP");
//        SQLManager.getInstance().execSQL("ALTER TABLE TB_SAMPLING_INFO ADD COLUMN templete NVARCHAR(1)");
        if (Utils.getDefaultFalsePrefrence(SplashActivity.this, Constants.SP_KEY_AUTHORIZAD)) {
            String jsonStr = Utils.getPreference(this, Constants.SP_KEY_AUTHINFO);
            Gson gson = new Gson();
            DataManager.getInstance().setMonitoringPadEntity(
                    gson.fromJson(jsonStr, MonitoringPadEntity.class));
            mHandler.sendEmptyMessageDelayed(MSG_INTENT_HOME, 800);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_INTENT_LOGIN, 1000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void sendIntent(Class<?> cls) {
        LogUtil.i(TAG, "sendIntent to:" + cls.getName());
        Intent mIntent = new Intent();
        mIntent.setClass(SplashActivity.this, cls);
        startActivity(mIntent);
        finish();
    }
}
