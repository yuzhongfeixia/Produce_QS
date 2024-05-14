package hd.utils.cn;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * 检测网络是否可用
 * 
 * @author xudl(Wisea)
 * 
 */
public class NetworkControl {

    public static NetType getNetType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return null;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return null;
        }
        String type = info.getTypeName();
        if (type.equalsIgnoreCase("wifi")) {
            return null;
        } else if (type.equalsIgnoreCase("mobile")) {// GPRS
            Uri uri = Uri.parse("content://telephony/carriers/preferapn");
            Cursor cr = context.getContentResolver().query(uri, null, null, null, null);
            while (cr != null && cr.moveToNext()) {
                NetType netType = new NetType();
                if (cr.getString(cr.getColumnIndex("proxy")) != null && !cr.getString(cr.getColumnIndex("proxy")).equals("")) {
                    netType.setProxy(cr.getString(cr.getColumnIndex("proxy")));
                    netType.setPort(cr.getInt(cr.getColumnIndex("port")));
                    netType.setWap(true);
                } else {
                    netType.setWap(false);
                }
                return netType;
            }
        }
        return null;
    }

    /**
     * 检查网络是否连接
     * 
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo == null) {
                return false;
            }
            return netInfo.isAvailable();
        }
        return false;
    }

    public static class NetType {
        private String proxy = "";
        private int port = 0;
        private boolean isWap = false;

        public String getProxy() {
            return proxy;
        }

        public void setProxy(String proxy) {
            this.proxy = proxy;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public boolean isWap() {
            return isWap;
        }

        public void setWap(boolean isWap) {
            this.isWap = isWap;
        }

    }
}
