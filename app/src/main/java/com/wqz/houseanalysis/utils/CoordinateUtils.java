package com.wqz.houseanalysis.utils;

import com.esri.core.geometry.Point;
import com.wqz.houseanalysis.bean.LatLngInfo;

public class CoordinateUtils
{
    public static final String BAIDU_LBS_TYPE = "bd09ll";
    public static double a = 6378245.0D;
    public static double ee = 0.006693421622965943D;

    public static LatLngInfo gps84_To_Gcj02(double lat, double lon)
    {
        if (outOfChina(lat, lon))
        {
            return new LatLngInfo(lat, lon);
        }
        double dLat = transformLat(lon - 105.0D, lat - 35.0D);
        double dLon = transformLon(lon - 105.0D, lat - 35.0D);
        double radLat = lat / 180.0D * 3.141592653589793D;
        double magic = Math.sin(radLat);
        magic = 1.0D - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = dLat * 180.0D
                / (a * (1.0D - ee) / (magic * sqrtMagic) * 3.141592653589793D);
        dLon = dLon * 180.0D
                / (a / sqrtMagic * Math.cos(radLat) * 3.141592653589793D);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new LatLngInfo(mgLat, mgLon);
    }

    public static LatLngInfo gcj_To_Gps84(double lat, double lon)
    {
        LatLngInfo gps = transform(lat, lon);
        double lontitude = lon * 2.0D - gps.longitude;
        double latitude = lat * 2.0D - gps.latitude;
        return new LatLngInfo(latitude, lontitude);
    }

    public static LatLngInfo gcj02_To_Bd09(double gg_lat, double gg_lon)
    {
        double x = gg_lon;
        double y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 2.E-005D
                * Math.sin(y * 3.141592653589793D);
        double theta = Math.atan2(y, x) + 3.E-006D
                * Math.cos(x * 3.141592653589793D);
        double bd_lon = z * Math.cos(theta) + 0.0065D;
        double bd_lat = z * Math.sin(theta) + 0.006D;
        return new LatLngInfo(bd_lat, bd_lon);
    }

    public static LatLngInfo bd09_To_Gcj02(double bd_lat, double bd_lon)
    {
        double x = bd_lon - 0.0065D;
        double y = bd_lat - 0.006D;
        double z = Math.sqrt(x * x + y * y) - 2.E-005D
                * Math.sin(y * 3.141592653589793D);
        double theta = Math.atan2(y, x) - 3.E-006D
                * Math.cos(x * 3.141592653589793D);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new LatLngInfo(gg_lat, gg_lon);
    }

    public static LatLngInfo bd09_To_Gps84(double bd_lat, double bd_lon)
    {
        LatLngInfo gcj02 = bd09_To_Gcj02(bd_lat, bd_lon);
        LatLngInfo map84 = gcj_To_Gps84(gcj02.latitude, gcj02.longitude);

        return map84;
    }

    public static LatLngInfo Gps84_To_bd09(double lat, double lon)
    {
        LatLngInfo gcj02 = gps84_To_Gcj02(lat, lon);
        LatLngInfo map84 = gcj02_To_Bd09(gcj02.latitude, gcj02.longitude);

        return map84;
    }

    public static boolean outOfChina(double lat, double lon)
    {
        if ((lon < 72.004000000000005D) || (lon > 137.8347D))
            return true;
        if ((lat < 0.8293D) || (lat > 55.827100000000002D))
            return true;
        return false;
    }

    public static LatLngInfo transform(double lat, double lon)
    {
        if (outOfChina(lat, lon))
        {
            return new LatLngInfo(lat, lon);
        }
        double dLat = transformLat(lon - 105.0D, lat - 35.0D);
        double dLon = transformLon(lon - 105.0D, lat - 35.0D);
        double radLat = lat / 180.0D * 3.141592653589793D;
        double magic = Math.sin(radLat);
        magic = 1.0D - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = dLat * 180.0D
                / (a * (1.0D - ee) / (magic * sqrtMagic) * 3.141592653589793D);
        dLon = dLon * 180.0D
                / (a / sqrtMagic * Math.cos(radLat) * 3.141592653589793D);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new LatLngInfo(mgLat, mgLon);
    }

