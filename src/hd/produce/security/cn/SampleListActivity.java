package hd.produce.security.cn;

import hd.produce.security.cn.data.SampleInfoEntity;
import hd.produce.security.cn.entity.TbMonitoringProject;
import hd.source.task.DataManager;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.utils.cn.CommonUtils;
import hd.utils.cn.Constants;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;
import hd.utils.cn.ViewUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator 任务详情列表页
 */
public class SampleListActivity extends Activity implements View.OnClickListener, ITaskListener {

    public static final String TAG = "SampleListActivity";
    private Button mBtnBack;
    private TextView mTvTittle;
    private TextView mTvName;
    private ListView mListView;
    private SampleListAdapter mAdapter;
    private String mTaskName;
    List<SampleInfoEntity> mDataList;
    private String mPadId;
    private String taskId;
    private int mType = 0;
    ProgressDialog mLoading;

    // 加载布局文件
    private LayoutInflater layoutInflater = null;

    private Handler mHandler = new Handler();

    private void updateUnCompleteList() {
        mAdapter.update(mDataList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samplelist);
        layoutInflater = getLayoutInflater();
        HdCnApp.getApplication().addActivity(this);
        mLoading = Utils.showProgress(SampleListActivity.this, null, SampleListActivity.this.getResources().getString(R.string.msg_loading_data), false, true);
        initData(this.getIntent());
        initViews();
    }

