package com.helper.west2ol.fzuhelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.activity.WebViewActivity;
import com.helper.west2ol.fzuhelper.bean.Yiban;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by CoderQiang on 2017/9/20.
 */

public class YibanAdapter extends RecyclerView.Adapter {

    private List<Yiban> yiben;
    private Context context;

    public YibanAdapter(Context context,List<Yiban> yiben) {
        super();
        this.yiben = yiben;
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new YibanHolder(LayoutInflater.from(context).inflate(R.layout.item_yiban,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("test","title"+yiben.get(position).getTitle());
        YibanHolder yibanHolder=(YibanHolder)holder;
        Yiban yiban = yiben.get(position);
        yibanHolder.title.setText(yiban.getTitle());
        ImageView imageView=yibanHolder.imageView;
        String image=yiban.getImage();
        if (image.contains("http")){
            Glide.with(context).load(yiban.getImage()).into(imageView);
        } else if (image.contains("jcgl")){
            imageView.setImageResource(R.drawable.jcgl3x);
        }else if (image.contains("jzd")){
            imageView.setImageResource(R.drawable.jzd);
        }else if (image.contains("ssfw")){
            imageView.setImageResource(R.drawable.ssfw);
        }else if (image.contains("zhcp")){
            imageView.setImageResource(R.drawable.zhcp);
        }else if (yiban.getTitle().toString().contains("CET")){
            imageView.setImageResource(R.drawable.cet_42);
            imageView.setScaleX(0.7f);
            imageView.setScaleY(0.7f);
        }else if (yiban.getTitle().toString().contains("单期绩点")){
            imageView.setImageResource(R.drawable.score);
            imageView.setScaleX(0.7f);
            imageView.setScaleY(0.7f);
        }
        final String url=yiban.getUrl();
        yibanHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, WebViewActivity.class);
                intent.putExtra("url",url);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class YibanHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView title;
        private LinearLayout layout;

        public YibanHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.item_yiban_image);
            title = (TextView) itemView.findViewById(R.id.item_yiban_title);
            layout=(LinearLayout) itemView.findViewById(R.id.item_yiban_layout);
        }
    }
}
