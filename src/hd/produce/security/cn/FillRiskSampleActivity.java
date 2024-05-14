package hd.produce.security.cn;

import hd.produce.security.cn.adapter.SelectAdapter;
import hd.produce.security.cn.data.BarCodeInfo;
import hd.produce.security.cn.data.GeneralcheckEntity;
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
 * 风险抽样单填写提交界面
 * 
 * @author xudl(Wisea)
 * 
 */
public class FillRiskSampleActivity extends SampleActivity implements ITaskListener {
    // 产品认证情况下拉框
    List<Sprinner> signList = new ArrayList<Sprinner>();

    // 条码
    private EditText barCode;
    // 获取条码按钮
    private TextView getBarCode;
    // 等级
    private EditText sampleGrade;
    // 商标
    private EditText sampleLogo;
    // 样品图片
    private ImageView image;
    // 抽样地点
    private EditText samplingAddress;
    // 抽样人员
    private EditText samplingPersons;
    // 包装：有
    private CheckBox wrapperTrue;
    // 包装：无
    private CheckBox wrapperFalse;
    // 标识：有
    private CheckBox markTrue;
    // 标识：无
    private CheckBox markFalse;
    // 型号规格
    private EditText sampleMode;
    // 执行标准
    private EditText sampleStandard;
    // 生产日期
    private TextView productDate;
    // 产品认证情况
    private Spinner signSpinner;
    // 证书编号
    private EditText certNum;
    // 抽样数量
    private EditText sampleCount;
    // 抽样基数
    private EditText cardnalNum;

    /** 受检单位情况 */
    // 通讯地址
    private EditText subAddress;
    // 邮编
    private EditText subZipCode;
    // 法人
    private EditText subLegal;
    // 联系人
    private EditText subContact;
    // 电话
    private EditText subTel;
    // 传真
    private EditText subFax;

    /** 生产单位情况 */
    // 生产
    private CheckBox proCheck;
    // 进货
    private CheckBox purchaseCheck;
    // 单位名称
    private EditText proUnitName;
    // 通讯地址
    private EditText proAddress;
    // 邮编
    private EditText proZipCode;
    // 法人
    private EditText proLegal;
    // 联系人
    private EditText proContact;
    // 电话
    private EditText proTel;
    // 传真
    private EditText proFax;

