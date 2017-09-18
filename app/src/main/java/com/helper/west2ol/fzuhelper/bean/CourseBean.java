package com.helper.west2ol.fzuhelper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/10/30.
 */

@Entity
public class CourseBean {

    private static final String JSON_XUENIAN = "FDScoreXuenian";

    @Id(autoincrement = true)
    private Long courseId;

    private String kcName;
    private String kcLocation;
    private int kcStartTime;
    private int kcEndTime;
    private int kcStartWeek;
    private int kcEndWeek;
    private boolean kcIsDouble=true;
    private boolean kcIsSingle=true;
    private int kcWeekend;
    private int kcYear;
    private int kcXuenian;
    private String kcNote;
    private int kcBackgroundId;


    @Unique()
    private String unique;

    @Generated(hash = 497578870)
    public CourseBean(Long courseId, String kcName, String kcLocation, int kcStartTime, int kcEndTime,
            int kcStartWeek, int kcEndWeek, boolean kcIsDouble, boolean kcIsSingle, int kcWeekend,
            int kcYear, int kcXuenian, String kcNote, int kcBackgroundId, String unique) {
        this.courseId = courseId;
        this.kcName = kcName;
        this.kcLocation = kcLocation;
        this.kcStartTime = kcStartTime;
        this.kcEndTime = kcEndTime;
        this.kcStartWeek = kcStartWeek;
        this.kcEndWeek = kcEndWeek;
        this.kcIsDouble = kcIsDouble;
        this.kcIsSingle = kcIsSingle;
        this.kcWeekend = kcWeekend;
        this.kcYear = kcYear;
        this.kcXuenian = kcXuenian;
        this.kcNote = kcNote;
        this.kcBackgroundId = kcBackgroundId;
        this.unique = unique;
    }

    @Generated(hash = 858107730)
    public CourseBean() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public String getKcNote() {
        return kcNote;
    }

    public void setKcNote(String kcNote) {
        this.kcNote = kcNote;
    }

    public void setKcName(String kcName){
        this.kcName = kcName;
    }
    public String getKcName(){
        return kcName;
    }

    public void setKcLocation(String kcLocation){
        this.kcLocation = kcLocation;
    }
    public String getKcLocation(){
        return kcLocation;
    }

    public int getKcStartTime() {
        return kcStartTime;
    }

    public void setKcStartTime(int kcStartTime) {
        this.kcStartTime = kcStartTime;
    }

    public int getKcEndTime() {
        return kcEndTime;
    }

    public void setKcEndTime(int kcEndTime) {
        this.kcEndTime = kcEndTime;
    }

    public int getKcStartWeek() {
        return kcStartWeek;
    }

    public void setKcStartWeek(int kcStartWeek) {
        this.kcStartWeek = kcStartWeek;
    }

    public int getKcEndWeek() {
        return kcEndWeek;
    }

    public void setKcEndWeek(int kcEndWeek) {
        this.kcEndWeek = kcEndWeek;
    }

    public boolean isKcIsDouble() {
        return kcIsDouble;
    }

    public void setKcIsDouble(boolean kcIsDouble) {
        this.kcIsDouble = kcIsDouble;
    }

    public int getKcWeekend() {
        return kcWeekend;
    }

    public void setKcWeekend(int kcWeekend) {
        this.kcWeekend = kcWeekend;
    }

    public boolean isKcIsSingle() {
        return kcIsSingle;
    }

    public void setKcIsSingle(boolean kcIsSingle) {
        this.kcIsSingle = kcIsSingle;
    }

    public int getKcYear() {
        return kcYear;
    }

    public void setKcYear(int kcYear) {
        this.kcYear = kcYear;
    }

    public int getKcXuenian() {
        return kcXuenian;
    }

    public void setKcXuenian(int kcXuenian) {
        this.kcXuenian = kcXuenian;
    }

    public int getKcBackgroundId() {
        return kcBackgroundId;
    }

    public void setKcBackgroundId(int kcBackgroundId) {
        this.kcBackgroundId = kcBackgroundId;
    }

    @Override
    public boolean equals(Object obj) {
        CourseBean courseBean= (CourseBean) obj;
        if (courseBean.getKcYear()==this.getKcYear()&&courseBean.getKcXuenian()==this.getKcXuenian())
            return true;
        else
            return false;
    }

    public boolean getKcIsDouble() {
        return this.kcIsDouble;
    }

    public boolean getKcIsSingle() {
        return this.kcIsSingle;
    }
}
