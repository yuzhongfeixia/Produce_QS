
package hd.produce.security.cn.fragment;

import hd.produce.security.cn.ProjectListActivity;
import hd.produce.security.cn.R;
import hd.produce.security.cn.TaskListActivity;
import hd.produce.security.cn.data.MonitoringProjectEntity;
import hd.produce.security.cn.entity.TbMonitoringProject;
import hd.produce.security.cn.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import hd.produce.security.cn.pulltorefresh.PullToRefreshExpandableListView;
import hd.produce.security.cn.view.SubListView;
import hd.source.task.DataManager;
import hd.utils.cn.Constants;
import hd.utils.cn.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListGroupFragment extends Fragment {
    private ExpandableListView expandableListView;

    private ExpandAdapter adapter;

    private List<String> itemInfos = new ArrayList<String>();

    private Map<String, ArrayList<String>> all;

    private LayoutInflater inflater;

    private ArrayList<Boolean> groupStatus = new ArrayList<Boolean>();
    
    private PullToRefreshExpandableListView viewContainer;
    
    private ProjectListActivity activity;

    public static final Fragment newInstance() {
        Fragment fragment = new TaskListGroupFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_list_group_layout, null);
        getValues();
        initViews(view);
        activity = (ProjectListActivity) this.getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getValues() {
        String[] keys = getActivity().getResources().getStringArray(R.array.group_keys);
        for (String key : keys) {
            itemInfos.add(key);
        }
        for (int i = 0; i < itemInfos.size(); i++) {
            groupStatus.add(false);
        }

    }

    private void initViews(View view) {
    	viewContainer = (PullToRefreshExpandableListView) view.findViewById(R.id.listview);
    	viewContainer.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(!Utils.hasInternet(activity)){
					viewContainer.onRefreshComplete();
					return;
				}
				activity.getProjectList();
			}
		});
