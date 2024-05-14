package hd.produce.security.cn.db;

import hd.utils.cn.ConverterUtil;
import hd.utils.cn.FileUtils;
import hd.utils.cn.LogUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class SQLManager {
    // Log标签
    private static final String TAG = "DataManager";
    // 数据库名称
    private static final String DB_NAME = "Produce_QS_DB.db";
    private static final String DB_NAME_JOURNAL = "Produce_QS_DB.db-journal";
    // 在手机里存放数据库的位置(/data/data/hd.produce.security.cn/Produce_QSdb.db)
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/hd.produce.security.cn/databases/";

    private static SQLManager dataManager;
    private static SQLiteDatabase db;

    private SQLManager() {
        // 初始化数据库
        initDatabase();
    }

    /***
     * 单立模式
     * 
     * @return
     */
    public static SQLManager getInstance() {
        if (null == dataManager) {
            dataManager = new SQLManager();
        }
        return dataManager;
    }

    /**
     * 取得数据库文件
     * 
     * @return
     */
    public static File getDatabaseFile() {
        return new File(DB_PATH + DB_NAME);
    }

    /**
     * 取得数据库文件
     * 
     * @return
     */
    public static File getDatabaseJournalFile() {
        return new File(DB_PATH + DB_NAME_JOURNAL);
    }

    /**
     * 初始化数据文件
     */
    private synchronized void initDatabase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        // 如果文件不存在则下载该文件
        if (dbFile.exists()) {
            // 打开数据库链接
            open(dbFile);
        }
    }

    /**
     * 更新数据库
     * 
     * @param newDbFile
     *            新数据库文件
     */
    public synchronized void overWriteDatabase() {
//        byte[] data = Base64.decode(newDbStr, Base64.DEFAULT);
        File database = SQLManager.getDatabaseFile();
        // 检测路径是否存在
        File parentPath = new File(database.getParent());
        // 不存在就创建
        if(!parentPath.exists()){
            parentPath.mkdirs();
        }
        File newDbFile = new File(database.getParent() + "/" + "tempDb.db");
        File newDbFileJournal = new File(database.getParent() + "/" + "tempDb.db-journal");
//        FileOutputStream fileOutPutStream;
//        try {
//            fileOutPutStream = new FileOutputStream(newDbFile);
//            fileOutPutStream.write(data);
//            fileOutPutStream.flush();
//            fileOutPutStream.close();
//        } catch (Exception e) {
//            LogUtil.e(TAG, "Error in overwrite database", e);
//
//        }
        if(newDbFile.exists()){
            // 先关闭数据库链接解除文件占用
            close();
            // 旧数据库
            File oldDbFile = new File(DB_PATH + DB_NAME);
            try {
                // 覆盖数据库
                FileUtils.copyFile(newDbFile, oldDbFile);
            } catch (IOException e) {
                LogUtil.e(TAG, "Error in overwrite database", e);
            }
            // 重新连接数据库
            open(oldDbFile);
            // 删除旧数据的journal(事务)文件
            FileUtils.deleteFile(getDatabaseJournalFile());
            // 删除新数据的备份文件
            FileUtils.deleteFile(newDbFile);
            FileUtils.deleteFile(newDbFileJournal);
        }
    }

    /**
     * 打开数据库链接
     * 
     * @param 数据库文件
     */
    private synchronized void open(File dbFile) {
        db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    /**
     * 调用SQLiteDatabase对象的close()方法关闭数据库
     */
    public synchronized void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    /**
     * 执行sql
     * 
     * @param sql
     *            SQL语句
     * @param params
     *            参数
     */
    public synchronized void execSQL(String sql, Object... params) {
        if (db != null && db.isOpen()) {
            db.execSQL(sql, params);
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
        if (ConverterUtil.isNotEmpty(tableName) && db.isOpen()) {
            try {
                db.beginTransaction();
                for (T entity : entityList) {
                    ContentValues contentValues = ConverterUtil.getContentValues(entity);
                    // db.insert(table, null, contentValues);
                    // insert Or update 代替检测ID冲突
                    db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                LogUtil.e(TAG, "Error in insertBatch", e);
            } finally {
                db.endTransaction();
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
    public synchronized <T> boolean insertBatch(String tableName, List<T> entityList) {
        if (ConverterUtil.isNotEmpty(tableName) && db.isOpen()) {
            try {
                db.beginTransaction();
                int commit = 0;
                for (T entity : entityList) {
                    ContentValues contentValues = ConverterUtil.getContentValues(entity);
                    db.insert(tableName, null, contentValues);
                    if (commit % 500 == 0) {
                        db.setTransactionSuccessful();
                        db.endTransaction();
                        db.beginTransaction();
                    }
                    commit++;
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                LogUtil.e(TAG, "Error in insertBatch", e);
                return false;
            } finally {
                db.endTransaction();
            }
            return true;
        }
        return false;
    }

    /**
     * 批量插入
     * 
     * @param tableName
     *            表名
     * @param entityList
     *            实体list
     */
    public synchronized boolean insertBatch(Map<String, List<?>> tableNameAndList) {
        if (ConverterUtil.isNotEmpty(tableNameAndList) && db.isOpen()) {
            try {
                db.beginTransaction();
                int commit = 0;
                for (String key : tableNameAndList.keySet()) {
                    List<?> list = tableNameAndList.get(key);
                    for (Object entity : list) {
                        ContentValues contentValues = ConverterUtil.getContentValues(entity);
                        db.insertWithOnConflict(key, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                        commit++;
                        if (commit % 500 == 0) {
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.beginTransaction();
                        }
                    }
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                LogUtil.e(TAG, "Error in insertBatch", e);
                return false;
            } finally {
                db.endTransaction();
            }
            return true;
        }
        return false;
    }

    /**
     * 清空表
     * 
     * @param tableName
     *            表名
     */
    public synchronized <E> void truncate(String tableName) {
        if (ConverterUtil.isNotEmpty(tableName) && db.isOpen()) {
            try {
                db.beginTransaction();
                // 清空表数据
                execSQL("DELETE FROM " + tableName);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                LogUtil.e(TAG, "Error in truncate", e);
            } finally {
                db.endTransaction();
            }
        }
    }

    /**
     * 删除表数据
     * 
     * @param tableName
     * @param id
     * @param where
     */
    public synchronized void delete(String tableName, String where, String... params) {
        db.delete(tableName, where, params);
    }

    /**
     * 更新表数据
     * 
     * @param tableName
     * @param values
     *            ContentValues
     * @param where
     * @param params
     */
    public synchronized void update(String tableName, ContentValues values, String where, String... params) {
        db.update(tableName, values, where, params);
    }

    /**
     * 更新表数据
     * 
     * @param tableName
     * @param values
     *            ContentValues
     * @param where
     * @param params
     */
    public synchronized void update(String sql, String... params) {
        db.execSQL(sql, params);
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
            LogUtil.e(TAG, "Error in insert:Your " + entity.getClass() + " is list,this method needs Object(entity).");
            return;
        }
        if (ConverterUtil.isNotEmpty(tableName) && db.isOpen()) {
            try {
                ContentValues contentValues = ConverterUtil.getContentValues(entity);
                db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
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
     * 
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
    public synchronized int count(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        int result = 0;
        try {
            Cursor cursor = db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            result = cursor.getCount();
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Error in select", e);
            result = 0;
        }
        return result;
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
    public synchronized <T> List<T> select(String tableName, Class<T> clazz, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        List<T> result = null;
        try {
            Cursor cursor = db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            result = getResultForCursor(cursor, clazz);
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Error in select", e);
            result = null;
        }
        return result;
    }

    /**
     * 通过SQL进行查询
     * 
     * @param sql
     *            SQL语句
     * @param clazz
     *            返回值类型
     * @param params
     *            参数
     * @return
     */
    public synchronized <T> List<T> select(String sql, Class<T> clazz, String[] params) {
        List<T> result = null;
        try {
            Cursor cursor = db.rawQuery(sql, params);
            result = getResultForCursor(cursor, clazz);
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Error in select", e);
            result = null;
        }
        return result;
    }

    /**
     * 通过SQL进行查询
     * 
     * @param sql
     *            SQL语句
     * @param clazz
     *            返回值类型
     * @param params
     *            参数
     * @return
     */
    public synchronized <T> T selectOne(String sql, Class<T> clazz, String[] params) {
        T result = null;
        try {
            Cursor cursor = db.rawQuery(sql, params);
            List<T> list = getResultForCursor(cursor, clazz);
            if (ConverterUtil.isNotEmpty(list)) {
                result = list.get(0);
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

    /**
     * 解析返回的游标为结果集
     * 
     * @param cursor
     *            游标
     * @param clazz
     *            返回的结果集类型
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> getResultForCursor(Cursor cursor, Class<T> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<T> result = null;
        if (cursor != null) {
            if (clazz.equals(Map.class)) {
                // 返回List<Map>类型
                List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                while (cursor.moveToNext()) {
                    // 拼装一行结果
                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        // 判断类型
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
                // 返回List<Entity>类型
                result = new ArrayList<T>();
                // 取得entity对应的set方法
                Map<String, Method> setMethod = ConverterUtil.getAllMethods(clazz, ConverterUtil.METHOD_SET);
                while (cursor.moveToNext()) {
                    // 拼装一行结果
                    T row = clazz.newInstance();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        String colName = cursor.getColumnName(i);
                        Method rowSetMethod = setMethod.get(colName);
                        // 判断有无对应的set方法就跳过
                        if (null == rowSetMethod) {
                            // // 如果既没有set方法也不是子表就跳过
                            // if(colName.indexOf("_") == -1 || colName.startsWith("_") || colName.endsWith("_")){
                            // continue;
                            // }
                            // // 拆分取得子表信息
                            // String[] colNames = colName.split("_");
                            // try {
                            // Field subField = clazz.getField(colNames[0]);
                            // subField.getType().getTypeParameters();
                            // } catch (Exception e) {
                            // // 如果找不到子表属性则跳过
                            // continue;
                            // }
                            continue;
                        }
                        // 判断类型
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
        return result;
    }
}
