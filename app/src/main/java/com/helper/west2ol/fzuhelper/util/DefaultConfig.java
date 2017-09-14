package com.helper.west2ol.fzuhelper.util;

/**
 * Created by CoderQiang on 2017/9/14.
 */

public class DefaultConfig {

    public static DefaultConfig defaultConfig=null;

    private  int nowWeek=0;
    private  int curYear = 0;
    private  int curXuenian=0;

    private DefaultConfig() {

    }

    public static DefaultConfig get(){
        if (defaultConfig == null) {
            defaultConfig = new DefaultConfig();
        }
        return defaultConfig;
    }

    public  int getNowWeek() {
        return nowWeek;
    }

    public  void setNowWeek(int nowWeek) {
        this.nowWeek = nowWeek;
    }

    public int getCurXuenian() {
        return curXuenian;
    }

    public void setCurXuenian(int curXuenian) {
        this.curXuenian = curXuenian;
    }

    public int getCurYear() {
        return curYear;
    }

    public void setCurYear(int curYear) {
        this.curYear = curYear;
    }
}
