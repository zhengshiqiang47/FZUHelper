package com.helper.west2ol.fzuhelper.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.FDScore;
import com.helper.west2ol.fzuhelper.bean.User;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by CoderQiang on 2017/9/13.
 */

public class DBManager {
    private final static String dbName = "fzu_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    //用户
    public void insertUser(User user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insert(user);
    }

    public void deleteUser(User user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.delete(user);
    }

    public void updateUser(User user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.update(user);
    }

    public User queryUser(String userAccount){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        List<User> users=userDao.queryRaw("userAccount=?",userAccount);
        System.out.println("Usersize:"+users.size());
        if (users != null && users.size() >= 1) {
            return users.get(0);
        }
        return null;
    }

    public List<User> queryUserList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        List<User> list = qb.list();
        return list;
    }


    //课表

    public void dropCourseBeans(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CourseBeanDao CourseBeanDao = daoSession.getCourseBeanDao();
        CourseBeanDao.deleteAll();
    }

    public void insertCourseBeans(List<CourseBean> CourseBeans) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CourseBeanDao CourseBeanDao = daoSession.getCourseBeanDao();

        for (CourseBean course:CourseBeans){
            CourseBeanDao.insert(course);
        }
    }

    public void insertCourseBean(CourseBean CourseBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CourseBeanDao CourseBeanDao = daoSession.getCourseBeanDao();
        CourseBeanDao.insert(CourseBean);
    }

    public void deleteCourseBean(CourseBean CourseBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CourseBeanDao CourseBeanDao = daoSession.getCourseBeanDao();
        CourseBeanDao.delete(CourseBean);
    }

    public void updateCourseBean(CourseBean CourseBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CourseBeanDao CourseBeanDao = daoSession.getCourseBeanDao();
        CourseBeanDao.update(CourseBean);
    }

    public List<CourseBean> queryCourseBeanList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CourseBeanDao CourseBeanDao = daoSession.getCourseBeanDao();
        QueryBuilder<CourseBean> qb = CourseBeanDao.queryBuilder();
        List<CourseBean> list = qb.list();
        return list;
    }



    //成绩

    public void insertFDScores(List<FDScore> FDScores) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FDScoreDao FDScoreDao = daoSession.getFDScoreDao();
        for (FDScore fdScore:FDScores){
            FDScoreDao.insert(fdScore);
        }
    }
    
    public void insertFDScore(FDScore FDScore) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FDScoreDao FDScoreDao = daoSession.getFDScoreDao();
        FDScoreDao.insert(FDScore);
    }

    public void deleteFDScore(FDScore FDScore) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FDScoreDao FDScoreDao = daoSession.getFDScoreDao();
        FDScoreDao.delete(FDScore);
    }

    public void updateFDScore(FDScore FDScore) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FDScoreDao FDScoreDao = daoSession.getFDScoreDao();
        FDScoreDao.update(FDScore);
    }

    public List<FDScore> queryFDScoreList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FDScoreDao FDScoreDao = daoSession.getFDScoreDao();
        QueryBuilder<FDScore> qb = FDScoreDao.queryBuilder();
        List<FDScore> list = qb.list();
        return list;
    }

    public void dropFDScores(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FDScoreDao FDScoreDao = daoSession.getFDScoreDao();
        FDScoreDao.deleteAll();
    }



}
