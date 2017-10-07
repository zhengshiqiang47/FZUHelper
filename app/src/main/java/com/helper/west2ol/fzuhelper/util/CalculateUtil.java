package com.helper.west2ol.fzuhelper.util;

import android.content.Context;
import android.util.TypedValue;

import com.helper.west2ol.fzuhelper.bean.FDScore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by CoderQiang on 2017/9/20.
 */

public class CalculateUtil {

    public static Map<String,List<FDScore>> getTermScores(List<FDScore> fdScores){
        Map<String, List<FDScore>> result = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);//降序
            }
        });
        for (FDScore fdScore : fdScores) {
            String xuenian =""+ fdScore.getYear() + fdScore.getXuenian();
            List<FDScore> scores=result.get(xuenian);
            if (scores == null) {
                scores = new ArrayList<>();
            }
            scores.add(fdScore);
            result.put(xuenian,scores);
        }
        return result;
    }

    public static String getWeekChinese(int i){
        switch (i) {
            case 1:return "一";
            case 2:return "二";
            case 3:return "三";
            case 4:return "四";
            case 5:return "五";
            case 6:return "六";
            case 7:return "日";
            default:return "无";
        }
    }

    public static int dp2px(Context context, float dpVal)

    {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,

                dpVal, context.getResources().getDisplayMetrics());

    }

    public static float px2dp(Context context, float pxVal)

    {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (pxVal / scale);

    }



}
