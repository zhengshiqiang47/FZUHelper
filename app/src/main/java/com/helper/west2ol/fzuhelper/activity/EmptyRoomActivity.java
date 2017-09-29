package com.helper.west2ol.fzuhelper.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.adapter.EmptyAdapter;
import com.helper.west2ol.fzuhelper.dto.EmptyRoom;
import com.helper.west2ol.fzuhelper.util.West2Server;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by coder on 2017/9/29.
 */

public class EmptyRoomActivity extends Activity {

    @Bind(R.id.empty_room_recycler)
    RecyclerView recyclerView;

    private Context context;

    String date;
    String build;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getIntent().getStringExtra("date");
        build = getIntent().getStringExtra("build");
        context=this;
        setContentView(R.layout.activity_empty_room_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Observable.create(new Observable.OnSubscribe<EmptyRoom>() {
            @Override
            public void call(Subscriber<? super EmptyRoom> subscriber) {
                EmptyRoom room=West2Server.getEmptyRoom(date, build);
                subscriber.onNext(room);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<EmptyRoom>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(EmptyRoom emptyRoom) {
                Log.i("Empty","onNext");
                EmptyAdapter emptyAdapter = new EmptyAdapter(context, emptyRoom);
                recyclerView.setAdapter(emptyAdapter);
            }
        });
    }
}
