package com.helper.west2ol.fzuhelper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by CoderQiang on 2016/11/12.
 */

public class InnerScrollView extends ScrollView {
    public InnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InnerScrollView(Context context) {
        super(context);
    }


    int oldX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int newX = (int) ev.getX();
                View view = (View) getParent();
                if (view == null) {
                    break;
                }
                while (!(view instanceof ScrollView) && !(view instanceof ListView)) {//暴力查找
                    view = (View) view.getParent();
                    if (view == null) {
                        break;
                    }
                }
                if (Math.abs(newX - oldX) > 50) {
                    //拦截
                    requestDisallowInterceptTouchEvent(false);
                    break;
                }

                if (getScrollY() + getHeight() >= computeVerticalScrollRange() + 1) {
                    //拦截，父响应
                    requestDisallowInterceptTouchEvent(false);
                } else {

                    //不拦截，事件给子view
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }
}
