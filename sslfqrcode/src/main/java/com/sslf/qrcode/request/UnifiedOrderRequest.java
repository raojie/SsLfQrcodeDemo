package com.sslf.qrcode.request;

/**
 * Method: UnifiedOrderRequest
 * Decription:
 * Author: raoj
 * Date: 2017/10/31
 **/
public class UnifiedOrderRequest {
    private String mchnt_id;//商户号,银联商户号
    private String term_no;//终端号,无则填充””（空字符串）
    private String term_order_no;//终端订单号,2017021612121200（格式:yyyyMMddHHmmss+两位序号(00~99)),同一个终端唯一的标记一笔订单。同一笔订单，如果上次获取二维码失败，再次获取时使用同一个订单号。
    private String deal_fee;//交易总金额,单位：分
    private String req_flag;//请求标记
    private String pay_Code;    //付款码信息	2851897108670722274	扫码枪扫描到的用户付款码信息
    private String goods_name;    //商品名称	食物	支付时显示的订单信息
    private String sign;    //签名

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

    public String getPay_Code() {
        return pay_Code;
    }

    public void setPay_Code(String pay_Code) {
        this.pay_Code = pay_Code;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "UnifiedOrderRequest{" +
                "mchnt_id='" + mchnt_id + '\'' +
                ", term_no='" + term_no + '\'' +
                ", term_order_no='" + term_order_no + '\'' +
                ", deal_fee='" + deal_fee + '\'' +
                ", req_flag='" + req_flag + '\'' +
                ", pay_Code='" + pay_Code + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
