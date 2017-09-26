package com.helper.west2ol.fzuhelper.util;

import com.helper.west2ol.fzuhelper.bean.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CoderQiang on 2017/9/14.
 */

public class DefaultConfig implements Serializable {

    public static DefaultConfig defaultConfig=null;

    private  int nowWeek=0;
    private  int curYear = 0;
    private  int curXuenian=0;

    private long beginDate=0;
    private Map<String,String> xqValues;
    private ArrayList<String> options;

    private String userAccount;
    private String userName;

    private boolean isLogin;

    private DefaultConfig() {

    }

    public static DefaultConfig get(){
        if (defaultConfig == null) {
            defaultConfig = new DefaultConfig();
        }
        return defaultConfig;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
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

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
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

    @Override
    public String toString() {
        return "DefaultConfig{" +
                "nowWeek=" + nowWeek +
                ", curYear=" + curYear +
                ", curXuenian=" + curXuenian +
                ", beginDate=" + beginDate +
                ", xqValues=" + xqValues +
                ", user=" + userAccount +
                ", isLogin=" + isLogin +
                '}';
    }
}
