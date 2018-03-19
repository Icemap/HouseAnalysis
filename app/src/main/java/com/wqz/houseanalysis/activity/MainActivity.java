package com.wqz.houseanalysis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.base.BaseImmersiveActivity;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.map.cluster.ClusterClickListener;
import com.wqz.houseanalysis.map.cluster.ClusterOverlay;
import com.wqz.houseanalysis.utils.ScreenUtils;
import com.wqz.houseanalysis.utils.SnackUtils;
import com.wqz.houseanalysis.utils.StatusUtils;

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
        clusterOverlay.setOnClusterClickListener(listener);
    }

    ClusterClickListener listener = new ClusterClickListener()
    {
        @Override
        public void onClick(Marker marker, List<AddressBean> clusterItems)
        {
            if(clusterItems != null && clusterItems.size() == 1)
            {
                final AddressBean addressBean = clusterItems.get(0);
                SnackUtils.makeSnackBar(clMainRoot, "打开" + addressBean.getName()
                                + "这个小区吗？", Snackbar.LENGTH_INDEFINITE,
                        famMain, new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        BaseApplication.getInstances().setCurrentAddress(addressBean);
                        jumpToActivity(addressBean);
                    }
                }, "确认");
            }
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

    @OnClick(R.id.fab_param_house)
    public void onParamHouseClicked()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, ParamHouseActivity.class));
    }

    @OnClick(R.id.fab_param)
    public void onParamClicked()
    {
        BaseApplication.getInstances().setCurrentCamera(aMap.getCameraPosition());
        startActivity(new Intent(this, ParamActivity.class));
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

    private void jumpToActivity(AddressBean addressBean)
    {
        switch (addressBean.getSrc())
        {
            case "AnJuKe":
                startActivity(new Intent(MainActivity.this, AnJuKeActivity.class));
                break;
            case "LianJia":
                startActivity(new Intent(MainActivity.this, ListLianJiaHouseActivity.class));
                break;
        }
    }
}
