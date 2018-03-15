package com.wqz.houseanalysis.activity.sub;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.activity.MainActivity;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.base.BaseImmersiveActivity;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.bean.ToBackEndPointBean;
import com.wqz.houseanalysis.dialog.DownloadDialog;
import com.wqz.houseanalysis.dialog.GetTimeDialog;
import com.wqz.houseanalysis.utils.FileUtils;
import com.wqz.houseanalysis.utils.ListUtils;
import com.wqz.houseanalysis.utils.StatusUtils;
import com.wqz.houseanalysis.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class FreeDrawActivity extends BaseImmersiveActivity
{
    AMap aMap;
    DownloadDialog downloadDialog;
    List<LatLng> pointList;

    @BindView(R.id.free_draw_map)
    MapView mapView;
    @BindView(R.id.tv_hint)
    TextView tvHint;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_free_draw;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        onMapInit(savedInstanceState);

        downloadDialog = new DownloadDialog(FreeDrawActivity.this);
    }

    private void onMapInit(Bundle savedInstanceState)
    {
        pointList = new ArrayList<>();

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
                pointList.add(latLng);
                onDrawPolygon();
            }
        });
    }

    private void onDrawPolygon()
    {
        aMap.clear();

        if(pointList.size() >= 3)
        {
            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.addAll(pointList);
            polygonOptions
                    .strokeWidth(5)
                    .strokeColor(Color.parseColor("#03a0e2"))
                    .fillColor(Color.parseColor("#8803a0e2"));
            aMap.addPolygon(polygonOptions);
        }
        for(LatLng latLng : pointList)
        {
            aMap.addMarker(new MarkerOptions().position(latLng).icon(
                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),R.mipmap.point_style)))
                    .anchor(0.5f, 0.5f));
        }
    }

    private void loadData()
    {
        List<ToBackEndPointBean> toEndPointList = new ArrayList<>();
        for(LatLng latLng : pointList)
        {
            toEndPointList.add(new ToBackEndPointBean(latLng.longitude, latLng.latitude));
        }

        downloadDialog.show();

        OkHttpUtils
                .post()
                .url(UrlUtils.FREE_DRAW_URL)
                .addParams("jsonPointList", new Gson().toJson(toEndPointList))
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
                        addressActiveStatus.title = "自由绘制限制";

                        List<AddressActiveStatus> statusList = StatusUtils.getStatusList();
                        ListUtils.setAllNodeStatusUnactive(statusList);
                        statusList.add(addressActiveStatus);
                        StatusUtils.setStatusList(statusList);

                        startActivity(new Intent(FreeDrawActivity.this, MainActivity.class));
                        downloadDialog.dismiss();
                        FreeDrawActivity.this.finish();
                    }
                });
    }

    @OnClick(R.id.iv_back)
    public void onBack()
    {
        pointList.remove(pointList.size() - 1);
        onDrawPolygon();
    }

    @OnClick(R.id.iv_clear)
    public void onClear()
    {
        pointList.clear();
        onDrawPolygon();
    }

    @OnClick(R.id.iv_confirm)
    public void onConfirm()
    {
        loadData();
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
