/**
 * 文件名称 : FillLiveStockSampleAct.java
 * 作者信息 : anjihang
 * 创建时间 : 2014年4月10日
 * 版权声明 : Copyright (c) doumi-tech All rights reserved
 */

package hd.produce.security.cn;

import hd.produce.security.cn.adapter.SelectAdapter;
import hd.produce.security.cn.dao.CommonDao;
import hd.produce.security.cn.data.BarCodeInfo;
import hd.produce.security.cn.data.LivestockEntity;
import hd.produce.security.cn.data.MonitoringSiteEntity;
import hd.produce.security.cn.entity.Sprinner;
import hd.produce.security.cn.ui.common.SampleActivity;
import hd.produce.security.cn.view.SubListView;
import hd.source.task.DataManager;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.source.task.WebServiceUtil;
import hd.utils.cn.BitmapUtils;
import hd.utils.cn.Constants;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.FileUtils;
import hd.utils.cn.LogUtil;
import hd.utils.cn.NetworkControl;
import hd.utils.cn.Utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 畜禽抽样单填写提交界面
 * 
 * @author xudl(Wisea)
 * 
 */
public class FillLiveStockSampleAct extends SampleActivity implements ITaskListener {
    // 子表数据
    private List<LivestockEntity> lists = new ArrayList<LivestockEntity>();

    // 抽样人员
    private EditText samplingPersons;
    // 通讯地址
    private EditText address;
    // 邮编
    private EditText zipCode;
    // 电话
    private EditText tel;
    // 抽样数量
    private EditText sampleNum;
    // 抽样基数
    private EditText cardinalNum;
    // 抽样依据
    private EditText sampleBasis;
    // 商标
    private EditText logo;
    // 抽样方式:总体随机
    private CheckBox random;
    // 抽样方式:其它
    private CheckBox other;
    // 样品包装:完好
    private CheckBox good;
    // 样品包装:不完好
    private CheckBox bad;
    // 签封标志:完好
    private CheckBox signalGood;
    // 签封标志:不完好
    private CheckBox signalBad;
    // 添加子表按钮
    private TextView addSubTable;

    private CommitListener commitListener;

    private Handler handler = new Handler();

