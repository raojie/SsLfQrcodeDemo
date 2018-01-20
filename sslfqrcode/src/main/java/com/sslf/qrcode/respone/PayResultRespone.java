package com.sslf.qrcode.respone;

/**
 * Method: PayResultRespone
 * Decription:
 * Author: raoj
 * Date: 2017/10/12
 **/
public class PayResultRespone {
    /**
     * mchnt_id : abc12345
     * term_no : abc12345
     * term_order_no : 2107021612121200
     * termfix_no : abc12345
     * sys_order_no : abc12345
     * result_code : 0
     * result_msg : SUCCESS
     * deal_fee : 100
     * pay_type : 0
     * sign : 1111111111111111
     */

    private String mchnt_id;
    private String term_no;
    private String term_order_no;
    private String termfix_no;
    private String sys_order_no;
    private String result_code;
    private String result_msg;
    private String deal_fee;
    private String pay_type;
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

    public String getTermfix_no() {
        return termfix_no;
    }

    public void setTermfix_no(String termfix_no) {
        this.termfix_no = termfix_no;
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

    public String getDeal_fee() {
        return deal_fee;
    }

    public void setDeal_fee(String deal_fee) {
        this.deal_fee = deal_fee;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "PayResultRespone{" +
                "mchnt_id='" + mchnt_id + '\'' +
                ", term_no='" + term_no + '\'' +
                ", term_order_no='" + term_order_no + '\'' +
                ", termfix_no='" + termfix_no + '\'' +
                ", sys_order_no='" + sys_order_no + '\'' +
                ", result_code='" + result_code + '\'' +
                ", result_msg='" + result_msg + '\'' +
                ", deal_fee='" + deal_fee + '\'' +
                ", pay_type='" + pay_type + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
