package com.heinsoft.heo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.heinsoft.heo.R;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author xiaoyl
 * @date 2013-07-20
 */
public class FragmentWebview extends BaseFragment   {
    private String title = "";

    IFragmentWebviewListener listener1;
    String url = "http://www.baidu.com";
    @Bind(R.id.tbsContent)
    com.tencent.smtt.sdk.WebView tbsContent;
    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;


    @Override
    public void onAttach(Context context) {
        try {
            listener1 = (IFragmentWebviewListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview_layout, container, false);
        ButterKnife.bind(FragmentWebview.this, view);
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
    private void refreshView() {

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

    public boolean canBack() {
        return tbsContent.canGoBack();
    }

    public void back() {
        if(tbsContent.canGoBack()) {
            tbsContent.goBack();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public interface IFragmentWebviewListener {

        void fragmentWebviewFinished();

    }
}
