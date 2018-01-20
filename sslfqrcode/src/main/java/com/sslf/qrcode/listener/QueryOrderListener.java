package com.sslf.qrcode.listener;


import com.sslf.qrcode.respone.QueryOrderRespone;

/**
 * Method: QueryOrderListener
 * Decription:
 * Author: raoj
 * Date: 2017/10/9
 **/
public interface QueryOrderListener {
    /**
     * 过程回调
     * @param message 回调信息
     */
    void onProcess(final String message);

    /**
     * 读结果回调
     * @param result true为成功，false为失败
     * @param message 回调信息
     * @param queryOrderRespone 回调结果
     */
    void onResult(final boolean result, final String message, final QueryOrderRespone queryOrderRespone);
}
