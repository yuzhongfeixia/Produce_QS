package hd.source.location;

import android.content.Context;
import android.content.Intent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import hd.produce.security.cn.HdCnApp.BDLocationSDKRegister;

public class LocationManager implements BDLocationSDKRegister {
    /**
     * ******************************** 百度地图定位相关参数开始
     * ****************************************
     */
    private static LocationClient locationClient = null;

    private LocationListenner locationListener = new LocationListenner();

    // 百度定位SDK可以返回三种坐标系，分别是bd09, bd09ll和gcj02，其中bd09ll能无偏差地显示在百度地图上。
    // gcj02是测局制定的。
    private static final String COOR_TYPE = "gcj02";

    private static final String BAIDU_LOCAL_SDK_SERVICE_NAME = "com.baidu.location.service_v2.9";

    // 定位sdk提供2种定位模式，定时定位和app主动请求定位。
    // setScanSpan < 1000 则为 app主动请求定位；
    // setScanSpan>=1000,则为定时定位模式（setScanSpan的值就是定时定位的时间间隔））
    // 定时定位模式中，定位sdk会按照app设定的时间定位进行位置更新，定时回调定位结果。
    // 此种定位模式适用于希望获得连续定位结果的情况。
    // 对于单次定位类应用，或者偶尔需要一下位置信息的app，可采用app主动请求定位这种模式。
    private static final int SCAN_SPAN_TIME = 500;

    private static final String PRODUCT_NAME = "com.youku.paike";

    private BaiduSDKLocationDataFeedbackListener dataFeedbackListener;

    private static boolean isLocating;

    private static BDLocation location;

    private Context context;

    /**
     * ********************************** 百度地图定位相关参数结束
     * *************************************
     */
    private static LocationManager locationManager;

    private LocationManager() {

    }

    /**
     * 获取单例对象
     *
     * @return LocationManager
     * @Title: newInstance
     * @date 2013-8-2 下午1:34:03
     */
    public static LocationManager newInstance() {

        if (locationManager != null) {
            return locationManager;
        } else {
            return new LocationManager();
        }

    }

    /**
     * 获取location
     *
     * @return BDLocation
     * @Title: getLocation
     * @date 2013-7-29 下午6:10:04
     */
    public BDLocation getLocation() {
        return location;
    }

    /**
     * 定位状态
     *
     * @return boolean
     * @Title: isLocating
     * @date 2013-7-29 下午6:16:44
     */
    public boolean isLocating() {
        return isLocating;
    }

    /**
     * 注册百度地图定位sdk并设置相关参数
     *
     * @return void
     * @Title: initBaiduLocateSDK
     * @date 2013-7-4 下午8:28:12
     */
    private void initBaiduLocateSDK(Context context) {
        locationClient = new LocationClient(context);
        locationClient.registerLocationListener(locationListener);
        setLocationOption();
    }

    // 设置相关参数
    private void setLocationOption() {
        // 旧版的option已经不能使用了
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true); // 打开gps
//        // 设置坐标类型 ,
//        option.setCoorType(COOR_TYPE);
//        option.setServiceName(BAIDU_LOCAL_SDK_SERVICE_NAME);
//        option.setPoiExtraInfo(true);
//        // 可以返回地理位置信息，例如“北京市海淀区海淀大街8号”,必须设置all并且是wifi定位的情况下才可以拿到详细地理位置信息
//        option.setAddrType("all");
//        option.setScanSpan(SCAN_SPAN_TIME);
//        option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先,不设置，默认是gps优先
//        option.setPoiNumber(10);
//        option.disableCache(true);// true表示禁用缓存定位，false表示启用缓存定位
//        option.setProdName(PRODUCT_NAME);
//        locationClient.setLocOption(option);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType(COOR_TYPE);//返回的定位结果是百度经纬度，默认值gcj02
//        int span=1000;
//        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
//        option.setIsNeedAddress(checkGeoLocation.isChecked());
        locationClient.setLocOption(option);
    }

    public void setDataFeedbackListener(
            BaiduSDKLocationDataFeedbackListener dataFeedbackListener) {

        this.dataFeedbackListener = dataFeedbackListener;
    }

    /**
     * 开始定位
     *
     * @return void
     * @Title: startLocate
     * @date 2013-7-2 下午4:32:26
     */
    public void startLocate() {
        isLocating = true;
        location = null;
        if (locationClient.isStarted()) {// 如果已经打开了定位SDK，则直接request
            locationClient.requestLocation();
        } else {
            locationClient.start();
        }
    }

    /**
     * 想主动结束定位调用这个方法
     *
     * @return void
     * @Title: stopLocate
     * @date 2013-7-2 下午4:31:12
     */
    public void stopLocate() {
        isLocating = false;
        if (locationClient.isStarted()) {
            locationClient.stop();
        }
    }

    /**
     * 定位的监听
     *
     * @author Liuzengchan
     * @Package com.youku.paike
     * @ClassName: LocationListenner
     * @mail liuzengchan@youku.com
     * @date 2013-7-4 下午8:34:34
     */
    class LocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            isLocating = false;

            if (location == null) {

                locationClient.stop();// 拿到数据之后主动关闭定位SDK
                return;
            }

            if (location.getLatitude() > 0 || location.getLongitude() > 0) {

                if (dataFeedbackListener != null) {
                    dataFeedbackListener.onReceiver(location);
                }
                LocationManager.this.location = location;

                Intent intent = new Intent("location");
                intent.putExtra("key", isLocating);
                context.sendBroadcast(intent);

                locationClient.stop();// 拿到数据之后主动关闭定位SDK
            }

            /**
             * else { locationClient.requestLocation();//再次请求 }
             */

        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;

            }
        }
    }

    @Override
    public void initBDSDK(Context context) {
        // TODO Auto-generated method stub
        this.context = context;
        initBaiduLocateSDK(context);

    }

}
