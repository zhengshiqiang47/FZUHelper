package com.helper.west2ol.fzuhelper.bean;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by zsq on 16-8-22.
 */
public class FDScoreLB {
    private static final String TAG = "FDScoreLB";
    private static final String FILENAME = "fdscore.json";
    private ArrayList<FDScore> scores;
    private static FDScoreLB scoreLB;
    private Context mAppContext;

    private FDScoreLB(Context context) {
        mAppContext=context;
        scores = new ArrayList<FDScore>();
    }

    public static FDScoreLB get(Context context) {
        if (scoreLB == null) {
            scoreLB=new FDScoreLB(context);
        }
        return scoreLB;
    }

    public void setScores(ArrayList<FDScore> scores){
        this.scores=scores;
    }

    public ArrayList<FDScore> getScores(){
        return scores;
    }

    private void clear() {
        scores.clear();
    }

}
