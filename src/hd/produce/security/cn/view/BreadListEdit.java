/**
 * 文件名称 : BreadListEdit.java
 * 作者信息 : anjihang
 * 创建时间 : 2014年4月25日
 * 版权声明 : Copyright (c) doumi-tech All rights reserved
 */

package hd.produce.security.cn.view;

import hd.produce.security.cn.entity.Sprinner;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class BreadListEdit extends EditText implements TextWatcher {

    /**
     * 数据源
     */
    private List<Sprinner> mData;
    /**
     * 从数据源查找出来的临时数据
     */
    private List<Sprinner> mTempData;
    private OnSearchCallback mOnSearchCallback;

    public BreadListEdit(Context context) {
        super(context, null);
    }

    public BreadListEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(this);
    }

    public void setData(List<Sprinner> data) {
        mData = data;
    }

    public void setOnSearchCallback(OnSearchCallback callback) {
        mOnSearchCallback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        check(s.toString());
    }

    private void check(final String name) {
        if (mTempData == null) {
            mTempData = new ArrayList<Sprinner>();
        }
        // 每次都清空数据
        mTempData.clear();
        if (mData == null || mData.isEmpty()) {
            mHandler.sendEmptyMessage(0);
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                if (TextUtils.isEmpty(name)) {
                    mTempData.addAll(mData);
                    mHandler.sendEmptyMessage(0);
                    return;
                }
                for (Sprinner entity : mData) {
                    if (entity.getName() != null && entity.getName().contains(name)) {
                        mTempData.add(entity);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }

        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mOnSearchCallback != null) {
                mOnSearchCallback.onSearchCallback(mTempData);
            }
        }

    };

    public void update() {
        check(getText().toString().trim());
    }

    public interface OnSearchCallback {
        public void onSearchCallback(List<Sprinner> data);
    }

}