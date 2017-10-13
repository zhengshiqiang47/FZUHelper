package com.helper.west2ol.fzuhelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.adapter.ExamAdapter;
import com.helper.west2ol.fzuhelper.bean.Exam;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.wang.avi.AVLoadingIndicatorView;

import java.security.cert.Certificate;
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
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by coder on 2017/10/7.
 *
 */

public class ExamFragment extends Fragment {


    private static final String TAG = "ExamFragment";
    @BindView(R.id.exam_menu)
    Button examMenu;
    @BindView(R.id.exam_bar)
    AppBarLayout examBar;
    @BindView(R.id.exam_recycler)
    RecyclerView examRecycler;
    @BindView(R.id.item_exam_loading)
    AVLoadingIndicatorView loadingView;
    @BindView(R.id.exam_twinkRefresh)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.item_exam_loading_layout)
    RelativeLayout loadingLayout;
    @BindView(R.id.exam_root_layout)
    CoordinatorLayout rootLayout;
    @BindView(R.id.exam_refresh)
    ImageView refrshIcon;

    Unbinder unbinder;

    DrawerLayout drawer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exam_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        initView();
        return rootView;
    }

    private void initView() {
        loadingView.hide();
        drawer=(DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        getExam(null);
        SinaRefreshView sinaRefreshView = new SinaRefreshView(getActivity());
        sinaRefreshView.setRefreshingStr("正在获取中...");
        refreshLayout.setHeaderView(sinaRefreshView);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                getExam(null);
            }
        });
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

    @OnClick({R.id.exam_menu,R.id.exam_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exam_menu:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.exam_refresh:
                getExam(null);
                break;
        }
    }

    public void showLoading(boolean isShow) {
        if (isShow) {
            loadingLayout.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.VISIBLE);
            loadingView.show();
        }else {
            loadingLayout.setVisibility(View.GONE);
            loadingView.hide();
            loadingView.setVisibility(View.GONE);
        }
    }

    public void finishRefresh() {
        showLoading(false);
        refreshLayout.finishRefreshing();
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
                try {
                    if (xuenian == null) {
                        exams = HtmlParseUtil.getExamInfo(getActivity());
                    }else {
                        exams = HtmlParseUtil.getHistoryExamInfo(getActivity(), xuenian);
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }

                subscriber.onNext(exams);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading(true);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Exam>>() {
                    @Override
                    public void onCompleted() {
                        finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "获取考表出错 ！将显示缓存数据，请稍后手动刷新", Toast.LENGTH_SHORT).show();
                        finishRefresh();
                        List<Exam> exams = DBManager.getInstance(getActivity()).queryExamList();
                        ExamAdapter adapter = new ExamAdapter(getActivity(),exams);
                        examRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        examRecycler.setAdapter(adapter);
                    }

                    @Override
                    public void onNext(List<Exam> exams) {
                        Log.i(TAG, "onNext:" + exams.size());
                        ExamAdapter adapter = new ExamAdapter(getActivity(),exams);
                        examRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        examRecycler.setAdapter(adapter);
                        String content="获取成绩成功";
                        Snackbar.make(rootLayout,content,Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

}
