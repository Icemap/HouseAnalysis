package com.wqz.houseanalysis.bean;

import android.content.res.Resources;
import android.content.res.TypedArray;

import com.wqz.houseanalysis.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wqz on 2018/3/22 0022.
 */

public class MenuBean
{
    private final String listTitle;
    private final String listClass;
    private final String listDes;
    private final int listImageId;

    private MenuBean(int listImageId, String listTitle, String listClass, String listDes)
    {
        this.listImageId = listImageId;
        this.listTitle = listTitle;
        this.listClass = listClass;
        this.listDes = listDes;
    }

    public String getListTitle()
    {
        return listTitle;
    }

    public String getListClass()
    {
        return listClass;
    }

    public String getListDes()
    {
        return listDes;
    }

    public int getListImageId()
    {
        return listImageId;
    }

    public static List<MenuBean> getAllMenus(Resources res) {
        String[] titles = res.getStringArray(R.array.list_titles);
        String[] classes = res.getStringArray(R.array.list_class);
        String[] deses = res.getStringArray(R.array.list_des);
        TypedArray images = res.obtainTypedArray(R.array.list_images);

        int size = titles.length;
        List<MenuBean> paintings = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            final int imageId = images.getResourceId(i, -1);
            paintings.add(new MenuBean(imageId, titles[i], classes[i], deses[i]));
        }
        images.recycle();
        return paintings;
    }
}
