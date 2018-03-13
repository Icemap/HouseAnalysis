package com.wqz.houseanalysis.base;

import android.app.Application;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.esri.android.runtime.ArcGISRuntime;
import com.tencent.bugly.Bugly;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Wqz on 2016/12/23.
 */

public class BaseApplication extends Application
{
    public static BaseApplication instances;
    private AMapLocationClient mLocationClient;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Bugly.init(getApplicationContext(), "85d9505fbb", true);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        instances = this;
        ArcGISRuntime.setClientId("o4AGICRmrKDsNHRY");
        initLoc();
    }

    public static BaseApplication getInstances()
    {
        return instances;
    }

    public void initLoc()
    {
        mLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption mLocationOption;
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(mLocationOption);
    }

    public AMapLocationClient getLocClient()
    {
        return mLocationClient;
    }
}
