package hd.produce.security.cn;

import hd.produce.security.cn.data.MonitoringBreedEntity;
import hd.produce.security.cn.data.MonitoringTaskDetailsEntity;
import hd.produce.security.cn.data.MonitoringTaskEntity;
import hd.produce.security.cn.entity.TbMonitoringProject;
import hd.source.task.DataManager;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.utils.cn.CommonUtils;
import hd.utils.cn.Constants;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Administrator 任务信息界面
 */
public class TaskInfoActivity extends MonitoredActivity implements ITaskListener {
    public static final String TAG = "TaskInfoActivity";
    private Button back;

    private TextView title;

    private Button forward;

    private TextView taskName;

    private TextView republic;

    private TextView endDate;

    private TextView sampleType;

    private TextView sampleArea;

    private TextView sampleLink;

    private TextView receiveSampleNum;

    private RelativeLayout unComplete;

    private TextView unCompleteNum;

    private RelativeLayout hasComplete;

    private TextView hasCompleteNum;

    private TextView addSampleInfo;

    // private TextView viewOtherTask;

    private MonitoringTaskEntity monitoringTaskEntity;

    private TbMonitoringProject monitoringProjectEntity;

    MonitoringTaskDetailsEntity monitoringTaskDetailsEntity;

    // 待抽样数量进度
    private ProgressBar pb_receiveSample;
    // 未完成数量进度
    private ProgressBar pb_unComplete;
    // 已完成数量进度
    private ProgressBar pb_complete;

    private int receiveSampleCount;

    private int sampleCount;

    private int unFinishSampleCount;

    private int finishedSampleCount = 0;

    private ProgressDialog mLoading;

    List<MonitoringBreedEntity> breedList = new ArrayList<MonitoringBreedEntity>();

    ArrayList<String> breadStrList = new ArrayList<String>();

    ArrayList<String> breadAgrCodeList = new ArrayList<String>();

    ArrayList<String> nameStrList = new ArrayList<String>();

    ArrayList<String> nameAgrCodeList = new ArrayList<String>();

    ArrayList<String> areaList = new ArrayList<String>();

    private String sampleCity;
    private String sampleCounty;

    private TaskDetailListListener localTaskListener = new TaskDetailListListener();

    private Handler mHandler = new Handler() {
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_task_info);
        HdCnApp.getApplication().addActivity(this);

        getIntentValues();
        initViews();
        bindData();
        setListener();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("taskCode", DataManager.getInstance().getSelectTask());
        TaskHelper.getLocalTask(TaskInfoActivity.this, localTaskListener, DataManager.TASK_GET_TASK_DETAIL_LIST, param);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 没网的情况下不可点击该操作
//        if (!Utils.isNetValid(TaskInfoActivity.this)) {// 没有网络
//            forward.setVisibility(View.GONE);
            // viewOtherTask.setVisibility(View.GONE);
//        } else {
//            forward.setVisibility(View.VISIBLE);
            // viewOtherTask.setVisibility(View.VISIBLE);
//        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("taskCode", DataManager.getInstance().getSelectTask());
        TaskHelper.getLocalTask(TaskInfoActivity.this, localTaskListener, DataManager.TASK_GET_TASK_DETAIL_LIST, param);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private void getIntentValues() {
        monitoringTaskEntity = (MonitoringTaskEntity) DataManager.getInstance().getSelectTaskEntity();
        // this.getIntent().getSerializableExtra(Constants.INTENT_KEY_PO);
        monitoringProjectEntity = DataManager.getInstance().getMonitoringProjectEntity(monitoringTaskEntity.getProjectCode());
        // determineSampleType();
    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title_text);
        title.setText(R.string.task_info);
        forward = (Button) findViewById(R.id.rightButton);
        forward.setVisibility(View.GONE);
        forward.setText(R.string.forward);

        taskName = (TextView) findViewById(R.id.task_name_edit);
        republic = (TextView) findViewById(R.id.republic_edit);
        endDate = (TextView) findViewById(R.id.date_end_edit);
        sampleType = (TextView) findViewById(R.id.sample_type_edit);
        sampleArea = (TextView) findViewById(R.id.sample_area_edit);
        sampleLink = (TextView) findViewById(R.id.sample_link_edit);

        receiveSampleNum = (TextView) findViewById(R.id.receive_sample_num_text);

        unComplete = (RelativeLayout) findViewById(R.id.un_complete_layout);
        unCompleteNum = (TextView) findViewById(R.id.un_complete_num_text);
        hasComplete = (RelativeLayout) findViewById(R.id.has_complete_layout);
        hasCompleteNum = (TextView) findViewById(R.id.has_complete_num_text);

