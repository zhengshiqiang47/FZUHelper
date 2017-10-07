package com.helper.west2ol.fzuhelper.util;

import android.content.Context;
import android.util.Log;


import com.google.gson.Gson;
import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.CourseBeanLab;
import com.helper.west2ol.fzuhelper.bean.Exam;
import com.helper.west2ol.fzuhelper.bean.FDScore;
import com.helper.west2ol.fzuhelper.bean.FDScoreLB;
import com.helper.west2ol.fzuhelper.bean.User;
import com.helper.west2ol.fzuhelper.bean.Yiban;
import com.helper.west2ol.fzuhelper.dao.DBManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/22.
 */

public class HtmlParseUtil {
    private static final String TAG = "HtmlParseUtil";

    /**
     * 获取当前学期课程表
     * @param context
     * @return
     */
    public static boolean getCurrentCourse(Context context) {
        DBManager dbManager=new DBManager(context);
        ArrayList<CourseBean> tempKcs = new ArrayList<>();
//        ArrayList<CourseBean> kcs = CourseBeanLab.get(context).getCourses();
//        if(kcs.size()>=2&&!isRefresh){
//            Log.i(TAG, "已经解析过");
//            return true;
//        }
        String result = HttpUtil.getCookieHtml("http://59.77.226.35/student/xkjg/wdxk/xkjg_list.aspx");
//        Log.i(TAG, result);
        Document document = Jsoup.parse(result);
        Log.i(TAG,"解析课表");

        //设置常用参数
        Element VIEWSTATE=document.select("input[id=__VIEWSTATE]").get(0);
        Element EVENTVALIDATION=document.select("input[id=__EVENTVALIDATION]").get(0);
        Elements options=document.select("option");
        ArrayList<String> optionStr = new ArrayList<>();
        for (Element element:options){
            optionStr.add(element.attr("value"));
            Log.i(TAG,"添加历史学期"+element.attr("value"));
        }
        DefaultConfig.get().setOptions(optionStr);
        FzuCookie.get().setVIEWSTATE(VIEWSTATE.attr("value"));
        FzuCookie.get().setEVENTVALIDATION(EVENTVALIDATION.attr("value"));
        FzuCookie.get().setExpTime(System.currentTimeMillis()+10*60*1000l);

        //开始解析课表
        Elements courseEles = document.select("tr[onmouseover=c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa']");
        for (int i = 0; i < courseEles.size(); i++) {
            Element kb = courseEles.get(i);
            Element titleEle = kb.select("td").get(1);
            Log.e(TAG,"titile:"+titleEle.text());
            String title = titleEle.text();
            //解析学年
            String yearStr = "2017";
            String xuenianStr = "01";
            int year = Integer.parseInt(yearStr);
            int xuenian = Integer.parseInt(xuenianStr);

            //解析课程备注:
            Element noteEle=kb.select("td").get(11);
            String note=noteEle.text();

            //解析课程信息
            String jiaoxueDagang=kb.select("td").get(2).select("a").get(0).attr("href").replace("javascript:pop1('","http://59.77.226.35").replace("');","").split("&")[0];
            String shoukeJihua=kb.select("td").get(2).select("a").get(1).attr("href").replace("javascript:pop1('","http://59.77.226.35").replace("');","").split("&")[0];
            String teacher=kb.select("td").get(7).text();

            Log.i(TAG, "大纲:" + jiaoxueDagang + " " + shoukeJihua + " " + teacher);
            Element timeEle = kb.select("td").get(8);

            String timeCou = timeEle.text();
            String[] strings = timeCou.split(" ");
            for (int j = 0; j < strings.length; j++) {
                CourseBean kc = new CourseBean();
                if (note.length()>=1){
                    kc.setKcNote(note);
                }

                kc.setTeacher(teacher);
                kc.setJiaoxueDagang(jiaoxueDagang);
                kc.setShoukeJihua(shoukeJihua);
                kc.setKcBackgroundId(i);
                kc.setKcYear(year);
                kc.setKcXuenian(xuenian);
                kc.setKcName(title);
                try {
                    String[] contents = strings[j].split(" ");
                    String[] week = contents[0].split("-");
                    int startWeek = Integer.parseInt(week[0]);
                    int endWeek = Integer.parseInt(week[1]);
                    kc.setKcStartWeek(startWeek);
                    kc.setKcEndWeek(endWeek);
//                    Log.i(TAG, "startweek" + startWeek + "  endweek" + endWeek);
                    int weekend = Integer.parseInt(contents[1].substring(2, 3));
                    kc.setKcWeekend(weekend);

                    if (contents[1].contains("单")) {
                        String timeStr = contents[1].substring(4, contents[1].length() - 4);
                        String[] time = timeStr.split("-");
                        int startTime = Integer.parseInt(time[0]);
                        int endTime = Integer.parseInt(time[1]);
                        kc.setKcStartTime(startTime);
                        kc.setKcEndTime(endTime);
                        kc.setKcIsDouble(false);

                    } else if (contents[1].contains("双")) {
                        String timeStr = contents[1].substring(4, contents[1].length() - 4);
                        String[] time = timeStr.split("-");
                        int startTime = Integer.parseInt(time[0]);
                        int endTime = Integer.parseInt(time[1]);
                        kc.setKcStartTime(startTime);
                        kc.setKcEndTime(endTime);
                        kc.setKcIsSingle(false);
                    } else {
                        String timeStr = contents[1].substring(4, contents[1].length() - 1);
                        String[] time = timeStr.split("-");
                        int startTime = Integer.parseInt(time[0]);
                        int endTime = Integer.parseInt(time[1]);
                        kc.setKcStartTime(startTime);
                        kc.setKcEndTime(endTime);
                    }

                    String location = contents[2];
                    kc.setKcLocation(location);
//                    String[] time=contents[1];
                    tempKcs.add(kc);
                } catch (Exception e) {
                    Log.i(TAG, "解析出错:"+title);
                }
            }
        }
//        List<CourseBean> courseBeans= CourseBeanLab.get(context).getCourses();
//        for (int i=0;i<courseBeans.size();i++){
//            courseBeans.remove(tempKcs.get(0));
//        }
        CourseBeanLab.get(context).getCourses().clear();
        CourseBeanLab.get(context).getCourses().addAll(tempKcs);
        Log.i(TAG,"共"+courseEles.size()+"个"+" 解析后:"+tempKcs.size()+"个");
        dbManager.dropCourseBeans();
        dbManager.insertCourseBeans(tempKcs);
        return true;
    }

