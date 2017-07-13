package com.helper.west2ol.fzuhelper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.helper.west2ol.fzuhelper.R;

/**
 * Created by Administrator on 2016/10/20.
 */

public class GradeFragment extends Fragment {
    Button menu_button_in_grade;
    DrawerLayout drawer;
    RecyclerView gradeRecycler;

    @Override
    public void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedIntanceState){
        View rootView = inflater.inflate(R.layout.fragment_grade , container , false);
        drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        menu_button_in_grade = (Button)rootView.findViewById(R.id.menu_button_in_grade);
        menu_button_in_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        initData();
        initView(rootView);
        return rootView;
    }

    private void initData(){

    }

    private void initView(View rootView){
        gradeRecycler=(RecyclerView)rootView.findViewById(R.id.grade_recycler);

    }

}
