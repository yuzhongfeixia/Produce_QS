/**
 * 文件名称 : BreadListAct.java
 * 作者信息 : anjihang
 * 创建时间 : 2014年4月25日
 * 版权声明 : Copyright (c) doumi-tech All rights reserved
 */

package hd.produce.security.cn;

import hd.produce.security.cn.MonitoringSiteAct.MenuAdapter;
import hd.produce.security.cn.entity.Sprinner;
import hd.produce.security.cn.view.BreadListEdit;
import hd.produce.security.cn.view.BreadListEdit.OnSearchCallback;
import hd.source.task.DataManager;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.utils.cn.Utils;

import java.util.List;

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
import android.widget.ListView;
import android.widget.TextView;

public class BreadListAct extends MonitoredActivity implements OnItemClickListener, OnClickListener {

    private String mKeyWords;
    private List<Sprinner> mBreedList;
    private BreadListEdit mEditText;
    private ListView mListView;
    private MenuAdapter mAdapter;
    private ProgressDialog progressDialog;
    private BreedListListener breedListListener = new BreedListListener();
    final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bread_list);
        mKeyWords = getIntent().getStringExtra("mKeyWords");
        initView();
        // 显示进度条
        progressDialog = Utils.showProgress(BreadListAct.this, null, getResources().getText(R.string.msg_extends_breed_list), false, false);
        TaskHelper.getLocalTask(this, breedListListener, DataManager.TASK_GET_BREED_LIST, null);
    }

    private void initView() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.rightButton).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.title_text)).setText("输入抽样品种");
        mEditText = (BreadListEdit) findViewById(R.id.edit);
        mEditText.setData(mBreedList);
        mEditText.setOnSearchCallback(new OnSearchCallback() {

            @Override
            public void onSearchCallback(List<Sprinner> list) {
                mBreedList = list;
                if (mBreedList == null)
                    return;
                mAdapter.notifyDataSetChanged();
            }

        });
        if (!TextUtils.isEmpty(mKeyWords)) {
            mEditText.setText(mKeyWords);
            mEditText.setSelection(mKeyWords.length());
        }
        mListView = (ListView) findViewById(R.id.layout_drop_down_menu_list);
        mAdapter = new MenuAdapter();
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mBreedList == null || mBreedList.size() <= position) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        Intent intent = new Intent();
        intent.putExtra("MonitoringBreedEntity", mBreedList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != mEditText) {
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
        switch (v.getId()) {
        case R.id.back:
            finish();
            break;
        }
    }

    /**
     * listview适配
     */
    public class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mBreedList != null) {
                return mBreedList.size();
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
                convertView = LayoutInflater.from(BreadListAct.this).inflate(R.layout.spinner_item_layout, null);
            }
            ((TextView) convertView.findViewById(R.id.spinner_item_text)).setText(mBreedList.get(position).getName());
            return convertView;
        }
    }

    private class BreedListListener implements ITaskListener {

        @SuppressWarnings("unchecked")
        @Override
        public void onTaskFinish(int result, int request, final Object entity) {
            // 取得样品名称
            if (request == DataManager.TASK_GET_BREED_LIST) {
                mHandler.post(new Runnable() {
                    public void run() {
                        // 取得抽样品种
                        mBreedList = (List<Sprinner>) entity;
                        mEditText.setData(mBreedList);
                        // 如果有关键字则初始化默认查询位置
                        if (!TextUtils.isEmpty(mKeyWords)) {
                            mEditText.setText(mKeyWords);
                            mEditText.setSelection(mKeyWords.length());
                        } else {
                            mAdapter = new MenuAdapter();
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        }

    }
}
