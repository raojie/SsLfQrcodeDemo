package com.sslf.qrcode.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Method: SignUtils
 * Decription:
 * Author: raoj
 * Date: 2017/9/29
 **/
public class SignUtils {
    /**
     * 对传入的字符串与其本身反向值进行异或处理 然后获取md5值
     *
     * @param signString
     * @return
     * @throws Exception
     */
    public static String encodeSignString(String signString) throws Exception {
        if (signString == null || "".equals(signString)) {
            return SignUtils.encodeSByMd5S("");
        }
        String reversePara = new StringBuffer(signString).reverse().toString();
        byte paraBytes[] = signString.getBytes("utf-8");
        byte reverseBytes[] = reversePara.toString().getBytes("utf-8");
        byte resultBytes[] = new byte[paraBytes.length];
        for (int i = 0; i < paraBytes.length; i++) {
            resultBytes[i] = (byte) (paraBytes[i] ^ reverseBytes[i]);
        }
        return SignUtils.encodeBByMd5S(resultBytes);
    }

    /**
     * 对字节数组进行MD5加密,返回字符串
     *
     * @param msg
     * @return
     */
    public static String encodeBByMd5S(byte[] msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg);
            byte[] b = md.digest();
            return Converter.bytesToHexStringNoSpace(b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 对字符串进行MD5加密，返回字符串
     *
     * @param msg
     * @return
     */
    public static String encodeSByMd5S(String msg) {
        try {
            return SignUtils.encodeBByMd5S(msg.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
