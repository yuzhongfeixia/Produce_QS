package hd.utils.cn;

import hd.produce.security.cn.HdCnApp;
import hd.produce.security.cn.R;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * 进度条共同类
 * 
 * @author xudl(Wisea)
 * 
 */
public class DownloadBroadcast {
    // 上下文
    private Context context;
    // 进度条
    private ProgressDialog mLoading;

    public DownloadBroadcast(Context context) {
        this.context = context;
        initReceiver();
    }

    /**
     * 取得当前进度
     * 
     * @return
     */
    public int getProgress() {
        return mLoading.getProgress();
    }

    /**
     * 显示进度条
     */
    public void show() {
        mLoading = Utils.showProgress(context, null, context.getResources().getString(R.string.msg_loading_data), true, false);
    }

    /**
     * 不显示进度条
     */
    public void dismiss() {
        mLoading.dismiss();
    }

    /**
     * 制作并发送一个修改进度条进度的广播
     * 
     * @param percent
     *            增长进度
     * @param content
     *            说明文字
     * @param isProcess
     *            是否为当前总进度
     */
    public static void sendBroadcast(int percent, String content, boolean isProcess) {
        Intent temIntent = new Intent("download_db");
        Bundle bundle = new Bundle();
        bundle.putInt("percent", percent);
        if (ConverterUtil.isNotEmpty(content)) {
            bundle.putString("content", content);
        }
        bundle.putBoolean("isProcess", isProcess);
        temIntent.putExtras(bundle);
        HdCnApp.getApplication().sendBroadcast(temIntent);
    }

    /**
     * 注册监听器
     */
    private void initReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("download_db");
        context.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    /**
     * 进度条广播接收器
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int percent = intent.getIntExtra("percent", 0);
            String content = intent.getStringExtra("content");
            boolean isProcess = intent.getBooleanExtra("isProcess", false);
            // 显示说明文字
            if (ConverterUtil.isNotEmpty(content)) {
                mLoading.setMessage(content);
            }
            // 是否为当前总进度
            if (isProcess) {
                mLoading.setProgress(percent);
            } else {
                // 如果只是增量则调用增加进度
                mLoading.incrementProgressBy(percent);
            }

            // 如果进度等于100证明进度完成
            if (mLoading.getProgress() >= 100) {
                mLoading.setMessage(context.getResources().getText(R.string.msg_download_complete));
                mLoading.dismiss();
            }
        }
    };
}
