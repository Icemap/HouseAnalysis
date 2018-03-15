package com.wqz.houseanalysis.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.bean.AddressActiveStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Wqz on 2018/3/15 0015.
 */

public class StatusAdapter extends BaseQuickAdapter<AddressActiveStatus, BaseViewHolder>
{
    public StatusAdapter(@Nullable List<AddressActiveStatus> data)
    {
        super(R.layout.adapter_status,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressActiveStatus item)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        helper.setText(R.id.status_title, item.title)
                .setText(R.id.status_date, format.format(item.analysisDate))
                .setText(R.id.status_param, "参数：" + item.param)
                .setText(R.id.status_num, "存有数据：" + item.addressList.size())
                .addOnClickListener(R.id.status_delete)
                .setVisible(R.id.v_check_signed, item.active);
    }
}