    public static ArrayList<CourseBean> getHistoryCourse(Context context, String xueNian){//学年格式 201702
        ArrayList<CourseBean> tempCourses = new ArrayList<>();
        if (FzuCookie.get().getEVENTVALIDATION()==null||FzuCookie.get().getExpTime()<=System.currentTimeMillis()){
            HttpUtil.Login(context,DBManager.getInstance(context).queryUser(DefaultConfig.get().getUserAccount()));
            getCurrentCourse(context);
        }
        String VIEWSTATE= FzuCookie.get().getVIEWSTATE();
        String EVENTVALIDATION= FzuCookie.get().getEVENTVALIDATION();
        Log.i(TAG,"VIEWSTATE:"+VIEWSTATE);
        Log.i(TAG,"EVENTVALIDATION:"+EVENTVALIDATION);
        //解析学年
        String yearStr = xueNian.substring(0,4);
        String xuenianStr = xueNian.substring(4,6);
        int year = Integer.parseInt(yearStr);
        int xuenian = Integer.parseInt(xuenianStr);

        ArrayList<CourseBean> courseBeen= CourseBeanLab.get(context).getCourses();
        CourseBean courseBean=new CourseBean();
        courseBean.setKcXuenian(xuenian);
        courseBean.setKcYear(year);
        if (courseBeen.contains(courseBean)){
            System.out.println("已解析过");
            return courseBeen;
        }

        String result = HttpUtil.getHistoryCourseHtml("http://59.77.226.35/student/xkjg/wdxk/xkjg_list.aspx",xueNian);
        Log.i(TAG, "Result:"+result);
        Document document = Jsoup.parse(result);

        //开始解析课表
        Elements courseEles = document.select("tr[onmouseover=c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa']");
        for (int i = 0; i < courseEles.size(); i++) {
            Element kb = courseEles.get(i);
            Element titleEle = kb.select("td").get(1);
            Log.e(TAG,"titile:"+titleEle.text());
            String title = titleEle.text();


            String jiaoxueDagang=kb.select("td").get(2).select("a").get(0).attr("href").replace("javascript:pop1('","http://59.77.226.35").replace("');","").split("&")[0];
            String shoukeJihua=kb.select("td").get(2).select("a").get(1).attr("href").replace("javascript:pop1('","http://59.77.226.35").replace("');","").split("&")[0];
            String teacher=kb.select("td").get(7).text();

            //解析课程备注:
            Element noteEle=kb.select("td").get(11);
            String note=noteEle.text();

            //解析上课时间
            Element timeEle = kb.select("td").get(8);

            String timeCou = timeEle.text();
            String[] strings = timeCou.split(" ");
            for (int j = 0; j < strings.length; j++) {
                CourseBean kc = new CourseBean();
                if (note.length()>=1){
                    kc.setKcNote(note);
                }

                kc.setTeacher(teacher);
                kc.setJiaoxueDagang(jiaoxueDagang);
                kc.setShoukeJihua(shoukeJihua);
                kc.setKcBackgroundId(i);
                kc.setKcYear(year);
                kc.setKcXuenian(xuenian);
                kc.setKcName(title);
                try {
                    String[] contents = strings[j].split(" ");

                    String[] week = contents[0].split("-");
                    int startWeek = Integer.parseInt(week[0]);
                    int endWeek = Integer.parseInt(week[1]);
                    kc.setKcStartWeek(startWeek);
                    kc.setKcEndWeek(endWeek);
//                    Log.i(TAG, "startweek" + startWeek + "  endweek" + endWeek);

                    int weekend = Integer.parseInt(contents[1].substring(2, 3));
                    kc.setKcWeekend(weekend);

                    if (contents[1].contains("单")) {
                        String timeStr = contents[1].substring(4, contents[1].length() - 4);
                        String[] time = timeStr.split("-");
                        int startTime = Integer.parseInt(time[0]);
                        int endTime = Integer.parseInt(time[1]);
                        kc.setKcStartTime(startTime);
                        kc.setKcEndTime(endTime);
                        kc.setKcIsDouble(false);

                    } else if (contents[1].contains("双")) {
                        String timeStr = contents[1].substring(4, contents[1].length() - 4);
                        String[] time = timeStr.split("-");
                        int startTime = Integer.parseInt(time[0]);
                        int endTime = Integer.parseInt(time[1]);
                        kc.setKcStartTime(startTime);
                        kc.setKcEndTime(endTime);
                        kc.setKcIsSingle(false);
                    } else {
                        String timeStr = contents[1].substring(4, contents[1].length() - 1);
                        String[] time = timeStr.split("-");
                        int startTime = Integer.parseInt(time[0]);
                        int endTime = Integer.parseInt(time[1]);
                        kc.setKcStartTime(startTime);
                        kc.setKcEndTime(endTime);
                    }

                    String location = contents[2];
                    kc.setKcLocation(location);
//                    String[] time=contents[1];
                    tempCourses.add(kc);
                } catch (Exception e) {
                    Log.i(TAG, "解析出错:"+title);
                }
            }
        }
//        List<CourseBean> courseBeans= CourseBeanLab.get(context).getCourses();
//        for (int i=0;i<courseBeans.size();i++){
//            courseBeans.remove(tempCourses.get(0));
//        }
        CourseBeanLab.get(context).getCourses().clear();
        CourseBeanLab.get(context).getCourses().addAll(tempCourses);
        DBManager dbManager=new DBManager(context);
        dbManager.dropCourseBeans();
        dbManager.insertCourseBeans(tempCourses);
        Log.i(TAG,"history共"+courseEles.size()+"个"+" 解析后:"+tempCourses.size()+"个"+" 总共:"+ CourseBeanLab.get(context).getCourses().size());
        return tempCourses;
    }



