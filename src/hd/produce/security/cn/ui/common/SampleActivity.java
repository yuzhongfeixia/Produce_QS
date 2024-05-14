package hd.produce.security.cn.ui.common;

import hd.produce.security.cn.BreadListAct;
import hd.produce.security.cn.CropActivity;
import hd.produce.security.cn.HdCnApp;
import hd.produce.security.cn.MonitoredActivity;
import hd.produce.security.cn.MonitoringSiteAct;
import hd.produce.security.cn.R;
import hd.produce.security.cn.adapter.SelectAdapter;
import hd.produce.security.cn.data.LivestockEntity;
import hd.produce.security.cn.data.MonitoringTaskEntity;
import hd.produce.security.cn.data.RoutinemonitoringEntity;
import hd.produce.security.cn.data.SampleInfoEntity;
import hd.produce.security.cn.entity.Sprinner;
import hd.produce.security.cn.entity.TbMonitoringProject;
import hd.source.location.LocationManager;
import hd.source.task.DataManager;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.source.task.WebServiceUtil;
import hd.utils.cn.Constants;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.google.zxing.client.android.CaptureActivity;

public class SampleActivity extends MonitoredActivity {
    /** Activity返回页面:二维码扫描 */
    public static final int REQ_BARCODE_CODE = 3301;

    /** Activity返回页面:拍照 */
    public static final int REQ_CAPTURE_CODE = 3302;

    /** Activity返回页面:截图 */
    public static final int REQ_CROP_CODE = 3303;

    /** Activity返回页面:受检单位选择 */
    public static final int REQ_MONITORING_SITE_CODE = 3304;

    /** Activity返回页面:农产品选择 */
    public static final int REQ_BREED_CODE = 3305;

    // 选中项目
    public TbMonitoringProject mMonitoringProjectEntity;
    // 选中任务
    public MonitoringTaskEntity mMonitoringTaskEntity;
    // 抽样单主表
    public SampleInfoEntity sampleInfoEntity = new SampleInfoEntity();
    // 样品数据
    public List<Sprinner> breedList = new ArrayList<Sprinner>();
    // 监测环节数据
    public List<Sprinner> monitorLinkList = new ArrayList<Sprinner>();
    // 地市数据
    public List<Sprinner> cityList = new ArrayList<Sprinner>();
    // 区县数据
    public List<Sprinner> countryList = new ArrayList<Sprinner>();
    // 经度
    public double logitude;
    // 纬度
    public double latitude;
    // 样品图片Url
    public Uri photoUri;

    // 是否为添加
    public boolean isAdd = false;
    // 是否为未完成
    public boolean isDoing = false;
    // 是否为提交抽样单
    public boolean isCommit = false;
    // 是否返回按钮
    public boolean isBack = false;

    // 当前上下文
    public Context thisContext;
    // 提交抽样单进度条
    public ProgressDialog progressDialog;
    // 标题
    public TextView title;
    // 返回按钮
    public Button back;
    // 保存按钮
    public TextView save;
    // 提交按钮
    public TextView commitText;
    // 受检单位
    public TextView subjectUnit;
    // 经纬度
    public EditText coordinate;
    // 获取经纬度按钮
    public TextView get_coordinate;
    // 样品名称
    public Spinner sampleNameSpinner;
    // 样品名称进度
    public ProgressBar sampleNameBar;
    // 样品名称输入按钮
    public TextView autoComplete;
    // 抽样日期
    public TextView sampleDate;
    // 监测环节进度
    public ProgressBar monitorLinkBar;
    // 监测环节下拉框
    public Spinner monitorLinkSpinner;
    // 市下拉框
    public Spinner citySpinner;
    // 区县进度
    public ProgressBar countryBar;
    // 县下拉框
    public Spinner countrySpinner;
    // 提交显示层
    public LinearLayout choose_layout;
    // 定位管理器
    public LocationManager locationManager;
    // 定位广播回调接收器
    public LocationReceiver locationReceiver;
    // 下拉框取值监听器
    public SprinnerSelectTaskListener selectListener;
    // 本地任务监听器
    public LocalTaskListener localTaskListener;
    // 提交抽样单监听器
    public WebTaskListener webTaskListener;

