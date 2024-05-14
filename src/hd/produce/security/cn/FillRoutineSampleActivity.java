package hd.produce.security.cn;

import hd.produce.security.cn.adapter.SelectAdapter;
import hd.produce.security.cn.dao.CommonDao;
import hd.produce.security.cn.data.BarCodeInfo;
import hd.produce.security.cn.data.MonitoringSiteEntity;
import hd.produce.security.cn.data.RoutinemonitoringEntity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 例行检查抽样单界面
 * 
 * @author xudl(Wisea)
 * 
 */
public class FillRoutineSampleActivity extends SampleActivity implements ITaskListener {
    private int mPosition = -1;

    private EditText legal;
    // 通讯地址
    private EditText address;
    // 邮编
    private EditText zipCode;
    // 电话
    private EditText tel;
    // 抽样人员
    private EditText samplingPersons;

    // 任务来源
    private EditText taskOriginEdit;

    // 执行标准
    private EditText standardEdit;

    final Handler handler = new Handler();

    // 子表数据
    private List<RoutinemonitoringEntity> lists = new ArrayList<RoutinemonitoringEntity>();

    private TextView addSubTable;

    private SubListView listview;

    private SubTableAdapter subAdapter;

    private int currentSubTablePos = 0;

    // private String imagePath;

    private int mSubViewPosition = -1;

