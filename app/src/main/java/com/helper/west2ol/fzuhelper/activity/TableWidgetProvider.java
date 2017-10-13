package com.helper.west2ol.fzuhelper.activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.FDScoreLB;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.presenter.CourseTablePresenterImpl;
import com.helper.west2ol.fzuhelper.util.CalculateUtil;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.FzuCookie;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;
import com.helper.west2ol.fzuhelper.util.SaveObjectUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TableWidgetProvider extends AppWidgetProvider {

    private static final String TAG="AppWidgetProvider";

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
    private Context context;
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
//    protected RelativeLayout course_table_layout;
    /** 屏幕宽度 **/
    protected int screenWidth;
    /** 课程格子平均宽度 **/
    protected int aveWidth;
    RelativeLayout course_table_layout;
    RelativeLayout root_layout;
    LinearLayout week_layout;
    private View view;

    private int yearpre=2016;
    private int weekpre=1;
    private int xuenianpre=1;
    private ImageView menuIcon;
    private ImageView accountIcon;
    private DrawerLayout mDrawerLayout;
    Button menu_button_in_course_table;

    private List<CourseBean> courseBeans;

    private int leftWidth=0;//第一列(课表序号列)所占宽度


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        this.context=context;
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG,"桌面插件刷新数据");
        DBManager dbManager = new DBManager(context);
        courseBeans=dbManager.queryCourseBeanList();
        Log.i(TAG,"桌面插件刷新数据:课程数"+courseBeans.size());
        this.context=context;
        /** 课程表body部分布局 */
        SaveObjectUtils saveObjectUtils=new SaveObjectUtils(context,"config");
        DefaultConfig defaultConfig=saveObjectUtils.getObject("config",DefaultConfig.class);
        int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            course_table_layout= (RelativeLayout) layoutInflater.inflate(R.layout.provider_course_layout,null);
            root_layout= (RelativeLayout) course_table_layout.findViewById(R.id.provieder_root_layout);
            week_layout= (LinearLayout) course_table_layout.findViewById(R.id.weekday_layout);
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) week_layout.getLayoutParams();
            params.width=dip2px(context,256);
            week_layout.setLayoutParams(params);
            if (defaultConfig == null) {
                return;
            }
            Log.i(TAG, "onUpdate: Week:"+defaultConfig.getNowWeek()+" year"+defaultConfig.getCurYear()+defaultConfig.getCurXuenian());
            showKB(defaultConfig.getNowWeek(),defaultConfig.getCurYear(),defaultConfig.getCurXuenian());
            course_table_layout.measure(0,0);
            course_table_layout.layout(0,0,0,0);
            Bitmap bitmap=Bitmap.createBitmap(loadBitmapFromView(context,course_table_layout));
            if (bitmap==null)Log.i("MAIN","NULL Image");
            views.setImageViewBitmap(R.id.widget_image,bitmap);

            Intent intent = new Intent(context, FlashActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            views.setOnClickPendingIntent(R.id.widget_image, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    static void updateWidget(Context context, AppWidgetProvider provider, int[] appWidgetIds) {

    }

    public static Bitmap loadBitmapFromView(Context context,View v) {
        if (v == null) {
            return null;
        }
        v.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        v.draw(c);
        return screenshot;
    }
    

    private void initKB(View v){
        //屏幕宽度
        int width =dip2px(context,250);
        int height = dip2px(context,320);
        //平均宽度
        int aveWidth = width / 7;
        //第一个空白格子设置为25宽
        this.screenWidth = width;
        this.aveWidth = aveWidth;

        int gridHeight = height / 12;
        for(int i = 1; i <= 12; i ++) {
            //i为行,j为列
            for (int j = 2; j <= 8; j++) {
                TextView tx = new TextView(context);
                tx.setId((i - 1) * 8 + j);
                //除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                //相对布局参数
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth * 33 / 32 + 1, gridHeight);
                //文字对齐方式
                tx.setGravity(Gravity.CENTER);
                if (j == 1) {
                    tx.setText(i+"");
                    rp.width = aveWidth * 3 / 4;
                    //设置他们的相对位置
                    if (i == 1)
                        rp.addRule(RelativeLayout.BELOW, empty.getId());
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                }
                tx.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                tx.setLayoutParams(rp);
                root_layout.addView(tx);
            }
        }
    }

    public void showKB(int week, int year, int xuenian){
        root_layout.removeAllViews();
        initKB(root_layout);
        DisplayMetrics dm = new DisplayMetrics();
        //屏幕宽度
        int width =dip2px(context,250);
        int height = dip2px(context,320);
        //平均宽度
        int aveWidth = width / 7;
        //第一个空白格子设置为25宽
        screenWidth = width;
        aveWidth = aveWidth;

        int gridHeight = height / 12;
        //设置课表界面
        //动态生成12 * maxCourseNum个textview
        for(int i = 1; i <= 12; i ++){
            //i为行,j为列
            for(int j = 2; j <= 8; j ++){
                TextView tx = new TextView(context);
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
                tx.setTextSize(11);
                //如果是第一列，需要设置课的序号（1 到 12）
                rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8  + j - 1);
                rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8  + j - 1);
                tx.setText("");
                tx.setLayoutParams(rp);
                root_layout.addView(tx);
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
        List<CourseBean> kcs = courseBeans;
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
            CourseBean kc= courseBeans.get(i);
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
            CourseBean kc = courseBeans.get(i);
            if(kc.getKcXuenian() !=xuenian||kc.getKcYear()!=year){
                continue;
            }
            TextView courseInfo = new TextView(context);
            String name=kc.getKcName();
            String location=kc.getKcLocation();
            if(name.length()>=13){
                name = name.substring(0, 11);
                name += "...";
            }
            courseInfo.setText(name+"\n\n"+kc.getKcLocation());
            //该textview的高度根据其节数的跨度来设置
            int timecount = Math.abs(kc.getKcEndTime()-kc.getKcStartTime()+1) ;
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(aveWidth * 31 / 32, (gridHeight - 5) * timecount );
            //textview的位置由课程开始节数和上课的时间（day of week）确定

            rlp.topMargin = 5 + (kc.getKcStartTime()- 1) * gridHeight+40;
            rlp.leftMargin = 1;
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
            courseInfo.setTextSize(8);
            courseInfo.setLayoutParams(rlp);

            //设置不透明度
            root_layout.addView(courseInfo);
        }
    }


    private void initKB2(View v){
        root_layout.removeAllViews();
        DisplayMetrics dm = new DisplayMetrics();
        int width =dip2px(context,250);
        int height = dip2px(context,320);
        //平均宽度
        int aveWidth = width / 7;
        //第一个空白格子设置为25宽
        this.screenWidth = width;
        this.aveWidth = aveWidth;
        for (int i=1;i<=7;i++) {
            //相对布局参数
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            rp.leftMargin=(i-1)*aveWidth- CalculateUtil.dp2px(context,2);
            rp.topMargin =5;

            TextView tx = new TextView(context);
            tx.setGravity(Gravity.CENTER);
            tx.setId((i - 1) * 8  + 1);
            tx.setSingleLine(true);
            //设置周数的序号（星期一到星期日）
            tx.setText("周"+CalculateUtil.getWeekChinese(i));
            tx.setTextSize(12);
            //设置他们的相对位置
            tx.setTextColor(context.getResources().getColor(R.color.colorBlack));
            tx.setLayoutParams(rp);
            root_layout.addView(tx);
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
            rp.leftMargin=(i-1)*aveWidth-CalculateUtil.dp2px(context,2);
            rp.addRule(RelativeLayout.BELOW,(i - 1) * 8  + 1);
            TextView tx = new TextView(context);
            tx.setGravity(Gravity.CENTER);
            tx.setSingleLine(true);
            //设置周数的序号（星期一到星期日）
            tx.setText(month+"-"+day);
            tx.setTextSize(12);
            //设置他们的相对位置
            tx.setTextColor(context.getResources().getColor(R.color.colorBlack));
            tx.setLayoutParams(rp);
            root_layout.addView(tx);
        }

    }

    /**
     * 显示课表
     * @param week 星期
     * @param year 年份
     * @param xuenian 学年 1或 2
     */
    private void showKB2(List<CourseBean> kcs,int week, int year, int xuenian){
        leftWidth=CalculateUtil.dp2px(context,20);
        if (root_layout != null) {
            root_layout.removeAllViews();
        }
        initKB2(view);
        //屏幕宽度
        int width =dip2px(context,250);
        int height = dip2px(context,320);
        //平均宽度
        int aveWidth = (width) / 7;
        //第一个空白格子设置为25宽
        int gridHeight = height / 11;

        //设置课表界面
        //动态生成11 * maxCourseNum个textview
        for(int i = 1; i <= 11; i ++){
            TextView tx = new TextView(context);
            tx.setId((i - 1) * 8  + 1);
            //相对布局参数
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth, gridHeight);
            tx.setGravity(Gravity.CENTER);
//          设置课的序号（1 到 12）
            tx.setText(i+"");
            rp.width = leftWidth;
            //设置他们的相对位置
            if (i != 1)
                rp.addRule(RelativeLayout.BELOW, (i - 2) * 8+1);
            if((i-1)%4==0&&i>=2){
                rp.topMargin=gridHeight/4;
            }
            tx.setTextColor(context.getResources().getColor(R.color.colorBlack));
            tx.setLayoutParams(rp);
            root_layout.addView(tx);
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
            TextView courseInfo = new TextView(context);
            String name=kc.getKcName();

            if(name.length()>=13){
                name = name.substring(0, 11);
                name += "...";
            }
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
            root_layout.addView(courseInfo);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}