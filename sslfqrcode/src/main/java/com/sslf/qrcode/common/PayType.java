package com.sslf.qrcode.common;

/**
 * Method: PayType
 * Decription:
 * Author: raoj
 * Date: 2017/10/9
 **/
public enum PayType {
    WECHAT(0, "微信支付"),
    ALI(1, "支付宝支付"),
    SCAN(2, "扫码支付"),
    QQWALLET(3, "QQ钱包"),
    BAIDUWALLET(8, "百度钱包"),
    JINGDONGWALLET(9, "京东支付");

    int key;
    String value;

    PayType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
