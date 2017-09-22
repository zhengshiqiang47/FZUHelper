package com.helper.west2ol.fzuhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.User;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.dao.DaoMaster;
import com.helper.west2ol.fzuhelper.util.ActivityController;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.HttpUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/20.
 */

public class LoginActivity extends Activity implements View.OnClickListener{
    private static final String TAG="LoginActivity";
    @Bind(R.id.login_button)
    Button login_button;
    @Bind(R.id.muser_editText)
    EditText muserEd;
    @Bind(R.id.passwd_editText)
    EditText passwdEd;

    private String id_1;
    private String num;//从Logincheck.asp获取的id和num

    private String id_2;//从LOGIN_CHK_XS获取,后面获取web信息的唯一标识码

    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        setContentView(R.layout.activity_login);
        ActivityController.addActivity(this);
        ButterKnife.bind(this);
        login_button = (Button)findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                final String muser = muserEd.getText().toString();
                final String passwd = passwdEd.getText().toString();
                if(muser==null){
                    muserEd.setText("请输入用户名");
                    return;
                }else if(passwd==null){
                    passwdEd.setText("请输入密码");
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user=new User();
                        user.setFzuPasssword(passwd);
                        user.setFzuAccount(muser);
                        DBManager dbManager=new DBManager(getApplicationContext());
                        List<User> users=dbManager.queryUserList();
                        boolean isExist=false;
                        for (User resUser : users) {
                            if (resUser.getFzuAccount().equals(user.getFzuAccount())){
                                resUser.setFzuPasssword(user.getFzuPasssword());
                                resUser.setIsLogin(true);
                                dbManager.updateUser(resUser);
                                isExist=true;
                                if (resUser.isLogin()){

                                }
                                break;
                            }
                        }
                        if (!isExist) {
                            user.setIsLogin(true);
                            dbManager.insertUser(user);
                        }
                        DefaultConfig.get().setUserAccount(user.getFzuAccount());
                        final String loginResponse = HttpUtil.Login(getApplicationContext(),user);
                        switch (loginResponse){
                            case "网络错误":
                                break;
                            case "密码错误":
                                Log.i(TAG, "密码错误");
                                break;
                            case "登录成功":
                                Intent intent = new Intent(LoginActivity.this , MainContainerActivity.class);
                                intent.putExtra("id" , id_2);
                                startActivity(intent);
                                break;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), loginResponse, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
        }
    }
}
