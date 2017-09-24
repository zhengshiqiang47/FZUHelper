package com.helper.west2ol.fzuhelper.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.CourseBeanLab;
import com.helper.west2ol.fzuhelper.bean.FDScoreLB;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.FzuCookie;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;
import com.helper.west2ol.fzuhelper.util.HttpUtil;
import com.helper.west2ol.fzuhelper.util.SaveObjectUtils;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CourseTableFragment extends Fragment{

    private static final String TAG = "KBActivity";
    /** 第一个无内容的格子 */
    protected TextView empty;
    /** 星期一的格子 */
    protected TextView monColum;
    /** 星期二的格子 */
    protected TextView tueColum;
    /** 星期三的格子 */
    protected TextView wedColum;
    /** 星期四的格子 */
    protected TextView thrusColum;
    /** 星期五的格子 */
    protected TextView friColum;
    /** 星期六的格子 */
    protected TextView satColum;
    /** 星期日的格子 */
    protected TextView sunColum;
    /** 课程表body部分布局 */
    protected RelativeLayout course_table_layout;
    /** 屏幕宽度 **/
    protected int screenWidth;
    /** 课程格子平均宽度 **/
    protected int aveWidth;

    private int yearpre=2016;
    private int weekpre=1;
    private int xuenianpre=1;
    private View view;
    private ImageView menuIcon;
    private ImageView accountIcon;
    private DrawerLayout mDrawerLayout;
    Button menu_button_in_course_table;
    @Bind(R.id.more_button_in_course_table)
    Button moreButton;
    @Bind(R.id.course_table_spinner)
    Spinner spinner;
//    @Bind(R.id.course_table_myscrollview)
//    TwinklingRefreshLayout refreshLayout;

    PopupWindow popupWindow;
    DrawerLayout drawer;
    private boolean isRefresh=false;

    private int leftWidth=0;//第一列所占宽度

    Map<Integer,CourseBean> courseBeanMap;

    SaveObjectUtils saveObjectUtils;

    @Override
    public void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedIntanceState){
        View rootView = inflater.inflate(R.layout.fragment_course_table , container , false);
        view=rootView;
        ButterKnife.bind(this, rootView);
        saveObjectUtils=new SaveObjectUtils(getActivity(),"config");
        FloatingActionButton add = (FloatingActionButton) rootView.findViewById(R.id.more_button_in_coursetable);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CourseTable" , "onClick");
                // add onClick
            }
        });
        drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        menu_button_in_course_table = (Button)rootView.findViewById(R.id.menu_button_in_course_table);
        menu_button_in_course_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//                Observable.create(new Observable.OnSubscribe<Object>() {
