
package hd.produce.security.cn.fragment;

import hd.produce.security.cn.ProjectListActivity;
import hd.produce.security.cn.R;
import hd.produce.security.cn.TaskListActivity;
import hd.produce.security.cn.entity.TbMonitoringProject;
import hd.produce.security.cn.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import hd.produce.security.cn.pulltorefresh.PullToRefreshListView;
import hd.source.task.DataManager;
import hd.utils.cn.Constants;
import hd.utils.cn.Utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListAllFragment extends Fragment {

    private ListView mListView;

    private TaskListAdapter mAdapter;

    private LayoutInflater inflater;
    
    private PullToRefreshListView pullToRefreshListView;
    
    private ProjectListActivity activity;

    public static final Fragment newInstance() {
        Fragment fragment = new TaskListAllFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_list_all_layout, null);
        initViews(view);
        activity = (ProjectListActivity) this.getActivity();
        return view;
    }

    private void initViews(View view) {
    	pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listview);
    	pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(!Utils.hasInternet(activity)){
					pullToRefreshListView.onRefreshComplete();
					return;
				}
				activity.getProjectList();
			}
		});
//    	activity.setOnRefreshListener(this);
//    	pullToRefreshListView.setOnRefreshListener(this);
//        mListView = (ListView) view.findViewById(R.id.listview);
    	mListView = pullToRefreshListView.getRefreshableView();
        mAdapter = new TaskListAdapter(this.getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Intent mIntent = new Intent();
                // mIntent.putExtra(Constants.INTENT_KEY_PO,
                // DataManager.getInstance()
                // .getMonitoringProjectEntityList().get(position));
                // mIntent.setClass(getActivity(), TaskInfoActivity.class);
                // startActivity(mIntent);
                // 设置选中项目
                String selectCode = DataManager.getInstance().getMonitoringProjectEntityList().get(position).getProjectCode();
                DataManager.getInstance().setSelectProject(selectCode);
                Intent mIntent = new Intent();
                mIntent.putExtra(Constants.INTENT_KEY_PROJECTID, selectCode);
                mIntent.setClass(getActivity(), TaskListActivity.class);
                startActivity(mIntent);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public void update() {
        mAdapter.notifyDataSetChanged();
    }

    class TaskListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public TaskListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return DataManager.getInstance().getMonitoringProjectEntityList().size();
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
            List<TbMonitoringProject> datas = DataManager.getInstance()
                    .getMonitoringProjectEntityList();
            if (datas == null || datas.size() <= 0) {
                return convertView;
            }
            String name = datas.get(position).getName();
            taskName.setText(name);
            return convertView;
        }
    }
    public void getProjectListComplete(){
    	if(pullToRefreshListView != null){
    		pullToRefreshListView.onRefreshComplete();	
    	}
    }

}
