package com.helper.west2ol.fzuhelper.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.CourseBeanLab;
import com.helper.west2ol.fzuhelper.bean.User;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.dao.DaoMaster;
import com.helper.west2ol.fzuhelper.fragment.CourseTableFragment;
import com.helper.west2ol.fzuhelper.fragment.GradeFragment;
import com.helper.west2ol.fzuhelper.fragment.MathFragment;
import com.helper.west2ol.fzuhelper.fragment.YibanFragment;
import com.helper.west2ol.fzuhelper.util.ActivityController;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;
import com.helper.west2ol.fzuhelper.util.HttpUtil;
import com.helper.west2ol.fzuhelper.util.SaveObjectUtils;
import com.helper.west2ol.fzuhelper.util.StatusBarCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainContainerActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG="MainActivity";

    public CourseTableFragment courseTableFragment;
    GradeFragment gradeFragment;
    MathFragment mathFragment;
    YibanFragment yibanFragment;

    private Fragment current;
    NavigationView navigationView;
    MenuItem course_Item;
    private String id;
    SaveObjectUtils saveObjectUtils;

    Bundle parameterToFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        ActivityController.addActivity(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        saveObjectUtils=new SaveObjectUtils(this,"config");
        initData();
        id = getIntent().getStringExtra("id");
        parameterToFragment = new Bundle();
        parameterToFragment.putString("id",id);
        courseTableFragment = new CourseTableFragment();
        courseTableFragment.setArguments(parameterToFragment);
        getFragmentManager().beginTransaction()
                .replace(R.id.main_container , courseTableFragment)
                .commit();
        current = courseTableFragment;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        course_Item = navigationView.getMenu().getItem(0);
        course_Item.setChecked(true);
    }

    private void initData(){
        DBManager dbManager=DBManager.getInstance(getApplicationContext());
        List<User> resUsers=dbManager.queryUserList();
        if (resUsers != null && resUsers.size() >= 1) {
            System.out.println("resUserCount:" + resUsers.size());
            System.out.println(resUsers.get(0).getFzuAccount()+" Pass:"+resUsers.get(0).getFzuPasssword());
        }
        List<CourseBean> courseBeens=dbManager.queryCourseBeanList();
        if (courseBeens != null&&CourseBeanLab.get(this.getApplicationContext()).getCourses().size()<=1) {
            Log.i(TAG, "couses:" + courseBeens.size());
            ArrayList<CourseBean> courseBeen=CourseBeanLab.get(this.getApplicationContext()).getCourses();
            for (CourseBean bean : courseBeens) {
                courseBeen.add(bean);
            }
        }
        DefaultConfig defaultConfig=saveObjectUtils.getObject("config",DefaultConfig.class);
        DefaultConfig config=DefaultConfig.get();
        if (defaultConfig != null) {
            config.setBeginDate(defaultConfig.getBeginDate());
            config.setUserAccount(defaultConfig.getUserAccount());
            config.setNowWeek(defaultConfig.getNowWeek());
            config.setCurXuenian(defaultConfig.getCurXuenian());
            config.setCurYear(defaultConfig.getCurYear());
            config.setXqValues(defaultConfig.getXqValues());
            config.setLogin(defaultConfig.isLogin());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    public void onDestroy(){
        System.out.println("onDestroy");
        DefaultConfig defaultConfig = DefaultConfig.get();
        SaveObjectUtils saveObjectUtils = new SaveObjectUtils(this,"config");
        saveObjectUtils.setObject("config", defaultConfig);
        super.onDestroy();
//        CourseBeanLab.get(this).getCourses().clear();
//        ActivityController.removeActivity(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /*
    菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_table, menu);
        return true;
    }
    /*
    菜单点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                parameterToFragment.putString("id",id);
                if (courseTableFragment == null) {
                    courseTableFragment = new CourseTableFragment();
                }
                switchFragment(current,courseTableFragment);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                }
//                courseTableFragment.setArguments(parameterToFragment);
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.main_container , courseTableFragment)
//                        .commit();
//                current=courseTableFragment;
                break;
            case R.id.item2:
                parameterToFragment.putString("id",id);
                if (gradeFragment == null) {
                    gradeFragment = new GradeFragment();
                }
                switchFragment(current,gradeFragment);

//                gradeFragment.setArguments(parameterToFragment);
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.main_container , gradeFragment)
//                        .commit();
//                current = gradeFragment;
                break;
//            case R.id.item3:
//                if (mathFragment == null) {
//                    mathFragment = new MathFragment();
//                }
//                switchFragment(current,mathFragment);
////                getFragmentManager().beginTransaction()
////                        .replace(R.id.main_container , mathFragment)
////                        .commit();
//                break;
//            case R.id.item4:
//                Intent intent4 = new Intent(MainContainerActivity.this , OtherActivity.class);
////                intent4.putExtra("id" , id);
////                startActivity(intent4);
//                break;
            case R.id.item5:
                if (yibanFragment == null) {
                    yibanFragment = new YibanFragment();
                }
                switchFragment(current,yibanFragment);
                break;
            case R.id.item8:
                parameterToFragment.putString("id",id);
                Intent intent_9 = new Intent(MainContainerActivity.this,SettingActivity.class);
                startActivity(intent_9);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchFragment(Fragment from, Fragment to){
        if(current !=to){
            if(!to.isAdded()){
                getFragmentManager().beginTransaction().add(R.id.main_container,to).hide(from).show(to).commit();
            }else {
                getFragmentManager().beginTransaction().hide(from).show(to).commit();
            }
            current=to;
        }

    }

}
