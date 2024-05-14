package hd.produce.security.cn;

import hd.produce.security.cn.adapter.SelectAdapter;
import hd.produce.security.cn.data.BarCodeInfo;
import hd.produce.security.cn.data.FreshMilkEntity;
import hd.produce.security.cn.data.MonitoringSiteEntity;
import hd.produce.security.cn.entity.Sprinner;
import hd.produce.security.cn.ui.common.SampleActivity;
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
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
 * @author Administrator 生鲜乳抽样单填写提交界面
 */
public class FillRawMilkSampleActivity extends SampleActivity implements ITaskListener {
    // 产品认证情况下拉框
    List<Sprinner> sampleTypeList = new ArrayList<Sprinner>();

    // 条码
    private EditText barCode;
    // 获取条码按钮
    private TextView getBarCode;
    // 抽样数量
    private EditText sampleCount;
    // 抽样基数
    private EditText cardnalNum;
    // 样品图片
    private ImageView image;
    // 抽样地点
    private EditText samplingAddress;
    // 抽样人员
    private EditText samplingPersons;
    // 抽样类型
    private Spinner sampleType;

    // 生鲜乳收购许可证
    // 有
    private CheckBox permitHaveCheck;
    // 无
    private CheckBox permitNoCheck;
    // 许可证号
    private EditText permitNum;
    // 备注
    private EditText permitMark;

    // 生鲜乳准运证
    // 有
    private CheckBox transportHaveCheck;
    // 无
    private CheckBox transportNoCheck;
    // 准运证号
    private EditText transportNum;
    // 备注
    private EditText transportMark;

    // 生鲜乳交接单
    // 有
    private CheckBox eirHaveCheck;
    // 无
    private CheckBox eirNoCheck;
    // 交奶去向
    private EditText eirWherebouts;
    // 备注
    private EditText eirMark;

    // 受检单位情况
    // 通讯地址
    private EditText subjectAddress;
    // 邮编
    private EditText subjectZipCode;

    // 法人信息
    // 法人
    private EditText legal;
    // 电话
    private EditText legalTel;

    // 受检人信息
    // 受检人
    private EditText subjectPersion;
    // 电话
    private EditText subjectTel;

    // 联系人信息
    // 联系人
    private EditText contact;
    // 电话
    private EditText contactTel;

