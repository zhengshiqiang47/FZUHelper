package com.helper.west2ol.fzuhelper.util;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class ActivityController {
    public static List<AppCompatActivity> activities = new ArrayList<AppCompatActivity>();
    public static void addActivity(AppCompatActivity activity){
        activities.add(activity);
    }
    public static void removeActivity(AppCompatActivity activity){
        activities.remove(activity);
    }
    public static void finashAll(){
        for(AppCompatActivity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
