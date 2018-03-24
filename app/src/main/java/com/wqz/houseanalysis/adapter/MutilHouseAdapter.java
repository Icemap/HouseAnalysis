package com.wqz.houseanalysis.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.AnJuKeHouseBean;
import com.wqz.houseanalysis.bean.LianJiaHouseBean;
import com.wqz.houseanalysis.bean.MultiHouseBean;
import com.wqz.houseanalysis.bean.MultiHouseItemType;
import com.wqz.houseanalysis.utils.ByteBooleanUtils;
import com.wqz.houseanalysis.utils.StringUtils;

import java.util.List;


/**
 * Created by 51667 on 2018/3/19.
 */

public class MutilHouseAdapter extends BaseMultiItemQuickAdapter<MultiHouseBean, BaseViewHolder>
{
    public MutilHouseAdapter(List<MultiHouseBean> data)
    {
        super(data);
        addItemType(MultiHouseItemType.LianJia, R.layout.adapter_lianjia_house);
        addItemType(MultiHouseItemType.AnJuKe, R.layout.adapter_anjuke_house);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiHouseBean itemParent)
    {
        switch (helper.getItemViewType()) {
            case MultiHouseItemType.LianJia:
                LianJiaHouseBean item = itemParent.getLianJiaHouseBean();

                if(!StringUtils.isNullOrEmpty(item.getImgUrl()))
                    Picasso .with(BaseApplication.getInstances())
                            .load(item.getImgUrl())
                            .centerCrop()
                            .resize(300, 200)
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

                break;
            case MultiHouseItemType.AnJuKe:
                AnJuKeHouseBean itemAn = itemParent.getAnJuKeHouseBean();
                if(!StringUtils.isNullOrEmpty(itemAn.getImgurl()))
                    Picasso.with(BaseApplication.getInstances())
                            .load(itemAn.getImgurl())
                            .centerCrop()
                            .resize(300, 200)
                            .into((ImageView)helper.getView(R.id.house_anjuke_image));

                helper  .setText(R.id.house_anjuke_title, itemAn.getTitle())
                        .setText(R.id.house_anjuke_room_num, itemAn.getRoomnum())
                        .setText(R.id.house_anjuke_area, itemAn.getArea() + "m²")
                        .setText(R.id.house_anjuke_flood, itemAn.getFlood())
                        .setText(R.id.house_anjuke_buildtime, itemAn.getBuildtime() + "年")
                        .setText(R.id.house_anjuke_address, itemAn.getAddress())
                        .setText(R.id.house_anjuke_district, "商圈：" + itemAn.getDistrict())
                        .setText(R.id.house_anjuke_total_price, "总价：" + itemAn.getTotalprice() + "万")
                        .setText(R.id.house_anjuke_unit_price, "单价：" + itemAn.getUnitprice() + "元/m²")
                        .setText(R.id.house_anjuke_good_at, "优势：" + itemAn.getGoodat());
                break;
        }
    }
}
