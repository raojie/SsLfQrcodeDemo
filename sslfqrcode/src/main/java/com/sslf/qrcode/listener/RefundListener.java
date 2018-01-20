package com.sslf.qrcode.listener;

import com.sslf.qrcode.respone.RefundRespone;

/**
 * Method: RefundListener
 * Decription:
 * Author: raoj
 * Date: 2017/11/17
 **/
public interface RefundListener {

    /**
     * 过程信息回调
     * @param message 回调信息
     */
    void onProcess(final String message);

    /**
     * 统一下单，支付结果，查询回调
     * @param result true为成功，false为失败
     * @param message 回调信息
     * @param respone 回调结果
     */
    void onRefundResult(final boolean result, final String message, final RefundRespone respone);
}
