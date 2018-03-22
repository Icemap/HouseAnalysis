package com.wqz.houseanalysis.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.MenuBean;

import java.util.List;

public class MenuAdapter extends BaseQuickAdapter<MenuBean, BaseViewHolder>
{
    public MenuAdapter(@Nullable List<MenuBean> data)
    {
        super(R.layout.adapter_menu_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MenuBean item)
    {
        Picasso.with(BaseApplication.getInstances())
                .load(item.getListImageId())
                .centerCrop()
                .resize(400, 200)
                .into((ImageView)helper.getView(R.id.list_item_image));

        helper .setText(R.id.list_item_title, item.getListTitle())
               .setText(R.id.list_item_res, item.getListDes());
    }
}
