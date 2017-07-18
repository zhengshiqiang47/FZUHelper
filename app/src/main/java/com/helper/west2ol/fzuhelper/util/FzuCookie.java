package com.helper.west2ol.fzuhelper.util;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/10/24.
 *
 * 在没有存入文件之前，用这个类的单例保存cookie
 */

public class FzuCookie {
    private static FzuCookie fzuCookie;
    String cookie;
    String id;
    String VIEWSTATE;
    String EVENTVALIDATION;
    long lastUpdateTime;

    ArrayList<String> options = new ArrayList<>();

    private FzuCookie() {

    }

    public static FzuCookie get(){
        if (fzuCookie == null) {
            fzuCookie=new FzuCookie();
        }
        return fzuCookie;
    }

    public void setCookie(String cookieStr) {
        cookie=cookieStr;
    }

    public String getCookie() {
       return cookie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVIEWSTATE() {
        return VIEWSTATE;
    }

    public void setVIEWSTATE(String VIEWSTATE) {
        this.VIEWSTATE = VIEWSTATE;
    }

    public String getEVENTVALIDATION() {
        return EVENTVALIDATION;
    }

    public void setEVENTVALIDATION(String EVENTVALIDATION) {
        this.EVENTVALIDATION = EVENTVALIDATION;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
