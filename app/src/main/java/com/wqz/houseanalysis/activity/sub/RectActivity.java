package com.wqz.houseanalysis.activity.sub;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.activity.MainActivity;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.dialog.DownloadDialog;
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

public class RectActivity extends BaseActivity
{
    AMap aMap;
    DownloadDialog downloadDialog;
    LatLng sP, eP;

    @BindView(R.id.rect_map)
    MapView mapView;
    @BindView(R.id.tv_hint)
    TextView tvHint;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_rect;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        onMapInit(savedInstanceState);

        downloadDialog = new DownloadDialog(RectActivity.this);
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
                if(sP == null)
                    sP = latLng;
                else if(eP == null)
                    eP = latLng;

                onDrawPolygon();
            }
        });
    }

    private void onDrawPolygon()
    {
        aMap.clear();

        if(sP == null)
            return;
        else if(eP == null)
        {
            aMap.addMarker(new MarkerOptions().position(sP).icon(
                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),R.mipmap.point_style))));
        }
        else
        {
            List<LatLng> pList = createRectangle();

            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.addAll(pList);
            polygonOptions
                    .strokeWidth(5)
                    .strokeColor(Color.parseColor("#03a0e2"))
                    .fillColor(Color.parseColor("#8803a0e2"));
            aMap.addPolygon(polygonOptions);

            for(LatLng latLng : pList)
            {
                aMap.addMarker(new MarkerOptions().position(latLng).icon(
                        BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.mipmap.point_style)))
                .anchor(0.5f, 0.5f));
            }
        }
    }

    private void loadData()
    {
        Double eLonMin = sP.longitude > eP.longitude ? eP.longitude : sP.longitude;
        Double eLonMax = sP.longitude < eP.longitude ? eP.longitude : sP.longitude;
        Double eLatMin = sP.latitude > eP.latitude ? eP.latitude : sP.latitude;
        Double eLatMax = sP.latitude < eP.latitude ? eP.latitude : sP.latitude;

        downloadDialog.show();

        OkHttpUtils
                .post()
                .url(UrlUtils.RECT_URL)
                .addParams("eLonMin", eLonMin + "")
                .addParams("eLonMax", eLonMax + "")
                .addParams("eLatMin", eLatMin + "")
                .addParams("eLatMax", eLatMax + "")
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
                        addressActiveStatus.title = "矩形绘制限制";

                        List<AddressActiveStatus> statusList = StatusUtils.getStatusList();
                        ListUtils.setAllNodeStatusUnactive(statusList);
                        statusList.add(addressActiveStatus);
                        StatusUtils.setStatusList(statusList);

                        startActivity(new Intent(RectActivity.this, MainActivity.class));
                        downloadDialog.dismiss();
                        RectActivity.this.finish();
                    }
                });
    }

    @OnClick(R.id.iv_clear)
    public void onClear()
    {
        sP = null;
        eP = null;
        onDrawPolygon();
    }

    @OnClick(R.id.iv_confirm)
    public void onConfirm()
    {
        loadData();
    }

    private List<LatLng> createRectangle()
    {
        List<LatLng> rectList = new ArrayList<>();
        rectList.add(sP);
        rectList.add(new LatLng(sP.latitude, eP.longitude));
        rectList.add(eP);
        rectList.add(new LatLng(eP.latitude, sP.longitude));
        return rectList;
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
