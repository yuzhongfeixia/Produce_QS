
package hd.produce.security.cn;

import hd.produce.security.cn.data.MonitoringTaskEntity;
import hd.source.task.DataManager;
import hd.utils.cn.Constants;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator 主功能列表页
 */
public class TaskListActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ProjectListActivity";

    private static final int MSG_INIT_DATA = 1;

    private static final int MSG_SEARCH_DATA = 2;

    private String mSearchKey;

    private String mProjectCode;

    private ListView mListView;

    ProgressDialog mLoading;

    private TextView title;

    private TaskListAdapter mAdapter;

    private Button back;

//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_INIT_DATA:
//                    mLoading = Utils.showProgress(
//                            TaskListActivity.this,
//                            null,
//                            TaskListActivity.this.getResources().getString(
//                                    R.string.msg_loading_data), false, true);
//                    MonitoringProjectEntity paramEntity = new MonitoringProjectEntity();
//                    paramEntity.setProjectCode(mProjectCode);
//                    paramEntity
//                            .setPadId(DataManager.getInstance().getMonitoringPadEntity().getId());
//                    TaskHelper.getTaskList(TaskListActivity.this, TaskListActivity.this,
//                            paramEntity, TaskHelper.REQUEST_GET_TASKINFO_LIST);
//                    break;
//                case MSG_SEARCH_DATA:
//                    mLoading = Utils.showProgress(
//                            TaskListActivity.this,
//                            null,
//                            TaskListActivity.this.getResources().getString(
//                                    R.string.msg_loading_data), false, true);
//                    MonitoringProjectEntity entity = new MonitoringProjectEntity();
//                    entity.setProjectCode(mProjectCode);
//                    entity.setTaskName(mSearchKey);
//                    entity.setPadId(DataManager.getInstance().getMonitoringPadEntity().getId());
//                    TaskHelper.getTaskList(TaskListActivity.this, TaskListActivity.this,
//                            entity, TaskHelper.REQUEST_SEARCH_TASKINFO_LIST);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        HdCnApp.getApplication().addActivity(this);
        mProjectCode = this.getIntent().getStringExtra(Constants.INTENT_KEY_PROJECTID);
        initViews();
//        mHandler.sendEmptyMessage(MSG_INIT_DATA);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
//        back.setVisibility(View.GONE);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title_text);
        title.setText(R.string.task_list);
        mListView = (ListView) this.findViewById(R.id.project_list);
        mAdapter = new TaskListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Intent mIntent = new Intent();
                // mIntent.putExtra(Constants.INTENT_KEY_PROJECTID,
                // DataManager.getInstance()
                // .getMonitoringProjectEntityList().get(position).getProjectCode());
                // mIntent.setClass(ProjectsListActivity.this,
                // ProjectListActivity.class);
                // startActivity(mIntent);
                Intent mIntent = new Intent();
                String selectTaskCode = DataManager.getInstance().getTaskListByProjectCode(mProjectCode).get(position).getTaskcode();
                DataManager.getInstance().setSelectTask(selectTaskCode);
                mIntent.putExtra(Constants.INTENT_KEY_PO, selectTaskCode);
                mIntent.setClass(TaskListActivity.this, TaskInfoActivity.class);
                startActivity(mIntent);
            }
        });
    }

    private void sendIntent(Class<?> cls) {
        Intent mIntent = new Intent();
        mIntent.setClass(TaskListActivity.this, cls);
        startActivity(mIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_setting:
                sendIntent(SysSettingActivity.class);
                break;
        }
    }

    class TaskListAdapter extends BaseAdapter {

        private Context mContext;

        private LayoutInflater inflater;

        public TaskListAdapter(Context context) {
            this.mContext = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            List<MonitoringTaskEntity> datas = DataManager.getInstance().getTaskListByProjectCode(mProjectCode);
            if (datas == null || datas.size() <= 0) {
                return 0;
            }
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.sub_item_list_layout, null);
            }
            TextView taskName = (TextView) convertView.findViewById(R.id.task_name);
            ImageView itemImage = (ImageView) convertView.findViewById(R.id.btn_item);
            itemImage.setImageResource(R.drawable.icon_detail);
            List<MonitoringTaskEntity> datas = DataManager.getInstance().getTaskListByProjectCode(mProjectCode);
            if (datas == null || datas.size() <= 0) {
                return convertView;
            }
            String name = datas.get(position).getTaskname();
            taskName.setText(name);
            return convertView;
        }
    }
}
