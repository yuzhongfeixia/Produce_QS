package hd.produce.security.cn;

import hd.produce.security.cn.data.MonitoringProjectEntity;
import hd.produce.security.cn.entity.TbMonitoringProject;
import hd.produce.security.cn.fragment.TaskListAllFragment;
import hd.produce.security.cn.fragment.TaskListGroupFragment;
import hd.produce.security.cn.fragment.TaskListSearchFragment;
import hd.source.task.DataManager;
import hd.source.task.DownloadAllData;
import hd.source.task.DownloadAllData.CompleteCallback;
import hd.source.task.ITaskListener;
import hd.source.task.TaskHelper;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.DownloadBroadcast;
import hd.utils.cn.NetworkControl;
import hd.utils.cn.Utils;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

/**
 * @author Administrator 主功能列表页
 */
public class ProjectListActivity extends FragmentActivity implements View.OnClickListener, ITaskListener {

    private static final String TAG = "ProjectListActivity";

    private static final int MSG_INIT_DATA = 1;

    private ImageButton mBtnSetting;

    private RadioButton group;

    private RadioButton all;

    private Fragment mContent;

    private String mOrgCode;

    private String mPadId;

    private String mProjectCode;

    private String mSearchKey;

    private ProgressDialog loadSite;

    private final int FRAGMENT_GROUP = 1;

    private final int FRAGMENT_ALL = 2;

