package com.helper.west2ol.fzuhelper.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.helper.west2ol.fzuhelper.BuildConfig;
import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.CourseBean;
import com.helper.west2ol.fzuhelper.bean.CourseBeanLab;
import com.helper.west2ol.fzuhelper.bean.User;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.dao.DaoMaster;
import com.helper.west2ol.fzuhelper.fragment.CourseTableFragment;
import com.helper.west2ol.fzuhelper.fragment.EmptyRoomFragment;
import com.helper.west2ol.fzuhelper.fragment.ExamFragment;
import com.helper.west2ol.fzuhelper.fragment.GradeFragment;
import com.helper.west2ol.fzuhelper.fragment.MathFragment;
import com.helper.west2ol.fzuhelper.fragment.YibanFragment;
import com.helper.west2ol.fzuhelper.util.ActivityController;
import com.helper.west2ol.fzuhelper.util.DateUtil;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.DownloadAPK;
import com.helper.west2ol.fzuhelper.util.FzuCookie;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;
import com.helper.west2ol.fzuhelper.util.HttpUtil;
import com.helper.west2ol.fzuhelper.util.SaveObjectUtils;
import com.helper.west2ol.fzuhelper.util.StatusBarCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainContainerActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG="MainActivity";
    private static  String VERSION="1.0";
    public static final int TABLE_FRAGMENT=0;
    public static final int SCORE_FRAGMENT=1;
    public static final int EXAM_FRAGMENT=2;
    public static final int YIBAN_FRAGMENT=3;
    public static final int EMPTY_ROOM_FRAGMENT=4;


    public CourseTableFragment courseTableFragment;
    GradeFragment gradeFragment;
    MathFragment mathFragment;
    ExamFragment examFragment;
    YibanFragment yibanFragment;
    EmptyRoomFragment emptyRoomFragment;

    private Fragment current;
    NavigationView navigationView;
    MenuItem course_Item;
    private String id;
    SaveObjectUtils saveObjectUtils;
    private Activity activity;

    Bundle parameterToFragment;
    int shortCut=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        activity=this;
        Fabric.with(this, new Crashlytics());
        requestPermission();
        checkVersion();
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
        current = courseTableFragment;
        shortCut=getIntent().getIntExtra("shortcut",0);
        Log.i(TAG, "onCreate: shortCut:" + shortCut);
        shortcutView(shortCut);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        course_Item = navigationView.getMenu().getItem(shortCut);
        course_Item.setChecked(true);
    }

    private void initData(){
        DBManager dbManager=DBManager.getInstance(getApplicationContext());
        if (DefaultConfig.get().getBeginDate() == 0) {
            DefaultConfig defaultConfig=saveObjectUtils.getObject("config",DefaultConfig.class);
            DefaultConfig config=DefaultConfig.get();
            if (defaultConfig != null) {
                config.setBeginDate(defaultConfig.getBeginDate());
                config.setUserAccount(defaultConfig.getUserAccount());
                config.setUserName(defaultConfig.getUserName());
                //打开应用时重新计算周数
                int week= DateUtil.getWeeks(defaultConfig.getBeginDate(),System.currentTimeMillis());
                config.setNowWeek(week);
                config.setCurXuenian(defaultConfig.getCurXuenian());
                config.setCurYear(defaultConfig.getCurYear());
                config.setXqValues(defaultConfig.getXqValues());
                config.setOptions(defaultConfig.getOptions());
                config.setLogin(defaultConfig.isLogin());
            }
        }
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
    }


    private void shortcutView(int shortcut){
        switch (shortcut) {
            case SCORE_FRAGMENT:
                parameterToFragment.putString("id",id);
                if (gradeFragment == null) {
                    gradeFragment = new GradeFragment();
                }
               current=gradeFragment;
                break;
            case EXAM_FRAGMENT:
                if (examFragment == null) {
                    examFragment = new ExamFragment();
                }
                current = examFragment;
                break;
            case YIBAN_FRAGMENT:
                if (yibanFragment == null) {
                    yibanFragment = new YibanFragment();
                }
                current=yibanFragment;
                break;
            case EMPTY_ROOM_FRAGMENT:
                if (emptyRoomFragment == null) {
                    emptyRoomFragment = new EmptyRoomFragment();
                }
                current = emptyRoomFragment;
                break;
        }

    }

    private boolean isFirst=true;
    //界面渲染完成回调
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (isFirst&&hasFocus) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container , current)
                    .commit();
            isFirst=false;
        }
        super.onWindowFocusChanged(hasFocus);

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
        Log.i(TAG,"onDestroy");
        DefaultConfig defaultConfig = DefaultConfig.get();
        FzuCookie fzuCookie = FzuCookie.get();
        SaveObjectUtils saveObjectUtils = new SaveObjectUtils(this,"config");
        saveObjectUtils.setObject("config", defaultConfig);
        saveObjectUtils.setObject("cookie",fzuCookie);
        super.onDestroy();
