package com.heinsoft.heo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.jakewharton.rxbinding2.view.RxView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author xiaoyl
 * @date 2013-07-20
 */
public class FragmentPayWebview extends BaseFragment   {
    private String title = "";

    String url = "http://www.baidu.com";
    @Bind(R.id.tbsContent)
    WebView tbsContent;
    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;

    @Bind(R.id.header_btn_lay)
    LinearLayout header_btn_lay;
    @Bind(R.id.header_title)
    TextView header_title;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview_layout, container, false);
        ButterKnife.bind(FragmentPayWebview.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshView();
    }

    public void loadUrl(String url){
        this.url = url;
    }

    public void setTitle(String t){
        title = t;
    }
    private void refreshView() {
        header_title.setText(title);
        RxView.clicks(header_btn_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());
        tbsContent.loadUrl(url);
        WebSettings webSettings = tbsContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        tbsContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                empty_lay.setVisibility(View.GONE);
                tbsContent.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        tbsContent.destroy();
    }

    @Override
    public void Back() {
        listener.gotoPay();
    }
}
