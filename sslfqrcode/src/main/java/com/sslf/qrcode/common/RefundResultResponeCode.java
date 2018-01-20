package com.sslf.qrcode.common;

/**
 * Method: RefundResultResponeCode
 * Decription:
 * Author: raoj
 * Date: 2017/11/17
 **/
public enum RefundResultResponeCode {

    //统一下单交易成功
    REFUND_SUCCESS(7001,"REFUND_SUCCESS","退款成功"),
    //统一下单交易失败
    REFUND_FAIL(7002,"REFUND_FAIL","退款失败"),
    //退款异常
    REFUND_EXCERTION(7003,"REFUND_FAIL","退款异常,收单系统出现错误:该笔订单不存在"),
    //统一下单交易失败
    DATA_CHECK_FAIL(9001,"DATA_CHECK_FAIL","数据验证不通过"),
    //请求二维码超时
    TIMEOUT(5,"TIMEOUT","网络请求超时"),
    //用户取消
    USERCANCEL(6,"USERCANCEL","用户取消");

    int key;
    String code;
    String value;

    RefundResultResponeCode(int key, String code, String value) {
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