    public static ArrayList<FDScore> getScore(Context context){//获取成绩
        ArrayList<FDScore> tempScores = new ArrayList<>();
        ArrayList<FDScore> scores = FDScoreLB.get(context).getScores();
        if (FzuCookie.get().getExpTime() <= System.currentTimeMillis()) {
            HttpUtil.Login(context, DBManager.getInstance(context).queryUser(DefaultConfig.get().getUserAccount()));
        }
        String scoreHtml= HttpUtil.getCookieHtml("http://59.77.226.35/student/xyzk/cjyl/score_sheet.aspx");
        Document document = Jsoup.parse(scoreHtml);
        Elements courseEles = document.select("tr[onmouseover=c=this.style.backgroundColor;this.style.backgroundColor='#CCFFaa']");
        for (int i = 0; i < courseEles.size(); i++) {
            Element kb = courseEles.get(i);
            Elements scoreEle=kb.select("td");
            //解析学年
            String yearStr = scoreEle.get(1).text().substring(0,4);
            String xuenianStr = scoreEle.get(1).text().substring(4,6);
            int year = Integer.parseInt(yearStr);
            int xuenian = Integer.parseInt(xuenianStr);
            //解析成绩
            Element titleEle = scoreEle.get(2);
            String title = titleEle.text();
            FDScore fdscore = new FDScore();
            fdscore.setName(title);
            Element xuefenEle = scoreEle.get(3);
            String score=scoreEle.get(4).text();
            String xuefen = xuefenEle.text();
            String jidian=scoreEle.get(5).text();
            fdscore.setXuefen(xuefen);
            fdscore.setYear(year);
            fdscore.setXuenian(xuenian);
            fdscore.setScore(score);
            fdscore.setJidian(jidian);
            tempScores.add(fdscore);
        }
        Log.i(TAG,"共"+scores.size()+"个成绩"+tempScores.size());
        FDScoreLB.get(context).setScores(tempScores);
        DBManager dbManager = new DBManager(context);
        dbManager.dropFDScores();
        dbManager.insertFDScores(tempScores);
        return tempScores;
    }

