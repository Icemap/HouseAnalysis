package com.wqz.houseanalysis.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by 51667 on 2018/3/25.
 */

public class AddressListItem extends SectionEntity<AddressBean>
{
    public AddressBean addressBean;

    public AddressListItem(boolean isHeader, String header)
    {
        super(isHeader, header);
    }

    public AddressListItem(AddressBean addressBean)
    {
        super(addressBean);
        this.addressBean = addressBean;
    }
}
