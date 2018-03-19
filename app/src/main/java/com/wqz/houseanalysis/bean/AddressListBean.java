package com.wqz.houseanalysis.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 51667 on 2018/3/19.
 */

public class AddressListBean
{
    private List<String> lianjiaAddressList;
    private List<String> anjukeAddressList;

    public List<String> getLianjiaAddressList()
    {
        if(lianjiaAddressList == null)
            lianjiaAddressList = new ArrayList<>();
        return lianjiaAddressList;
    }

    public void setLianjiaAddressList(List<String> lianjiaAddressList)
    {
        this.lianjiaAddressList = lianjiaAddressList;
    }

    public List<String> getAnjukeAddressList()
    {
        if(anjukeAddressList == null)
            anjukeAddressList = new ArrayList<>();
        return anjukeAddressList;
    }

    public void setAnjukeAddressList(List<String> anjukeAddressList)
    {
        this.anjukeAddressList = anjukeAddressList;
    }
}
