package com.wqz.houseanalysis.utils;

import java.util.regex.Pattern;

/**
 * Created by 51667 on 2018/3/13.
 */

public class StringUtils
{
    public static boolean isNumeric(String str)
    {
        if(isNullOrEmpty(str)) return false;
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isNullOrEmpty(String str)
    {
        return str == null || str.isEmpty();
    }
}
