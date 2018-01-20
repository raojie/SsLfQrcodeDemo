package com.sslf.qrcode.common;

/**
 * Method: UnifiedOrderResponseCode
 * Decription:
 * Author: raoj
 * Date: 2017/11/1
 **/
public enum UnifiedOrderResponseCode {
    //统一下单成功
    SUCCESS(5000,"SUCCESS","预下单交易成功"),
    //统一下单失败
    FAIL(5001,"FAIL","预下单交易失败"),
    //统一下单交易成功
    TRADE_SUCCESS(6001,"TRADE_SUCCESS","统一下单交易成功"),
    //统一下单支付处理中
    TRADE_HANDLE(6002,"TRADE_HANDLE","支付处理中"),
    //统一下单交易失败
    TRADE_FAIL(6003,"TRADE_FAIL","支付处理中"),
    //支付异常
    TRADE_EXCEPTION(6004,"TRADE_EXCEPTION","支付异常,原商品订单异常"),
    //支付异常
    QUERY_EXCEPTION(6005,"QUERY_EXCEPTION","查询异常"),
    //请求二维码超时
    TIMEOUT(5,"TIMEOUT","网络请求超时"),
    //用户取消
    USERCANCEL(6,"USERCANCEL","用户取消");

    int key;
    String code;
    String value;

    UnifiedOrderResponseCode(int key, String code, String value) {
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
