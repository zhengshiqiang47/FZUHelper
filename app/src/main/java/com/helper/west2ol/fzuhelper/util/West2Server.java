package com.helper.west2ol.fzuhelper.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.west2ol.fzuhelper.adapter.EmptyAdapter;
import com.helper.west2ol.fzuhelper.dto.Empty;
import com.helper.west2ol.fzuhelper.dto.EmptyRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by coder on 2017/9/29.
 */

public class West2Server {

    public static void main(String[] args){
//        getEmptyRoom("2017-09-30","x1");
    }

    public static Map<String,List<Empty>> getEmptyRoom(String date, String build, String time, Map<String,List<Empty>> emptyMap){
        for (int j=1;j<=9;j++) {
            Map<String, String> params = new HashMap<>();
            params.put("time", date);
            params.put("build",build);
            if (time == null) {
                params.put("start", j*2-1+"");
                params.put("end",(j*2)+"");
                if (j == 5) {
                    params.put("end",(j*2+1)+"");
                } else if (j == 6) {
                    params.put("start", "1");
                    params.put("end","4");
                } else if (j == 7) {
                    params.put("start", "5");
                    params.put("end","8");
                } else if (j == 8) {
                    params.put("start", "9");
                    params.put("end","11");
                } else if (j == 9) {
                    params.put("start", "1");
                    params.put("end","11");
                }
            }else {
                params.put("start", time.split("-")[0]);
                params.put("end", time.split("-")[1]);
            }
            String result=HttpUtil.getEmptyByParam("https://fzuhelper.learning2learn.cn/fzuhelper/blankclassroom",params);
            EmptyRoom emptyRoom = new Gson().fromJson(result, EmptyRoom.class);
            List<EmptyRoom.DataListBean.DataBean> dataList = emptyRoom.getDataList().getData();
            List<Empty> empties = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                Empty empty = new Empty();
                empty.setRoomName(dataList.get(i).getRoomName());
                empty.setType(EmptyAdapter.TYPE_CHILD);
                if (i != dataList.size() - 1) {
                    empties.add(empty);
                }
            }
            emptyMap.put(params.get("start")+"-"+params.get("end"), empties);
            if (time != null) {
                break;
            }
        }

        return emptyMap;
    }


}
