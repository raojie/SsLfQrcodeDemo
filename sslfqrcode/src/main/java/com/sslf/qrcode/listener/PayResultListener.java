package com.sslf.qrcode.listener;

import com.sslf.qrcode.respone.PayResultRespone;
import com.sslf.qrcode.respone.PreOrderRespone;

/**
 * Method: PayResultListener
 * Decription:
 * Author: raoj
 * Date: 2017/10/12
 **/
public interface PayResultListener {
    /**
     * 过程信息回调
     * @param message 回调信息
     */
    void onProcess(final String message);

    /**
     * 二维码回调
     * @param qrCode 二维码信息
     */
    void onQrcode(final String qrCode);

    /**
     * 二维码请求回调
     * @param result true为成功，false为失败
     * @param message 回调信息
     * @param preOrderRespone 回调结果
     */
    void onQrcodeResult(final boolean result, final String message, final PreOrderRespone preOrderRespone);

    /**
     * 支付结果查询过程回调
     * @param message 回调信息
     * @param payResultRespone 回调结果
     */
    void onQueryResult(final String message, final PayResultRespone payResultRespone);


    /**
     * 支付结果查询结果回调
     * @param result true为成功，false为失败
     * @param message 回调信息
     * @param payResultRespone 回调结果
     */
    void onResult(final boolean result, final String message, final PayResultRespone payResultRespone);
}
