package com.wqz.houseanalysis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseImmersiveActivity;

public class SplashActivity extends BaseImmersiveActivity
{
    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_splash;
    }

    @Override
    protected void onInitLogic()
    {
        super.onInitLogic();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 2000);
    }
}
