package com.sslf.qrcode.common;

/**
 * Method: RequestUrl
 * Decription: 接口请求地址
 * Author: raoj
 * Date: 2017/9/29
 **/
public class RequestUrl {
    //测试环境地址
    public static final String TEST = "https://paytest.sdses.com:8443/scanpay";
    //生产环境地址
    public static final String PRODUCT = "https://pay.sdses.com:8443/scanpay";
    //预下单接口，主扫，C扫B
    public static final String Order_Pre = "app/prePayTransact.do"; //TODO 神思提供的两份文档请求地址不同？？？ trade/prePayTransact.do
    //预下单接口,支付结果查询
    public static final String Pay_Result = "app/paymentQuery.do"; //TODO 神思提供的两份文档请求地址不同？？？ trade/paymentQuery.do
    //统一下单接口，被扫，B扫C
    public static final String Pay_Pre_B_C = "trade/prePayUnifiedTransact.do"; //TODO 神思提供的两份文档请求地址不同？？？ /app/paymentQuery.do
    //预下单接口,支付结果查询
    public static final String Pay_Result_B_C = "trade/paymentQuery.do";
    //退款接口
    public static final String Refund = "trade/paymentRefund.do";

}
