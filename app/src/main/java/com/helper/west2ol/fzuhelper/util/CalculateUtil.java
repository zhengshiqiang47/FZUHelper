package com.helper.west2ol.fzuhelper.util;

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

}
