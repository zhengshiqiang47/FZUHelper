package com.helper.west2ol.fzuhelper.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.CourseBeanLab;
import com.helper.west2ol.fzuhelper.bean.User;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.util.ActivityController;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.SaveObjectUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/20.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    View logout;
    @BindView(R.id.back_button_in_setting)
    Button back_Button;
    @BindView(R.id.share_app)
    RelativeLayout shareLayout;
    @BindView(R.id.about_us)
    RelativeLayout aboutLayout;

    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
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
        back_Button.setOnClickListener(this);
        logout = findViewById(R.id.logout_button);
        shareLayout.setOnClickListener(this);
        logout.setOnClickListener(this);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout_button:
                DBManager dbManager=new DBManager(this);
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
                saveObjectUtils.setObject("config",null);
                saveObjectUtils.setObject("cookie",null);
                CourseBeanLab.get(this).getCourses().clear();
                ActivityController.finashAll();
                Intent intent = new Intent(SettingActivity.this , LoginActivity_1.class);
                startActivity(intent);
                break;
            case R.id.back_button_in_setting:
                this.finish();
                break;
            case R.id.share_app:
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, DefaultConfig.get().getNewVersionUrl());
                Log.i("Setting", "onClick: share");
                startActivity(Intent.createChooser(textIntent, "分享"));
                break;
            default:
                Toast.makeText(getApplicationContext(), "此功能即将来袭，敬请期待!!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    //011917
}