    private int fragmentType = FRAGMENT_GROUP;
    private DownloadBroadcast dbc;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_INIT_DATA:
                loadSite = Utils.showProgress(ProjectListActivity.this, null, ProjectListActivity.this.getResources().getString(R.string.msg_extends_monintring_Site), false, false);
                TaskHelper.getLocalTask(ProjectListActivity.this, ProjectListActivity.this, DataManager.TASK_GET_MONITORING_SITE, null);
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);
        HdCnApp.getApplication().addActivity(this);
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
        if (mContent == null) {
            initFragment();
        }
        mOrgCode = DataManager.getInstance().getMonitoringPadEntity().getOrgCode();
        mPadId = DataManager.getInstance().getMonitoringPadEntity().getId();
        switchContent(mContent);
        initViews();
        setListeners();
        dbc = new DownloadBroadcast(ProjectListActivity.this);
        getProjectList();
        // findViewById(R.id.btn_download).performClick();
        // videoBridgeRecive();
    }

    private void initFragment() {
        if (fragmentType == FRAGMENT_GROUP) {
            mContent = TaskListGroupFragment.newInstance();
        } else {
            mContent = TaskListAllFragment.newInstance();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    private void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void initViews() {
        // mEditSearch = (EditText) this.findViewById(R.id.edit_search);
        mBtnSetting = (ImageButton) this.findViewById(R.id.btn_setting);
        // mBtnSearch = (ImageButton) this.findViewById(R.id.btn_search);
        group = (RadioButton) findViewById(R.id.btn_switch_group);
        all = (RadioButton) findViewById(R.id.btn_switch_all);
        group.setChecked(true);

    }

    private void setListeners() {
        mBtnSetting.setOnClickListener(this);
        // mBtnSearch.setOnClickListener(this);
        group.setOnClickListener(this);
        all.setOnClickListener(this);
        findViewById(R.id.btn_download).setOnClickListener(this);
    }

    private void sendIntent(Class<?> cls) {
        Intent mIntent = new Intent();
        mIntent.setClass(ProjectListActivity.this, cls);
        startActivity(mIntent);
    }

    private void onGroupClick() {
        fragmentType = FRAGMENT_GROUP;
        initFragment();
        switchContent(mContent);
    }

    private void onAllClick() {
        fragmentType = FRAGMENT_ALL;
        initFragment();
        switchContent(mContent);
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
        case R.id.btn_setting:
            sendIntent(SysSettingActivity.class);
            break;
        case R.id.btn_switch_group:
            onGroupClick();
            break;
        case R.id.btn_switch_all:
            onAllClick();
            break;
        // case R.id.btn_search:
        // mSearchKey = mEditSearch.getText().toString().trim();
        // mHandler.sendEmptyMessage(MSG_SEARCH_DATA);
        // if (fragmentType == FRAGMENT_ALL) {
        //
        // } else {
        // ((TaskListGroupFragment) mContent).getFilter().filter(mSearchKey);
        // }
        // break;
        case R.id.btn_download:
            // 下载所有数据到本地
            // mLoading = Utils.showProgress(ProjectListActivity.this, null, ProjectListActivity.this.getResources().getString(R.string.msg_loading_data), true, false);
            if(!NetworkControl.checkNet(ProjectListActivity.this)){
                Utils.showTips(this, getResources().getString(R.string.msg_net_error));
                return;
            }
            dbc.show();
            // new Thread() {
            // public void run() {
            // while (dbc.getProgress() != 100) {
            // try {
            // // Intent temIntent = new Intent("download_db");
            // // Bundle bundle = new Bundle();
            // // bundle.putInt("percent", ConverterUtil.toInteger(ConverterUtil.getCheckCode(1)));
            // int percent = ConverterUtil.toInteger(ConverterUtil.getCheckCode(1));
            // if (dbc.getProgress() < 10) {
            // // bundle.putString("content", "正在加载行政区划数据....");
            // DownloadBroadcast.sendBroadcast(percent, "正在加载行政区划数据....");
            // } else if (dbc.getProgress() > 10 && dbc.getProgress() < 20) {
            // // bundle.putString("content", "正在加载受检单位数据....");
            // DownloadBroadcast.sendBroadcast(percent, "正在加载受检单位数据....");
            // } else if (dbc.getProgress() > 20 && dbc.getProgress() < 70) {
            // DownloadBroadcast.sendBroadcast(percent, "正在加载项目数据....");
            // // bundle.putString("content", "正在加载项目数据....");
            // } else if (dbc.getProgress() > 70 && dbc.getProgress() < 100) {
            // DownloadBroadcast.sendBroadcast(percent, "正在加载任务数据....");
            // // bundle.putString("content", "正在加载任务数据....");
            // }
            // // temIntent.putExtras(bundle);
            // // HdCnApp.getApplication().sendBroadcast(temIntent);
            // Thread.sleep(500);
            // } catch (InterruptedException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // }
            // }
            // }.start();
            DownloadAllData.getInstance(this).setCompleteCallback(new CompleteCallback() {
                @Override
                public void downloadCompleted(int code) {
                    if (code == DownloadAllData.SUCCESS) {
                        getProjectList();
                        if (fragmentType == FRAGMENT_ALL) {
                            ((TaskListAllFragment) mContent).update();
                            ((TaskListAllFragment) mContent).getProjectListComplete();
                        } else {
                            ((TaskListGroupFragment) mContent).update();
                            ((TaskListGroupFragment) mContent).getProjectListComplete();
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showTips(ProjectListActivity.this, ProjectListActivity.this.getResources().getString(R.string.msg_load_data));
                            }
                        });
                    } else if (code == DownloadAllData.NO_DATA) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showTips(ProjectListActivity.this, ProjectListActivity.this.getResources().getString(R.string.msg_load_no_data));
                            }
                        });
                    } else {
                        if (fragmentType == FRAGMENT_ALL) {
                            ((TaskListAllFragment) mContent).update();
                            ((TaskListAllFragment) mContent).getProjectListComplete();
                        } else {
                            ((TaskListGroupFragment) mContent).update();
                            ((TaskListGroupFragment) mContent).getProjectListComplete();
                        }
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showTips(ProjectListActivity.this, ProjectListActivity.this.getResources().getString(R.string.msg_load_faild));
                            }
                        });

                    }
                }
            });
            DownloadAllData.getInstance(this).download();
            break;
        default:
            break;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onTaskFinish(int result, final int request, Object entity) {
        if (result == ITaskListener.RESULT_OK) {
            if (request == DataManager.TASK_GET_PROJECT_LIST) {
                List<TbMonitoringProject> results = (List<TbMonitoringProject>) entity;
                if (ConverterUtil.isEmpty(results)) {
//                    // 展开受检单位
//                    mHandler.sendEmptyMessage(MSG_INIT_DATA);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showTips(ProjectListActivity.this, ProjectListActivity.this.getResources().getString(R.string.msg_no_project));
                        }
                    });
                    return;
                } else {
                }
                DataManager.getInstance().getRoutinemonitoringEntityList().clear();
                DataManager.getInstance().getGeneralcheckEntityList().clear();
                DataManager.getInstance().getSpecialTaskEntityList().clear();
                DataManager.getInstance().getSuperviseCheckEntityList().clear();
                for (TbMonitoringProject project : results) {
                    int type = Integer.valueOf(project.getType());
                    switch (type) {
                    case 1:
                        DataManager.getInstance().getRoutinemonitoringEntityList().add(project);
                        break;
                    case 2:
                        DataManager.getInstance().getGeneralcheckEntityList().add(project);
                        break;
                    case 3:
                        DataManager.getInstance().getSpecialTaskEntityList().add(project);
                        break;
                    case 4:
                        DataManager.getInstance().getSuperviseCheckEntityList().add(project);
                        break;
                    default:
                        break;
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (fragmentType == FRAGMENT_ALL) {
                            ((TaskListAllFragment) mContent).update();
                            ((TaskListAllFragment) mContent).getProjectListComplete();
                        } else {
                            ((TaskListGroupFragment) mContent).update();
                            ((TaskListGroupFragment) mContent).getProjectListComplete();
                        }
                    }
                });
            }
            if (request == DataManager.TASK_GET_MONITORING_SITE) {
                Utils.dismissDialog(loadSite);
            }
        } else if (result == ITaskListener.RESULT_NET_ERROR) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
