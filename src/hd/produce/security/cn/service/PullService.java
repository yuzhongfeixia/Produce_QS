
package hd.produce.security.cn.service;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import hd.produce.security.cn.R;
import hd.produce.security.cn.SplashActivity;
import hd.produce.security.cn.data.PadMQ;
import hd.source.task.DataManager;
import hd.source.task.WebServiceUtil;
import hd.utils.cn.Constants;
import hd.utils.cn.LogUtil;

import java.io.EOFException;

public class PullService extends Service {
    private boolean isServiceAlive = true;

    // 消息推送间隔时间，秒
    private final int pullGapSecond = 300;

    private NotificationManager nm;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        nm = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
//        loopPull();

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isServiceAlive = false;
    }

    private void loopPull() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isServiceAlive) {
                    try {
                        SoapObject request = WebServiceUtil.receiverMQRequest(DataManager
                                .getInstance().getMonitoringPadEntity().getId());
                        SoapObject entity = WebServiceUtil.callWebService(request);
                        if (entity != null) {
                            String jsonStr = WebServiceUtil.receiverMQ((SoapObject) entity);
                            LogUtil.i("TEST", jsonStr);
                            if (!TextUtils.isEmpty(jsonStr)) {
                                if (TextUtils.equals("anyType{}", jsonStr)) {
                                    break;
                                }
                                try {
                                    JSONObject jsonobj = new JSONObject(jsonStr);
                                    if (jsonobj != null
                                            && (jsonobj.has("padforwardInfoS")
                                                    || jsonobj.has("PadTCInfoS") || jsonobj
                                                        .has("padforwardInfoSuccess"))) {
                                        Gson gson = new Gson();
                                        PadMQ padMQ = gson.fromJson(jsonStr, PadMQ.class);
                                        DataManager.getInstance().setPadMQ(padMQ);
                                        sendBroad();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (EOFException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000 * pullGapSecond);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    private void sendBroad() {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_RECEIVE_FOR_SAMPLE);
        this.sendBroadcast(intent);
    }

    private void showNotification(String tickerText, Bundle bundle) {
        Notification n = new Notification();
        n.icon = R.drawable.ic_launcher;
        n.when = System.currentTimeMillis();
        n.tickerText = tickerText;
        n.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        // n.vibrate = ;

        n.flags = Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent(this, SplashActivity.class);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, Constants.NOTIFACTION_KEY_ID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        n.setLatestEventInfo(this, "待接收消息", tickerText, contentIntent);

        nm.notify(Constants.NOTIFACTION_KEY_ID, n);
    }

}
