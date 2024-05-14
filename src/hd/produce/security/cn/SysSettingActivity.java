
package hd.produce.security.cn;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hd.source.task.DataManager;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.source.task.WebServiceUtil;
import hd.utils.cn.Constants;
import hd.utils.cn.StringUtils;
import hd.utils.cn.Utils;

public class SysSettingActivity extends Activity implements View.OnClickListener, ITaskListener {

    private static final int MSG_BUTTON_ENABLE = 1;

    private static final int MSG_ERROR_INPUT = 2;

    private static final int MSG_ERROR_PWD = 3;

    private Button mBtnBack;

    private TextView mTvTittle;

    private Button mBtnSwitch;

    private TextView mTvUserName;
    
    private TextView mTvCity;

    private RelativeLayout mRlChangePwd;

    private Button mBtnLogout;

    private CustomDialog mDialog;

    private TextView mVersion;
    
    private int mLockKey;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_BUTTON_ENABLE:
                    mBtnSwitch.setEnabled(true);
                    break;
                case MSG_ERROR_INPUT:
                    Utils.showTips(SysSettingActivity.this, "");
                    break;
                case MSG_ERROR_PWD:
                    Utils.showTips(SysSettingActivity.this, "");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        HdCnApp.getApplication().addActivity(this);
        initData();
        initViews();

    }

    private void initData() {
        mLockKey = Utils.getPreferenceInt(this, Constants.SP_KEY_CHANGEPWD_LOCK,
                Constants.CHANGEPWD_LOCK_ON);

    }

    private void initViews() {
        mBtnBack = (Button) findViewById(R.id.back);
        mBtnBack.setOnClickListener(this);
        mTvTittle = (TextView) findViewById(R.id.title_text);
        mTvTittle.setText(R.string.sys_setting);
        mTvUserName = (TextView) this.findViewById(R.id.user_name);
        mTvUserName.setText(DataManager.getInstance().getMonitoringPadEntity().getUsername());
        mTvCity = (TextView) this.findViewById(R.id.cur_city);
        String orgName = DataManager.getInstance().getMonitoringPadEntity().getOrgName();
        if (!StringUtils.isEmpty(orgName)) {
            mTvCity.setText(orgName);
        }
        mRlChangePwd = (RelativeLayout) this.findViewById(R.id.rl_change_pwd);
//        mBtnSwitch = (Button) this.findViewById(R.id.btn_switch);
//        mBtnSwitch.setOnClickListener(this);
//        if (mLockKey == Constants.CHANGEPWD_LOCK_OFF) {
//            mRlChangePwd.setVisibility(View.GONE);
//            mBtnSwitch.setBackgroundResource(R.drawable.switch_close);
//        } else {
//            mRlChangePwd.setVisibility(View.VISIBLE);
//            mBtnSwitch.setBackgroundResource(R.drawable.switch_open);
//        }
        mRlChangePwd.setOnClickListener(this);
        mVersion = (TextView) this.findViewById(R.id.lable_version);
        mVersion.setText(HdCnApp.VERSION_NAME);
//        mBtnSwitch = (Button) this.findViewById(R.id.btn_switch);
//        mBtnSwitch.setOnClickListener(this);
        mBtnLogout = (Button) this.findViewById(R.id.btn_logout);
        mBtnLogout.setOnClickListener(this);

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
//            case R.id.btn_switch:
//                mBtnSwitch.setEnabled(false);
//                switch (mLockKey) {
//                    case Constants.CHANGEPWD_LOCK_OFF:
//                        mRlChangePwd.setVisibility(View.VISIBLE);
//                        mBtnSwitch.setBackgroundResource(R.drawable.switch_open);
//                        mLockKey = Constants.CHANGEPWD_LOCK_ON;
//                        Utils.savePreferenceInt(this, Constants.SP_KEY_CHANGEPWD_LOCK,
//                                Constants.CHANGEPWD_LOCK_ON);
//                        break;
//                    case Constants.CHANGEPWD_LOCK_ON:
//                        mRlChangePwd.setVisibility(View.GONE);
//                        mBtnSwitch.setBackgroundResource(R.drawable.switch_close);
//                        mLockKey = Constants.CHANGEPWD_LOCK_OFF;
//                        Utils.savePreferenceInt(this, Constants.SP_KEY_CHANGEPWD_LOCK,
//                                Constants.CHANGEPWD_LOCK_OFF);
//                        break;
//                    default:
//                        break;
//                }
//                mHandler.sendEmptyMessageDelayed(MSG_BUTTON_ENABLE, 200);
//                break;
            case R.id.btn_logout:
                Utils.showExitDialog(this, new Utils.OnDialogDoneListener() {
                    @Override
                    public void onDialogDone() {
                        Utils.removePreference(SysSettingActivity.this, Constants.SP_KEY_AUTHINFO);
                        Utils.removePreference(SysSettingActivity.this, Constants.SP_KEY_AUTHORIZAD);
                        Intent intent = new Intent();
                        intent.setClass(SysSettingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        HdCnApp.getApplication().clearActivity();
                        DataManager.getInstance().cleanProject();
//                        finish();
                    }
                });
                break;
            case R.id.rl_change_pwd:
                mDialog = new CustomDialog(this, R.layout.change_pwd_dialog,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mDialog != null && mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mDialog != null && mDialog.isShowing()) {
                                    mDialog.commitPwd();
                                }
                            }
                        });
                mDialog.setCancelable(true);
                mDialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTaskFinish(int result, int request, Object entity) {
        if (result == ITaskListener.RESULT_OK) {
            try {
                boolean isSuccess = WebServiceUtil.modifyUserInfo((SoapObject) entity);
                if (isSuccess) {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            Utils.showTips(SysSettingActivity.this, SysSettingActivity.this
                                    .getResources().getString(R.string.msg_change_pwd_ok));
                            if (mDialog != null && mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                        }

                    });
                } else {
                    Utils.showTips(SysSettingActivity.this, SysSettingActivity.this.getResources()
                            .getString(R.string.msg_change_pwd_err));
                }
            } catch (Exception e) {
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        Utils.showTips(SysSettingActivity.this, SysSettingActivity.this
                                .getResources().getString(R.string.msg_load_faild));
                    }

                });
            }

        } else {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    Utils.showTips(SysSettingActivity.this, SysSettingActivity.this.getResources()
                            .getString(R.string.msg_load_faild));
                }

            });

        }

    }

    class CustomDialog extends Dialog {
        int layoutRes;// 布局文件

        Context context;

        private EditText mOldPwd;

        private EditText mNewPwd;

        private EditText mCommitPwd;

        private Button mCancle;

        private Button mCommit;

        View.OnClickListener mCancleListener;

        View.OnClickListener mCommitListener;

        public CustomDialog(Context context) {
            super(context);
            this.context = context;
        }

        /**
         * 自定义布局的构造方法
         * 
         * @param context
         * @param resLayout
         */
        public CustomDialog(Context context, int resLayout, View.OnClickListener cancleListener,
                View.OnClickListener commitListener) {
            super(context);
            this.context = context;
            this.layoutRes = resLayout;
            this.mCancleListener = cancleListener;
            this.mCommitListener = commitListener;
        }

        /**
         * 自定义主题及布局的构造方法
         * 
         * @param context
         * @param theme
         * @param resLayout
         */
        public CustomDialog(Context context, int theme, int resLayout) {
            super(context, theme);
            this.context = context;
            this.layoutRes = resLayout;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(layoutRes);
            this.setTitle(R.string.change_pwd);
            mCancle = (Button) this.findViewById(R.id.btn_cancle);
            mCommit = (Button) this.findViewById(R.id.btn_commit);
            mCancle.setOnClickListener(mCancleListener);
            mCommit.setOnClickListener(this.mCommitListener);
            mOldPwd = (EditText) this.findViewById(R.id.et_old_pwd);
            mNewPwd = (EditText) this.findViewById(R.id.et_new_pwd);
            mCommitPwd = (EditText) this.findViewById(R.id.et_commit_pwd);
        }

        public void commitPwd() {
            String oldPwd = mOldPwd.getText().toString().trim();
            String newPwd = mNewPwd.getText().toString().trim();
            String commitPwd = mCommitPwd.getText().toString().trim();
            if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd)
                    || TextUtils.isEmpty(commitPwd)) {
                mHandler.sendEmptyMessage(MSG_ERROR_INPUT);
                return;
            }
            if (!newPwd.equals(commitPwd)) {
                mHandler.sendEmptyMessage(MSG_ERROR_PWD);
                return;
            }
            TaskHelper.modifyUserInfo(this.context, SysSettingActivity.this, DataManager
                    .getInstance().getMonitoringPadEntity().getUsername(), oldPwd, commitPwd);
        }
    }
}
