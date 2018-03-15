package com.wqz.houseanalysis.base;

import android.app.Application;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.model.CameraPosition;
import com.tencent.bugly.Bugly;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Wqz on 2016/12/23.
 */

public class BaseApplication extends Application
{
    public static BaseApplication instances;
    private AMapLocationClient mLocationClient;
    private CameraPosition currentCamera;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Bugly.init(getApplicationContext(), "85d9505fbb", true);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(180000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        instances = this;
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

    public CameraPosition getCurrentCamera()
    {
        return currentCamera;
    }

    public void setCurrentCamera(CameraPosition currentCamera)
    {
        this.currentCamera = currentCamera;
    }
}
