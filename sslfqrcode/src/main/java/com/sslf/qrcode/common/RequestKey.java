package com.sslf.qrcode.common;

import com.sslf.qrcode.request.PreOrderRequest;

import java.util.List;

/**
 * Method: RequestKey
 * Decription:
 * Author: raoj
 * Date: 2017/9/29
 **/
public enum  RequestKey {
    termfix_no(0, "termfix_no"),
    mchnt_id(1, "mchnt_id"),
    term_no(2, "term_no"),
    term_order_no(3, "term_order_no"),
    deal_fee(4, "deal_fee"),
    req_flag(5, "req_flag"),
    vege_count(6, "vege_count"),
    sign(7, "sign"),
    vege_info(8, "vege_info"),
    pay_Code(9, "pay_Code"),
    goods_name(10, "goods_name");

    int key;
    String value;

    RequestKey(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
