package com.java.crm.commens.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * data数据类型的处理工具类
 */
public class DateUtils {
    public static String formatDateTime(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowStr = sdf.format(date);
        return nowStr;
    }
}