    private SubListView mListView;
    private SubTableAdapter subAdapter;
    private int mCurrentSubTablePos = 0;
    private int mPosition = -1;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_fill_live_stock_sample);

        // 初始化值
        getIntentValues();
        initView();
        setListener();

        String intentKey = coordinate.getEditableText().toString();
        if (null == intentKey || "".equals(intentKey)) {
            startLocation();
        }

        TaskHelper.getLocalTask(this, selectListener, DataManager.TASK_GET_MONITOR_LINK_LIST, null);
        TaskHelper.getLocalTask(this, selectListener, DataManager.TASK_GET_CITY_LIST, null);
        TaskHelper.getLocalTask(this, selectListener, DataManager.TASK_GET_BREED_LIST, null);
    }

    @Override
    public void setListener() {
        super.setListener();
        commitText.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
        // 未完成才有改按钮事件
        if (isAdd || isDoing) {
            addSubTable.setOnClickListener(onClickListener);
        }

        // 添加单选监听器
        OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch (buttonView.getId()) {
                    case R.id.random_check:
                        other.setChecked(false);
                        break;
                    case R.id.other_check:
                        random.setChecked(false);
                        break;
                    case R.id.good_check:
                        bad.setChecked(false);
                        break;
                    case R.id.bad_check:
                        good.setChecked(false);
                        break;
                    case R.id.signal_good_check:
                        signalBad.setChecked(false);
                        break;
                    case R.id.signal_bad_check:
                        signalGood.setChecked(false);
                        break;
                    default:
                        break;
                    }
                }

            }
        };
        random.setOnCheckedChangeListener(onCheckedChangeListener);
        other.setOnCheckedChangeListener(onCheckedChangeListener);
        good.setOnCheckedChangeListener(onCheckedChangeListener);
        bad.setOnCheckedChangeListener(onCheckedChangeListener);
        signalGood.setOnCheckedChangeListener(onCheckedChangeListener);
        signalBad.setOnCheckedChangeListener(onCheckedChangeListener);

        sampleNameSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sampleInfoEntity.setAgrCode(((Sprinner) sampleNameSpinner.getSelectedItem()).getId());
                sampleInfoEntity.setSampleName(((Sprinner) sampleNameSpinner.getSelectedItem()).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        commitListener = new CommitListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title_text);
        title.setText(getResources().getString(R.string.lab_livestock));
        mListView = (SubListView) findViewById(R.id.listview);
        commitText = (TextView) findViewById(R.id.commit);
        save = (TextView) findViewById(R.id.save_sample_text);

        // 经纬度
        coordinate = (EditText) findViewById(R.id.coordinate_edit);
        // 获取经纬度按钮
        get_coordinate = (TextView) findViewById(R.id.get_coordinate);
        // 抽样场所下拉框
        monitorLinkSpinner = (Spinner) findViewById(R.id.sample_sites_spinner);
        monitorLinkSpinner.setEnabled(false);
        monitorLinkBar = (ProgressBar) findViewById(R.id.sample_sites_bar);
        // 抽样时间
        sampleDate = (TextView) findViewById(R.id.sample_date_edit);
        // 地市下拉框
        citySpinner = (Spinner) findViewById(R.id.sample_city_spinner);
        // 区县下拉框
        countrySpinner = (Spinner) findViewById(R.id.sample_area_spinner);
        countrySpinner.setEnabled(false);
        countryBar = (ProgressBar) findViewById(R.id.country_bar);
        // 样品名称下拉框
        sampleNameSpinner = (Spinner) findViewById(R.id.sample_name_spinner);
        sampleNameSpinner.setEnabled(false);
        sampleNameBar = (ProgressBar) findViewById(R.id.breed_bar);
        // 输入按钮
        autoComplete = (TextView) findViewById(R.id.do_auto_complete);
        // 抽样人员
        samplingPersons = (EditText) findViewById(R.id.sample_user_edit);
        // 受检单位名字
        subjectUnit = (TextView) findViewById(R.id.inspected_unit_edit);
        // 通讯地址
        address = (EditText) findViewById(R.id.sample_address_edit);
        // 邮编
        zipCode = (EditText) findViewById(R.id.sample_zip_code_edit);
        // 电话
        tel = (EditText) findViewById(R.id.sample_tel_edit);
        // 抽样数量
        sampleNum = (EditText) findViewById(R.id.sample_num_edit);
        // 抽样基数
        cardinalNum = (EditText) findViewById(R.id.cardinal_num_edit);
        // 抽样依据
        sampleBasis = (EditText) findViewById(R.id.sample_basis_edit);
        // 商标
        logo = (EditText) findViewById(R.id.logo_edit);
        // 抽样方式:总体随机
        random = (CheckBox) findViewById(R.id.random_check);
        // 抽样方式:其它
        other = (CheckBox) findViewById(R.id.other_check);
        // 样品包装:完好
        good = (CheckBox) findViewById(R.id.good_check);
        // 样品包装:不完好
        bad = (CheckBox) findViewById(R.id.bad_check);
        // 签封标志:完好
        signalGood = (CheckBox) findViewById(R.id.signal_good_check);
        // 签封标志:不完好
        signalBad = (CheckBox) findViewById(R.id.signal_bad_check);

        // 添加子表按钮
        addSubTable = (TextView) findViewById(R.id.add_sub_table);
        // 提交显示层
        choose_layout = (LinearLayout) findViewById(R.id.choose_layout);
        if (isAdd) {
            // 如果是新增，则创建一个空的子entity
            LivestockEntity livestock = new LivestockEntity();
            livestock.setId(ConverterUtil.getUUID());
            // 设置抽样单ID
            livestock.setSamplingMonadId(sampleInfoEntity.getSamplingMonadId());
            // 设置样品ID(第一个子表的sampleCode与主表相同)
            livestock.setSampleCode(sampleInfoEntity.getSampleCode());
            // 任务ID
            livestock.setTaskCode(mMonitoringTaskEntity.getTaskcode());
            // 抽样单创建时间
            livestock.setSamplingTime(ConverterUtil.toString(System.currentTimeMillis()));
            addSubTable(livestock);
            return;
        } else {
            // 如果是已完成，则不可更改
            if (!isDoing) {
                coordinate.setEnabled(false);
                get_coordinate.setEnabled(false);
                monitorLinkSpinner.setEnabled(false);
                sampleDate.setEnabled(false);
                citySpinner.setEnabled(false);
                countrySpinner.setEnabled(false);
                sampleNameSpinner.setEnabled(false);
                autoComplete.setEnabled(false);
                samplingPersons.setEnabled(false);
                subjectUnit.setEnabled(false);
                address.setEnabled(false);
                zipCode.setEnabled(false);
                tel.setEnabled(false);
                sampleNum.setEnabled(false);
                cardinalNum.setEnabled(false);
                sampleBasis.setEnabled(false);
                logo.setEnabled(false);
                random.setEnabled(false);
                other.setEnabled(false);
                good.setEnabled(false);
                bad.setEnabled(false);
                signalGood.setEnabled(false);
                signalBad.setEnabled(false);
                addSubTable.setText(getResources().getString(R.string.view_sub_table));
                choose_layout.setVisibility(View.GONE);
            }
        }

        // 经纬度
        if (null != sampleInfoEntity.getLongitude() && null != sampleInfoEntity.getLatitude()) {
            coordinate.setText(sampleInfoEntity.getLongitude() + "E" + "," + sampleInfoEntity.getLatitude() + "N");
        }
        // 抽样时间
        sampleDate.setText(sampleInfoEntity.getSamplingDate());
        // 抽样人员
        samplingPersons.setText(sampleInfoEntity.getSamplingPersons());
        // 受检单位
        subjectUnit.setText(sampleInfoEntity.getUnitFullname());
        // 通讯地址
        address.setText(sampleInfoEntity.getUnitAddress());
        // 邮编
        zipCode.setText(sampleInfoEntity.getZipCode());
        // 电话
        tel.setText(sampleInfoEntity.getTelphone());
        // 抽样场所\地市\区县\样品名称 的回显在调用Task里面
        if (ConverterUtil.isNotEmpty(sampleInfoEntity.getLivestockEntityList())) {
            // 抽样数量
            sampleNum.setText(sampleInfoEntity.getLivestockEntityList().get(0).getSamplingCount());
            // 抽样基数
            cardinalNum.setText(sampleInfoEntity.getLivestockEntityList().get(0).getSamplingBaseCount());
            // 抽样依据
            sampleBasis.setText(sampleInfoEntity.getLivestockEntityList().get(0).getTaskSource());
            // 商标
            logo.setText(sampleInfoEntity.getLivestockEntityList().get(0).getTradeMark());
            // 抽样方式
            String mode = sampleInfoEntity.getLivestockEntityList().get(0).getSamplingMode();
            if (ConverterUtil.isNotEmpty(mode)) {
                if (mode.equals("0")) {
                    // 总体随机
                    random.setChecked(true);
                } else if (mode.equals("1")) {
                    // 其它
                    other.setChecked(true);
                }
            }

            // 样品包装
            String pack = sampleInfoEntity.getLivestockEntityList().get(0).getPack();
            if (ConverterUtil.isNotEmpty(pack)) {
                if (pack.equals("0")) {
                    // 完好
                    good.setChecked(true);
                } else if (pack.equals("1")) {
                    // 不完好
                    bad.setChecked(true);
                }
            }

            // 签封标志
            String signFlg = sampleInfoEntity.getLivestockEntityList().get(0).getSignFlg();
            if (ConverterUtil.isNotEmpty(signFlg)) {
                if (signFlg.equals("0")) {
                    // 完好
                    signalGood.setChecked(true);
                } else if (signFlg.equals("1")) {
                    // 不完好
                    signalBad.setChecked(true);
                }
            }

        }
        addBatchSubTable(sampleInfoEntity.getLivestockEntityList());
    }

    @Override
    public void onBackPressed() {
        if (isAdd || isDoing) {
            isBack = true;
            isCommit = false;
            onSaveClick();
        } else {
            super.onBackPressed();
        }
    }

    private void onCommitClick() {
        if(!NetworkControl.checkNet(thisContext)){
            Utils.showTips(this, getResources().getString(R.string.msg_net_error));
            return;
        }
        for (LivestockEntity rme : lists) {
            if (TextUtils.isEmpty(rme.getdCode())) {
                Utils.showTips(this, "条码不能为空");
                return;
            }

            if (rme.getdCode().length() != 13) {
                Utils.showTips(this, "请填写正确的条码");
                return;
            }
            for (LivestockEntity temp : lists) {
                // 自己则跳过
                if (rme.getId().equals(temp.getId())) {
                    continue;
                }
                if (rme.getdCode().equals(temp.getdCode())) {
                    Utils.showTips(this, "条码不能重复");
                    return;
                }
            }
        }
        Utils.showConfimDialog(FillLiveStockSampleAct.this, FillLiveStockSampleAct.this.getResources().getText(R.string.msg_confim_commit), new Utils.OnDialogDoneListener() {
            @Override
            public void onDialogDone() {
                onSaveClick();
            }
        });
    }

    private void checkDCode() {
        try {
            int index = mPosition + 1;
            if (index <= 0) {
                index = 1;
            }
            progressDialog.setMessage(MessageFormat.format(ConverterUtil.toString(getResources().getText(R.string.msg_check_dCode)), index, lists.size()));
            TaskHelper.checkDCode(this, commitListener, lists.get(mPosition).getdCode(), mMonitoringTaskEntity.getProjectCode());
        } catch (Exception e) {
            e.printStackTrace();
            mPosition = -1;
            Utils.showTips(this, "提交失败");
            Utils.dismissDialog(progressDialog);
        }
    }

    private void onSaveClick() {
        setCoordation();
        saveSampleInfoToEntity();
        Map<String, Object> param = new HashMap<String, Object>();
        if (ConverterUtil.isEmpty(sampleInfoEntity.getTemplete())) {
            sampleInfoEntity.setTemplete(mMonitoringProjectEntity.getTemplete());
        }
        param.put("sampleInfo", sampleInfoEntity);
        TaskHelper.getLocalTask(FillLiveStockSampleAct.this, FillLiveStockSampleAct.this, DataManager.TASK_SAVE_SAMPLE, param);
    }

    /**
     * 增加子表
     * 
     * @param rme
     */
    private void addSubTable(LivestockEntity rme) {
        if (lists.size() <= 0) {
            subAdapter = new SubTableAdapter(lists);
            mListView.setAdapter(subAdapter);
        } else if(lists.size() >= 15){
            Utils.showTips(this, ConverterUtil.toString(getResources().getText(R.string.msg_morest_15_sub)));
            return;
        }
        lists.add(rme);
        subAdapter.notifyDataSetChanged();
    }

    /**
     * 批量增加子表
     * 
     * @param rmes
     */
    private void addBatchSubTable(List<LivestockEntity> rmes) {
        if (lists.size() <= 0) {
            subAdapter = new SubTableAdapter(lists);
            mListView.setAdapter(subAdapter);
        }
        if (rmes == null) {
            return;
        }
        for (LivestockEntity rme : rmes) {
            // 任务ID
            rme.setTaskCode(mMonitoringTaskEntity.getTaskcode());
            lists.add(rme);
        }
        subAdapter.notifyDataSetChanged();
    }

    @Override
    public void saveSampleInfoToEntity() {
        super.saveSampleInfoToEntity();
        // 经度
        sampleInfoEntity.setLongitude(String.valueOf(logitude));
        // 纬度
        sampleInfoEntity.setLatitude(String.valueOf(latitude));
        // 抽样场所
        sampleInfoEntity.setMonitoringLink(((Sprinner) monitorLinkSpinner.getSelectedItem()).getId());
        // 抽样时间
        sampleInfoEntity.setSamplingDate(ConverterUtil.toString(sampleDate.getText()));
        // 抽样地市
        sampleInfoEntity.setCityCode(((Sprinner) citySpinner.getSelectedItem()).getId());
        // 抽样区县
        sampleInfoEntity.setCountyCode(((Sprinner) countrySpinner.getSelectedItem()).getId());
        // 样品名称
        sampleInfoEntity.setAgrCode(((Sprinner) sampleNameSpinner.getSelectedItem()).getId());
        sampleInfoEntity.setSampleName(((Sprinner) sampleNameSpinner.getSelectedItem()).getName());
        // 抽样人员
        sampleInfoEntity.setSamplingPersons(ConverterUtil.toString(samplingPersons.getEditableText()));
        // 受检单位
        sampleInfoEntity.setUnitFullname(ConverterUtil.toString(subjectUnit.getText()));
        // 通讯地址
        sampleInfoEntity.setUnitAddress(ConverterUtil.toString(address.getText()));
        // 邮编
        sampleInfoEntity.setZipCode(ConverterUtil.toString(zipCode.getText()));
        // 电话
        sampleInfoEntity.setTelphone(ConverterUtil.toString(tel.getText()));

        for (LivestockEntity rme : lists) {
            // 抽样数量
            rme.setSamplingCount(ConverterUtil.toString(sampleNum.getText()));
            // 抽样基数
            rme.setSamplingBaseCount(ConverterUtil.toString(cardinalNum.getText()));
            // 抽样依据
            rme.setTaskSource(ConverterUtil.toString(sampleBasis.getText()));
            // 商标
            rme.setTradeMark(ConverterUtil.toString(logo.getText()));
            // 总体随机
            if (random.isChecked()) {
                rme.setSamplingMode("0");
            }
            // 其它
            if (other.isChecked()) {
                rme.setSamplingMode("1");
            }
            // 完好
            if (good.isChecked()) {
                rme.setPack("0");
            }
            // 不完好
            if (bad.isChecked()) {
                rme.setPack("1");
            }
            // 完好
            if (signalGood.isChecked()) {
                rme.setSignFlg("0");
            }
            // 不完好
            if (signalBad.isChecked()) {
                rme.setSignFlg("1");
            }
        }
        sampleInfoEntity.setLivestockEntityList(lists);
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.commit:
                isCommit = true;
                onCommitClick();
                break;
            case R.id.save_sample_text:
                isBack = false;
                isCommit = false;
                onSaveClick();
                break;
            case R.id.add_sub_table:
                LivestockEntity livestock = new LivestockEntity();
                // 复制上一个表的数据
                if (lists != null && !lists.isEmpty()) {
                    LivestockEntity r = lists.get(lists.size() - 1);
                    livestock.setSamplingBaseCount(r.getSamplingBaseCount());
                    livestock.setSamplingCount(r.getSamplingCount());
                    livestock.setTradeMark(r.getTradeMark());
                    livestock.setTaskSource(r.getTaskSource());
                    livestock.setCargoOwner(r.getCargoOwner());
                    livestock.setAnimalOrigin(r.getAnimalOrigin());
                    livestock.setCardNumber(r.getCardNumber());
                    livestock.setRemark(r.getRemark());
                    livestock.setSamplingMode(r.getSamplingMode());
                    livestock.setPack(r.getPack());
                    livestock.setSignFlg(r.getSignFlg());
                    //livestock.setSamplePath(r.getSamplePath());
                    livestock.setSamplingAddress(r.getSamplingAddress());
                    livestock.setSamplingMonadId(r.getSamplingMonadId());
                } else {
                    livestock.setSamplingMonadId(sampleInfoEntity.getSamplingMonadId());
                }
                livestock.setId(ConverterUtil.getUUID());
                livestock.setTaskCode(mMonitoringTaskEntity.getTaskcode());
                // 设置样品ID
                livestock.setSampleCode(ConverterUtil.getUUID());
                livestock.setSamplingTime(ConverterUtil.toString(System.currentTimeMillis()));
                addSubTable(livestock);
                break;
            case R.id.subjects_name_edit:
                sendIntentToMonitoringSite();
                break;
            default:
                break;
            }
        }
    };

    @SuppressWarnings("unchecked")
    @Override
    public void onTaskFinish(int result, int request, final Object entity) {
        // 本地保存抽样单
        if (request == DataManager.TASK_SAVE_SAMPLE) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (ConverterUtil.toBoolean(entity)) {
                        if (isCommit) {
                            progressDialog = Utils.showProgress(FillLiveStockSampleAct.this, null, FillLiveStockSampleAct.this.getResources().getText(R.string.msg_commit_sample), false, false);
                            // 验证二维码
                            mPosition = 0;
                            checkDCode();
                            return;
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isBack) {
                                    Utils.showTips(FillLiveStockSampleAct.this, FillLiveStockSampleAct.this.getResources().getString(R.string.msg_save_success));
                                    FillLiveStockSampleAct.super.onBackPressed();
                                } else {
                                    Utils.showTips(FillLiveStockSampleAct.this, FillLiveStockSampleAct.this.getResources().getString(R.string.msg_save_success));
                                    setResult(Activity.RESULT_CANCELED);
                                    finish();
                                }
                            }
                        });
                    }
                }
            });
        }

        if (request == DataManager.TASK_DELETE_SAMPLE) {
            // 删除抽样单
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Map<String, Object> res = (Map<String, Object>) entity;
                    boolean delSuccess = ConverterUtil.toBoolean(res.get("result"));
                    if (delSuccess) {
                        int pos = ConverterUtil.toInteger(res.get("position"));
                        lists.remove(pos);
                        List<LivestockEntity> delList = new ArrayList<LivestockEntity>();
                        delList.addAll(lists);
                        subAdapter.update(delList);
                    }
                }
            });
        }
    }

    class SubTableAdapter extends BaseAdapter {
        private List<LivestockEntity> mList;

        public SubTableAdapter(List<LivestockEntity> list) {
            mList = list;
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mList == null) {
                return 0;
            }
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public void update(List<LivestockEntity> list) {
            mList = list;
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(FillLiveStockSampleAct.this).inflate(R.layout.fill_live_stock_sample_item_layout, null);
            TextView index = (TextView) convertView.findViewById(R.id.barcode_layout_index_tv);
            index.setText("表" + String.valueOf(position + 1));

            TextView del = (TextView) convertView.findViewById(R.id.del);
            del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Utils.showConfimDialog(FillLiveStockSampleAct.this, FillLiveStockSampleAct.this.getResources().getText(R.string.msg_confim_delete), new Utils.OnDialogDoneListener() {
                        @Override
                        public void onDialogDone() {
                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("sampleCode", lists.get(position).getSampleCode());
                            params.put("templete", CommonDao.TB_LIVESTOCK);
                            params.put("position", position);
                            TaskHelper.getLocalTask(FillLiveStockSampleAct.this, FillLiveStockSampleAct.this, DataManager.TASK_DELETE_SAMPLE, params);
                        }
                    });
                }
            });

            // 二维码
            EditText barCode;
            // 获取条码
            TextView getBarCode;
            // 样品图片
            ImageView image;
            // 畜主/货主
            EditText liveStockOwner;
            // 动物产地/来源
            EditText liveStockOriginPlace;
            // 检疫证号
            EditText quarantine;
            // 抽样地点
            EditText samplingAddress;
            // 备注
            EditText mark;

            getBarCode = (TextView) convertView.findViewById(R.id.barcode_text);
            barCode = (EditText) convertView.findViewById(R.id.barcode_edit);
            image = (ImageView) convertView.findViewById(R.id.image);
            liveStockOwner = (EditText) convertView.findViewById(R.id.live_stock_owner_edit);
            liveStockOriginPlace = (EditText) convertView.findViewById(R.id.live_stock_origin_place_edit);
            quarantine = (EditText) convertView.findViewById(R.id.quarantine_edit);
            mark = (EditText) convertView.findViewById(R.id.mark_edit);
            samplingAddress = (EditText) convertView.findViewById(R.id.sample_address_layout_detail_edit);

            barCode.setText(lists.get(position).getdCode());

            liveStockOwner.setText(lists.get(position).getCargoOwner());
            liveStockOriginPlace.setText(lists.get(position).getAnimalOrigin());
            quarantine.setText(lists.get(position).getCardNumber());
            mark.setText(lists.get(position).getRemark());
            samplingAddress.setText(lists.get(position).getSamplingAddress());

            getBarCode.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCurrentSubTablePos = position;
                    sendIntentToBarCode();
                }
            });

            if (ConverterUtil.isNotEmpty(lists.get(position).getdCode())) {
                barCode.setText(lists.get(position).getdCode());
                try {
                    barCode.setSelection(lists.get(position).getdCode().length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            barCode.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    lists.get(position).setdCode(s.toString());
                }
            });

            image.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCurrentSubTablePos = position;
                    sendIntentToCameraPicture();
                }
            });

            // 显示样品图片
            String imagePath = lists.get(position).getSamplePath();
            if (ConverterUtil.isNotEmpty(imagePath)) {
                Bitmap bitmap = BitmapUtils.decodeFile(Utils.getImageDirectory() + imagePath);
                if (bitmap != null) {
                    image.setImageBitmap(bitmap);
                }
            }

            liveStockOwner.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        lists.get(position).setCargoOwner(s.toString());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            liveStockOriginPlace.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        lists.get(position).setAnimalOrigin(s.toString());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            quarantine.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        lists.get(position).setCardNumber(s.toString());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            mark.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        lists.get(position).setRemark(s.toString());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            samplingAddress.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        lists.get(position).setSamplingAddress(s.toString());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            if (!isDoing) {
                del.setVisibility(View.GONE);
                barCode.setEnabled(false);
                getBarCode.setEnabled(false);
                image.setEnabled(false);
                liveStockOwner.setEnabled(false);
                liveStockOriginPlace.setEnabled(false);
                quarantine.setEnabled(false);
                samplingAddress.setEnabled(false);
                mark.setEnabled(false);
            }

            return convertView;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
            case REQ_BARCODE_CODE:
                onBarCodeResult(data);
                break;
            case REQ_CAPTURE_CODE:
                sendIntentToCapture(data);
                break;
            case REQ_CROP_CODE:
                onCropResult(data);
                break;
            case REQ_MONITORING_SITE_CODE:
                if (data == null)
                    return;
                String name = data.getStringExtra("mKeyWords");
                if (!TextUtils.isEmpty(name)) {
                    subjectUnit.setText(name);
                } else {
                    subjectUnit.setText("");
                }
                MonitoringSiteEntity entity = (MonitoringSiteEntity) data.getSerializableExtra("MonitoringSite");
                setMonitoringSite(entity);
                break;
            case REQ_BREED_CODE:
                if (data == null) {
                    return;
                }
                Sprinner breadEntity = (Sprinner) data.getSerializableExtra("MonitoringBreedEntity");
                if (breadEntity != null) {
                    sampleInfoEntity.setAgrCode(breadEntity.getId());
                    sampleInfoEntity.setSampleName(breadEntity.getName());
                    SelectAdapter selectAdapter = (SelectAdapter) sampleNameSpinner.getAdapter();
                    sampleNameSpinner.setSelection(selectAdapter.getPosById(breadEntity.getId()));
                }
                break;
            default:
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置受检单位
     * 
     * @param entity
     */
    private void setMonitoringSite(MonitoringSiteEntity entity) {
        if (entity != null) {
            if (entity.getAddress() == null) {
                address.setText("");
            } else {
                address.setText(entity.getAddress());
            }
            if (entity.getZipcode() == null) {
                zipCode.setText("");
            } else {
                zipCode.setText(entity.getZipcode());
            }
            if (entity.getContact() == null) {
                tel.setText("");
            } else {
                tel.setText(entity.getContact());
            }
            if (ConverterUtil.isNotEmpty(entity.getCode())) {
                sampleInfoEntity.setUnitFullcode(entity.getCode());
            }
        }
    }

    /**
     * @param intent
     *            裁剪之后的返回结果
     */
    private void onCropResult(Intent intent) {
        LogUtil.i("TAG", "onCropResult");
        // 拍照后存储的图片位置
        String imagePath = intent.getStringExtra(Constants.CROP_IMAGE_PATH);
        lists.get(mCurrentSubTablePos).setSamplePath("/" + FileUtils.getFileName(imagePath));
        subAdapter.notifyDataSetChanged();
    }

    /**
     * 扫描二维码之后的返回结果
     */
    private void onBarCodeResult(Intent intent) {
        BarCodeInfo barCodeInfo = (BarCodeInfo) intent.getSerializableExtra(Constants.BARCODE_KEY);
        String barCodeContent = barCodeInfo.getDisplayContent();
        lists.get(mCurrentSubTablePos).setdCode(barCodeContent);

        subAdapter.notifyDataSetChanged();
    }

    private class CommitListener implements ITaskListener {

        @Override
        public void onTaskFinish(int result, int request, final Object entity) {
            if (request == TaskHelper.REQUEST_CHECK_DCODE) {
                handler.post(new Runnable() {
                    public void run() {
                        // 验证二维码
                        String message = WebServiceUtil.checkDCodeResult((SoapObject) entity);
                        if (TextUtils.isEmpty(message)) {
                            // 循环检测二维码
                            mPosition++;
                            if (mPosition < lists.size()) {
                                checkDCode();
                            } else {
                                mPosition = -1;
                                Map<String, String> imageMap = new HashMap<String, String>();
                                for (LivestockEntity rmEntity : lists) {
                                    if (ConverterUtil.isNotEmpty(rmEntity.getSamplePath())) {
                                        Bitmap bitmap = BitmapUtils.getBitmap(Utils.getImageDirectory() + rmEntity.getSamplePath());
                                        String fileName = rmEntity.getSamplePath();
                                        // 制作上传图片的Map
                                        if (ConverterUtil.isNotEmpty(bitmap)) {
                                            imageMap.put(fileName, Base64.encodeToString(Utils.getPhotoBytes(bitmap), Base64.DEFAULT));
                                        }
                                    }
                                }
                                if(imageMap.isEmpty()){
                                    try {
                                        // Web提交抽样单
                                        progressDialog.setMessage(getResources().getText(R.string.msg_commit_sample));
                                        TaskHelper.saveSample(thisContext, webTaskListener, sampleInfoEntity, sampleInfoEntity.getTemplete());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Utils.showToast(thisContext, "提交失败");
                                        Utils.dismissDialog(progressDialog);
                                    }
                                    return;
                                }
                                progressDialog.setMessage(MessageFormat.format(ConverterUtil.toString(getResources().getText(R.string.msg_uploading_img)), imageMap.keySet().size()));
                                // 利用父类的共同方法上传样品图片
                                TaskHelper.saveSampleImage(FillLiveStockSampleAct.this, webTaskListener, imageMap);
                            }
                        } else {
                            mPosition = -1;
                            Utils.dismissDialog(progressDialog);
                            Utils.showToast(FillLiveStockSampleAct.this, message);
                        }
                    }
                });
            }
        }
    };
}
