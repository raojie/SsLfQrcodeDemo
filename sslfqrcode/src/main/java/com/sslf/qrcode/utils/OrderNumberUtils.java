package com.sslf.qrcode.utils;

import java.text.SimpleDateFormat;

/**
 * Method: OrderNumberUtils
 * Decription: 订单生成工具
 * Author: raoj
 * Date: 2017/9/29
 **/
public class OrderNumberUtils {

    public static String getOrderNo(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = format.format(System.currentTimeMillis());
        return date;
    }

    public static String getOrderNo(String serialNo){
        boolean random = false;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = format.format(System.currentTimeMillis());
        //前六位不重复数字
        String sixFirst = RandomNumberUtils.getRandomStringByLength(6);
        //后六位不重复数字
        String sixLast = RandomNumberUtils.getRandomStringByLength(6);

        while (ifEqual(sixFirst,sixLast)) {
            sixLast = RandomNumberUtils.getRandomStringByLength(6);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date)
                .append(serialNo)
                .append(sixFirst)
                .append(sixLast);
        return stringBuilder.toString();
    }

    public static boolean ifEqual(String sixFirst,String sixLast){
        boolean random = sixFirst.equals(sixLast);
        return random;
    }

}