    //解析考场信息
    public static List<Exam> getExamInfo(Context context){
        List<Exam> exams = new ArrayList<>();
        if (StringUtil.isEmpty(FzuCookie.get().getId()) || FzuCookie.get().getExpTime() <= System.currentTimeMillis()) {
            HttpUtil.Login(context, DBManager.getInstance(context).queryUser(DefaultConfig.get().getUserAccount()));
        }
        String result=HttpUtil.getCookieHtml("http://59.77.226.35/student/xkjg/examination/exam_list.aspx");
        if (result == null) {
            return null;
        }
        Document document = Jsoup.parse(result);
        Elements examElements=document.select("table[id=ContentPlaceHolder1_DataList_xxk]").select("tr[style=height:30px; border-bottom:1px solid gray; border-left:1px solid gray; vertical-align:middle;]");
        Log.i(TAG, "getExamInfo: examList:" + examElements.size());
        for (int i=0;i<examElements.size();i++) {
            Element element = examElements.get(i);
            Elements tds=element.select("td");
            String name = tds.get(0).text();
            String xuefen=tds.get(1).text();
            String teacher = tds.get(2).text();
            String address = tds.get(3).text();
            String zuohao = tds.get(4).text();
            Exam exam = new Exam(name, xuefen, teacher, address, zuohao);
            exams.add(exam);
        }
//        DBManager manager=DBManager.getInstance(context);
//        manager.dropExams();
//        manager.insertExams(exams);
        Log.i(TAG, "getExamInfo: 考场数量"+exams.size()+" "+exams.get(0).getName());
        return exams;
    }

    //解析个人信息
    public static boolean getStudentInfo(Context context){
        if (!StringUtil.isEmpty(FzuCookie.get().getId())) {
            HttpUtil.Login(context, DBManager.getInstance(context).queryUser(DefaultConfig.get().getUserAccount()));
        }
        String result=HttpUtil.getCookieHtml("http://59.77.226.35/jcxx/xsxx/StudentInformation.aspx");
        if (result == null) {
            return false;
        }
        Document document = Jsoup.parse(result);
        Elements tableEles=document.select("table[style=border-collapse: collapse; table-layout:fixed;]");
        String xuehao=tableEles.select("span[id=ContentPlaceHolder1_LB_xh]").text();
        String name=tableEles.select("tr").get(0).select("span[id=ContentPlaceHolder1_LB_xm]").text();
        String sex=tableEles.select("span[id=ContentPlaceHolder1_LB_xb]").text();
        String xymc=tableEles.select("span[id=ContentPlaceHolder1_LB_xymc]").text();
        String zymc=tableEles.select("span[id=ContentPlaceHolder1_LB_zymc]").text();
        String nianji=tableEles.select("span[id=ContentPlaceHolder1_LB_nj").text();
        String banji=tableEles.select("span[id=ContentPlaceHolder1_LB_bh]").text();
        DBManager dbManager = new DBManager(context);
        User user=dbManager.queryUser(xuehao);
        user.setName(name);
        user.setSex(sex);
        user.setXymc(xymc);
        user.setZymc(zymc);
        user.setNianji(nianji);
        user.setBanji(banji);
        DefaultConfig.get().setUserName(name);
        dbManager.updateUser(user);
        return true;
    }

