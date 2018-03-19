package com.wqz.houseanalysis.utils;

/**
 * Created by 51667 on 2018/3/13.
 */

public class UrlUtils
{
    public static String filePath = "/com.wqz.house/status.json";

    public static String BASE_URL = "http://119.29.56.196:8080/EstateDataKeeper";

    public static String BUS_TIME_URL = BASE_URL + "/analysis/limit/busTime";
    public static String BUS_TRANSFER_URL = BASE_URL + "/analysis/limit/transfer";
    public static String BUS_TIME_AND_TRANSFER_URL = BASE_URL + "/analysis/limit/transferAndBusTime";
    public static String LENGTH_URL = BASE_URL + "/analysis/limit/length";
    public static String FREE_DRAW_URL = BASE_URL + "/analysis/limit/polygon";
    public static String RECT_URL = BASE_URL + "/analysis/limit/rect";
    public static String PARAM_URL = BASE_URL + "/analysis/limit/param";

    public static String DATA_LIANJIA = BASE_URL + "/house/lianjia";
    public static String DATA_ANJUKE = BASE_URL + "/house/anjuke";
    public static String DATA_PARAM = BASE_URL + "/house/getData/byParam";
}
