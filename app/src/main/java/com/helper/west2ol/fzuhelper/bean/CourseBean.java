package com.helper.west2ol.fzuhelper.bean;

/**
 * Created by Administrator on 2016/10/30.
 */

public class CourseBean {
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
}
