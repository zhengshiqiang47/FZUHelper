package com.helper.west2ol.fzuhelper.fragment;

import android.animation.ArgbEvaluator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.activity.MainContainerActivity;
import com.helper.west2ol.fzuhelper.adapter.GradeAdapter;
import com.helper.west2ol.fzuhelper.bean.FDScore;
import com.helper.west2ol.fzuhelper.bean.FDScoreLB;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.util.CalculateUtil;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/20.
 */

public class GradeFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    private static final String TAG="GradeFrament";
    //    Button menu_button_in_grade;
    DrawerLayout drawer;

    Context context;
    PagerAdapter mAdapter;
    int color;
    boolean isHidden=false;

    @BindView(R.id.menu_button_in_course_table)
    Button menu_button_in_course_table;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.grade_title)
    TextView title;
    @BindView(R.id.grade_loading_layout)
    RelativeLayout loadingLayout;
    @BindView(R.id.grade_loading)
    AVLoadingIndicatorView loadingView;
    @BindView(R.id.grade_refresh)
    ImageView refreshGrade;

    List<android.support.v4.app.Fragment> fragments;
    Map<String,List<FDScore>> scoreMap;
    Map<Integer,String> tabTitle;

    @Override
    public void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
        context=this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedIntanceState){
        View rootView = inflater.inflate(R.layout.fragment_grade , container , false);
        ButterKnife.bind(this, rootView);
        initView();
        initData();
        return rootView;
    }

    private void initData(){
        showLoading(false);
        List<FDScore> fdScores= DBManager.getInstance(getActivity()).queryFDScoreList();
        if (fdScores != null && fdScores.size() >= 1) {
            scoreMap= CalculateUtil.getTermScores(fdScores);
            initViewPager();

            return;
        }
        Observable.create(new Observable.OnSubscribe<List<FDScore>>() {
            @Override
            public void call(Subscriber<? super List<FDScore>> subscriber) {
                loadingView.show();
                List<FDScore> scores=HtmlParseUtil.getScore(context);
                subscriber.onNext(scores);
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
                .subscribe(new Observer<List<FDScore>>() {

                    @Override
                    public void onCompleted() {
                        initViewPager();
                        showLoading(false);
                        Snackbar.make(viewPager,"刷新成功",Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showLoading(false);
                        Snackbar.make(viewPager,"服务器出错",Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(List<FDScore> scores) {
                        scoreMap= CalculateUtil.getTermScores(scores);
                    }
                });
    }

    private void initView(){
        refreshGrade.setOnClickListener(this);
        color=getResources().getColor(R.color.colorPrimary);
        drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (!isHidden){
                    ArgbEvaluator evaluator = new ArgbEvaluator();
                    int evaluate = (Integer) evaluator.evaluate(1-slideOffset, getResources().getColor(R.color.colorPrimary), color);
                    setBg(evaluate);
                    title.setTranslationX(400*slideOffset);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
//                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        menu_button_in_course_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }

    public void showLoading(boolean isShow) {
        if (isShow) {
            loadingLayout.setVisibility(View.VISIBLE);
            loadingView.show();
        }else {
            loadingLayout.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
            loadingView.hide();
        }
    }

    private void refreshData(){
        mAdapter=null;
        RotateAnimation rotateAnimation = new RotateAnimation(0, -360,refreshGrade.getPivotX(),refreshGrade.getPivotY());
        rotateAnimation.setDuration(1000l);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        refreshGrade.startAnimation(rotateAnimation);
        showLoading(true);
        Observable.create(new Observable.OnSubscribe<List<FDScore>>() {
            @Override
            public void call(Subscriber<? super List<FDScore>> subscriber) {
                List<FDScore> scores=HtmlParseUtil.getScore(context);
                subscriber.onNext(scores);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<FDScore>>() {

                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: 刷新成绩完成");
                        showLoading(false);
                        initViewPager();//Here is a Bug
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<FDScore> scores) {
                        scoreMap= CalculateUtil.getTermScores(scores);
                    }
                });
    }

    //设置过渡颜色
    private void initViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ArgbEvaluator evaluator = new ArgbEvaluator();
                if (position == 0) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.journey_green));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_2_blue), getResources().getColor(R.color.tab_1_green));
                    setBg(evaluate);
                    color=evaluate;
                }
                if (0 < position && position < 1) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_1_green));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_1_green), getResources().getColor(R.color.tab_2_blue));
                    setBg(evaluate);
                }

                if (position == 1) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_3_purple));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_1_green), getResources().getColor(R.color.tab_3_purple));
                    setBg(evaluate);
                    color=evaluate;
                }

                if (1 < position && position < 2) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_3_purple));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_3_purple), getResources().getColor(R.color.tab_1_green));
                    setBg(evaluate);
                }


                if (position == 2) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.score_70));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_3_purple), getResources().getColor(R.color.score_70));
                    setBg(evaluate);
                    color=evaluate;
                }

                if (2 < position && position < 3) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.score_70));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.score_70), getResources().getColor(R.color.course_color1));
                    setBg(evaluate);
                }
                if (position == 3) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.score_70), getResources().getColor(R.color.colorPrimary));
                    setBg(evaluate);
                    color=evaluate;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fragments = new ArrayList<android.support.v4.app.Fragment>();
        tabTitle=new HashMap<>();
        Iterator<Map.Entry<String,List<FDScore>>> iterator=scoreMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,List<FDScore>> entry=iterator.next();
            fragments.add(GradeDetailFragment.getInstance(entry.getValue(),getActivity()));
            tabTitle.put(tabTitle.size(),entry.getKey());
        }
        mAdapter = new FragAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        InitTabLayout();
    }

    private void setBg(int evaluate){
        mTabLayout.setBackgroundColor(evaluate);
        getActivity().getWindow().setStatusBarColor(evaluate);
        appBarLayout.setBackgroundColor(evaluate);

//        textView.setBackgroundColor(evaluate);
    }

    private void InitTabLayout() {
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grade_refresh:
                refreshData();
                break;
        }
    }


    class FragAdapter extends FragmentPagerAdapter {
        private List<android.support.v4.app.Fragment> fragments;

        public FragAdapter(android.support.v4.app.FragmentManager fm, List<android.support.v4.app.Fragment> fragments) {
            super(fm);
            this.fragments=fragments;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle.get(position);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        isHidden=hidden;
        super.onHiddenChanged(hidden);
    }

}
