package com.test.yanxiu.im_ui.constacts.DatabaseFramework.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author by frc on 2018/3/7.
 */

public class BaseDaoFactory {
    private static BaseDaoFactory outInstance;

    public static BaseDaoFactory getOutInstance(String dbPath) {
        if (outInstance == null) {
            outInstance=new BaseDaoFactory(dbPath);
        }
        return outInstance;
    }

    private SQLiteDatabase sqLiteDatabase;
    //定义建数据库的路径
    // 建议大家写到SD卡中  好处是APP被删除了，下次再安装的时候，数据还在

    private String sqliteDatabasePath;

    //设计一个数据库的连接池
//    protected Map<String, BaseDao> map = Collections.synchronizedMap(new HashMap<String, BaseDao>());


    private BaseDaoFactory(String dbPath) {
        //todo 可以判断是否有sd卡  默认存储路径为：/data/data/(packageName)/databases/dbName
        try {
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        } catch (Exception e) {
            Log.e("frc", e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * @param daoClass
     * @param entityClass
     * @param <T>         BaseDao
     * @param <M>         User
     * @return
     */
    public synchronized <T extends BaseDao<M>, M> T getBaseDao(Class<T> daoClass, Class<M> entityClass) {
        BaseDao<M> baseDao = null;
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(sqLiteDatabase, entityClass);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }

}
