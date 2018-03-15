package com.wqz.houseanalysis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.activity.sub.BusTimeActivity;
import com.wqz.houseanalysis.activity.sub.FreeDrawActivity;
import com.wqz.houseanalysis.activity.sub.LengthActivity;
import com.wqz.houseanalysis.activity.sub.RectActivity;
import com.wqz.houseanalysis.activity.sub.TimeAndTransferActivity;
import com.wqz.houseanalysis.activity.sub.TransferNumActivity;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.base.BaseImmersiveActivity;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.map.cluster.ClusterOverlay;
import com.wqz.houseanalysis.utils.FileUtils;
import com.wqz.houseanalysis.utils.ScreenUtils;
import com.wqz.houseanalysis.utils.StatusUtils;
import com.wqz.houseanalysis.utils.UrlUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseImmersiveActivity
{
    AMap aMap;
    ClusterOverlay clusterOverlay;

    @BindView(R.id.main_map)
    MapView mapView;
    @BindView(R.id.cl_main_root)
    CoordinatorLayout clMainRoot;
    @BindView(R.id.fab_menu)
    FloatingActionsMenu famMain;
    @BindView(R.id.fab_time)
    FloatingActionButton fabTime;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        onMapInit(savedInstanceState);
    }
    private void onMapResume()
    {
        if (aMap == null)
            aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);

        checkAddressExist();

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.showMyLocation(true);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }

    private void onMapInit(Bundle savedInstanceState)
    {
        mapView.onCreate(savedInstanceState);
    }

    public void checkAddressExist()
    {
        List<AddressActiveStatus> addressBeanList = StatusUtils.getStatusList();
        if(addressBeanList == null || addressBeanList.size() == 0)
            return;

        List<AddressBean> activeStatus = new ArrayList<>();
        for(AddressActiveStatus status : addressBeanList)
        {
            if(status.active)
                activeStatus = status.addressList;
        }

        aMap.clear();
        clusterOverlay = new ClusterOverlay(aMap, activeStatus,
                ScreenUtils.dp2px(getApplicationContext(), 50),
                getApplicationContext());
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
        onMapResume();
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

    @OnClick(R.id.fab_time)
    public void onBusTimeClicked()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, BusTimeActivity.class));
    }

    @OnClick(R.id.fab_transfer)
    public void onBusTransferNumClicked()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, TransferNumActivity.class));
    }

    @OnClick(R.id.fab_bus)
    public void onBusTimeAndTransferNumClicked()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, TimeAndTransferActivity.class));
    }

    @OnClick(R.id.fab_length)
    public void onLengthClicked()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, LengthActivity.class));
    }

    @OnClick(R.id.fab_draw)
    public void onFreeDrawClicked()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, FreeDrawActivity.class));
    }

    @OnClick(R.id.fab_rect)
    public void onRectDrawClicked()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, RectActivity.class));
    }

    @OnClick(R.id.fab_address_list)
    public void onAddressList()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, ListAddressActivity.class));
    }

    @OnClick(R.id.fab_setting)
    public void onSettingClicked()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, ListStatusActivity.class));
    }
}
