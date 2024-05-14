
package hd.produce.security.cn;

import com.google.gson.Gson;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import hd.produce.security.cn.data.MonitoringPadEntity;
import hd.produce.security.cn.service.PullService;
import hd.source.task.DataManager;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.source.task.WebServiceUtil;
import hd.utils.cn.Constants;
import hd.utils.cn.LogUtil;
import hd.utils.cn.StringUtils;
import hd.utils.cn.Utils;

/**
 * @author Administrator 登陆页
 */
public class LoginActivity extends Activity implements View.OnClickListener, ITaskListener {

    private static final String TAG = "LoginActivity";

    private static final int MSG_LOGIN = 1;

    private static final int MSG_INTENT = 2;

    private ImageView mSaveUserImage;

    private EditText mEditUser;

    private EditText mEditPwd;

    private Button mBtnLogin;

    private boolean isSaveUser = true;

    private String mStrUser;

    private String mStrPwd;

    ProgressDialog mLoading;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOGIN:
                    runLoginTask();
                    break;
                case MSG_INTENT:
                    sendIntent(ProjectListActivity.class);
                    break;

                default:
                    break;
            }
        }
    };

    private void runLoginTask() {
        mLoading = Utils.showProgress(this, null,
                this.getResources().getString(R.string.msg_loading_longin), false, true);
        TaskHelper.login(this, this, mStrUser, mStrPwd);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        HdCnApp.getApplication().addActivity(this);
        isSaveUser = Utils.getBooleanPrefrence(this, Constants.SP_KEY_ISSAVE);
        initViews();
        String name = Utils.getPreference(this, Constants.SP_KEY_USERNAME);
        if (isSaveUser && !TextUtils.isEmpty(name)) {
            mEditUser.setText(name);
        }
        // TODO:测试，发版前删掉
//         mEditUser.setText("chouyang_sz");
//         mEditPwd.setText("123456");
    }

    private void initViews() {
        mSaveUserImage = (ImageView) this.findViewById(R.id.image_save_user);
        if (isSaveUser) {
            mSaveUserImage.setImageResource(R.drawable.checkbox_select);
        } else {
            mSaveUserImage.setImageResource(R.drawable.checkbox_empty);
        }
        mSaveUserImage.setOnClickListener(this);
        mEditUser = (EditText) this.findViewById(R.id.edit_user);
        mEditUser.setOnClickListener(this);
        mEditPwd = (EditText) this.findViewById(R.id.edit_pwd);
        mEditPwd.setOnClickListener(this);
        mBtnLogin = (Button) this.findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
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
            case R.id.image_save_user:
                if (isSaveUser) {
                    mSaveUserImage.setImageResource(R.drawable.checkbox_empty);
                    isSaveUser = false;
                } else {
                    mSaveUserImage.setImageResource(R.drawable.checkbox_select);
                    isSaveUser = true;
                }
                break;
            case R.id.btn_login:
                mStrUser = mEditUser.getText().toString().trim();
                mStrPwd = mEditPwd.getText().toString().trim();
                if (TextUtils.isEmpty(mStrUser) || TextUtils.isEmpty(mStrPwd)) {
                    Utils.showToast(this, this.getResources().getString(R.string.text_input_empty));
                    break;
                }
                if (StringUtils.checkFull(mStrUser) || StringUtils.checkChinese(mStrUser)
                        || StringUtils.checkFull(mStrPwd) || StringUtils.checkChinese(mStrPwd)) {
                    Utils.showToast(this, this.getResources().getString(R.string.text_input_error));
                } else {
                    mHandler.sendEmptyMessage(MSG_LOGIN);
                }
                break;
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void sendIntent(Class<?> cls) {
        LogUtil.i(TAG, "sendIntent to:" + cls.getName());
        Utils.dismissDialog(mLoading);
        Utils.setBooleanPrefrence(this, Constants.SP_KEY_ISSAVE, isSaveUser);
        if (isSaveUser) {
            Utils.savePreference(this, Constants.SP_KEY_USERNAME, mStrUser);
        }
        Intent mIntent = new Intent();
        mIntent.setClass(LoginActivity.this, cls);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onTaskFinish(int result, int request, Object entity) {
        if (result == ITaskListener.RESULT_OK) {
            try {
                LogUtil.i(TAG, entity.toString());
                MonitoringPadEntity resultEntity = WebServiceUtil
                        .checkLogInInfo((SoapObject) entity);
                if (resultEntity != null && TextUtils.equals(resultEntity.getFlg(), "0")) {
                    DataManager.getInstance().setMonitoringPadEntity(resultEntity);
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(resultEntity, MonitoringPadEntity.class);
                    Utils.savePreference(this, Constants.SP_KEY_AUTHINFO, jsonStr);
                    Utils.setBooleanPrefrence(this, Constants.SP_KEY_AUTHORIZAD, true);
                    mHandler.sendEmptyMessage(MSG_INTENT);
                    startPullService();
                } else {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            Utils.showTips(LoginActivity.this, LoginActivity.this.getResources()
                                    .getString(R.string.msg_login_error));
                            Utils.dismissDialog(mLoading);
                        }

                    });
                }
            } catch (Exception e) {
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        Utils.showTips(LoginActivity.this, LoginActivity.this.getResources()
                                .getString(R.string.msg_login_error));
                        Utils.dismissDialog(mLoading);
                    }

                });
            }

        } else {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Utils.showTips(LoginActivity.this,
                            LoginActivity.this.getResources().getString(R.string.msg_login_faild));
                    Utils.dismissDialog(mLoading);
                }

            });

        }

    }

    /**
     * 登陆成功之后开启轮询服务来获取转发的消息
     * 
     * @Title: startPullService
     * @return void
     * @date 2013-12-11 上午10:39:02
     */
    private void startPullService() {
        Intent intent = new Intent(LoginActivity.this, PullService.class);
//        startService(intent);
    }
}
