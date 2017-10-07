package com.helper.west2ol.fzuhelper.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.adapter.EmptyAdapter;
import com.helper.west2ol.fzuhelper.dto.EmptyRoom;
import com.helper.west2ol.fzuhelper.util.West2Server;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import freemarker.ext.beans.HashAdapter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by coder on 2017/9/29.
 */

public class EmptyRoomActivity extends Activity {

    @BindView(R.id.empty_room_layout)
    LinearLayout layout;
    @BindView(R.id.empty_room_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.empty_room_refreshLayout)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.empty_room_back)
    ImageView backIcon;
    @BindView(R.id.empty_room_title)
    TextView titleTv;
    @BindView(R.id.empty_room_other_date)
    TextView otherDateTv;


    TimePickerView pvTime;

    private Context context;
    private Map<String, List<EmptyAdapter.Empty>> emptyMap = new HashMap<>();
    String selectDate;
    String build;
    String time;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectDate = getIntent().getStringExtra("date");
        build = getIntent().getStringExtra("build");
        time = getIntent().getStringExtra("time");
        context=this;
        setContentView(R.layout.activity_empty_room_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titleTv.setText(selectDate);
        SinaRefreshView sinaRefreshView=new SinaRefreshView(context);
        sinaRefreshView.setRefreshingStr("查询中 请耐心等待...");

        refreshLayout.setHeaderView(sinaRefreshView);
        refreshLayout.startRefresh();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                getEmptyRoom();
            }
        });
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                System.out.println("select");
                titleTv.setText(new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()));
                selectDate=new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
                refreshLayout.startRefresh();
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).setSubmitText("查询").build();
        otherDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getEmptyRoom(){
        Observable.create(new Observable.OnSubscribe<Map>() {
            @Override
            public void call(Subscriber<? super Map> subscriber) {
                emptyMap = new HashMap<>();
                emptyMap=West2Server.getEmptyRoom(selectDate, build,time,emptyMap);
                subscriber.onNext(emptyMap);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Map>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Map map) {
                Log.i("Empty","onNext");
                EmptyAdapter emptyAdapter = new EmptyAdapter(context,emptyMap,recyclerView);
                recyclerView.setAdapter(emptyAdapter);
                refreshLayout.finishRefreshing();
                Snackbar.make(layout, "加载完成", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
