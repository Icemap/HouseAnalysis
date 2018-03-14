package com.wqz.houseanalysis.activity.sub;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
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
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.dialog.DownloadDialog;
import com.wqz.houseanalysis.dialog.GetTimeDialog;
import com.wqz.houseanalysis.dialog.GetTransferNumDialog;
import com.wqz.houseanalysis.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.sql.Time;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class TimeAndTransferActivity extends BaseActivity
{
    AMap aMap;
    Marker marker;
    private LatLng startPoint;
    private int time;
    private int transferNum;

    DownloadDialog downloadDialog;

    @BindView(R.id.time_and_transfer_num_map)
    MapView mapView;
    @BindView(R.id.tv_time_and_transfer_num_hint)
    TextView tvHint;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_time_and_tranfer;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        onMapInit(savedInstanceState);

        downloadDialog = new DownloadDialog(TimeAndTransferActivity.this);
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
                GetTimeDialog getTimeDialog = new GetTimeDialog(TimeAndTransferActivity.this);
                getTimeDialog.show();
                getTimeDialog.setStatusListener(timeStatusListener);

                if(marker != null) marker.destroy();
                marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(
                        BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.mipmap.self_loc))));
                startPoint = latLng;
                tvHint.setText("选择完毕");
            }
        });
    }

    GetTimeDialog.StatusListener timeStatusListener = new GetTimeDialog.StatusListener()
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
            GetTransferNumDialog getTransferDialog = new GetTransferNumDialog(TimeAndTransferActivity.this);
            getTransferDialog.show();
            getTransferDialog.setStatusListener(transferNumStatusListener);
        }
    };

    GetTransferNumDialog.StatusListener transferNumStatusListener = new GetTransferNumDialog.StatusListener()
    {
        @Override
        public void onDimiss()
        {
            tvHint.setText("给你一个重新选择起点的机会=。=");
        }

        @Override
        public void onConfirm(Integer transferNum)
        {
            TimeAndTransferActivity.this.transferNum = transferNum;
            loadData();
        }
    };

    private void loadData()
    {
        downloadDialog.show();

        OkHttpUtils
                .get()
                .url(UrlUtils.BUS_TIME_AND_TRANSFER_URL)
                .addParams("mins", time + "")
                .addParams("transferNum", transferNum + "")
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
                        BaseApplication.getInstances().setAddressBeanList(addressBeans);

                        startActivity(new Intent(TimeAndTransferActivity.this, MainActivity.class));
                        downloadDialog.dismiss();
                        TimeAndTransferActivity.this.finish();
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
