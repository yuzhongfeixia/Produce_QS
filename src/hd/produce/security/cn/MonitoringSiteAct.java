/**
 * 文件名称 : MonitoringSiteAct.java
 * 作者信息 : anjihang
 * 创建时间 : 2014年4月17日
 * 版权声明 : Copyright (c) doumi-tech All rights reserved
 */

package hd.produce.security.cn;

import hd.produce.security.cn.dao.CommonDao;
import hd.produce.security.cn.data.MonitoringSiteEntity;
import hd.produce.security.cn.view.MonitoringSiteEdit;
import hd.source.task.DataManager;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.Utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MonitoringSiteAct extends MonitoredActivity implements OnItemClickListener, OnClickListener {

    private String mKeyWords;
    private List<MonitoringSiteEntity> mMenuList = new ArrayList<MonitoringSiteEntity>();
    private MonitoringSiteEdit mEditText;
    private ListView mListView;
    private MenuAdapter mAdapter;
    private MonitoringSiteEntity mMonitoringSiteEntity;
    private MonitoringSiteListener monitoringSiteListener = new MonitoringSiteListener();
    private Handler mHandler = new Handler();
    private ProgressDialog loadSite;
    private int pageCount = 1;
    private int count = 0;
    private int page = 0;
    private String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_monitoring_site);
        mKeyWords = getIntent().getStringExtra("mKeyWords");
        initView();
        TaskHelper.getLocalTask(MonitoringSiteAct.this, monitoringSiteListener, DataManager.TASK_GET_MONITORING_SITE_PAGE, null);
    }

    private void initView() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.rightButton).setOnClickListener(this);
        findViewById(R.id.rightButton).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.title_text)).setText("输入受检单位名称");
        ((Button) findViewById(R.id.rightButton)).setText("完成");

        mEditText = (MonitoringSiteEdit) findViewById(R.id.edit);
        mListView = (ListView) findViewById(R.id.layout_drop_down_menu_list);
        mListView.setOnItemClickListener(this);
        mEditText.setOnSearchCallback(new OnSearchCallback() {

            @Override
            public void onSearchCallback(List<MonitoringSiteEntity> list) {
                mMenuList = list;
                if (mMenuList == null) {
                    return;
                }
                mAdapter = new MenuAdapter();
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

        });
        msg = MonitoringSiteAct.this.getResources().getString(R.string.msg_extends_monintring_Site);
        mAdapter = new MenuAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mMenuList == null || mMenuList.size() <= position) {
            return;
        }
        mMonitoringSiteEntity = mMenuList.get(position);
        mEditText.setText(mMonitoringSiteEntity.getName());
        mEditText.setSelection(mMonitoringSiteEntity.getName().length());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        switch (v.getId()) {
        case R.id.back:
            finish();
            break;
        case R.id.rightButton:
            Intent intent = new Intent();
            String name = mEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(name)) {
                intent.putExtra("mKeyWords", name);
                for (MonitoringSiteEntity site : mMenuList) {
                    if (site.getName().equals(name)) {
                        mMonitoringSiteEntity = site;
                        break;
                    }
                }
            } else {
                mMonitoringSiteEntity = new MonitoringSiteEntity();
            }
            intent.putExtra("MonitoringSite", mMonitoringSiteEntity);
            setResult(RESULT_OK, intent);
            finish();
            break;
        }
    }

    /**
     * listview适配
     */
    public class MenuAdapter extends BaseAdapter {

        public MenuAdapter() {
        }

        @Override
        public int getCount() {
            if (mMenuList != null) {
                return mMenuList.size();
            }
            return 0;
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
                convertView = LayoutInflater.from(MonitoringSiteAct.this).inflate(R.layout.spinner_item_layout, null);
            }
            ((TextView) convertView.findViewById(R.id.spinner_item_text)).setText(mMenuList.get(position).getName());
            return convertView;
        }

    }

    public interface OnSearchCallback {
        public void onSearchCallback(List<MonitoringSiteEntity> data);
    }

    private class MonitoringSiteListener implements ITaskListener {

        @SuppressWarnings("unchecked")
        @Override
        public void onTaskFinish(int result, int request, final Object entity) {
            if (request == DataManager.TASK_GET_MONITORING_SITE_PAGE) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> param = new HashMap<String, Object>();
                        Map<String, Object> res = (Map<String, Object>) entity;
                        count = ConverterUtil.toInteger(res.get("count"));
                        page = ConverterUtil.toInteger(res.get("page"));
                        // 如果缓存中已经存在则不用重新获取
                        if (count == DataManager.getInstance().getAllMonintringSiteList().size()) {
                            // menulist是临时画面展示用,会根据过滤结果而改变,所以要再次判断 如果count与受检单位结果一致，那么menulist就需要改成最新的
                            if(mMenuList.size() != DataManager.getInstance().getAllMonintringSiteList().size()){
                                mMenuList = DataManager.getInstance().getAllMonintringSiteList();
                            }
                            mEditText.setData(mMenuList);
                            if (!TextUtils.isEmpty(mKeyWords)) {
                                mEditText.setText(mKeyWords);
                                mEditText.setSelection(mKeyWords.length());
                            }
                            Utils.dismissDialog(loadSite);
                            return;
                        }
                        // 显示进度条
                        loadSite = Utils.showProgress(MonitoringSiteAct.this, null, MessageFormat.format(msg, CommonDao.LIMIT_SITE, ConverterUtil.toString(count)), false, false);
                        // 从第一页开始加载受检单位
                        param.put("page", pageCount);
                        TaskHelper.getLocalTask(MonitoringSiteAct.this, monitoringSiteListener, DataManager.TASK_GET_MONITORING_SITE, param);
                    }
                });
            }
            if (request == DataManager.TASK_GET_MONITORING_SITE) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> param = new HashMap<String, Object>();
                        // 只动态加载一页数据到画面 因为画面一页显示的数据很少，只是做一个动态加数据的样子，这样会节省效率
                        if (pageCount <= 1) {
                            mMenuList = DataManager.getInstance().getAllMonintringSiteList();
                            mAdapter.notifyDataSetChanged();
                        }
                        // 如果页码没有全部加载就继续查询
                        if (pageCount <= page) {
                            pageCount++;
                            // 计算limit
                            int num = pageCount * ConverterUtil.toInteger(CommonDao.LIMIT_SITE);
                            // 如果超过总数 则设置成总数
                            if(num > count){
                                num = count;
                            }
                            // 改变进度条显示信息
                            loadSite.setMessage(MessageFormat.format(msg, ConverterUtil.toString(num), ConverterUtil.toString(count)));
                            param.put("page", pageCount);
                            TaskHelper.getLocalTask(MonitoringSiteAct.this, monitoringSiteListener, DataManager.TASK_GET_MONITORING_SITE, param);
                        } else {
                            // 全部加载完毕 刷新画面
                            mMenuList = DataManager.getInstance().getAllMonintringSiteList();
                            mEditText.setData(mMenuList);

                            // 如果有关键字则初始化默认查询位置
                            if (!TextUtils.isEmpty(mKeyWords)) {
                                mEditText.setText(mKeyWords);
                                mEditText.setSelection(mKeyWords.length());
                            } else {
                                mAdapter = new MenuAdapter();
                                mListView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }

                            // 关闭进度条
                            Utils.dismissDialog(loadSite);
                        }
                    }
                });
            }
        }
    }

}