    //解析开学时间
    public static boolean getBeginDate(String xq){
        Map<String, String> params = new HashMap<>();
        if (xq != null) {
            params.put("xq",DefaultConfig.get().getXqValues().get(xq));
        }
        String html= HttpUtil.getHtmlByParam("http://59.77.226.32/xl.asp",params);
//        System.out.println("html:"+html);
        Document document = Jsoup.parse(html);
        String now=document.select("div[style=padding:5px;border:1px black dotted]").text();
//        Log.i(TAG, "now:" + now);
        Elements dayEles=document.select("td:matches(正式上课*)");
        Element dayEle = dayEles.get(1);
        String date=document.select("table[cellspacing]").get(1).text();
        date=date.split("为正式上课")[0];
        int length=date.length();
        date=date.substring(length-5,length);
        //存储所有学期数
        Map<String,String> xqs=DefaultConfig.get().getXqValues();
        Elements options=document.select("option");
        for (Element ele:options) {
            xqs.put(ele.text(),ele.attr("value"));
        }
        int month=Integer.parseInt(date.split("-")[0]);
        int day=Integer.parseInt(date.split("-")[1]);
        System.out.println("month:"+month+" day:"+day);
        Calendar calendar=Calendar.getInstance();
        if (xq == null) {
            System.out.println("year:"+Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
            calendar.set(Calendar.getInstance().get(Calendar.YEAR),month-1,day,0,0);
        }else {
            calendar.set(Integer.parseInt(xq.substring(0,4)),month-1,day);
        }
        //存储当前设置的学期开学时间
        DefaultConfig.get().setBeginDate(calendar.getTimeInMillis());

        //根据开学时间和当前时间计算出当前周数
        long startMillis=calendar.getTimeInMillis();
        long nowMillis=System.currentTimeMillis();
        int week=DateUtil.getWeeks(startMillis,nowMillis);
        DefaultConfig.get().setNowWeek(week);
        return true;
    }

    //解析当前学期
    public static void getDate(){
        String html=HttpUtil.getHtml("http://59.77.226.32/tt.asp");
        Document document = Jsoup.parse(html);
        String curWeek=document.select("font[color=#FF0000]").get(0).text();
        int week = Integer.parseInt(curWeek);
        String yearStr = document.select("b").get(0).text();
        String year=yearStr.substring(yearStr.indexOf("学年")-4,yearStr.indexOf("学年"));
        String xuenian=yearStr.substring(yearStr.indexOf("学期")-2,yearStr.indexOf("学期"));
        DefaultConfig defaultConfig=DefaultConfig.get();

//        defaultConfig.setNowWeek(week);
        defaultConfig.setCurXuenian(Integer.parseInt(xuenian));
        defaultConfig.setCurYear(Integer.parseInt(year));
        return;
    }

    //获取易班动态列表
    public static List<Yiban> getYibanList(){
        String result=HttpUtil.getHtml("https://fzuhelper.learning2learn.cn/fzuhelper/yibanlist");
        Log.i(TAG,result);
        YibanResult yibanResult=new Gson().fromJson(result,YibanResult.class);
        Log.i(TAG,"size"+yibanResult.getData().size());
        return yibanResult.getData();
    }

    public static class YibanResult{

        private List<Yiban> data;

        public List<Yiban> getData() {
            return data;
        }

        public void setData(List<Yiban> data) {
            this.data = data;
        }
        
    }
}

