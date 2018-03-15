package com.wqz.houseanalysis.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.AnJuKeHouseBean;
import com.wqz.houseanalysis.bean.LianJiaHouseBean;
import com.wqz.houseanalysis.utils.ByteBooleanUtils;
import com.wqz.houseanalysis.utils.StringUtils;

import java.util.List;

/**
 * Created by 51667 on 2018/3/15.
 */

public class AnJuKeAdapter extends BaseQuickAdapter<AnJuKeHouseBean, BaseViewHolder>
{
    public AnJuKeAdapter(@Nullable List<AnJuKeHouseBean> data)
    {
        super(R.layout.adapter_anjuke_house, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AnJuKeHouseBean item)
    {
        if(!StringUtils.isNullOrEmpty(item.getImgurl()))
            Picasso .with(BaseApplication.getInstances())
                    .load(item.getImgurl())
                    .centerCrop()
                    .resize(200, 100)
                    .into((ImageView)helper.getView(R.id.house_anjuke_image));

        helper  .setText(R.id.house_anjuke_title, item.getTitle())
                .setText(R.id.house_anjuke_room_num, item.getRoomnum())
                .setText(R.id.house_anjuke_area, item.getArea() + "m²")
                .setText(R.id.house_anjuke_flood, item.getFlood())
                .setText(R.id.house_anjuke_buildtime, item.getBuildtime() + "年")
                .setText(R.id.house_anjuke_address, item.getAddress())
                .setText(R.id.house_anjuke_district, "商圈：" + item.getDistrict())
                .setText(R.id.house_anjuke_total_price, "总价：" + item.getTotalprice() + "万")
                .setText(R.id.house_anjuke_unit_price, "单价：" + item.getUnitprice() + "元/m²")
                .setText(R.id.house_anjuke_good_at, "优势：" + item.getGoodat());
    }
}
