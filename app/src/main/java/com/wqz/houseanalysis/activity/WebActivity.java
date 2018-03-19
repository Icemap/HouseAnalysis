package com.wqz.houseanalysis.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;

import java.net.URISyntaxException;

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
                if (!shouldOverrideUrlLoadingByApp(view, url))
                {
                    view.loadUrl(url);
                }
                return true;
            }

            private boolean shouldOverrideUrlLoadingByApp(WebView view, String url)
            {
                if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                    //不处理http, https, ftp的请求
                    return false;
                }
                Intent intent;
                try
                {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                }
                catch (URISyntaxException e) {
                    Log.e("[WebView]", "URISyntaxException: " + e.getLocalizedMessage());
                    return false;
                }
                intent.setComponent(null);

                try
                {
                    WebActivity.this.startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    Log.e("[WebView]", "ActivityNotFoundException: " + e.getLocalizedMessage());
                    return false;
                }
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
