package com.wqz.houseanalysis.bean;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 51667 on 2018/3/19.
 */

public class MultiHouseBean implements MultiItemEntity, Comparable
{
    private int entityType;
    private AnJuKeHouseBean anJuKeHouseBean;
    private LianJiaHouseBean lianJiaHouseBean;

    public MultiHouseBean(int entityType)
    {
        this.entityType = entityType;
    }

    @Override
    public int getItemType()
    {
        return entityType;
    }


    public AnJuKeHouseBean getAnJuKeHouseBean()
    {
        return anJuKeHouseBean;
    }

    public void setAnJuKeHouseBean(AnJuKeHouseBean anJuKeHouseBean)
    {
        this.anJuKeHouseBean = anJuKeHouseBean;
    }

    public LianJiaHouseBean getLianJiaHouseBean()
    {
        return lianJiaHouseBean;
    }

    public void setLianJiaHouseBean(LianJiaHouseBean lianJiaHouseBean)
    {
        this.lianJiaHouseBean = lianJiaHouseBean;
    }

    @Override
    public int compareTo(@NonNull Object o)
    {
        int thisPrice = 0;
        int comparePrice = 0;
        MultiHouseBean compareBean = (MultiHouseBean)o;

        if(this.entityType == MultiHouseItemType.LianJia)
            thisPrice = (int)this.getLianJiaHouseBean().getTotalprice();
        else if(this.entityType == MultiHouseItemType.AnJuKe)
            thisPrice = (int)this.getAnJuKeHouseBean().getTotalprice();

        if(compareBean.entityType == MultiHouseItemType.LianJia)
            comparePrice = (int)compareBean.getLianJiaHouseBean().getTotalprice();
        else if(compareBean.entityType == MultiHouseItemType.AnJuKe)
            comparePrice = (int)compareBean.getAnJuKeHouseBean().getTotalprice();

        return thisPrice - comparePrice;
    }
}
