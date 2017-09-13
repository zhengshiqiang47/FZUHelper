package com.helper.west2ol.fzuhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.util.ActivityController;
import com.helper.west2ol.fzuhelper.util.HttpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/20.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
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
                        final String loginResponse = HttpUtil.Login(getApplicationContext());
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
