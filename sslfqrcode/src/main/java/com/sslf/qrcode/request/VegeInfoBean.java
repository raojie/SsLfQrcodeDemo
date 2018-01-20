package com.sslf.qrcode.request;

/**
 * Method: VegeInfoBean
 * Decription:
 * Author: raoj
 * Date: 2017/9/29
 **/
public class VegeInfoBean {

    private String vege_name;//菜名
    private String vege_tracode;//追溯码
    private String vege_weight;//重量
    private String vege_price;//单价
    private String vege_fee;//金额

    public String getVege_name() {
        return vege_name;
    }

    public void setVege_name(String vege_name) {
        this.vege_name = vege_name;
    }

    public String getVege_tracode() {
        return vege_tracode;
    }

    public void setVege_tracode(String vege_tracode) {
        this.vege_tracode = vege_tracode;
    }

    public String getVege_weight() {
        return vege_weight;
    }

    public void setVege_weight(String vege_weight) {
        this.vege_weight = vege_weight;
    }

    public String getVege_price() {
        return vege_price;
    }

    public void setVege_price(String vege_price) {
        this.vege_price = vege_price;
    }

    public String getVege_fee() {
        return vege_fee;
    }

    public void setVege_fee(String vege_fee) {
        this.vege_fee = vege_fee;
    }
}