    @Override
    protected void onResume() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("taskCode", taskId);
        param.put("padId", mPadId);
        switch (mType) {
        case Constants.INTENT_TYPE_DOING:
            mTvTittle.setText(R.string.tittle_doing);
            TaskHelper.getLocalTask(SampleListActivity.this, SampleListActivity.this, DataManager.TASK_GET_UNFINISHED_LIST_BY_TASK, param);
            break;
        case Constants.INTENT_TYPE_DONE:
            mTvTittle.setText(R.string.tittle_done);
            TaskHelper.getLocalTask(SampleListActivity.this, SampleListActivity.this, DataManager.TASK_GET_FINISHED_LIST_BY_TASK, param);
            break;
        }
        super.onResume();
    }

    private void initData(Intent intent) {
        try {
            mType = intent.getExtras().getInt(Constants.INTENT_KEY, 0);
            mTaskName = intent.getExtras().getString(Constants.INTENT_KEY_TASKNAME);
            mPadId = DataManager.getInstance().getMonitoringPadEntity().getId();
            taskId = DataManager.getInstance().getSelectTask();
        } catch (NullPointerException e) {
            LogUtil.e(TAG, "Error in initData", e);
        }
    }

    private void initViews() {
        mBtnBack = (Button) findViewById(R.id.back);
        mBtnBack.setOnClickListener(this);
        mTvTittle = (TextView) findViewById(R.id.title_text);
        mTvName = (TextView) findViewById(R.id.task_name);
        mTvName.setText(mTaskName);
        mListView = (ListView) this.findViewById(R.id.list_task);
        mAdapter = new SampleListAdapter();
        mListView.setAdapter(mAdapter);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("taskCode", taskId);
        param.put("padId", mPadId);
        switch (mType) {
        case Constants.INTENT_TYPE_DOING:
            mTvTittle.setText(R.string.tittle_doing);
            TaskHelper.getLocalTask(SampleListActivity.this, SampleListActivity.this, DataManager.TASK_GET_UNFINISHED_LIST_BY_TASK, param);
            break;
        case Constants.INTENT_TYPE_DONE:
            mTvTittle.setText(R.string.tittle_done);
            TaskHelper.getLocalTask(SampleListActivity.this, SampleListActivity.this, DataManager.TASK_GET_FINISHED_LIST_BY_TASK, param);
            break;
        default:
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void onBackClick() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.back:
            onBackClick();
            break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onTaskFinish(int result, int request, final Object entitys) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDataList = (List<SampleInfoEntity>) entitys;
                if (ConverterUtil.isEmpty(mDataList)) {
                    Utils.showTips(SampleListActivity.this, SampleListActivity.this.getResources().getString(R.string.msg_load_no_data));
                }
                updateUnCompleteList();
                Utils.dismissDialog(mLoading);
            }
        });
    }

    class SampleListAdapter extends BaseAdapter implements ITaskListener {

        // private Class<?> sampleClass = null;

        private List<SampleInfoEntity> mData;

        public long getItemId(int i) {
            return i;
        }

        public Object getItem(int position) {
            if (null != mData && mData.size() > position) {
                return mData.get(position);
            }
            return null;
        }

        public int getCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }

        public void update(List<SampleInfoEntity> data) {
            mData = data;
            this.notifyDataSetChanged();
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = layoutInflater.inflate(R.layout.item_samplelist, null);
                holder = new ViewHolder();
                holder.sampleImage = (ImageView) convertView.findViewById(R.id.sample_image);
                holder.sampleNum = (TextView) convertView.findViewById(R.id.sample_num);
                holder.dCode = (TextView) convertView.findViewById(R.id.dcode);
                holder.prodeuceName = (TextView) convertView.findViewById(R.id.prodeuce_name);
                holder.inspectedUnits = (TextView) convertView.findViewById(R.id.inspected_units);
                holder.checkTime = (TextView) convertView.findViewById(R.id.check_time);
                holder.btnDel = (Button) convertView.findViewById(R.id.btn_del);
                holder.btnDetail = (Button) convertView.findViewById(R.id.btn_detail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SampleInfoEntity sample = (SampleInfoEntity) getItem(position);
            if (sample == null) {
                return convertView;
            }
            // 条码
            holder.dCode.setText(sample.getdCode());
            // 产品名称
            holder.prodeuceName.setText(sample.getSampleName());
            // 受检单位
            holder.inspectedUnits.setText(sample.getUnitFullname());
            // 抽样时间
            holder.checkTime.setText(sample.getSamplingDate());
            OnClick click = new OnClick(position);
            ViewUtils.setViewState(holder.btnDetail, View.VISIBLE);
            switch (mType) {
            case Constants.INTENT_TYPE_DOING:
                // holder.sampleImage.setImageBitmap(bm);
                ViewUtils.setViewState(holder.btnDel, View.VISIBLE);
                ViewUtils.setTextView(holder.btnDetail, SampleListActivity.this.getResources().getString(R.string.go_on));
                break;
            case Constants.INTENT_TYPE_DONE:
                ViewUtils.setViewState(holder.btnDel, View.GONE);
                ViewUtils.setTextView(holder.btnDetail, SampleListActivity.this.getResources().getString(R.string.detail));
                break;
            default:
                break;
            }

            String cropPath = sample.getSamplePath();
            Bitmap bitmap = BitmapFactory.decodeFile(Utils.getImageDirectory() + cropPath);
            if (bitmap != null) {
                holder.sampleImage.setImageBitmap(bitmap);
            }

            ViewUtils.setClickListener(holder.btnDel, click);
            ViewUtils.setClickListener(holder.btnDetail, click);

            return convertView;

        }

        class ViewHolder {
            ImageView sampleImage;

            TextView sampleNum, dCode, prodeuceName, inspectedUnits, checkTime;

            Button btnDel, btnDetail;
        }

        class OnClick implements View.OnClickListener {

            private int pos;

            public OnClick(int pos) {
                this.pos = pos;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                case R.id.btn_del:
                    Utils.showConfimDialog(SampleListActivity.this, SampleListActivity.this.getResources().getText(R.string.msg_confim_delete), new Utils.OnDialogDoneListener() {
                        @Override
                        public void onDialogDone() {
                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("sampleCode", mData.get(pos).getSampleCode());
                            params.put("templete", mData.get(pos).getTemplete());
                            params.put("position", pos);
                            TaskHelper.getLocalTask(SampleListActivity.this, SampleListAdapter.this, DataManager.TASK_DELETE_SAMPLE, params);
                        }
                    });
                    break;
                case R.id.btn_detail:
                    TbMonitoringProject monitoringProjectEntity = DataManager.getInstance().getSelectProjectEntity();
                    // 确定抽样单类型
                    Class<?> sampleClass = CommonUtils.getSampleTemplete(monitoringProjectEntity.getTemplete());
                    Intent mIntent = new Intent();
                    mIntent.putExtra(Constants.INTENT_KEY_PO, mData.get(pos));
                    switch (mType) {
                    case Constants.INTENT_TYPE_DOING:
                        // 继续执行
                        mIntent.putExtra(Constants.INTENT_KEY_DOING, true);
                        mIntent.setClass(SampleListActivity.this, sampleClass);
                        break;
                    case Constants.INTENT_TYPE_DONE:
                        // 查看详情
                        mIntent.putExtra(Constants.INTENT_KEY_DOING, false);
                        mIntent.setClass(SampleListActivity.this, sampleClass);
                        break;
                    }
                    SampleListActivity.this.startActivity(mIntent);
                    break;
                default:
                    break;
                }
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onTaskFinish(int result, int request, final Object entity) {
            if (request == DataManager.TASK_DELETE_SAMPLE) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 删除抽样单
                        Map<String, Object> res = (Map<String, Object>) entity;
                        boolean delSuccess = ConverterUtil.toBoolean(res.get("result"));
                        if (delSuccess) {
//                            int pos = ConverterUtil.toInteger(res.get("position"));
//                            mData.remove(pos);
//                            SampleListAdapter.this.update(mData);
                            onResume();
                            Utils.showToast(SampleListActivity.this, SampleListActivity.this.getResources().getString(R.string.msg_del_success));
                        } else {
                            Utils.showToast(SampleListActivity.this, SampleListActivity.this.getResources().getString(R.string.msg_del_faild));
                        }
                    }
                });
            }
        }
    }

}
