package com.wqz.houseanalysis.bean;

import java.util.List;

/**
 * Created by 51667 on 2018/3/19.
 */

public class RequestHouseBean
{
    private List<LianJiaHouseBean> lianjiaHouseDataList;
    private List<AnJuKeHouseBean> anjukeHouseDataList;

    public List<LianJiaHouseBean> getLianjiaHouseDataList()
    {
        return lianjiaHouseDataList;
    }

    public void setLianjiaHouseDataList(List<LianJiaHouseBean> lianjiaHouseDataList)
    {
        this.lianjiaHouseDataList = lianjiaHouseDataList;
    }

    public List<AnJuKeHouseBean> getAnjukeHouseDataList()
    {
        return anjukeHouseDataList;
    }

    public void setAnjukeHouseDataList(List<AnJuKeHouseBean> anjukeHouseDataList)
    {
        this.anjukeHouseDataList = anjukeHouseDataList;
    }
}
