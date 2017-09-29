package com.helper.west2ol.fzuhelper.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.adapter.WheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.activity.EmptyRoomActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2017/9/28.
 */

public class EmptyRoomFragment extends Fragment {
    private static final String TAG="EmptyRoomFragment";

    @Bind(R.id.room_search_btn)
    TextView searchButton;
    @Bind(R.id.room_wheel)
    WheelView wheelView;
    @Bind(R.id.room_submit)
    TextView submitBtn;
    TimePickerView pvTime;

    String selectDate;
    String build;

    {
        selectDate = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
        build="d1";
    }

    private Map<String,String> roomMap=new HashMap<String,String>(){
        {put("西三","x3");put("西二","x2");put("西一","x1");put("中楼","zl");put("东一","d3");put("东二","d2");put("东三","d3");put("文楼","wkl");}
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_room_layout, null);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView(){
        pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                searchButton.setText(new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()));
                selectDate=new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).setSubmitText("确定").build();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
        final List<String> room= Arrays.asList("西一","西二","西三","中楼","东一","东二","东三");
        WheelAdapter wheelAdapter=new WheelAdapter() {
            private List<String> rooms=room;

            @Override
            public int getItemsCount() {
                return room.size();
            }

            @Override
            public Object getItem(int index) {
                return room.get(index);
            }

            @Override
            public int indexOf(Object o) {
                return room.indexOf(o);
            }
        };
        wheelView.setAdapter(wheelAdapter);
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.i(TAG,"选中"+roomMap.get(room.get(index)));
                build = roomMap.get(room.get(index));
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EmptyRoomActivity.class);
                intent.putExtra("date", selectDate);
                intent.putExtra("build", build);
                startActivity(intent);
            }
        });
    }
}
