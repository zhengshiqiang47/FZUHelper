package com.helper.west2ol.fzuhelper.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CoderQiang on 2016/10/24.
 *
 *
 * OKHttp的网络拦截器，用于获取Cookie和id
 */

public class LoginInterceptor implements Interceptor {
    private static final String TAG="LoginInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        Response response=null;
        try{
            response=chain.proceed(request);
        }catch (Exception e){
            int tryCount=0;
            while ((response==null||!response.isSuccessful())&&tryCount<10){
                Log.i(TAG,"重试请求");
                response=chain.proceed(request);
                tryCount++;
            }
        }
        String cookie=response.header("Set-Cookie");
        if (cookie != null) {
            FzuCookie.get().setCookie(cookie);
            FzuCookie.get().setLastUpdateTime(System.currentTimeMillis());
            FzuCookie.get().setExpTime(System.currentTimeMillis()+5*60*1000l);
        }
        String idStr=response.header("Location");
        if (idStr != null) {
            int i=0;
            int length=0;
            String temp;
            while(i<idStr.length()){
                temp=idStr.substring(i,i+2);
                if(temp.equals("id")){
                    length=i;
                    break;
                }
                i++;
            }
            FzuCookie.get().setId(idStr.substring(length+3));
        }

        return response;
    }
}
