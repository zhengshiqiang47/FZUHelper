package com.helper.west2ol.fzuhelper.fragment;

import android.animation.ArgbEvaluator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.activity.MainContainerActivity;
import com.helper.west2ol.fzuhelper.adapter.GradeAdapter;
import com.helper.west2ol.fzuhelper.bean.FDScoreLB;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/20.
 */

public class GradeFragment extends Fragment {
    private static final String TAG="GradeFrament";
//    Button menu_button_in_grade;
    DrawerLayout drawer;

    Context context;
    PagerAdapter mAdapter;

    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBarLayout;
    List<android.support.v4.app.Fragment> fragments;
//    LinearLayout tabLayout;

    @Override
    public void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
        context=this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedIntanceState){
        View rootView = inflater.inflate(R.layout.fragment_grade , container , false);
        ButterKnife.bind(this, rootView);
        drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
//        menu_button_in_grade = (Button)rootView.findViewById(R.id.menu_button_in_grade);
//        menu_button_in_grade.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawer.openDrawer(Gravity.LEFT);
//            }
//        });
        initViewPager();
        InitTabLayout();
        initData();
        initView(rootView);
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
//                gradeRecycler.setAdapter(new GradeAdapter(context, FDScoreLB.get(context).getScores()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }


    private void initViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ArgbEvaluator evaluator = new ArgbEvaluator();
                if (position == 0) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.journey_green));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_2_blue), getResources().getColor(R.color.tab_1_green));
                    setBg(evaluate);
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
                }

                if (1 < position && position < 2) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_3_purple));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_3_purple), getResources().getColor(R.color.tab_1_green));
                    mTabLayout.setBackgroundColor(evaluate);
                }


                if (position == 2) {
                    mTabLayout.setBackgroundColor(getResources().getColor(R.color.tab_pink));
                    int evaluate = (Integer) evaluator.evaluate(positionOffset, getResources().getColor(R.color.tab_3_purple), getResources().getColor(R.color.tab_pink));
                    setBg(evaluate);
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
        fragments.add(new GradeDetailFragment());
        fragments.add(new GradeDetailFragment());
        fragments.add(new GradeDetailFragment());
        mAdapter = new FragAdapter(((MainContainerActivity)getActivity()).getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
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

    int x=0,y=0;

    private void initView(View rootView){

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
            if (position == 0) return "201601";
            else if (position == 1) return "201602";
            else if (position == 2) return "201701";
            else return "其它";
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }
    }

}
