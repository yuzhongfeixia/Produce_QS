
package hd.utils.cn;

import android.os.Environment;

public class Constants {
    public static final String STR_EMPTY = "";

    public static final String STR_MD5 = "MD5";

    public static final String STR_NEW = "NEW";

    public static final String NET_EMPTY_DATA = "net_empty_data";

    public static final String NET_NONE = "net_none";

    public static final String NET_FAILED = "net_failed";

    public static final String STR_ERROR_CON = "ERROR_CON";

    public static final String STR_ALREADY = "ALREADY";

    public static final String STR_FULL = "FULL";

    public static final String DEFAULT_THUMB = "DEFAULT_THUMB";

    public static final String NETWORK_TYPE = "NETWORK_TYPE";

    /** webContext路径 */
    public static final String SP_KEY_WEBCONTEXTPATH = "WebContextPath";
    
    public static final String SP_KEY_AUTHINFO = "AuthInfo";

    public static final String SP_KEY_USERNAME = "UserName";

    public static final String SP_KEY_ISSAVE = "IsSaveName";

    public static final String SP_KEY_AUTHORIZAD = "Authorized";

    public static final String SP_KEY_CHANGEPWD_LOCK = "CHANGEPWD_LOCK";

    public static final String SP_KEY_SYS_AREA = "SysAreaCode";

    public static final String SP_KEY_DONING_SAMPLE_SIZE = "DONING_SAMPLE_SIZE";

    public static final String SP_KEY_DONING_SAMPLE = "DONING_SAMPLE";
    /** 缓存Key:受检单位 */
    public static final String SP_KEY_MONITORING_SITE = "SP_KEY_MONITORING_SITE";

    public static final String RAW_MILK_FLAG = "RAW_MILK_FLAG";

    public static final int CHANGEPWD_LOCK_ON = 0;

    public static final int CHANGEPWD_LOCK_OFF = 1;

    public static final String INTENT_KEY = "INTENT_TO_SL";

    public static final String INTENT_KEY_DOING = "IS_DOING";

    public static final String INTENT_KEY_SAMPLETYPE = "SAMPLETYPE";

    public static final String INTENT_KEY_TASKCODE = "TASKCODE";

    public static final String INTENT_KEY_TASKNAME = "TASKNAME";

    public static final String INTENT_KEY_PROJECTID = "PROJECTID";

    public static final String INTENT_KEY_PO = "INTENT_KEY_PO";
    
    /** 日期控件回调 */
    public static final String INTENT_CALL_BACK_DATEPICK = "INTENT_CALL_BACK_DATEPICK";
    
    /** 是否为添加抽样单 */
    public static final String INTENT_KEY_ADD_SAMPLE = "IS_ADD";
    /** 选中样品ID*/
    public static final String INTENT_KEY_SELECT_SAMPLE_ID = "SELECT_SAMPLE_ID";
    /** 选中抽样单ID*/
    public static final String INTENT_KEY_SELECT_SAMPLE_MONAD_ID = "SELECT_SAMPLE_MONAD_ID";

    public static final int INTENT_TYPE_DOING = 1;

    public static final int INTENT_TYPE_DONE = 2;

    public static final int ROUTINE_SAMPLE_TYPE = 1;

    public static final int SUPERVISE_SAMPLE = 2;

    public static final int RISK_SAMPLE = 3;

    public static final int RAW_MILK_SAMPLE = 4;

    public static final int LIVE_STOCK_SAMPLE = 5;

    // webservice methodName
    public static final int HTTP_SO_TIMEOUT = 10000;
    
    public static final int HTTP_SAVE_SAMPLE_TIMEOUT = 5 * 60 * 1000;

    public static final String XML_ENC = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    public static final String METHOD_NAME_GET_TASKINFO_LIST = "getMonitoringTaskList";

    public static final String METHOD_NAME_GET_MONITOR_NAME = "getNameForMonitorType";

    public static final String METHOD_NAME_SAVE_SAMPLEINFO = "saveSamplingInfoOnDb";

    public static final String METHOD_NAME_GET_PROINFO_LIST = "getMonitoringProjectInfoListByOrgCode";

    public static final String METHOD_NAME_GET_TASKINFO = "getMonitoringTaskInfoByTaskCode";
    
    public static final String METHOD_NAME_GET_All_MONITOR_SITE = "getAllMonitoringSite";

    public static final String METHOD_NAME_GET_BREED = "getMonitoringBreedForTask";

    public static final String METHOD_NAME_FORWARD_TASKINFO = "forwardMonitoringTaskInfo";

    public static final String METHOD_NAME_LOGIN = "checkLogInInfo";

