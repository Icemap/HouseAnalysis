package com.wqz.houseanalysis.utils;

import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.bean.AddressListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 51667 on 2018/3/24.
 */

public class SortUtils
{
    public static List<AddressListItem> getSortedByAddress()
    {
        AddressActiveStatus addressActiveStatus = StatusUtils.getActiveStatus();
        List<AddressListItem> result = new ArrayList<>();
        if(addressActiveStatus == null || addressActiveStatus.addressList == null)
            return result;

        //Build Keys
        List<String> sortKeys = new ArrayList<>();
        for(AddressBean addressBean : addressActiveStatus.addressList)
        {
            String sortKey = getSortKey(addressBean.getAddress());
            if(!sortKeys.contains(sortKey))
                sortKeys.add(sortKey);
        }

        for(String sortKey : sortKeys)
        {
            result.add(new AddressListItem(true, sortKey));
            for(AddressBean addressBean : addressActiveStatus.addressList)
            {
                if(addressBean.getAddress().contains(sortKey))
                    result.add(new AddressListItem(addressBean));
            }
        }

        return result;
    }

    private static String getSortKey(String str)
    {
        if(StringUtils.isNullOrEmpty(str)) return "";

        String key = str.substring(str.indexOf("市") + 1, str.indexOf("区") + 1);

        return StringUtils.isNullOrEmpty(key)? "" : key;
    }
}
