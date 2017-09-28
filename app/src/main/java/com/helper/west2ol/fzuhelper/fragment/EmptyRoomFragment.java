package com.helper.west2ol.fzuhelper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.bigkoo.pickerview.TimePickerView;
import com.helper.west2ol.fzuhelper.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2017/9/28.
 */

public class EmptyRoomFragment extends Fragment {
    @Bind(R.id.room_search_btn)
    Button searchButton;

    TimePickerView pvTime;

    private Map<String,String> roomMap=new HashMap<String,String>(){
        {put("西三","x3");put("西二","x2");put("西一","x1");put("中楼","zl");put("东一","d3");put("东二","d2");put("东三","d3");put("文楼","wkl");}
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_room_layout, null);
        ButterKnife.bind(this,view);
        pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                searchButton.setText(new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()));
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).setSubmitText("查询").build();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pvTime.show();
            }
        });
        return view;
    }
}
