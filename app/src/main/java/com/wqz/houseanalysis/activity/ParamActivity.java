package com.wqz.houseanalysis.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.activity.sub.BusTimeActivity;
import com.wqz.houseanalysis.activity.sub.LengthActivity;
import com.wqz.houseanalysis.activity.sub.TransferNumActivity;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.bean.ToBackEndPointBean;
import com.wqz.houseanalysis.dialog.DownloadDialog;
import com.wqz.houseanalysis.dialog.GetTimeDialog;
import com.wqz.houseanalysis.dialog.GetTransferNumDialog;
import com.wqz.houseanalysis.dialog.LengthDialog;
import com.wqz.houseanalysis.utils.ListUtils;
import com.wqz.houseanalysis.utils.SnackUtils;
import com.wqz.houseanalysis.utils.StatusUtils;
import com.wqz.houseanalysis.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class ParamActivity extends BaseActivity
{

    AMap aMap;
    Marker marker;
    LatLng sP, eP;
    List<LatLng> pointList;
    LatLng cP;
    Integer length;
    Integer transferNum;

    Map<String, String> paramMap;
    DownloadDialog downloadDialog;
    ProcessType currentProcess;

    @BindView(R.id.param_map)
    MapView mapView;
    @BindView(R.id.tv_hint)
    TextView tvHint;

    public enum ProcessType
    {
        RECT_LIMIT,
        FREE_DRAW_LIMIT,
        BASE_CENTER_POINT,
        LENGTH_LIMIT,
        TRANSFER_NUM_LIMIT,
        BUS_TIME_LIMIT
    }

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_param;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        onMapInit(savedInstanceState);
        paramMap = new HashMap<>();
        pointList = new ArrayList<>();

        currentProcess = ProcessType.RECT_LIMIT;

        downloadDialog = new DownloadDialog(ParamActivity.this);
    }

    @OnClick(R.id.tv_confirm)
    public void onConfirm()
    {
        switch (currentProcess)
        {
            case RECT_LIMIT:
                if(sP == null || eP == null)
                {
                    Toast.makeText(ParamActivity.this, "请完成矩形绘制", Toast.LENGTH_SHORT).show();
                    return;
                }
                rectAddParam();
                toNextStep();
                aMap.clear();
                break;
            case FREE_DRAW_LIMIT:
                if(pointList == null || pointList.size() == 0)
                {
                    Toast.makeText(ParamActivity.this, "请完成多边形绘制", Toast.LENGTH_SHORT).show();
                    return;
                }
                freeDrawAddParam();
                toNextStep();
                aMap.clear();
                break;
            case BASE_CENTER_POINT:
                if(cP == null)
                {
                    Toast.makeText(ParamActivity.this, "请选择中心", Toast.LENGTH_SHORT).show();
                    return;
                }
                paramMap.put("originLon", cP.longitude + "");
                paramMap.put("originLat", cP.latitude + "");

                toNextStep();
                aMap.clear();

                LengthDialog lengthDialog = new LengthDialog(ParamActivity.this);
                lengthDialog.show();
                lengthDialog.setStatusListener(statusListener);

                break;
            case LENGTH_LIMIT:
                paramMap.put("length", length + "");
                toNextStep();
                aMap.clear();
                GetTransferNumDialog getTransferDialog = new GetTransferNumDialog(ParamActivity.this);
                getTransferDialog.show();
                getTransferDialog.setStatusListener(transferNumStatusListener);

                break;
            case TRANSFER_NUM_LIMIT:
                paramMap.put("transferNum", transferNum + "");
                toNextStep();
                break;
        }
    }

    @OnClick(R.id.tv_next_step)
    public void toNextStep()
    {
        switch (currentProcess)
        {
            case RECT_LIMIT:
                currentProcess = ProcessType.FREE_DRAW_LIMIT;
                tvHint.setText("自由绘制限制");
                break;
            case FREE_DRAW_LIMIT:
                currentProcess = ProcessType.BASE_CENTER_POINT;
                tvHint.setText("分析中心点选择,为后方分析基础");
                break;
            case BASE_CENTER_POINT:
                currentProcess = ProcessType.LENGTH_LIMIT;
                tvHint.setText("圆形限制");
                if(cP == null)
                {
                    //TODO loadData
                }
                break;
            case LENGTH_LIMIT:
                currentProcess = ProcessType.TRANSFER_NUM_LIMIT;
                tvHint.setText("换乘次数限制");
                break;
            case TRANSFER_NUM_LIMIT:
                currentProcess = ProcessType.BUS_TIME_LIMIT;
                tvHint.setText("公交时间限制");
                break;
        }
    }

    private void rectAddParam()
    {
        Double eLonMin = sP.longitude > eP.longitude ? eP.longitude : sP.longitude;
        Double eLonMax = sP.longitude < eP.longitude ? eP.longitude : sP.longitude;
        Double eLatMin = sP.latitude > eP.latitude ? eP.latitude : sP.latitude;
        Double eLatMax = sP.latitude < eP.latitude ? eP.latitude : sP.latitude;

        paramMap.put("eLonMin", eLonMin + "");
        paramMap.put("eLonMax", eLonMax + "");
        paramMap.put("eLatMin", eLatMin + "");
        paramMap.put("eLatMax", eLatMax + "");
    }

    private void freeDrawAddParam()
    {
        List<ToBackEndPointBean> toEndPointList = new ArrayList<>();
        for(LatLng latLng : pointList)
        {
            toEndPointList.add(new ToBackEndPointBean(latLng.longitude, latLng.latitude));
        }

        paramMap.put("jsonPointList", new Gson().toJson(toEndPointList));
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
                switch (currentProcess)
                {
                    case RECT_LIMIT:
                        if(sP == null)
                            sP = latLng;
                        else if(eP == null)
                            eP = latLng;
                        onDrawRect();
                        break;
                    case FREE_DRAW_LIMIT:
                        pointList.add(latLng);
                        onDrawPolygon();
                        break;
                    case BASE_CENTER_POINT:
                        if(marker != null) marker.destroy();
                        marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(
                                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                        .decodeResource(getResources(),R.mipmap.self_loc))));
                        cP = latLng;
                        break;
                }
            }
        });
    }

    private void onDrawRect()
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

    private List<LatLng> createRectangle()
    {
        List<LatLng> rectList = new ArrayList<>();
        rectList.add(sP);
        rectList.add(new LatLng(sP.latitude, eP.longitude));
        rectList.add(eP);
        rectList.add(new LatLng(eP.latitude, sP.longitude));
        return rectList;
    }

    @OnClick(R.id.iv_clear)
    public void onClear()
    {
        switch (currentProcess)
        {
            case RECT_LIMIT:
                sP = null;
                eP = null;
                onDrawRect();

                break;
            case FREE_DRAW_LIMIT:
                pointList.clear();
                onDrawPolygon();

                break;

            case LENGTH_LIMIT:
                LengthDialog lengthDialog = new LengthDialog(ParamActivity.this);
                lengthDialog.show();
                lengthDialog.setStatusListener(statusListener);
                break;

            case TRANSFER_NUM_LIMIT:
                GetTransferNumDialog getTransferDialog = new GetTransferNumDialog(ParamActivity.this);
                getTransferDialog.show();
                getTransferDialog.setStatusListener(transferNumStatusListener);

        }
    }

    @OnClick(R.id.iv_back)
    public void onBack()
    {
        switch (currentProcess)
        {
            case RECT_LIMIT:
                if(sP == null) return;
                else if(eP == null) sP = null;
                else eP = null;
                onDrawRect();
                break;
            case FREE_DRAW_LIMIT:
                pointList.remove(pointList.size() - 1);
                onDrawPolygon();
                break;
            case LENGTH_LIMIT:
                LengthDialog lengthDialog = new LengthDialog(ParamActivity.this);
                lengthDialog.show();
                lengthDialog.setStatusListener(statusListener);
                break;
            case TRANSFER_NUM_LIMIT:
                GetTransferNumDialog getTransferDialog = new GetTransferNumDialog(ParamActivity.this);
                getTransferDialog.show();
                getTransferDialog.setStatusListener(transferNumStatusListener);
                break;
        }
    }

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
            ParamActivity.this.transferNum = transferNum;
            tvHint.setText("最多忍受" + transferNum + "次换车");
        }
    };

    LengthDialog.StatusListener statusListener = new LengthDialog.StatusListener()
    {
        @Override
        public void onDimiss()
        {
        }

        @Override
        public void onConfirm(Integer length)
        {
            aMap.clear();

            ParamActivity.this.length = length;
            aMap.addCircle(new CircleOptions().
                    center(cP).
                    radius(length * 1000).
                    fillColor(Color.parseColor("#8803a0e2")).
                    strokeColor(Color.parseColor("#03a0e2")).
                    strokeWidth(5));
        }
    };

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