package com.helper.west2ol.fzuhelper.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.adapter.WheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.activity.EmptyRoomActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2017/9/28.
 */

public class EmptyRoomFragment extends Fragment {
    private static final String TAG="EmptyRoomFragment";

    @BindView(R.id.room_search_btn)
    TextView searchButton;
    @BindView(R.id.room_wheel)
    WheelView wheelView;
    @BindView(R.id.room_submit)
    TextView submitBtn;
    @BindView(R.id.empty_room_menu)
    Button menuButton;
    @BindView(R.id.empty_room_other_time)
    TextView timeBtn;
    @BindView(R.id.empty_room_date_num)
    TextView dateNumTv;


    TimePickerView pvTime;
    DrawerLayout drawer;
    OptionsPickerView pickerView;

    String selectDate;
    String build;

    {
        selectDate = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
        build="d1";
    }

    private Map<String,String> roomMap=new HashMap<String,String>(){
        {put("西三","x3");put("西二","x2");put("西一","x1");put("中楼","zl");put("东一","d3");put("东二","d2");put("东三","d3");put("文楼","wkl");}
    };
    private List<String> sTimes = new ArrayList<String>();
    private List<List<String>> eTimes = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_room_layout, null);
        ButterKnife.bind(this,view);
        initData();
        initView();
        return view;
    }

    private void initData() {
        sTimes = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            sTimes.add("第 " + (i + 1) + " 节");
            List<String> eTime = new ArrayList<>();
            for (int j=i;j<=10;j++) {
                eTime.add("第 " + (j + 1) + " 节");
            }
            eTimes.add(eTime);
        }
    }

    private void initView(){
        dateNumTv.setText(new SimpleDateFormat("d").format(System.currentTimeMillis()));
        drawer=(DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                searchButton.setText(new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()));
                selectDate=new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
                dateNumTv.setText(new SimpleDateFormat("d").format(date.getTime()));
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).setSubmitText("确定").build();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
        pickerView=new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String start=sTimes.get(options1).replaceAll("\\D*([0-9]*)\\D*","$1");
                String end=eTimes.get(options1).get(options2).replaceAll("\\D*([0-9]*)\\D*","$1");
                Intent intent = new Intent(getActivity(), EmptyRoomActivity.class);
                intent.putExtra("date", selectDate);
                intent.putExtra("build", build);
                intent.putExtra("time", start+"-"+end);
                startActivity(intent);
            }
        }).setSubmitText("立即查询").setTitleText("起止节数").setLabels("-","","").build();
        pickerView.setPicker(sTimes,eTimes);
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
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.show();
            }
        });
    }
}
