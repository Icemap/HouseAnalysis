package com.wqz.houseanalysis.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;

import butterknife.BindView;

public class WebActivity extends BaseActivity
{

    @BindView(R.id.wv_single)
    WebView wvSingle;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_web;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        WebSettings webSetting = wvSingle.getSettings();
        String UA = "Mozilla/5.0 (Linux; U; Android 6.0.1; zh-cn; MI 4W Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.146 Mobile Safari/537.36 XiaoMi/MiuiBrowser/9.3.5";
        webSetting.setUserAgentString(UA);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAppCacheEnabled(false);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvSingle.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });
        wvSingle.loadUrl(BaseApplication.getInstances().getCurrentUrl());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvSingle.canGoBack())
        {
            wvSingle.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }
}
