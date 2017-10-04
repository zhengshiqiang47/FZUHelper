package com.helper.west2ol.fzuhelper.presenter;

import android.content.Context;

import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CoderQiang on 2017/10/3.
 * 课表契约类
 */

public interface CourseTableContact {

    interface CoursePresenter extends BasePresenter {
        void getCurrentCourse();
        void getHistoryCourse(String year);
        void addOptionPicker();
        void addWeekPicker();
        void switchWeek(int week);
        void showCourse();
        void addPoupWindow(int viewId);
    }

    interface CourseView extends BaseView<CoursePresenter> {
        void showKB(List<CourseBean> courseBeans,int week, int year, int xuenian, Map<Integer,CourseBean> courseBeanMap);
        void showDrawerInfo(DefaultConfig defaultConfig);
        void showLoading(boolean isShow);
        void showOptionPicker(ArrayList<String> options);
        void showWeekPicer(List<String> weeks);
        void finishGetCourse(DefaultConfig defaultConfig, ArrayList<String> options,boolean isHistoryCourse);
        void popupWindow(int viewId,CourseBean courseBean);
        Context getParentActivity();
    }
}
