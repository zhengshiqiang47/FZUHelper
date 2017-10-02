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
import android.widget.Button;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.adapter.GradeAdapter;
import com.helper.west2ol.fzuhelper.bean.FDScore;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2017/9/18.
 */

public class GradeDetailFragment extends Fragment {

    @BindView(R.id.grade_detail_recycler)
    RecyclerView gradeRecycler;

    Context context;
    List<FDScore> fdScores;

    public static GradeDetailFragment getInstance(List<FDScore> fdScores,Context context){
        GradeDetailFragment fragment = new GradeDetailFragment();
        fragment.fdScores=fdScores;
        fragment.context=context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedIntanceState){
        View rootView = inflater.inflate(R.layout.fragment_grade_detail , container , false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        gradeRecycler.setAdapter(new GradeAdapter(context,fdScores));
        gradeRecycler.setLayoutManager(layoutManager);
    }

}