//        CourseBeanLab.get(this).getCourses().clear();
//        ActivityController.removeActivity(this);
    }

    @Override
    public void onResume(){
        if (shortCut != 0) {
            shortcutView(shortCut);
        }
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
                break;
            case R.id.item2:
                parameterToFragment.putString("id",id);
                if (gradeFragment == null) {
                    gradeFragment = new GradeFragment();
                }
                switchFragment(current,gradeFragment);
                break;
            case R.id.item3:
                if (examFragment == null) {
                    examFragment = new ExamFragment();
                }
                switchFragment(current,examFragment);
                break;
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
            case R.id.item6:
                if (emptyRoomFragment == null) {
                    emptyRoomFragment = new EmptyRoomFragment();
                }
                switchFragment(current,emptyRoomFragment);
                break;
            case R.id.item8:
                parameterToFragment.putString("id",id);
                Intent intent_9 = new Intent(MainContainerActivity.this,SettingActivity.class);
                startActivity(intent_9);
                break;
            case R.id.item9:
                logout();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        AlertDialog.Builder logoutDialog =
                new AlertDialog.Builder(MainContainerActivity.this);
        logoutDialog.setIcon(R.drawable.icon_f);
        logoutDialog.setTitle("注销");
        logoutDialog.setMessage("确定退出当前账号?");
        logoutDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager dbManager=new DBManager(MainContainerActivity.this);
                        List<User> users=dbManager.queryUserList();
                        for (User user:users){
                            if (user.getFzuAccount().equals(DefaultConfig.get().getUserAccount())) {
                                user.setIsLogin(false);
                                dbManager.updateUser(user);
                                break;
                            }
                        }
                        dbManager.dropCourseBeans();
                        dbManager.dropFDScores();
                        SaveObjectUtils saveObjectUtils=new SaveObjectUtils(getApplicationContext(),"config");
                        DefaultConfig config=saveObjectUtils.getObject("config", DefaultConfig.class);
                        config.setUserAccount("");
                        saveObjectUtils.setObject("config", null);
                        saveObjectUtils.setObject("cookie", null);
                        CourseBeanLab.get(MainContainerActivity.this).getCourses().clear();
                        ActivityController.finashAll();
                        Intent intent = new Intent(MainContainerActivity.this , LoginActivity_1.class);
                        startActivity(intent);
                        finish();
                    }
                });;
        logoutDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        logoutDialog.show();
    }

    private void switchFragment(Fragment from, Fragment to){
        if(current !=to){
            if(!to.isAdded()){
                getSupportFragmentManager().beginTransaction().add(R.id.main_container,to).hide(from).show(to).commit();
            }else {
                getSupportFragmentManager().beginTransaction().hide(from).show(to).commit();
            }
            current=to;
        }

    }

    public void requestPermission(){
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this,"please give me the permission",Toast.LENGTH_SHORT).show();
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

    private void checkVersion(){
        Log.i(TAG, "checkVersion:检测版本号");
        Observable.create(new Observable.OnSubscribe<Object>() {

            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    HttpUtil.Version version=HttpUtil.getVeison();
                    if (version != null) {
                        subscriber.onNext(version);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {
                        final HttpUtil.Version version=(HttpUtil.Version)o;
                        DefaultConfig.get().setNewVersionUrl(version.getUrl());
                        String versionStr=version.getVersion();
                        if (!versionStr.equals(VERSION)){
                            Log.i(TAG, "onNext: 版本不对应"+"远程服务器版本"+versionStr);
                            AlertDialog.Builder normalDialog =
                                    new AlertDialog.Builder(MainContainerActivity.this);
                            normalDialog.setIcon(R.drawable.icon_f);
                            normalDialog.setTitle("检测到新版本");
                            normalDialog.setMessage("是否更新"+versionStr+"版本?\n(由于此是beta版本，使用强制更新策略，这意味着你不更新无法使用这个版本^-^)\n如遇安装时闪退请检查是否给予运行时权限");
                            normalDialog.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DownloadAPK.getInstance(version.getUrl()).download(activity);
                                        }
                                    });
                            normalDialog.setNegativeButton("退出",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).setCancelable(false);
                            normalDialog.show();
                        }

                    }
                });
    }


}
