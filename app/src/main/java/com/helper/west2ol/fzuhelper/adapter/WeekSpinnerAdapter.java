package com.helper.west2ol.fzuhelper.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.helper.west2ol.fzuhelper.R;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2017/9/27.
 */

public class WeekSpinnerAdapter extends BaseAdapter {

    private ArrayList<Integer> weeks;
    private Context context;


    public WeekSpinnerAdapter(Context context) {
        this.context = context;
        this.weeks = new ArrayList<>();
        for (int i=1;i<=22;i++) {
            weeks.add(i);
        }
    }

    @Override
    public int getCount() {
        return weeks.size();
    }

    @Override
    public Object getItem(int position) {
        return weeks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.spinner_course_layout,null);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.spinner_course_layout,null);
        return view;
    }
}
