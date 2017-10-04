package com.helper.west2ol.fzuhelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.adapter.YibanAdapter;
import com.helper.west2ol.fzuhelper.bean.Yiban;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.FzuCookie;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;
import com.helper.west2ol.fzuhelper.util.HttpUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2017/9/20.
 */

public class YibanFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.yiban_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.menu_button_in_yiban)
    Button menuButton;
    @BindView(R.id.yiban_loading)
    AVLoadingIndicatorView yibanLoading;
    @BindView(R.id.yiban_refresh)
    ImageView refreshIcon;

    DrawerLayout drawerLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_yiban,container,false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView(){
        recyclerView.removeAllViews();
        refresh();
        refreshIcon.setOnClickListener(this);
        drawerLayout= (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yiban_refresh:
                RotateAnimation rotateAnimation = new RotateAnimation(0, -360,refreshIcon.getPivotX(),refreshIcon.getPivotY());
                rotateAnimation.setDuration(1000l);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                refreshIcon.startAnimation(rotateAnimation);
                initView();
                break;
            default:
        }
    }

    public void refresh(){
        Observable.create(new Observable.OnSubscribe<List<Yiban>>() {
            @Override
            public void call(Subscriber<? super List<Yiban>> subscriber) {
                List<Yiban> dataBeens=HtmlParseUtil.getYibanList();
                subscriber.onNext(dataBeens);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        yibanLoading.show();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Yiban>>() {
                    @Override
                    public void onCompleted() {
                        yibanLoading.hide();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Yiban> yiben) {
                        recyclerView.setAdapter(new YibanAdapter(getActivity(),yiben));
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                });
    }
}
