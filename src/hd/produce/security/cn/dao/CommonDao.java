package hd.produce.security.cn.dao;

import hd.produce.security.cn.data.FreshMilkEntity;
import hd.produce.security.cn.data.GeneralcheckEntity;
import hd.produce.security.cn.data.LivestockEntity;
import hd.produce.security.cn.data.MonitoringSiteEntity;
import hd.produce.security.cn.data.MonitoringTaskDetailsEntity;
import hd.produce.security.cn.data.MonitoringTaskEntity;
import hd.produce.security.cn.data.RoutinemonitoringEntity;
import hd.produce.security.cn.data.SampleInfoEntity;
import hd.produce.security.cn.data.SuperviseCheckEntity;
import hd.produce.security.cn.db.SQLManager;
import hd.produce.security.cn.entity.Sprinner;
import hd.produce.security.cn.entity.TbMonitoringProject;
import hd.utils.cn.Constants;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.LogUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;

public class CommonDao {
    // 返回受检单位多少条结果
    public static String LIMIT_SITE = "300";
    // 项目数据表
    public static String TB_MONITORING_PROJECT = "TB_MONITORING_PROJECT";
    // 监测任务表
    public static String TB_MONITORING_TASK = "TB_MONITORING_TASK";
    // 监测任务详情表
    public static String TB_MONITORING_TASK_DETAILS = "TB_MONITORING_TASK_DETAILS";
    // 受检单位
    public static String TB_MONITORING_SITE = "TB_MONITORING_SITE";
    // 抽样品种表
    public static String TB_MONITORING_BREED = "TB_MONITORING_BREED";
    // 抽样地区(地市)
    public static String TB_MONITORING_AREA_CITY = "TB_MONITORING_AREA_CITY";
    // 抽样地区(区县)
    public static String TB_MONITORING_AREA_COUNTRY = "TB_MONITORING_AREA_COUNTRY";
    // 抽样环节
    public static String TB_MONITORING_LINK_INFO = "TB_MONITORING_LINK_INFO";
    // 抽样单
    public static String TB_SAMPLING_INFO = "TB_SAMPLING_INFO";
    // 例行监测抽样单表
    public static String TB_ROUTINE_MONITORING = "TB_ROUTINE_MONITORING";
    // 普查(风险)抽样单
    public static String TB_GENERAL_CHECK = "TB_GENERAL_CHECK";
    // 监督抽查抽样单
    public static String TB_SUPERVISE_CHECK = "TB_SUPERVISE_CHECK";
    // 生鲜乳抽样单
    public static String TB_FRESH_MILK = "TB_FRESH_MILK";
    // 畜禽抽样单
    public static String TB_LIVESTOCK = "TB_LIVESTOCK";

    private static CommonDao commonDao;
    private SQLManager sqlManager;

    private CommonDao() {
        // 建立数据库连接
        sqlManager = SQLManager.getInstance();
    }

    /***
     * 单立模式
     * 
     * @return
     */
    public static CommonDao getInstance() {
        if (null == commonDao) {
            return new CommonDao();
        }
        return commonDao;
    }

    /**
     * 取得受检单位
     * 
     * @return
     */
    public List<MonitoringSiteEntity> getMonitoringSiteList() {
        return sqlManager.select(TB_MONITORING_SITE, MonitoringSiteEntity.class);
    }

    /**
     * 取得受检单位
     * 
     * @param limit
     *            每页显示条数
     * @param index
     *            页码
     * @return
     */
    public List<MonitoringSiteEntity> getMonitoringSiteList(int index) {
        int num = ConverterUtil.toInteger(LIMIT_SITE) * (index - 1);
        String[] params = { LIMIT_SITE, ConverterUtil.toString(num) };
        String sql = "select * from " + TB_MONITORING_SITE + " where code is not null order by code limit ? offset ?";
        return sqlManager.select(sql, MonitoringSiteEntity.class, params);
        // return sqlManager.select(TB_MONITORING_SITE, MonitoringSiteEntity.class);
    }

