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
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.base.BaseImmersiveActivity;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.dialog.DownloadDialog;
import com.wqz.houseanalysis.dialog.GetTimeDialog;
import com.wqz.houseanalysis.dialog.GetTransferNumDialog;
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

public class TransferNumActivity extends BaseImmersiveActivity
{
    AMap aMap;
    Marker marker;
    private LatLng startPoint;
    int transferNum;
    DownloadDialog downloadDialog;

    @BindView(R.id.transfer_num_map)
    MapView mapView;
    @BindView(R.id.tv_transfer_num_hint)
    TextView tvHint;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_transfer_num;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        onMapInit(savedInstanceState);

        downloadDialog = new DownloadDialog(TransferNumActivity.this);
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
                GetTransferNumDialog getTransferDialog = new GetTransferNumDialog(TransferNumActivity.this);
                getTransferDialog.show();
                getTransferDialog.setStatusListener(statusListener);

                if(marker != null) marker.destroy();
                marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(
                        BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(),R.mipmap.self_loc))));
                startPoint = latLng;
                tvHint.setText("选择完毕");
            }
        });
    }

    GetTransferNumDialog.StatusListener statusListener = new GetTransferNumDialog.StatusListener()
    {
        @Override
        public void onDimiss()
        {
            tvHint.setText("给你一个重新选择起点的机会=。=");
        }

        @Override
        public void onConfirm(Integer transferNum)
        {
            TransferNumActivity.this.transferNum = transferNum;
            loadData();
        }
    };

    private void loadData()
    {
        downloadDialog.show();

        OkHttpUtils
                .get()
                .url(UrlUtils.BUS_TRANSFER_URL)
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
                        AddressActiveStatus addressActiveStatus = new AddressActiveStatus();
                        addressActiveStatus.addressList = addressBeans;
                        addressActiveStatus.active = true;
                        addressActiveStatus.analysisDate = new Date();
                        addressActiveStatus.title = "换乘次数限制";
                        addressActiveStatus.param = transferNum + "次";

                        List<AddressActiveStatus> statusList = StatusUtils.getStatusList();
                        ListUtils.setAllNodeStatusUnactive(statusList);
                        statusList.add(addressActiveStatus);
                        StatusUtils.setStatusList(statusList);

                        startActivity(new Intent(TransferNumActivity.this, MainActivity.class));
                        downloadDialog.dismiss();
                        TransferNumActivity.this.finish();
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