    public static final String METHOD_NAME_GET_AREA = "getMonitoringAreaForTask";

    public static final String METHOD_NAME_GET_LINK = "getMonitoringLinkForTask";

    public static final String METHOD_NAME_GET_PAD_LIST = "getOtherPadInfo";

    public static final String METHOD_NAME_CHANGE_PWD = "modifyUserInfo";

    public static final String METHOD_NAME_GET_FINISHI_SAMPLES = "getFinishSamplingInfo";

    public static final String METHOD_NAME_RECEIVER_MQ = "receiverMQ";

    public static final String METHOD_NAME_GET_SYS_AREA = "getSysMonitoringArea";
    //获取抽样环节
    public static final String METHOD_NAME_GET_LINK_INFO = "getSampleMonitoringLink";
    /** 取得数据库文件 */
    public static final String METHOD_NAME_GET_DATABASE = "downLoadInitData";
    /** 保存样品图片 */
    public static final String METHOD_NAME_SAVE_IMAGE_FOR_SAMPLE = "saveImageForSample";
    /** 取得WebContext路径 */
    public static final String METHOD_NAME_GET_WEBCONTEXT_PATH = "getWebContextPath";

    public final static String IMAGE_URI = "iamge_uri";

    public final static String CROP_IMAGE_URI = "crop_image_uri";
    
    public final static String CROP_IMAGE_PATH = "CROP_IMAGE_PATH";

    public final static String CROP_REMARK_NAME_KEY = "CROP_REMARK_NAME_KEY";

    public final static String CROP_BITMAP_KEY = "CROP_BITMAP_KEY";

    public final static String CROP_SAVE_KEY = "CROP_SAVE_KEY";

    public final static String BARCODE_KEY = "BARCODE_KEY";

    public final static String BARCODE_BITMAP_KEY = "BARCODE_BITMAP_KEY";

    public final static String MONITOR_TASK_ENTITY_KEY = "MONITOR_TASK_ENTITY_KEY";
    
    public final static String FOR_SAMPLE_NUM_KEY = "FOR_SAMPLE_NUM_KEY" ;

    public final static String MONITOR_PROJECT_ENTITY_KEY = "MONITOR_PROJECT_ENTITY_KEY";

    public final static String SAMPLE_AREA_KEY = "SAMPLE_AREA_KEY";

    public final static String SAMPLE_LINK_KEY = "SAMPLE_LINK_KEY";

    public final static String SAMPLE_BREAD_KEY = "SAMPLE_BREAD_KEY";

    public final static String SAMPLE_BREAD_AGR_CODE_KEY = "SAMPLE_BREAD_AGR_CODE_KEY";
    
    public final static String SAMPLE_NAME_KEY = "SAMPLE_NAME_KEY";
    
    public final static String SAMPLE_NAME_AGR_CODE_KEY = "SAMPLE_NAME_AGR_CODE_KEY";
    
    public final static int NOTIFACTION_KEY_ID = 100001;

    public final static String ACTION_RECEIVE_FOR_SAMPLE = "ACTION_RECEIVE_FOR_SAMPLE";
    
    public final static String SAMPLE_CITY_KEY = "SAMPLE_CITY_KEY" ;
    
    public final static String SAMPLE_COUNTY_KEY = "SAMPLE_COUNTY_KEY"; 
    
    public final static String SAMPLE_TYPE = "SAMPLE_TYPE";
    
    public final static String CHECK_DCODE = "checkDcode";
    
    // 系统文件存放目录
    public final static String SD_CARD_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/Produce_QS";
    
    // 图片存放目录
    public final static String PATH_IMAGES = SD_CARD_DIRECTORY + "/images";
    
    /**
     * 抽样单类型:例行监测抽样单
     */
    public final static String TEMPLETE_ROUTINE_MONITORING = "1";
    /**
     * 抽样单类型:监督抽查抽样单
     */
    public final static String TEMPLETE_SUPERVISE_CHECK = "2";
    /**
     * 抽样单类型:普查(风险)抽样
     */
    public final static String TEMPLETE_GENERAL_CHECK = "3";
    /**
     * 抽样单类型:生鲜乳抽样单
     */
    public final static String TEMPLETE_FRESH_MILK = "4";
    /**
     * 抽样单类型:畜禽抽样单
     */
    public final static String TEMPLETE_LIVESTOCK = "5";
    
    /** 路径:服务器发布路径 */
    public final static String PATH_WEB_CONTEXT_ROOT = "0";
    /** 路径:服务器图片路径 */
    public final static String PATH_WEB_CONTEXT_IMAGE = "1";

}
