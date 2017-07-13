package com.helper.west2ol.fzuhelper.bean;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/5.
 */

public class CourseBeanLab {
    private static CourseBeanLab courseBeanLab;
    private ArrayList<CourseBean> courses;
    private Context mContext;
    private boolean isParse;

    private CourseBeanLab(Context context){
        mContext=context;
        courses=new ArrayList<CourseBean>();
    }

    public static CourseBeanLab get(Context context){
        if (courseBeanLab == null) {
            courseBeanLab = new CourseBeanLab(context);
        }
        return courseBeanLab;
    }

    public void setCourses(ArrayList<CourseBean> courses){
        this.courses=courses;
    }

    public ArrayList<CourseBean> getCourses(){
        return courses;
    }

    public boolean isParse() {
        return isParse;
    }

    public void setParse(boolean parse) {
        isParse = parse;
    }
}
