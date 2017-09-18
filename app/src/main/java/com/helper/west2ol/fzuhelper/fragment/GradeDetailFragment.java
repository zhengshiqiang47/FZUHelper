package com.helper.west2ol.fzuhelper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helper.west2ol.fzuhelper.R;

import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2017/9/18.
 */

public class GradeDetailFragment extends Fragment {

    RecyclerView gradeRecycler;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedIntanceState){
        View rootView = inflater.inflate(R.layout.fragment_grade_detail , container , false);
        ButterKnife.bind(this, rootView);
        gradeRecycler=(RecyclerView)rootView.findViewById(R.id.grade_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        gradeRecycler.setLayoutManager(layoutManager);
        return rootView;
    }
}
