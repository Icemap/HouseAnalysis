package com.wqz.houseanalysis.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.activity.map.cluster.Cluster;
import com.wqz.houseanalysis.activity.map.cluster.ClusterItem;
import com.wqz.houseanalysis.activity.map.cluster.ClusterOverlay;
import com.wqz.houseanalysis.activity.sub.BusTimeActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.base.BaseImmersiveActivity;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
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

    @BindDrawable(R.drawable.ic_place_orange_24dp)
    Drawable selfLoc;
    @BindDrawable(R.drawable.ic_business_blue_12dp)
    Drawable addressLoc;

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

    private void onMapInit(Bundle savedInstanceState)
    {
        mapView.onCreate(savedInstanceState);

        if (aMap == null)
            aMap = mapView.getMap();

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

    @OnClick(R.id.fab_time)
    public void onBusTimeClicked()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, BusTimeActivity.class));
        finish();
    }

    public void checkAddressExist()
    {
        List<AddressBean> addressBeanList =  BaseApplication.getInstances().getAddressBeanList();
        if(addressBeanList == null || addressBeanList.size() == 0)
            return;

        clusterOverlay = new ClusterOverlay(aMap, addressBeanList,
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
