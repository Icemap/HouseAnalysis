package com.wqz.houseanalysis.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.LianJiaHouseBean;
import com.wqz.houseanalysis.utils.ByteBooleanUtils;

import java.util.List;

/**
 * Created by 51667 on 2018/3/15.
 */

public class LianJiaHouseAdapter extends BaseQuickAdapter<LianJiaHouseBean, BaseViewHolder>
{
    public LianJiaHouseAdapter(@Nullable List<LianJiaHouseBean> data)
    {
        super(R.layout.adapter_lianjia_house, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LianJiaHouseBean item)
    {
        Picasso .with(BaseApplication.getInstances())
                .load(item.getImgUrl())
                .centerCrop()
                .resize(200, 100)
                .into((ImageView)helper.getView(R.id.house_lianjia_image));

        helper.setText(R.id.house_lianjia_title, item.getTitle())
                .setText(R.id.house_lianjia_address, item.getAddress())
                .setText(R.id.house_lianjia_room_num, item.getRoomNum())
                .setText(R.id.house_lianjia_area, item.getArea() + "m²")
                .setText(R.id.house_lianjia_face_to, "朝向：" + item.getFaceto())
                .setText(R.id.house_lianjia_decoration, "装修程度" + item.getRoomNum())
                .setText(R.id.house_lianjia_has_lift, ByteBooleanUtils.byte2Boolean((byte)item.getHaslift())
                    ? "有电梯" : "无电梯")
                .setText(R.id.house_lianjia_flood, item.getFlood())
                .setText(R.id.house_lianjia_buildtime, item.getBuildtime() + "年")
                .setText(R.id.house_lianjia_structure, item.getStructure())
                .setText(R.id.house_lianjia_district, "商圈类型：" + item.getDistrict())
                .setText(R.id.house_lianjia_room_num, item.getRoomNum())
                .setText(R.id.house_lianjia_tax, "免税情况：" + item.getTaxfree())
                .setText(R.id.house_lianjia_total_price, "总价：" + item.getTotalprice() + "万")
                .setText(R.id.house_lianjia_unit_price, "单价：" + item.getUnitprice() + "元/m²");
    }
}
