package com.helper.west2ol.fzuhelper.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.util.ActivityController;

/**
 * Created by Administrator on 2016/11/5.
 */

public class ForumOfJiaxiActivity extends AppCompatActivity  implements View.OnClickListener{
    Button back_button;
    @Override
    public void onCreate(Bundle savedStanceState){
        super.onCreate(savedStanceState);
        setContentView(R.layout.activity_forum_of_jiaxi);
        ActivityController.addActivity(this);

        back_button = (Button)findViewById(R.id.back_button_in_jiaxi);
        back_button.setOnClickListener(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button_in_jiaxi:
                finish();
                break;
        }
    }
}
