package com.sslf.qrcode.respone;

/**
 * Method: UnifiedOrderResultRespone
 * Decription:
 * Author: raoj
 * Date: 2017/11/1
 **/
public class UnifiedOrderResultRespone {
    private String term_order_no;
    private String sys_order_no;
    private String result_code;
    private String result_msg;
    private String error_msg;
    private String pay_type;
    private String deal_fee;
    private String sign;

    public String getTerm_order_no() {
        return term_order_no;
    }

    public void setTerm_order_no(String term_order_no) {
        this.term_order_no = term_order_no;
    }

    public String getSys_order_no() {
        return sys_order_no;
    }

    public void setSys_order_no(String sys_order_no) {
        this.sys_order_no = sys_order_no;
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

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getDeal_fee() {
        return deal_fee;
    }

    public void setDeal_fee(String deal_fee) {
        this.deal_fee = deal_fee;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "UnifiedOrderResultRespone{" +
                "term_order_no='" + term_order_no + '\'' +
                ", sys_order_no='" + sys_order_no + '\'' +
                ", result_code='" + result_code + '\'' +
                ", result_msg='" + result_msg + '\'' +
                ", error_msg='" + error_msg + '\'' +
                ", pay_type='" + pay_type + '\'' +
                ", deal_fee='" + deal_fee + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
