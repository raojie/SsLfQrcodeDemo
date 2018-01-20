package com.sslf.qrcode.common;

/**
 * Method: PreOrderResponeCode
 * Decription:
 * Author: raoj
 * Date: 2017/9/29
 **/
public enum PreOrderResponeCode {
    //业务处理成功，返回二维码
    SUCCESS(0,"SUCCESS","业务处理成功，返回二维码"),
    //处理失败，未能成功获取二维码
    NOQRCODE(1,"NOQRCODE","处理失败，未能成功获取二维码"),
    //数据校验失败
    DATACHECKEXP(2,"DATACHECKEXP","数据校验失败"),
    //数据处理异常
    DATADEALEXP(3,"DATADEALEXP","数据处理异常"),
    //数据异常、获取二维码异常,预交易下单失败，具体原因见提示信息
    DATAEXCEPTION(4,"DATAEXCEPTION","数据异常、获取二维码异常"),
    //请求二维码超时
    TIMEOUT(5,"TIMEOUT","网络请求超时"),
    //用户取消
    USERCANCEL(6,"USERCANCEL","用户取消");

    int key;
    String code;
    String value;

    PreOrderResponeCode(int key, String code, String value) {
        this.key = key;
        this.code = code;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
