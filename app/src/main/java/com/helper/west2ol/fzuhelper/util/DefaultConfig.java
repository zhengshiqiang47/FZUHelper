package com.helper.west2ol.fzuhelper.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CoderQiang on 2017/9/14.
 */

public class DefaultConfig {

    public static DefaultConfig defaultConfig=null;

    private  int nowWeek=0;
    private  int curYear = 0;
    private  int curXuenian=0;

    private long beginDate=0;
    private Map<String,String> xqValues;

    private DefaultConfig() {

    }

    public static DefaultConfig get(){
        if (defaultConfig == null) {
            defaultConfig = new DefaultConfig();
        }
        return defaultConfig;
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public Map<String, String> getXqValues() {
        if (xqValues == null) {
            xqValues = new HashMap<>();
        }
        return xqValues;
    }

    public void setXqValues(Map<String, String> xqValues) {
        this.xqValues = xqValues;
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
