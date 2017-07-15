package com.helper.west2ol.fzuhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.FDScore;
import com.helper.west2ol.fzuhelper.bean.FDScoreLB;

import java.util.List;

/**
 * Created by coderqiang on 2017/7/14.
 */

public class GradeAdapter extends RecyclerView.Adapter {

    private List<FDScore> fdScores;
    private Context context;

    public GradeAdapter(Context context,List<FDScore> scores) {
        super();
        fdScores=scores;
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GradeHolder(LayoutInflater.from(context).inflate(R.layout.item_grade_recycler,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GradeHolder gradeHolder = (GradeHolder) holder;
        FDScore fdScore = fdScores.get(position);
        gradeHolder.gradeTitleText.setText(fdScore.getName());
        gradeHolder.scoreText.setText(fdScore.getScore());
        gradeHolder.jidianText.setText(fdScore.getJidian());
        gradeHolder.xuefenText.setText(fdScore.getXuefen());
    }

    @Override
    public int getItemCount() {
        return fdScores.size();
    }

    private class GradeHolder extends RecyclerView.ViewHolder{

        TextView gradeTitleText;
        TextView xuefenText;
        TextView jidianText;
        TextView scoreText;

        public GradeHolder(View itemView) {
            super(itemView);
            gradeTitleText = (TextView) itemView.findViewById(R.id.item_grade_title);
            xuefenText = (TextView) itemView.findViewById(R.id.item_grade_xuefen);
            jidianText=(TextView)itemView.findViewById(R.id.item_grade_jidian);
            scoreText = (TextView) itemView.findViewById(R.id.item_grade_score);

        }
    }
}
