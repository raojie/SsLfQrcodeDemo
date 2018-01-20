package com.sslf.qrcode.common;

/**
 * Method: RequestFlag
 * Decription: 请求标记字典
 * Author: raoj
 * Date: 2017/9/29
 **/
public enum RequestFlag {
    ONE(1,"首次请求"),
    TWO(2,"上次未成功获取二维码，再次获取");

    int key;
    String value;

    RequestFlag(int key, String value) {
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
