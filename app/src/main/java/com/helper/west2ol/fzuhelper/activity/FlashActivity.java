package com.helper.west2ol.fzuhelper.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.Exam;
import com.helper.west2ol.fzuhelper.bean.User;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.util.DateUtil;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.FzuCookie;
import com.helper.west2ol.fzuhelper.util.HttpUtil;
import com.helper.west2ol.fzuhelper.util.SaveObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by coderqiang on 2017/9/22.
 */

public class FlashActivity extends Activity {


    private static final String TAG="FlashActivity";
    private Context context;
    private ShortcutManager mShortcutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_flash);
        context=this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        flashLogin();
    }

    SaveObjectUtils saveObjectUtils=null;

    private void flashLogin() {
        setupShortcuts();
        saveObjectUtils=new SaveObjectUtils(this,"config");
        try {
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
            }else {
                Intent intent = new Intent(FlashActivity.this , LoginActivity_1.class);
                intent.putExtra("id" , "");
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.i(TAG, "flashLogin: 捕获到出错!");
            e.printStackTrace();
            
        }

       
        FzuCookie fzuCookie = saveObjectUtils.getObject("cookie", FzuCookie.class);
        if (fzuCookie != null) {
            FzuCookie.get(fzuCookie);
        }

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                DBManager dbManager=new DBManager(context);
                List<User> users=dbManager.queryUserList();
                if (users != null) {
                    for (User user : users) {
                        Log.i(TAG, "call: userName:"+user.getFzuAccount()+" pass:"+user.getFzuPasssword()+" isLogin:"+user.getIsLogin());
                        if (user.isLogin() == true) {
                            if (FzuCookie.get().getExpTime()>=System.currentTimeMillis()){
                                subscriber.onCompleted();
                                return;
                            }
                            DefaultConfig.get().setUserAccount(user.getFzuAccount());
                            saveObjectUtils.setObject("config",DefaultConfig.get());
                            String loginResponse=null;
                            try {
                                loginResponse = HttpUtil.Login(getApplicationContext(),user);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Fabric.with(context, new Crashlytics());
                                subscriber.onError(new RuntimeException("网路错误"));
                            }
                            subscriber.onNext(loginResponse);
//                                    for (User otherUser : users) {
//                                        if (!otherUser.getFzuAccount().equals(user.getFzuAccount())) {
//                                            otherUser.setIsLogin(false);
//                                            dbManager.updateUser(otherUser);
//                                        }
//                                    }
                            Log.i(TAG, "loginResponse:"+loginResponse);
                            switch (loginResponse){
                                case "网络错误":
                                    subscriber.onError(new RuntimeException("网路错误"));
                                    break;
                                case "密码错误":
                                case "登录失败，请检查用户名和密码是否正确!":
                                    user.setIsLogin(false);
                                    dbManager.updateUser(user);
                                    Log.i(TAG, "密码错误");
                                    break;
                                case "登录成功":
                                    subscriber.onCompleted();
                                    return;
                            }
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(FlashActivity.this , LoginActivity_1.class);
                intent.putExtra("id" , "");
                startActivity(intent);
                finish();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {

            @Override
            public void onCompleted() {
                Intent intent = new Intent(FlashActivity.this , MainContainerActivity.class);
                intent.putExtra("id" , "");
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"请检查网络连接，某些功能可能无法使用!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FlashActivity.this , MainContainerActivity.class);
                intent.putExtra("id" , "");
                startActivity(intent);
                finish();
            }

            @Override
            public void onNext(Object o) {
                Toast.makeText(getApplicationContext(), (String)o, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupShortcuts() {
        mShortcutManager = getSystemService(ShortcutManager.class);
        List<ShortcutInfo> infos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i == 1) {
                Intent intent = new Intent(this, MainContainerActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("shortcut", MainContainerActivity.EXAM_FRAGMENT);
                ShortcutInfo info = new ShortcutInfo.Builder(this, "id" + i)
                        .setShortLabel("考场")
                        .setLongLabel("考场查询" )
                        .setIcon(Icon.createWithResource(this, R.drawable.icon_exam))
                        .setIntent(intent)
                        .build();
                infos.add(info);
            } else if (i == 2) {
                Intent intent = new Intent(this, MainContainerActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("shortcut", MainContainerActivity.SCORE_FRAGMENT);
                ShortcutInfo info = new ShortcutInfo.Builder(this, "id" + i)
                        .setShortLabel("成绩")
                        .setLongLabel("成绩查询" )
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_grade))
                        .setIntent(intent)
                        .build();
                infos.add(info);
            }else if (i == 3) {
                Intent intent = new Intent(this, MainContainerActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("shortcut", MainContainerActivity.YIBAN_FRAGMENT);
                ShortcutInfo info = new ShortcutInfo.Builder(this, "id" + i)
                        .setShortLabel("易班工具")
                        .setLongLabel("易班工具(含一键评议" )
                        .setIcon(Icon.createWithResource(this, R.drawable.icon_yiban))
                        .setIntent(intent)
                        .build();
                infos.add(info);
            }else if (i == 4) {
                Intent intent = new Intent(this, MainContainerActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("shortcut", MainContainerActivity.EMPTY_ROOM_FRAGMENT);
                ShortcutInfo info = new ShortcutInfo.Builder(this, "id" + i)
                        .setShortLabel("空教室")
                        .setLongLabel("空教室查询" )
                        .setIcon(Icon.createWithResource(this, R.drawable.icon_room))
                        .setIntent(intent)
                        .build();
                infos.add(info);
            }
//            manager.addDynamicShortcuts(Arrays.asList(info));
        }
        mShortcutManager.setDynamicShortcuts(infos);
    }
}
