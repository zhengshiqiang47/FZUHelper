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
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.CourseBeanLab;
import com.helper.west2ol.fzuhelper.bean.FDScoreLB;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;

import java.util.ArrayList;

public class TableWidgetProvider extends AppWidgetProvider {

    private static final String TAG="AppWidgetProvider";
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
    private View view;

    private int yearpre=2016;
    private int weekpre=1;
    private int xuenianpre=1;
    private ImageView menuIcon;
    private ImageView accountIcon;
    private DrawerLayout mDrawerLayout;
    Button menu_button_in_course_table;


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        this.context=context;
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG,"testHere");
        this.context=context;
        /** 课程表body部分布局 */

        int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            course_table_layout= (RelativeLayout) layoutInflater.inflate(R.layout.content_course_table,null);
            showKB(1,2017,1);
            course_table_layout.measure(0,0);
            course_table_layout.layout(0,0,0,0);
            Bitmap bitmap=Bitmap.createBitmap(loadBitmapFromView(context,course_table_layout));
            if (bitmap==null)Log.i("MAIN","NULL Image");
            views.setImageViewBitmap(R.id.widget_image,bitmap);

            Intent intent = new Intent(context, MainContainerActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            views.setOnClickPendingIntent(R.id.widget_image, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
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
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                R.color.colorRed,
//                R.color.colorAccent,
//                R.color.colorBackground);
//        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
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
                course_table_layout.addView(tx);
            }
        }
    }

    public void showKB(int week, int year, int xuenian){
        course_table_layout.removeAllViews();
        initKB(course_table_layout);
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
        ArrayList<CourseBean> kcs = CourseBeanLab.get(context).getCourses();
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
            CourseBean kc= CourseBeanLab.get(context).getCourses().get(i);
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

            CourseBean kc= CourseBeanLab.get(context).getCourses().get(i);
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
                    Toast.makeText(context, "刷新成功", Toast.LENGTH_SHORT).show();
                    //swipeRefreshLayout.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    };

    private void refreshDate(){
        CourseBeanLab.get(context).getCourses().clear();
        FDScoreLB.get(context).getScores().clear();
        HtmlParseUtil.getCurrentCourse(context);
    }

    private class getCourse extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HtmlParseUtil.getCurrentCourse(context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            showKB(2, 2017, 1);
            Log.i(TAG, "显示课表");

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