//                    @Override
//                    public void call(Subscriber<? super Object> subscriber) {
//                        HtmlParseUtil.getHistoryCourse(getActivity(),"201602");
//                        subscriber.onCompleted();
//                    }
//                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber() {
//                    @Override
//                    public void onCompleted() {
//                        showKB(1, 2016, 2);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//
//                    }
//                });
            }
        });
        if (CourseBeanLab.get(this.getActivity()).getCourses() == null||CourseBeanLab.get(this.getActivity()).getCourses().size()<=1) {
            getCourse();
        }
        initData();
        initView();
        Log.i("CourseTable", "初始化完成");
        return rootView;
    }

    private void initView(){
        popupWindow= new PopupWindow(getActivity());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.poup_course_detail, null));
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.PoupAnimation);
    }

    private void getCourse(){
        Log.i(TAG,"getCourse");
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                HtmlParseUtil.getCurrentCourse(getActivity().getApplicationContext(),false);
                HtmlParseUtil.getBeginDate(null);
                HtmlParseUtil.getDate();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber() {
            @Override
            public void onCompleted() {
                DefaultConfig defaultConfig=DefaultConfig.get();
                FzuCookie fzuCookie=FzuCookie.get();
                saveObjectUtils.setObject("config", defaultConfig);
                saveObjectUtils.setObject("cookie",fzuCookie);
                Log.i(TAG,defaultConfig.getCurYear()+" "+defaultConfig.getCurXuenian()+" "+defaultConfig.getNowWeek()+" "+defaultConfig.getUserAccount());
                spinner.setSelection(defaultConfig.getNowWeek()-1);
                showKB(defaultConfig.getNowWeek(), defaultConfig.getCurYear(), defaultConfig.getCurXuenian());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }




    private void initData(){

        List<String> weeks = new ArrayList<>();
        for (int i=0;i<22;i++) {
            weeks.add("第 "+(i+1)+" 周");
        }
        courseBeanMap = new HashMap<>();
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(getActivity(), R.layout.item_week_spinner_show, weeks);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(DefaultConfig.get().getNowWeek());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("position:"+position);
                DefaultConfig.get().setNowWeek(position);
                showKB(position+1,DefaultConfig.get().getCurYear(),DefaultConfig.get().getCurXuenian());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void initKB(View v){


        empty = (TextView) v.findViewById(R.id.test_empty);
        monColum = (TextView) v.findViewById(R.id.test_monday_course);
        tueColum = (TextView) v.findViewById(R.id.test_tuesday_course);
        wedColum = (TextView) v.findViewById(R.id.test_wednesday_course);
        thrusColum = (TextView) v.findViewById(R.id.test_thursday_course);
        friColum = (TextView) v.findViewById(R.id.test_friday_course);
        satColum  = (TextView) v.findViewById(R.id.test_saturday_course);
        sunColum = (TextView) v.findViewById(R.id.test_sunday_course);
        course_table_layout = (RelativeLayout) v.findViewById(R.id.test_course_rl);
        int curWeek=DefaultConfig.get().getNowWeek();
        long beginDate=DefaultConfig.get().getBeginDate();
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(beginDate);
        calendar.add(Calendar.WEEK_OF_MONTH,+curWeek-1);
        System.out.println("");
//        calendar.add(Calendar.DAY_OF_WEEK,-calendar.get(Calendar.DAY_OF_WEEK)+1);
        //设置课表对应日期，这样写万不得已，有空重构！！！
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        monColum.setText(month+"-"+day);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        tueColum.setText(month+"-"+day);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        wedColum.setText(month+"-"+day);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        thrusColum.setText(month+"-"+day);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        friColum.setText(month+"-"+day);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        satColum.setText(month+"-"+day);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
        sunColum.setText(month+"-"+day);
        SinaRefreshView refreshView = new SinaRefreshView(getActivity());
//        refreshLayout.setHeaderView(refreshView);
//        refreshLayout.setOnRefreshListener(new TwinklingRefreshLayout.OnRefreshListener(){
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                refreshLayout.finishRefreshing();
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });
//        refreshLayout.setEnableLoadmore(false);
//        refreshLayout.setEnableOverlayRefreshView(true);
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                R.color.colorRed,
//                R.color.colorAccent,
//                R.color.colorBackground);
//        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //屏幕宽度
        int width = dm.widthPixels;
        //平均宽度
        int aveWidth = width / 7;
        //第一个空白格子设置为25宽
        this.screenWidth = width;
        this.aveWidth = aveWidth;

        int height = dm.heightPixels;
        int gridHeight = height / 12;
        for(int i = 1; i <= 12; i ++) {
            //i为行,j为列
            for (int j = 1; j <= 8; j++) {
                TextView tx = new TextView(getActivity().getApplicationContext());
                tx.setId((i - 1) * 8 + j);
                //除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                //相对布局参数
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth * 33 / 32 + 1, gridHeight);
                //文字对齐方式
                tx.setGravity(Gravity.CENTER);
                if (j == 1) {
                    tx.setText(i+"");
                    rp.width = aveWidth * 2 / 5;

                    leftWidth=rp.width;
                    //设置他们的相对位置
                    if (i == 1)
                        rp.addRule(RelativeLayout.BELOW, empty.getId());
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                    if((i-1)%4==0&&i>=2){
                        rp.topMargin=gridHeight/4;
                    }
                }
                tx.setTextColor(getResources().getColor(R.color.colorBlack));
                tx.setLayoutParams(rp);
                course_table_layout.addView(tx);
            }
        }
    }

    /**
     * 显示课表
     * @param week 星期
     * @param year 年份
     * @param xuenian 学年 1或 2
     */
    public void showKB(int week, int year, int xuenian){
        if (course_table_layout != null) {
            course_table_layout.removeAllViews();
        }
        initKB(view);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //屏幕宽度
        int width = dm.widthPixels;
        //平均宽度
        int aveWidth = (width-leftWidth-50) / 7;
        //第一个空白格子设置为25宽
        screenWidth = width;
        aveWidth = aveWidth;
        int height = dm.heightPixels;

        int gridHeight = height / 12;
        //设置课表界面
        //动态生成12 * maxCourseNum个textview
        for(int i = 1; i <= 12; i ++){
            //i为行,j为列
            for(int j = 2; j <= 8; j ++){
                TextView tx = new TextView(getActivity().getApplicationContext());
                tx.setId((i - 1) * 8  + j);
                //除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
//                if(j < 8)
//                    tx.setBackgroundDrawable(this.
//                            getResources().getDrawable(R.drawable.course_text_view_bg));
//                else
//                    tx.setBackgroundDrawable(this.
//                            getResources().getDrawable(R.drawable.course_table_last_colum));
                //相对布局参数
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth * 33 / 32 + 1, gridHeight);
                //文字对齐方式
                tx.setGravity(Gravity.CENTER);
                //如果是第一列，需要设置课的序号（1 到 12）
                if (j == 1) {
                    tx.setText(i+"");
                    rp.width = aveWidth * 3 / 4;
                    //设置他们的相对位置
                    if (i == 1)
                        rp.addRule(RelativeLayout.BELOW, empty.getId());
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                }
                rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8  + j - 1);
                rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8  + j - +1);
                tx.setLayoutParams(rp);
                course_table_layout.addView(tx);
            }
        }
        //课表颜色背景
        int[] background = {
                R.drawable.course_bg1,
                R.drawable.course_bg2,
                R.drawable.course_bg3,
                R.drawable.course_bg4,
                R.drawable.course_bg5,
                R.drawable.course_bg6,
                R.drawable.course_bg7,
                R.drawable.course_bg8,
                R.drawable.course_bg9,
                R.drawable.course_bg10,
                R.drawable.course_bg11,
                R.drawable.course_bg12,
                R.drawable.course_bg13,
                R.drawable.course_bg14,
                R.drawable.course_bg15,
                R.drawable.course_bg16,
                R.drawable.course_bg1,
                R.drawable.course_bg2,
                R.drawable.course_bg3,
                R.drawable.course_bg4,
                R.drawable.course_bg5,
                R.drawable.course_bg6,
                R.drawable.course_bg7,
                R.drawable.course_bg8,
                R.drawable.course_bg9,
                R.drawable.course_bg10,
                R.drawable.course_bg11,
                R.drawable.course_bg12,
                R.drawable.course_bg13,
                R.drawable.course_bg14,
                R.drawable.course_bg15,
                R.drawable.course_bg16,
        };

        // 添加课程信息
        ArrayList<CourseBean> kcs = CourseBeanLab.get(getActivity().getApplicationContext()).getCourses();
        Log.i(TAG, "课程数" + kcs.size());
        int[][][] mark=new int[8][13][26];
        for(int j=0;j<8;j++) {
            for(int k=0;k<13;k++) {
                for(int l=0;l<26;l++) {
                    mark[j][k][l]=0;
                }
            }
        }

        List<CourseBean> courseBeen=CourseBeanLab.get(getActivity().getApplicationContext()).getCourses();
        for (int i=0;i<kcs.size();i++) {
            CourseBean kc= courseBeen.get(i);
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
            CourseBean kc= courseBeen.get(i);
            if(kc.getKcXuenian() !=xuenian||kc.getKcYear()!=year){
                continue;
            }
            final TextView courseInfo = new TextView(getActivity());
            String name=kc.getKcName();
            String location=kc.getKcLocation();
            if(name.length()>=13){
                name = name.substring(0, 11);
                name += "...";
            }
            courseInfo.setText(name+"\n\n"+kc.getKcLocation());
            //该textview的高度根据其节数的跨度来设置
            int timecount = Math.abs(kc.getKcEndTime()-kc.getKcStartTime()+1) ;
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(aveWidth * 31 / 32, (gridHeight - 5) * timecount +timecount/4*5);
            //textview的位置由课程开始节数和上课的时间（day of week）确定
            rlp.topMargin = 5 + (kc.getKcStartTime()- 1) * gridHeight+(kc.getKcStartTime()/4*(gridHeight/4));
            rlp.leftMargin = 2;
            // 偏移由这节课是星期几决定
            rlp.addRule(RelativeLayout.RIGHT_OF, kc.getKcWeekend());
            //字体居中
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
            //设置不透明度
            course_table_layout.addView(courseInfo);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showKB(weekpre,yearpre,xuenianpre);
//                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
                    //swipeRefreshLayout.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    };

    private void refreshDate(){
        CourseBeanLab.get(getActivity()).getCourses().clear();
        FDScoreLB.get(getActivity()).getScores().clear();
//        KCLB.get(getActivity()).getKcs().clear();
//        FDScoreLB.get(getActivity()).getScores().clear();
//        String Xuehao = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE).getString("passwd","");
//        Log.i("KBFragment", "密码" +).getSharedPreferences("userinfo", Context.MODE_PRIVATE).getString("username", "");
//        String Passwd = getActivity( Passwd);
//        Log.i("KBFragment", "学号" + UserInformation.get(getActivity()).getXuehao());
//        HtmlAnalyze.getScore(getActivity(), Xuehao, Passwd);
        HtmlParseUtil.getCurrentCourse(getActivity().getApplicationContext(),true);
    }
}
