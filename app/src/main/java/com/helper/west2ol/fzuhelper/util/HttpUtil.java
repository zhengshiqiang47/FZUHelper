package com.helper.west2ol.fzuhelper.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.helper.west2ol.fzuhelper.bean.User;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/** Login
 * Http request
 * Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*
 *  /*;q=0.8
 * Accept-Encoding:gzip, deflate
 * Accept-Language:zh-CN,zh;q=0.8
 * Cache-Control:max-age=0
 * Connection:keep-alive
 * Content-Length:39
 * Content-Type:application/x-www-form-urlencoded
 * Cookie:ASPSESSIONIDAQCAAAQC=JCFOLEEBMHFIFIEDDLCNFAEA
 * Host:59.77.226.32
 * Origin:http://jwch.fzu.edu.cn
 * Referer:http://jwch.fzu.edu.cn/
 * Upgrade-Insecure-Requests:1
 * User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.22 Safari/537.36 SE 2.X MetaSr 1.0
 */

public class HttpUtil {
    private static final String TAG = "HttpUtil";
    public static final String LOGIN = "http://59.77.226.32/logincheck.asp";
    // parameter muser,passwwd,x=0,y=0
    public static final String LOGIN_CHK_XS = "http://59.77.226.35/loginchk_xs.aspx";
    // parameter id num       从logincheck.asp获取
    //返回 id

    /**
     * 获取资源
     */
    public static final String BASE = "http://59.77.226.35/";
    public static final String getCourse = "student/xkjg/wdxk/xkjg_list.aspx"; //课表
    public static final String getGrade = "student/jscp/TeaList.aspx"; //成绩
    public static final String getExamClassRoom = "student/jscp/TeaList.aspx"; //考场 同上

    public static final String getEmptyClassRoom = "kkgl/kbcx/kbcx_choose.aspx"; //空教室


    public static String Login(Context context,User user) throws Exception{
        Log.i(TAG, "user:" + user.getFzuAccount()+" pass:"+user.getFzuPasssword());
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new LoginInterceptor()).build();
        FormBody formBody=new FormBody.Builder().add("muser",user.getFzuAccount()).add("passwd",user.getFzuPasssword()).build();
        Request request=new Request.Builder()
                .url("http://59.77.226.32/logincheck.asp")
                .method("Post",null)
                .post(formBody)
                .addHeader("Referer","http://jwch.fzu.edu.cn/")
                .addHeader("Connection","keep-alive")
                .build();

