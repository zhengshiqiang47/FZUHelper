package com.helper.west2ol.fzuhelper.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.CourseBeanLab;
import com.helper.west2ol.fzuhelper.bean.FDScoreLB;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.presenter.CourseTableContact;
import com.helper.west2ol.fzuhelper.presenter.CourseTablePresenterImpl;
import com.helper.west2ol.fzuhelper.util.CalculateUtil;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.FzuCookie;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;
import com.helper.west2ol.fzuhelper.util.HttpUtil;
import com.helper.west2ol.fzuhelper.util.SaveObjectUtils;
import com.helper.west2ol.fzuhelper.view.MyFab;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.wang.avi.AVLoadingIndicatorView;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CourseTableFragment extends Fragment implements View.OnClickListener,CourseTableContact.CourseView{
    private static final String TAG = "CourseTableFragment";

    protected TextView empty;/** 第一个无内容的格子 */
    int[] background = {
            R.drawable.course_bg1, R.drawable.course_bg2, R.drawable.course_bg3, R.drawable.course_bg4,
            R.drawable.course_bg5, R.drawable.course_bg6, R.drawable.course_bg7, R.drawable.course_bg8,
            R.drawable.course_bg9, R.drawable.course_bg10, R.drawable.course_bg11, R.drawable.course_bg12,
            R.drawable.course_bg13, R.drawable.course_bg14, R.drawable.course_bg15, R.drawable.course_bg16,
            R.drawable.course_bg1, R.drawable.course_bg2, R.drawable.course_bg3, R.drawable.course_bg4,
            R.drawable.course_bg5, R.drawable.course_bg6, R.drawable.course_bg7, R.drawable.course_bg8,
            R.drawable.course_bg9, R.drawable.course_bg10, R.drawable.course_bg11, R.drawable.course_bg12,
            R.drawable.course_bg13, R.drawable.course_bg14, R.drawable.course_bg15, R.drawable.course_bg16,
    };

    Button menu_button_in_course_table;
    @BindView(R.id.more_button_in_course_table)
    Button moreButton;
    @BindView(R.id.course_table_nice_spinner)
    NiceSpinner niceSpinner;
    @BindView(R.id.course_layout)
    LinearLayout layout;
    @BindView(R.id.course_table_loading)
    AVLoadingIndicatorView loadingView;
    @BindView(R.id.course_table_loading_layout)
    RelativeLayout loadingLayout;
    @BindView(R.id.weekday_layout)
    RelativeLayout weekLayout;
    /** 课程表body部分布局 */
    @BindView(R.id.test_course_rl)
    RelativeLayout course_table_layout;
    @BindView(R.id.fab)
    MyFab fab;
    @BindView(R.id.course_table_footer)
    Toolbar toolbar;
    @BindView(R.id.fab_sheet_item_setting)
    RelativeLayout settingBtn;
    @BindView(R.id.fab_sheet_item_refresh)
    RelativeLayout refreshBtn;
    @BindView(R.id.fab_sheet_item_create)
    RelativeLayout createBtn;
    @BindView(R.id.fab_sheet_item_date)
    RelativeLayout dateBtn;

    private View view;

    PopupWindow popupWindow;
    DrawerLayout drawer;
    MaterialSheetFab materialSheetFab;

    private int leftWidth=0;//第一列(课表序号列)所占宽度

    private CourseTableFragment fragment;
    private CourseTableContact.CoursePresenter coursePresenter;

    @Override
    public void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
        fragment=this;
        coursePresenter=new CourseTablePresenterImpl(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedIntanceState){
        View rootView = inflater.inflate(R.layout.fragment_course_table , container , false);
        ButterKnife.bind(this, rootView);
        view=rootView;
        initView(rootView);
        coursePresenter.start();
        Log.i("CourseTable", "初始化完成");
        return rootView;
    }

    private void initView(View rootView){
        loadingView.hide();
        drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        menu_button_in_course_table = (Button)rootView.findViewById(R.id.menu_button_in_course_table);
        fab.setOnClickListener(this);
        menu_button_in_course_table.setOnClickListener(this);
        moreButton.setOnClickListener(this);
        loadingLayout.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
        createBtn.setOnClickListener(this);
        popupWindow= new PopupWindow(getActivity());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.poup_course_detail, null));
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.PoupAnimation);
        View sheetView = rootView.findViewById(R.id.fab_sheet);
        View overlay = rootView.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.colorPrimary);
        int fabColor = getResources().getColor(R.color.colorPrimary);
        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                sheetColor, fabColor);


        List<String> weeks = new LinkedList<>();
        for (int i=0;i<22;i++) {
            weeks.add("第 "+(i+1)+" 周") ;
        }
        niceSpinner.attachDataSource(weeks);
        niceSpinner.setTextColor(Color.WHITE);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("position:"+position);
                DefaultConfig.get().setNowWeek(position+1);
                coursePresenter.switchWeek(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        try {
            Log.i(TAG, "initView: NowWeek:"+DefaultConfig.get().getNowWeek());
            niceSpinner.setSelectedIndex(DefaultConfig.get().getNowWeek()-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initKB(View v){
        empty = (TextView) v.findViewById(R.id.test_empty);
        weekLayout.removeAllViews();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels-leftWidth;//屏幕宽度
        int aveWidth = width / 7;//平均宽度
        for (int i=1;i<=7;i++) {
            //相对布局参数
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            rp.leftMargin=(i-1)*aveWidth-CalculateUtil.dp2px(getActivity(),2);
            rp.topMargin =5;

            TextView tx = new TextView(getActivity());
            tx.setGravity(Gravity.CENTER);
            tx.setId((i - 1) * 8  + 1);
            tx.setSingleLine(true);
            //设置周数的序号（星期一到星期日）
            tx.setText("周"+CalculateUtil.getWeekChinese(i));
            tx.setTextSize(12);
            //设置他们的相对位置
            tx.setTextColor(getResources().getColor(R.color.colorBlack));
            tx.setLayoutParams(rp);
            weekLayout.addView(tx);
        }

        int curWeek=DefaultConfig.get().getNowWeek();
        long beginDate=DefaultConfig.get().getBeginDate();
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(beginDate);
        calendar.add(Calendar.WEEK_OF_MONTH,+curWeek-1);
        for (int i = 1; i <= 7; i++) {
            int month=calendar.get(Calendar.MONTH)+1;
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            rp.leftMargin=(i-1)*aveWidth-CalculateUtil.dp2px(getActivity(),2);
            rp.addRule(RelativeLayout.BELOW,(i - 1) * 8  + 1);
            TextView tx = new TextView(getActivity());
            tx.setGravity(Gravity.CENTER);
            tx.setSingleLine(true);
            //设置周数的序号（星期一到星期日）
            tx.setText(month+"-"+day);
            tx.setTextSize(12);
            //设置他们的相对位置
            tx.setTextColor(getResources().getColor(R.color.colorBlack));
            tx.setLayoutParams(rp);
            weekLayout.addView(tx);
        }

    }

    /**
     * 显示课表
     * @param week 星期
     * @param year 年份
     * @param xuenian 学年 1或 2
     */
    public void showKB(List<CourseBean> kcs,int week, int year, int xuenian,Map<Integer,CourseBean> courseBeanMap){
        leftWidth=CalculateUtil.dp2px(getActivity(),20);
        if (course_table_layout != null) {
            course_table_layout.removeAllViews();
        }
        courseBeanMap.clear();
        initKB(view);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //屏幕宽度
        int width = dm.widthPixels-leftWidth;
        //平均宽度
        int aveWidth = (width) / 7;
        //第一个空白格子设置为25宽
        int height = dm.heightPixels;
        int gridHeight = height / 11;

        //设置课表界面
        //动态生成11 * maxCourseNum个textview
        for(int i = 1; i <= 11; i ++){
            TextView tx = new TextView(getActivity());
            tx.setId((i - 1) * 8  + 1);
            //相对布局参数
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth, gridHeight);
            tx.setGravity(Gravity.CENTER);
//          设置课的序号（1 到 12）
            tx.setText(i+"");
            rp.width = leftWidth;
            //设置他们的相对位置
            if (i == 1)
                rp.addRule(RelativeLayout.BELOW, empty.getId());
            else
                rp.addRule(RelativeLayout.BELOW, (i - 2) * 8+1);
            if((i-1)%4==0&&i>=2){
                rp.topMargin=gridHeight/4;
            }
            tx.setTextColor(getResources().getColor(R.color.colorBlack));
            tx.setLayoutParams(rp);
            course_table_layout.addView(tx);
        }

        // 添加课程信息
        Log.i(TAG, "课程数" + kcs.size());
        int[][][] mark=new int[8][13][26];
        for(int j=0;j<8;j++) {
            for(int k=0;k<13;k++) {
                for(int l=0;l<26;l++) {
                    mark[j][k][l]=0;
                }
            }
        }

        for (int i=0;i<kcs.size();i++) {
            CourseBean kc= kcs.get(i);
            if(kc.getKcXuenian() !=xuenian||kc.getKcYear()!=year){
                continue;
            }
            if(kc.getKcStartWeek()<=week&&kc.getKcEndWeek()>=week){
                if(kc.isKcIsSingle()&&week%2==1){
                    for(int j=kc.getKcStartTime();j<=kc.getKcEndTime();j++) {
                        mark[kc.getKcWeekend()][j][week]=1;
                    }
                } else if(kc.isKcIsDouble() && week % 2 == 0){
                    for(int j=kc.getKcStartTime();j<=kc.getKcEndTime();j++) {
                        mark[kc.getKcWeekend()][j][week]=1;
                    }
                }
            }
        }

        for (int i=0;i<kcs.size();i++) {
            CourseBean kc= kcs.get(i);
            if(kc.getKcXuenian() !=xuenian||kc.getKcYear()!=year){
                continue;
            }
            TextView courseInfo = new TextView(getActivity());
            courseInfo.setId(courseBeanMap.size()+100);
            String name=kc.getKcName();

            if(name.length()>=13){
                name = name.substring(0, 11);
                name += "...";
            }
            courseInfo.setOnClickListener(this);
            courseInfo.setText(name+"\n\n"+kc.getKcLocation());
            //该textview的高度根据其节数的跨度来设置
            int timecount = Math.abs(kc.getKcEndTime()-kc.getKcStartTime()+1) ;
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(aveWidth-8, (gridHeight - 5) * timecount +timecount/4*5);
            //textview的位置由课程开始节数和上课的时间（day of week）确定
            rlp.topMargin = 5 + (kc.getKcStartTime()- 1) * gridHeight+(kc.getKcStartTime()/4*(gridHeight/4));
            rlp.leftMargin = leftWidth+(kc.getKcWeekend()-1)*(aveWidth); // 偏移由这节课是星期几决定
//          rlp.addRule(RelativeLayout.RIGHT_OF, kc.getKcWeekend());
            courseInfo.setGravity(Gravity.CENTER);
            // 设置一种背景 根据当前周数设定
            if(kc.getKcStartWeek()<=week&&kc.getKcEndWeek()>=week){
                if(kc.isKcIsSingle()&&week%2==1){
                    for(int j=kc.getKcStartTime();j<=kc.getKcEndTime();j++) {
                        mark[kc.getKcWeekend()][j][week]=1;
                    }
                    courseInfo.setBackgroundResource(background[kc.getKcBackgroundId()]);
                    courseInfo.getBackground().setAlpha(200);

                } else if(kc.isKcIsDouble() && week % 2 == 0){
                    for(int j=kc.getKcStartTime();j<=kc.getKcEndTime();j++) {
                        mark[kc.getKcWeekend()][j][week]=1;
                    }
                    courseInfo.setBackgroundResource(background[kc.getKcBackgroundId()]);
                    courseInfo.getBackground().setAlpha(200);
                }
            }
            courseInfo.setTextColor(Color.WHITE);
            if(kc.getKcStartWeek()>week||kc.getKcEndWeek()<week||(!kc.isKcIsDouble()&&week%2==0)||(!kc.isKcIsSingle()&&week%2==1)){
                int flag=0;
                for(int j=kc.getKcStartTime();j<=kc.getKcEndTime();j++) {
                    if (mark[kc.getKcWeekend()][j][week] == 1) {
                        flag=1;
                        break;
                    }
                    mark[kc.getKcWeekend()][j][week]=1;
                }
                if (flag == 1) {
                    continue;
                }

                courseInfo.setBackgroundResource(R.drawable.course_bg0);
                courseInfo.getBackground().setAlpha(200);
                courseInfo.setTextColor(Color.GRAY);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                courseInfo.setElevation(12.0f);
            }
            courseInfo.setTextSize(12);
            courseInfo.setLayoutParams(rlp);
            courseBeanMap.put(courseInfo.getId(),kc);
            course_table_layout.addView(courseInfo);
        }
    }

    @Override
    public void showDrawerInfo(DefaultConfig defaultConfig) {
        TextView headerNameText = (TextView) drawer.findViewById(R.id.nav_header_name);
        TextView headerWeekText= (TextView) drawer.findViewById(R.id.nav_header_week);
        TextView headerXnText= (TextView) drawer.findViewById(R.id.nav_header_xuenian);
        headerNameText.setText(defaultConfig.getUserName());
        headerWeekText.setText("第 "+defaultConfig.getNowWeek()+" 周");
        headerXnText.setText(defaultConfig.getCurYear()+"年"+defaultConfig.getCurXuenian()+"学期");
    }

    @Override
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

    @Override
    public void showOptionPicker(final ArrayList<String> options) {
        OptionsPickerView pickerView=new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Log.i(TAG, options.get(options1));
                coursePresenter.getHistoryCourse(options.get(options1));
                DefaultConfig.get().setCurXuenian(Integer.parseInt(options.get(options1).substring(4,6)));
                DefaultConfig.get().setCurYear(Integer.parseInt(options.get(options2).substring(0,4)));
            }
        }).build();
        pickerView.setPicker(options);
        pickerView.show(true);
    }

    @Override
    public void showWeekPicer(final List<String> weeks) {
        OptionsPickerView pickerView=new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                int week=Integer.parseInt(weeks.get(options1).replaceAll("\\D*(\\d*)\\D*","$1"));
                Log.i(TAG, "第"+week+"周");
                DefaultConfig.get().setNowWeek(week);
                niceSpinner.setSelectedIndex(options1);
                coursePresenter.showCourse();
            }
        }).build();
        pickerView.setPicker(weeks);
        pickerView.show(true);
    }


    @Override
    public void finishGetCourse(DefaultConfig defaultConfig,ArrayList<String> options,boolean isHistoryCourse) {
        if (!isHistoryCourse) {
            moreButton.setOnClickListener(fragment);
            niceSpinner.setSelectedIndex(defaultConfig.getNowWeek()-1);
            coursePresenter.showCourse();
            showDrawerInfo(defaultConfig);
            showLoading(false);
            String content="加载"+defaultConfig.getCurYear()+"学年"+defaultConfig.getCurXuenian()+"学期课表数据完成";
            Snackbar.make(layout,content,Snackbar.LENGTH_SHORT).show();
        }else {
            niceSpinner.setSelectedIndex(defaultConfig.getNowWeek()-1);
            coursePresenter.showCourse();
            String content="切换至"+defaultConfig.getCurYear()+"学年"+defaultConfig.getCurXuenian()+"学期";
            Snackbar.make(layout,content,Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public Context getParentActivity() {
        return getActivity();
    }

    public void popupWindow(int viewId,CourseBean courseBean){
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha =0.5f; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha =1.0f; //0.0-1.0
                getActivity().getWindow().setAttributes(lp);
            }
        });
        popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
        View contentView=popupWindow.getContentView();
        TranslateAnimation translateAnimation=new TranslateAnimation(1.0f,1.0f,3.0f,1.0f);
        translateAnimation.setDuration(400l);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        LinearLayout titleLayout = (LinearLayout) contentView.findViewById(R.id.course_detail_title_layout);
        TextView titleText = (TextView) contentView.findViewById(R.id.course_detail_title);
        TextView teachText= (TextView) contentView.findViewById(R.id.course_detail_teacher);
        TextView roomText = (TextView) contentView.findViewById(R.id.course_detail_room);
        TextView jieshuText = (TextView) contentView.findViewById(R.id.course_detail_time);
        TextView weekText = (TextView) contentView.findViewById(R.id.course_detail_week);
        TextView noteText= (TextView) contentView.findViewById(R.id.course_detail_note);
        titleText.setText(courseBean.getKcName());
        teachText.startAnimation(translateAnimation);
        teachText.setText(courseBean.getTeacher());
        roomText.setText(courseBean.getKcLocation());
        teachText.startAnimation(translateAnimation);
        jieshuText.setText(courseBean.getKcStartTime()+" - "+courseBean.getKcEndTime());
        weekText.setText(courseBean.getKcStartWeek()+" - "+courseBean.getKcEndWeek());
        noteText.setText(courseBean.getKcNote());

        titleLayout.setBackgroundResource(background[courseBean.getKcBackgroundId()]);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.0f,1.5f,1.0f,0.5f,0.5f);
        scaleAnimation.setDuration(700l);
        titleLayout.startAnimation(scaleAnimation);
    }


    @Override
    public void setPresenter(CourseTableContact.CoursePresenter presenter) {
        this.coursePresenter=presenter;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.more_button_in_course_table:
                coursePresenter.addOptionPicker();
                break;
            case R.id.menu_button_in_course_table:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.course_table_loading_layout:
                Log.i(TAG, "onClick: loading中");
                break;
            case R.id.fab_sheet_item_refresh:
                coursePresenter.getCurrentCourse();
                materialSheetFab.hideSheet();
                break;
            case R.id.fab_sheet_item_setting:
                coursePresenter.addWeekPicker();
                materialSheetFab.hideSheet();
                break;
            case R.id.fab_sheet_item_date:
                coursePresenter.addOptionPicker();
                materialSheetFab.hideSheet();
                break;
            case R.id.fab_sheet_item_create:
                materialSheetFab.hideSheet();
                break;
            default:
                coursePresenter.addPoupWindow(view.getId());
        }
    }
}
