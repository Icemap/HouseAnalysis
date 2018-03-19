package com.wqz.houseanalysis.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 51667 on 2018/3/19.
 */

public class MultiHouseBean implements MultiItemEntity
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
}
