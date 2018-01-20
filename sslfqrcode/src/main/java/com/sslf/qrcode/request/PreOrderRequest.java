package com.sslf.qrcode.request;

import java.util.List;

/**
 * Method: PreOrderRequest
 * Decription:
 * Author: raoj
 * Date: 2017/9/29
 **/
public class PreOrderRequest {

    private String termfix_no;//终端硬件编号,唯一的标记一个终端。
    private String mchnt_id;//商户号,银联商户号
    private String term_no;//终端号,无则填充””（空字符串）
    private String term_order_no;//终端订单号,2017021612121200（格式:yyyyMMddHHmmss+两位序号(00~99)),同一个终端唯一的标记一笔订单。同一笔订单，如果上次获取二维码失败，再次获取时使用同一个订单号。
    private String deal_fee;//交易总金额,单位：分
    private String req_flag;//请求标记
    private String vege_count;//交易详情笔数
    private String sign;//签名
    private String vege_info;//菜品信息

    public String getTermfix_no() {
        return termfix_no;
    }

    public void setTermfix_no(String termfix_no) {
        this.termfix_no = termfix_no;
    }

    public String getMchnt_id() {
        return mchnt_id;
    }

    public void setMchnt_id(String mchnt_id) {
        this.mchnt_id = mchnt_id;
    }

    public String getTerm_no() {
        return term_no;
    }

    public void setTerm_no(String term_no) {
        this.term_no = term_no;
    }

    public String getTerm_order_no() {
        return term_order_no;
    }

    public void setTerm_order_no(String term_order_no) {
        this.term_order_no = term_order_no;
    }

    public String getDeal_fee() {
        return deal_fee;
    }

    public void setDeal_fee(String deal_fee) {
        this.deal_fee = deal_fee;
    }

    public String getReq_flag() {
        return req_flag;
    }

    public void setReq_flag(String req_flag) {
        this.req_flag = req_flag;
    }

    public String getVege_count() {
        return vege_count;
    }

    public void setVege_count(String vege_count) {
        this.vege_count = vege_count;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getVege_info() {
        return vege_info;
    }

    public void setVege_info(String vege_info) {
        this.vege_info = vege_info;
    }

}