    public static double transformLat(double x, double y)
    {
        double ret = -100.0D + 2.0D * x + 3.0D * y + 0.2D * y * y + 0.1D * x
                * y + 0.2D * Math.sqrt(Math.abs(x));

        ret += (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math
                .sin(2.0D * x * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(y * 3.141592653589793D) + 40.0D * Math
                .sin(y / 3.0D * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (160.0D * Math.sin(y / 12.0D * 3.141592653589793D) + 320.0D * Math
                .sin(y * 3.141592653589793D / 30.0D)) * 2.0D / 3.0D;
        return ret;
    }

    public static double transformLon(double x, double y)
    {
        double ret = 300.0D + x + 2.0D * y + 0.1D * x * x + 0.1D * x * y + 0.1D
                * Math.sqrt(Math.abs(x));

        ret += (20.0D * Math.sin(6.0D * x * 3.141592653589793D) + 20.0D * Math
                .sin(2.0D * x * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(x * 3.141592653589793D) + 40.0D * Math
                .sin(x / 3.0D * 3.141592653589793D)) * 2.0D / 3.0D;
        ret += (150.0D * Math.sin(x / 12.0D * 3.141592653589793D) + 300.0D * Math
                .sin(x / 30.0D * 3.141592653589793D)) * 2.0D / 3.0D;

        return ret;
    }

    public static LatLngInfo mercatorToLonLat(double lon, double lat)
    {
        double toX = lon / 20037508.34D * 180.0D;
        double toY = lat / 20037508.34D * 180.0D;
        toY = 57.295779513082323D * (2.0D * Math.atan(Math
                .exp(toY * 3.141592653589793D / 180.0D)) - 1.570796326794897D);
        return new LatLngInfo(toY, toX);
    }

    public static LatLngInfo lonLatToMercator(double lon, double lat)
    {
        double toX = lon * 20037508.34D / 180.0D;
        double toY = Math.log(Math
                .tan((90.0D + lat) * 3.141592653589793D / 360.0D)) / 0.0174532925199433D;
        toY = toY * 20037508.34D / 180.0D;

        return new LatLngInfo(toY, toX);
    }

    public static LatLngInfo mercatorToGcj(double lon, double lat)
    {
        LatLngInfo latLngInfo = mercatorToLonLat(lon, lat);
        return gps84_To_Gcj02(latLngInfo.latitude, latLngInfo.longitude);
    }

    public static LatLngInfo gcjToMercator(double lon, double lat)
    {
        LatLngInfo latLngInfo = gcj_To_Gps84(lat, lon);
        return lonLatToMercator(latLngInfo.longitude, latLngInfo.latitude);
    }

    public static String decimalToDMS(double coord)
    {
        double mod = coord % 1.0D;
        int intPart = (int) coord;

        String degrees = String.valueOf(intPart);
        coord = mod * 60.0D;
        mod = coord % 1.0D;
        intPart = (int) coord;
        if (intPart < 0)
        {
            intPart *= -1;
        }
        String minutes = String.valueOf(intPart);
        coord = mod * 60.0D;
        intPart = (int) coord;
        if (intPart < 0)
        {
            intPart *= -1;
        }
        String seconds = String.valueOf(intPart);
        String output = degrees + "/1," + minutes + "/1," + seconds + "/1";
        return output;
    }

    public static boolean checkGpsCoordinate(double latitude, double longitude)
    {
        return (latitude > -90.0D) && (latitude < 90.0D)
                && (longitude > -180.0D) && (longitude < 180.0D)
                && (latitude != 0.0D) && (longitude != 0.0D);
    }

    public static Point lonLat2Point(LatLngInfo info)
    {
        return new Point(info.longitude, info.latitude);
    }

    public static Point lonLatToMercatorPoint(double lon, double lat)
    {
        double toX = lon * 20037508.34D / 180.0D;
        double toY = Math.log(Math
                .tan((90.0D + lat) * 3.141592653589793D / 360.0D)) / 0.0174532925199433D;
        toY = toY * 20037508.34D / 180.0D;

        return lonLat2Point(new LatLngInfo(toY, toX));
    }

    public static Point gcj02ToMorcatorPoint(double lon, double lat)
    {
        return lonLat2Point(gcjToMercator(lon,lat));
    }

    public static Point wgs84ToGcj02ToMorcatorPoint(double lon, double lat)
    {
        LatLngInfo pos = gps84_To_Gcj02(lat, lon);
        return lonLat2Point(lonLatToMercator(pos.longitude,pos.latitude));
    }
}