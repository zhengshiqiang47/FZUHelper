package com.helper.west2ol.fzuhelper.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import com.gordonwong.materialsheetfab.AnimatedFab;

/**
 * Created by CoderQiang on 2017/10/3.
 */

public class MyFab extends FloatingActionButton implements AnimatedFab {

    public MyFab(Context context) {
        super(context);
    }

    public MyFab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void show() {
        show(0,0);
    }

    @Override
    public void show(float translationX, float translationY) {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(View.INVISIBLE);
    }
}
