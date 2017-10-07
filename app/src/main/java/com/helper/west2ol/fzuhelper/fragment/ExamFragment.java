package com.helper.west2ol.fzuhelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.helper.west2ol.fzuhelper.R;
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
    @BindView(R.id.exam_refresh)
    ImageView examRefresh;
    @BindView(R.id.exam_bar)
    AppBarLayout examBar;
    @BindView(R.id.exam_recycler)
    RecyclerView examRecycler;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exam_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.exam_menu, R.id.exam_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exam_menu:
                break;
            case R.id.exam_refresh:
                getExam();
                break;
        }
    }

    public void getExam(){
        Observable.create(new Observable.OnSubscribe<List<Exam>>() {
            @Override
            public void call(Subscriber<? super List<Exam>> subscriber) {
                List<Exam> exams= HtmlParseUtil.getExamInfo(getActivity());
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
                    }
                });
    }
}
