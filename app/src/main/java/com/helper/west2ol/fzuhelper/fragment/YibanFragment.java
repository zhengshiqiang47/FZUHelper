package com.helper.west2ol.fzuhelper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.adapter.YibanAdapter;
import com.helper.west2ol.fzuhelper.dao.DBManager;
import com.helper.west2ol.fzuhelper.util.DefaultConfig;
import com.helper.west2ol.fzuhelper.util.FzuCookie;
import com.helper.west2ol.fzuhelper.util.HtmlParseUtil;
import com.helper.west2ol.fzuhelper.util.HttpUtil;

import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2017/9/20.
 */

public class YibanFragment extends Fragment {

    @Bind(R.id.yiban_recycler)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_yiban,container,false);
        ButterKnife.bind(rootView);
        initView();
        return rootView;
    }

    private void initView(){
        Observable.create(new Observable.OnSubscribe<List<HtmlParseUtil.YibanResult.DataBean>>() {
            @Override
            public void call(Subscriber<? super List<HtmlParseUtil.YibanResult.DataBean>> subscriber) {
//                HtmlParseUtil.
//                subscriber.onCompleted();
                List<HtmlParseUtil.YibanResult.DataBean> dataBeens=HtmlParseUtil.getYibanList();
                subscriber.onNext(dataBeens);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                List<HtmlParseUtil.YibanResult.DataBean> dataBeen= (List<HtmlParseUtil.YibanResult.DataBean>) o;
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new YibanAdapter(getActivity(),dataBeen));
            }
        });
    }
}
