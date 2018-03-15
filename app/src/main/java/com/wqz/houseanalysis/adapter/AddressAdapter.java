package com.wqz.houseanalysis.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Wqz on 2018/3/15 0015.
 */

public class AddressAdapter extends BaseQuickAdapter<AddressBean, BaseViewHolder>
{
    public AddressAdapter(@Nullable List<AddressBean> data)
    {
        super(R.layout.adapter_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressBean item)
    {
        Integer resId = R.mipmap.unknown_sign;
        switch (item.getSrc())
        {
            case "AnJuKe":
                resId = R.mipmap.anjuke_sign;
                break;
            case "LianJia":
                resId = R.mipmap.lianjia_sign;
                break;
        }

        helper.setText(R.id.address_title, item.getName())
                .setText(R.id.address_address, item.getAddress())
                .setText(R.id.address_loc, item.getLon() + ", " + item.getLat())
                .setBackgroundRes(R.id.address_src, resId);
    }
}
