package com.wqz.houseanalysis.bean;

import com.amap.api.maps.model.LatLng;
import com.wqz.houseanalysis.activity.map.cluster.ClusterItem;

/**
 * Created by 51667 on 2018/3/13.
 */

public class AddressBean implements ClusterItem
{

    /**
     * id : 2
     * name : 淘金坑住宅小区
     * address : 广东省广州市越秀区淘金坑
     * lon : 113.287217
     * lat : 23.138807
     * src : AnJuKe
     */

    private int id;
    private String name;
    private String address;
    private double lon;
    private double lat;
    private String src;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public double getLon()
    {
        return lon;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public String getSrc()
    {
        return src;
    }

    public void setSrc(String src)
    {
        this.src = src;
    }

    @Override
    public LatLng getPosition()
    {
        return new LatLng(lat, lon);
    }
}
