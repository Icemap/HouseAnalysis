package com.wqz.houseanalysis.bean;

/**
 * Created by Wqz on 2018/3/15 0015.
 */

public class ToBackEndPointBean
{
    public Double lon;
    public Double lat;

    public ToBackEndPointBean()
    {

    }

    public ToBackEndPointBean(Double lon, Double lat)
    {
        this.lat = lat;
        this.lon = lon;
    }
}
