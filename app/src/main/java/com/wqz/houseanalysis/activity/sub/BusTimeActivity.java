package com.wqz.houseanalysis.activity.sub;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.activity.MainActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.base.BaseImmersiveActivity;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.dialog.DownloadDialog;
import com.wqz.houseanalysis.dialog.GetTimeDialog;
import com.wqz.houseanalysis.utils.FileUtils;
import com.wqz.houseanalysis.utils.ListUtils;
import com.wqz.houseanalysis.utils.StatusUtils;
import com.wqz.houseanalysis.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Date;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import okhttp3.Call;

public class BusTimeActivity extends BaseImmersiveActivity
{
    AMap aMap;
    Marker marker;
    private LatLng startPoint;
    private int time;
    DownloadDialog downloadDialog;

    @BindView(R.id.bus_time_map)
    MapView mapView;
    @BindView(R.id.tv_hint)
    TextView tvHint;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_bus_time;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        onMapInit(savedInstanceState);

        downloadDialog = new DownloadDialog(BusTimeActivity.this);
    }

    private void onMapInit(Bundle savedInstanceState)
    {
        mapView.onCreate(savedInstanceState);

        if (aMap == null)
            aMap = mapView.getMap();

        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                BaseApplication.getInstances().getCurrentCamera()));

        aMap.getUiSettings().setZoomControlsEnabled(false);

        aMap.setOnMapClickListener(new AMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                GetTimeDialog getTimeDialog = new GetTimeDialog(BusTimeActivity.this);
                getTimeDialog.show();
                getTimeDialog.setStatusListener(statusListener);

                if(marker != null) marker.destroy();
                marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(
                        BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),R.mipmap.self_loc))));
                startPoint = latLng;
                tvHint.setText("选择完毕");
            }
        });
    }

    GetTimeDialog.StatusListener statusListener = new GetTimeDialog.StatusListener()
    {
        @Override
        public void onDimiss()
        {
            tvHint.setText("给你一个重新选择起点的机会=。=");
        }

        @Override
        public void onConfirm(Integer mins)
        {
            time = mins;
            loadData();
        }
    };

    private void loadData()
    {
        downloadDialog.show();

        OkHttpUtils
                .get()
                .url(UrlUtils.BUS_TIME_URL)
                .addParams("mins", time + "")
                .addParams("originLon", startPoint.longitude + "")
                .addParams("originLat", startPoint.latitude + "")
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        tvHint.setText("网络错误，再试一次吧");
                        downloadDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        List<AddressBean> addressBeans = new Gson().fromJson(response,
                                new TypeToken<List<AddressBean>>(){}.getType());
                        AddressActiveStatus addressActiveStatus = new AddressActiveStatus();
                        addressActiveStatus.addressList = addressBeans;
                        addressActiveStatus.active = true;
                        addressActiveStatus.analysisDate = new Date();
                        addressActiveStatus.title = "公交时间限制";
                        addressActiveStatus.param = time + "分钟";

                        List<AddressActiveStatus> statusList = StatusUtils.getStatusList();
                        ListUtils.setAllNodeStatusUnactive(statusList);
                        statusList.add(addressActiveStatus);
                        StatusUtils.setStatusList(statusList);

                        startActivity(new Intent(BusTimeActivity.this, MainActivity.class));
                        downloadDialog.dismiss();
                        BusTimeActivity.this.finish();
                    }
                });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