    private CommitListener commitListener;

    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_fill_raw_milk_sample);

        // 初始化值
        getIntentValues();
        // 初始化产品认证情况下拉框
        sampleTypeList = getSampleTypeList();
        initViews();
        setListener();

        String intentKey = coordinate.getEditableText().toString();
        if (null == intentKey || "".equals(intentKey)) {
            startLocation();
        }

        TaskHelper.getLocalTask(this, selectListener, DataManager.TASK_GET_MONITOR_LINK_LIST, null);
        TaskHelper.getLocalTask(this, selectListener, DataManager.TASK_GET_CITY_LIST, null);
        TaskHelper.getLocalTask(this, selectListener, DataManager.TASK_GET_BREED_LIST, null);
    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title_text);
        title.setText(getResources().getString(R.string.lab_fresh_milk));
        commitText = (TextView) findViewById(R.id.commit);
        save = (TextView) findViewById(R.id.save_sample_text);

        // 条码
        barCode = (EditText) findViewById(R.id.barcode_edit);
        // 获取条码按钮
        getBarCode = (TextView) findViewById(R.id.barcode_text);
        // 抽样量
        sampleCount = (EditText) findViewById(R.id.sample_num_edit);
        // 抽样基数
        cardnalNum = (EditText) findViewById(R.id.cardinal_num_edit);
        // 样品图片
        image = (ImageView) findViewById(R.id.image);
        // 经纬度
        coordinate = (EditText) findViewById(R.id.coordinate_edit);
        // 获取经纬度按钮
        get_coordinate = (TextView) findViewById(R.id.get_coordinate);
        // 样品名称下拉框
        sampleNameSpinner = (Spinner) findViewById(R.id.sample_name_spinner);
        sampleNameSpinner.setEnabled(false);
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
        sampleNameBar = (ProgressBar) findViewById(R.id.breed_bar);
        // 输入按钮
        autoComplete = (TextView) findViewById(R.id.do_auto_complete);
        // 抽样日期
        sampleDate = (TextView) findViewById(R.id.sample_date_edit);
        // 抽样场所
        // 抽样场所下拉框
        monitorLinkSpinner = (Spinner) findViewById(R.id.sample_sites_spinner);
        monitorLinkSpinner.setEnabled(false);
        monitorLinkBar = (ProgressBar) findViewById(R.id.sample_sites_bar);
        // 地市下拉框
        citySpinner = (Spinner) findViewById(R.id.sample_city_spinner);
        // 区县下拉框
        countrySpinner = (Spinner) findViewById(R.id.sample_area_spinner);
        countrySpinner.setEnabled(false);
        countryBar = (ProgressBar) findViewById(R.id.country_bar);
        // 抽样地址
        samplingAddress = (EditText) findViewById(R.id.sample_address_layout_detail_edit);
        // 抽样人员
        samplingPersons = (EditText) findViewById(R.id.sample_user_edit);
        // 抽样类型
        sampleType = (Spinner) findViewById(R.id.sample_type_spinner);
        SelectAdapter sampleTypeAdapter = new SelectAdapter(this, sampleTypeList);
        sampleType.setAdapter(sampleTypeAdapter);

        // 生鲜乳收购许可证
        // 有
        permitHaveCheck = (CheckBox) findViewById(R.id.purchase_permit_have_check);
        // 无
        permitNoCheck = (CheckBox) findViewById(R.id.purchase_permit_no_check);
        // 许可证号
        permitNum = (EditText) findViewById(R.id.purchase_permit_have_edit);
        // 备注
        permitMark = (EditText) findViewById(R.id.purchase_permit_no_edit);

        // 生鲜乳准运证
        // 有
        transportHaveCheck = (CheckBox) findViewById(R.id.transport_have_check);
        // 无
        transportNoCheck = (CheckBox) findViewById(R.id.transport_no_check);
        // 准运证号
        transportNum = (EditText) findViewById(R.id.transport_have_edit);
        // 备注
        transportMark = (EditText) findViewById(R.id.transport_no_edit);

        // 生鲜乳交接单
        // 有
        eirHaveCheck = (CheckBox) findViewById(R.id.eir_have_check);
        // 无
        eirNoCheck = (CheckBox) findViewById(R.id.eir_no_check);
        // 交奶去向
        eirWherebouts = (EditText) findViewById(R.id.wherebouts_edit);
        // 备注
        eirMark = (EditText) findViewById(R.id.eir_edit);

        // 受检单位情况
        // 单位名称
        subjectUnit = (TextView) findViewById(R.id.inspected_unit_edit);
        // 通讯地址
        subjectAddress = (EditText) findViewById(R.id.subjects_address_edit);
        // 邮编
        subjectZipCode = (EditText) findViewById(R.id.subjects_zip_code_edit);

        // 法人信息
        // 法人
        legal = (EditText) findViewById(R.id.subjects_legal_edit);
        // 电话
        legalTel = (EditText) findViewById(R.id.subjects_legal_tel_edit);

        // 受检人信息
        // 受检人
        subjectPersion = (EditText) findViewById(R.id.subjects_contact_edit);
        // 电话
        subjectTel = (EditText) findViewById(R.id.subjects_tel_edit);

        // 联系人信息
        // 联系人
        contact = (EditText) findViewById(R.id.subjects_contact_contact_edit);
        // 电话
        contactTel = (EditText) findViewById(R.id.subjects_contact_tel_edit);

        // 提交显示层
        choose_layout = (LinearLayout) findViewById(R.id.choose_layout);

        FreshMilkEntity fmEntity;
        if (isAdd) {
            // 如果是新增，则创建一个空的子entity
            fmEntity = new FreshMilkEntity();
            // 设置子表ID
            fmEntity.setId(ConverterUtil.getUUID());
            // 设置样品ID(子表的sampleCode与主表相同)
            fmEntity.setSampleCode(sampleInfoEntity.getSampleCode());
            // 设置抽样单创建时间
            sampleInfoEntity.setSamplingTime(ConverterUtil.toString(System.currentTimeMillis()));
            // 设置样品ID
            sampleInfoEntity.setFreshMilkEntity(fmEntity);
        } else {
            fmEntity = sampleInfoEntity.getFreshMilkEntity();
            if (fmEntity == null) {
                fmEntity = new FreshMilkEntity();
                sampleInfoEntity.setFreshMilkEntity(fmEntity);
            }
            // 如果是已完成，则不可更改
            if (!isDoing) {
                barCode.setEnabled(false);
                getBarCode.setEnabled(false);
                sampleCount.setEnabled(false);
                cardnalNum.setEnabled(false);
                image.setEnabled(false);
                sampleNameSpinner.setEnabled(false);
                autoComplete.setEnabled(false);
                citySpinner.setEnabled(false);
                countrySpinner.setEnabled(false);
                samplingAddress.setEnabled(false);
                samplingPersons.setEnabled(false);
                sampleType.setEnabled(false);
                // 生鲜乳收购许可证
                permitHaveCheck.setEnabled(false);
                permitNoCheck.setEnabled(false);
                permitNum.setEnabled(false);
                permitMark.setEnabled(false);
                // 生鲜乳准运证
                transportHaveCheck.setEnabled(false);
                transportNoCheck.setEnabled(false);
                transportNum.setEnabled(false);
                transportMark.setEnabled(false);
                // 生鲜乳交接单
                eirHaveCheck.setEnabled(false);
                eirNoCheck.setEnabled(false);
                eirWherebouts.setEnabled(false);
                eirMark.setEnabled(false);
                // 受检单位情况
                subjectAddress.setEnabled(false);
                subjectZipCode.setEnabled(false);
                // 法人信息
                legal.setEnabled(false);
                legalTel.setEnabled(false);
                // 受检人信息
                subjectPersion.setEnabled(false);
                subjectTel.setEnabled(false);
                // 联系人信息
                contact.setEnabled(false);
                contactTel.setEnabled(false);
                choose_layout.setVisibility(View.GONE);
            }
        }
        // 条码
        barCode.setText(sampleInfoEntity.getdCode());
        // 抽样量
        sampleCount.setText(fmEntity.getSamplingCount());
        // 抽样基数
        cardnalNum.setText(fmEntity.getSamplingBaseCount());
        // 样品图片
        if (ConverterUtil.isNotEmpty(sampleInfoEntity.getSamplePath())) {
            Bitmap cropBitmap = BitmapUtils.decodeFile(Utils.getImageDirectory() + sampleInfoEntity.getSamplePath());
            if (cropBitmap != null) {
                image.setImageBitmap(cropBitmap);
            }
        }
        // 经纬度
        if (null != sampleInfoEntity.getLongitude() && null != sampleInfoEntity.getLatitude()) {
            coordinate.setText(sampleInfoEntity.getLongitude() + "E" + "," + sampleInfoEntity.getLatitude() + "N");
        }
        // 抽样时间
        sampleDate.setText(sampleInfoEntity.getSamplingDate());
        // 抽样地点
        samplingAddress.setText(sampleInfoEntity.getSamplingAddress());
        // 抽样人员
        samplingPersons.setText(sampleInfoEntity.getSamplingPersons());
        // 抽样类型
        sampleType.setSelection(sampleTypeAdapter.getPosById(fmEntity.getType()));

        // 生鲜乳收购许可证
        if ("0".equals(fmEntity.getBuyLicence())) {
            permitNoCheck.setChecked(true);
        } else if ("1".equals(fmEntity.getBuyLicence())) {
            permitHaveCheck.setChecked(true);
        }
        permitNum.setText(fmEntity.getLicenceNo());
        permitMark.setText(fmEntity.getLicenceRemark());

        // 生鲜乳准运证
        if ("0".equals(fmEntity.getNavicert())) {
            transportNoCheck.setChecked(true);
        } else if ("1".equals(fmEntity.getNavicert())) {
            transportHaveCheck.setChecked(true);
        }
        transportNum.setText(fmEntity.getNavicertNo());
        transportMark.setText(fmEntity.getNavicertRemark());

        // 生鲜乳交接单
        if ("0".equals(fmEntity.getDeliveryReceitp())) {
            eirNoCheck.setChecked(true);
        } else if ("1".equals(fmEntity.getDeliveryReceitp())) {
            eirHaveCheck.setChecked(true);
        }
        eirWherebouts.setText(fmEntity.getDirection());
        eirMark.setText(fmEntity.getDeliveryReceitpRemark());

        // 受检单位情况
        subjectUnit.setText(sampleInfoEntity.getUnitFullname());
        subjectAddress.setText(sampleInfoEntity.getUnitAddress());
        subjectZipCode.setText(sampleInfoEntity.getZipCode());

        // 法人信息
        // 法人
        legal.setText(sampleInfoEntity.getLegalPerson());
        legalTel.setText(fmEntity.getTelphone());

        // 受检人信息
        subjectPersion.setText(fmEntity.getExaminee());
        subjectTel.setText(fmEntity.getTelphone2());

        // 联系人信息
        contact.setText(sampleInfoEntity.getContact());
        contactTel.setText(sampleInfoEntity.getTelphone());
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
        if (TextUtils.isEmpty(ConverterUtil.toString(barCode.getText()))) {
            Utils.showTips(this, "条码不能为空");
            return;
        }

        if (ConverterUtil.toNotNullString(barCode.getText()).length() != 13) {
            Utils.showTips(this, "请填写正确的条码");
            return;
        }
        Utils.showConfimDialog(FillRawMilkSampleActivity.this, FillRawMilkSampleActivity.this.getResources().getText(R.string.msg_confim_commit), new Utils.OnDialogDoneListener() {
            @Override
            public void onDialogDone() {
                onSaveClick();
            }
        });
    }

    private void checkDCode() {
        try {
            progressDialog.setMessage(MessageFormat.format(ConverterUtil.toString(getResources().getText(R.string.msg_check_dCode)), 1, 1));
            TaskHelper.checkDCode(this, commitListener, ConverterUtil.toString(barCode.getEditableText()), mMonitoringTaskEntity.getProjectCode());
        } catch (Exception e) {
            e.printStackTrace();
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
        TaskHelper.getLocalTask(FillRawMilkSampleActivity.this, FillRawMilkSampleActivity.this, DataManager.TASK_SAVE_SAMPLE, param);
    }

    @Override
    public void saveSampleInfoToEntity() {
        super.saveSampleInfoToEntity();
        FreshMilkEntity fmEntity = sampleInfoEntity.getFreshMilkEntity();
        // 条码
        sampleInfoEntity.setdCode(barCode.getEditableText().toString());
        // 抽样量
        fmEntity.setSamplingCount(ConverterUtil.toString(sampleCount.getEditableText()));
        // 抽样基数
        fmEntity.setSamplingBaseCount(ConverterUtil.toString(cardnalNum.getEditableText()));
        // 经度
        sampleInfoEntity.setLongitude(String.valueOf(logitude));
        // 纬度
        sampleInfoEntity.setLatitude(String.valueOf(latitude));
        // 样品名称
        sampleInfoEntity.setAgrCode(((Sprinner) sampleNameSpinner.getSelectedItem()).getId());
        sampleInfoEntity.setSampleName(((Sprinner) sampleNameSpinner.getSelectedItem()).getName());
        // 抽样时间
        sampleInfoEntity.setSamplingDate(ConverterUtil.toString(sampleDate.getText()));
        // 抽样场所
        sampleInfoEntity.setMonitoringLink(((Sprinner) monitorLinkSpinner.getSelectedItem()).getId());
        // 抽样地市
        sampleInfoEntity.setCityCode(((Sprinner) citySpinner.getSelectedItem()).getId());
        // 抽样区县
        sampleInfoEntity.setCountyCode(((Sprinner) countrySpinner.getSelectedItem()).getId());
        // 抽样地点
        sampleInfoEntity.setSamplingAddress(ConverterUtil.toString(samplingAddress.getEditableText()));
        // 抽样人员
        sampleInfoEntity.setSamplingPersons(ConverterUtil.toString(samplingPersons.getEditableText()));
        // 抽样类型
        fmEntity.setType(((Sprinner) sampleType.getSelectedItem()).getId());

        // 生鲜乳收购许可证
        if (permitNoCheck.isChecked()) {
            fmEntity.setBuyLicence("0");
        } else if (permitHaveCheck.isChecked()) {
            fmEntity.setBuyLicence("1");
        }
        fmEntity.setLicenceNo(ConverterUtil.toString(permitNum.getEditableText()));
        fmEntity.setLicenceRemark(ConverterUtil.toString(permitMark.getEditableText()));

        // 生鲜乳准运证
        if (transportNoCheck.isChecked()) {
            fmEntity.setNavicert("0");
        } else if (transportHaveCheck.isChecked()) {
            fmEntity.setNavicert("1");
        }
        fmEntity.setNavicertNo(ConverterUtil.toString(transportNum.getEditableText()));
        fmEntity.setNavicertRemark(ConverterUtil.toString(transportMark.getEditableText()));

        // 生鲜乳交接单
        if (eirNoCheck.isChecked()) {
            fmEntity.setDeliveryReceitp("0");
        } else if (eirHaveCheck.isChecked()) {
            fmEntity.setDeliveryReceitp("1");
        }
        fmEntity.setDirection(ConverterUtil.toString(eirWherebouts.getEditableText()));
        fmEntity.setDeliveryReceitpRemark(ConverterUtil.toString(eirMark.getEditableText()));

        // 受检单位情况
        sampleInfoEntity.setUnitFullname(ConverterUtil.toString(subjectUnit.getText()));
        sampleInfoEntity.setUnitAddress(ConverterUtil.toString(subjectAddress.getEditableText()));
        sampleInfoEntity.setZipCode(ConverterUtil.toString(subjectZipCode.getEditableText()));

        // 法人信息
        sampleInfoEntity.setLegalPerson(ConverterUtil.toString(legal.getEditableText()));
        fmEntity.setTelphone(ConverterUtil.toString(legalTel.getEditableText()));

        // 受检人信息
        fmEntity.setExaminee(ConverterUtil.toString(subjectPersion.getEditableText()));
        fmEntity.setTelphone2(ConverterUtil.toString(subjectTel.getEditableText()));

        // 联系人信息
        sampleInfoEntity.setContact(ConverterUtil.toString(contact.getEditableText()));
        sampleInfoEntity.setTelphone(ConverterUtil.toString(contactTel.getEditableText()));
    }

    final OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.commit:
                isCommit = true;
                onCommitClick();
                break;
            case R.id.barcode_text:
                sendIntentToBarCode();
                break;
            case R.id.image:
                sendIntentToCameraPicture();
                break;
            case R.id.save_sample_text:
                isBack = false;
                isCommit = false;
                onSaveClick();
                break;
            case R.id.subjects_unit_name_edit:
                sendIntentToMonitoringSite();
                break;
            default:
                break;
            }
        }
    };

    @Override
    public void onTaskFinish(int result, int request, final Object entity) {
        // 本地保存抽样单
        if (request == DataManager.TASK_SAVE_SAMPLE) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (ConverterUtil.toBoolean(entity)) {
                        if (isCommit) {
                            progressDialog = Utils.showProgress(FillRawMilkSampleActivity.this, null, FillRawMilkSampleActivity.this.getResources().getText(R.string.msg_commit_sample), false, false);
                            // 验证二维码
                            checkDCode();
                            return;
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isBack) {
                                    Utils.showTips(FillRawMilkSampleActivity.this, FillRawMilkSampleActivity.this.getResources().getString(R.string.msg_save_success));
                                    FillRawMilkSampleActivity.super.onBackPressed();
                                } else {
                                    Utils.showTips(FillRawMilkSampleActivity.this, FillRawMilkSampleActivity.this.getResources().getString(R.string.msg_save_success));
                                    setResult(Activity.RESULT_CANCELED);
                                    finish();
                                }
                            }
                        });
                    }
                }
            });
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
                subjectAddress.setText("");
            } else {
                subjectAddress.setText(entity.getAddress());
            }
            if (entity.getZipcode() == null) {
                subjectZipCode.setText("");
            } else {
                subjectZipCode.setText(entity.getZipcode());
            }
            if (entity.getLegalPerson() == null) {
                legal.setText("");
            } else {
                legal.setText(entity.getLegalPerson());
            }
            if (entity.getContact() == null) {
                legalTel.setText("");
            } else {
                legalTel.setText(entity.getContact());
            }
            if (entity.getContactPerson() == null) {
                contact.setText("");
            } else {
                contact.setText(entity.getContactPerson());
            }
            if (entity.getContact() == null) {
                contactTel.setText("");
            } else {
                contactTel.setText(entity.getContact());
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
        sampleInfoEntity.setSamplePath("/" + FileUtils.getFileName(imagePath));
        Bitmap cropBitmap = BitmapUtils.getBitmap(Utils.getImageDirectory() + sampleInfoEntity.getSamplePath());
        if (cropBitmap != null) {
            image.setImageBitmap(cropBitmap);
        }
    }

    @Override
    public void setListener() {
        super.setListener();
        commitText.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
        getBarCode.setOnClickListener(onClickListener);
        image.setOnClickListener(onClickListener);

        // 添加单选监听器
        OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch (buttonView.getId()) {
                    case R.id.purchase_permit_have_check:
                        permitNoCheck.setChecked(false);
                        break;
                    case R.id.purchase_permit_no_check:
                        permitHaveCheck.setChecked(false);
                        break;
                    case R.id.transport_have_check:
                        transportNoCheck.setChecked(false);
                        break;
                    case R.id.transport_no_check:
                        transportHaveCheck.setChecked(false);
                        break;
                    case R.id.eir_have_check:
                        eirNoCheck.setChecked(false);
                        break;
                    case R.id.eir_no_check:
                        eirHaveCheck.setChecked(false);
                        break;
                    default:
                        break;
                    }
                }

            }
        };

        permitHaveCheck.setOnCheckedChangeListener(onCheckedChangeListener);
        permitNoCheck.setOnCheckedChangeListener(onCheckedChangeListener);
        transportHaveCheck.setOnCheckedChangeListener(onCheckedChangeListener);
        transportNoCheck.setOnCheckedChangeListener(onCheckedChangeListener);
        eirHaveCheck.setOnCheckedChangeListener(onCheckedChangeListener);
        eirNoCheck.setOnCheckedChangeListener(onCheckedChangeListener);

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
     * 扫描二维码之后的返回结果
     */
    private void onBarCodeResult(Intent intent) {
        BarCodeInfo barCodeInfo = (BarCodeInfo) intent.getSerializableExtra(Constants.BARCODE_KEY);
        String barCodeContent = barCodeInfo.getDisplayContent();
        sampleInfoEntity.setdCode(barCodeContent);
        barCode.setText(barCodeContent);
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
                            Map<String, String> imageMap = new HashMap<String, String>();
                            if (ConverterUtil.isNotEmpty(sampleInfoEntity.getSamplePath())) {
                                Bitmap bitmap = BitmapUtils.getBitmap(Utils.getImageDirectory() + sampleInfoEntity.getSamplePath());
                                String fileName = sampleInfoEntity.getSamplePath();
                                // 制作上传图片的Map
                                if (ConverterUtil.isNotEmpty(bitmap)) {
                                    imageMap.put(fileName, Base64.encodeToString(Utils.getPhotoBytes(bitmap), Base64.DEFAULT));
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
                            TaskHelper.saveSampleImage(FillRawMilkSampleActivity.this, webTaskListener, imageMap);
                        } else {
                            Utils.dismissDialog(progressDialog);
                            Utils.showToast(FillRawMilkSampleActivity.this, message);
                        }
                    }
                });
            }
        }
    };
}
