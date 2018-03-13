package com.wqz.houseanalysis.activity.map.cluster;

import android.graphics.drawable.Drawable;

/**
 * Created by 51667 on 2018/3/14.
 */

public interface ClusterRender
{
    /**
     * 根据聚合点的元素数目返回渲染背景样式
     *
     * @param clusterNum
     * @return
     */
    Drawable getDrawAble(int clusterNum);
}
