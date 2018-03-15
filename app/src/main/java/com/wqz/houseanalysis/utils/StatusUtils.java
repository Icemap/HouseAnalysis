package com.wqz.houseanalysis.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.AddressActiveStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Wqz on 2018/3/15 0015.
 */

public class StatusUtils
{
    public static List<AddressActiveStatus> getStatusList()
    {
        String fileName = "status.json";
        String res="";
        try
        {
            FileInputStream fin = BaseApplication.getInstances()
                    .openFileInput(fileName);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            fin.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(StringUtils.isNullOrEmpty(res))
            return new ArrayList<>();

        return new Gson().fromJson(res, new TypeToken<
                List<AddressActiveStatus>>(){}.getType());
    }

    public static void setStatusList(List<AddressActiveStatus> statusList)
    {
        String str = new Gson().toJson(statusList);
        String fileName = "status.json";
        try
        {
            BaseApplication.getInstances().deleteFile(fileName);

            FileOutputStream fout = BaseApplication.getInstances().
                    openFileOutput(fileName, MODE_PRIVATE);
            byte[] bytes = str.getBytes();
            fout.write(bytes);
            fout.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static AddressActiveStatus getActiveStatus()
    {
        String fileName = "status.json";
        String res="";
        try
        {
            FileInputStream fin = BaseApplication.getInstances()
                    .openFileInput(fileName);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            fin.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(StringUtils.isNullOrEmpty(res))
            return null;

        List<AddressActiveStatus> statusList = new Gson().fromJson(res, new TypeToken<
                List<AddressActiveStatus>>(){}.getType());

        for(AddressActiveStatus status : statusList)
        {
            if(status.active)
                return status;
        }
        return null;
    }
}
