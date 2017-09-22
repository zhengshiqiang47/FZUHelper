package com.helper.west2ol.fzuhelper.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.helper.west2ol.fzuhelper.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by coderqiang on 2017/9/22.
 */

public class WebViewActivity extends Activity {

    private static final String TAG="WebViewActivity";
    Map<String,String> extraHeaders = new HashMap<String, String>();
    {
        extraHeaders.put("User-Agent","FzuHelper");
    }

    @Bind(R.id.yiban_webview)
    WebView webView;
    @Bind(R.id.webview_title)
    TextView titleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        init();
    }



    private void init(){
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        String url=getIntent().getStringExtra("url");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url,extraHeaders);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleView.setText(title);
            }
        });

        webView.loadUrl(url,extraHeaders);
    }
}
