package com.wqz.houseanalysis.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.bean.AddressListItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Wqz on 2018/3/15 0015.
 */

public class AddressAdapter extends BaseSectionQuickAdapter<AddressListItem, BaseViewHolder>
{
    public AddressAdapter(@Nullable List<AddressListItem> data)
    {
        super(R.layout.adapter_address, R.layout.adapter_string_header, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressListItem item)
    {
        Integer resId = R.mipmap.unknown_sign;
        switch (item.addressBean.getSrc())
        {
            case "AnJuKe":
                resId = R.mipmap.anjuke_sign;
                break;
            case "LianJia":
                resId = R.mipmap.lianjia_sign;
                break;
        }

        helper.setText(R.id.address_title, item.addressBean.getName())
                .setText(R.id.address_address, item.addressBean.getAddress())
                .setBackgroundRes(R.id.address_src, resId);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, AddressListItem item)
    {
        helper.setText(R.id.tv_header, item.header);
    }
}