    // 下拉框回调句柄
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    /**
     * 执行onCreate操作
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisContext = this;
        // 添加Activitylist
        HdCnApp.getApplication().addActivity(this);
        initLocation();

        IntentFilter filter = new IntentFilter("location");
        locationReceiver = new LocationReceiver();
        this.registerReceiver(locationReceiver, filter);
    }

    /**
     * 
     * 支持onDestory操作
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(locationReceiver);
    }

    /**
     * 初始化值
     * 
     */
    public void getIntentValues() {
        Intent intent = getIntent();
        // 是否为添加模式
        isAdd = intent.getBooleanExtra(Constants.INTENT_KEY_ADD_SAMPLE, false);

        if (isAdd) {
            sampleInfoEntity = new SampleInfoEntity();
            // 如果是新增操作，就一定是未完成
            isDoing = true;
            // 如果是新增操作 要设置ID和抽样单ID
            sampleInfoEntity.setId(ConverterUtil.getUUID());
            sampleInfoEntity.setSamplingMonadId(ConverterUtil.getUUID());
            // 设置样品ID
            sampleInfoEntity.setSampleCode(ConverterUtil.getUUID());
        } else {
            // 是否为未完成
            isDoing = intent.getBooleanExtra(Constants.INTENT_KEY_DOING, false);
            sampleInfoEntity = (SampleInfoEntity) intent.getSerializableExtra(Constants.INTENT_KEY_PO);
        }

        // 取得选择的项目
        mMonitoringProjectEntity = DataManager.getInstance().getSelectProjectEntity();
        // 取得选择的任务
        mMonitoringTaskEntity = DataManager.getInstance().getSelectTaskEntity();

        // 几个共同下拉框的监听器
        selectListener = new SprinnerSelectTaskListener();
    }

