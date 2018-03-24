package com.wqz.houseanalysis.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.activity.AnJuKeActivity;
import com.wqz.houseanalysis.activity.ListLianJiaHouseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.base.BaseFragment;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.map.cluster.ClusterClickListener;
import com.wqz.houseanalysis.map.cluster.ClusterOverlay;
import com.wqz.houseanalysis.utils.ScreenUtils;
import com.wqz.houseanalysis.utils.StatusUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AddressMapFragment extends BaseFragment
{
    AMap aMap;
    ClusterOverlay clusterOverlay;

    @BindView(R.id.main_map)
    MapView mapView;
    @BindView(R.id.rl_address_map_root)
    RelativeLayout rlMainRoot;

    @Override
    protected int initLayoutId()
    {
        return R.layout.fragment_address_map;
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
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

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
                ScreenUtils.dp2px(getActivity().getApplicationContext(), 50),
                getActivity().getApplicationContext());
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
                Snackbar.make(rlMainRoot,"打开" + addressBean.getName()
                        + "这个小区吗？",  Snackbar.LENGTH_INDEFINITE)
                        .setAction("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                BaseApplication.getInstances().setCurrentAddress(addressBean);
                                jumpToActivity(addressBean)
                                ;
                            }
                        }).show();
            }
        }
    };


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
        onMapResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void jumpToActivity(AddressBean addressBean)
    {
        switch (addressBean.getSrc())
        {
            case "AnJuKe":
                startActivity(new Intent(getActivity(), AnJuKeActivity.class));
                break;
            case "LianJia":
                startActivity(new Intent(getActivity(), ListLianJiaHouseActivity.class));
                break;
        }
    }
}
