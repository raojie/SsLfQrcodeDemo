package com.sslf.qrcode.common;

/**
 * Method: PayResultResponeCode
 * Decription:
 * Author: raoj
 * Date: 2017/10/12
 **/
public enum PayResultResponeCode {
    //业务处理成功，返回二维码
    SUCCESS(0,"SUCCESS","支付成功"),
    //处理失败，未能成功获取二维码
    FAIL(1,"FAIL","支付失败"),
    //数据校验失败
    NOORDER(2,"NOORDER","查询不到原始订单"),
    //数据处理异常
    NOPAYRESULT(3,"NOPAYRESULT","没有支付结果"),
    //数据异常、获取二维码异常,预交易下单失败，具体原因见提示信息
    DATACHECKEXP(4,"DATACHECKEXP","数据校验失败"),
    //请求二维码超时
    TIMEOUT(5,"TIMEOUT","网络请求超时"),
    //用户取消
    USERCANCEL(6,"USERCANCEL","用户取消");

    int key;
    String code;
    String value;

    PayResultResponeCode(int key, String code, String value) {
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