        Response response=okHttpClient.newCall(request).execute();
        if(!response.message().equals("OK")){
            Log.i(TAG,"网络出错");
            return "网络出错";
        }
        String result = new String(response.body().bytes());
        if (result.contains("charset=gb2312")){
            result=new String(result.getBytes(),"gb2312");
        }
        Log.i(TAG, "Login: "+result);
        Log.i(TAG, result);
        if(result.contains("密码错误，请重新登录，或与学院教学办联系！")||result.contains("用户名错误，请确认是否输入错误，用户名前请不要加字母！！")){
            Log.i(TAG,"密码错误");
            return "密码错误";
        }
        if (result.contains("left.aspx")){
            Log.i(TAG, "登录成功");
            return "登录成功";
        }
        return "登录失败，请检查用户名和密码是否正确!";
    }
    /**
     * http://59.77.226.35/student/xkjg/wdxk/xkjg_list.aspx
     * @param targetUrl
     * @return
     */
    public static String getCookieHtml(String targetUrl){
        String html=null;
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        Log.i(TAG, "id:" + FzuCookie.get().getId());
        Request request=new Request.Builder()
                .url(targetUrl+"?id="+ FzuCookie.get().getId())
                .addHeader("Cookie", FzuCookie.get().getCookie()+"")
                .addHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6")
                .addHeader("Connection","keep-alive")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result=new String(response.body().bytes());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    public static String getHistoryCourseHtml(String targetUrl,String xuenian){
        String html=null;
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        Log.i(TAG, "id:" + FzuCookie.get().getId());
        RequestBody requestBody=new FormBody.Builder()
                .add("ctl00$ContentPlaceHolder1$DDL_xnxq",xuenian)
                .add("ctl00$ContentPlaceHolder1$zylbdpl","本专业")
                .add("ctl00$ContentPlaceHolder1$BT_submit","确定")
                .add("__EVENTVALIDATION", FzuCookie.get().EVENTVALIDATION)
                .add("__VIEWSTATE", FzuCookie.get().VIEWSTATE).build();
        Request request=new Request.Builder()
                .url(targetUrl+"?id="+ FzuCookie.get().getId())
                .addHeader("Cookie", FzuCookie.get().getCookie()+"")
                .addHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6")
                .addHeader("Connection","keep-alive")
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(!response.message().equals("OK")){
                Log.i(TAG,"获取课表失败，message不是Ok");
                return null;
            }
            String result=new String(response.body().bytes());
//            Log.i(TAG, "result" + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }


    public static String getHtmlByParam(String url,Map<String,String> params){
        String html=null;
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
//        Log.i(TAG, "id:" + FzuCookie.get().getId());
        FormBody.Builder formBodyBuilder=new FormBody.Builder();
        Iterator<Map.Entry<String,String>> iterator=params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry=iterator.next();
            System.out.println("isNull:"+(entry==null)+" key:"+entry.getKey()+" value:"+entry.getValue());
            formBodyBuilder.add(entry.getKey(), (String) entry.getValue());
        }
        RequestBody requestBody=formBodyBuilder.build();
        Request request=new Request.Builder()
                .url(url)
                .addHeader("Cookie", FzuCookie.get().getCookie()+"")
                .addHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6")
                .addHeader("Connection","keep-alive")
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(!response.message().equals("OK")){
                Log.i(TAG,"获取数据，message不是Ok"+response.message());
                Log.i(TAG, new String(response.body().bytes(), "gb2312"));
                return null;
            }
            String result=new String(response.body().bytes(),"gb2312");
//            Log.i(TAG, "getParams");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    public static String getEmptyByParam(String url,Map<String,String> params){
        String html=null;
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
//        Log.i(TAG, "id:" + FzuCookie.get().getId());
        FormBody.Builder formBodyBuilder=new FormBody.Builder();
        Iterator<Map.Entry<String,String>> iterator=params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry=iterator.next();
            System.out.println("isNull:"+(entry==null)+" key:"+entry.getKey()+" value:"+entry.getValue());
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody=formBodyBuilder.build();
        Request request=new Request.Builder()
                .url(url)
                .addHeader("Cookie", FzuCookie.get().getCookie()+"")
                .addHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6")
                .addHeader("Connection","keep-alive")
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result=new String(response.body().bytes());
//            Log.i(TAG, "getParams");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    public static String getExamByParam(String url,Map<String,String> params){
        String html=null;
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
//        Log.i(TAG, "id:" + FzuCookie.get().getId());
        FormBody.Builder formBodyBuilder=new FormBody.Builder();
        Iterator<Map.Entry<String,String>> iterator=params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry=iterator.next();
            System.out.println("isNull:"+(entry==null)+" key:"+entry.getKey()+" value:"+entry.getValue());
            formBodyBuilder.add(entry.getKey(), (String) entry.getValue());
        }
        RequestBody requestBody=formBodyBuilder.build();
        Request request=new Request.Builder()
                .url(url+"?id="+FzuCookie.get().getId())
                .addHeader("Cookie", FzuCookie.get().getCookie()+"")
                .addHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6")
                .addHeader("Connection","keep-alive")
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result=new String(response.body().bytes());
//            Log.i(TAG, "getParams");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }


    public static String getHtml(String url) throws Exception{
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder().url(url)
                .addHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6")
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;charset=utf-8;q=0.9,image/webp,*/*;q=0.8")
                .addHeader("Accept-Encoding","gzip, deflate, sdch").build();
        Response response=client.newCall(request).execute();
        if (response.isSuccessful()) {
            String result = new String(response.body().bytes(),"gb2312");
            return result;
        }
        return null;
    }

    public static Version getVeison() throws Exception{
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder().url("http://url.w2fzu.com/version.json")
                .addHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6")
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;charset=utf-8;q=0.9,image/webp,*/*;q=0.8")
                .addHeader("Accept-Encoding","gzip, deflate, sdch").build();
        Response response=client.newCall(request).execute();
        if (response.isSuccessful()) {
            String result = new String(response.body().bytes());
            Version version = new Gson().fromJson(result, Version.class);
            return version;
        }
        return null;
    }

    public static class Version{

        /**
         * version : 1.0
         * url : http://url.w2fzu.com/fzuHelper_beta.apk
         */

        private String version;
        private String url;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
