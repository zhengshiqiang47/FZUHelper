package com.helper.west2ol.fzuhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.bean.Exam;
import com.helper.west2ol.fzuhelper.util.StringUtil;

import java.util.List;

/**
 * Created by coderqiang on 2017/10/12.
 */

public class ExamAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ExamFragment";

    private Context context;
    private List<Exam> exams;

    public ExamAdapter(Context context, List<Exam> exams) {
        this.exams = exams;
        Log.i(TAG, "ExamAdapter: "+exams.size());
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExamHolder(LayoutInflater.from(context).inflate(R.layout.item_exam_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Exam exam = exams.get(position);
        ExamHolder examHolder = (ExamHolder) holder;
        examHolder.nameTv.setText(exam.getName());
        if (exam.getAddress().length()>=7) {
            examHolder.addressTv.setText(exam.getAddress());
            examHolder.decorationTv.setVisibility(View.GONE);

        }else {
            examHolder.addressLyout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    private class ExamHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView addressTv;
        LinearLayout addressLyout;
        TextView decorationTv;

        public ExamHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.item_exam_name_text);
            addressLyout = (LinearLayout) itemView.findViewById(R.id.item_exam_layout);
            addressTv = (TextView) itemView.findViewById(R.id.item_exam_address_text);
            decorationTv = (TextView) itemView.findViewById(R.id.item_exam_decoration);
        }
    }


}
