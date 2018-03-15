package com.wqz.houseanalysis.bean;

/**
 * Created by 51667 on 2018/3/15.
 */

public class AnJuKeHouseBean
{
    /**
     * id : 444
     * title : 麓湖御景高层，116方580万，东南向安静，间隔方正，三房带
     * imgurl : https://pic1.ajkimg.com/display/hj/4742a16fdc7079fbd2451355bcfdc815/240x180m.jpg?t=1
     * contenturl : https://guangzhou.anjuke.com/prop/view/A1140555853
     * roomnum : 3室2厅
     * area : 116.0
     * flood : 高层(共31层)
     * buildtime : 2009
     * address : 麓湖御景
     * district : 越秀-淘金-麓苑路39号
     * totalprice : 580.0
     * unitprice : 50000.0
     * goodat : 近地铁
     */

    private int id;
    private String title;
    private String imgurl;
    private String contenturl;
    private String roomnum;
    private double area;
    private String flood;
    private int buildtime;
    private String address;
    private String district;
    private double totalprice;
    private double unitprice;
    private String goodat;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getImgurl()
    {
        return imgurl;
    }

    public void setImgurl(String imgurl)
    {
        this.imgurl = imgurl;
    }

    public String getContenturl()
    {
        return contenturl;
    }

    public void setContenturl(String contenturl)
    {
        this.contenturl = contenturl;
    }

    public String getRoomnum()
    {
        return roomnum;
    }

    public void setRoomnum(String roomnum)
    {
        this.roomnum = roomnum;
    }

    public double getArea()
    {
        return area;
    }

    public void setArea(double area)
    {
        this.area = area;
    }

    public String getFlood()
    {
        return flood;
    }

    public void setFlood(String flood)
    {
        this.flood = flood;
    }

    public int getBuildtime()
    {
        return buildtime;
    }

    public void setBuildtime(int buildtime)
    {
        this.buildtime = buildtime;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getDistrict()
    {
        return district;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public double getTotalprice()
    {
        return totalprice;
    }

    public void setTotalprice(double totalprice)
    {
        this.totalprice = totalprice;
    }

    public double getUnitprice()
    {
        return unitprice;
    }

    public void setUnitprice(double unitprice)
    {
        this.unitprice = unitprice;
    }

    public String getGoodat()
    {
        return goodat;
    }

    public void setGoodat(String goodat)
    {
        this.goodat = goodat;
    }
}
