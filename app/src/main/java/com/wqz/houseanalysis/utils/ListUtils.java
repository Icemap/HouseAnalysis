package com.wqz.houseanalysis.utils;

import com.wqz.houseanalysis.bean.AddressActiveStatus;

import java.util.List;

/**
 * Created by Wqz on 2018/3/15 0015.
 */

public class ListUtils
{
    public static void setAllNodeStatusUnactive(List<AddressActiveStatus> statusList)
    {
        for(AddressActiveStatus status : statusList)
        {
            status.active = false;
        }
    }
}