    private CommitListener commitListener;

    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_fill_risk_sample);

        // 初始化值
        getIntentValues();
        // 初始化产品认证情况下拉框
        signList = getSignList();
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title_text);
        title.setText(getResources().getString(R.string.lab_general_check));
        commitText = (TextView) findViewById(R.id.commit);
        save = (TextView) findViewById(R.id.save_sample_text);

        // 条码
        barCode = (EditText) findViewById(R.id.barcode_edit);
        // 获取条码按钮
        getBarCode = (TextView) findViewById(R.id.barcode_text);
        // 等级
        sampleGrade = (EditText) this.findViewById(R.id.grade_edit);
        // 商标
        sampleLogo = (EditText) this.findViewById(R.id.logo_edit);
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
        // 包装：有
        wrapperTrue = (CheckBox) this.findViewById(R.id.wrapper_have_check);
        // 包装：无
        wrapperFalse = (CheckBox) this.findViewById(R.id.wrapper_none_check);
        // 标识：有
        markTrue = (CheckBox) this.findViewById(R.id.mark_have_check);
        // 标识：无
        markFalse = (CheckBox) this.findViewById(R.id.mark_none_check);
        // 型号规格
        sampleMode = (EditText) this.findViewById(R.id.mode_specification_edit);
        // 执行标准
        sampleStandard = (EditText) this.findViewById(R.id.standard_edit);
        // 生产日期
        productDate = (TextView) this.findViewById(R.id.production_date_edit);
        sampleDate = (TextView) findViewById(R.id.sample_date_edit);
        // 产品认证情况
        signSpinner = (Spinner) findViewById(R.id.production_certification_spinner);
        SelectAdapter signAdapter = new SelectAdapter(this, signList);
        signSpinner.setAdapter(signAdapter);
        signAdapter.notifyDataSetChanged();
        // 证书编号
        certNum = (EditText) this.findViewById(R.id.certificate_num_edit);
        // 抽样数量
        sampleCount = (EditText) this.findViewById(R.id.sample_count_edit);
        // 抽样基数
        cardnalNum = (EditText) this.findViewById(R.id.cardinal_num_edit);
        // 抽样场所下拉框
        monitorLinkSpinner = (Spinner) findViewById(R.id.sample_sites_spinner);
        monitorLinkSpinner.setEnabled(false);
        monitorLinkBar = (ProgressBar) findViewById(R.id.sample_sites_bar);

        // 受检单位情况
        // 单位名称
        subjectUnit = (TextView) this.findViewById(R.id.inspected_unit_edit);
        // 通讯地址
        subAddress = (EditText) this.findViewById(R.id.subjects_address_edit);
        // 邮编
        subZipCode = (EditText) this.findViewById(R.id.subjects_zip_code_edit);
        // 法人
        subLegal = (EditText) this.findViewById(R.id.subjects_legal_edit);
        // 联系人
        subContact = (EditText) this.findViewById(R.id.subjects_contact_edit);
        // 电话
        subTel = (EditText) this.findViewById(R.id.subjects_tel_edit);
        // 传真
        subFax = (EditText) this.findViewById(R.id.subjects_fax_edit);

        // 生产单位情况
        // 生产
        proCheck = (CheckBox) this.findViewById(R.id.produce_unit_check);
        // 进货
        purchaseCheck = (CheckBox) this.findViewById(R.id.purchase_check);
        // 单位名称
        proUnitName = (EditText) this.findViewById(R.id.unit_name_edit);
        // 通讯地址
        proAddress = (EditText) this.findViewById(R.id.produce_address_edit);
        // 邮编
        proZipCode = (EditText) this.findViewById(R.id.produce_zip_code_edit);
        // 法人
        proLegal = (EditText) this.findViewById(R.id.produce_legal_edit);
        // 联系人
        proContact = (EditText) this.findViewById(R.id.produce_contact_edit);
        // 电话
        proTel = (EditText) this.findViewById(R.id.produce_tel_edit);
        // 传真
        proFax = (EditText) this.findViewById(R.id.produce_fax_edit);

        // 提交显示层
        choose_layout = (LinearLayout) findViewById(R.id.choose_layout);
        GeneralcheckEntity generalcheckEntity;
        if (isAdd) {
            // 如果是新增，则创建一个空的子entity
            generalcheckEntity = new GeneralcheckEntity();
            // 设置子表ID
            generalcheckEntity.setId(ConverterUtil.getUUID());
            // 设置样品ID(子表的sampleCode与主表相同)
            generalcheckEntity.setSampleCode(sampleInfoEntity.getSampleCode());
            // 设置抽样单创建时间
            sampleInfoEntity.setSamplingTime(ConverterUtil.toString(System.currentTimeMillis()));
            sampleInfoEntity.setGeneralcheckEntity(generalcheckEntity);
        } else {
            generalcheckEntity = sampleInfoEntity.getGeneralcheckEntity();
            if (generalcheckEntity == null) {
                generalcheckEntity = new GeneralcheckEntity();
                sampleInfoEntity.setGeneralcheckEntity(generalcheckEntity);
            }
            // 如果是已完成，则不可更改
            if (!isDoing) {
                barCode.setEnabled(false);
                getBarCode.setEnabled(false);
                sampleGrade.setEnabled(false);
                sampleLogo.setEnabled(false);
                image.setEnabled(false);
                coordinate.setEnabled(false);
                get_coordinate.setEnabled(false);
                sampleNameSpinner.setEnabled(false);
                autoComplete.setEnabled(false);
                citySpinner.setEnabled(false);
                countrySpinner.setEnabled(false);
                samplingAddress.setEnabled(false);
                samplingPersons.setEnabled(false);
                wrapperTrue.setEnabled(false);
                wrapperFalse.setEnabled(false);
                markTrue.setEnabled(false);
                markFalse.setEnabled(false);
                sampleMode.setEnabled(false);
                sampleStandard.setEnabled(false);
                productDate.setEnabled(false);
                signSpinner.setEnabled(false);
                certNum.setEnabled(false);
                sampleCount.setEnabled(false);
                cardnalNum.setEnabled(false);
                subjectUnit.setEnabled(false);
                subAddress.setEnabled(false);
                subZipCode.setEnabled(false);
                subLegal.setEnabled(false);
                subContact.setEnabled(false);
                subTel.setEnabled(false);
                subFax.setEnabled(false);
                proCheck.setEnabled(false);
                purchaseCheck.setEnabled(false);
                proUnitName.setEnabled(false);
                proAddress.setEnabled(false);
                proZipCode.setEnabled(false);
                proLegal.setEnabled(false);
                proContact.setEnabled(false);
                proTel.setEnabled(false);
                proFax.setEnabled(false);
                choose_layout.setVisibility(View.GONE);
            }
        }
        // 条码
        barCode.setText(sampleInfoEntity.getdCode());
        // 等级
        sampleGrade.setText(generalcheckEntity.getGrade());
        // 商标
        sampleLogo.setText(generalcheckEntity.getTradeMark());
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
        // 抽样地点
        samplingAddress.setText(sampleInfoEntity.getSamplingAddress());
        // 抽样人员
        samplingPersons.setText(sampleInfoEntity.getSamplingPersons());
        // 包装
        if ("1".equals(generalcheckEntity.getPack())) {
            wrapperTrue.setChecked(true);
        } else if ("0".equals(generalcheckEntity.getPack())) {
            wrapperFalse.setChecked(true);
        }
        // 标识
        if ("1".equals(generalcheckEntity.getFlag())) {
            markTrue.setChecked(true);
        } else if ("0".equals(generalcheckEntity.getFlag())) {
            markFalse.setChecked(true);
        }
        // 规格型号
        sampleMode.setText(generalcheckEntity.getSpecifications());
        // 执行标准
        sampleStandard.setText(generalcheckEntity.getExecStandard());
        // 生产日期
        productDate.setText(generalcheckEntity.getBatchNumber());
        // 抽样时间
        sampleDate.setText(sampleInfoEntity.getSamplingDate());
        // 产品认证情况
        signSpinner.setSelection(signAdapter.getPosById(generalcheckEntity.getProductCer()));
        // 证书编号
        certNum.setText(generalcheckEntity.getProductCerNo());
        // 抽样数量
        sampleCount.setText(generalcheckEntity.getSamplingCount());
        // 抽样基数
        cardnalNum.setText(generalcheckEntity.getSamplingCardinal());

        // 受检单位情况
        // 单位名称
        subjectUnit.setText(sampleInfoEntity.getUnitFullname());
        // 通讯地址
        subAddress.setText(sampleInfoEntity.getUnitAddress());
        // 邮编
        subZipCode.setText(sampleInfoEntity.getZipCode());
        // 法人
        subLegal.setText(sampleInfoEntity.getLegalPerson());
        // 联系人
        subContact.setText(sampleInfoEntity.getContact());
        // 电话
        subTel.setText(sampleInfoEntity.getTelphone());
        // 传真
        subFax.setText(sampleInfoEntity.getFax());

        // 生产单位情况
        String proPro = generalcheckEntity.getUnitProperties();
        if ("0".equals(proPro)) {
            // 生产
            proCheck.setChecked(true);
        } else if ("1".equals(proPro)) {
            // 进货
            purchaseCheck.setChecked(true);
        }
        // 单位名称
        proUnitName.setText(generalcheckEntity.getUnitName());
        // 通讯地址
        proAddress.setText(generalcheckEntity.getUnitAddress());
        // 邮编
        proZipCode.setText(generalcheckEntity.getZipCode());
        // 法人
        proLegal.setText(generalcheckEntity.getLegalPerson());
        // 联系人
        proContact.setText(generalcheckEntity.getContacts());
        // 电话
        proTel.setText(generalcheckEntity.getTelphone2());
        // 传真
        proFax.setText(generalcheckEntity.getFax2());
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
        Utils.showConfimDialog(FillRiskSampleActivity.this, FillRiskSampleActivity.this.getResources().getText(R.string.msg_confim_commit), new Utils.OnDialogDoneListener() {
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
        TaskHelper.getLocalTask(FillRiskSampleActivity.this, FillRiskSampleActivity.this, DataManager.TASK_SAVE_SAMPLE, param);
    }

    @Override
    public void saveSampleInfoToEntity() {
        super.saveSampleInfoToEntity();
        GeneralcheckEntity generalcheckEntity = sampleInfoEntity.getGeneralcheckEntity();
        // 条码
        sampleInfoEntity.setdCode(barCode.getEditableText().toString());
        // 等级
        generalcheckEntity.setGrade(sampleGrade.getEditableText().toString());
        // 商标
        generalcheckEntity.setTradeMark(sampleLogo.getEditableText().toString());
        // 经度
        sampleInfoEntity.setLongitude(String.valueOf(logitude));
        // 纬度
        sampleInfoEntity.setLatitude(String.valueOf(latitude));
        // 样品名称
        sampleInfoEntity.setAgrCode(((Sprinner) sampleNameSpinner.getSelectedItem()).getId());
        sampleInfoEntity.setSampleName(((Sprinner) sampleNameSpinner.getSelectedItem()).getName());
        // 抽样地市
        sampleInfoEntity.setCityCode(((Sprinner) citySpinner.getSelectedItem()).getId());
        // 抽样区县
        sampleInfoEntity.setCountyCode(((Sprinner) countrySpinner.getSelectedItem()).getId());
        // 抽样地点
        sampleInfoEntity.setSamplingAddress(ConverterUtil.toString(samplingAddress.getEditableText()));
        // 抽样人员
        sampleInfoEntity.setSamplingPersons(ConverterUtil.toString(samplingPersons.getEditableText()));
        // 包装
        if (wrapperTrue.isChecked()) {
            generalcheckEntity.setPack("1");
        } else if (wrapperFalse.isChecked()) {
            generalcheckEntity.setPack("0");
        }
        // 标识
        if (markTrue.isChecked()) {
            generalcheckEntity.setFlag("1");
        } else if (markFalse.isChecked()) {
            generalcheckEntity.setFlag("0");
        }
        // 规格型号
        generalcheckEntity.setSpecifications(ConverterUtil.toString(sampleMode.getText()));
        // 执行标准
        generalcheckEntity.setExecStandard(ConverterUtil.toString(sampleStandard.getText()));
        // 生产日期
        generalcheckEntity.setBatchNumber(ConverterUtil.toString(productDate.getText()));
        // 抽样时间
        sampleInfoEntity.setSamplingDate(ConverterUtil.toString(sampleDate.getText()));
        // 产品认证情况
        generalcheckEntity.setProductCer(((Sprinner) signSpinner.getSelectedItem()).getId());
        // 证书编号
        generalcheckEntity.setProductCerNo(ConverterUtil.toString(certNum.getText()));
        // 抽样数量
        generalcheckEntity.setSamplingCount(ConverterUtil.toString(sampleCount.getText()));
        // 抽样基数
        generalcheckEntity.setSamplingCardinal(ConverterUtil.toString(cardnalNum.getText()));
        // 抽样场所
        sampleInfoEntity.setMonitoringLink(((Sprinner) monitorLinkSpinner.getSelectedItem()).getId());

        // 受检单位情况
        // 单位名称
        sampleInfoEntity.setUnitFullname(ConverterUtil.toString(subjectUnit.getText()));
        // 通讯地址
        sampleInfoEntity.setUnitAddress(subAddress.getEditableText().toString());
        // 邮编
        sampleInfoEntity.setZipCode(subZipCode.getEditableText().toString());
        // 法人
        sampleInfoEntity.setLegalPerson(subLegal.getEditableText().toString().trim());
        // 联系人
        sampleInfoEntity.setContact(subContact.getEditableText().toString().trim());
        // 电话
        sampleInfoEntity.setTelphone(subTel.getEditableText().toString());
        // 传真
        sampleInfoEntity.setFax(subFax.getEditableText().toString());

        // 生产单位情况
        String proPro = "";
        if (proCheck.isChecked()) {
            // 生产
            proPro = "0";
        } else if (purchaseCheck.isChecked()) {
            // 进货
            proPro = "1";
        }
        generalcheckEntity.setUnitProperties(proPro);

        // 生产单位名称
        generalcheckEntity.setUnitName(proUnitName.getEditableText().toString());

        // 生产单位地址
        generalcheckEntity.setUnitAddress(proAddress.getEditableText().toString());
        // 生产单位邮编
        generalcheckEntity.setZipCode(proZipCode.getEditableText().toString());
        // 生产单位法人
        generalcheckEntity.setLegalPerson(proLegal.getEditableText().toString());
        // 生产单位联系人
        generalcheckEntity.setContacts(proContact.getEditableText().toString());
        // 生产单位电话
        generalcheckEntity.setTelphone2(proTel.getEditableText().toString());
        // 生产单位传真
        generalcheckEntity.setFax2(proFax.getEditableText().toString());
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {

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
            case R.id.production_date_edit:
                Utils.showTimePickerDialog(thisContext, productDate, ConverterUtil.toString(productDate.getText()));
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
                            progressDialog = Utils.showProgress(FillRiskSampleActivity.this, null, FillRiskSampleActivity.this.getResources().getText(R.string.msg_commit_sample), false, false);
                            // 验证二维码
                            checkDCode();
                            return;
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isBack) {
                                    Utils.showTips(FillRiskSampleActivity.this, FillRiskSampleActivity.this.getResources().getString(R.string.msg_save_success));
                                    FillRiskSampleActivity.super.onBackPressed();
                                } else {
                                    Utils.showTips(FillRiskSampleActivity.this, FillRiskSampleActivity.this.getResources().getString(R.string.msg_save_success));
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
                subAddress.setText("");
            } else {
                subAddress.setText(entity.getAddress());
            }
            if (entity.getZipcode() == null) {
                subZipCode.setText("");
            } else {
                subZipCode.setText(entity.getZipcode());
            }
            if (entity.getLegalPerson() == null) {
                subLegal.setText("");
            } else {
                subLegal.setText(entity.getLegalPerson());
            }
            if (entity.getContactPerson() == null) {
                subContact.setText("");
            } else {
                subContact.setText(entity.getContactPerson());
            }
            if (entity.getContact() == null) {
                subTel.setText("");
            } else {
                subTel.setText(entity.getContact());
            }
            if (entity.getFax() == null) {
                subFax.setText("");
            } else {
                subFax.setText(entity.getFax());
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
        productDate.setOnClickListener(onClickListener);

        // 添加单选监听器
        OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch (buttonView.getId()) {
                    case R.id.mark_have_check:
                        markFalse.setChecked(false);
                        break;
                    case R.id.mark_none_check:
                        markTrue.setChecked(false);
                        break;
                    case R.id.wrapper_have_check:
                        wrapperFalse.setChecked(false);

                        break;
                    case R.id.wrapper_none_check:
                        wrapperTrue.setChecked(false);
                        break;
                    case R.id.produce_unit_check:
                        purchaseCheck.setChecked(false);
                        break;
                    case R.id.purchase_check:
                        proCheck.setChecked(false);
                        break;
                    default:
                        break;
                    }
                }
            }
        };
        wrapperTrue.setOnCheckedChangeListener(onCheckedChangeListener);
        wrapperFalse.setOnCheckedChangeListener(onCheckedChangeListener);
        markTrue.setOnCheckedChangeListener(onCheckedChangeListener);
        markFalse.setOnCheckedChangeListener(onCheckedChangeListener);
        proCheck.setOnCheckedChangeListener(onCheckedChangeListener);
        purchaseCheck.setOnCheckedChangeListener(onCheckedChangeListener);

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
                            TaskHelper.saveSampleImage(FillRiskSampleActivity.this, webTaskListener, imageMap);
                        } else {
                            Utils.dismissDialog(progressDialog);
                            Utils.showToast(FillRiskSampleActivity.this, message);
                        }
                    }
                });
            }
        }
    };
}