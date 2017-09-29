package com.helper.west2ol.fzuhelper.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.west2ol.fzuhelper.dto.EmptyRoom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by coder on 2017/9/29.
 */

public class West2Server {

    public static void main(String[] args){
        getEmptyRoom("2017-09-30","x1");
    }

    public static EmptyRoom getEmptyRoom(String date,String build){
        Map<String, String> params = new HashMap<>();
        params.put("time", date);
        params.put("start", "1");
        params.put("end","2");
        params.put("build",build);
        String result=HttpUtil.getEmptyByParam("https://fzuhelper.learning2learn.cn/fzuhelper/blankclassroom",params);
//        Log.i("West2Server", result);
        EmptyRoom emptyRoom = new Gson().fromJson(result, EmptyRoom.class);
        return emptyRoom;
    }

}
