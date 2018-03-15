package com.wqz.houseanalysis.activity.sub;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.activity.MainActivity;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.base.BaseImmersiveActivity;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.dialog.DownloadDialog;
import com.wqz.houseanalysis.dialog.GetTimeDialog;
import com.wqz.houseanalysis.dialog.GetTransferNumDialog;
import com.wqz.houseanalysis.dialog.LengthDialog;
import com.wqz.houseanalysis.utils.FileUtils;
import com.wqz.houseanalysis.utils.ListUtils;
import com.wqz.houseanalysis.utils.SnackUtils;
import com.wqz.houseanalysis.utils.StatusUtils;
import com.wqz.houseanalysis.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Date;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import okhttp3.Call;

public class LengthActivity extends BaseImmersiveActivity
{
    AMap aMap;
    Marker marker;
    private LatLng startPoint;
    private int length;
    DownloadDialog downloadDialog;
    Circle circle;

    @BindView(R.id.length_map)
    MapView mapView;
    @BindView(R.id.tv_length_hint)
    TextView tvHint;
    @BindView(R.id.rl_length_root)
    RelativeLayout rlRoot;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_length;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        onMapInit(savedInstanceState);

        downloadDialog = new DownloadDialog(LengthActivity.this);
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
                LengthDialog lengthDialog = new LengthDialog(LengthActivity.this);
                lengthDialog.show();
                lengthDialog.setStatusListener(statusListener);

                if(marker != null) marker.destroy();
                marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(
                        BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.mipmap.self_loc))));
                startPoint = latLng;
                tvHint.setText("选择完毕");
            }
        });
    }

    LengthDialog.StatusListener statusListener = new LengthDialog.StatusListener()
    {
        @Override
        public void onDimiss()
        {
            tvHint.setText("给你一个重新选择起点的机会=。=");
        }

        @Override
        public void onConfirm(Integer length)
        {
            aMap.clear();

            LengthActivity.this.length = length;
            circle = aMap.addCircle(new CircleOptions().
                    center(startPoint).
                    radius(length * 1000).
                    fillColor(Color.parseColor("#8803a0e2")).
                    strokeColor(Color.parseColor("#03a0e2")).
                    strokeWidth(5));

            SnackUtils.makeSnackBar(rlRoot, "确定是这个圆内吗？", Snackbar.LENGTH_INDEFINITE, tvHint, new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    loadData();
                }
            }, "确认");
        }
    };

    private void loadData()
    {
        downloadDialog.show();

        OkHttpUtils
                .get()
                .url(UrlUtils.LENGTH_URL)
                .addParams("length", length * 1000 + "")
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
                        addressActiveStatus.title = "直线距离限制";
                        addressActiveStatus.param = length + "千米";

                        List<AddressActiveStatus> statusList = StatusUtils.getStatusList();
                        ListUtils.setAllNodeStatusUnactive(statusList);
                        statusList.add(addressActiveStatus);
                        StatusUtils.setStatusList(statusList);

                        startActivity(new Intent(LengthActivity.this, MainActivity.class));
                        downloadDialog.dismiss();
                        LengthActivity.this.finish();
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
