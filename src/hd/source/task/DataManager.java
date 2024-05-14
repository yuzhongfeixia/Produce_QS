package hd.source.task;

import hd.produce.security.cn.HdCnApp;
import hd.produce.security.cn.dao.CommonDao;
import hd.produce.security.cn.data.CityInfo;
import hd.produce.security.cn.data.CountyInfo;
import hd.produce.security.cn.data.FreshMilkEntity;
import hd.produce.security.cn.data.GeneralcheckEntity;
import hd.produce.security.cn.data.LinkInfo;
import hd.produce.security.cn.data.LivestockEntity;
import hd.produce.security.cn.data.MonitoringBreedEntity;
import hd.produce.security.cn.data.MonitoringPadEntity;
import hd.produce.security.cn.data.MonitoringSiteEntity;
import hd.produce.security.cn.data.MonitoringTaskDetailsEntity;
import hd.produce.security.cn.data.MonitoringTaskEntity;
import hd.produce.security.cn.data.PadMQ;
import hd.produce.security.cn.data.RoutinemonitoringEntity;
import hd.produce.security.cn.data.SampleInfoEntity;
import hd.produce.security.cn.data.SuperviseCheckEntity;
import hd.produce.security.cn.data.SysAreaCodeEntity;
import hd.produce.security.cn.entity.Sprinner;
import hd.produce.security.cn.entity.TbFreshMilk;
import hd.produce.security.cn.entity.TbGeneralCheck;
import hd.produce.security.cn.entity.TbLivestock;
import hd.produce.security.cn.entity.TbMonitoringProject;
import hd.produce.security.cn.entity.TbRoutineMonitoring;
import hd.produce.security.cn.entity.TbSuperviseCheck;
import hd.source.cn.ImageDownload;
import hd.utils.cn.Constants;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.DownloadBroadcast;
import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DataManager {

    public static String ALL_BREAD = "all_bread";
    // 取得项目列表
    public static final int TASK_GET_PROJECT_LIST = 0;
    // 取得任务列表
    public static final int TASK_GET_TASK_LIST = TASK_GET_PROJECT_LIST + 1;
    // 取得任务对应未完成的抽样单
    public static final int TASK_GET_UNFINISHED_LIST_BY_TASK = TASK_GET_TASK_LIST + 1;
    // 取得任务对应已完成的抽样单
    public static final int TASK_GET_FINISHED_LIST_BY_TASK = TASK_GET_UNFINISHED_LIST_BY_TASK + 1;
    // 取得任务对应未完成的抽样单数量
    public static final int TASK_GET_UNFINISHED_COUNT_BY_TASK = TASK_GET_FINISHED_LIST_BY_TASK + 1;
    // 取得任务对应已完成的抽样单数量
    public static final int TASK_GET_FINISHED_COUNT_BY_TASK = TASK_GET_UNFINISHED_COUNT_BY_TASK + 1;
    // 取得任务详情
    public static final int TASK_GET_TASK_DETAIL_LIST = TASK_GET_FINISHED_COUNT_BY_TASK + 1;
    // 取得地市
    public static final int TASK_GET_CITY_LIST = TASK_GET_TASK_DETAIL_LIST + 1;
    // 取得区县
    public static final int TASK_GET_COUNTRY_LIST = TASK_GET_CITY_LIST + 1;
    // 取得监测环节
    public static final int TASK_GET_MONITOR_LINK_LIST = TASK_GET_COUNTRY_LIST + 1;
    // 取得抽样品种
    public static final int TASK_GET_BREED_LIST = TASK_GET_MONITOR_LINK_LIST + 1;
    // 保存抽样单
    public static final int TASK_SAVE_SAMPLE = TASK_GET_BREED_LIST + 1;
    // 删除抽样单
    public static final int TASK_DELETE_SAMPLE = TASK_SAVE_SAMPLE + 1;
    // 查询抽样单
    public static final int TASK_GET_SAMPLE = TASK_DELETE_SAMPLE + 1;
    // 取得受检单位
    public static final int TASK_GET_MONITORING_SITE = TASK_GET_SAMPLE + 1;
    // 取得受检单位页码
    public static final int TASK_GET_MONITORING_SITE_PAGE = TASK_GET_MONITORING_SITE + 1;
    // 用子表ID删除抽样单
    public static final int TASK_DELETE_SAMPLE_BY_SUBID = TASK_GET_MONITORING_SITE_PAGE + 1;
    // 更新抽样单状态
    public static final int TASK_UPDATE_SAMPLE_STATUS = TASK_DELETE_SAMPLE_BY_SUBID + 1;

    private static DataManager mInstance;
    private static CommonDao commDao;

    // private DatabaseManager mDatabaseManager;

    private MonitoringPadEntity mMonitoringPadEntity;

    private SysAreaCodeEntity mSysAreaCodeEntity;

    private List<TbMonitoringProject> mMonitoringProjectEntityList;

    private Map<String, List<MonitoringTaskEntity>> mTaskEntityListMap;

    private Map<String, MonitoringTaskDetailsEntity> mMonitoringTaskDetailsMap;

    private Map<String, Integer> mFinishedSampleCount;

    // private Map<String, List<String>> mLinkListMap;

    private Map<String, List<LinkInfo>> linkInfoMap;

    private Map<String, List<String>> mAreaListMap;

    private List<MonitoringSiteEntity> mAllMonintringSiteList;

    private Map<String, List<MonitoringBreedEntity>> mBreadListMap;

    private Map<String, List<MonitoringBreedEntity>> sampleNameListMap;

    private Map<String, List<String>> mBreadStrListMap;

    private Map<String, List<String>> sampleNameStrListMap;

    private Map<String, List<String>> mBreadAgrCodeListMap;

    private Map<String, List<String>> mSampleNameAgrCodeListMap;

    private List<MonitoringBreedEntity> mAllBreedList;

    private Map<String, List<TbMonitoringProject>> mProEntityListSearchMap;

    private List<Sprinner> cityList;
    private Map<String, List<Sprinner>> countryMap;
    private Map<String, List<Sprinner>> breedMap;

    private String selectProject;
    private String selectTask;

    private PadMQ mPadMQ;

    public PadMQ getPadMQ() {
        return mPadMQ;
    }

    public void setPadMQ(PadMQ padMQ) {
        this.mPadMQ = padMQ;
    }

    // 例行监测
    private List<TbMonitoringProject> mRoutinemonitoringEntityList;

    // 普查
    private List<TbMonitoringProject> mGeneralcheckEntityList;

    // 监督抽查
    private List<TbMonitoringProject> mSuperviseCheckEntityList;

    // 专项
    private List<TbMonitoringProject> mSpecialTaskEntityList;

    private Map<String, List<SampleInfoEntity>> mDoneSampleMap;

    // 未完成的抽样单
    private List<SampleInfoEntity> unfinishedSampleInfoList = new ArrayList<SampleInfoEntity>();

    // 例行监测抽样单List
    private List<TbRoutineMonitoring> routineMonitoringList = new ArrayList<TbRoutineMonitoring>();
    // 普查(风险)抽样单List
    private List<TbGeneralCheck> generalCheckList = new ArrayList<TbGeneralCheck>();
    // 监督抽查抽样单List
    private List<TbSuperviseCheck> superviseCheckList = new ArrayList<TbSuperviseCheck>();
    // 生鲜乳抽样单List
    private List<TbFreshMilk> freshMilkList = new ArrayList<TbFreshMilk>();
    // 畜禽抽样单List
    private List<TbLivestock> livestockList = new ArrayList<TbLivestock>();

    public DataManager() {
        // mDatabaseManager =
        // DatabaseManager.newInstance(HdCnApp.getApplication());
        commDao = CommonDao.getInstance();
        cleanProject();
    }

    public synchronized static DataManager getInstance() {
        if (mInstance == null) {
            synchronized (DataManager.class) {
                mInstance = new DataManager();
            }
        }
        return mInstance;
    }

    public synchronized void cleanProject() {
        getMonitoringProjectEntityList().clear();
        getRoutinemonitoringEntityList().clear();
        getGeneralcheckEntityList().clear();
        getSuperviseCheckEntityList().clear();
        getSpecialTaskEntityList().clear();
        getProEntityListSearchMap().clear();
        getTaskEntityListMap().clear();

        routineMonitoringList.clear();
        generalCheckList.clear();
        superviseCheckList.clear();
        freshMilkList.clear();
        livestockList.clear();
    }

    public MonitoringPadEntity getMonitoringPadEntity() {
        if (mMonitoringPadEntity == null) {
            if (Utils.getDefaultFalsePrefrence(HdCnApp.getApplication(), Constants.SP_KEY_AUTHORIZAD)) {
                String jsonStr = Utils.getPreference(HdCnApp.getApplication(), Constants.SP_KEY_AUTHINFO);
                Gson gson = new Gson();
                mMonitoringPadEntity = gson.fromJson(jsonStr, MonitoringPadEntity.class);
            }
        }
        return mMonitoringPadEntity;
    }

    /**
     * 取得当前单位编码
     * 
     * @return
     */
    public String getOrgCode() {
        if (ConverterUtil.isEmpty(mMonitoringPadEntity)) {
            return null;
        }
        return mMonitoringPadEntity.getOrgCode();
    }

    /**
     * 取得padId
     * 
     * @return
     */
    public String getPadId() {
        if (ConverterUtil.isEmpty(mMonitoringPadEntity)) {
            return null;
        }
        return mMonitoringPadEntity.getId();
    }

    public void setMonitoringPadEntity(MonitoringPadEntity monitoringPadEntity) {
    	LogUtil.i(monitoringPadEntity.toString());
        this.mMonitoringPadEntity = monitoringPadEntity;
    }

    public List<TbMonitoringProject> getMonitoringProjectEntityList() {
        if (mMonitoringProjectEntityList == null) {
            mMonitoringProjectEntityList = new ArrayList<TbMonitoringProject>();
        }
        return mMonitoringProjectEntityList;
    }

    public void setAllMonitoringBreedEntityList(List<MonitoringBreedEntity> monitoringBreedEntity) {
        if (mAllBreedList != null) {
            mAllBreedList.clear();
        }
        getAllMonitoringBreedEntityList().addAll(monitoringBreedEntity);
    }

    public List<MonitoringBreedEntity> getAllMonitoringBreedEntityList() {
        if (mAllBreedList == null) {
            mAllBreedList = new ArrayList<MonitoringBreedEntity>();
        }
        return mAllBreedList;
    }

    public void setMonitoringProjectEntityList(List<TbMonitoringProject> monitoringProjectEntities) {
        if (mMonitoringProjectEntityList != null) {
            mMonitoringProjectEntityList.clear();
        }
        getMonitoringProjectEntityList().addAll(monitoringProjectEntities);
    }

    public List<TbMonitoringProject> getRoutinemonitoringEntityList() {
        if (mRoutinemonitoringEntityList == null) {
            mRoutinemonitoringEntityList = new ArrayList<TbMonitoringProject>();
        }
        return mRoutinemonitoringEntityList;
    }

    public void setRoutinemonitoringEntityList(List<TbMonitoringProject> routinemonitoringEntityList) {
        if (mRoutinemonitoringEntityList != null) {
            mRoutinemonitoringEntityList.clear();
        }
        this.mRoutinemonitoringEntityList.addAll(routinemonitoringEntityList);
    }

    public List<TbMonitoringProject> getGeneralcheckEntityList() {
        if (mGeneralcheckEntityList == null) {
            mGeneralcheckEntityList = new ArrayList<TbMonitoringProject>();
        }
        return mGeneralcheckEntityList;
    }

    public void setGeneralcheckEntityList(List<TbMonitoringProject> generalcheckEntityList) {
        if (mGeneralcheckEntityList != null) {
            mGeneralcheckEntityList.clear();
        }
        this.mGeneralcheckEntityList.addAll(generalcheckEntityList);
    }

    public List<TbMonitoringProject> getSuperviseCheckEntityList() {
        if (mSuperviseCheckEntityList == null) {
            mSuperviseCheckEntityList = new ArrayList<TbMonitoringProject>();
        }
        return mSuperviseCheckEntityList;
    }

    public void setSuperviseCheckEntityList(List<TbMonitoringProject> superviseCheckEntityList) {
        if (mSuperviseCheckEntityList != null) {
            mSuperviseCheckEntityList.clear();
        }
        this.mSuperviseCheckEntityList.addAll(superviseCheckEntityList);
    }

    public List<TbMonitoringProject> getSpecialTaskEntityList() {
        if (mSpecialTaskEntityList == null) {
            mSpecialTaskEntityList = new ArrayList<TbMonitoringProject>();
        }
        return mSpecialTaskEntityList;
    }

    public void setSpecialTaskEntityList(List<TbMonitoringProject> specialTaskEntityList) {
        if (mSpecialTaskEntityList != null) {
            mSpecialTaskEntityList.clear();
        }
        this.mSpecialTaskEntityList.addAll(specialTaskEntityList);
    }

    public TbMonitoringProject getMonitoringProjectEntity(String projectCode) {
        for (TbMonitoringProject entity : getMonitoringProjectEntityList()) {
            if (entity.getProjectCode().equals(projectCode)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 取得选中项目
     * 
     * @return
     */
    public TbMonitoringProject getSelectProjectEntity() {
        return getMonitoringProjectEntity(getSelectProject());
    }

    /**
     * 取得选中任务
     * 
     * @return
     */
    public MonitoringTaskEntity getSelectTaskEntity() {
        return getMonitoringTaskEntity(getSelectProject(), getSelectTask());
    }

    public MonitoringTaskEntity getMonitoringTaskEntity(String projectCode, String taskCode) {
        List<MonitoringTaskEntity> taskList = getTaskEntityListMap().get(projectCode);
        for (MonitoringTaskEntity entity : taskList) {
            if (entity != null && TextUtils.equals(taskCode, entity.getTaskcode())) {
                return entity;
            }
        }
        return null;
    }

    // public List<SampleInfoEntity> getDoingList(String taskCode, String padId)
    // {
    // return mDatabaseManager.getSamples(taskCode, padId);
    // }

    // public void delSample(int id) {
    // this.mDatabaseManager.delSample(id);
    // }

    public Map<String, List<TbMonitoringProject>> getProEntityListSearchMap() {
        if (mProEntityListSearchMap == null) {
            mProEntityListSearchMap = new HashMap<String, List<TbMonitoringProject>>();
        }
        return mProEntityListSearchMap;
    }

    public void setProEntityListSearchMap(Map<String, List<TbMonitoringProject>> proEntityListSearchMap) {
        this.mProEntityListSearchMap = proEntityListSearchMap;
    }

    public List<SampleInfoEntity> getDoneList(String taskCode, String padId) {
        return getDoneSampleMap().get(taskCode + padId);
    }

    public Map<String, List<SampleInfoEntity>> getDoneSampleMap() {
        if (mDoneSampleMap == null) {
            mDoneSampleMap = new HashMap<String, List<SampleInfoEntity>>();
        }
        return mDoneSampleMap;
    }

    public void setDoneSampleMap(Map<String, List<SampleInfoEntity>> mDoneSampleMap) {
        this.mDoneSampleMap = mDoneSampleMap;
    }

    public Map<String, List<MonitoringTaskEntity>> getTaskEntityListMap() {
        if (mTaskEntityListMap == null) {
            mTaskEntityListMap = new HashMap<String, List<MonitoringTaskEntity>>();
        }
        return mTaskEntityListMap;
    }

    public void setTaskEntityListMap(Map<String, List<MonitoringTaskEntity>> mTaskEntityListMap) {
        this.mTaskEntityListMap = mTaskEntityListMap;
    }

    /**
     * 获取城市列表
     * 
     * @return
     */
    public List<CityInfo> getCityInfos() {
        List<CityInfo> cityInfos = new ArrayList<CityInfo>();
        int cityInfoSize = getSysAreaCodeEntity().getJsonCity().size();
        for (int i = 0; i < cityInfoSize; i++) {
            CityInfo cityInfo = new CityInfo();
            cityInfo.setCityCode(getSysAreaCodeEntity().getJsonCity().get(i).getCode());
            cityInfo.setCityName(getSysAreaCodeEntity().getJsonCity().get(i).getAreaname());
            ArrayList<CountyInfo> countyInfos = new ArrayList<CountyInfo>();
            int countySize = getSysAreaCodeEntity().getJsonCity().get(i).getJsonCounty().size();

            for (int k = 0; k < countySize; k++) {
                CountyInfo countyInfo = new CountyInfo();
                countyInfo.setCountyCode(getSysAreaCodeEntity().getJsonCity().get(i).getJsonCounty().get(k).getCode());
                countyInfo.setCountyName(getSysAreaCodeEntity().getJsonCity().get(i).getJsonCounty().get(k).getAreaname());
                countyInfos.add(countyInfo);
            }
            cityInfo.setCountyList(countyInfos);
            cityInfos.add(cityInfo);
        }
        return cityInfos;
    }

    public SysAreaCodeEntity getSysAreaCodeEntity() {
        if (mSysAreaCodeEntity == null) {
            String jsonStr = Utils.getPreference(HdCnApp.getApplication(), Constants.SP_KEY_SYS_AREA);
            if (!TextUtils.isEmpty(jsonStr)) {
                Gson gson = new Gson();
                Type listType = new TypeToken<LinkedList<SysAreaCodeEntity>>() {
                }.getType();
                List<SysAreaCodeEntity> list = gson.fromJson(jsonStr, listType);
                if (list != null && list.size() > 0) {
                    mSysAreaCodeEntity = list.get(0);
                }
            }
        }
        return mSysAreaCodeEntity;
    }

    public void setSysAreaCodeEntity(SysAreaCodeEntity mSysAreaCodeEntity) {
        this.mSysAreaCodeEntity = mSysAreaCodeEntity;
    }

    public synchronized void saveSample(String taskCode, String padId, SampleInfoEntity sampleInfoEntity) {
        Gson gson = new Gson();
        Set<String> set = Utils.getPreferenceSet(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode + padId);
        sampleInfoEntity.setSampleCode(String.valueOf(System.currentTimeMillis()));
        if (set == null || set.size() <= 0) {
            String jsonStr = gson.toJson(sampleInfoEntity, SampleInfoEntity.class);
            LogUtil.i(LogUtil.TAG, jsonStr);
            set = new HashSet<String>();
            set.add(jsonStr);
            Utils.removePreference(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode + padId);
            Utils.savePreference(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode + padId, set);
        } else {
            // sampleInfoEntity.setSampleCode(String.valueOf(set.size() + 1));
            String jsonStr = gson.toJson(sampleInfoEntity, SampleInfoEntity.class);
            set.add(jsonStr);
            LogUtil.i(LogUtil.TAG, set.size() + "");
            Utils.removePreference(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode + padId);
            Utils.savePreference(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode + padId, set);
        }
    }

    public synchronized boolean delSample(String taskCode, String padId, SampleInfoEntity sampleInfoEntity) {
        Set<String> set = Utils.getPreferenceSet(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode + padId);
        if (set == null || set.size() <= 0) {
            Utils.removePreference(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode + padId);
            return false;
        } else {
            Gson gson = new Gson();
            String jsonStr = gson.toJson(sampleInfoEntity, SampleInfoEntity.class);
            String rem = jsonStr;
            for (String item : set) {
                SampleInfoEntity info = gson.fromJson(item, SampleInfoEntity.class);
                LogUtil.i(LogUtil.TAG, "SampleCode:" + info.getSampleCode() + "=====" + sampleInfoEntity.getSampleCode());
                if (TextUtils.equals(info.getSampleCode(), sampleInfoEntity.getSampleCode())) {
                    LogUtil.i(LogUtil.TAG, "找到对应Sample");
                    rem = item;
                    break;
                }
            }
            if (set.contains(rem)) {
                set.remove(rem);
                LogUtil.i(LogUtil.TAG, set.size() + "");
                Utils.removePreference(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode);
                Utils.savePreference(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode, set);
            } else {
                return false;
            }
        }
        return true;
    }

    public synchronized List<SampleInfoEntity> getDoningSamples(String taskCode, String padId) {
        List<SampleInfoEntity> list = new ArrayList<SampleInfoEntity>();
        Gson gson = new Gson();
        Set<String> set = Utils.getPreferenceSet(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode + padId);
        if (set == null || set.size() <= 0) {
            Utils.removePreference(HdCnApp.getApplication(), Constants.SP_KEY_DONING_SAMPLE + taskCode + padId);
            return list;
        } else {
            for (String jsonStr : set) {
                if (!TextUtils.isEmpty(jsonStr)) {
                    SampleInfoEntity sample = gson.fromJson(jsonStr, SampleInfoEntity.class);
                    list.add(sample);
                }
            }
        }
        return list;
    }

    public Map<String, List<String>> getAreaListMap() {
        if (mAreaListMap == null) {
            mAreaListMap = new HashMap<String, List<String>>();
        }
        return mAreaListMap;
    }

    public void setAreaListMap(Map<String, List<String>> mAreaListMap) {
        this.mAreaListMap = mAreaListMap;
    }

    /**
     * public Map<String, List<String>> getLinkListMap() { if (mLinkListMap == null) { mLinkListMap = new HashMap<String, List<String>>(); } return mLinkListMap; } public void
     * setLinkListMap(Map<String, List<String>> mLinkListMap) { this.mLinkListMap = mLinkListMap; }
     */

    public Map<String, List<LinkInfo>> getLinkInfoListMap() {
        if (linkInfoMap == null) {
            linkInfoMap = new HashMap<String, List<LinkInfo>>();
        }
        return linkInfoMap;
    }

    public void setLinkListMap(Map<String, List<LinkInfo>> mLinkListMap) {
        this.linkInfoMap = mLinkListMap;
    }

    public Map<String, List<String>> getBreadAgrCodeListMap() {
        if (mBreadAgrCodeListMap == null) {
            mBreadAgrCodeListMap = new HashMap<String, List<String>>();
        }
        return mBreadAgrCodeListMap;
    }

    public Map<String, List<String>> getSampleNameAgrCodeListMap() {
        if (mSampleNameAgrCodeListMap == null) {
            mSampleNameAgrCodeListMap = new HashMap<String, List<String>>();
        }
        return mSampleNameAgrCodeListMap;
    }

    public Map<String, List<String>> getBreadStrListMap() {
        if (mBreadStrListMap == null) {
            mBreadStrListMap = new HashMap<String, List<String>>();
        }
        return mBreadStrListMap;
    }

    public Map<String, List<String>> getSampleNameStrListMap() {
        if (sampleNameStrListMap == null) {
            sampleNameStrListMap = new HashMap<String, List<String>>();
        }
        return sampleNameStrListMap;
    }

    public Map<String, MonitoringTaskDetailsEntity> getMonitoringTaskDetailsMap() {
        if (mMonitoringTaskDetailsMap == null) {
            mMonitoringTaskDetailsMap = new HashMap<String, MonitoringTaskDetailsEntity>();
        }
        return mMonitoringTaskDetailsMap;
    }

    public void setMonitoringTaskDetailsMap(Map<String, MonitoringTaskDetailsEntity> mMonitoringTaskDetailsEntityMap) {
        this.mMonitoringTaskDetailsMap = mMonitoringTaskDetailsEntityMap;
    }

    /**
     * 
     * 展开受检单位到内存
     * 
     * @return
     */
    public void extendsMonintringSite() {
        long count = commDao.getMonitoringSiteCount();
        DownloadBroadcast.sendBroadcast(0, "正在展开受检单位到缓存(" + count + ")....", false);
        mAllMonintringSiteList = commDao.getMonitoringSiteList();
        DownloadBroadcast.sendBroadcast(10, "展开受检单位完成....", false);
    }

    /**
     * 取得受检单位页码
     * 
     * @return
     */
    public Map<String, Object> getMonintringSitePageNum() {
        Map<String, Object> result = new HashMap<String, Object>();
        long num = 0l;
        long limit = ConverterUtil.toLong(CommonDao.LIMIT_SITE);
        long count = commDao.getMonitoringSiteCount();
        result.put("count", count);
        num = count / limit;
        if (count % limit != 0) {
            num += 1;
        }
        result.put("page", num);
        return result;
    }

    /**
     * 更新抽样单状态为1(本地非空就代表已完成)
     * 
     * @param params
     * @return
     */
    public boolean updateSampleStatusComplete(Map<String, Object> params) {
        String samplingMonadId = ConverterUtil.toString(params.get("samplingMonadId"));
        return commDao.updateSampleStatusComplete(samplingMonadId);
    }

    /**
     * 取得受检单位
     * 
     * @param index
     *            页码
     * @return
     */
    public List<MonitoringSiteEntity> getMonintringSiteList(int index) {
        List<MonitoringSiteEntity> list = commDao.getMonitoringSiteList(index);
        mAllMonintringSiteList.addAll(list);
        return list;
    }

    /**
     * 取得受检单位
     * 
     * @return
     */
    public List<MonitoringSiteEntity> getAllMonintringSiteList() {
        if (mAllMonintringSiteList == null) {
            if (mAllMonintringSiteList == null) {
                mAllMonintringSiteList = new ArrayList<MonitoringSiteEntity>();
            }
        }
        return mAllMonintringSiteList;
    }

    public void setAllMonintringSiteList(List<MonitoringSiteEntity> mAllMonintringSiteList) {
        this.mAllMonintringSiteList = mAllMonintringSiteList;
    }

    public Map<String, Integer> getFinishSampleCount() {
        if (mFinishedSampleCount == null) {
            mFinishedSampleCount = new HashMap<String, Integer>();
        }
        return mFinishedSampleCount;
    }

    public Map<String, List<MonitoringBreedEntity>> getBreadListMap() {
        if (mBreadListMap == null) {
            mBreadListMap = new HashMap<String, List<MonitoringBreedEntity>>();
        }
        return mBreadListMap;
    }

    public Map<String, List<MonitoringBreedEntity>> getSampleNameListMap() {
        if (sampleNameListMap == null) {
            sampleNameListMap = new HashMap<String, List<MonitoringBreedEntity>>();
        }
        return sampleNameListMap;
    }

    public String getSelectProject() {
        return selectProject;
    }

    public void setSelectProject(String selectProject) {
        this.selectProject = selectProject;
    }

    public String getSelectTask() {
        return selectTask;
    }

    public void setSelectTask(String selectTask) {
        this.selectTask = selectTask;
    }

    /**
     * 备份抽样单
     */
    public void backUpSamples() {
        DownloadBroadcast.sendBroadcast(0, "正在备份本地抽样单....", false);
        unfinishedSampleInfoList = commDao.getUnfinishedSampleInfoList();
        DownloadBroadcast.sendBroadcast(10, "备份本地抽样单完成,请不要强制退出程序....", false);
        // DownloadBroadcast.sendBroadcast(0, "正在备份本地例行监测抽样单....", false);
        // routineMonitoringList = commDao.getUnfinishedRoutineMonitoringList();
        // DownloadBroadcast.sendBroadcast(2, "正在备份本地普查抽样单....", false);
        // generalCheckList = commDao.getUnfinishedGeneralCheckList();
        // DownloadBroadcast.sendBroadcast(2, "正在备份本地监督抽查抽样单....", false);
        // superviseCheckList = commDao.getUnfinishedSuperviseCheckList();
        // DownloadBroadcast.sendBroadcast(2, "正在备份本地生鲜乳抽样单....", false);
        // freshMilkList = commDao.getUnfinishedFreshMilkList();
        // DownloadBroadcast.sendBroadcast(2, "正在备份本地畜禽抽样单....", false);
        // livestockList = commDao.getUnfinishedLivestockList();
        // List<SampleInfoEntity> sampleInfoList = SQLManager.getInstance().select(DatabaseManager.TB_SAMPLING_INFO, SampleInfoEntity.class, null, where, null, null, null, orderBy, null);
        // for (SampleInfoEntity sampleInfo : sampleInfoList) {
        // String temp = sampleInfo.getTemplete();
        // if (Constants.TEMPLETE_ROUTINE_MONITORING.equals(temp)) {
        // SQLManager.getInstance().selectOne(DatabaseManager.TB_ROUTINE_MONITORING,RoutinemonitoringEntity.class
        // }
        // if (Constants.TEMPLETE_SUPERVISE_CHECK.equals(temp)) {
        //
        // }
        // if (Constants.TEMPLETE_GENERAL_CHECK.equals(temp)) {
        //
        // }
        // if (Constants.TEMPLETE_FRESH_MILK.equals(temp)) {
        //
        // }
        // if (Constants.TEMPLETE_LIVESTOCK.equals(temp)) {
        //
        // }
        // }
    }

    /**
     * 恢复抽样单数据
     */
    public void recoverSamples() {
        DownloadBroadcast.sendBroadcast(0, "正在恢复本地抽样单....", false);
        commDao.insertSampleInfo(unfinishedSampleInfoList);
        DownloadBroadcast.sendBroadcast(20, "恢复本地抽样单完成....", false);
    }

    /**
     * 下载到抽样单后需要下载对应的图片
     */
    public void downLoadServerImages() {
        int count = 0;
        DownloadBroadcast.sendBroadcast(0, "正在下载抽样单图片(" + count + ")....", false);
        List<SampleInfoEntity> sampleList = commDao.getSampleInfoList();
        for (SampleInfoEntity sample : sampleList) {
            String samplePath = sample.getSamplePath();
            if (ConverterUtil.isNotEmpty(samplePath)) {
                File file = new File(Utils.getImageDirectory(), samplePath);
                // 如果文件本地不存在就从服务器上下载
                if (!file.exists()) {
                    String webContextPath = Utils.getPreference(HdCnApp.getInstance().getApplicationContext(), Constants.SP_KEY_WEBCONTEXTPATH);
                    ImageDownload.download(file, webContextPath + samplePath);
                }
            }
            // 扫描到的抽样单+1
            count++;
            DownloadBroadcast.sendBroadcast(0, "正在下载抽样单图片(" + count + ")....", false);
        }
    }

    /**
     * 取得所有项目
     * 
     * @return
     */
    public List<TbMonitoringProject> getProjectList() {
        mMonitoringProjectEntityList = commDao.getProjectList();
        return mMonitoringProjectEntityList;
    }

    /**
     * 按任务取得未完成的抽样单
     * 
     * @param params
     *            参数
     * @return
     */
    public List<SampleInfoEntity> getUnfinishedListByTask(Map<String, Object> params) {
        return commDao.getUnfinishedListByTask(ConverterUtil.toString(params.get("taskCode")), ConverterUtil.toString(params.get("padId")));
    }

    /**
     * 按任务取得已完成的抽样单
     * 
     * @param params
     *            参数
     * @return
     */
    public List<SampleInfoEntity> getFinishedListByTask(Map<String, Object> params) {
        return commDao.getFinishedListByTask(ConverterUtil.toString(params.get("taskCode")), ConverterUtil.toString(params.get("padId")));
    }

    /**
     * 按任务取得未完成的抽样单数量
     * 
     * @return
     */
    public int getUnfinishedCountBySelectTask() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("taskCode", getSelectTask());
        params.put("padId", getPadId());
        return getUnfinishedCountByTask(params);
    }

    /**
     * 按任务取得未完成的抽样单数量
     * 
     * @param params
     *            参数
     * @return
     */
    public int getUnfinishedCountByTask(Map<String, Object> params) {
        return commDao.getUnfinishedCountByTask(ConverterUtil.toString(params.get("taskCode")), ConverterUtil.toString(params.get("padId")));
    }

    /**
     * 按任务取得已完成的抽样单数量
     * 
     * @return
     */
    public int getFinishedCountBySelectTask() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("taskCode", getSelectTask());
        params.put("padId", getPadId());
        return getFinishedCountByTask(params);
    }

    /**
     * 按任务取得已完成的抽样单数量
     * 
     * @param params
     *            参数
     * @return
     */
    public int getFinishedCountByTask(Map<String, Object> params) {
        return commDao.getFinishedCountByTask(ConverterUtil.toString(params.get("taskCode")), ConverterUtil.toString(params.get("padId")));
    }

    /**
     * 取得任务详情
     * 
     * @param params
     *            参数
     * @return
     */
    public MonitoringTaskDetailsEntity getTaskDetailsEntity(Map<String, Object> params) {
        return commDao.getTaskDetailsEntity(ConverterUtil.toString(params.get("taskCode")));
    }

    /**
     * 取得项目对应的任务
     * 
     * @param projectCode
     *            项目ID
     * @return
     */
    public List<MonitoringTaskEntity> getTaskListByProjectCode(String projectCode) {
        List<MonitoringTaskEntity> list = getTaskEntityListMap().get(projectCode);
        if (ConverterUtil.isEmpty(list)) {
            list = commDao.getTaskListByProjectCode(projectCode);
            if (ConverterUtil.isEmpty(list)) {
                return null;
            }
            getTaskEntityListMap().put(projectCode, list);
        }
        return list;
    }

    /**
     * 取得当前项目对应的抽样品种
     * 
     * @return
     */
    public List<Sprinner> getBreedBySelectProject() {
        if (ConverterUtil.isNotEmpty(breedMap)) {
            if (breedMap.containsKey(getSelectProject())) {
                return breedMap.get(getSelectProject());
            }
        } else {
            breedMap = new HashMap<String, List<Sprinner>>();
        }
        List<Sprinner> breedList = commDao.getBreedByProject(getSelectProject());
        breedMap.put(getSelectProject(), breedList);
        return breedList;
    }

    /**
     * 删除抽样单
     * 
     * @param params
     *            参数列表
     * @return
     */
    public boolean deleteSample(Map<String, Object> params) {
        String sampleCode = ConverterUtil.toString(params.get("sampleCode"));
        String templete = ConverterUtil.toString(params.get("templete"));
        return commDao.deleteSample(sampleCode, templete);
    }

    /**
     * 保存抽样单
     * 
     * @param params
     * @return
     */
    public boolean saveSample(Map<String, Object> params) {
        SampleInfoEntity sampleInfo = (SampleInfoEntity) params.get("sampleInfo");
        if (ConverterUtil.isEmpty(sampleInfo)) {
            return true;
        }
        Map<String, List<?>> insertMap = new LinkedHashMap<String, List<?>>();
        List<SampleInfoEntity> mainList = new ArrayList<SampleInfoEntity>();
        List<?> subList = new ArrayList<Object>();
        String templete = sampleInfo.getTemplete();
        // 例行监测抽样单表
        if (Constants.TEMPLETE_ROUTINE_MONITORING.equals(templete)) {
            for (RoutinemonitoringEntity subTable : sampleInfo.getRoutinemonitoringList()) {
                SampleInfoEntity mainTable = null;
                if (commDao.existsRoutineSampleById(subTable.getId())) {
                    if (ConverterUtil.isNotEmpty(sampleInfo.getSampleCode())) {
                        // 如果它的主表是它自己就直接用
                        if (sampleInfo.getSampleCode().equals(subTable.getSampleCode())) {
                            mainTable = sampleInfo;
                        } else {
                            // 如果不是就重新从数据库取得
                            mainTable = commDao.getSampleInfoBySampleCode(subTable.getSampleCode());
                            // 从数据取得的主表数据要替换成新的
                            // 单位全称(受检)
                            mainTable.setUnitFullname(sampleInfo.getUnitFullname());
                            // 法定代表人(受检)
                            mainTable.setLegalPerson(sampleInfo.getLegalPerson());
                            // 通讯地址(受检)
                            mainTable.setUnitAddress(sampleInfo.getUnitAddress());
                            // 邮政编码
                            mainTable.setZipCode(sampleInfo.getZipCode());
                            // 联系电话
                            mainTable.setTelphone(sampleInfo.getTelphone());
                            // 抽样时间
                            mainTable.setSamplingDate(sampleInfo.getSamplingDate());
                            // 抽样人员
                            mainTable.setSamplingPersons(sampleInfo.getSamplingPersons());
                            // 经纬度
                            mainTable.setLongitude(sampleInfo.getLongitude());
                            mainTable.setLatitude(sampleInfo.getLatitude());
                            // 监测环节
                            mainTable.setMonitoringLink(sampleInfo.getMonitoringLink());
                            // 地市
                            mainTable.setCityCode(sampleInfo.getCityCode());
                            // 区县
                            mainTable.setCountyCode(sampleInfo.getCountyCode());
                        }
                    }
                } else {
                    // 如果子表不存在 需要新增主表
                    mainTable = sampleInfo.clone();
                    mainTable.setId(ConverterUtil.getUUID());
                }
                // 样品ID
                mainTable.setSampleCode(subTable.getSampleCode());
                // 图片路径
                mainTable.setSamplePath(subTable.getSamplePath());
                // 设置条码
                mainTable.setdCode(subTable.getdCode());
                // 农产品code
                mainTable.setAgrCode(subTable.getAgrCode());
                // 样品名称
                mainTable.setSampleName(subTable.getSampleName());
                // 备注
                mainTable.setRemark(subTable.getRemark());
                // 抽样详细地址
                mainTable.setSamplingAddress(subTable.getSamplingAddress());
                String samplingTime = null;
                if (ConverterUtil.isEmpty(subTable.getSamplingTime())) {
                    samplingTime = ConverterUtil.toString(System.currentTimeMillis());
                } else {
                    samplingTime = subTable.getSamplingTime();
                }
                // 抽样单定义时间
                mainTable.setSamplingTime(samplingTime);
                mainList.add(mainTable);
            }
            subList = sampleInfo.getRoutinemonitoringList();
            insertMap.put(CommonDao.TB_SAMPLING_INFO, mainList);
            insertMap.put(CommonDao.TB_ROUTINE_MONITORING, subList);
        }
        // 普查(风险)抽样单
        if (Constants.TEMPLETE_GENERAL_CHECK.equals(templete)) {
            SampleInfoEntity mainTable = sampleInfo;
            GeneralcheckEntity subTable = sampleInfo.getGeneralcheckEntity();

            if (ConverterUtil.isEmpty(mainTable.getSamplingTime())) {
                mainTable.setSamplingTime(ConverterUtil.toString(System.currentTimeMillis()));
            }

            mainList.add(sampleInfo);
            List<GeneralcheckEntity> gcList = new ArrayList<GeneralcheckEntity>();
            gcList.add(subTable);
            subList = gcList;
            insertMap.put(CommonDao.TB_SAMPLING_INFO, mainList);
            insertMap.put(CommonDao.TB_GENERAL_CHECK, subList);
        }
        // 监督抽查抽样单
        if (Constants.TEMPLETE_SUPERVISE_CHECK.equals(templete)) {
            SampleInfoEntity mainTable = sampleInfo;
            SuperviseCheckEntity subTable = sampleInfo.getSuperviseCheckEntity();

            if (ConverterUtil.isEmpty(mainTable.getSamplingTime())) {
                mainTable.setSamplingTime(ConverterUtil.toString(System.currentTimeMillis()));
            }

            mainList.add(sampleInfo);
            List<SuperviseCheckEntity> scList = new ArrayList<SuperviseCheckEntity>();
            scList.add(subTable);
            subList = scList;
            insertMap.put(CommonDao.TB_SAMPLING_INFO, mainList);
            insertMap.put(CommonDao.TB_SUPERVISE_CHECK, subList);
        }
        // 生鲜乳抽样单
        if (Constants.TEMPLETE_FRESH_MILK.equals(templete)) {
            SampleInfoEntity mainTable = sampleInfo;
            FreshMilkEntity subTable = sampleInfo.getFreshMilkEntity();

            if (ConverterUtil.isEmpty(mainTable.getSamplingTime())) {
                mainTable.setSamplingTime(ConverterUtil.toString(System.currentTimeMillis()));
            }

            mainList.add(sampleInfo);
            List<FreshMilkEntity> scList = new ArrayList<FreshMilkEntity>();
            scList.add(subTable);
            subList = scList;
            insertMap.put(CommonDao.TB_SAMPLING_INFO, mainList);
            insertMap.put(CommonDao.TB_FRESH_MILK, subList);
        }
        // 畜禽抽样单
        if (Constants.TEMPLETE_LIVESTOCK.equals(templete)) {
            for (LivestockEntity subTable : sampleInfo.getLivestockEntityList()) {
                SampleInfoEntity mainTable = null;
                if (commDao.existsLiveStockSampleById(subTable.getId())) {
                    if (ConverterUtil.isNotEmpty(sampleInfo.getSampleCode())) {
                        // 如果它的主表是它自己就直接用
                        if (sampleInfo.getSampleCode().equals(subTable.getSampleCode())) {
                            mainTable = sampleInfo;
                        } else {
                            // 如果不是就重新从数据库取得
                            mainTable = commDao.getSampleInfoBySampleCode(subTable.getSampleCode());
                            // 从数据取得的主表数据要替换成新的
                            // 经纬度
                            mainTable.setLongitude(sampleInfo.getLongitude());
                            mainTable.setLatitude(sampleInfo.getLatitude());
                            // 抽样场所
                            mainTable.setMonitoringLink(sampleInfo.getMonitoringLink());
                            // 抽样时间
                            mainTable.setSamplingDate(sampleInfo.getSamplingDate());
                            // 地市
                            mainTable.setCityCode(sampleInfo.getCityCode());
                            // 区县
                            mainTable.setCountyCode(sampleInfo.getCountyCode());
                            // 农产品code
                            mainTable.setAgrCode(sampleInfo.getAgrCode());
                            // 样品名称
                            mainTable.setSampleName(sampleInfo.getSampleName());
                            // 抽样人员
                            mainTable.setSamplingPersons(sampleInfo.getSamplingPersons());
                            // 单位全称(受检)
                            mainTable.setUnitFullname(sampleInfo.getUnitFullname());
                            // 通讯地址(受检)
                            mainTable.setUnitAddress(sampleInfo.getUnitAddress());
                            // 邮政编码
                            mainTable.setZipCode(sampleInfo.getZipCode());
                            // 电话
                            mainTable.setTelphone(sampleInfo.getTelphone());
                        }
                    }
                } else {
                    // 如果子表不存在 需要新增主表
                    mainTable = sampleInfo.clone();
                    mainTable.setId(ConverterUtil.getUUID());
                }
                // 样品ID
                mainTable.setSampleCode(subTable.getSampleCode());
                // 图片路径
                mainTable.setSamplePath(subTable.getSamplePath());
                // 设置条码
                mainTable.setdCode(subTable.getdCode());
                // 抽样详细地址
                mainTable.setSamplingAddress(subTable.getSamplingAddress());
                // 备注
                mainTable.setRemark(subTable.getRemark());
                String samplingTime = null;
                // 抽样单创建时间
                if (ConverterUtil.isEmpty(subTable.getSamplingTime())) {
                    samplingTime = ConverterUtil.toString(System.currentTimeMillis());
                } else {
                    samplingTime = subTable.getSamplingTime();
                }
                // 抽样单定义时间
                mainTable.setSamplingTime(samplingTime);
                mainList.add(mainTable);
            }
            subList = sampleInfo.getLivestockEntityList();
            insertMap.put(CommonDao.TB_SAMPLING_INFO, mainList);
            insertMap.put(CommonDao.TB_LIVESTOCK, subList);
        }
        return commDao.insertSampleInfo(insertMap);
    }

    /**
     * 取得当前项目对应的监测环节
     * 
     * @return
     */
    public List<Sprinner> getMonitorLinkBySelectProject() {
        return commDao.getMonitorLinkList(getSelectProject());
    }

    /**
     * 取得地市下拉框数据
     * 
     * @return
     */
    public List<Sprinner> getCitySpinnerList() {
        if (ConverterUtil.isNotEmpty(cityList)) {
            return cityList;
        }
        cityList = commDao.getCityList();
        return cityList;
    }

    /**
     * 取得地市对应的区县
     * 
     * @param cityCode
     * @return
     */
    public List<Sprinner> getCountrySpinnerList(Map<String, Object> params) {
        String cityCode = ConverterUtil.toString(params.get("cityCode"));
        if (ConverterUtil.isNotEmpty(countryMap) && countryMap.containsKey(countryMap.get(cityCode))) {
            return countryMap.get(cityCode);
        } else {
            countryMap = new HashMap<String, List<Sprinner>>();
            List<Sprinner> countryList = commDao.getCountryList(cityCode);
            if (ConverterUtil.isNotEmpty(countryList)) {
                countryMap.put(cityCode, countryList);
                return countryList;
            }
        }
        return null;
    }

    /**
     * 取得接口对应的数据
     * 
     * @param taskMethod
     * @return
     */
    public Object getTask(int taskMethod, Map<String, Object> params) {
        Object result = null;
        switch (taskMethod) {
        case TASK_GET_PROJECT_LIST:
            // 取得所有项目
            result = getProjectList();
            break;
        case TASK_GET_UNFINISHED_LIST_BY_TASK:
            // 按任务取得未完成的抽样单
            result = getUnfinishedListByTask(params);
            break;
        case TASK_GET_FINISHED_LIST_BY_TASK:
            // 按任务取得已完成的抽样单
            result = getFinishedListByTask(params);
            break;
        case TASK_GET_UNFINISHED_COUNT_BY_TASK:
            // 按任务取得未完成的抽样单数量
            result = getUnfinishedCountByTask(params);
            break;
        case TASK_GET_FINISHED_COUNT_BY_TASK:
            // 按任务取得已完成的抽样单数量
            result = getFinishedCountByTask(params);
            break;
        case TASK_GET_TASK_DETAIL_LIST:
            // 取得任务详情
            result = getTaskDetailsEntity(params);
            break;
        case TASK_GET_CITY_LIST:
            // 取得地址
            result = getCitySpinnerList();
            break;
        case TASK_GET_COUNTRY_LIST:
            // 取得区县
            result = getCountrySpinnerList(params);
            break;
        case TASK_GET_MONITOR_LINK_LIST:
            // 取得监测环节
            result = getMonitorLinkBySelectProject();
            break;
        case TASK_GET_BREED_LIST:
            // 取得抽样品种
            result = getBreedBySelectProject();
            break;
        case TASK_SAVE_SAMPLE:
            // 保存抽样单
            result = saveSample(params);
            break;
        case TASK_DELETE_SAMPLE:
            // 保存抽样单
            params.put("result", deleteSample(params));
            result = params;
            break;
        case TASK_GET_MONITORING_SITE:
            // 取得受检单位
            result = getMonintringSiteList(ConverterUtil.toInteger(params.get("page")));
            // getMonintringSiteList();
            break;
        case TASK_GET_MONITORING_SITE_PAGE:
            // 取得受检单位页码
            result = getMonintringSitePageNum();
            break;
        case TASK_DELETE_SAMPLE_BY_SUBID:
            // 用子表ID删除抽样单
            result = getMonintringSitePageNum();
            break;
        case TASK_UPDATE_SAMPLE_STATUS:
            // 更新抽样单状态(提交成功后要把状态改为已完成)
            result = updateSampleStatusComplete(params);
            break;
        default:
            break;
        }
        return result;
    }
}
