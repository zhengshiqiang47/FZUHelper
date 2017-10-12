package com.helper.west2ol.fzuhelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.adapter.ExamAdapter;
import com.helper.west2ol.fzuhelper.bean.Exam;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by coder on 2017/10/7.
 */

public class ExamFragment extends Fragment {


    private static final String TAG = "ExamFragment";
    @BindView(R.id.exam_menu)
    Button examMenu;
    @BindView(R.id.exam_bar)
    AppBarLayout examBar;
    @BindView(R.id.exam_recycler)
    RecyclerView examRecycler;
    @BindView(R.id.exam_tool_bar)
    android.support.v7.widget.Toolbar toolbar;

    Unbinder unbinder;

    DrawerLayout drawer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exam_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        toolbar = (android.support.v7.widget.Toolbar) rootView.findViewById(R.id.exam_tool_bar);
        setHasOptionsMenu(true);
        initView();
        return rootView;
    }

    private void initView() {
        drawer=(DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        toolbar.inflateMenu(R.menu.fragemnt_exam_menu);
        getExam(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragemnt_exam_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick({R.id.exam_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exam_menu:
                drawer.openDrawer(Gravity.LEFT);
                getExam("201601");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void getExam(final String xuenian){
        Observable.create(new Observable.OnSubscribe<List<Exam>>() {
            @Override
            public void call(Subscriber<? super List<Exam>> subscriber) {
                List<Exam> exams=null;
                if (xuenian == null) {
                    exams = HtmlParseUtil.getExamInfo(getActivity());
                }else {
                    exams = HtmlParseUtil.getHistoryExamInfo(getActivity(), xuenian);
                }
                subscriber.onNext(exams);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Exam>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Exam> exams) {
                        Log.i(TAG, "onNext:" + exams.size());
                        ExamAdapter adapter = new ExamAdapter(getActivity(),exams);
                        examRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        examRecycler.setAdapter(adapter);
                    }
                });
    }

}
