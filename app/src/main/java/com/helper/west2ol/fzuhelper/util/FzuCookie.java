package com.helper.west2ol.fzuhelper.util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/10/24.
 *
 * 在没有存入文件之前，用这个类的单例保存cookie
 */

public class FzuCookie implements Serializable{
    private static FzuCookie fzuCookie;

    long expTime;
    String cookie;
    String id;
    String VIEWSTATE;
    String EVENTVALIDATION;
    long lastUpdateTime;
    String SCORE_VIEWSTATE;
    String SCORE_EVENTVALIDATION;


    private FzuCookie() {

    }

    private FzuCookie(FzuCookie cookie) {

        this.expTime=cookie.getExpTime();
        this.cookie = cookie.getCookie();
        this.id=cookie.getId();
        this.VIEWSTATE=cookie.getVIEWSTATE();
        this.EVENTVALIDATION=cookie.getEVENTVALIDATION();
        this.lastUpdateTime=cookie.getLastUpdateTime();
    }

    public static FzuCookie get(){
        if (fzuCookie == null) {
            fzuCookie=new FzuCookie();
        }
        return fzuCookie;
    }

    public static FzuCookie get(FzuCookie cookie){
        if (fzuCookie == null) {
            fzuCookie=new FzuCookie(cookie);
        }
        return fzuCookie;
    }

    public String getSCORE_VIEWSTATE() {
        return SCORE_VIEWSTATE;
    }

    public void setSCORE_VIEWSTATE(String SCORE_VIEWSTATE) {
        this.SCORE_VIEWSTATE = SCORE_VIEWSTATE;
    }

    public String getSCORE_EVENTVALIDATION() {
        return SCORE_EVENTVALIDATION;
    }

    public void setSCORE_EVENTVALIDATION(String SCORE_EVENTVALIDATION) {
        this.SCORE_EVENTVALIDATION = SCORE_EVENTVALIDATION;
    }

    public long getExpTime() {
        return expTime;
    }

    public void setExpTime(long expTime) {
        this.expTime = expTime;
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

}
