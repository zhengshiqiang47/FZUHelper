package com.helper.west2ol.fzuhelper.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.FDScore;
import com.helper.west2ol.fzuhelper.util.RegexUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by coderqiang on 2017/7/14.
 */

public class GradeAdapter extends RecyclerView.Adapter {

    private List<FDScore> fdScores;
    private Context context;

    public GradeAdapter(Context context, List<FDScore> scores) {
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
        if (position == 0) {
            gradeHolder.gradeTitleText.setText("课程");
            gradeHolder.scoreText.setText("成绩");
            gradeHolder.jidianText.setText("绩点");
            gradeHolder.xuefenText.setText("学分");
            gradeHolder.imageView.setImageResource(R.drawable.item_score_icon);
            return;
        }
        FDScore fdScore = fdScores.get(position-1);
        gradeHolder.gradeTitleText.setText(fdScore.getName());
        gradeHolder.scoreText.setText(fdScore.getScore());
        if (fdScore.getScore().contains("尚未录入")){
            gradeHolder.scoreText.setText("暂无");
        }
        gradeHolder.jidianText.setText(fdScore.getJidian());
        gradeHolder.xuefenText.setText(fdScore.getXuefen());
        gradeHolder.imageView.setImageResource(R.drawable.item_score_icon);
        GradientDrawable drawable= (GradientDrawable) gradeHolder.imageView.getDrawable();
        int colorId = 0;
        if (Pattern.matches(RegexUtil.number,fdScore.getScore())){
            int score = Integer.parseInt(fdScore.getScore());
            if (score < 60) {
                colorId = R.color.colorWhite;
            }else if (score<70){
                colorId = R.color.score_70;
            } else if (score < 80) {
                colorId = R.color.score_80;
            } else if (score < 90) {
                colorId= R.color.score_90;
            }else{
                colorId= R.color.score_100;
            }
            drawable.setColor(context.getResources().getColor(colorId));
        }else {
            drawable.setColor(context.getResources().getColor(R.color.colorWhite));
        }

    }

    @Override
    public int getItemCount() {
        return fdScores.size()+1;
    }

    private class GradeHolder extends RecyclerView.ViewHolder{

        TextView gradeTitleText;
        TextView xuefenText;
        TextView jidianText;
        TextView scoreText;
        ImageView imageView;

        public GradeHolder(View itemView) {
            super(itemView);
            gradeTitleText = (TextView) itemView.findViewById(R.id.item_grade_title);
            xuefenText = (TextView) itemView.findViewById(R.id.item_grade_xuefen);
            jidianText=(TextView)itemView.findViewById(R.id.item_grade_jidian);
            scoreText = (TextView) itemView.findViewById(R.id.item_grade_score);
            imageView = (ImageView) itemView.findViewById(R.id.item_grade_icon);
        }
    }
}
