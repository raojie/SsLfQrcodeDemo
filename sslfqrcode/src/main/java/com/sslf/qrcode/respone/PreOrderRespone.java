package com.sslf.qrcode.respone;

/**
 * Method: PreOrderRespone
 * Decription:
 * Author: raoj
 * Date: 2017/9/29
 **/
public class PreOrderRespone {

    /**
     * term_order_no : 2017021612121201
     * result_code : 0
     * result_msg :  SUCCESS
     * qrcode_url : http://XXXXXXX/XXXX
     * sign : 111111
     */

    private String term_order_no;
    private String result_code;
    private String result_msg;
    private String qrcode_url;
    private String sign;

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

    public String getQrcode_url() {
        return qrcode_url;
    }

    public void setQrcode_url(String qrcode_url) {
        this.qrcode_url = qrcode_url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "PreOrderRespone{" +
                "term_order_no='" + term_order_no + '\'' +
                ", result_code='" + result_code + '\'' +
                ", result_msg='" + result_msg + '\'' +
                ", qrcode_url='" + qrcode_url + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
