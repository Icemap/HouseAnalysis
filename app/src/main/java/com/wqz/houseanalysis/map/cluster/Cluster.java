package com.wqz.houseanalysis.map.cluster;


import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.wqz.houseanalysis.bean.AddressBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 51667 on 2018/3/14.
 */

public class Cluster
{
    private LatLng mLatLng;
    private List<AddressBean> mClusterItems;
    private Marker mMarker;


    Cluster( LatLng latLng) {

        mLatLng = latLng;
        mClusterItems = new ArrayList<AddressBean>();
    }

    void addClusterItem(AddressBean clusterItem) {
        mClusterItems.add(clusterItem);
    }

    int getClusterCount() {
        return mClusterItems.size();
    }



    LatLng getCenterLatLng() {
        return mLatLng;
    }

    void setMarker(Marker marker) {
        mMarker = marker;
    }

    Marker getMarker() {
        return mMarker;
    }

    List<AddressBean> getClusterItems() {
        return mClusterItems;
    }
}