//                    Utils.showTips(ProjectListActivity.this, ProjectListActivity.this.getResources().getString(R.string.msg_load_faild));
                    // 所有异常都按照网络异常处理
                    Utils.showTips(ProjectListActivity.this, ProjectListActivity.this.getResources().getString(R.string.msg_net_error_retry));
                    loadOver(request);
                }
            });
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Utils.showTips(ProjectListActivity.this, ProjectListActivity.this.getResources().getString(R.string.msg_load_no_data));
                    loadOver(request);
                }
            });
        }

    }

    private void loadOver(int request) {
        if (request == TaskHelper.REQUEST_GET_PROINFO_LIST) {
            if (fragmentType == FRAGMENT_ALL) {
                // ((TaskListAllFragment) mContent).update();
                ((TaskListAllFragment) mContent).getProjectListComplete();
            } else {
                ((TaskListGroupFragment) mContent).getProjectListComplete();
            }
        } else if (request == TaskHelper.REQUEST_SEARCH_PROINFO_LIST) {
            ((TaskListSearchFragment) mContent).getProjectListComplete();
        }
    }

    public void getProjectList() {
        TaskHelper.getLocalTask(ProjectListActivity.this, ProjectListActivity.this, DataManager.TASK_GET_PROJECT_LIST, null);
    }

    public void getSearchList() {
        MonitoringProjectEntity entity = new MonitoringProjectEntity();
        entity.setProjectCode(mProjectCode);
        entity.setTaskName(mSearchKey);
        entity.setPadId(DataManager.getInstance().getMonitoringPadEntity().getId());
        TaskHelper.getTaskList(ProjectListActivity.this, ProjectListActivity.this, entity, TaskHelper.REQUEST_SEARCH_TASKINFO_LIST);
        TaskHelper.getProjectList(ProjectListActivity.this, ProjectListActivity.this, mOrgCode, mPadId, mSearchKey, TaskHelper.REQUEST_SEARCH_PROINFO_LIST);
    }
}