    private CommitListener commitListener;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_fill_routine_sample);

        // 初始化值
        getIntentValues();
        initViews();
        setListener();

        String intentKey = coordinate.getEditableText().toString();
        if (null == intentKey || "".equals(intentKey)) {
            startLocation();
        }

        TaskHelper.getLocalTask(this, selectListener, DataManager.TASK_GET_MONITOR_LINK_LIST, null);
        TaskHelper.getLocalTask(this, selectListener, DataManager.TASK_GET_CITY_LIST, null);
    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title_text);
        title.setText(getResources().getString(R.string.lab_routine_monitoring));
        commitText = (TextView) findViewById(R.id.commit);
        save = (TextView) findViewById(R.id.save_sample_text);

        // 受检单位
        subjectUnit = (TextView) findViewById(R.id.inspected_unit_edit);
        // 法人
        legal = (EditText) findViewById(R.id.legal_edit);
        // 通讯地址
        address = (EditText) findViewById(R.id.address_edit);
        // 邮编
        zipCode = (EditText) findViewById(R.id.zip_code_edit);
        // 联系电话
        tel = (EditText) findViewById(R.id.tel_edit);
        // 抽样时间
        sampleDate = (TextView) findViewById(R.id.sample_date_edit);
        // 抽样人员
        samplingPersons = (EditText) findViewById(R.id.sample_user_edit);
        // 经纬度
        coordinate = (EditText) findViewById(R.id.coordinate_edit);
        // 取得经纬度按钮
        get_coordinate = (TextView) findViewById(R.id.get_coordinate);
        // 监测环节下拉框
        monitorLinkSpinner = (Spinner) findViewById(R.id.monitor_link_spinner);
        monitorLinkSpinner.setEnabled(false);
        monitorLinkBar = (ProgressBar) findViewById(R.id.monitor_link_bar);
        // 地市下拉框
        citySpinner = (Spinner) findViewById(R.id.sample_city_spinner);
        countrySpinner = (Spinner) findViewById(R.id.sample_area_spinner);
        countrySpinner.setEnabled(false);
        countryBar = (ProgressBar) findViewById(R.id.country_bar);
        // 添加子表按钮
        addSubTable = (TextView) findViewById(R.id.add_sub_table);
        listview = (SubListView) findViewById(R.id.listview);
        // 任务来源
        taskOriginEdit = (EditText) findViewById(R.id.task_origin_edit);
        // 执行标准
        standardEdit = (EditText) findViewById(R.id.standard_edit);
        // 提交显示层
        choose_layout = (LinearLayout) findViewById(R.id.choose_layout);
        if (isAdd) {
            // 如果是新增，则创建一个空的子entity
            RoutinemonitoringEntity rme = new RoutinemonitoringEntity();
            rme.setId(ConverterUtil.getUUID());
            // 设置抽样单ID
            rme.setSamplingMonadId(sampleInfoEntity.getSamplingMonadId());
            // 设置样品ID(第一个子表的sampleCode与主表相同)
            rme.setSampleCode(sampleInfoEntity.getSampleCode());
            // 任务ID
            rme.setTaskCode(mMonitoringTaskEntity.getTaskcode());
            // 抽样单创建时间
            rme.setSamplingTime(ConverterUtil.toString(System.currentTimeMillis()));
            addSubTable(rme);
            return;
        } else {
            // 如果是已完成，则不可更改
            if (!isDoing) {
                subjectUnit.setEnabled(false);
                legal.setEnabled(false);
                address.setEnabled(false);
                zipCode.setEnabled(false);
                tel.setEnabled(false);
                sampleDate.setEnabled(false);
                samplingPersons.setEnabled(false);
                coordinate.setEnabled(false);
                get_coordinate.setEnabled(false);
                monitorLinkSpinner.setEnabled(false);
                citySpinner.setEnabled(false);
                countrySpinner.setEnabled(false);
                taskOriginEdit.setEnabled(false);
                standardEdit.setEnabled(false);
                addSubTable.setText(getResources().getString(R.string.view_sub_table));
                choose_layout.setVisibility(View.GONE);
            }
        }
        // 受检单位
        subjectUnit.setText(sampleInfoEntity.getUnitFullname());
        // 法人或负责人
        legal.setText(sampleInfoEntity.getLegalPerson());
        // 通讯地址
        address.setText(sampleInfoEntity.getUnitAddress());
        // 邮编
        zipCode.setText(sampleInfoEntity.getZipCode());
        // 联系电话
        tel.setText(sampleInfoEntity.getTelphone());
        // 抽样时间
        sampleDate.setText(sampleInfoEntity.getSamplingDate());
        // 抽样人员
        samplingPersons.setText(sampleInfoEntity.getSamplingPersons());
        // 经纬度
        if (null != sampleInfoEntity.getLongitude() && null != sampleInfoEntity.getLatitude()) {
            coordinate.setText(sampleInfoEntity.getLongitude() + "E" + "," + sampleInfoEntity.getLatitude() + "N");
        }
        // 监测环节\地市\区县 的回显在调用Task里面

        if (ConverterUtil.isNotEmpty(sampleInfoEntity.getRoutinemonitoringList())) {
            // 任务来源
            taskOriginEdit.setText(sampleInfoEntity.getRoutinemonitoringList().get(0).getTaskSource());
            // 执行标准
            standardEdit.setText(sampleInfoEntity.getRoutinemonitoringList().get(0).getExecStanderd());
        }
        addBatchSubTable(sampleInfoEntity.getRoutinemonitoringList());

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

        commitListener = new CommitListener();
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
        for (RoutinemonitoringEntity rme : lists) {
            if (TextUtils.isEmpty(rme.getdCode())) {
                Utils.showTips(this, "条码不能为空");
                return;
            }

            if (rme.getdCode().length() != 13) {
                Utils.showTips(this, "请填写正确的条码");
                return;
            }
            for (RoutinemonitoringEntity temp : lists) {
                // 自己则跳过
                if(rme.getId().equals(temp.getId())){
                    continue;
                }
                if (rme.getdCode().equals(temp.getdCode())) {
                    Utils.showTips(this, "条码不能重复");
                    return;
                }
            }
        }
        Utils.showConfimDialog(FillRoutineSampleActivity.this, FillRoutineSampleActivity.this.getResources().getText(R.string.msg_confim_commit), new Utils.OnDialogDoneListener() {
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
        // try {
        // if(!setCoordation()){
        // return;
        // }
        if (lists.size() <= 0) {
            Utils.showTips(this, ConverterUtil.toString(getResources().getText(R.string.msg_least_one_sub)));
            return;
        }
        setCoordation();
        saveSampleInfoToEntity();
        Map<String, Object> param = new HashMap<String, Object>();
        if (ConverterUtil.isEmpty(sampleInfoEntity.getTemplete())) {
            sampleInfoEntity.setTemplete(mMonitoringProjectEntity.getTemplete());
        }
        param.put("sampleInfo", sampleInfoEntity);
        TaskHelper.getLocalTask(FillRoutineSampleActivity.this, FillRoutineSampleActivity.this, DataManager.TASK_SAVE_SAMPLE, param);
    }

    private void addSubTable(RoutinemonitoringEntity rme) {
        if (lists.size() <= 0) {
            subAdapter = new SubTableAdapter(lists);
            listview.setAdapter(subAdapter);
        } else if(lists.size() >= 15){
            Utils.showTips(this, ConverterUtil.toString(getResources().getText(R.string.msg_morest_15_sub)));
            return;
        }
        // RoutinemonitoringEntity rme = new RoutinemonitoringEntity();
        lists.add(rme);
        subAdapter.notifyDataSetChanged();
    }

    private void addBatchSubTable(List<RoutinemonitoringEntity> rmes) {
        if (lists.size() <= 0) {
            subAdapter = new SubTableAdapter(lists);
            listview.setAdapter(subAdapter);
        }
        // RoutinemonitoringEntity rme = new RoutinemonitoringEntity();
        if (rmes == null) {
            return;
        }
        for (RoutinemonitoringEntity rme : rmes) {
            // 任务ID
            rme.setTaskCode(mMonitoringTaskEntity.getTaskcode());
            lists.add(rme);
        }
        subAdapter.notifyDataSetChanged();
    }

    @Override
    public void saveSampleInfoToEntity() {
        super.saveSampleInfoToEntity();
        // 受检单位
        sampleInfoEntity.setUnitFullname(ConverterUtil.toString(subjectUnit.getText()));
        // 法人
        sampleInfoEntity.setLegalPerson(ConverterUtil.toString(legal.getEditableText()));
        // 通讯地址
        sampleInfoEntity.setUnitAddress(ConverterUtil.toString(address.getEditableText()));
        // 邮编
        sampleInfoEntity.setZipCode(ConverterUtil.toString(zipCode.getEditableText()));
        // 联系电话
        sampleInfoEntity.setTelphone(ConverterUtil.toString(tel.getEditableText()));
        // 抽样时间
        sampleInfoEntity.setSamplingDate(ConverterUtil.toString(sampleDate.getText()));
        // 抽样人员
        sampleInfoEntity.setSamplingPersons(ConverterUtil.toString(samplingPersons.getEditableText()));
        // 经度
        sampleInfoEntity.setLongitude(String.valueOf(logitude));
        // 纬度
        sampleInfoEntity.setLatitude(String.valueOf(latitude));
        // 监测环节
        sampleInfoEntity.setMonitoringLink(((Sprinner) monitorLinkSpinner.getSelectedItem()).getId());
        // 抽样地市
        sampleInfoEntity.setCityCode(((Sprinner) citySpinner.getSelectedItem()).getId());
        // 抽样区县
        sampleInfoEntity.setCountyCode(((Sprinner) countrySpinner.getSelectedItem()).getId());
        // sampleInfoEntity.setDCode(barCodeContent);
        for (RoutinemonitoringEntity rme : lists) {
            // 任务来源
            rme.setTaskSource(taskOriginEdit.getText().toString());
            // 执行标准
            rme.setExecStanderd(standardEdit.getText().toString());
        }
        sampleInfoEntity.setRoutinemonitoringList(lists);

    }

    final OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

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
                RoutinemonitoringEntity rme = new RoutinemonitoringEntity();
                // 复制上一个表的数据
                if (lists != null && !lists.isEmpty()) {
                    RoutinemonitoringEntity r = lists.get(lists.size() - 1);
                    rme.setSampleSource(r.getSampleSource());
                    rme.setSampleCount(r.getSampleCount());
                    rme.setTaskSource(r.getTaskSource());
                    rme.setExecStanderd(r.getExecStanderd());
                    rme.setRemark(r.getRemark());
                    //rme.setSamplePath(r.getSamplePath());
                    rme.setSamplingAddress(r.getSamplingAddress());
                    rme.setSamplingMonadId(r.getSamplingMonadId());
                } else {
                    rme.setSamplingMonadId(sampleInfoEntity.getSamplingMonadId());
                }
                rme.setId(ConverterUtil.getUUID());
                rme.setTaskCode(mMonitoringTaskEntity.getTaskcode());
                // 设置样品ID
                rme.setSampleCode(ConverterUtil.getUUID());
                rme.setSamplingTime(ConverterUtil.toString(System.currentTimeMillis()));

                addSubTable(rme);
                break;
            case R.id.inspected_unit_edit:
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
                            progressDialog = Utils.showProgress(FillRoutineSampleActivity.this, null, getResources().getText(R.string.msg_commit_sample), false, false);
                            // 验证二维码
                            mPosition = 0;
                            checkDCode();
                            return;
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isBack) {
                                    Utils.showTips(FillRoutineSampleActivity.this, FillRoutineSampleActivity.this.getResources().getString(R.string.msg_save_success));
                                    FillRoutineSampleActivity.super.onBackPressed();
                                } else {
                                    Utils.showTips(FillRoutineSampleActivity.this, FillRoutineSampleActivity.this.getResources().getString(R.string.msg_save_success));
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
                        List<RoutinemonitoringEntity> delList = new ArrayList<RoutinemonitoringEntity>();
                        delList.addAll(lists);
                        subAdapter.update(delList);
                    }
                }
            });
        }
    }

    class SubTableAdapter extends BaseAdapter {
        private List<RoutinemonitoringEntity> mList;
        private List<Sprinner> breedList;

        public SubTableAdapter(List<RoutinemonitoringEntity> list) {
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
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public void update(List<RoutinemonitoringEntity> list) {
            mList = list;
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(FillRoutineSampleActivity.this).inflate(R.layout.fill_routine_sample_item_layout, null);

            TextView index = (TextView) convertView.findViewById(R.id.barcode_layout_index_tv);
            index.setText("表" + String.valueOf(position + 1));
            TextView del = (TextView) convertView.findViewById(R.id.del);

            del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Utils.showConfimDialog(FillRoutineSampleActivity.this, FillRoutineSampleActivity.this.getResources().getText(R.string.msg_confim_delete), new Utils.OnDialogDoneListener() {
                        @Override
                        public void onDialogDone() {
                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("sampleCode", lists.get(position).getSampleCode());
                            params.put("templete", CommonDao.TB_ROUTINE_MONITORING);
                            params.put("position", position);
                            TaskHelper.getLocalTask(FillRoutineSampleActivity.this, FillRoutineSampleActivity.this, DataManager.TASK_DELETE_SAMPLE, params);
                        }
                    });
                }
            });

            final String selectCode = lists.get(position).getAgrCode();
            final Spinner sampleName = (Spinner) convertView.findViewById(R.id.sample_name_spinner);
            sampleName.setEnabled(false);
            sampleName.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                    Sprinner sprinner = (Sprinner) sampleName.getAdapter().getItem(pos);
                    lists.get(position).setAgrCode(sprinner.getId());
                    lists.get(position).setSampleName(sprinner.getName());
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // do nothing
                }
            });
            final ProgressBar breed_bar = (ProgressBar) convertView.findViewById(R.id.breed_bar);
            final TextView autoComplete = (TextView) convertView.findViewById(R.id.do_auto_complete);
            autoComplete.setEnabled(false);
            autoComplete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mSubViewPosition = position;
                    String name = lists.get(position).getSampleName();
                    sendIntentToBreedList(name);
                }

            });

            TextView sampleTask = (TextView) convertView.findViewById(R.id.sample_task_edit);
            sampleTask.setText(mMonitoringTaskEntity.getTaskname());
            // lists.get(position).sets
            TextView getBarCode = (TextView) convertView.findViewById(R.id.barcode_text);
            getBarCode.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    currentSubTablePos = position;
                    sendIntentToBarCode();
                }
            });
            EditText barCode = (EditText) convertView.findViewById(R.id.barcode_edit);

            if(ConverterUtil.isNotEmpty(lists.get(position).getdCode())){
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

            ImageView image = (ImageView) convertView.findViewById(R.id.image);
            image.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    currentSubTablePos = position;
                    sendIntentToCameraPicture();
                }
            });
            String imagePath = lists.get(position).getSamplePath();
            if (ConverterUtil.isNotEmpty(imagePath)) {
                Bitmap bitmap = BitmapUtils.decodeFile(Utils.getImageDirectory() + imagePath);
                if (bitmap != null) {
                    image.setImageBitmap(bitmap);
                }
            }

            EditText sampleSource = (EditText) convertView.findViewById(R.id.sample_origin_edit);
            sampleSource.setText(lists.get(position).getSampleSource());
            sampleSource.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    lists.get(position).setSampleSource(s.toString());
                }
            });
            EditText sampleNum = (EditText) convertView.findViewById(R.id.sample_num_edit);
            String sampleNumStr = String.valueOf(lists.get(position).getSampleCount());
            if (TextUtils.isEmpty(sampleNumStr) || sampleNumStr.equals("null")) {
                sampleNum.setText("");
            } else {
                sampleNum.setText(sampleNumStr);
            }

            sampleNum.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        lists.get(position).setSampleCount(s.toString());
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            EditText remark = (EditText) convertView.findViewById(R.id.mark_edit);
            remark.setText(lists.get(position).getRemark());
            remark.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    lists.get(position).setRemark(s.toString());
                }
            });
            // 抽样详细地址
            EditText address = (EditText) convertView.findViewById(R.id.address_layout_detail_edit);
            address.setText(lists.get(position).getSamplingAddress());
            address.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    lists.get(position).setSamplingAddress(s.toString());
                }
            });

            if (ConverterUtil.isEmpty(breedList)) {
                ITaskListener mListener = new ITaskListener() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onTaskFinish(int result, int request, final Object entity) {
                        if (request == DataManager.TASK_GET_BREED_LIST) {
                            handler.post(new Runnable() {
                                public void run() {
                                    // 取得抽样品种
                                    breedList = (List<Sprinner>) entity;
                                    // 设置抽样品种适配器
                                    SelectAdapter sampleNameAdapter = new SelectAdapter(FillRoutineSampleActivity.this, breedList);
                                    sampleName.setAdapter(sampleNameAdapter);
                                    // 设定默认选择
                                    if (ConverterUtil.isEmpty(selectCode)) {
                                        sampleName.setSelection(0);
                                    } else {
                                        sampleName.setSelection(sampleNameAdapter.getPosById(selectCode));
                                    }
                                    // 更新视图
                                    sampleNameAdapter.notifyDataSetChanged();
                                    // 更新画面
                                    breed_bar.setVisibility(View.GONE);
                                    if (isDoing) {
                                        sampleName.setEnabled(true);
                                        autoComplete.setEnabled(true);
                                    }
                                }
                            });
                        }

                    }
                };
                TaskHelper.getLocalTask(FillRoutineSampleActivity.this, mListener, DataManager.TASK_GET_BREED_LIST, null);
            } else {
                // 设置抽样品种适配器
                SelectAdapter sampleNameAdapter = new SelectAdapter(FillRoutineSampleActivity.this, breedList);
                sampleName.setAdapter(sampleNameAdapter);
                // 设定默认选择
                if (ConverterUtil.isEmpty(selectCode)) {
                    sampleName.setSelection(0);
                } else {
                    sampleName.setSelection(sampleNameAdapter.getPosById(selectCode));
                }
                // 更新视图
                sampleNameAdapter.notifyDataSetChanged();
                // 更新画面
                breed_bar.setVisibility(View.GONE);
                if (isDoing) {
                    sampleName.setEnabled(true);
                    autoComplete.setEnabled(true);
                }
            }
            if (!isDoing) {
                del.setVisibility(View.GONE);
                barCode.setEnabled(false);
                getBarCode.setEnabled(false);
                image.setEnabled(false);
                sampleSource.setEnabled(false);
                sampleNum.setEnabled(false);
                sampleName.setEnabled(false);
                autoComplete.setEnabled(false);
                remark.setEnabled(false);
                address.setEnabled(false);
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
                if (data == null || mSubViewPosition == -1)
                    return;
                Sprinner breadEntity = (Sprinner) data.getSerializableExtra("MonitoringBreedEntity");
                if (breadEntity != null) {
                    lists.get(mSubViewPosition).setAgrCode(breadEntity.getId());
                    lists.get(mSubViewPosition).setSampleName(breadEntity.getName());
                    subAdapter.notifyDataSetChanged();
                }
                mSubViewPosition = -1;
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
            if (entity.getLegalPerson() == null) {
                legal.setText("");
            } else {
                legal.setText(entity.getLegalPerson());
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
        lists.get(currentSubTablePos).setSamplePath("/" + FileUtils.getFileName(imagePath));
        subAdapter.notifyDataSetChanged();
    }

    /**
     * 扫描二维码之后的返回结果
     * 
     * @param intent
     */
    public void onBarCodeResult(Intent intent) {
        BarCodeInfo barCodeInfo = (BarCodeInfo) intent.getSerializableExtra(Constants.BARCODE_KEY);
        String barCodeContent = barCodeInfo.getDisplayContent();
        lists.get(currentSubTablePos).setdCode(barCodeContent);

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
                                for (RoutinemonitoringEntity rmEntity : lists) {
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
                                TaskHelper.saveSampleImage(FillRoutineSampleActivity.this, webTaskListener, imageMap);
                            }
                        } else {
                            mPosition = -1;
                            Utils.dismissDialog(progressDialog);
                            Utils.showToast(FillRoutineSampleActivity.this, message);
                        }
                    }
                });
            }
        }
    }
}
