package com.helper.west2ol.fzuhelper.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.helper.west2ol.fzuhelper.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
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
    
    @BindView(R.id.yiban_webview)
    WebView webView;
    @BindView(R.id.webview_title)
    TextView titleView;
    @BindView(R.id.back_button_in_webview)
    Button backButton;
    @BindView(R.id.more_button_in_webview)
    Button moreButton;

    private Activity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        context=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        init();
        initView();
    }

    private void initView(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }else {
                    context.onBackPressed();
                }

            }
        });
    }

    private void init(){
        final WebSettings webSettings = webView.getSettings();
        String ua=webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua+":FzuHelper");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        String url=getIntent().getStringExtra("url");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
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
