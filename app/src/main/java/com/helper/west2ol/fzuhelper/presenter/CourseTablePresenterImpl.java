package com.helper.west2ol.fzuhelper.presenter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.CourseBeanLab;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.FzuCookie;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;
import com.helper.west2ol.fzuhelper.util.SaveObjectUtils;
import com.helper.west2ol.fzuhelper.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2017/9/18.
 */

public class CourseTablePresenterImpl implements CourseTableContact.CoursePresenter{
    public static final String TAG="CoursePresenterImpl";

    ArrayList<String> options = new ArrayList<>();
    private CourseTableContact.CourseView courseFragment;
    private SaveObjectUtils saveObjectUtils;
    private Context activity;
    private DefaultConfig defaultConfig;
    Map<Integer,CourseBean> courseBeanMap;

    public CourseTablePresenterImpl(CourseTableContact.CourseView courseFragment) {
        this.courseFragment = courseFragment;
        courseFragment.setPresenter(this);
        activity=courseFragment.getParentActivity();
    }


    @Override
    public void start() {
        courseBeanMap = new HashMap<>();
        defaultConfig=DefaultConfig.get();
        options= DefaultConfig.get().getOptions();
        saveObjectUtils=new SaveObjectUtils(courseFragment.getParentActivity(),"config");
        if (CourseBeanLab.get(activity).getCourses() == null||CourseBeanLab.get(activity).getCourses().size()<=1) {
            getCurrentCourse();
        }
        if (!StringUtil.isEmpty(DefaultConfig.get().getUserName())) {
            courseFragment.showDrawerInfo(defaultConfig);
        }
        courseFragment.showKB(defaultConfig.getNowWeek(), defaultConfig.getCurYear(), defaultConfig.getCurXuenian(),courseBeanMap);
    }

    @Override
    public void getCurrentCourse() {
        courseFragment.showLoading(true);
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                HtmlParseUtil.getCurrentCourse(activity);
                HtmlParseUtil.getBeginDate(null);
                HtmlParseUtil.getStudentInfo(activity);
                HtmlParseUtil.getDate();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber() {
            @Override
            public void onCompleted() {
                DefaultConfig defaultConfig = DefaultConfig.get();
                options=DefaultConfig.get().getOptions();
                saveObjectUtils.setObject("config", defaultConfig);
                courseFragment.finishGetCourse(defaultConfig,options,false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });

    }


    @Override
    public void getHistoryCourse(final String xueNian) {
        courseFragment.showLoading(true);
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                HtmlParseUtil.getHistoryCourse(activity,xueNian);
                HtmlParseUtil.getBeginDate(xueNian);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber() {
            @Override
            public void onCompleted() {
                DefaultConfig defaultConfig=DefaultConfig.get();
                defaultConfig.setCurYear(Integer.parseInt(xueNian.substring(0,4)));
                defaultConfig.setCurXuenian(Integer.parseInt(xueNian.substring(4,6)));
                defaultConfig.setNowWeek(1);
                FzuCookie fzuCookie=FzuCookie.get();
                saveObjectUtils.setObject("config", defaultConfig);
                saveObjectUtils.setObject("cookie",fzuCookie);
                Log.i(TAG,defaultConfig.getCurYear()+" "+defaultConfig.getCurXuenian()+" "+defaultConfig.getNowWeek()+" "+defaultConfig.getUserAccount());
//                spinner.setSelection(defaultConfig.getNowWeek()-1);
                courseFragment.finishGetCourse(defaultConfig,options,true);
                courseFragment.showLoading(false);
                courseFragment.showDrawerInfo(defaultConfig);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    @Override
    public void saveConfig() {

    }

    @Override
    public void addOptionPicker() {
        if (options == null || options.size() == 0) {
            return;
        }
        courseFragment.showOptionPicker(options);
    }

    @Override
    public void switchWeek(int week) {
        courseFragment.showKB(week,DefaultConfig.get().getCurYear(),DefaultConfig.get().getCurXuenian(),courseBeanMap);
    }

    @Override
    public void showCourse() {
        courseFragment.showKB(defaultConfig.getNowWeek(), defaultConfig.getCurYear(), defaultConfig.getCurXuenian(),courseBeanMap);
    }

    @Override
    public void addPoupWindow(int viewId) {
        CourseBean courseBean=courseBeanMap.get(viewId);
        if (courseBean != null) {
            courseFragment.popupWindow(viewId,courseBean);
        }
    }

}
