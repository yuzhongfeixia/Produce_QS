package hd.source.task;

import hd.produce.security.cn.data.MonitoringProjectEntity;
import hd.produce.security.cn.data.MonitoringTaskDetailsEntity;
import hd.produce.security.cn.data.SampleInfoEntity;
import hd.source.cn.AsyncTask;
import hd.utils.cn.Constants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;

public class TaskHelper {

    // REQUEST
    public final static int REQUEST_LOGIN = 1;

    public final static int REQUEST_GET_PROINFO_LIST = REQUEST_LOGIN + 1;

    public final static int REQUEST_GET_TASKINFO_LIST = REQUEST_GET_PROINFO_LIST + 1;

    public final static int REQUEST_SEARCH_TASKINFO_LIST = REQUEST_GET_TASKINFO_LIST + 1;

    public final static int REQUEST_GET_MONITOR_NAME = REQUEST_SEARCH_TASKINFO_LIST + 1;

    public final static int REQUEST_GET_TASKINFO = REQUEST_GET_MONITOR_NAME + 1;

    public final static int REQUEST_GET_AREA = REQUEST_GET_TASKINFO + 1;

    public final static int REQUEST_GET_LINK = REQUEST_GET_AREA + 1;

    public final static int REQUEST_GET_BREED = REQUEST_GET_LINK + 1;

    public final static int REQUEST_SAVE_SAMPLEINFO = REQUEST_GET_BREED + 1;

    public final static int REQUEST_FORWARD_TASKINFO = REQUEST_SAVE_SAMPLEINFO + 1;

    public final static int REQUEST_GET_PAD_LIST = REQUEST_FORWARD_TASKINFO + 1;

    public final static int REQUEST_CHANGE_PWD = REQUEST_GET_PAD_LIST + 1;

    public final static int REQUEST_GET_FINISHI_SAMPLES = REQUEST_CHANGE_PWD + 1;

    public final static int REQUEST_GET_RECEIVER_MQ = REQUEST_GET_FINISHI_SAMPLES + 1;

    public final static int REQUEST_SEARCH_PROINFO_LIST = REQUEST_GET_RECEIVER_MQ + 1;

    public final static int REQUEST_GET_SYS_AREA = REQUEST_SEARCH_PROINFO_LIST + 1;

    public final static int REQUEST_GET_LINK_INFO = REQUEST_GET_SYS_AREA + 1;

    public final static int REQUEST_GET_All_MONITOR_SITE = REQUEST_GET_LINK_INFO + 1;

    public final static int REQUEST_CHECK_DCODE = REQUEST_GET_All_MONITOR_SITE + 1;

    public final static int REQUEST_GET_SAMPLE_NAME = REQUEST_CHECK_DCODE + 1;
    /**
     * 取得数据库文件
     */
    public final static int REQUEST_GET_DATABASE = REQUEST_GET_SAMPLE_NAME + 1;
    /** 保存样品图片 */
    public final static int REQUEST_SAVE_SAMPLE_IMAGE = REQUEST_GET_DATABASE + 1;
    /** 取得webContext路径 */
    public final static int REQUEST_GET_WEBCONTEXT_PATH = REQUEST_SAVE_SAMPLE_IMAGE + 1;

    private static ExecutorService LIMITED_TASK_EXECUTOR;

    public static HashMap<Context, ArrayList<WeakReference<AsyncTask>>> mTaskMap = new HashMap<Context, ArrayList<WeakReference<AsyncTask>>>();

    public static void cancelTask(Context context) {
        if (mTaskMap.containsKey(context)) {
            ArrayList<WeakReference<AsyncTask>> tasks = mTaskMap.get(context);
            for (WeakReference<AsyncTask> baseHttpTask : tasks) {
                AsyncTask task = baseHttpTask.get();
                if (null != task) {
                    task.cancel(true);
                }
            }
            tasks.clear();
            mTaskMap.remove(context);
        }
    }

    private static void initExecutor() {
        if (null == LIMITED_TASK_EXECUTOR) {
            LIMITED_TASK_EXECUTOR = (ExecutorService) Executors.newFixedThreadPool(7);
        }
    }

    private static void addTask(Context context, AsyncTask task) {
        if (null == context) {
            return;
        }
        if (!mTaskMap.containsKey(context)) {
            ArrayList<WeakReference<AsyncTask>> list = new ArrayList<WeakReference<AsyncTask>>();
            list.add(new WeakReference<AsyncTask>(task));
            mTaskMap.put(context, list);
        } else {
            mTaskMap.get(context).add(new WeakReference<AsyncTask>(task));
        }
    }

