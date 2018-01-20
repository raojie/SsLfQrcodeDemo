package com.sslf.qrcode.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Method: Converter
 * Decription:
 * Author: raoj
 * Date: 2017/9/29
 **/
public class Converter {
    /**
     * 把16进制字符串转换成字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        hex = hex.toUpperCase();
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static final String byteToHexString(byte b) {
        return Integer.toHexString(255 & b);
    }

    public static final String bytesToHexString(byte[] bArray) {
        if (bArray != null && bArray.length != 0) {
            StringBuffer sb = new StringBuffer(bArray.length);

            for (int i = 0; i < bArray.length; ++i) {
                String sTemp = Integer.toHexString(255 & bArray[i]);
                if (sTemp.length() < 2) {
                    sb.append(0);
                }
                sb.append(sTemp.toUpperCase());
                sb.append(" ");
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public static final String bytesToHexStringNoSpace(byte[] bArray) {
        if (bArray != null && bArray.length != 0) {
            StringBuffer sb = new StringBuffer(bArray.length);
            for (int i = 0; i < bArray.length; ++i) {
                String sTemp = Integer.toHexString(255 & bArray[i]);
                if (sTemp.length() < 2) {
                    sb.append(0);
                }
                sb.append(sTemp.toUpperCase());
                sb.append("");
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 字符串转换
     * @param hexString 原hex字符串
     * @return 转换后字符串
     * <p>例:"3132333435363738"转换为"12345678"
     */
    public static String hexString2String (String hexString) {
        byte[] byteary = hexStringToByte(hexString);

        Charset cs = Charset.forName ("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate (byteary.length);
        bb.put (byteary);
        bb.flip ();
        CharBuffer cb = cs.decode (bb);

        return cb.toString();
    }

    /**
     * 字符串转换
     * @param string 原字符串
     * @return 转换后字符串
     * <p>"12345678"转换为"3132333435363738"
     */
    public static String string2HexString(String string) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = string.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * 字符串左对齐，右补指定字符.
     *
     * @param fillChar
     *            要补齐的字符
     * @param totalLength
     *            补齐后的长度
     * @param content
     *            要补齐的字符串
     * @return 转换后的字符串
     */
    public static String padLeft(char fillChar, long totalLength, String content) {
        int currentLength = content.length();
        if (currentLength > totalLength) {
            return content;
        } else {
            for (int i = 0; i < totalLength - currentLength; i++) {
                content = content + fillChar;
            }
        }
        return content;
    }

    /**
     * 字符串右对齐，左补指定字符.
     *
     * @param fillChar
     *            要补齐的字符
     * @param totalLength
     *            补齐后的长度
     * @param content
     *            要补齐的字符串
     * @return 转换后的字符串
     */
    public static String padRight(char fillChar, long totalLength,
                                  String content) {
        int currentLength = content.length();
        if (currentLength > totalLength) {
            return content;
        } else {
            for (int i = 0; i < totalLength - currentLength; i++) {
                content = fillChar + content;
            }
        }
        return content;
    }

    /**
     * 转成ascii码字符串
     * @param str
     * @return
     */
    public static String parseAscii(String str){
        StringBuilder sb=new StringBuilder();
        byte[] bs=str.getBytes();
        for(int i=0;i<bs.length;i++)
            sb.append(toHex(bs[i]));
        return sb.toString();
    }

    public static String toHex(int n){
        StringBuilder sb=new StringBuilder();
        if(n/16==0){
            return toHexUtil(n);
        }else{
            String t=toHex(n/16);
            int nn=n%16;
            sb.append(t).append(toHexUtil(nn));
        }
        return sb.toString();
    }

    private static String toHexUtil(int n){
        String rt="";
        switch(n){
            case 10:rt+="A";break;
            case 11:rt+="B";break;
            case 12:rt+="C";break;
            case 13:rt+="D";break;
            case 14:rt+="E";break;
            case 15:rt+="F";break;
            default:
                rt+=n;
        }
        return rt;
    }
}
