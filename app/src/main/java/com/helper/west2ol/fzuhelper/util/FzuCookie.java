package com.helper.west2ol.fzuhelper.util;

/**
 * Created by CoderQiang on 2016/10/24.
 *
 * 在没有存入文件之前，用这个类的单例保存cookie
 */

public class FzuCookie {
    private static FzuCookie fzuCookie;
    String cookie;
    String id;

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
}