        addSampleInfo = (TextView) findViewById(R.id.add_sample_task);
        // viewOtherTask = (TextView) findViewById(R.id.view_other_task);
        pb_receiveSample = (ProgressBar) findViewById(R.id.receive_sample_progressBar);
        pb_unComplete = (ProgressBar) findViewById(R.id.un_complete_progressBar);
        pb_complete = (ProgressBar) findViewById(R.id.has_complete_progressBar);
    }

    private void bindData() {
        try {
            taskName.setText(monitoringTaskEntity.getTaskname());
            // 发布时间与截止时间在任务详情中取得 onTashFinsh
            // republic.setText(monitoringProjectEntity.getOgrname());
            // endDate.setText(monitoringProjectEntity.getEndtime());
            sampleType.setText(monitoringProjectEntity.getProjectBree());
            sampleArea.setText(monitoringTaskEntity.getSampleArea());
            sampleLink.setText(monitoringProjectEntity.getLinkInfo());
        } catch (Exception e) {
            LogUtil.e(TAG, "Error in bindData", e);
        }
    }

    private void setListener() {
        back.setOnClickListener(onClickListener);
        forward.setOnClickListener(onClickListener);

        unComplete.setOnClickListener(onClickListener);
        hasComplete.setOnClickListener(onClickListener);

        addSampleInfo.setOnClickListener(onClickListener);
        // viewOtherTask.setOnClickListener(onClickListener);
    }

    private void onBackClick() {
        super.onBackPressed();
    }

    /**
     * 转发
     */
    private void onForwardClick() {
        Intent intent = new Intent();
        intent.putExtra(Constants.MONITOR_TASK_ENTITY_KEY, monitoringTaskEntity);
        intent.putExtra(Constants.FOR_SAMPLE_NUM_KEY, receiveSampleCount);
        sendIntent(intent, SampleStaffActivity.class);
    }

    /**
     * 未完成点击
     */
    private void onUnCompleteClick() {
        Intent intent = new Intent();
        // mType = intent.getExtras().getInt(Constants.INTENT_KEY, 0);
        // mTaskCode =
        // intent.getExtras().getString(Constants.INTENT_KEY_TASKCODE);
        intent.putExtra(Constants.INTENT_KEY, Constants.INTENT_TYPE_DOING);
        intent.putExtra(Constants.INTENT_KEY_TASKCODE, monitoringTaskEntity.getTaskcode());
        intent.putExtra(Constants.INTENT_KEY_TASKNAME, monitoringTaskEntity.getTaskname());
        // intent.putExtra(Constants.INTENT_KEY_PROJECTID, monitoringProjectEntity.getProjectCode());
        sendIntent(intent, SampleListActivity.class);
    }

    /**
     * 已经完成
     */
    private void onHadCompleteClick() {
        try {
            Intent intent = new Intent();
            intent.putExtra(Constants.INTENT_KEY, Constants.INTENT_TYPE_DONE);
            intent.putExtra(Constants.INTENT_KEY_TASKCODE, monitoringTaskEntity.getTaskcode());
            intent.putExtra(Constants.INTENT_KEY_TASKNAME, monitoringTaskEntity.getTaskname());
            // intent.putExtra(Constants.INTENT_KEY_PROJECTID, monitoringProjectEntity.getProjectCode());

            sendIntent(intent, SampleListActivity.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加抽样信息
     */
    private void addSampleTask() {
        Intent it = new Intent();
        it.putExtra(Constants.INTENT_KEY_ADD_SAMPLE, true);
        it.putExtra(Constants.MONITOR_TASK_ENTITY_KEY, monitoringTaskEntity);
        it.putExtra(Constants.MONITOR_PROJECT_ENTITY_KEY, monitoringProjectEntity);

        it.putExtra(Constants.SAMPLE_AREA_KEY, areaList);
        // it.putExtra(Constants.SAMPLE_LINK_KEY, linkList);

        it.putExtra(Constants.SAMPLE_BREAD_KEY, breadStrList);
        it.putExtra(Constants.SAMPLE_BREAD_AGR_CODE_KEY, breadAgrCodeList);

        it.putExtra(Constants.SAMPLE_NAME_KEY, nameStrList);
        it.putExtra(Constants.SAMPLE_NAME_AGR_CODE_KEY, nameAgrCodeList);

        it.putExtra(Constants.SAMPLE_CITY_KEY, sampleCity);
        it.putExtra(Constants.SAMPLE_COUNTY_KEY, sampleCounty);

        it.putExtra(Constants.SAMPLE_TYPE, monitoringProjectEntity.getTemplete());
        Class<?> cls = CommonUtils.getSampleTemplete(monitoringProjectEntity.getTemplete());
        sendIntent(it, cls);
    }

    /**
     * 查看其他任务(暂时删除)
     */
    private void viewOtherTask() {
        Intent intent = new Intent();
        intent.putExtra(Constants.MONITOR_TASK_ENTITY_KEY, monitoringTaskEntity);
        intent.putExtra(Constants.FOR_SAMPLE_NUM_KEY, receiveSampleCount);
        sendIntent(intent, TaskInfoForSampleActivity.class);
    }

    private void sendIntent(Intent intent, Class<?> cls) {

        // Intent mIntent = new Intent();
        intent.setClass(TaskInfoActivity.this, cls);
        startActivity(intent);
    }

    final OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View v, int pos, long arg3) {
            // TODO Auto-generated method stub
            switch (adapterView.getId()) {

            // case R.id.sample_link_spinner:
            //
            // break;
            // case R.id.sample_area_spinner:
            //
            // break;
            default:
                break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    };

    final OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.back:
                onBackClick();
                break;
            case R.id.rightButton:
                onForwardClick();
                break;
            /**
             * case R.id.receive_sample_layout: onReceiveSampleClick(); break;
             */
            case R.id.un_complete_layout:
                onUnCompleteClick();
                break;
            case R.id.has_complete_layout:
                onHadCompleteClick();
                break;
            case R.id.add_sample_task:
                addSampleTask();
                break;
            // case R.id.view_other_task:
            // viewOtherTask();
            // break;
            default:
                break;
            }
        }
    };

    private class TaskDetailListListener implements ITaskListener {

        @Override
        public void onTaskFinish(int result, final int request, final Object entity) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    switch (request) {
                    // 取得任务详情
                    case DataManager.TASK_GET_TASK_DETAIL_LIST:
                        monitoringTaskDetailsEntity = (MonitoringTaskDetailsEntity) entity;
                        sampleCount = monitoringTaskDetailsEntity.getTaskCount();
                        // 发布单位
                        republic.setText(monitoringTaskDetailsEntity.getReleaseunit());
                        // 截止时间
                        endDate.setText(monitoringTaskDetailsEntity.getEndTime());
                        // 取得任务对应未完成的抽样单数量
                        unFinishSampleCount = ConverterUtil.toInteger(DataManager.getInstance().getUnfinishedCountBySelectTask(), 0);
                        // 取得任务对应已完成的抽样单数量
                        finishedSampleCount = ConverterUtil.toInteger(DataManager.getInstance().getFinishedCountBySelectTask(), 0);
                        calculateCounts();
                        break;
                    default:
                        break;
                    }
                }
            });
        }

    }

    @Override
    public void onTaskFinish(int result, final int request, final Object entity) {
        if (result == ITaskListener.RESULT_OK) {

        } else if (result == ITaskListener.RESULT_NET_ERROR) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
//                    Utils.showTips(TaskInfoActivity.this, TaskInfoActivity.this.getResources().getString(R.string.msg_load_faild));
                    // 所有异常都按照网络异常处理
                    Utils.showTips(TaskInfoActivity.this, TaskInfoActivity.this.getResources().getString(R.string.msg_net_error_retry));
                    Utils.dismissDialog(mLoading);
                }
            });
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sampleCount = 0;
                    unFinishSampleCount = 0;
                    finishedSampleCount = 0;
                    calculateCounts();
                    Utils.showTips(TaskInfoActivity.this, TaskInfoActivity.this.getResources().getString(R.string.msg_load_no_data));
                    Utils.dismissDialog(mLoading);
                }
            });
        }
    }

    /**
     * 计算3种数量
     */
    private void calculateCounts() {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // 隐藏计算进度条
                pb_unComplete.setVisibility(View.GONE);
                // 显示未完成数量
                unCompleteNum.setText(ConverterUtil.toString(unFinishSampleCount));
                unCompleteNum.setVisibility(View.VISIBLE);

                // 隐藏计算进度条
                pb_complete.setVisibility(View.GONE);
                // 显示已完成数量
                hasCompleteNum.setText(ConverterUtil.toString(finishedSampleCount));
                hasCompleteNum.setVisibility(View.VISIBLE);

                receiveSampleCount = sampleCount - unFinishSampleCount - finishedSampleCount;
                if (receiveSampleCount <= 0) {
                    receiveSampleCount = 0;
                }
                // 隐藏计算进度条
                pb_receiveSample.setVisibility(View.GONE);
                // 显示待抽样数量
                receiveSampleNum.setText(ConverterUtil.toString(receiveSampleCount));
                receiveSampleNum.setVisibility(View.VISIBLE);

            }

        });
    }
}