    /**
     * 取得受检单位的数量
     * 
     * @return
     */
    public long getMonitoringSiteCount() {
        return sqlManager.count(TB_MONITORING_SITE, null, "code is not null", null, null, null, null, null);
    }

    /**
     * 按任务取得未完成的抽样单
     * 
     * @param taskCode
     *            任务ID
     * @param padId
     *            padId
     * @return
     */
    public List<SampleInfoEntity> getUnfinishedListByTask(String taskCode, String padId) {
        String where = "where t1.taskCode = t2.taskCode and t2.projectCode = t3.projectCode and t1.taskCode = ? and t1.padId = ? and t1.sampleStatus is null ";
        // 取得主表
        List<SampleInfoEntity> entityList = getSampleList(where, taskCode, padId);
        if (ConverterUtil.isEmpty(entityList)) {
            return null;
        }
        return entityList;
    }

    /**
     * 按任务取得已完成的抽样单
     * 
     * @param taskCode
     * @param padId
     * @return
     */
    public List<SampleInfoEntity> getFinishedListByTask(String taskCode, String padId) {
        String where = "where t1.taskCode = t2.taskCode and t2.projectCode = t3.projectCode and t1.taskCode = ? and t1.padId = ? and t1.sampleStatus is not null ";
        // 取得主表
        List<SampleInfoEntity> entityList = getSampleList(where, taskCode, padId);
        if (ConverterUtil.isEmpty(entityList)) {
            return null;
        }
        return entityList;
    }

    /**
     * 按任务取得未完成的抽样单数量
     * 
     * @param taskCode
     * @param padId
     * @return
     */
    public int getUnfinishedCountByTask(String taskCode, String padId) {
        String where = "taskCode = ? and padId = ? and sampleStatus is null";
        String[] param = { taskCode, padId };
        String orderBy = "samplingTime asc";
        return sqlManager.count(TB_SAMPLING_INFO, null, where, param, null, null, orderBy, null);
    }

    /**
     * 按任务取得已完成的抽样单数量
     * 
     * @param taskCode
     * @param padId
     * @return
     */
    public int getFinishedCountByTask(String taskCode, String padId) {
        String where = "taskCode = ? and padId = ? and sampleStatus is not null";
        String[] param = { taskCode, padId };
        String orderBy = "samplingTime asc";
        return sqlManager.count(TB_SAMPLING_INFO, null, where, param, null, null, orderBy, null);
    }

    /**
     * 取得任务详情
     * 
     * @param taskCode
     *            任务ID
     * @return
     */
    public MonitoringTaskDetailsEntity getTaskDetailsEntity(String taskCode) {
        String[] param = { taskCode };
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t2.* FROM TB_MONITORING_TASK t1,TB_MONITORING_TASK_DETAILS t2 where t1.taskCode = t2.taskCode and t1.taskCode = ? order by areacode");
        return (MonitoringTaskDetailsEntity) sqlManager.selectOne(sql.toString(), MonitoringTaskDetailsEntity.class, param);
    }

    /**
     * 取得抽样单信息
     * 
     * @param where
     *            查询条件
     * @param params
     *            参数
     * @return
     */
    public List<SampleInfoEntity> getSampleList(String where, String... params) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t1.*,t3.templete FROM TB_SAMPLING_INFO t1,TB_MONITORING_TASK t2,TB_MONITORING_PROJECT t3 ");
        sql.append(where);
        sql.append("order by samplingTime");
        // 取得主表
        List<SampleInfoEntity> entityList = sqlManager.select(sql.toString(), SampleInfoEntity.class, params);
        if (ConverterUtil.isEmpty(entityList)) {
            return null;
        }

