package com.sslf.qrcode.respone;

/**
 * Method: UnifiedOrderRespone
 * Decription:
 * Author: raoj
 * Date: 2017/10/31
 **/
public class UnifiedOrderRespone {
    private String term_order_no;//终端订单号,2017021612121200（格式:yyyyMMddHHmmss+两位序号(00~99)),同一个终端唯一的标记一笔订单。同一笔订单，如果上次获取二维码失败，再次获取时使用同一个订单号。
    private String result_code;//返回码,业务返回码
    private String result_msg;//提示信息,返回描述信息
    private String error_msg;//具体描述信息,具体成功或失败的描述信息
    private String payInfo;//支付要素,4 微信 5 支付宝
    private String deal_fee;//支付金额,单位：分
    private String payTime;//支付时间,yyyymmddHHmmss
    private String sys_order_no;    //系统订单号	系统订单号，为服务商的订单号
    private String sign;    //签名

    public String getTerm_order_no() {
        return term_order_no;
    }

    public void setTerm_order_no(String term_order_no) {
        this.term_order_no = term_order_no;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getDeal_fee() {
        return deal_fee;
    }

    public void setDeal_fee(String deal_fee) {
        this.deal_fee = deal_fee;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getSys_order_no() {
        return sys_order_no;
    }

    public void setSys_order_no(String sys_order_no) {
        this.sys_order_no = sys_order_no;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "UnifiedOrderRespone{" +
                "term_order_no='" + term_order_no + '\'' +
                ", result_code='" + result_code + '\'' +
                ", result_msg='" + result_msg + '\'' +
                ", error_msg='" + error_msg + '\'' +
                ", payInfo='" + payInfo + '\'' +
                ", deal_fee='" + deal_fee + '\'' +
                ", payTime='" + payTime + '\'' +
                ", sys_order_no='" + sys_order_no + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