    /**
     * 跳转受检单位选择
     * 
     */
    public void sendIntentToMonitoringSite() {
        Intent intent = new Intent();
        intent.setClass(thisContext, MonitoringSiteAct.class);
        String name = subjectUnit.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            intent.putExtra("mKeyWords", name);
        }
        startActivityForResult(intent, REQ_MONITORING_SITE_CODE);
    }

    /**
     * 跳转到农产品选择
     * 
     * @param projectCode
     *            项目Id
     * @param agrName
     *            农产品名称(默认选择)
     */
    public void sendIntentToBreedList(String agrName) {
        Intent intent = new Intent();
        intent.setClass(thisContext, BreadListAct.class);
        intent.putExtra("mKeyWords", agrName);
        startActivityForResult(intent, REQ_BREED_CODE);
    }

    /**
     * 跳转到条码扫描
     * 
     */
    public void sendIntentToBarCode() {
        Intent intent = new Intent();
        intent.setClass(thisContext, CaptureActivity.class);
        this.startActivityForResult(intent, REQ_BARCODE_CODE);
    }

    /**
     * 跳转到系统拍照
     * 
     */
    public void sendIntentToCameraPicture() {
        // sd卡已挂载状态
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File imagePath = Utils.getImageDirectory();
            // 生成文件
            File image = new File(imagePath, System.currentTimeMillis() + DataManager.getInstance().getPadId() + ".jpg");
            photoUri = Uri.fromFile(image);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            // intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            startActivityForResult(intent, REQ_CAPTURE_CODE);

        } else {
            // 无sd卡或异常
            Utils.showTips(this, "没有存储卡，请检查。");
        }
    }

    /**
     * 跳转到图片编辑
     * 
     * @param intent
     */
    public void sendIntentToCapture(Intent intent) {
        Uri uri = null;
        if (intent != null && intent.getData() != null) {
            uri = intent.getData();
        }

        // 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
        if (uri == null) {
            if (photoUri != null) {
                uri = photoUri;
            }
        }

        // startPhotoCrop(u, REQ_CROP_CODE); // 图片裁剪
        Intent it = new Intent();
        it.putExtra(Constants.IMAGE_URI, uri);
        it.setClass(thisContext, CropActivity.class);
        startActivityForResult(it, REQ_CROP_CODE);
    }

    /**
     * 初始化定位器
     */
    private void initLocation() {
        locationManager = LocationManager.newInstance();
    }

    /**
     * 进行定位操作
     */
    public void startLocation() {
        locationManager.startLocate();
    }

    /**
     * 设置经纬度坐标
     * 
     * @return 是否设置成功
     */
    public boolean setCoordation() {
        String coor = coordinate.getEditableText().toString();
        coor = coor.replace("E", "").replace("N", "");
        String[] coors = coor.split(",");
        if (coors.length >= 2) {
            try {
                logitude = Double.parseDouble(coors[0]);
                latitude = Double.parseDouble(coors[1]);
            } catch (Exception e) {
                Utils.showTips(thisContext, thisContext.getResources().getString(R.string.coordinate_error));
                logitude = 0d;
                latitude = 0d;
                return false;
            }
        }
        return true;
    }

    /**
     * 按钮点击事件监听器
     * 
     */
    private final OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.back:
                // 返回
                onBackClick();
                break;
            case R.id.sample_date_edit:
                // 调用日期函数
                callTimePicker(ConverterUtil.toString(((TextView) v).getText()));
                break;
            case R.id.get_coordinate:
                // 进行定位
                locationManager.startLocate();
                break;
            case R.id.inspected_unit_edit:
                // 进入选择受检单位
                Intent intent = new Intent();
                intent.setClass(thisContext, MonitoringSiteAct.class);
                String name = ((TextView) v).getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    intent.putExtra("mKeyWords", name);
                }
                startActivityForResult(intent, REQ_MONITORING_SITE_CODE);
                break;
            case R.id.do_auto_complete:
                // 进入农产品选择
                sendIntentToBreedList(sampleInfoEntity.getSampleName());
                break;
            }
        }
    };

    /**
     * 下拉框选择事件监听器
     * 
     */
    private final OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View v, int pos, long arg3) {
            switch (adapterView.getId()) {

            case R.id.sample_city_spinner:
                // cityPos = pos;
                // countyInfos = cityInfos.get(pos).getCountyList();
                countryBar.setVisibility(View.VISIBLE);
                countrySpinner.setEnabled(false);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("cityCode", ((Sprinner) adapterView.getAdapter().getItem(pos)).getId());
                TaskHelper.getLocalTask(thisContext, selectListener, DataManager.TASK_GET_COUNTRY_LIST, params);
                break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    public void setListener() {
        if (webTaskListener == null) {
            webTaskListener = new WebTaskListener();
        }
        if (localTaskListener == null) {
            localTaskListener = new LocalTaskListener();
        }
        back.setOnClickListener(onClickListener);
        subjectUnit.setOnClickListener(onClickListener);
        sampleDate.setOnClickListener(onClickListener);
        get_coordinate.setOnClickListener(onClickListener);
        citySpinner.setOnItemSelectedListener(onItemSelectedListener);
        if (ConverterUtil.isNotEmpty(autoComplete)) {
            autoComplete.setOnClickListener(onClickListener);
        }
    }

    public void onBackClick() {
        setResult(RESULT_CANCELED);
        onBackPressed();
    }

    /**
     * 保存主表信息
     * 
     */
    public void saveSampleInfoToEntity() {
        // 项目ID
        if (ConverterUtil.isEmpty(sampleInfoEntity.getProjectCode())) {
            sampleInfoEntity.setProjectCode(mMonitoringProjectEntity.getProjectCode());
        }
        // 任务ID
        if (ConverterUtil.isEmpty(sampleInfoEntity.getTaskCode())) {
            sampleInfoEntity.setTaskCode(mMonitoringTaskEntity.getTaskcode());
        }
        // padId
        if (ConverterUtil.isEmpty(sampleInfoEntity.getPadId())) {
            sampleInfoEntity.setPadId(DataManager.getInstance().getPadId());
        }
        // 抽样机构ID
        if (ConverterUtil.isEmpty(sampleInfoEntity.getSamplingOrgCode())) {
            sampleInfoEntity.setSamplingOrgCode(DataManager.getInstance().getOrgCode());
        }
        // 监测类型
        if (ConverterUtil.isEmpty(sampleInfoEntity.getMonitorType())) {
            sampleInfoEntity.setMonitorType(mMonitoringTaskEntity.getMonitorType());
        }
        // 抽样单模板
        if (ConverterUtil.isEmpty(sampleInfoEntity.getTemplete())) {
            sampleInfoEntity.setTemplete(mMonitoringProjectEntity.getTemplete());
        }
    }

    /**
     * 定位广播回调接收器
     * 
     * @author xudl(Wisea)
     * 
     */
    public class LocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            boolean isLocating = intent.getBooleanExtra("key", false);

            if (!isLocating) {
                BDLocation location = locationManager.getLocation();
                logitude = location.getLongitude();// 经度
                latitude = location.getLatitude();// 维度
                if (4.9e-324d == latitude) {
                    latitude = 0d;
                }
                if (4.9e-324d == logitude) {
                    logitude = 0d;
                }
                StringBuilder sf = new StringBuilder();
                sf.append(logitude).append("E,").append(latitude).append("N");
                coordinate.setText(sf.toString());
            }
        }
    }

    /**
     * 调用日期选择器
     * 
     * @param date
     *            初始化默认值
     * @param onClickListener
     * @return
     */
    public void callTimePicker(String date) {
        // 取得系统时间
        long initDate = System.currentTimeMillis();
        // 如果初始日期为空就用系统时间代替
        if (ConverterUtil.isNotEmpty(date)) {
            initDate = ConverterUtil.toDate(date, "yyyy-MM-dd").getTime();
        }
        // 创建弹出框构造器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 加载弹出框样式
        View view = View.inflate(this, R.layout.pick_date, null);
        // 取得android日历控件
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        builder.setView(view);
        // 取得java日历工具
        Calendar cal = Calendar.getInstance();
        // 设置日期
        cal.setTimeInMillis(initDate);
        // 初始化选择日期
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        // 设置弹出框提示文字
        builder.setTitle(this.getResources().getText(R.string.pleaseChoice));
        // 设置确定按钮方法
        builder.setPositiveButton(this.getResources().getText(R.string.done), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuffer sf = new StringBuffer();
                // 将日期按照yyyy-MM-dd的形式转型成字符串 datePicker中的month是从0开始的 需要+1
                sf.append(String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                // 设置日期
                sampleDate.setText(sf.toString());
                sampleDate.requestFocus();
                // 关闭对话框
                dialog.cancel();
            }
        });
        // 完成对话框bulid参数
        Dialog dialog = builder.create();
        // 显示对话框
        dialog.show();
    }

    public class SprinnerSelectTaskListener implements ITaskListener {
        @SuppressWarnings("unchecked")
        @Override
        public void onTaskFinish(int result, int request, final Object entity) {
            // 监测环节
            if (request == DataManager.TASK_GET_MONITOR_LINK_LIST) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // 取得当前项目对应的抽样环节
                        monitorLinkList = (List<Sprinner>) entity;
                        SelectAdapter monitorLinkAdapter = new SelectAdapter(thisContext, monitorLinkList);
                        monitorLinkSpinner.setAdapter(monitorLinkAdapter);
                        monitorLinkAdapter.notifyDataSetChanged();
                        // 如果不是添加要初始化选择位置
                        if (!isAdd) {
                            monitorLinkSpinner.setSelection(monitorLinkAdapter.getPosById(sampleInfoEntity.getMonitoringLink()));
                        }
                        if (isDoing) {
                            monitorLinkSpinner.setEnabled(true);
                        }
                        monitorLinkBar.setVisibility(View.GONE);
                    }

                });
            }
            // 取得地市
            if (request == DataManager.TASK_GET_CITY_LIST) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cityList = (List<Sprinner>) entity;
                        SelectAdapter cityAdapter = new SelectAdapter(thisContext, cityList);
                        citySpinner.setAdapter(cityAdapter);
                        cityAdapter.notifyDataSetChanged();
                        // 如果不是添加要初始化选择位置
                        if (!isAdd) {
                            citySpinner.setSelection(cityAdapter.getPosById(sampleInfoEntity.getCityCode()));
                        }
                        if (isDoing) {
                            citySpinner.setEnabled(true);
                        }
                    }

                });
            }

            // 取得地市对应的区县
            if (request == DataManager.TASK_GET_COUNTRY_LIST) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        countryList = (List<Sprinner>) entity;
                        SelectAdapter countryAdapter = new SelectAdapter(thisContext, countryList);
                        countrySpinner.setAdapter(countryAdapter);
                        countryAdapter.notifyDataSetChanged();
                        // 如果不是添加要初始化选择位置
                        if (!isAdd) {
                            countrySpinner.setSelection(countryAdapter.getPosById(sampleInfoEntity.getCountyCode()));
                        }
                        if (isDoing) {
                            countrySpinner.setEnabled(true);
                        }
                        countryBar.setVisibility(View.GONE);
                    }
                });
            }

            // 取得样品名称
            if (request == DataManager.TASK_GET_BREED_LIST) {
                handler.post(new Runnable() {
                    public void run() {
                        // 取得抽样品种
                        breedList = (List<Sprinner>) entity;
                        // 设置抽样品种适配器
                        SelectAdapter sampleNameAdapter = new SelectAdapter(thisContext, breedList);
                        sampleNameSpinner.setAdapter(sampleNameAdapter);
                        // 如果不是添加要初始化选择位置
                        if (!isAdd) {
                            sampleNameSpinner.setSelection(sampleNameAdapter.getPosById(sampleInfoEntity.getAgrCode()));
                        }
                        if (isDoing) {
                            sampleNameSpinner.setEnabled(true);
                            autoComplete.setEnabled(true);
                        }
                        sampleNameBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    private class LocalTaskListener implements ITaskListener {
        @Override
        public void onTaskFinish(int result, int request, Object entity) {
            if (request == DataManager.TASK_UPDATE_SAMPLE_STATUS) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showToast(thisContext, "提交成功");
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
                return;
            }
        }

    }

    private class WebTaskListener implements ITaskListener {

        @Override
        public void onTaskFinish(int result, int request, final Object entity) {
            if (result == RESULT_OK) {
                if (request == TaskHelper.REQUEST_SAVE_SAMPLE_IMAGE) {
                    // Web上传图片
                    handler.post(new Runnable() {
                        public void run() {
                            // 上传图片返回结果
                            Map<String, String> imageReqMap = WebServiceUtil.getSampleImageMap((SoapObject) entity);
                            // key:图片名 value:子表id
                            for (String key : imageReqMap.keySet()) {
                                String templete = sampleInfoEntity.getTemplete();
                                // 例行监测抽样单表
                                if (Constants.TEMPLETE_ROUTINE_MONITORING.equals(templete)) {
                                    // 例行监测抽样单的图片存储在实体的子表中
                                    for (RoutinemonitoringEntity rmEntity : sampleInfoEntity.getRoutinemonitoringList()) {
                                        if (ConverterUtil.isNotEmpty(rmEntity.getSamplePath())) {
                                            // 取得文件名
                                            if (key.equals(rmEntity.getSamplePath())) {
                                                // 如果返回值不为空就设置子表图片ID
                                                if (ConverterUtil.isNotEmpty(imageReqMap.get(key))) {
                                                    rmEntity.setImgContent(imageReqMap.get(key));
                                                } else {
                                                    Utils.showToast(thisContext, rmEntity.getSampleName() + ":样品图片上传失败,请重试");
                                                }
                                            }
                                        }
                                    }
                                } else if (Constants.TEMPLETE_LIVESTOCK.equals(templete)) {
                                    // 畜禽抽样单的图片存储在实体的子表中
                                    for (LivestockEntity ltEntity : sampleInfoEntity.getLivestockEntityList()) {
                                        if (ConverterUtil.isNotEmpty(ltEntity.getSamplePath())) {
                                            // 取得文件名
                                            if (key.equals(ltEntity.getSamplePath())) {
                                                // 如果返回值不为空就设置子表图片ID
                                                if (ConverterUtil.isNotEmpty(imageReqMap.get(key))) {
                                                    ltEntity.setImgContent(imageReqMap.get(key));
                                                } else {
                                                    Utils.showToast(thisContext, ltEntity.getSampleName() + ":样品图片上传失败,请重试");
                                                }
                                            }
                                        }
                                    }

                                } else {
                                    // 普查(风险)抽样单\监督抽查抽样单\生鲜乳抽样单的图片存储在实体类的主表中
                                    if (ConverterUtil.isNotEmpty(sampleInfoEntity.getSamplePath())) {
                                        // 取得文件名
                                        if (key.equals(sampleInfoEntity.getSamplePath())) {
                                            // 如果返回值不为空就设置子表图片ID
                                            if (ConverterUtil.isNotEmpty(imageReqMap.get(key))) {
                                                sampleInfoEntity.setImgContent(imageReqMap.get(key));
                                            } else {
                                                Utils.showToast(thisContext, sampleInfoEntity.getSampleName() + ":样品图片上传失败,请重试");
                                            }
                                        }
                                    }
                                }
                            }
                            try {
                                // Web提交抽样单
                                progressDialog.setMessage(getResources().getText(R.string.msg_commit_sample));
                                TaskHelper.saveSample(thisContext, webTaskListener, sampleInfoEntity, sampleInfoEntity.getTemplete());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Utils.showToast(thisContext, "提交失败");
                                Utils.dismissDialog(progressDialog);
                            }
                        }
                    });
                } else if (request == TaskHelper.REQUEST_SAVE_SAMPLEINFO) {
                    // Web保存抽样单
                    handler.post(new Runnable() {
                        public void run() {
                            Utils.dismissDialog(progressDialog);
                            // 提交数据
                            try {
                                // 取得Web提交的返回值
                                int code = WebServiceUtil.saveSampleInfo((SoapObject) entity);
                                switch (code) {
                                case 0:
                                    Utils.showToast(thisContext, "录入异常失败");
                                    break;
                                case 1:
                                    Map<String, Object> param = new HashMap<String, Object>();
                                    param.put("samplingMonadId", sampleInfoEntity.getSamplingMonadId());
                                    // 本地改变抽样单的状态为已完成
                                    TaskHelper.getLocalTask(thisContext, localTaskListener, DataManager.TASK_UPDATE_SAMPLE_STATUS, param);
                                    break;
                                case 2:
                                    Utils.showToast(thisContext, "重复的条码，录入失败");
                                    break;
                                default:
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Utils.dismissDialog(progressDialog);
                                Utils.showToast(thisContext, "连接服务器失败");
                            }
                        }
                    });
                }
            } else if (result == RESULT_NET_ERROR) {
                // Web保存抽样单
                handler.post(new Runnable() {
                    public void run() {
                        Utils.dismissDialog(progressDialog);
                        //Utils.showTips(thisContext, thisContext.getResources().getString(R.string.msg_net_error));
                        // 所有异常都按照网络异常处理
                        Utils.showTips(thisContext, thisContext.getResources().getString(R.string.msg_net_error_retry));
                    }
                });
            } else if (result == RESULT_FAILED) {
                // Web保存抽样单
                handler.post(new Runnable() {
                    public void run() {
                        Utils.dismissDialog(progressDialog);
                        //Utils.showTips(thisContext, thisContext.getResources().getString(R.string.msg_host_response_error));
                        // 所有异常都按照网络异常处理
                        Utils.showTips(thisContext, thisContext.getResources().getString(R.string.msg_net_error_retry));
                    }
                });
            } else {
                // Web保存抽样单
                handler.post(new Runnable() {
                    public void run() {
                        Utils.dismissDialog(progressDialog);
                        //Utils.showTips(thisContext, thisContext.getResources().getString(R.string.msg_host_no_response));
                        // 所有异常都按照网络异常处理
                        Utils.showTips(thisContext, thisContext.getResources().getString(R.string.msg_net_error_retry));
                    }
                });
            }
        }
    }

    /**
     * 
     * 初始化产品认证情况下拉框的值
     */
    public List<Sprinner> getSignList() {
        List<Sprinner> signList = new ArrayList<Sprinner>();
        Sprinner free = new Sprinner();
        free.setId("1");
        free.setName("无公害农产品");
        signList.add(free);

        Sprinner green = new Sprinner();
        green.setId("2");
        green.setName("绿色食品");
        signList.add(green);

        Sprinner organo = new Sprinner();
        organo.setId("3");
        organo.setName("有机农产品");
        signList.add(organo);

        Sprinner other = new Sprinner();
        other.setId("4");
        other.setName("其他认证");
        signList.add(other);
        return signList;
    }

    /**
     * 初始化抽样类型下拉框的值
     */
    public List<Sprinner> getSampleTypeList() {
        List<Sprinner> typeList = new ArrayList<Sprinner>();
        Sprinner free = new Sprinner();
        free.setId("0");
        free.setName("奶畜养殖场开办");
        typeList.add(free);

        Sprinner green = new Sprinner();
        green.setId("1");
        green.setName("奶农专业合作社开办");
        typeList.add(green);

        Sprinner organo = new Sprinner();
        organo.setId("2");
        organo.setName("乳制品生产企业开办");
        typeList.add(organo);

        Sprinner other = new Sprinner();
        other.setId("3");
        other.setName("其他");
        typeList.add(other);
        return typeList;
    }
}
