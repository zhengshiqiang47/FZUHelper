package com.helper.west2ol.fzuhelper.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.adapter.GradeAdapter;
import com.helper.west2ol.fzuhelper.bean.FDScore;
import com.helper.west2ol.fzuhelper.bean.FDScoreLB;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/20.
 */

public class GradeFragment extends Fragment {
    Button menu_button_in_grade;
    DrawerLayout drawer;
    RecyclerView gradeRecycler;
    Context context;

    @Override
    public void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
        context=this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedIntanceState){
        View rootView = inflater.inflate(R.layout.fragment_grade , container , false);
        drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        menu_button_in_grade = (Button)rootView.findViewById(R.id.menu_button_in_grade);
        menu_button_in_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        gradeRecycler=(RecyclerView)rootView.findViewById(R.id.grade_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        gradeRecycler.setLayoutManager(layoutManager);
        initData();
        initView();

        return rootView;
    }

    private void initData(){
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onNext(null);
                HtmlParseUtil.getScore(context,false);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {

            @Override
            public void onCompleted() {
                gradeRecycler.setAdapter(new GradeAdapter(context, FDScoreLB.get(context).getScores()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    private void initView(){


    }

}