//        expandableListView = (ExpandableListView) view.findViewById(R.id.listview);
    	expandableListView = viewContainer.getRefreshableView();
        adapter = new ExpandAdapter(this.getActivity());
        expandableListView.setAdapter(adapter);

        expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPos) {
                groupStatus.set(groupPos, false);
                adapter.notifyDataSetChanged();
            }

        });
        expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPos) {
                groupStatus.set(groupPos, true);
                adapter.notifyDataSetChanged();
            }

        });

    }

    class ExpandAdapter extends BaseExpandableListAdapter {
        private Context context;

        // private LayoutInflater inflater;

        public ExpandAdapter(Context context) {

            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            return itemInfos.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // return childList.get(groupPosition).size();
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return itemInfos.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return itemInfos.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            View groupView = null;
            if (convertView == null) {
                groupView = newGroupView(parent);
            } else {
                groupView = convertView;
            }
            bindGroupView(groupPosition, groupView);
            return groupView;
        }

        private View newGroupView(ViewGroup parent) {
            return inflater.inflate(R.layout.item_tasklist, null);
        }

        private void bindGroupView(final int groupPosition, View groupView) {
            TextView text = (TextView) groupView.findViewById(R.id.task_name);
            text.setText(itemInfos.get(groupPosition));
            ImageView image = (ImageView) groupView.findViewById(R.id.btn_item);
            if (groupStatus.get(groupPosition)) {
                image.setImageResource(R.drawable.arrow_expand);
            } else {
                image.setImageResource(R.drawable.arrow_collapse);
            }

        }

        @Override
        public View getChildView(int groupPosition, int childPosition,

        boolean isLastChild, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View childView = null;
            if (convertView == null) {
                childView = newChildView(parent, groupPosition);
            } else {
                childView = convertView;
            }
            bindChildView(groupPosition, childPosition, childView);
            return childView;
        }

        private View newChildView(ViewGroup parent, final int groupPosition) {

            View v = inflater.inflate(R.layout.item_tasklist_child_layout, null);

            return v;
        }

        private void bindChildView(int groupPosition, int childPosition, View childView) {
            SubListView subListView = (SubListView) childView.findViewById(R.id.listview);
            List<TbMonitoringProject> childeItemInfos = null;
            switch (groupPosition) {
                case 0:
                    childeItemInfos = DataManager.getInstance().getRoutinemonitoringEntityList();
                    break;
                case 1:
                    childeItemInfos = DataManager.getInstance().getGeneralcheckEntityList();
                    break;
                case 2:
                    childeItemInfos = DataManager.getInstance().getSpecialTaskEntityList();
                    break;
                case 3:
                    childeItemInfos = DataManager.getInstance().getSuperviseCheckEntityList();
                    break;
                default:
                    break;
            }
            final List<TbMonitoringProject> finalChildeItemInfos = childeItemInfos;
            SubListAdapter subAdapter = new SubListAdapter(finalChildeItemInfos, groupPosition,
                    inflater);
            subListView.setAdapter(subAdapter);
            subListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    if (finalChildeItemInfos != null && finalChildeItemInfos.size() > position) {
                        // Intent mIntent = new Intent();
                        // mIntent.putExtra(Constants.INTENT_KEY_PO,
                        // finalChildeItemInfos.get(position));
                        // mIntent.setClass(getActivity(),
                        // TaskInfoActivity.class);
                        // startActivity(mIntent);
                        // 设置选中项目
                        String selectCode = finalChildeItemInfos.get(position).getProjectCode();
                        DataManager.getInstance().setSelectProject(selectCode);
                        Intent mIntent = new Intent();
                        mIntent.putExtra(Constants.INTENT_KEY_PROJECTID, selectCode);
                        mIntent.setClass(getActivity(), TaskListActivity.class);
                        startActivity(mIntent);
                    }
                }
            });

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

    }

    class SubListAdapter extends BaseAdapter implements Filterable{

        private LayoutInflater inflater;

        private int groupPos;

        private List<TbMonitoringProject> childeItemInfos;
        private List<TbMonitoringProject> allItemInfos;
        
        private final Object mLock = new Object();

        public SubListAdapter(List<TbMonitoringProject> childeItemInfos, int groupPos,
                LayoutInflater inflater) {
            this.childeItemInfos = childeItemInfos;
            this.allItemInfos = childeItemInfos;
            this.groupPos = groupPos;
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return childeItemInfos.size();
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
            View v;
            if (convertView == null) {
                v = inflater.inflate(R.layout.sub_item_list_layout, null);
            } else {
                v = convertView;
            }
            if (childeItemInfos == null || childeItemInfos.size() <= 0) {
                return v;
            }
            TbMonitoringProject project = childeItemInfos.get(position);
            if (project != null) {
                TextView taskName = (TextView) v.findViewById(R.id.task_name);
                taskName.setText(project.getName());
               
                ImageView image = (ImageView) v.findViewById(R.id.btn_item);
                image.setImageResource(R.drawable.icon_detail);
            }

            return v;
        }

        @Override
        public Filter getFilter() {
            return projectFilter;
        }

        private Filter projectFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                // 持有过滤操作完成之后的数据。该数据包括过滤操作之后的数据的值以及数量。 count:数量 values包含过滤操作之后的数据的值
                FilterResults results = new FilterResults();
                List<TbMonitoringProject> subList = new ArrayList<TbMonitoringProject>();
                if (prefix == null || prefix.length() == 0) {
                    synchronized (mLock) {
                        results.values = subList;
                        results.count = subList.size();
                    }
                 } else {
                     // 做筛选
                     for(TbMonitoringProject entity : allItemInfos){
                         if(entity.getName().contains(prefix)){
                             subList.add(entity);
                         }
                     }
                     // 然后将这个新的集合数据赋给FilterResults对象
                     results.values = subList;
                     results.count = subList.size();
                 }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // 重新将与适配器相关联的List重赋值一下
                childeItemInfos = (List<TbMonitoringProject>) results.values;
                if (results.count > 0) {
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.notifyDataSetInvalidated();
                }
            }
        };
    }
    public void getProjectListComplete(){
    	if(viewContainer != null){
    		viewContainer.onRefreshComplete();	
    	}
    }
    
    public void update() {
        adapter.notifyDataSetChanged();
    }
    
//    public void filter(String prefix) {
//        int gCount = adapter.getGroupCount();
//        for (int i = 0; i < gCount; i++) {
//            int cCount = adapter.getChildrenCount(i);
//            for (int j = 0; j < cCount; j++) {
//                ((SubListView)adapter.getChildView(i, j, false, null, (ViewGroup) adapter.getGroup(i)).findViewById(R.id.listview)).setAdapter(adapter);
//            }
//        }
//    }
}
