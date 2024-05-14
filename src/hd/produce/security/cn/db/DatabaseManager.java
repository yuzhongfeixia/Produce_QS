package hd.produce.security.cn.db;

import hd.produce.security.cn.HdCnApp;
import hd.produce.security.cn.data.SampleInfoEntity;
import hd.utils.cn.ConverterUtil;
import hd.utils.cn.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DatabaseManager extends SQLiteOpenHelper {
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

    private static final String TAG = "DatabaseManager";

    private static SQLiteDatabase _db;

    private static DatabaseManager databaseManager;

    private static final String DB_NAME = "Produce_QS_DB.db";
    // 在手机里存放数据库的位置(/data/data/hd.produce.security.cn/Produce_QS_DB.db)
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/hd.produce.security.cn/";

    private static List<String> CREATE = new ArrayList<String>();

    private static List<String> UPDATE = new ArrayList<String>();

    private static final int DB_VERSION = UPDATE.size() + 1;

    private Context context = null;

    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public static DatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(HdCnApp.getApplication().getApplicationContext());
        }
        databaseManager.open();
        return databaseManager;
    }

    /**
     * 空间不够存储的时候设为只读
     * 
     * @throws android.database.sqlite.SQLiteException
     */
    private synchronized void open() throws SQLiteException {
        try {
            _db = databaseManager.getWritableDatabase();
        } catch (SQLiteException e) {
            _db = databaseManager.getReadableDatabase();
        }
    }

    /**
     * 调用SQLiteDatabase对象的close()方法关闭数据库
     */
    public synchronized void close() {
        if (_db != null) {
            _db.close();
            _db = null;
        }
    }

    public synchronized void execSQL(String sql, Object... params) {
        if (_db != null && _db.isOpen()) {
            _db.execSQL(sql, params);
        }
    }

    /**
     * 批量插入
     * 
     * @param tableName
     *            表名
     * @param entityList
     *            实体list
     */
    public synchronized <T> void insertOrUpdateBatch(String tableName, List<T> entityList) {
        if (ConverterUtil.isNotEmpty(tableName) && _db.isOpen()) {
            try {
                _db.beginTransaction();
                for (T entity : entityList) {
                    ContentValues contentValues = ConverterUtil.getContentValues(entity);
                    // _db.insert(table, null, contentValues);
                    // insert Or update 代替检测ID冲突
                    _db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                }
                _db.setTransactionSuccessful();
            } catch (Exception e) {
                LogUtil.e(TAG, "Error in insertBatch", e);
            } finally {
                _db.endTransaction();
            }
        }
    }

    /**
     * 批量插入
     * 
     * @param tableName
     *            表名
     * @param entityList
     *            实体list
     */
    public synchronized <T> void insertBatch(String tableName, List<T> entityList) {
        if (ConverterUtil.isNotEmpty(tableName) && _db.isOpen()) {
            try {
                _db.beginTransaction();
                int commit = 0;
                for (T entity : entityList) {
                    ContentValues contentValues = ConverterUtil.getContentValues(entity);
                    // _db.insert(table, null, contentValues);
                    // insert Or update 代替检测ID冲突
                    _db.insert(tableName, null, contentValues);
                    if (commit % 500 == 0) {
                        _db.setTransactionSuccessful();
                        _db.endTransaction();
                        _db.beginTransaction();
                    }
                    commit++;
                }
                _db.setTransactionSuccessful();
            } catch (Exception e) {
                LogUtil.e(TAG, "Error in insertBatch", e);
            } finally {
                _db.endTransaction();
            }
        }
    }

    /**
     * 清空表
     * 
     * @param tableName
     *            表名
     */
    public synchronized <E> void truncate(String tableName) {
        if (ConverterUtil.isNotEmpty(tableName) && _db.isOpen()) {
            try {
                _db.beginTransaction();
                // 清空表数据
                execSQL("DELETE FROM " + tableName);
                // // 重置自增长ID
                // execSQL("DELETE FROM sqlite_sequence WHERE name=?", tableName);
                _db.setTransactionSuccessful();
            } catch (Exception e) {
                LogUtil.e(TAG, "Error in insertBatch", e);
            } finally {
                _db.endTransaction();
            }
        }
    }

    /**
     * 插入
     * 
     * @param tableName
     *            表名
     * @param entity
     *            实体类
     */
    public synchronized void insert(String tableName, Object entity) {
        if (entity instanceof List<?>) {
            LogUtil.e(TAG, "Error in insert:Your " + entity.getClass() + " is list,this method needs Object(entity),please use insertBatch(List<Object>).");
            return;
        }
        if (ConverterUtil.isNotEmpty(tableName) && _db.isOpen()) {
            try {
                ContentValues contentValues = ConverterUtil.getContentValues(entity);
                _db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (Exception e) {
                LogUtil.e(TAG, "Error in insert", e);
            }
        }
    }

    /**
     * 使用SQLite查询一条数据
     * 
     * @param tableName
     *            表名
     * @param clazz
     *            容器Class(Entity or Map)
     * @param columns
     *            列名
     * @param selection
     *            where条件
     * @param selectionArgs
     *            where参数
     * @param groupBy
     *            分组条件
     * @param having
     *            where聚合条件
     * @param orderBy
     *            排序条件
     * @return 结果集
     */
    public synchronized <T> Object selectOne(String tableName, Class<T> clazz, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        List<T> result = select(tableName, clazz, columns, selection, selectionArgs, groupBy, having, orderBy, "1");
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询某表中的第一条记录
     * @param tableName
     * @param clazz
     * @return
     */
    public synchronized <T> Object selectOne(String tableName, Class<T> clazz) {
        return selectOne(tableName, clazz, null, null, null, null, null, null);
    }

    /**
     * 查询某表全部数据
     * 
     * @param tableName
     * @param clazz
     * @return
     */
    public synchronized <T> List<T> select(String tableName, Class<T> clazz) {
        return select(tableName, clazz, null, null, null, null, null, null, null);
    }

    /**
     * 使用SQLite查询数据
     * 
     * 
     * @param tableName
     *            表名
     * @param clazz
     *            容器Class(Entity or Map)
     * @param columns
     *            列名
     * @param selection
     *            where条件
     * @param selectionArgs
     *            where参数
     * @param groupBy
     *            分组条件
     * @param having
     *            where聚合条件
     * @param orderBy
     *            排序条件
     * @param limit
     *            返回记录数
     * @return 结果集
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> List<T> select(String tableName, Class<T> clazz, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        List<T> result = null;
        try {
            Cursor cursor = _db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor != null) {
                if (clazz.equals(Map.class)) {
                    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                    while (cursor.moveToNext()) {
                        Map<String, Object> rowMap = new HashMap<String, Object>();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            switch (cursor.getType(i)) {
                            case Cursor.FIELD_TYPE_BLOB:
                            case Cursor.FIELD_TYPE_NULL:
                            case Cursor.FIELD_TYPE_STRING:
                                rowMap.put(cursor.getColumnName(i), cursor.getString(i));
                                break;
                            case Cursor.FIELD_TYPE_FLOAT:
                                rowMap.put(cursor.getColumnName(i), cursor.getFloat(i));
                                break;
                            case Cursor.FIELD_TYPE_INTEGER:
                                rowMap.put(cursor.getColumnName(i), cursor.getInt(i));
                                break;
                            }
                        }
                        resultList.add(rowMap);
                    }
                    result = (List<T>) resultList;
                } else {
                    result = new ArrayList<T>();
                    Map<String, Method> setMethod = ConverterUtil.getAllMethods(clazz, ConverterUtil.METHOD_SET);
                    while (cursor.moveToNext()) {
                        T row = clazz.newInstance();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            Method rowSetMethod = setMethod.get(cursor.getColumnName(i));
                            if (null == rowSetMethod) {
                                continue;
                            }
                            switch (cursor.getType(i)) {
                            case Cursor.FIELD_TYPE_BLOB:
                            case Cursor.FIELD_TYPE_NULL:
                            case Cursor.FIELD_TYPE_STRING:
                                rowSetMethod.invoke(row, cursor.getString(i));
                                break;
                            case Cursor.FIELD_TYPE_FLOAT:
                                rowSetMethod.invoke(row, cursor.getFloat(i));
                                break;
                            case Cursor.FIELD_TYPE_INTEGER:
                                rowSetMethod.invoke(row, cursor.getInt(i));
                                break;
                            }
                        }
                        result.add(row);
                    }
                }
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Error in select", e);
            result = null;
        }
        return result;
    }

    // /**
    // * ID重复性校验
    // */
    // private synchronized boolean isExist(String table, String id) {
    // if (TextUtils.isEmpty(id)) {
    // return false;
    // }
    // String where = "id=? AND delFlg=0";
    // String[] args = { id };
    // boolean isExist = false;
    // Cursor cursor = _db.query(table, null, where, args, null, null, null);
    // if (cursor != null) {
    // isExist = cursor.moveToNext();
    // cursor.close();
    // }
    // return isExist;
    // }

    public synchronized void saveSample(SampleInfoEntity sampleInfoEntity) {
        if (_db.isOpen()) {
            _db.execSQL(
                    "insert into unfinished(taskCode,dCode,samplePath,agrCode, cityCode ," + "monitoringLink, padId, samplingDateStr ," + "unitFullname, unitAddress, zipCode,"
                            + "legalPerson , contact , telphone," + " fax , remark , sampleCode ," + "labCode , isQualified , sampleStatus ," + "detectionCode , spCode , countyCode ,"
                            + "projectName , sampleName , projectCode ," + "complete , reportingDate , samplingOrgCode ,"
                            + "monitorType ,year ,orgCode ,printCount ,cityAndCountry ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[] { sampleInfoEntity.getTaskCode(), sampleInfoEntity.getdCode(), sampleInfoEntity.getSamplePath(), sampleInfoEntity.getAgrCode(), sampleInfoEntity.getCityCode(),
                            sampleInfoEntity.getMonitoringLink(), sampleInfoEntity.getPadId(), sampleInfoEntity.getSamplingDateStr(), sampleInfoEntity.getUnitFullname(),
                            sampleInfoEntity.getUnitAddress(), sampleInfoEntity.getZipCode(), sampleInfoEntity.getLegalPerson(), sampleInfoEntity.getContact(), sampleInfoEntity.getTelphone(),
                            sampleInfoEntity.getFax(), sampleInfoEntity.getRemark(), sampleInfoEntity.getSampleCode(), sampleInfoEntity.getLabCode(), sampleInfoEntity.getIsQualified(),
                            sampleInfoEntity.getSampleStatus(), sampleInfoEntity.getDetectionCode(), sampleInfoEntity.getSpCode(), sampleInfoEntity.getCountyCode(), sampleInfoEntity.getProjectName(),
                            sampleInfoEntity.getSampleName(), sampleInfoEntity.getProjectCode(), sampleInfoEntity.getComplete(), sampleInfoEntity.getReportingDate(),
                            sampleInfoEntity.getSamplingOrgCode(), sampleInfoEntity.getMonitorType(), sampleInfoEntity.getYear(), sampleInfoEntity.getOrgCode(), sampleInfoEntity.getPrintCount(),
                            sampleInfoEntity.getCityAndCountry(), });
            LogUtil.i(TAG, "save");
        }
    }

    private void initCreateTables() {
        // 创建检测项目数据表
        CREATE.add(createMonitoringProject());
        // 监测任务表
        CREATE.add(createMonitoringTask());
        // 创建监测任务详情表
        CREATE.add(createMonitoringTaskDetails());
        // 创建受检单位表
        CREATE.add(createMonitoringSite());
        // 创建抽样品种表
        CREATE.add(createMonitoringBreed());
        // 创建抽样地市表
        CREATE.add(createMonitoringCity());
        // 创建抽样区县表
        CREATE.add(createMonitoringCountry());
        // 创建抽样环节表
        CREATE.add(createMonitoringLinkInfo());
        // 创建抽样单
        CREATE.add(createSampleInfo());
        // 创建抽样单子表
        CREATE.add(createRoutineMonitoring());
        // 创建普查(风险)抽样单表
        CREATE.add(createGeneralCheck());
        // 创建监督抽查抽样单
        CREATE.add(createSuperviseCheck());
        // 创建生鲜乳抽样单表
        CREATE.add(createFreshMilk());
        // 创建畜禽抽样单表
        CREATE.add(createLivestock());
    }

    /**
     * 创建检测项目数据表
     * 
     * @param db
     */
    private String createMonitoringProject() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_MONITORING_PROJECT);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 方案ID
        sql.append("planCode NVARCHAR(32), ");
        // 项目名称
        sql.append("name NVARCHAR(200), ");
        // 牵头单位编号
        sql.append("leadunit NVARCHAR(32), ");
        // 监测开始时间
        sql.append("starttime NVARCHAR(32), ");
        // 监测结束时间
        sql.append("endtime NVARCHAR(32), ");
        // 是否抽检分离
        sql.append("detached NVARCHAR(50), ");
        // 项目状态
        sql.append("state NVARCHAR(50), ");
        // 项目ID
        sql.append("projectCode NVARCHAR(32), ");
        // 发布时间
        sql.append("publishDate NVARCHAR(32), ");
        // 抽样单模板
        sql.append("sampleTemplet NVARCHAR(50), ");
        // 行业
        sql.append("industryCode NVARCHAR(50), ");
        // 整拆包分发FLG
        sql.append("packingFlg INTEGER, ");
        // 机构名称
        sql.append("ogrname NVARCHAR(50), ");
        // 方案级别
        sql.append("plevel NVARCHAR(1), ");
        // 抽样品种(接口返回,任务详情显示用)
        sql.append("projectBree NVARCHAR(500), ");
        // 任务详情的抽样环节(字符串)
        sql.append("linkInfo NVARCHAR(200), ");
        // 监测类型
        sql.append("type NVARCHAR(50))");
        return sql.toString();
    }

    /**
     * 创建监测任务表
     * 
     * @param db
     */
    private String createMonitoringTask() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_MONITORING_TASK);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 项目ID
        sql.append("projectCode NVARCHAR(32), ");
        // 任务名称
        sql.append("taskname NVARCHAR(50), ");
        // 质检机构
        sql.append("orgCode NVARCHAR(32), ");
        // 行政区划
        sql.append("areacode NVARCHAR(6), ");
        // 监测环节
        sql.append("monitoringLink NVARCHAR(50), ");
        // 抽样品种
        sql.append("agrCode NVARCHAR(32), ");
        // 抽样数量
        sql.append("samplingCount INTEGER, ");
        // 任务编码
        sql.append("taskcode NVARCHAR(32), ");
        // 方案级别
        sql.append("plevel NVARCHAR(1), ");
        // 抽样单模板
        sql.append("sampleTemplet NVARCHAR(50), ");
        // 任务详情的抽样地区(字符串,接口返回,画面显示)
        sql.append("sampleArea NVARCHAR(50), ");
        // 监测类型
        sql.append("monitorType NVARCHAR(32))");
        return sql.toString();
    }

    /**
     * 创建监测任务详情表
     * 
     * @param db
     */
    private String createMonitoringTaskDetails() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_MONITORING_TASK_DETAILS);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 客户端(PAD)
        sql.append("padId NVARCHAR(32), ");
        // 分配数量
        sql.append("taskCount INTEGER, ");
        // 分配时间
        sql.append("assignTime NVARCHAR(32), ");
        // 任务ID
        sql.append("taskCode NVARCHAR(32), ");
        // 完成情况
        sql.append("taskStatus NVARCHAR(50) DEFAULT ('0'), ");
        // 发布单位
        sql.append("releaseunit NVARCHAR(500), ");
        // 截止时间
        sql.append("endTime NVARCHAR(50))");
        return sql.toString();
    }

    /**
     * 创建受检单位表
     * 
     * @param db
     */
    private String createMonitoringSite() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_MONITORING_SITE);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 监测点代码
        sql.append("code NVARCHAR(32), ");
        // 监测点名称
        sql.append("name NVARCHAR(500), ");
        // 法定代表人或负责人
        sql.append("legalPerson NVARCHAR(32), ");
        // 邮编
        sql.append("zipcode NVARCHAR(32), ");
        // 详细地址
        sql.append("address NVARCHAR(255), ");
        // 联系电话
        sql.append("contact NVARCHAR(15), ");
        // 所属区域
        sql.append("areacode NVARCHAR(6), ");
        // 所属区县
        sql.append("areacode2 NVARCHAR(6), ");
        // 监测环节
        sql.append("monitoringLink NVARCHAR(50), ");
        // 企业性质
        sql.append("enterprise NVARCHAR(50), ");
        // 企业规模
        sql.append("scale NVARCHAR(50), ");
        // 主管单位
        sql.append("unit NVARCHAR(128), ");
        // 经度地理坐标
        sql.append("longitude NVARCHAR(32), ");
        // 纬度地理坐标
        sql.append("latitude NVARCHAR(32), ");
        // 联系人
        sql.append("contactPerson NVARCHAR(32), ");
        // 传真
        sql.append("fax NVARCHAR(15), ");
        // 乡镇/街道
        sql.append("townandstreet NVARCHAR(500), ");
        // 规模类型
        sql.append("scaleType NVARCHAR(10), ");
        // 规模单位
        sql.append("scaleUnit NVARCHAR(10))");
        return sql.toString();
    }

    /**
     * 创建抽样品种表
     * 
     * @param db
     */
    private String createMonitoringBreed() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_MONITORING_BREED);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 父节点
        sql.append("pId NVARCHAR(32), ");
        // 项目ID
        sql.append("projectCode NVARCHAR(32), ");
        // 农产品名称
        sql.append("agrName NVARCHAR(50), ");
        // 农产品编码
        sql.append("agrCode NVARCHAR(32))");
        return sql.toString();
    }

    /**
     * 创建抽样地区(地市)
     * 
     * @param db
     */
    private String createMonitoringCity() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_MONITORING_AREA_CITY);
        // 地区代码
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 地区父节点代码
        sql.append("pid NVARCHAR(32), ");
        // 地区名称
        sql.append("name NVARCHAR(32))");
        return sql.toString();
    }

    /**
     * 创建抽样地区(区县)
     * 
     * @param db
     */
    private String createMonitoringCountry() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_MONITORING_AREA_COUNTRY);
        // 地区代码
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 地区父节点代码
        sql.append("pid NVARCHAR(32), ");
        // 地区名称
        sql.append("name NVARCHAR(32))");
        return sql.toString();
    }

    /**
     * 创建抽样环节
     * 
     * @param db
     */
    private String createMonitoringLinkInfo() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_MONITORING_LINK_INFO);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 抽样环节ID
        sql.append("code NVARCHAR(32), ");
        // 项目ID
        sql.append("projectId NVARCHAR(32), ");
        // 抽样环节名称
        sql.append("name NVARCHAR(50))");
        return sql.toString();
    }

    /**
     * 创建抽样单表
     * 
     * @return
     */
    private String createSampleInfo() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_SAMPLING_INFO);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 监测任务
        sql.append("taskCode NVARCHAR(32), ");
        // 二维码
        sql.append("dCode NVARCHAR(52), ");
        // 样品图片
        sql.append("samplePath NVARCHAR(255), ");
        // 经度地理坐标
        sql.append("longitude NVARCHAR(32), ");
        // 纬度地理坐标
        sql.append("latitude NVARCHAR(32), ");
        // 农产品CODE
        sql.append("agrCode NVARCHAR(32), ");
        // 抽样地市
        sql.append("cityCode NVARCHAR(6), ");
        // 抽样区县
        sql.append("countyCode NVARCHAR(6), ");
        // 地区名称(接口返回)
        sql.append("cityAndCountry NVARCHAR(50), ");
        // 抽样环节
        sql.append("monitoringLink NVARCHAR(50), ");
        // 抽样环境名称(接口返回)
        sql.append("monitoringLinkName NVARCHAR(100), ");
        // 抽样人(Pad)
        sql.append("padId NVARCHAR(32), ");
        // 抽样时间
        sql.append("samplingDate NVARCHAR(32), ");
        // 单位编码(受检)
        sql.append("unitFullcode NVARCHAR(32), ");
        // 单位全称(受检)
        sql.append("unitFullname NVARCHAR(500), ");
        // 通讯地址(受检)
        sql.append("unitAddress NVARCHAR(128), ");
        // 邮编(受检)
        sql.append("zipCode NVARCHAR(6), ");
        // 法定代表人(受检)
        sql.append("legalPerson NVARCHAR(64), ");
        // 联系人(受检)
        sql.append("contact NVARCHAR(64), ");
        // 电话(受检)
        sql.append("telphone NVARCHAR(64), ");
        // 传真(受检)
        sql.append("fax NVARCHAR(64), ");
        // 备注
        sql.append("remark NVARCHAR(255), ");
        // 样品编号
        sql.append("sampleCode NVARCHAR(32), ");
        // 样品名称(接口返回)
        sql.append("sampleName NVARCHAR(32), ");
        // 实验室编码
        sql.append("labCode NVARCHAR(32), ");
        // 样品是否合格
        sql.append("isQualified NVARCHAR(50), ");
        // 抽样单状态
        sql.append("sampleStatus NVARCHAR(50), ");
        // 检测机构
        sql.append("detectionCode NVARCHAR(32), ");
        // 制样编码
        sql.append("spCode NVARCHAR(32), ");
        // 信息完整度
        sql.append("complete NVARCHAR(1) DEFAULT ('0'), ");
        // 上报时间
        sql.append("reportingDate NVARCHAR(32), ");
        // 抽样单位
        sql.append("samplingOrgCode NVARCHAR(32), ");
        // 打印数量
        sql.append("printCount INTEGER, ");
        // 检测上报时间
        sql.append("detectionReportingDate NVARCHAR(32), ");
        // 抽样详细地址
        sql.append("samplingAddress NVARCHAR(128), ");
        // 抽样人员姓名
        sql.append("samplingPersons NVARCHAR(50), ");
        // 抽样单ID
        sql.append("samplingMonadId NVARCHAR(32))");
        return sql.toString();
    }

    /**
     * 创建例行监测抽样单表
     * 
     * @return
     */
    private String createRoutineMonitoring() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_ROUTINE_MONITORING);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 样品编号
        sql.append("sampleCode NVARCHAR(32), ");
        // 样品来源
        sql.append("sampleSource NVARCHAR(64), ");
        // 样品量(n/N)
        sql.append("sampleCount NVARCHAR(50), ");
        // 任务来源
        sql.append("taskSource NVARCHAR(64), ");
        // 执行标准
        sql.append("execStanderd NVARCHAR(64), ");
        // 抽样单ID
        sql.append("samplingMonadId  NVARCHAR(32))");
        return sql.toString();
    }

    /**
     * 创建普查(风险)抽样单表
     * 
     * @return
     */
    private String createGeneralCheck() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_GENERAL_CHECK);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 样品编号
        sql.append("sampleCode NVARCHAR(32), ");
        // 商标
        sql.append("tradeMark NVARCHAR(32), ");
        // 包装
        sql.append("pack NVARCHAR(50), ");
        // 规格
        sql.append("specifications NVARCHAR(32), ");
        // 标识
        sql.append("flag NVARCHAR(50), ");
        // 执行标准
        sql.append("execStanderd NVARCHAR(128), ");
        // 生产日期或批号
        sql.append("batchNumber NVARCHAR(128), ");
        // 产品认证情况
        sql.append("productCer NVARCHAR(50), ");
        // 证书编号
        sql.append("productCerNo NVARCHAR(128), ");
        // 抽样数量
        sql.append("samplingCount NVARCHAR(50), ");
        // 受检人与摊位号
        sql.append("stall NVARCHAR(128),");
        // 电话
        sql.append("telphone NVARCHAR(16),");
        // 传真
        sql.append("fax NVARCHAR(16),");
        // 单位性质(生产)
        sql.append("unitProperties NVARCHAR(16),");
        // 单位名称(生产)
        sql.append("unitName NVARCHAR(128),");
        // 通讯地址(生产)
        sql.append("unitAddress NVARCHAR(128),");
        // 邮编(生产)
        sql.append("zipCode NVARCHAR(6),");
        // 法定代表人(生产)
        sql.append("legalPerson NVARCHAR(16),");
        // 联系人(生产)
        sql.append("contacts NVARCHAR(16),");
        // 电话(生产)
        sql.append("telphone2 NVARCHAR(16),");
        // 传真(生产)
        sql.append("fax2 NVARCHAR(16),");
        // 抽样文件编号
        sql.append("samplingNo NVARCHAR(32),");
        // 等级
        sql.append("grade NVARCHAR(32),");
        // 抽样基数
        sql.append("samplingCardinal NVARCHAR(50))");
        return sql.toString();
    }

    /**
     * 创建监督抽查抽样单表
     * 
     * @return
     */
    private String createSuperviseCheck() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_SUPERVISE_CHECK);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 样品编号
        sql.append("sampleCode NVARCHAR(32), ");
        // 商标
        sql.append("tradeMark NVARCHAR(32), ");
        // 包装
        sql.append("pack NVARCHAR(50), ");
        // 规格
        sql.append("specifications NVARCHAR(32), ");
        // 标识
        sql.append("flag NVARCHAR(50), ");
        // 生产日期或批号
        sql.append("batchNumber NVARCHAR(128), ");
        // 执行标准
        sql.append("execStanderd NVARCHAR(128), ");
        // 产品认证情况
        sql.append("productCer NVARCHAR(50), ");
        // 证书编号
        sql.append("productCerNo NVARCHAR(32), ");
        // 获证日期
        sql.append("certificateTime NVARCHAR(32), ");
        // 抽样数量
        sql.append("samplingCount NVARCHAR(50), ");
        // 抽样基数
        sql.append("samplingBaseCount NVARCHAR(50),");
        // 通知书编号及有效期
        sql.append("noticeDetails NVARCHAR(128))");
        return sql.toString();
    }

    /**
     * 创建生鲜乳抽样单表
     * 
     * @return
     */
    private String createFreshMilk() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_FRESH_MILK);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 样品编号
        sql.append("sampleCode NVARCHAR(32), ");
        // 抽样数量
        sql.append("samplingCount NVARCHAR(50), ");
        // 抽样基数
        sql.append("samplingBaseCount NVARCHAR(50),");
        // 生鲜乳类型
        sql.append("type NVARCHAR(50), ");
        // 类型备注
        sql.append("typeRemark NVARCHAR(64), ");
        // 生鲜乳收购许可证
        sql.append("buyLicence NVARCHAR(50), ");
        // 许可证号
        sql.append("licenceNo NVARCHAR(32), ");
        // 许可证备注
        sql.append("licenceRemark NVARCHAR(64), ");
        // 生鲜乳准运证
        sql.append("navicert NVARCHAR(50), ");
        // 准运证号
        sql.append("navicertNo NVARCHAR(32), ");
        // 准运证备注
        sql.append("navicertRemark NVARCHAR(64), ");
        // 生鲜乳交接单
        sql.append("deliveryReceitp NVARCHAR(50), ");
        // 交接单备注
        sql.append("deliveryReceitpRemark NVARCHAR(64), ");
        // 交奶去向
        sql.append("direction NVARCHAR(128), ");
        // 联系电话(法定代表人)
        sql.append("telphone NVARCHAR(16), ");
        // 受检人
        sql.append("examinee NVARCHAR(16), ");
        // 联系电话(受检人)
        sql.append("telphone2 NVARCHAR(16), ");
        // 抽样日期和时间
        sql.append("samplingDate NVARCHAR(32))");
        return sql.toString();
    }

    /**
     * 创建畜禽抽样单表
     * 
     * @return
     */
    private String createLivestock() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TB_LIVESTOCK);
        // ID
        sql.append("(id NVARCHAR(32) PRIMARY KEY NOT NULL, ");
        // 样品编号
        sql.append("sampleCode NVARCHAR(32), ");
        // 商标
        sql.append("tradeMark NVARCHAR(32), ");
        // 包装
        sql.append("pack NVARCHAR(50), ");
        // 规格
        sql.append("specifications NVARCHAR(32), ");
        // 标识
        sql.append("flag NVARCHAR(50), ");
        // 签封标志
        sql.append("signFlg NVARCHAR(50), ");
        // 畜主/货主
        sql.append("cargoOwner NVARCHAR(50), ");
        // 动物产地/来源
        sql.append("animalOrigin NVARCHAR(50), ");
        // 检疫证号
        sql.append("cardNumber NVARCHAR(50), ");
        // 抽样依据
        sql.append("taskSource NVARCHAR(50), ");
        // 抽样数量
        sql.append("samplingCount NVARCHAR(50), ");
        // 抽样基数
        sql.append("samplingBaseCount NVARCHAR(50),");
        // 保存情况
        sql.append("saveSaveSituation NVARCHAR(50), ");
        // 抽样方式
        sql.append("samplingMode NVARCHAR(50), ");
        // 抽样单ID
        sql.append("samplingMonadId NVARCHAR(50))");
        return sql.toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initCreateTables();
        for (String sql : CREATE) {
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                LogUtil.e(TAG, "Failed to create the tables", e);
            }
        }
        onUpgrade(db, 1, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // int version = newVersion - 1;
        // for (int i = 0; i < version; i++) {
        // try {
        // db.execSQL(UPDATE[i]);
        // } catch (Exception e) {
        // }
        // }
        for (String sql : UPDATE) {
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                LogUtil.e(TAG, "Failed to update the tables", e);
            }
        }
    }

    /**
     * 删除数据库
     * 
     * @param context
     * @return
     */
    public boolean deleteDatabase() {
        return context.deleteDatabase(DB_NAME);
    }
}
