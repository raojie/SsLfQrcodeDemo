package com.sslf.qrcode.listener;

import com.sslf.qrcode.respone.UnifiedOrderRespone;
import com.sslf.qrcode.respone.UnifiedOrderResultRespone;

/**
 * Method: UnifiedOrderListener
 * Decription:
 * Author: raoj
 * Date: 2017/11/1
 **/
public interface UnifiedOrderListener {
    /**
     * 过程信息回调
     * @param message 回调信息
     */
    void onProcess(final String message);

    /**
     * 统一下单，下单结果回调
     * @param result true为成功，false为失败
     * @param message 回调信息
     * @param respone 回调结果
     */
    void onUnifiedResult(final boolean result, final String message, final UnifiedOrderRespone respone);

    /**
     * 统一下单，支付结果，查询回调
     * @param result true为成功，false为失败
     * @param message 回调信息
     * @param respone 回调结果
     */
    void onQueryResult(final boolean result, final String message, final UnifiedOrderResultRespone respone);
}
