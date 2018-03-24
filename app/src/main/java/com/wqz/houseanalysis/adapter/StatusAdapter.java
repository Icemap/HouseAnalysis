package com.wqz.houseanalysis.adapter;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alexvasilkov.android.commons.texts.SpannableBuilder;
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

        if(item == null) return;

        String[] strArray = item.param.split("@#");

        SpannableBuilder builder = new SpannableBuilder(mContext);
        for(int i = 0; i < strArray.length; i++)
        {
            String[] strParamKeyValue = strArray[i].split("__");
            if(strParamKeyValue.length < 2) continue;

            builder.createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                    .append(strArray[i].split("__")[0]).append(": ")
                    .clearStyle()
                    .append(strArray[i].split("__")[1])
                    .append("\n");
        }

        helper.setText(R.id.status_title, item.title)
                .setText(R.id.status_date, format.format(item.analysisDate))
                .setText(R.id.status_param, builder.build())
                .setText(R.id.status_num, "存有数据：" + (item.addressList == null ? 0 : item.addressList.size()))
                .addOnClickListener(R.id.status_delete)
                .setVisible(R.id.v_check_signed, item.active);
    }
}
