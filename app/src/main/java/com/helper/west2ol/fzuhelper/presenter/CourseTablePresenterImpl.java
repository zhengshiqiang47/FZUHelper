package com.helper.west2ol.fzuhelper.presenter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.CourseBeanLab;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.util.CalculateUtil;
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
import rx.functions.Action0;
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
    private List<CourseBean> courseBeans;

    public CourseTablePresenterImpl(CourseTableContact.CourseView courseFragment) {
        this.courseFragment = courseFragment;
        courseFragment.setPresenter(this);
        activity=courseFragment.getParentActivity();
    }

    /**
     * 初始化工作
     */
    @Override
    public void start() {
        courseBeanMap = new HashMap<>();
        defaultConfig=DefaultConfig.get();
        options= DefaultConfig.get().getOptions();
        saveObjectUtils=new SaveObjectUtils(courseFragment.getParentActivity(),"config");
        courseBeans=CourseBeanLab.get(activity).getCourses();
        if (courseBeans == null||courseBeans.size()<=1||DefaultConfig.get().getNowWeek()==0|| StringUtil.isEmpty(defaultConfig.getUserName())||defaultConfig.getCurXuenian()==0) {
            getCurrentCourse();
        }else {
            courseFragment.showKB(courseBeans,defaultConfig.getNowWeek(), defaultConfig.getCurYear(), defaultConfig.getCurXuenian(),courseBeanMap);
        }
        if (!StringUtil.isEmpty(DefaultConfig.get().getUserName())) {
            courseFragment.showDrawerInfo(defaultConfig);
        }

    }

    /**
     * 获取当前课表
     */
    @Override
    public void getCurrentCourse() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    HtmlParseUtil.getCurrentCourse(activity);
                    HtmlParseUtil.getBeginDate(null);
                    HtmlParseUtil.getStudentInfo(activity);
                    HtmlParseUtil.getDate();
                } catch (Exception e) {
                    subscriber.onError(e);
                    return;
                }
                subscriber.onCompleted();
            }})
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        courseFragment.showLoading(true);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber() {
            @Override
            public void onCompleted() {
                DefaultConfig defaultConfig = DefaultConfig.get();
                options=DefaultConfig.get().getOptions();
                saveObjectUtils.setObject("config", defaultConfig);
                saveObjectUtils.setObject("cookie",FzuCookie.get());
                courseFragment.finishGetCourse(defaultConfig,options,false);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                courseFragment.onRefreshError();
            }

            @Override
            public void onNext(Object o) {

            }
        });

    }

    /**
     * 获取历史课表
     * @param xueNian 学年学期 如:201701
     */

    @Override
    public void getHistoryCourse(final String xueNian) {
        courseFragment.showLoading(true);
        Observable.create(new Observable.OnSubscribe<List<CourseBean>>() {
            @Override
            public void call(Subscriber<? super List<CourseBean>> subscriber) {
                List<CourseBean> courseBeans= null;
                try {
                    courseBeans = HtmlParseUtil.getHistoryCourse(activity,xueNian);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HtmlParseUtil.getBeginDate(xueNian);
                subscriber.onNext(courseBeans);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<CourseBean>>() {
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
                courseFragment.onRefreshError();
            }

            @Override
            public void onNext(List<CourseBean> courseBeans) {
                CourseTablePresenterImpl.this.courseBeans=courseBeans;
            }
        });
    }

    @Override
    public void addOptionPicker() {
        if (options == null || options.size() == 0) {
            return;
        }
        courseFragment.showOptionPicker(options);
    }

    @Override
    public void addWeekPicker() {
        List<String> weeks = new ArrayList<>();
        for (int i=1;i<=22;i++) {
            weeks.add("第"+i+"周");
        }
        courseFragment.showWeekPicer(weeks);
    }

    /**
     * 切换周数
     * @param week
     */
    @Override
    public void switchWeek(int week) {
        courseFragment.showKB(courseBeans,week,DefaultConfig.get().getCurYear(),DefaultConfig.get().getCurXuenian(),courseBeanMap);
    }

    /**
     * 显示课表
     */
    @Override
    public void showCourse() {
        courseFragment.showKB(courseBeans,defaultConfig.getNowWeek(), defaultConfig.getCurYear(), defaultConfig.getCurXuenian(),courseBeanMap);
    }

    /**
     * 课程详情弹窗
     * @param viewId 对应的课程viewID
     *
     */
    @Override
    public void addPoupWindow(int viewId) {
        CourseBean courseBean=courseBeanMap.get(viewId);
        if (courseBean != null) {
            courseFragment.popupWindow(viewId,courseBean);
        }
    }

}
