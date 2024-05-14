package hd.source.task;

import hd.produce.security.cn.HdCnApp;
import hd.produce.security.cn.data.FreshMilkEntity;
import hd.produce.security.cn.data.GeneralcheckEntity;
import hd.produce.security.cn.data.LinkInfo;
import hd.produce.security.cn.data.LivestockEntity;
import hd.produce.security.cn.data.MonitoringBreedEntity;
import hd.produce.security.cn.data.MonitoringPadEntity;
import hd.produce.security.cn.data.MonitoringProjectEntity;
import hd.produce.security.cn.data.MonitoringSiteEntity;
import hd.produce.security.cn.data.MonitoringTaskDetailsEntity;
import hd.produce.security.cn.data.MonitoringTaskEntity;
import hd.produce.security.cn.data.RoutinemonitoringEntity;
import hd.produce.security.cn.data.SampleInfoEntity;
import hd.produce.security.cn.data.SuperviseCheckEntity;
import hd.produce.security.cn.data.SysAreaCodeEntity;
import hd.produce.security.cn.db.DatabaseManager;
import hd.produce.security.cn.entity.TbMonitoringArea;
import hd.produce.security.cn.entity.TbMonitoringLinkInfo;
import hd.produce.security.cn.entity.TbMonitoringProject;
import hd.utils.cn.Constants;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WebServiceUtil {

    public static final String TAG = "WebServiceUtil";

    // 定义webservice的命名空间
    // http://pad.webservice.service.nky.hippo.com/
    public static final String SERVICE_NAMESPACE = "http://webservice.hippo.nky.com/";

    // 定义webservice提供服务的url
    // http://117.74.74.38/jiance/webservice/PadWebService?wsdl

    // 测试
//     public static final String SERVICE_URL = "http://192.168.20.246:8090/jiance/webservice/PadWebService";
//    public static final String SERVICE_URL = "http://192.168.1.103:8080/framework3.3.2/webservice/PadWebService";
    
	public static final String HOST_URL = "https://jc.jsncpaq.com";
//    public static final String HOST_URL = "http://124.70.55.183:8090/jiance";
	// 正式
	public static final String SERVICE_URL = HOST_URL + "/webservice/PadWebService";
	public static final String UPLOAD_PATH = "/upload";
     
    

    // public static final String SERVICE_URL = "http://218.94.151.152:8080/jiance/webservice/PadWebService";

    // public static final String SERVICE_URL =
    // "http://192.168.20.240:8080/jiance/webservice/PadWebService?wsdl";

    // public static final String SERVICE_URL =
    // "http://192.168.100.129:8080/webservice/PadWebService?wsdl";

    /**
     * 根据监测类型取得监测类型名称
     * 
     * @param so
     * @return String
     */
    public static String getNameForMonitorType(SoapObject so) {

        String result = Constants.STR_EMPTY;
        if (so != null) {
            result = so.getProperty("return").toString();
        }
        return result;
    }

    /**
     * 根据监测类型取得监测类型名称
     * 
     * @param monitorType
     * @return SoapObject
     */
    public static SoapObject getNameForMonitorTypeRequest(String monitorType) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_MONITOR_NAME;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("monitorType", monitorType);

        return request;
    }

    /**
     * 获取抽样所有地区信息
     * 
     * @param so
     * @return String
     */
    public static void getSysMonitoringArea(SoapObject so) {
        try {
            String jsonStr = Constants.STR_EMPTY;
            if (so != null) {
                jsonStr = so.getProperty("return").toString();
                if (!TextUtils.isEmpty(jsonStr)) {
                    Utils.savePreference(HdCnApp.getApplication(), Constants.SP_KEY_SYS_AREA, jsonStr);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<LinkedList<SysAreaCodeEntity>>() {
                    }.getType();
                    List<SysAreaCodeEntity> list = gson.fromJson(jsonStr, listType);
                    if (list != null && list.size() > 0) {
                        // 因为只有江苏所以只取一个(江苏省)
                        SysAreaCodeEntity province = list.get(0);
                        List<TbMonitoringArea> cityList = new ArrayList<TbMonitoringArea>();
                        List<TbMonitoringArea> countyList = new ArrayList<TbMonitoringArea>();
                        // TbMonitoringArea newProvince = new TbMonitoringArea();
                        // newProvince.setId(province.getCode());
                        // 100000是全国的行政区划代码
                        // newProvince.setPid("100000");
                        // newProvince.setName(province.getAreaname());
                        // countyList.add(newProvince);
                        // 取得江苏下所有行政区划
                        List<SysAreaCodeEntity> jiansuArea = province.getJsonCity();
                        // 循环地市
                        for (SysAreaCodeEntity city : jiansuArea) {
                            TbMonitoringArea newCity = new TbMonitoringArea();
                            newCity.setId(city.getCode());
                            newCity.setPid(province.getCode());
                            newCity.setName(city.getAreaname());
                            cityList.add(newCity);
                            // 循环区县
                            for (SysAreaCodeEntity county : city.getJsonCounty()) {
                                TbMonitoringArea newCounty = new TbMonitoringArea();
                                newCounty.setId(county.getCode());
                                newCounty.setPid(newCity.getId());
                                newCounty.setName(county.getAreaname());
                                countyList.add(newCounty);
                            }
                        }
                        DatabaseManager.getInstance().truncate(DatabaseManager.TB_MONITORING_AREA_CITY);
                        DatabaseManager.getInstance().truncate(DatabaseManager.TB_MONITORING_AREA_COUNTRY);
                        DatabaseManager.getInstance().insertBatch(DatabaseManager.TB_MONITORING_AREA_CITY, cityList);
                        DatabaseManager.getInstance().insertBatch(DatabaseManager.TB_MONITORING_AREA_COUNTRY, countyList);
                        // DataManager.getInstance().setSysAreaCodeEntity(list.get(0));
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Error in getSysMonitoringArea", e);
        }
    }

    /**
     * 获取抽样所有地区信息
     * 
     * @return SoapObject
     */
    public static SoapObject getSysMonitoringAreaRequest() {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_SYS_AREA;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        return request;
    }

    /**
     * 获取抽样所有地区信息
     * 
     * @return SoapObject
     */
    public static SoapObject getDatabase() {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_DATABASE;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("orgCode", DataManager.getInstance().getOrgCode());
        request.addProperty("padId", DataManager.getInstance().getPadId());
        return request;
    }

    /**
     * 取抽样品种
     * 
     * @param so
     * @return List<MonitoringBreedEntity>
     */
    public static List<MonitoringBreedEntity> getMonitoringBreedForTask(SoapObject so, String projectCode) {

        List<MonitoringBreedEntity> resultList = new ArrayList<MonitoringBreedEntity>();
        if (so != null) {
            MonitoringBreedEntity monitoringBreedEntity;
            int size = so.getPropertyCount();
            for (int i = 0; i < size; i++) {
                try {
                    SoapObject child = (SoapObject) so.getProperty(i);
                    monitoringBreedEntity = new MonitoringBreedEntity();
                    monitoringBreedEntity.parseBySoapObject(child);
                    monitoringBreedEntity.setProjectCode(projectCode);
                    resultList.add(monitoringBreedEntity);
                } catch (Exception e) {
                    LogUtil.e(TAG, "Error in getMonitoringBreedForTask", e);
                }
            }
            if (!resultList.isEmpty()) {
                DatabaseManager.getInstance().insertOrUpdateBatch(DatabaseManager.TB_MONITORING_BREED, resultList);
            }
        }
        return resultList;
    }

    public static ArrayList<String> getMonitoringBreadStr(List<MonitoringBreedEntity> list) {
        ArrayList<String> lists = new ArrayList<String>();
        for (MonitoringBreedEntity entity : list) {
            lists.add(entity.getAgrName());
        }
        return lists;
    }

    public static ArrayList<String> getMonitoringBreadAgrCode(List<MonitoringBreedEntity> list) {
        ArrayList<String> lists = new ArrayList<String>();
        for (MonitoringBreedEntity entity : list) {
            lists.add(entity.getAgrCode());
        }
        return lists;
    }

    public static String getMonitoringBreed(List<MonitoringBreedEntity> list) {

        StringBuilder result = new StringBuilder(Constants.STR_EMPTY);
        if (list != null) {
            for (int i = 0; i < list.size() - 1; i++) {
                result.append(list.get(i).getAgrName()).append(",");
            }
            result.append(list.get(list.size() - 1).getAgrName());
            // for (MonitoringBreedEntity entity : list) {
            //
            // }
        }
        return result.toString();
    }

    /**
     * 根据任务code取得抽样品种
     * 
     * @param taskCode
     * @return SoapObject
     */
    public static SoapObject getMonitoringBreedForTaskRequest(String taskCode, String projectCode) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_BREED;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        if (!TextUtils.isEmpty(taskCode)) {
            request.addProperty("taskCode", taskCode);
        }
        request.addProperty("projectCode", projectCode);

        return request;
    }

    /**
     * 根据项目code取得样品名称
     * 
     * @return SoapObject
     */
    public static SoapObject getSampleNameForTaskRequest(String projectCode) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_BREED;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("projectCode", projectCode);

        return request;
    }

    /**
     * 根据项目ID查询任务列表
     * 
     * @param so
     * @return List<MonitoringTaskEntity>
     */
    public static List<MonitoringTaskEntity> getTaskInfoList(SoapObject so) {

        List<MonitoringTaskEntity> resultList = new ArrayList<MonitoringTaskEntity>();
        // 抽样单的抽样品种(list)
        List<MonitoringBreedEntity> breedList = new ArrayList<MonitoringBreedEntity>();
        if (so != null) {
            MonitoringTaskEntity monitoringTaskEntity;
            MonitoringTaskDetailsEntity taskDetail = null;
            int size = so.getPropertyCount();
            for (int i = 0; i < size; i++) {
                try {
                    SoapObject child = (SoapObject) so.getProperty(i);
                    monitoringTaskEntity = new MonitoringTaskEntity();
                    monitoringTaskEntity.parseBySoapObject(child);
                    if (ConverterUtil.isNotEmpty(monitoringTaskEntity.getProjectBreedList())) {
                        breedList.addAll(monitoringTaskEntity.getProjectBreedList());
                    }
                    if (ConverterUtil.isNotEmpty(monitoringTaskEntity.getTaskDetail())) {
                        taskDetail = monitoringTaskEntity.getTaskDetail();
                    }
                    resultList.add(monitoringTaskEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!resultList.isEmpty()) {
                DatabaseManager.getInstance().insertOrUpdateBatch(DatabaseManager.TB_MONITORING_TASK, resultList);
            }
            // 如果有子表信息存储
            if (ConverterUtil.isNotEmpty(taskDetail)) {
                DatabaseManager.getInstance().insert(DatabaseManager.TB_MONITORING_TASK_DETAILS, taskDetail);
            }
            if (!breedList.isEmpty()) {
                DatabaseManager.getInstance().insertOrUpdateBatch(DatabaseManager.TB_MONITORING_BREED, breedList);
            }
        }
        return resultList;
    }

    /**
     * 根据项目ID查询任务列表
     * 
     * @param paramEntity
     * @return SoapObject
     */
    public static SoapObject getTaskInfoListRequest(MonitoringProjectEntity paramEntity) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_TASKINFO_LIST;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(paramEntity, MonitoringProjectEntity.class);
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setName("monitoringProjectEntity");
        propertyInfo.setValue(jsonStr);
        propertyInfo.setType(String.class);
        request.addProperty(propertyInfo);
        // request.addProperty("monitoringProjectEntity", paramEntity);
        return request;
    }

    /**
     * 提交样本检测信息
     * 
     * @param so
     * @return String
     */
    public static int saveSampleInfo(SoapObject so) {
        String result = "0";
        if (so != null) {
            try {
                result = so.getPropertyAsString("return");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Integer.valueOf(result);
    }

    /**
     * 提交样本检测信息
     * 
     * @param sampleInfoEntity
     * @return SoapObject
     */
    public static SoapObject saveSampleInfoRequest(SampleInfoEntity sampleInfoEntity, String sampleMonadType) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_SAVE_SAMPLEINFO;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(sampleInfoEntity, SampleInfoEntity.class);
        // LogUtil.i("WebServiceTestTask", jsonStr);
        // Log.d("lzc", "test json===========>" + jsonStr);
        // System.out.println(jsonStr);
        request.addProperty("samplingInfo", jsonStr);
        request.addProperty("sampleMonadType", sampleMonadType);

        return request;
    }

    /**
     * 提交样本图片
     * 
     * @param imgContentMap
     * @return SoapObject
     */
    public static SoapObject saveSampleImageRequest(Map<String, String> imgContentMap) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_SAVE_IMAGE_FOR_SAMPLE;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(imgContentMap);
        request.addProperty("imgContentMap", jsonStr);
        return request;
    }

    /**
     * 根据质检机构查询项目列表
     * 
     * @param so
     * @return List<MonitoringProjectEntity>
     */
    public static List<TbMonitoringProject> getProjectInfo(SoapObject so) {
        LogUtil.i("test", so.toString());
        List<TbMonitoringProject> resultList = new ArrayList<TbMonitoringProject>();
        // 抽样单的抽样环节 (list)
        List<TbMonitoringLinkInfo> linkList = new ArrayList<TbMonitoringLinkInfo>();
        if (so != null) {
            // MonitoringProjectEntity monitoringProjectEntity;
            // TbMonitoringProject monitoringProjectEntity;
            int size = so.getPropertyCount();
            for (int i = 0; i < size; i++) {
                try {
                    SoapObject child = (SoapObject) so.getProperty(i);
                    TbMonitoringProject monitoringProjectEntity = new TbMonitoringProject();
                    monitoringProjectEntity.parseBySoapObject(child);
                    if (ConverterUtil.isNotEmpty(monitoringProjectEntity.getLinkInfoList())) {
                        linkList.addAll(monitoringProjectEntity.getLinkInfoList());
                    }
                    resultList.add(monitoringProjectEntity);
                } catch (Exception e) {
                    LogUtil.e(TAG, "Error in getProjectInfo", e);
                }
            }
            DatabaseManager.getInstance().truncate(DatabaseManager.TB_MONITORING_PROJECT);
            DatabaseManager.getInstance().truncate(DatabaseManager.TB_MONITORING_LINK_INFO);
            DatabaseManager.getInstance().insertBatch(DatabaseManager.TB_MONITORING_PROJECT, resultList);
            // 抽样单的抽样环节 (list)
            DatabaseManager.getInstance().insertBatch(DatabaseManager.TB_MONITORING_LINK_INFO, linkList);
        }

        return resultList;
    }

    /**
     * 根据质检机构查询项目列表
     * 
     * @param orgCode
     * @return SoapObject
     */
    public static SoapObject getProjectInfoRequest(String orgCode, String padId, String projectName) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_PROINFO_LIST;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("orgCode", orgCode);
        request.addProperty("padId", padId);
        if (!TextUtils.isEmpty(projectName)) {
            request.addProperty("projectName", projectName);
        }

        return request;
    }

    /**
     * 获取所有受检单位信息
     * 
     * @param orgCode
     * @return SoapObject
     */
    public static SoapObject getAllMonitoringSiteRequest() {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_All_MONITOR_SITE;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);

        return request;
    }

    /**
     * 取得样品图片上传结果
     * 
     * @param so
     * @return
     */
    public static Map<String, String> getSampleImageMap(SoapObject so) {
        Map<String, String> map = new HashMap<String, String>();
        if (so != null) {
            int size = so.getPropertyCount();
            if (size > 0) {
                String json = so.getPropertyAsString(0);
                Gson gson = new Gson();
                map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
                }.getType());
            }
        }
        return map;
    }

    /**
     * 获取所有受检单位信息
     * 
     * @param orgCode
     * @return SoapObject
     */
    public static List<MonitoringSiteEntity> getAllMonitoringSite(SoapObject so) {
        List<MonitoringSiteEntity> resultList = new ArrayList<MonitoringSiteEntity>();
        if (so != null) {
            MonitoringSiteEntity monitoringSiteEntity;
            int size = so.getPropertyCount();
            String json = so.getPropertyAsString(0);
            Gson gson = new Gson();
            long startToJson = System.currentTimeMillis();
            resultList = gson.fromJson(json, new TypeToken<List<MonitoringSiteEntity>>() {
            }.getType());
            long endToJson = System.currentTimeMillis();
            LogUtil.e("转JSON耗时:" + (endToJson - startToJson) / 1000f + "秒");
            // for (int i = 0; i < size; i++) {
            // try {
            // SoapObject child = (SoapObject) so.getProperty(i);
            // monitoringSiteEntity = new MonitoringSiteEntity();
            // monitoringSiteEntity.parseBySoapObject(child);
            // resultList.add(monitoringSiteEntity);
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            //
            // }
            if (resultList != null && !resultList.isEmpty()) {
                long startDel = System.currentTimeMillis();
                // 清空受检单位
                DatabaseManager.getInstance().truncate(DatabaseManager.TB_MONITORING_SITE);
                long endDel = System.currentTimeMillis();
                LogUtil.e("本地化删除耗时:" + (endDel - startDel) / 1000f + "秒");
                long startLoc = System.currentTimeMillis();
                // 存储受检单位
                DatabaseManager.getInstance().insertBatch(DatabaseManager.TB_MONITORING_SITE, resultList);
                long endLoc = System.currentTimeMillis();
                LogUtil.e("本地化插入耗时:" + (endLoc - startLoc) / 1000f + "秒");
                // DataManager.getInstance().setAllMonintringSiteList(resultList);
            }
        }
        return resultList;
    }

    /**
     * 根据任务code取得任务详情
     * 
     * @param so
     * @return MonitoringTaskDetailsEntity
     */
    public static MonitoringTaskDetailsEntity getTaskInfo(SoapObject so) {
        if (so != null) {
            SoapObject detail = (SoapObject) so.getProperty("return");
            MonitoringTaskDetailsEntity monitoringTaskDetailsEntity = new MonitoringTaskDetailsEntity();
            monitoringTaskDetailsEntity.parseBySoapObject(detail);
            DatabaseManager dba = DatabaseManager.getInstance();
            dba.truncate(DatabaseManager.TB_MONITORING_TASK_DETAILS);
            dba.insert(DatabaseManager.TB_MONITORING_TASK_DETAILS, monitoringTaskDetailsEntity);
            return monitoringTaskDetailsEntity;
        }
        return null;
    }

    /**
     * 根据任务code取得任务详情
     * 
     * @param taskCode
     * @return SoapObject
     */
    public static SoapObject getTaskInfoRequest(String taskCode, String padId) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_TASKINFO;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("taskCode", taskCode);
        request.addProperty("padId", padId);

        return request;
    }

    /**
     * 根据任务code取得抽样地区
     * 
     * @param so
     * @return List<String>
     */
    public static List<String> getMonitoringAreaForTask(SoapObject so) {
        List<String> resultList = new ArrayList<String>();
        if (so != null) {
            String result;
            int size = so.getPropertyCount();
            for (int i = 0; i < size; i++) {
                try {
                    result = so.getPropertyAsString(i);
                    resultList.add(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return resultList;
    }

    /**
     * 根据任务code取得抽样地区
     * 
     * @param taskCode
     * @return SoapObject
     */
    public static SoapObject getMonitoringAreaRequest(String taskCode) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_AREA;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("taskCode", taskCode);

        return request;
    }

    /**
     * 根据任务code取得抽样环节
     * 
     * @param so
     * @return List<String>
     */
    public static List<String> getMonitoringLinkForTask(SoapObject so) {
        List<String> resultList = new ArrayList<String>();
        if (so != null) {
            String result;
            int size = so.getPropertyCount();
            for (int i = 0; i < size; i++) {
                try {
                    result = so.getPropertyAsString(i);
                    resultList.add(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return resultList;
    }

    /**
     * 根据任务code取得抽样环节
     * 
     * @param taskCode
     * @return SoapObject
     */
    public static SoapObject getMonitoringLinkRequest(String taskCode) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_LINK;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("taskCode", taskCode);

        return request;
    }

    /**
     * PAD轮询取数据接口
     * 
     * @param so
     * @return String
     */
    public static String receiverMQ(SoapObject so) {

        String result = Constants.STR_EMPTY;
        if (so != null) {
            result = so.getPropertyAsString("return");
        }
        return result;
    }

    /**
     * PAD轮询取数据接口
     * 
     * @param padId
     * @return SoapObject
     */
    public static SoapObject receiverMQRequest(String padId) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_RECEIVER_MQ;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("padId", padId);

        return request;
    }

    /**
     * 任务转发接口
     * 
     * @param so
     * @return
     */
    public static boolean forwardTask(SoapObject so) {
        Boolean result = false;
        if (so != null) {
            String str = so.getPropertyAsString("return");
            if ("true".equalsIgnoreCase(str)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * 任务转发接口
     * 
     * @param paramMonitoringTaskDetailsEntity
     * @param padId
     * @param total
     * @return SoapObject
     */
    public static SoapObject forwardTaskRequest(MonitoringTaskDetailsEntity paramMonitoringTaskDetailsEntity, String padId, int total, String flagStr) {
        String methodName = Constants.METHOD_NAME_FORWARD_TASKINFO;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(paramMonitoringTaskDetailsEntity, MonitoringTaskDetailsEntity.class);
        Log.d("lzc", "jsonStr==============>" + jsonStr);
        Log.d("lzc", "padId==============>" + padId);
        Log.d("lzc", "total==============>" + total);
        Log.d("lzc", "flagStr==============>" + flagStr);
        request.addProperty("paramMonitoringTaskDetailsEntity", jsonStr);
        request.addProperty("padId", padId);
        request.addProperty("total", total);
        request.addProperty("flagStr", flagStr);

        return request;
    }

    /**
     * 验证是否登陆成功
     * 
     * @param so
     * @return MonitoringPadEntity
     */
    public static MonitoringPadEntity checkLogInInfo(SoapObject so) {

        MonitoringPadEntity monitoringPadEntity = new MonitoringPadEntity();
        if (so != null) {
            SoapObject detail = (SoapObject) so.getProperty("return");
            monitoringPadEntity.parseBySoapObject(detail);
        }
        return monitoringPadEntity;
    }

    /**
     * 验证是否登陆成功
     * 
     * @param user
     * @param pwd
     * @return SoapObject
     */
    public static SoapObject checkLogInInfoRequest(String user, String pwd) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_LOGIN;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("userName", user);
        request.addProperty("passWord", pwd);

        return request;
    }

    /**
     * 修改密码
     * 
     * @param so
     * @return Boolean
     */
    public static Boolean modifyUserInfo(SoapObject so) {
        Boolean result = false;
        if (so != null) {
            String str = so.getPropertyAsString("return");
            if ("1".equals(str)) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 修改密码
     * 
     * @param userName
     * @param oldPassWord
     * @param newPassWord
     * @return SoapObject
     */
    public static SoapObject modifyUserInfoRequest(String userName, String oldPassWord, String newPassWord) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_CHANGE_PWD;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("userName", userName);
        request.addProperty("oldPassWord", oldPassWord);
        request.addProperty("newPassWord", newPassWord);

        return request;
    }

    /**
     * 根据padId和任务code获取其他抽样员信息
     * 
     * @param so
     * @return List<SampleInfoEntity>
     */
    public static List<SampleInfoEntity> getFinishSamplingInfo(SoapObject so) {
        // 抽样单主表
        List<SampleInfoEntity> resultList = new ArrayList<SampleInfoEntity>();
        // 例行监测信息表
        List<RoutinemonitoringEntity> rmList = new ArrayList<RoutinemonitoringEntity>();
        // 普查(风险)子表
        List<GeneralcheckEntity> gcList = new ArrayList<GeneralcheckEntity>();
        // 监督抽查子表
        List<SuperviseCheckEntity> scList = new ArrayList<SuperviseCheckEntity>();
        // 生鲜乳子表
        List<FreshMilkEntity> fmList = new ArrayList<FreshMilkEntity>();
        // 畜禽子表
        List<LivestockEntity> leList = new ArrayList<LivestockEntity>();
        if (so != null) {
            DatabaseManager dba = DatabaseManager.getInstance();
            SampleInfoEntity sampleInfoEntity;
            int size = so.getPropertyCount();
            for (int i = 0; i < size; i++) {
                try {
                    SoapObject child = (SoapObject) so.getProperty(i);
                    sampleInfoEntity = new SampleInfoEntity();
                    sampleInfoEntity.parseBySoapObject(child);
                    resultList.add(sampleInfoEntity);
                    // 例行监测子表
                    if (ConverterUtil.isNotEmpty(sampleInfoEntity.getRoutinemonitoringList())) {
                        rmList.addAll(sampleInfoEntity.getRoutinemonitoringList());
                        continue;
                    }
                    // 普查(风险)子表
                    if (ConverterUtil.isNotEmpty(sampleInfoEntity.getGeneralcheckEntity())) {
                        gcList.add(sampleInfoEntity.getGeneralcheckEntity());
                        continue;
                    }
                    // 监督抽查子表
                    if (ConverterUtil.isNotEmpty(sampleInfoEntity.getSuperviseCheckEntity())) {
                        scList.add(sampleInfoEntity.getSuperviseCheckEntity());
                        continue;
                    }
                    // 生鲜乳子表
                    if (ConverterUtil.isNotEmpty(sampleInfoEntity.getFreshMilkEntity())) {
                        fmList.add(sampleInfoEntity.getFreshMilkEntity());
                        continue;
                    }
                    // 畜禽子表
                    if (ConverterUtil.isNotEmpty(sampleInfoEntity.getLivestockEntityList())) {
                        leList.addAll(sampleInfoEntity.getLivestockEntityList());
                        continue;
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, "Error in getFinishSamplingInfo", e);
                }
            }
            dba.truncate(DatabaseManager.TB_SAMPLING_INFO);
            dba.truncate(DatabaseManager.TB_ROUTINE_MONITORING);
            dba.truncate(DatabaseManager.TB_GENERAL_CHECK);
            dba.truncate(DatabaseManager.TB_SUPERVISE_CHECK);
            dba.truncate(DatabaseManager.TB_FRESH_MILK);
            dba.truncate(DatabaseManager.TB_LIVESTOCK);
            if (!resultList.isEmpty()) {
                dba.insertOrUpdateBatch(DatabaseManager.TB_SAMPLING_INFO, resultList);
            }
            if (!rmList.isEmpty()) {
                dba.insertOrUpdateBatch(DatabaseManager.TB_ROUTINE_MONITORING, rmList);
            }
            if (!gcList.isEmpty()) {
                dba.insertOrUpdateBatch(DatabaseManager.TB_GENERAL_CHECK, gcList);
            }
            if (!scList.isEmpty()) {
                dba.insertOrUpdateBatch(DatabaseManager.TB_SUPERVISE_CHECK, scList);
            }
            if (!fmList.isEmpty()) {
                dba.insertOrUpdateBatch(DatabaseManager.TB_FRESH_MILK, fmList);
            }
            if (!leList.isEmpty()) {
                dba.insertOrUpdateBatch(DatabaseManager.TB_LIVESTOCK, leList);
            }
        }
        return resultList;
    }

    /**
     * 根据padId和任务code获取其他抽样员信息
     * 
     * @param padId
     * @param taskCode
     * @param sampleMonadType
     * @return SoapObject
     */
    public static SoapObject getFinishSamplingInfoRequest(String padId, String taskCode, String sampleMonadType, String projectCode) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_FINISHI_SAMPLES;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("padId", padId);
        request.addProperty("taskCode", taskCode);
        request.addProperty("sampleMonadType", sampleMonadType);
        request.addProperty("projectCode", projectCode);

        return request;
    }

    /**
     * 根据padId和任务code获取其他抽样员信息
     * 
     * @param so
     * @return List<MonitoringPadEntity>
     */
    public static List<MonitoringPadEntity> getOtherPadInfo(SoapObject so) {
        List<MonitoringPadEntity> resultList = new ArrayList<MonitoringPadEntity>();
        if (so != null) {
            MonitoringPadEntity monitoringPadEntity;
            int size = so.getPropertyCount();
            for (int i = 0; i < size; i++) {
                try {
                    SoapObject child = (SoapObject) so.getProperty(i);
                    monitoringPadEntity = new MonitoringPadEntity();
                    monitoringPadEntity.parseBySoapObject(child);
                    resultList.add(monitoringPadEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return resultList;
    }

    /**
     * 根据padId和任务code获取其他抽样员信息
     * 
     * @param padId
     * @param taskCode
     * @return SoapObject
     */
    public static SoapObject getOtherPadInfoRequest(String padId, String taskCode) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_PAD_LIST;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("padId", padId);
        if (taskCode != null) {
            request.addProperty("taskCode", taskCode);
        }

        return request;
    }

    /**
     * 调用WebService
     * 
     * @param soapObject
     * @return SoapObject
     */
    public static SoapObject callWebService(SoapObject soapObject) throws EOFException {
        return callWebService(soapObject, Constants.HTTP_SO_TIMEOUT);
    }

    /**
     * 调用WebService
     * 
     * @param soapObject
     * @return SoapObject
     */
    public static SoapObject callWebService(SoapObject soapObject, int timeOut) throws EOFException {
        HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL, timeOut);
        // httpTransportSE.setXmlVersionTag(Constants.XML_ENC);
        LogUtil.i(soapObject.toString());
        try {
            httpTransportSE.debug = true;
            // 使用SOAP1.1协议创建Envelop对象
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            // 实例化SoapObject对象
            // envelope.bodyOut = soapObject;
            envelope.setOutputSoapObject(soapObject);
            // 设置与.NET提供的webservice保持较好的兼容性
            envelope.dotNet = true;

            // 调用webservice
            httpTransportSE.call(null, envelope);
            if (envelope.getResponse() != null) {
                // 获取服务器响应返回的SOAP消息
                // TODO:
                SoapObject result = (SoapObject) envelope.bodyIn;
                LogUtil.w(result.toString());
                // 解析服务器响应的SOAP消息
                return result;
            }
        } catch (SoapFault e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用WebService
     * 
     * @param soapObject
     * @param methodName
     * @return SoapObject
     */
    public static SoapObject callWebService(SoapObject soapObject, String methodName) {
        HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL, Constants.HTTP_SO_TIMEOUT);
        // httpTransportSE.setXmlVersionTag(Constants.XML_ENC);
        try {
            httpTransportSE.debug = true;
            // 使用SOAP1.1协议创建Envelop对象
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            // 实例化SoapObject对象
            // envelope.bodyOut = soapObject;
            envelope.setOutputSoapObject(soapObject);
            // 设置与.NET提供的webservice保持较好的兼容性
            envelope.dotNet = true;

            // 调用webservice
            // httpTransportSE.call(null, envelope);
            httpTransportSE.call(SERVICE_NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                // 获取服务器响应返回的SOAP消息
                // TODO:
                SoapObject result = (SoapObject) envelope.bodyIn;
                // 解析服务器响应的SOAP消息
                return result;
            }
        } catch (SoapFault e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用WebService
     * 
     * @param envelope
     * @return SoapObject
     */
    public static SoapObject callWebService(SoapSerializationEnvelope envelope) {
        HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL, Constants.HTTP_SO_TIMEOUT);
        // httpTransportSE.setXmlVersionTag(Constants.XML_ENC);
        try {
            httpTransportSE.debug = true;
            // 调用webservice
            httpTransportSE.call(null, envelope);
            if (envelope.getResponse() != null) {
                // 获取服务器响应返回的SOAP消息
                // TODO:
                SoapObject result = (SoapObject) envelope.bodyIn;
                // 解析服务器响应的SOAP消息
                LogUtil.i(TAG, result.toString());
                return result;
            }
        } catch (SoapFault e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SoapObject getLinkInfoRequest(String industryCode) {
        // 调用 的方法
        String methodName = Constants.METHOD_NAME_GET_LINK_INFO;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        if (industryCode != null) {
            request.addProperty("industry", industryCode);
        }

        return request;
    }

    public static List<LinkInfo> getLinkInfo(SoapObject so) {
        List<LinkInfo> resultList = new ArrayList<LinkInfo>();
        if (so != null) {

            int size = so.getPropertyCount();
            for (int i = 0; i < size; i++) {
                try {
                    /**
                     * LinkInfo result = new LinkInfo(); SoapObject detail = (SoapObject) so.getProperty("return"); result.parseBySoapObject(detail); resultList.add(result);
                     */

                    LinkInfo result = new LinkInfo();
                    SoapObject child = (SoapObject) so.getProperty(i);
                    result.parseBySoapObject(child);
                    resultList.add(result);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return resultList;
    }

    /**
     * 验证二维码
     * 
     * @param dCode
     * @param projectCode
     * @return SoapObject
     */
    public static SoapObject checkDCode(String dCode, String projectCode) {
        // 调用 的方法
        String methodName = Constants.CHECK_DCODE;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        request.addProperty("dCode", dCode);
        request.addProperty("projectCode", projectCode);

        return request;
    }
    
    /**
     * 取得WebContextPath
     * 
     * @return
     */
    public static SoapObject getWebContextPath() {
        // 调用的方法
        String methodName = Constants.METHOD_NAME_GET_WEBCONTEXT_PATH;
        SoapObject request = new SoapObject(SERVICE_NAMESPACE, methodName);
        // 0:服务器发布路径 1:服务器图片存储路径
        request.addProperty("type", Constants.PATH_WEB_CONTEXT_IMAGE);
        return request;
    }

    /**
     * 解析WebContext路径
     * 
     * @param so
     * @return
     */
    public static String decodeWebContextPath(SoapObject so) {
        if (so != null) {
            int count = so.getPropertyCount();
            if (count > 0) {
            	String temp = so.getPropertyAsString(0);
            	return HOST_URL + temp.substring(temp.indexOf(UPLOAD_PATH));
            }
        }
        return null;
    }

    /**
     * 验证二维码
     * 
     * @param so
     * @return String
     */
    public static String checkDCodeResult(SoapObject so) {
        String result = null;
        if (so != null) {
            try {
                result = so.getPropertyAsString("return");
                if (result != null && result.equals("anyType{}")) {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
