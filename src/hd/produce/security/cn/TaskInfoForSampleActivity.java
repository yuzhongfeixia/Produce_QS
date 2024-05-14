
package hd.produce.security.cn;

import hd.produce.security.cn.data.MonitoringPadEntity;
import hd.produce.security.cn.data.MonitoringTaskDetailsEntity;
import hd.produce.security.cn.data.MonitoringTaskEntity;
import hd.source.task.DataManager;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.source.task.WebServiceUtil;
import hd.utils.cn.Constants;
import hd.utils.cn.Utils;

import java.util.ArrayList;

import org.ksoap2.serialization.SoapObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator 任务信息替抽样界面
 */
public class TaskInfoForSampleActivity extends MonitoredActivity implements ITaskListener {
    private Button back;

    private TextView title;

    private TextView taskEdit;

    private ListView listview;

    private LayoutInflater inflater;

    private TaskInfoAdapter adapter;

    private ProgressDialog dialog;

    private MonitoringTaskEntity mte;

    private ArrayList<MonitoringPadEntity> list = new ArrayList<MonitoringPadEntity>();

    private Handler handler = new Handler();

    private int sampleCount;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_task_info_for_sample);
        HdCnApp.getApplication().addActivity(this);
        mte = (MonitoringTaskEntity) this.getIntent().getSerializableExtra(
                Constants.MONITOR_TASK_ENTITY_KEY);
        sampleCount = this.getIntent().getIntExtra(Constants.FOR_SAMPLE_NUM_KEY, 0);
        initViews();
        setActions();
        getTaskInfoForSampleList();

    }

    private void getTaskInfoForSampleList() {
        dialog = Utils.showProgress(this, null, "正在获取数据", false, true);
        TaskHelper.getOtherPadInfo(this, this, DataManager.getInstance().getMonitoringPadEntity()
                .getId(), mte.getTaskcode());
    }

    private void initViews() {
//    	sampleCount = this.getIntent().getIntExtra(Constants.FOR_SAMPLE_NUM_KEY, -1);
        inflater = this.getLayoutInflater();
        back = (Button) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title_text);
        title.setText(R.string.task_info);
        taskEdit = (TextView) findViewById(R.id.remark_edit);
        taskEdit.setText(mte.getTaskname());
        listview = (ListView) findViewById(R.id.listview);
        adapter = new TaskInfoAdapter();
        listview.setAdapter(adapter);

    }

    private void setActions() {
        back.setOnClickListener(onClickListener);
    }

    private void onBackClick() {
        super.onBackPressed();
    }

    final OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.back:
                    onBackClick();
                    break;

                default:
                    break;
            }
        }
    };

    class TaskInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int pos, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.task_info_for_sample_item_layout, null);
                holder.sampler = (TextView) view.findViewById(R.id.task_name_text);
                holder.taskNum = (TextView) view.findViewById(R.id.task_num_text);
                holder.forSample = (TextView) view.findViewById(R.id.for_sample_text);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.taskNum.setText(String.valueOf(list.get(pos).getTaskCount()));
            holder.sampler.setText(list.get(pos).getUsername());
            int taskNum = list.get(pos).getTaskCount();
//            int taskNum = 0;
        	if(taskNum <= 0){
//        		holder.forSample.setClickable(false);
        		holder.forSample.setEnabled(false);
        		holder.forSample.setBackgroundColor(Color.GRAY);
        	}else{
        		holder.forSample.setEnabled(true);
        		holder.forSample.setBackgroundResource(R.drawable.btn_green_selector);
        	}
            holder.forSample.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                	
                    final EditText edit = new EditText(TaskInfoForSampleActivity.this);
                    new AlertDialog.Builder(TaskInfoForSampleActivity.this).setTitle("请输入替抽数量")
                            .setIcon(android.R.drawable.ic_dialog_info).setView(edit)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    // TODO Auto-generated method stub
                                    int forSampleNum = 0;
                                    String forwardStr = edit.getEditableText().toString();
                                    if (!TextUtils.isEmpty(forwardStr)) {
                                        forSampleNum = Integer.parseInt(forwardStr);
                                    }
                                    if(forSampleNum <= 0){
                                    	Utils.showTips(TaskInfoForSampleActivity.this, "请输入正确的替抽数量");
                                    	return;
                                    }
                                    if(forSampleNum > list.get(pos).getTaskCount()){
                                    	Utils.showTips(TaskInfoForSampleActivity.this, "输入的替抽数量大于用户的待抽样数了");
                                    	return;
                                    }
                                    forSample(pos, forSampleNum);
                                }
                            }).setNegativeButton("取消", null).show();
                }

            });
            return view;
        }

    }

    private void forSample(final int pos, int forSampleNum) {
        dialog = Utils.showProgress(TaskInfoForSampleActivity.this, null, "替抽中", false, true);

        MonitoringTaskDetailsEntity paramMonitoringTaskDetailsEntity = DataManager.getInstance()
                .getMonitoringTaskDetailsMap().get(mte.getTaskcode());

        TaskHelper.forwardTask(TaskInfoForSampleActivity.this, new ITaskListener() {

            @Override
            public void onTaskFinish(int result, int request, Object entity) {
                // TODO Auto-generated method stub
                Utils.dismissDialog(dialog);
                if (result == ITaskListener.RESULT_OK) {
                    try {
                        if (WebServiceUtil.forwardTask((SoapObject) entity)) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                	list.get(pos).setTaskCount(list.get(pos).getTaskCount() - 1);
                                    Utils.showToast(TaskInfoForSampleActivity.this, "替抽成功");
                                    updateList();
                                }

                            });
                        } else {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    Utils.showToast(TaskInfoForSampleActivity.this, "替抽失败");
                                }

                            });
                        }
                    } catch (Exception e) {
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Utils.showToast(TaskInfoForSampleActivity.this, "替抽失败");
                            }

                        });
                    }
                } else if (result == ITaskListener.RESULT_FAILED) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Utils.showToast(TaskInfoForSampleActivity.this, "替抽失败");
                        }

                    });
                }
            }
        }, paramMonitoringTaskDetailsEntity, list.get(pos).getId(), -forSampleNum, "1");

    }

    class ViewHolder {
        TextView sampler;

        TextView taskNum;

        TextView forSample;
    }

    private void updateList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskFinish(int result, int request, Object entity) {
        // TODO Auto-generated method stub
        Utils.dismissDialog(dialog);
        if (result == ITaskListener.RESULT_OK) {
            try {
                list = (ArrayList<MonitoringPadEntity>) WebServiceUtil
                        .getOtherPadInfo((SoapObject) entity);
                Log.d("lzc", "list size===================>" + list.size());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        updateList();
                    }

                });
            } catch (Exception e) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Utils.showTips(TaskInfoForSampleActivity.this, TaskInfoForSampleActivity.this.getResources()
                                .getString(R.string.msg_load_faild));
                    }

                });
            }
        } else if(result == ITaskListener.RESULT_NET_ERROR){ 
            handler.post(new Runnable() {
                
                @Override
                public void run() {
//                    Utils.showTips(TaskInfoForSampleActivity.this, TaskInfoForSampleActivity.this.getResources()
//                            .getString(R.string.msg_load_faild));
                    // 所有异常都按照网络异常处理
                    Utils.showTips(TaskInfoForSampleActivity.this, TaskInfoForSampleActivity.this.getResources().getString(R.string.msg_net_error_retry));
                }
                
            });
        } else {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    Utils.showTips(TaskInfoForSampleActivity.this, TaskInfoForSampleActivity.this.getResources()
                            .getString(R.string.msg_load_no_data));
                }

            });
        }
    }
}
