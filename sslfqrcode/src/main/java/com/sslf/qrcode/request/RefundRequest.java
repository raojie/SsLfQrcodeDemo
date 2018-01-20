package com.sslf.qrcode.request;

/**
 * Method: RefundRequest
 * Decription:
 * Author: raoj
 * Date: 2017/11/17
 **/
public class RefundRequest {
    private String mchnt_id;
    private String term_no;
    private String term_order_no;
    private String sign;

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "RefundRequest{" +
                "mchnt_id='" + mchnt_id + '\'' +
                ", term_no='" + term_no + '\'' +
                ", term_order_no='" + term_order_no + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}

