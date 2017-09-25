package com.helper.west2ol.fzuhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.User;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.HttpUtil;
import com.helper.west2ol.fzuhelper.util.SaveObjectUtils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by coderqiang on 2017/9/22.
 */

public class FlashActivity extends Activity {

    private static final String TAG="LoginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_flash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        flashLogin();
    }



    private void flashLogin() {
        SaveObjectUtils saveObjectUtils=new SaveObjectUtils(this,"config");
        DefaultConfig defaultConfig=saveObjectUtils.getObject("config",DefaultConfig.class);
        DefaultConfig config=DefaultConfig.get();
        if (defaultConfig != null) {
            config.setBeginDate(defaultConfig.getBeginDate());
            config.setUserAccount(defaultConfig.getUserAccount());
            config.setNowWeek(defaultConfig.getNowWeek());
            config.setCurXuenian(defaultConfig.getCurXuenian());
            config.setCurYear(defaultConfig.getCurYear());
            config.setXqValues(defaultConfig.getXqValues());
            config.setOptions(defaultConfig.getOptions());
            config.setLogin(defaultConfig.isLogin());
        }
        final DBManager dbManager=new DBManager(this);
        final List<User> users=dbManager.queryUserList();
        if (users != null) {
                    Observable.create(new Observable.OnSubscribe<Object>() {
                        @Override
                        public void call(Subscriber<? super Object> subscriber) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            for (User user : users) {
                                if (user.isLogin() == true) {
                                    DefaultConfig.get().setUserAccount(user.getFzuAccount());
                                    final String loginResponse = HttpUtil.Login(getApplicationContext(),user);
                                    subscriber.onNext(loginResponse);
                                    switch (loginResponse){
                                        case "网络错误":
                                            break;
                                        case "密码错误":
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
                            Intent intent = new Intent(FlashActivity.this , LoginActivity.class);
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

                        }

                        @Override
                        public void onNext(Object o) {
                            Toast.makeText(getApplicationContext(), (String)o, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}
