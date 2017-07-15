package com.helper.west2ol.fzuhelper.util;

import android.content.Context;
import android.util.Log;

import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.CourseBeanLab;
import com.helper.west2ol.fzuhelper.bean.FDScore;
import com.helper.west2ol.fzuhelper.bean.FDScoreLB;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/22.
 */

public class HtmlParseUtil {
    private static final String TAG = "HtmlParseUtil";

    public static boolean getCourse(Context context,boolean isRefresh) {
        ArrayList<CourseBean> tempKcs = new ArrayList<>();
        ArrayList<CourseBean> kcs = CourseBeanLab.get(context).getCourses();
        if(kcs.size()>=2&&!isRefresh){
            Log.i(TAG, "已经解析过");
            return true;
        }
        String result = HttpUtil.getCourseHtml("http://59.77.226.35/student/xkjg/wdxk/xkjg_list.aspx");
        Document document = Jsoup.parse(result);
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
            //解析成绩
            FDScore fdscore = new FDScore();
            fdscore.setName(title);
            Element jihuaEle = kb.select("td").get(2);

//            Element scoreEle = kb.select("td").get(4);
//            String score = scoreEle.text();
//            fdscore.setScore(score);

//            Element jidianEle = kb.select("td").get(5);
//            String jidian = scoreEle.text();
//            fdscore.setJidian(jidian);

            Element xuefenEle = kb.select("td").get(4);
            String xuefen = xuefenEle.text();
            fdscore.setXuefen(xuefen);
            fdscore.setYear(year);
            fdscore.setXuenian(xuenian);

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
        CourseBeanLab.get(context).setCourses(tempKcs);
        Log.i(TAG,"共"+courseEles.size()+"个"+" 解析后:"+tempKcs.size()+"个");
        return true;
    }

    public static ArrayList<FDScore> getScore(Context context, boolean isRefresh){
        ArrayList<FDScore> tempScores = new ArrayList<>();
        ArrayList<FDScore> scores = FDScoreLB.get(context).getScores();
        if (scores.size()>1&&!isRefresh){
            Log.i(TAG,"已解析过且不刷新");
            return scores;
        }
        String scoreHtml=HttpUtil.getCourseHtml("http://59.77.226.35/student/xyzk/cjyl/score_sheet.aspx");
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
        return tempScores;
    }

    public static boolean getBeginDate(Context context){
        String html=HttpUtil.getHtml("http://59.77.226.32/xl.asp");
        Log.i(TAG,html);
        Document document = Jsoup.parse(html);
        String now=document.select("div[style=padding:5px;border:1px black dotted]").text();
        Log.i(TAG, "now:" + now);
//        Elements monthEles=document.select("td[colspan=8]");
//        String beginText=monthEles.get(0).text();
//        String beginMonth=beginText.split("年")[1].split("月")[0];
//        Log.i(TAG, "开学月份:" + beginMonth);
//        int month=Integer.parseInt(beginMonth)-1;
        Elements dayEles=document.select("td:matches(正式上课*)");
        Element dayEle = dayEles.get(1);
        String date=document.select("table[cellspacing]").get(1).text();
        date=date.split("为正式上课")[0];
        int length=date.length();
        date=date.substring(length-5,length);
        int month=Integer.parseInt(date.split("-")[0]);
        int day=Integer.parseInt(date.split("-")[1]);
        Log.i(TAG, "month:" + month + " day" + day);
        return false;
    }
}

