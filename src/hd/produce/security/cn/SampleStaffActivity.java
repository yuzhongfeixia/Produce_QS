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

import android.app.ProgressDialog;
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
 * @author Administrator 抽样员界面
 */
public class SampleStaffActivity extends MonitoredActivity implements
		ITaskListener {
	private Button back;
	private TextView title;
	private ListView listview;

	private LayoutInflater inflater;

	private SampleStaffAdapter adapter;

	private ProgressDialog dialog;

	private MonitoringTaskEntity mte;

	final Handler handler = new Handler();

	private ArrayList<MonitoringPadEntity> list = new ArrayList<MonitoringPadEntity>();
	//总的抽样数量
	private int sampleCount ;
	//当前没转发的数量
	private int currentSampleNum;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.activity_sample_staff);
		HdCnApp.getApplication().addActivity(this);
		mte = (MonitoringTaskEntity) this.getIntent().getSerializableExtra(
				Constants.MONITOR_TASK_ENTITY_KEY);
		sampleCount = this.getIntent().getIntExtra(Constants.FOR_SAMPLE_NUM_KEY, 0);
		initViews();
		setActions();
		getStaffList();

	}

	private void getStaffList() {
		dialog = Utils.showProgress(this, null, "正在获取数据。。。", false, true);
		// TaskHelper.forwardTask(this, this, paramMonitoringTaskDetailsEntity,
		// padId, total, flagStr);
		TaskHelper.getOtherPadInfo(this, this, DataManager.getInstance()
				.getMonitoringPadEntity().getId(),null);
	}

	private void initViews() {
		inflater = this.getLayoutInflater();
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title_text);
		title.setText(R.string.sample_staff);
		listview = (ListView) findViewById(R.id.listview);
		adapter = new SampleStaffAdapter();
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

	class SampleStaffAdapter extends BaseAdapter {

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
			final ViewHolder holder;
			if (view == null) {
				holder = new ViewHolder();
				view = inflater
						.inflate(R.layout.sample_staff_item_layout, null);
				holder.sampler = (TextView) view
						.findViewById(R.id.task_name_text);
				holder.taskNum = (EditText) view
						.findViewById(R.id.forward_num_edit);
				holder.send = (TextView) view.findViewById(R.id.send_text);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.sampler.setText(list.get(pos).getUsername());
			holder.send.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if(sampleCount <= 0){
						Utils.showTips(SampleStaffActivity.this, "没有需要替抽的抽样单");
						return;
					}
					int forwardNum = 0;
					String forwardStr = holder.taskNum.getEditableText().toString();
					if(!TextUtils.isEmpty(forwardStr)){
						forwardNum = Integer.parseInt(forwardStr);
					}
					if(forwardNum <= 0){
						Utils.showTips(SampleStaffActivity.this, "输入转发数不能小于1");
						return;
					}
					if(forwardNum > sampleCount){
						Utils.showTips(SampleStaffActivity.this, "转发数不能大于替抽数量");
						return;
					}
					currentSampleNum = forwardNum;
					dialog = Utils.showProgress(SampleStaffActivity.this, null, "转发中", false, true);
					MonitoringTaskDetailsEntity paramMonitoringTaskDetailsEntity = DataManager.getInstance().getMonitoringTaskDetailsMap().get(mte.getTaskcode());
					
					TaskHelper.forwardTask(SampleStaffActivity.this, new ITaskListener() {
						
						@Override
						public void onTaskFinish(int result, int request, Object entity) {
							// TODO Auto-generated method stub
							Utils.dismissDialog(dialog);
							if(result == ITaskListener.RESULT_OK){
							    try {
							        if (WebServiceUtil.forwardTask((SoapObject) entity)) {
	                                    handler.post(new Runnable(){

	                                        @Override
	                                        public void run() {
	                                            // TODO Auto-generated method stub
	                                            Utils.showToast(SampleStaffActivity.this, "转发成功");
	                                            sampleCount = sampleCount - currentSampleNum;
	                                        }
	                                        
	                                    });
	                                } else {
	                                    handler.post(new Runnable(){

	                                        @Override
	                                        public void run() {
	                                            // TODO Auto-generated method stub
	                                            Utils.showToast(SampleStaffActivity.this, "转发失败");
	                                        }
	                                        
	                                    });
	                                }
                                } catch (Exception e) {
                                    handler.post(new Runnable(){

                                        @Override
                                        public void run() {
                                            // TODO Auto-generated method stub
                                            Utils.showToast(SampleStaffActivity.this, "转发失败");
                                        }
                                        
                                    });
                                }
							}else if(result == ITaskListener.RESULT_FAILED){
								handler.post(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Utils.showToast(SampleStaffActivity.this, "转发失败");
									}
									
								});
							}
						}
					}, paramMonitoringTaskDetailsEntity,list.get(pos).getId(), forwardNum, "2");
				}
				
			});
			return view;
		}
	}

	class ViewHolder {
		TextView sampler;
		EditText taskNum;
		TextView send;
	}

	private void updateList() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onTaskFinish(int result, int request, Object entity) {
		// TODO Auto-generated method stub
		Log.d("lzc", "result========================>" + result);
		Utils.dismissDialog(dialog);
		if (result == ITaskListener.RESULT_OK) {
		    try {
		        list = (ArrayList<MonitoringPadEntity>) WebServiceUtil
	                    .getOtherPadInfo((SoapObject) entity);
	            Log.d("lzc", "list size===================>" + list.size());
	            handler.post(new Runnable() {

	                @Override
	                public void run() {
	                    // TODO Auto-generated method stub
	                    updateList();
	                }

	            });
            } catch (Exception e) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Utils.showToast(SampleStaffActivity.this, "获取数据失败");
                    }

                });
            }
			
		} else {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Utils.showToast(SampleStaffActivity.this, "获取数据失败");
				}

			});
		}
	}
}
