package com.java.crm.commens.utils;

import java.util.UUID;

/**
 * 获取UUID
 */
public class UUIDUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