        // 变量主表取得子表
        for (SampleInfoEntity entity : entityList) {
            String templete = entity.getTemplete();
            String subWhere = "sampleCode = ?";

            String[] param = { entity.getSampleCode() };
            // 例行监测抽样单表
            if (Constants.TEMPLETE_ROUTINE_MONITORING.equals(templete)) {
                String getSubSql = "select t2.id as id, t2.sampleCode as sampleCode, t1.dCode as dCode, t1.samplePath as samplePath, t1.agrCode as agrCode, t1.sampleName as sampleName,"
                        + " t1.remark as remark, t1.samplingAddress as samplingAddress, t1.samplingTime as samplingTime, t2.sampleSource as sampleSource, t2.sampleCount as sampleCount, "
                        + "t2.taskSource as taskSource, t2.execStanderd as execStanderd, t1.samplingMonadId as samplingMonadId " + "from TB_SAMPLING_INFO t1, " + "(select t2.* "
                        + "from TB_SAMPLING_INFO t1, TB_ROUTINE_MONITORING t2 " + "where t1.samplingMonadId = t2.samplingMonadId and t2.samplingMonadId = ? and t1.sampleCode = ?) t2 "
                        + "where t1.sampleCode = t2.sampleCode order by samplingTime asc";
                String[] rParam = { entity.getSamplingMonadId(), entity.getSampleCode() };
                entity.setRoutinemonitoringList(sqlManager.select(getSubSql, RoutinemonitoringEntity.class, rParam));
                // entity.putDataToSubTable(Constants.TEMPLETE_ROUTINE_MONITORING);
                continue;
            }
            // 普查(风险)抽样单
            if (Constants.TEMPLETE_GENERAL_CHECK.equals(templete)) {
                entity.setGeneralcheckEntity((GeneralcheckEntity) sqlManager.selectOne(TB_GENERAL_CHECK, GeneralcheckEntity.class, null, subWhere, param, null, null, null));
                continue;
            }
            // 监督抽查抽样单
            if (Constants.TEMPLETE_SUPERVISE_CHECK.equals(templete)) {
                entity.setSuperviseCheckEntity((SuperviseCheckEntity) sqlManager.selectOne(TB_SUPERVISE_CHECK, SuperviseCheckEntity.class, null, subWhere, param, null, null, null));
                continue;
            }
            // 生鲜乳抽样单
            if (Constants.TEMPLETE_FRESH_MILK.equals(templete)) {
                entity.setFreshMilkEntity((FreshMilkEntity) sqlManager.selectOne(TB_FRESH_MILK, FreshMilkEntity.class, null, subWhere, param, null, null, null));
                continue;
            }
            // 畜禽抽样单
            if (Constants.TEMPLETE_LIVESTOCK.equals(templete)) {
                String getSubSql = "select t2.id as id, t2.sampleCode as sampleCode, t1.dCode as dCode, t1.samplePath as samplePath, t2.cargoOwner as cargoOwner, t2.animalOrigin as animalOrigin, "
                        + "t2.cardNumber as cardNumber, t2.taskSource as taskSource, t1.samplingAddress as samplingAddress, t1.remark as remark, t1.samplingTime as samplingTime, t1.samplingMonadId as samplingMonadId, "
                        + "t2.samplingCount as samplingCount, t2.samplingBaseCount as samplingBaseCount, t2.tradeMark as tradeMark, t2.samplingMode as samplingMode, t2.pack as pack, t2.signFlg as signFlg "
                        + "from TB_SAMPLING_INFO t1, " + "(select t2.* " + "from TB_SAMPLING_INFO t1, TB_LIVESTOCK t2 "
                        + "where t1.samplingMonadId = t2.samplingMonadId and t2.samplingMonadId = ? and t1.sampleCode = ?) t2 " + "where t1.sampleCode = t2.sampleCode order by samplingTime asc";
                String[] lParam = { entity.getSamplingMonadId(), entity.getSampleCode() };
                entity.setLivestockEntityList(sqlManager.select(getSubSql, LivestockEntity.class, lParam));
                // entity.putDataToSubTable(Constants.TEMPLETE_LIVESTOCK);
                continue;
            }
        }
        return entityList;
    }

    /**
     * 根据样品编码取得抽样单
     * 
     * @param sampleCode
     * @return
     */
    public SampleInfoEntity getSampleInfoBySampleCode(String sampleCode) {
        String where = "sampleCode = ?";
        String[] param = { sampleCode };
        return (SampleInfoEntity) sqlManager.selectOne(TB_SAMPLING_INFO, SampleInfoEntity.class, null, where, param, null, null, null);
    }

    /**
     * 取得未完成抽样单数据(即本地没有提交的数据)
     * 
     * @return
     */
    public List<SampleInfoEntity> getUnfinishedSampleInfoList() {
        String where = "where t1.taskCode = t2.taskCode and t2.projectCode = t3.projectCode and t1.sampleStatus is null ";
        return getSampleList(where);
    }

    //
    // /**
    // * 取得未完成抽样单数据(即本地没有提交的数据)
    // *
    // * @return
    // */
    // private <T> List<T> getUnfinishedList(String table, Class<T> clazz) {
    // List<T> result = null;
    // StringBuilder sql = new StringBuilder();
    // sql.append("SELECT t1.*,t2.sampleSource,t2.sampleCount,t2.taskSource,t2.execStanderd ");
    // sql.append("FROM TB_SAMPLING_INFO t1,");
    // sql.append(table);
    // sql.append(" t2 ");
    // sql.append("WHERE t1.sampleCode = t2.sampleCode AND t1.sampleStatus is null ");
    // sql.append("ORDER BY t1.samplingTime asc");
    // result = sqlManager.select(sql.toString(), clazz);
    // return result;
    // }
    //
    // /**
    // * 取得未完成例行监测抽样单数据(即本地没有提交的数据)
    // *
    // * @return
    // */
    // public List<TbRoutineMonitoring> getUnfinishedRoutineMonitoringList() {
    // return getUnfinishedList(TB_ROUTINE_MONITORING, TbRoutineMonitoring.class);
    // }
    //
    // /**
    // * 取得未完成普查(风险)抽样单数据(即本地没有提交的数据)
    // *
    // * @return
    // */
    // public List<TbGeneralCheck> getUnfinishedGeneralCheckList() {
    // return getUnfinishedList(TB_GENERAL_CHECK, TbGeneralCheck.class);
    // }
    //
    // /**
    // * 取得未完成监督抽查抽样单数据(即本地没有提交的数据)
    // *
    // * @return
    // */
    // public List<TbSuperviseCheck> getUnfinishedSuperviseCheckList() {
    // return getUnfinishedList(TB_SUPERVISE_CHECK, TbSuperviseCheck.class);
    // }
    //
    // /**
    // * 取得未完成生鲜乳抽样单数据(即本地没有提交的数据)
    // *
    // * @return
    // */
    // public List<TbFreshMilk> getUnfinishedFreshMilkList() {
    // return getUnfinishedList(TB_FRESH_MILK, TbFreshMilk.class);
    // }
    //
    // /**
    // * 取得未完成畜禽抽样单数据(即本地没有提交的数据)
    // *
    // * @return
    // */
    // public List<TbLivestock> getUnfinishedLivestockList() {
    // return getUnfinishedList(TB_LIVESTOCK, TbLivestock.class);
    // }

    /**
     * 取得已完成的抽样主表
     * 
     * @return
     */
    public List<SampleInfoEntity> getFinishedSampleInfoList() {
        String where = "sampleStatus is not null";
        String orderBy = "samplingTime asc";
        // 取得主表
        return sqlManager.select(TB_SAMPLING_INFO, SampleInfoEntity.class, null, where, null, null, null, orderBy, null);
    }

    /**
     * 取得抽样主表
     * 
     * @return
     */
    public List<SampleInfoEntity> getSampleInfoList() {
        // 取得主表
        return sqlManager.select(TB_SAMPLING_INFO, SampleInfoEntity.class, null, null, null, null, null, null, null);
    }

    /**
     * 插入抽样单
     * 
     * @param insertMap
     */
    public boolean insertSampleInfo(Map<String, List<?>> insertMap) {
        // 统一插入
        return sqlManager.insertBatch(insertMap);
    }

    /**
     * 插入抽样信息
     * 
     * @param entityList
     */
    public void insertSampleInfo(List<SampleInfoEntity> entityList) {
        // 不能为空
        if (ConverterUtil.isEmpty(entityList)) {
            return;
        }
        Map<String, List<?>> insertMap = new LinkedHashMap<String, List<?>>();
        List<RoutinemonitoringEntity> rmList = new ArrayList<RoutinemonitoringEntity>();
        List<GeneralcheckEntity> gcList = new ArrayList<GeneralcheckEntity>();
        List<SuperviseCheckEntity> scList = new ArrayList<SuperviseCheckEntity>();
        List<FreshMilkEntity> fmList = new ArrayList<FreshMilkEntity>();
        List<LivestockEntity> ltList = new ArrayList<LivestockEntity>();
        // 制作子表list
        for (SampleInfoEntity sample : entityList) {
            List<RoutinemonitoringEntity> rmEntity = sample.getRoutinemonitoringList();
            if (ConverterUtil.isNotEmpty(rmEntity)) {
                rmList.addAll(rmEntity);
                continue;
            }
            GeneralcheckEntity gcEntity = sample.getGeneralcheckEntity();
            if (ConverterUtil.isNotEmpty(gcEntity)) {
                gcList.add(gcEntity);
                continue;
            }
            SuperviseCheckEntity scEntity = sample.getSuperviseCheckEntity();
            if (ConverterUtil.isNotEmpty(scEntity)) {
                scList.add(scEntity);
                continue;
            }
            FreshMilkEntity fmEntity = sample.getFreshMilkEntity();
            if (ConverterUtil.isNotEmpty(fmEntity)) {
                fmList.add(fmEntity);
                continue;
            }
            List<LivestockEntity> ltEntity = sample.getLivestockEntityList();
            if (ConverterUtil.isNotEmpty(ltEntity)) {
                ltList.addAll(ltEntity);
                continue;
            }
        }
        insertMap.put(TB_SAMPLING_INFO, entityList);
        if (ConverterUtil.isNotEmpty(rmList)) {
            insertMap.put(TB_ROUTINE_MONITORING, rmList);
        }
        if (ConverterUtil.isNotEmpty(gcList)) {
            insertMap.put(TB_GENERAL_CHECK, gcList);
        }
        if (ConverterUtil.isNotEmpty(scList)) {
            insertMap.put(TB_SUPERVISE_CHECK, scList);
        }
        if (ConverterUtil.isNotEmpty(fmList)) {
            insertMap.put(TB_FRESH_MILK, fmList);
        }
        if (ConverterUtil.isNotEmpty(rmList)) {
            insertMap.put(TB_LIVESTOCK, ltList);
        }
        // 统一插入
        sqlManager.insertBatch(insertMap);
    }

    /**
     * 取得所有项目
     * 
     * @return
     */
    public List<TbMonitoringProject> getProjectList() {
        return sqlManager.select(TB_MONITORING_PROJECT, TbMonitoringProject.class, null, null, null, null, null, null, null);
    }

    /**
     * 取得项目对应的抽样品种
     * 
     * @param projectCode
     * @return
     */
    public List<Sprinner> getBreedByProject(String projectCode) {
        String[] param = { projectCode };
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.agrCode as id,t.agrName as name FROM " + TB_MONITORING_BREED + " t where t.projectCode = ?");
        return sqlManager.select(sql.toString(), Sprinner.class, param);
    }

    /**
     * 取得项目对应的任务
     * 
     * @param projectCode
     *            项目ID
     * @return
     */
    public List<MonitoringTaskEntity> getTaskListByProjectCode(String projectCode) {
        String where = "projectCode = ?";
        String[] param = { projectCode };
        String orderBy = "areacode";
        return sqlManager.select(TB_MONITORING_TASK, MonitoringTaskEntity.class, null, where, param, null, null, orderBy, null);
    }

    /**
     * 取得地市列表
     * 
     * @return
     */
    public List<Sprinner> getCityList() {
        String where = "pid = ?";
        // 江苏省行政区划代码320000
        String[] param = { "320000" };
        String orderBy = "id";
        return sqlManager.select(TB_MONITORING_AREA_CITY, Sprinner.class, null, where, param, null, null, orderBy, null);
    }

    /**
     * 取得区县列表
     * 
     * @return
     */
    public List<Sprinner> getCountryList(String cityCode) {
        String where = "pid = ?";
        String[] param = { cityCode };
        String orderBy = "id";
        return sqlManager.select(TB_MONITORING_AREA_COUNTRY, Sprinner.class, null, where, param, null, null, orderBy, null);
    }

    /**
     * 取得项目对于的监测环节
     * 
     * @param projectCode
     *            项目ID
     * @return
     */
    public List<Sprinner> getMonitorLinkList(String projectCode) {
        String[] param = { projectCode };
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.code as id,t.name FROM " + TB_MONITORING_LINK_INFO + " t where t.projectId = ?");
        return sqlManager.select(sql.toString(), Sprinner.class, param);
    }

    /**
     * 删除抽样单
     * 
     * @param id
     * @return
     */
    public boolean deleteSample(String sampleCode, String templete) {
        try {
            // 先删除子表
            if (ConverterUtil.isNotEmpty(templete)) {
                // 例行监测抽样单表
                if (Constants.TEMPLETE_ROUTINE_MONITORING.equals(templete)) {
                    sqlManager.delete(TB_ROUTINE_MONITORING, "sampleCode = ?", sampleCode);
                }
                // 普查(风险)抽样单
                if (Constants.TEMPLETE_GENERAL_CHECK.equals(templete)) {
                    sqlManager.delete(TB_GENERAL_CHECK, "sampleCode = ?", sampleCode);
                }
                // 监督抽查抽样单
                if (Constants.TEMPLETE_SUPERVISE_CHECK.equals(templete)) {
                    sqlManager.delete(TB_SUPERVISE_CHECK, "sampleCode = ?", sampleCode);
                }
                // 生鲜乳抽样单
                if (Constants.TEMPLETE_FRESH_MILK.equals(templete)) {
                    sqlManager.delete(TB_FRESH_MILK, "sampleCode = ?", sampleCode);
                }
                // 畜禽抽样单
                if (Constants.TEMPLETE_LIVESTOCK.equals(templete)) {
                    sqlManager.delete(TB_LIVESTOCK, "sampleCode = ?", sampleCode);
                }
            }
            sqlManager.delete(TB_SAMPLING_INFO, "sampleCode = ?", sampleCode);
        } catch (Exception e) {
            LogUtil.e("CommonDao", "Error in deleteSample", e);
            return false;
        }
        return true;
    }

    /**
     * 更新抽样单状态为1(本地非空就代表已完成)
     * 
     * @param samplingMonadId
     *            抽样单ID
     * @return
     */
    public boolean updateSampleStatusComplete(String samplingMonadId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sampleStatus", "1");
        String where = "samplingMonadId = ?";
        sqlManager.update(TB_SAMPLING_INFO, contentValues, where, samplingMonadId);
        return true;
    }

    /**
     * 根据ID取得例行监测子表
     * 
     * @param id
     * @return
     */
    public boolean existsRoutineSampleById(String id) {
        String where = "id = ?";
        String[] param = { id };
        RoutinemonitoringEntity rmEntity = (RoutinemonitoringEntity) sqlManager.selectOne(TB_ROUTINE_MONITORING, RoutinemonitoringEntity.class, null, where, param, null, null, null);
        if (ConverterUtil.isEmpty(rmEntity)) {
            return false;
        }
        return true;
    }

    /**
     * 根据ID取得畜禽子表
     * 
     * @param id
     * @return
     */
    public boolean existsLiveStockSampleById(String id) {
        String where = "id = ?";
        String[] param = { id };
        LivestockEntity rmEntity = (LivestockEntity) sqlManager.selectOne(TB_LIVESTOCK, LivestockEntity.class, null, where, param, null, null, null);
        if (ConverterUtil.isEmpty(rmEntity)) {
            return false;
        }
        return true;
    }
}
