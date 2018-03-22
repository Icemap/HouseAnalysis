package com.wqz.houseanalysis.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.android.commons.texts.SpannableBuilder;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.bean.ToBackEndPointBean;
import com.wqz.houseanalysis.dialog.DownloadDialog;
import com.wqz.houseanalysis.dialog.GetAnalysisNameDialog;
import com.wqz.houseanalysis.dialog.GetTimeDialog;
import com.wqz.houseanalysis.dialog.GetTransferNumDialog;
import com.wqz.houseanalysis.dialog.LengthDialog;
import com.wqz.houseanalysis.utils.ListUtils;
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
    Integer time;
    String name;

    Map<String, String> paramMap;
    DownloadDialog downloadDialog;
    ProcessType currentProcess;

    @BindView(R.id.param_map)
    MapView mapView;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    public enum ProcessType
    {
        RECT_LIMIT,
        FREE_DRAW_LIMIT,
        BASE_CENTER_POINT,
        LENGTH_LIMIT,
        TRANSFER_NUM_LIMIT,
        BUS_TIME_LIMIT,
        ANALYSIS_NAME
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

                break;
            case LENGTH_LIMIT:
                paramMap.put("length", length + "000");
                toNextStep();
                aMap.clear();

                break;
            case TRANSFER_NUM_LIMIT:
                paramMap.put("transferNum", transferNum + "");
                toNextStep();

                break;
            case BUS_TIME_LIMIT:
                paramMap.put("mins", time + "");
                toNextStep();
                break;
            case ANALYSIS_NAME:
                loadData();
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
                if(cP == null)
                    loadData();
                currentProcess = ProcessType.LENGTH_LIMIT;
                tvHint.setText("圆形限制");

                LengthDialog lengthDialog = new LengthDialog(ParamActivity.this);
                lengthDialog.show();
                lengthDialog.setStatusListener(statusListener);

                break;
            case LENGTH_LIMIT:
                currentProcess = ProcessType.TRANSFER_NUM_LIMIT;
                tvHint.setText("换乘次数限制");

                GetTransferNumDialog getTransferDialog = new GetTransferNumDialog(ParamActivity.this);
                getTransferDialog.show();
                getTransferDialog.setStatusListener(transferNumStatusListener);
                break;
            case TRANSFER_NUM_LIMIT:
                currentProcess = ProcessType.BUS_TIME_LIMIT;
                tvHint.setText("公交时间限制");

                GetTimeDialog getTimeDialog = new GetTimeDialog(ParamActivity.this);
                getTimeDialog.show();
                getTimeDialog.setStatusListener(timeStatusListener);
                break;
            case BUS_TIME_LIMIT:
                currentProcess = ProcessType.ANALYSIS_NAME;
                tvConfirm.setText("开始分析");
                tvHint.setText("分析名称设置");

                GetAnalysisNameDialog getNameDialog = new GetAnalysisNameDialog(ParamActivity.this);
                getNameDialog.show();
                getNameDialog.setStatusListener(nameListener);
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

        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.getUiSettings().setZoomControlsEnabled(false);

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.showMyLocation(true);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

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

    private void loadData()
    {
        downloadDialog.show();

        OkHttpUtils
                .post()
                .url(UrlUtils.PARAM_URL)
                .params(paramMap)
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
                        addressActiveStatus.title = name;
                        addressActiveStatus.param = param2Str();

                        List<AddressActiveStatus> statusList = StatusUtils.getStatusList();
                        ListUtils.setAllNodeStatusUnactive(statusList);
                        statusList.add(addressActiveStatus);
                        StatusUtils.setStatusList(statusList);

                        downloadDialog.dismiss();
                        ParamActivity.this.finish();
                    }
                });
    }

    private String param2Str()
    {
        StringBuilder sbParam = new StringBuilder();
        if (paramMap.containsKey("eLonMin") && paramMap.containsKey("eLonMax") &&
            paramMap.containsKey("eLatMin") && paramMap.containsKey("eLatMax"))
        {
            sbParam
                    .append("矩形限制__[")
                    .append(paramMap.get("eLonMin")).append(",")
                    .append(paramMap.get("eLonMax")).append(",")
                    .append(paramMap.get("eLatMin")).append(",")
                    .append(paramMap.get("eLatMax")).append("]@#");
        }

        if(cP != null)
        {
            sbParam.append("中心点经纬度__(")
                    .append(cP.longitude).append(",")
                    .append(cP.latitude).append(")@#");
        }

        if(length != null)
        {
            sbParam.append("圆形限制__").append(length).append("000m半径@#");
        }

        if(transferNum != null)
        {
            sbParam.append("换乘次数__").append(transferNum).append("次@#");
        }

        if(time != null)
        {
            sbParam.append("公交时间__").append(time).append("分钟@#");
        }

        if(pointList != null && pointList.size() != 0)
        {
            sbParam.append("自由绘制__");
            for(LatLng latLng : pointList)
            {
                sbParam.append("(").append(latLng.longitude)
                       .append(",").append(latLng.latitude)
                       .append(")");
            }
            sbParam.append("@#");
        }
        return sbParam.toString();
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
                break;

            case ANALYSIS_NAME:
                GetAnalysisNameDialog getAnalysisNameDialog = new GetAnalysisNameDialog(ParamActivity.this);
                getAnalysisNameDialog.show();
                getAnalysisNameDialog.setStatusListener(nameListener);
                break;
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

            case ANALYSIS_NAME:
                GetAnalysisNameDialog getAnalysisNameDialog = new GetAnalysisNameDialog(ParamActivity.this);
                getAnalysisNameDialog.show();
                getAnalysisNameDialog.setStatusListener(nameListener);
                break;
        }
    }

    GetTransferNumDialog.StatusListener transferNumStatusListener = new GetTransferNumDialog.StatusListener()
    {
        @Override
        public void onDimiss()
        {
            tvHint.setText("右下角回退再次打开弹窗");
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
            tvHint.setText("右下角回退再次打开弹窗");
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

    GetAnalysisNameDialog.StatusListener nameListener = new GetAnalysisNameDialog.StatusListener() {
        @Override
        public void onDimiss()
        {
            tvHint.setText("右下角回退再次打开弹窗");
        }

        @Override
        public void onConfirm(String analysisName)
        {
            name = analysisName;
            tvHint.setText("分析名称为:" + analysisName);
        }
    };

    GetTimeDialog.StatusListener timeStatusListener = new GetTimeDialog.StatusListener()
    {
        @Override
        public void onDimiss()
        {
            tvHint.setText("右下角回退再次打开弹窗");
        }

        @Override
        public void onConfirm(Integer mins)
        {
            time = mins;
            tvHint.setText("最大公交时间为:" + time + "分钟");
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
