package com.helper.west2ol.fzuhelper.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.helper.west2ol.fzuhelper.bean.CourseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/30.
 * 以coursetable为例，编写了其增删改查
 * 在loginactivity登陆时调用create,并删除原有表
 */
public class SQLiteUtil {

    public static SQLiteDatabase createDataBases(SQLiteDatabase db){
        db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.helper.west2ol/databases/courseTable.db" , null);
        return db;
    }

    public  static void createCourseTable(SQLiteDatabase db){
        String CREATE_COURSETABLE = "create table coursetable("
                + "name VARCHAR(50) ,"
                + "lacation VARCHAR(50) ,"
                + "starttime  VARCHAR(50) ,"
                + "endtime  VARCHAR(50) ,"
                + "startweek  VARCHAR(50) ,"
                + "endweek  VARCHAR(50) ,"
                + "isdouble  VARCHAR(50) ,"
                + "weekend  VARCHAR(50) ,"
                + "issingle  VARCHAR(50) ,"
                + "year  VARCHAR(50) ,"
                + "xuenian  VARCHAR(50) )";
        db.execSQL(CREATE_COURSETABLE);
    }

    public static void insertCourseTable(CourseBean courseBean , SQLiteDatabase db){
        String kcName = courseBean.getKcName();
        String kcLocation = courseBean.getKcLocation();
        int kcStartTime = courseBean.getKcStartTime();
        int kcEndTime = courseBean.getKcEndTime();
        int kcStartWeek = courseBean.getKcStartWeek();
        int kcEndWeek = courseBean.getKcEndWeek();
        boolean kcIsDouble = courseBean.isKcIsDouble();
        int kcWeekend = courseBean.getKcWeekend();
        boolean kcIsSingle = courseBean.isKcIsSingle();
        int kcYear = courseBean.getKcYear();
        int kcXuenian = courseBean.getKcXuenian();
        ContentValues values = new ContentValues();
        values.put("name" , kcName);
        values.put("lacation" , kcLocation);
        values.put("starttime" , kcStartTime+"");
        values.put("endtime" , kcEndTime+"");
        values.put("startweek" , kcStartWeek+"");
        values.put("endweek" , kcEndWeek+"");
        values.put("isdouble" , kcIsDouble+"");
        values.put("weekend" , kcWeekend+"");
        values.put("issingle" , kcIsSingle+"");
        values.put("year" , kcYear+"");
        values.put("xuenian" , kcXuenian+"");
        db.insert("coursetable",null,values);
        values.clear();
    }
    public static List<CourseBean> readCourseTable(SQLiteDatabase db){
        Cursor curser =  db.query("coursetable", null , null , null , null ,null , null , null);
        int sum = curser.getCount();
        List<CourseBean> courseList = new ArrayList<CourseBean>();
        if(curser.moveToFirst()){
            for(int i = 0 ; i < sum ; i++){
                CourseBean courseBean = new CourseBean();
                curser.move(i);
                courseBean.setKcName(curser.getString(0));
                courseBean.setKcLocation(curser.getString(1));
                courseBean.setKcStartTime(Integer.valueOf(curser.getString(2)));
                courseBean.setKcEndTime(Integer.valueOf(curser.getString(3)));
                courseBean.setKcStartWeek(Integer.valueOf(curser.getString(4)));
                courseBean.setKcEndWeek(Integer.valueOf(curser.getString(5)));
                courseBean.setKcIsDouble(Boolean.valueOf(curser.getString(6)));
                courseBean.setKcWeekend(Integer.valueOf(curser.getString(7)));
                courseBean.setKcIsSingle(Boolean.valueOf(curser.getString(8)));
                courseBean.setKcYear(Integer.valueOf(curser.getString(9)));
                courseBean.setKcXuenian(Integer.valueOf(curser.getString(10)));
                courseList.add(courseBean);
            }
        }
        return courseList;
    }
    public static void deleteCourseTable(SQLiteDatabase db){
        db.execSQL("drop table if exists coursetable");
    }
}
