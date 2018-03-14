package com.wqz.houseanalysis.map.cluster;


import com.amap.api.maps.model.LatLng;

/**
 * Created by 51667 on 2018/3/14.
 */

public interface ClusterItem {
    /**
     * 返回聚合元素的地理位置
     *
     * @return
     */
    LatLng getPosition();
}
