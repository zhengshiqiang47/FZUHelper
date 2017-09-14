package com.helper.west2ol.fzuhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.util.ActivityController;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;

/**
 * Created by Administrator on 2016/10/20.
 */

public class OtherActivity extends AppCompatActivity implements View.OnClickListener{
    Button back_button;
    ImageView jiaxi_button;
    ImageView testButton;
    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        setContentView(R.layout.activity_other);
        ActivityController.addActivity(this);
        testButton=(ImageView)findViewById(R.id.item5_Button);
        testButton.setOnClickListener(this);
        back_button = (Button)findViewById(R.id.back_button_in_other);
        back_button.setOnClickListener(this);
        jiaxi_button = (ImageView)findViewById(R.id.item3_Button);
        jiaxi_button.setOnClickListener(this);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button_in_other:
                finish();
                break;
            case R.id.item3_Button:
                Intent startJiaxiIntent = new Intent(OtherActivity.this , ForumOfJiaxiActivity.class);
                startActivity(startJiaxiIntent);
                break;
            case R.id.item5_Button:
                Log.i("OtherActivity","抓取校历");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        HtmlParseUtil.getBeginDate(getApplicationContext());
                    }
                }).start();
                break;
        }
    }


}