    public static void login(Context context, ITaskListener listener, String user, String pwd) {
        SoapObject request = WebServiceUtil.checkLogInInfoRequest(user, pwd);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_LOGIN);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getProjectList(Context context, ITaskListener listener, String orgCode, String padId, String projectName, int requestCode) {
        SoapObject request = WebServiceUtil.getProjectInfoRequest(orgCode, padId, projectName);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, requestCode);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getLocalTask(Context context, ITaskListener listener, int taskMethod, Map<String, Object> param) {
        LocalDBTask task = new LocalDBTask(taskMethod, param);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getAllMonitoringSite(Context context, ITaskListener listener) {
        SoapObject request = WebServiceUtil.getAllMonitoringSiteRequest();
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_All_MONITOR_SITE);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getTaskList(Context context, ITaskListener listener, MonitoringProjectEntity paramEntity, int requestCode) {
        SoapObject request = WebServiceUtil.getTaskInfoListRequest(paramEntity);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, requestCode);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getFinishTaskList(Context context, ITaskListener listener, String padId, String taskCode, String sampleMonadType, String projectCode) {
        SoapObject request = WebServiceUtil.getFinishSamplingInfoRequest(padId, taskCode, sampleMonadType, projectCode);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_FINISHI_SAMPLES);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getTaskInfo(Context context, ITaskListener listener, String taskId, String padId) {
        SoapObject request = WebServiceUtil.getTaskInfoRequest(taskId, padId);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_TASKINFO);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void saveSample(Context context, ITaskListener listener, SampleInfoEntity sampleInfoEntity, String sampleMonadType) {
        SoapObject request = WebServiceUtil.saveSampleInfoRequest(sampleInfoEntity, sampleMonadType);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_SAVE_SAMPLEINFO, Constants.HTTP_SAVE_SAMPLE_TIMEOUT);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    /**
     * 提交样品图片
     * 
     * @param context
     * @param listener
     * @param imgContentMap
     */
    public static void saveSampleImage(Context context, ITaskListener listener, Map<String, String> imgContentMap) {
        SoapObject request = WebServiceUtil.saveSampleImageRequest(imgContentMap);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_SAVE_SAMPLE_IMAGE, Constants.HTTP_SAVE_SAMPLE_TIMEOUT);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getNameForMonitorType(Context context, ITaskListener listener, String monitorType) {
        SoapObject request = WebServiceUtil.getNameForMonitorTypeRequest(monitorType);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_MONITOR_NAME);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getMonitoringLink(Context context, ITaskListener listener, String taskCode) {
        SoapObject request = WebServiceUtil.getMonitoringLinkRequest(taskCode);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_LINK);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getMonitoringBreedForTask(Context context, ITaskListener listener, String taskCode, String projectCode) {
        SoapObject request = WebServiceUtil.getMonitoringBreedForTaskRequest(taskCode, projectCode);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_BREED);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getSampleNameForTask(Context context, ITaskListener listener, String projectCode) {
        SoapObject request = WebServiceUtil.getSampleNameForTaskRequest(projectCode);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_SAMPLE_NAME);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getMonitoringArea(Context context, ITaskListener listener, String taskCode) {
        SoapObject request = WebServiceUtil.getMonitoringAreaRequest(taskCode);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_AREA);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void forwardTask(Context context, ITaskListener listener, MonitoringTaskDetailsEntity paramMonitoringTaskDetailsEntity, String padId, int total, String flagStr) {
        SoapObject request = WebServiceUtil.forwardTaskRequest(paramMonitoringTaskDetailsEntity, padId, total, flagStr);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_FORWARD_TASKINFO);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void receiverMQ(Context context, ITaskListener listener, String padId) {
        SoapObject request = WebServiceUtil.receiverMQRequest(padId);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_RECEIVER_MQ);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getOtherPadInfo(Context context, ITaskListener listener, String padId, String taskCode) {
        SoapObject request = WebServiceUtil.getOtherPadInfoRequest(padId, taskCode);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_PAD_LIST);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void modifyUserInfo(Context context, ITaskListener listener, String userName, String oldPassWord, String newPassWord) {
        SoapObject request = WebServiceUtil.modifyUserInfoRequest(userName, oldPassWord, newPassWord);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_CHANGE_PWD);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getSysMonitoringArea(Context context, ITaskListener listener) {
        SoapObject request = WebServiceUtil.getSysMonitoringAreaRequest();
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_SYS_AREA);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getLinkInfo(Context context, ITaskListener listener, String industryCode) {
        SoapObject request = WebServiceUtil.getLinkInfoRequest(industryCode);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_LINK_INFO);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void checkDCode(Context context, ITaskListener listener, String dCode, String projectCode) {
        SoapObject request = WebServiceUtil.checkDCode(dCode, projectCode);
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_CHECK_DCODE);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    public static void getDatabase(Context context, ITaskListener listener, int timeOut) {
        SoapObject request = WebServiceUtil.getDatabase();
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_DATABASE, timeOut);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }

    /**
     * 取得WebContext路径
     * 
     * @param context
     * @param listener
     */
    public static void getWebContextPath(Context context, ITaskListener listener) {
        SoapObject request = WebServiceUtil.getWebContextPath();
        BaseWebServiceTask task = new BaseWebServiceTask(context, request, REQUEST_GET_WEBCONTEXT_PATH);
        initExecutor();
        task.executeOnExecutor(LIMITED_TASK_EXECUTOR, listener);
        addTask(context, task);
    }
}
