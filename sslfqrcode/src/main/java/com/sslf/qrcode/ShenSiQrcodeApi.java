package com.sslf.qrcode;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.sslf.qrcode.common.PayResultResponeCode;
import com.sslf.qrcode.common.PreOrderResponeCode;
import com.sslf.qrcode.common.RefundResultResponeCode;
import com.sslf.qrcode.common.RequestKey;
import com.sslf.qrcode.common.RequestType;
import com.sslf.qrcode.common.RequestUrl;
import com.sslf.qrcode.common.UnifiedOrderResponseCode;
import com.sslf.qrcode.listener.PayResultListener;
import com.sslf.qrcode.listener.QueryOrderListener;
import com.sslf.qrcode.listener.PreOrderListener;
import com.sslf.qrcode.listener.RefundListener;
import com.sslf.qrcode.listener.UnifiedOrderListener;
import com.sslf.qrcode.request.PayResultRequest;
import com.sslf.qrcode.request.QueryOrderRequset;
import com.sslf.qrcode.request.PreOrderRequest;
import com.sslf.qrcode.request.RefundRequest;
import com.sslf.qrcode.request.UnifiedOrderQueryRequest;
import com.sslf.qrcode.request.UnifiedOrderRequest;
import com.sslf.qrcode.respone.PayResultRespone;
import com.sslf.qrcode.respone.QueryOrderRespone;
import com.sslf.qrcode.respone.PreOrderRespone;
import com.sslf.qrcode.respone.RefundRespone;
import com.sslf.qrcode.respone.UnifiedOrderRespone;
import com.sslf.qrcode.respone.UnifiedOrderResultRespone;
import com.sslf.qrcode.utils.OkHttpUtils;
import com.sslf.qrcode.utils.SignUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Method: ShenSiQrcodeApi
 * Decription:
 * Author: raoj
 * Date: 2017/9/29
 **/
public class ShenSiQrcodeApi {

    private static final String TAG = "ShenSiQrcodeApi";
    private PreOrderListener preOrderListener;
    private QueryOrderListener queryOrderListener;
    private PayResultListener payResultListener;
    private UnifiedOrderListener unifiedOrderListener;
    private RefundListener refundListener;

    private boolean userCancel = false;
    private boolean mightHas_C_B_PayResult = false; //扫码支付是否有支付结果
    private boolean mightHas_B_C_PayResult = false; //付款码支付是否有支付结果
    private boolean mightHasRefundResult = false; //付款码支付是否有支付结果

    private OkHttpClient payClient = null;
    private OkHttpClient unifiedOrderClient = null;
    private OkHttpClient pollingOrderClient = null;
    private OkHttpClient refundClient = null;
    private Call payCall = null;
    private Call unifiedOrderCall = null;
    private Call pollingOrderCall = null;
    private Call refundCall = null;
    private int tmo;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private Handler mHandler;//全局变量
    private QueryOrderRequset queryOrderRequset;
    private UnifiedOrderQueryRequest unifiedOrderQueryRequest;
    private RefundRequest refundRequest;
    private Context mContext;
    //开始时间
    private long startTime;
    //剩余时间
    private long remainTime;

    private String currentPlatform = RequestUrl.PRODUCT; //当前支付平台,默认为生产环境平台

    public ShenSiQrcodeApi(Context context) {
        this.mContext = context;
    }

    public void setRefundListener(RefundListener listener) {
        if (refundListener == null) {
            this.refundListener = listener;
        }
    }

    public void setPreOrderListener(PreOrderListener listener) {
        if (preOrderListener == null) {
            this.preOrderListener = listener;
        }
    }

    public void setUnifiedOrderListener(UnifiedOrderListener listener) {
        if (unifiedOrderListener == null) {
            this.unifiedOrderListener = listener;
        }
    }

    public void setQueryOrderListener(QueryOrderListener listener) {
        if (queryOrderListener == null) {
            this.queryOrderListener = listener;
        }
    }

    public void setPayResultListener(PayResultListener listener) {
        if (payResultListener == null) {
            this.payResultListener = listener;
        }
    }

    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }};

    private HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private SSLContext getsslContext(){
        SSLContext sslContext = null;
        try {
            //构造自己的SSLContext
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    private RefundRespone refundRespone = null;
    public synchronized void refund(final RefundRequest refundRequest, final int timeout_secs) {
        startTime = System.currentTimeMillis();
        remainTime = timeout_secs * 1000;
        userCancel = false;
        String url = null;
        try {
            if (currentPlatform == RequestUrl.TEST) {
                url = RequestUrl.TEST + "/" + RequestUrl.Refund;
            } else {
                url = RequestUrl.PRODUCT + "/" + RequestUrl.Refund;
            }
            if (refundListener == null) {
                return;
            }
            if (refundRespone == null) {
                refundRespone = new RefundRespone();
            }
            //===============================================
//            refundRequest = new UnifiedOrderQueryRequest();
//            unifiedOrderQueryRequest.setMchnt_id(unifiedOrderRequest.getMchnt_id());
//            unifiedOrderQueryRequest.setTerm_no(unifiedOrderRequest.getTerm_no());
//            unifiedOrderQueryRequest.setTerm_order_no(unifiedOrderRequest.getTerm_order_no());
//
//            StringBuilder signStr = new StringBuilder();
//            signStr.append(unifiedOrderQueryRequest.getTerm_order_no())
//                    .append(unifiedOrderQueryRequest.getMchnt_id())
//                    .append(unifiedOrderQueryRequest.getTerm_no());
//            String sign = SignUtils.encodeSignString(signStr.toString());
//            unifiedOrderQueryRequest.setSign(sign);
            //===============================================

            refundClient = OkHttpUtils.getInstance().getOkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add(RequestKey.mchnt_id.getValue(), refundRequest.getMchnt_id())
                    .add(RequestKey.term_no.getValue(), refundRequest.getTerm_no())
                    .add(RequestKey.term_order_no.getValue(), refundRequest.getTerm_order_no())
                    .add(RequestKey.sign.getValue(), refundRequest.getSign())
                    .build();
            final Call call = refundClient.newCall(getRequest(url, requestBody));
            refundCall = call;
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (userCancel) {
                        userCancel = false;
                        //如果是主动取消的情况下
                        refundListener.onProcess("用户取消...");
                        refundListener.onRefundResult(false, RefundResultResponeCode.USERCANCEL.getCode(), refundRespone);
//                        Log.i(TAG, "payCall--->onFailure--->用户取消");
                        return;
                    }
//                    Log.i(TAG, "payCall--->onFailure--->请求二维码失败");
                    refundListener.onProcess("退款失败...");
                    refundListener.onRefundResult(false, RefundResultResponeCode.REFUND_FAIL.getCode(), refundRespone);
                    return;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    Log.e(TAG, "退款返回:" + str);
                    if (response.code() == 200) {
                        refundRespone = new Gson().fromJson(str, RefundRespone.class);
                        if (Integer.valueOf(refundRespone.getResult_code()).equals(RefundResultResponeCode.REFUND_SUCCESS.getKey())) {
                            refundListener.onRefundResult(true, RefundResultResponeCode.REFUND_SUCCESS.getCode(), refundRespone);
                            return;
                        } else if (Integer.valueOf(refundRespone.getResult_code()).equals(RefundResultResponeCode.DATA_CHECK_FAIL.getKey())) {
                            refundListener.onRefundResult(true, RefundResultResponeCode.DATA_CHECK_FAIL.getCode(), refundRespone);
                            return;
                        } else {
                            refundListener.onRefundResult(false, RefundResultResponeCode.REFUND_FAIL.getCode(), refundRespone);
                            return;
                        }
                    } else {
                        refundListener.onProcess("网络请求成功，退款失败...");
                        refundListener.onRefundResult(false, RefundResultResponeCode.REFUND_FAIL.getCode(), refundRespone);
                        return;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UnifiedOrderRespone unifiedOrderResponse = null;
    private UnifiedOrderResultRespone unifiedOrderResultRespone = null;
    public synchronized void unifiedOrder(final UnifiedOrderRequest unifiedOrderRequest, final int timeout_secs) {
        startTime = System.currentTimeMillis();
        remainTime = timeout_secs * 1000;
        userCancel = false;
        String url = null;
        try {
            setMightHas_B_C_PayResult(false);
            if (currentPlatform == RequestUrl.TEST) {
                url = RequestUrl.TEST + "/" + RequestUrl.Pay_Pre_B_C;
            } else {
                url = RequestUrl.PRODUCT + "/" + RequestUrl.Pay_Pre_B_C;
            }
            if (unifiedOrderListener == null) {
                return;
            }
            if (unifiedOrderRequest.getPay_Code() == null) {
                unifiedOrderListener.onUnifiedResult(false, "请求条码码串信息错误", unifiedOrderResponse);
                return;
            }
            if (unifiedOrderResponse == null) {
                unifiedOrderResponse = new UnifiedOrderRespone();
            }
            //===============================================
            unifiedOrderQueryRequest = new UnifiedOrderQueryRequest();
            unifiedOrderQueryRequest.setMchnt_id(unifiedOrderRequest.getMchnt_id());
            unifiedOrderQueryRequest.setTerm_no(unifiedOrderRequest.getTerm_no());
            unifiedOrderQueryRequest.setTerm_order_no(unifiedOrderRequest.getTerm_order_no());

            StringBuilder signStr = new StringBuilder();
            signStr.append(unifiedOrderQueryRequest.getTerm_order_no())
                    .append(unifiedOrderQueryRequest.getMchnt_id())
                    .append(unifiedOrderQueryRequest.getTerm_no());
            String sign = SignUtils.encodeSignString(signStr.toString());
            unifiedOrderQueryRequest.setSign(sign);
            //===============================================

            unifiedOrderClient = OkHttpUtils.getInstance().getOkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add(RequestKey.mchnt_id.getValue(), unifiedOrderRequest.getMchnt_id())
                    .add(RequestKey.term_no.getValue(), unifiedOrderRequest.getTerm_no())
                    .add(RequestKey.term_order_no.getValue(), unifiedOrderRequest.getTerm_order_no())
                    .add(RequestKey.deal_fee.getValue(), unifiedOrderRequest.getDeal_fee())
                    .add(RequestKey.req_flag.getValue(), unifiedOrderRequest.getReq_flag())
                    .add(RequestKey.pay_Code.getValue(), unifiedOrderRequest.getPay_Code())
                    .add(RequestKey.goods_name.getValue(), unifiedOrderRequest.getGoods_name().toString())
                    .add(RequestKey.sign.getValue(), unifiedOrderRequest.getSign())
                    .build();
            final Call call = unifiedOrderClient.newCall(getRequest(url, requestBody));
            unifiedOrderCall = call;
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (userCancel) {
                        userCancel = false;
                        //如果是主动取消的情况下
                        unifiedOrderListener.onProcess("用户取消...");
                        unifiedOrderListener.onUnifiedResult(false, PreOrderResponeCode.USERCANCEL.getCode(), unifiedOrderResponse);
//                        Log.i(TAG, "payCall--->onFailure--->用户取消");
                        return;
                    }
//                    Log.i(TAG, "payCall--->onFailure--->请求二维码失败");
                    unifiedOrderListener.onProcess("统一下单失败...");
                    unifiedOrderListener.onUnifiedResult(false, UnifiedOrderResponseCode.FAIL.getCode(), unifiedOrderResponse);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    if (response.code() == 200) {
                        unifiedOrderResponse = new Gson().fromJson(str, UnifiedOrderRespone.class);
                        if (Integer.valueOf(unifiedOrderResponse.getResult_code()).equals(UnifiedOrderResponseCode.SUCCESS.getKey())) { // 下单成功
                            unifiedOrderListener.onProcess("统一下单成功...");
                            unifiedOrderListener.onUnifiedResult(true, UnifiedOrderResponseCode.SUCCESS.getCode(), unifiedOrderResponse);
                            while (true) {
                                if (userCancel) {
                                    userCancel = false;
                                    unifiedOrderListener.onProcess("用户取消...");
                                    unifiedOrderListener.onUnifiedResult(false, UnifiedOrderResponseCode.USERCANCEL.getCode(), unifiedOrderResponse);
                                    //Log.i(TAG, "payCall--->onResponse--->用户取消");
                                    return;
                                }
                                if (isMightHas_B_C_PayResult()) {
                                    Log.e(TAG, "isMightHas_B_C_PayResult == true");
                                    setMightHas_B_C_PayResult(false);
                                    return;
                                } else {
                                    Log.e(TAG, "isMightHas_B_C_PayResult == false");
                                    if (!checkTimeOut()) {
                                        pollingQueryOrder(RequestType.B_SCAN_C, unifiedOrderQueryRequest);
                                    } else {
                                        unifiedOrderListener.onProcess("交易超时...");
                                        unifiedOrderListener.onUnifiedResult(false, UnifiedOrderResponseCode.TIMEOUT.getCode(), unifiedOrderResponse);
//                                    Log.i(TAG, "payCall--->onResponse--->交易超时");
                                        return;
                                    }
                                }
                                try {
                                    Thread.sleep(3000); //每3秒轮询一次支付结果
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        unifiedOrderListener.onProcess("网络请求成功，统一下单失败...");
                        unifiedOrderListener.onUnifiedResult(false, UnifiedOrderResponseCode.FAIL.getCode(), unifiedOrderResponse);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void pollingQueryOrder(int type, final UnifiedOrderQueryRequest unifiedOrderQueryRequest){
        String url = null;
        try {
            if (currentPlatform == RequestUrl.TEST) { //测试环境
                if (type == RequestType.B_SCAN_C) {
                    url = RequestUrl.TEST + "/" + RequestUrl.Pay_Result_B_C;
                } else {
                    url = RequestUrl.TEST + "/" + RequestUrl.Pay_Result;
                }
            } else {
                if (type == RequestType.B_SCAN_C) { //生产环境
                    url = RequestUrl.PRODUCT + "/" + RequestUrl.Pay_Result_B_C;
                } else {
                    url = RequestUrl.PRODUCT + "/" + RequestUrl.Pay_Result;
                }
            }
            if (unifiedOrderListener == null) {
                return;
            }
            if (unifiedOrderResultRespone == null) {
                unifiedOrderResultRespone = new UnifiedOrderResultRespone();
            }
            unifiedOrderListener.onProcess("统一下单支付结果查询...");
            pollingOrderClient = OkHttpUtils.getInstance().getOkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add(RequestKey.mchnt_id.getValue(), unifiedOrderQueryRequest.getMchnt_id())
                    .add(RequestKey.term_no.getValue(), unifiedOrderQueryRequest.getTerm_no())
                    .add(RequestKey.term_order_no.getValue(), unifiedOrderQueryRequest.getTerm_order_no())
                    .add(RequestKey.sign.getValue(), unifiedOrderQueryRequest.getSign())
                    .build();
            final Call call = pollingOrderClient.newCall(getRequest(url, requestBody));
            pollingOrderCall = call;
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (userCancel) {
                        userCancel = false;
                        unifiedOrderListener.onProcess("用户取消...");
                        unifiedOrderListener.onQueryResult(false, UnifiedOrderResponseCode.USERCANCEL.getCode(), unifiedOrderResultRespone);
                        //Log.i(TAG, "payCall--->onResponse--->用户取消");
                        return;
                    }
                    unifiedOrderListener.onProcess("预下单支付结果查询失败...");
                    unifiedOrderListener.onQueryResult(false, UnifiedOrderResponseCode.TRADE_FAIL.getCode(), unifiedOrderResultRespone);
                    setMightHas_B_C_PayResult(true);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    if (response.code() == 200) {
                        if (userCancel) {
                            userCancel = false;
                            unifiedOrderListener.onProcess("用户取消...");
                            unifiedOrderListener.onQueryResult(false, UnifiedOrderResponseCode.USERCANCEL.getCode(), unifiedOrderResultRespone);
                            unifiedOrderResultRespone = null;
                            return;
                        }
                        unifiedOrderResultRespone = new Gson().fromJson(str, UnifiedOrderResultRespone.class);
//                        Log.i(TAG, "unifiedOrderResultRespone:" + unifiedOrderResultRespone.toString());
                        if (Integer.valueOf(unifiedOrderResultRespone.getResult_code())
                                .equals(UnifiedOrderResponseCode.TRADE_SUCCESS.getKey())) { //支付成功---6001
                            unifiedOrderListener.onQueryResult(true, UnifiedOrderResponseCode.TRADE_SUCCESS.getCode(), unifiedOrderResultRespone);
                            setMightHas_B_C_PayResult(true);
                            unifiedOrderResultRespone = null;
                            return;
                        } else if(Integer.valueOf(unifiedOrderResultRespone.getResult_code())
                                .equals(UnifiedOrderResponseCode.TRADE_HANDLE.getKey())) {//支付处理中---6002
//                            unifiedOrderListener.onQueryResult(false, unifiedOrderResultRespone.getResult_code(), unifiedOrderResultRespone);
//                            setMightHas_B_C_PayResult(true);
//                            unifiedOrderResultRespone = null;
//                            return;
                        } else if(Integer.valueOf(unifiedOrderResultRespone.getResult_code())
                                .equals(UnifiedOrderResponseCode.TRADE_FAIL.getKey())) {//支付失败---6003
                            unifiedOrderListener.onQueryResult(false, unifiedOrderResultRespone.getResult_code(), unifiedOrderResultRespone);
                            setMightHas_B_C_PayResult(true);
                            unifiedOrderResultRespone = null;
                            return;
                        } else if(Integer.valueOf(unifiedOrderResultRespone.getResult_code())
                                .equals(UnifiedOrderResponseCode.TRADE_EXCEPTION.getKey())) {//支付异常,原商品订单异常---6004
                            unifiedOrderListener.onQueryResult(false, unifiedOrderResultRespone.getResult_code(), unifiedOrderResultRespone);
                            setMightHas_B_C_PayResult(true);
                            unifiedOrderResultRespone = null;
                            return;
                        } else if(Integer.valueOf(unifiedOrderResultRespone.getResult_code())
                                .equals(UnifiedOrderResponseCode.QUERY_EXCEPTION.getKey())) {//查询异常---6005
                            unifiedOrderListener.onQueryResult(false, unifiedOrderResultRespone.getResult_code(), unifiedOrderResultRespone);
                            setMightHas_B_C_PayResult(true);
                            unifiedOrderResultRespone = null;
                            return;
                        } else { //支付失败 获取支付结果错误, 请重试
                            unifiedOrderListener.onQueryResult(false, unifiedOrderResultRespone.getResult_code(), unifiedOrderResultRespone);
                            setMightHas_B_C_PayResult(true);
                            unifiedOrderResultRespone = null;
                            return;
                        }
//                          else if (payResultRespone.getResult_msg().equals(PayResultResponeCode.NOORDER.getCode())) {//查询不到原始订单
//                            payResultListener.onQueryResult(PayResultResponeCode.NOORDER.getCode(), payResultRespone);
//                        } else if (payResultRespone.getResult_msg().equals(PayResultResponeCode.NOPAYRESULT.getCode())) {//没有支付结果
//                            payResultListener.onQueryResult(PayResultResponeCode.NOPAYRESULT.getCode(), payResultRespone);
//                        } else if (payResultRespone.getResult_msg().equals(PayResultResponeCode.DATACHECKEXP.getCode())) {//数据校验失败
//                            payResultListener.onQueryResult(PayResultResponeCode.DATACHECKEXP.getCode(), payResultRespone);
//                        }
                    } else {
                        unifiedOrderListener.onQueryResult(false, UnifiedOrderResponseCode.TRADE_FAIL.getCode(), unifiedOrderResultRespone);
                        setMightHas_B_C_PayResult(true);
                        unifiedOrderResultRespone = null;
                        return;
                    }
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PreOrderRespone preOrderRespone = null;
    private PayResultRespone payResultRespone  = null;

    public synchronized void pay(final PayResultRequest payResultRequest, final int timeout_secs) {
        startTime = System.currentTimeMillis();
        remainTime = timeout_secs * 1000;
        userCancel = false;
        String url = null;
        try {
            setMightHas_C_B_PayResult(false);
            if (currentPlatform == RequestUrl.TEST) {
                url = RequestUrl.TEST + "/" + RequestUrl.Order_Pre;
            } else {
                url = RequestUrl.PRODUCT + "/" + RequestUrl.Order_Pre;
            }
            if (mHandler == null) {
                mHandler = new Handler(mContext.getMainLooper());
            }
            if (payResultListener == null) {
                return;
            }
            if (preOrderRespone == null) {
                preOrderRespone = new PreOrderRespone();
            }
            if (payResultRespone == null) {
                payResultRespone = new PayResultRespone();
            }
            //===============================================
            queryOrderRequset = new QueryOrderRequset();
            queryOrderRequset.setMchnt_id(payResultRequest.getMchnt_id());
            queryOrderRequset.setTerm_no(payResultRequest.getTerm_no());
            queryOrderRequset.setTerm_order_no(payResultRequest.getTerm_order_no());
            queryOrderRequset.setTermfix_no(payResultRequest.getTermfix_no());

            StringBuilder signStr = new StringBuilder();
            signStr.append(queryOrderRequset.getTerm_order_no())
                    .append(queryOrderRequset.getTermfix_no());
            String sign = SignUtils.encodeSignString(signStr.toString());
            queryOrderRequset.setSign(sign);
            //===============================================
            payResultListener.onProcess("请求二维码...");
            payClient = OkHttpUtils.getInstance().getOkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add(RequestKey.termfix_no.getValue(), payResultRequest.getTermfix_no())
                    .add(RequestKey.mchnt_id.getValue(), payResultRequest.getMchnt_id())
                    .add(RequestKey.term_no.getValue(), payResultRequest.getTerm_no())
                    .add(RequestKey.term_order_no.getValue(), payResultRequest.getTerm_order_no())
                    .add(RequestKey.deal_fee.getValue(), payResultRequest.getDeal_fee())
                    .add(RequestKey.req_flag.getValue(), payResultRequest.getReq_flag())
                    .add(RequestKey.vege_count.getValue(), payResultRequest.getVege_count())
                    .add(RequestKey.vege_info.getValue(), payResultRequest.getVege_info().toString())
                    .add(RequestKey.sign.getValue(), payResultRequest.getSign())
                    .build();
            final Call call = payClient.newCall(getRequest(url, requestBody));
            payCall = call;
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (userCancel) {
                        preOrderRespone = new PreOrderRespone();
                        userCancel = false;
                        //如果是主动取消的情况下
                        payResultListener.onProcess("用户取消...");
                        payResultListener.onQrcodeResult(false, PreOrderResponeCode.USERCANCEL.getCode(), preOrderRespone);
//                        Log.i(TAG, "payCall--->onFailure--->用户取消");
                        return;
                    }
//                    Log.i(TAG, "payCall--->onFailure--->请求二维码失败");
                    payResultListener.onProcess("请求二维码失败...");
                    payResultListener.onQrcodeResult(false, PreOrderResponeCode.NOQRCODE.getCode(), preOrderRespone);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    if (response.code() == 200) {
                        preOrderRespone = new Gson().fromJson(str, PreOrderRespone.class);
                        if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.SUCCESS.getKey())) { //业务处理成功，返回二维码
                            payResultListener.onQrcode(preOrderRespone.getQrcode_url());
                            while (true) {
                                if (userCancel) {
                                    userCancel = false;
                                    payResultListener.onProcess("用户取消...");
                                    payResultListener.onQrcodeResult(false, PreOrderResponeCode.USERCANCEL.getCode(), preOrderRespone);
                                    preOrderRespone = null;
//                                    Log.i(TAG, "payCall--->onResponse--->用户取消");
                                    return;
                                }
                                if (isMightHas_C_B_PayResult()) {
                                    Log.e(TAG, "isMightHas_C_B_PayResult == true");
                                    setMightHas_C_B_PayResult(false);
                                    return;
//                                    if (payResultRespone.getResult_msg() != null && payResultRespone.getResult_msg().toString().length() > 0) {
//                                        Log.e(TAG, "onResponse--->payResultRespone.getResult_msg() != null");
//                                        if (!payResultRespone.getResult_msg().equals(PayResultResponeCode.NOPAYRESULT.getCode())) {
//                                            Log.e(TAG, "payCall--->onResponse--->payResultRespone.getResult_msg() != null");
//                                            return;
//                                        }
//                                    }
                                } else {
                                    Log.e(TAG, "isMightHas_C_B_PayResult == false");
                                    if (!checkTimeOut()) {
                                        pollingQueryOrder(queryOrderRequset);
                                    } else {
                                        payResultListener.onProcess("交易超时...");
                                        payResultListener.onQrcodeResult(false, PreOrderResponeCode.TIMEOUT.getCode(), preOrderRespone);
                                        preOrderRespone = null;
//                                    Log.i(TAG, "payCall--->onResponse--->交易超时");
                                        return;
                                    }
                                }
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
//                                     mHandler.post(pollingOrderRunnable);
                        } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.NOQRCODE.getKey())) { //处理失败，未能成功获取二维码
                            payResultListener.onQrcodeResult(false, PreOrderResponeCode.NOQRCODE.getCode(), preOrderRespone);
                            preOrderRespone = null;
                            return;
                        } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.DATACHECKEXP.getKey())) { //数据校验失败
                            payResultListener.onQrcodeResult(false, PreOrderResponeCode.DATACHECKEXP.getCode(), preOrderRespone);
                            preOrderRespone = null;
                            return;
                        } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.DATADEALEXP.getKey())) { //数据处理异常
                            payResultListener.onQrcodeResult(false, PreOrderResponeCode.DATADEALEXP.getCode(), preOrderRespone);
                            preOrderRespone = null;
                            return;
                        } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.DATAEXCEPTION.getKey())) { //预交易下单失败，具体原因见提示信息
                            payResultListener.onQrcodeResult(false, PreOrderResponeCode.DATAEXCEPTION.getCode(), preOrderRespone);
                            preOrderRespone = null;
                            return;
                        }
                    } else {
                        payResultListener.onProcess("网络请求成功，返回二维码失败...");
                        payResultListener.onQrcodeResult(false, PreOrderResponeCode.NOQRCODE.getCode(), preOrderRespone);
                        preOrderRespone = null;
                        return;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void pollingQueryOrder(final QueryOrderRequset payResultRequset){
        String url = null;
        try {
            if (currentPlatform == RequestUrl.TEST) { //测试环境
                url = RequestUrl.TEST + "/" + RequestUrl.Pay_Result;
            } else {
                url = RequestUrl.PRODUCT + "/" + RequestUrl.Pay_Result;
            }
            if (payResultListener == null) {
                return;
            }
            if (payResultRespone == null) {
                payResultRespone = new PayResultRespone();
            }
            payResultListener.onProcess("支付结果查询...");
            pollingOrderClient = OkHttpUtils.getInstance().getOkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add(RequestKey.termfix_no.getValue(), payResultRequset.getTermfix_no())
                    .add(RequestKey.mchnt_id.getValue(), payResultRequset.getMchnt_id())
                    .add(RequestKey.term_no.getValue(), payResultRequset.getTerm_no())
                    .add(RequestKey.term_order_no.getValue(), payResultRequset.getTerm_order_no())
                    .add(RequestKey.sign.getValue(), payResultRequset.getSign())
                    .build();
            final Call call = pollingOrderClient.newCall(getRequest(url, requestBody));
            pollingOrderCall = call;
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (userCancel) {
                        preOrderRespone = new PreOrderRespone();
                        userCancel = false;
                        //如果是主动取消的情况下
                        payResultListener.onProcess("用户取消...");
                        payResultListener.onQrcodeResult(false, PayResultResponeCode.USERCANCEL.getCode(), preOrderRespone);
                        payResultRespone = null;
                        return;
                    }
                    payResultListener.onProcess("支付结果查询失败...");
                    payResultListener.onResult(false, PayResultResponeCode.FAIL.getCode(), payResultRespone);
                    setMightHas_C_B_PayResult(true);
                    payResultRespone = null;
                    return;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    if (response.code() == 200) {
                        if (userCancel) {
                            userCancel = false;
                            payResultListener.onProcess("用户取消...");
                            payResultListener.onResult(false, PayResultResponeCode.USERCANCEL.getCode(), payResultRespone);
                            payResultRespone = null;
                            return;
                        }
                        payResultRespone = new Gson().fromJson(str, PayResultRespone.class);
                        if (payResultRespone.getResult_msg().equals(PayResultResponeCode.SUCCESS.getCode())) { //支付成功
                            payResultListener.onResult(true, PayResultResponeCode.SUCCESS.getCode(), payResultRespone);
                            setMightHas_C_B_PayResult(true);
                            payResultRespone = null;
                            return;
                        } else if (payResultRespone.getResult_msg().equals(PayResultResponeCode.FAIL.getCode())) { //支付失败 获取支付结果错误 ，请重试
                            payResultListener.onQueryResult(PayResultResponeCode.FAIL.getCode(), payResultRespone);
                            setMightHas_C_B_PayResult(true);
                            payResultRespone = null;
                            return;
                        } else if (payResultRespone.getResult_msg().equals(PayResultResponeCode.NOORDER.getCode())) {//查询不到原始订单
                            payResultListener.onQueryResult(PayResultResponeCode.NOORDER.getCode(), payResultRespone);
                            setMightHas_C_B_PayResult(true);
                            payResultRespone = null;
                            return;
                        } else if (payResultRespone.getResult_msg().equals(PayResultResponeCode.NOPAYRESULT.getCode())) {//没有支付结果
                            payResultListener.onQueryResult(PayResultResponeCode.NOPAYRESULT.getCode(), payResultRespone);
                            payResultRespone = null;
                            return;
                        } else if (payResultRespone.getResult_msg().equals(PayResultResponeCode.DATACHECKEXP.getCode())) {//数据校验失败
                            payResultListener.onQueryResult(PayResultResponeCode.DATACHECKEXP.getCode(), payResultRespone);
                            setMightHas_C_B_PayResult(true);
                            payResultRespone = null;
                            return;
                        }
                    } else {
                        payResultListener.onResult(false, PayResultResponeCode.FAIL.getCode(), payResultRespone);
                        setMightHas_C_B_PayResult(true);
                        payResultRespone = null;
                        return;
                    }
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void preOrder(final PreOrderRequest preOrderRequest, int timeout_secs){
        if (preOrderListener == null) {
            return;
        }
        preOrderListener.onProcess("请求二维码...");
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = client.newBuilder();

        builder.sslSocketFactory(getsslContext().getSocketFactory());
        builder.hostnameVerifier(hostnameVerifier);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(timeout_secs, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        client = builder.build();
        RequestBody requestBody = new FormBody.Builder()
                .add(RequestKey.termfix_no.getValue(), preOrderRequest.getTermfix_no())
                .add(RequestKey.mchnt_id.getValue(), preOrderRequest.getMchnt_id())
                .add(RequestKey.term_no.getValue(), preOrderRequest.getTerm_no())
                .add(RequestKey.term_order_no.getValue(), preOrderRequest.getTerm_order_no())
                .add(RequestKey.deal_fee.getValue(), preOrderRequest.getDeal_fee())
                .add(RequestKey.req_flag.getValue(), preOrderRequest.getReq_flag())
                .add(RequestKey.vege_count.getValue(), preOrderRequest.getVege_count())
                .add(RequestKey.vege_info.getValue(), preOrderRequest.getVege_info().toString())
                .add(RequestKey.sign.getValue(), preOrderRequest.getSign())
                .build();
        Request request = new Request.Builder()
                .url(RequestUrl.Order_Pre)
                .addHeader("ContenType", "application/x-java-serialized-object")
                .addHeader("ContenType", "application/json;charset=utf-8")
                .addHeader("ContenType", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //如果超时并未超过指定次数，则重新连接
//                if (e.getCause().equals(SocketTimeoutException.class) && preOrderRequestTimes < maxRequestTimes) {
//                    preOrderListener.onProcess("重新发起请求" + preOrderRequestTimes +"...");
//                    preOrderRequestTimes++;
//                    call.enqueue(this);
//                } else {
//                    PreOrderRespone preOrderRespone = new PreOrderRespone();
//                    preOrderListener.onResult(false, "请求失败...", preOrderRespone);
//                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str = response.body().string();
                if (response.code() == 200) {
                    PreOrderRespone preOrderRespone = new Gson().fromJson(str, PreOrderRespone.class);
                    preOrderListener.onResult(true, preOrderRespone.getResult_msg(), preOrderRespone);
                } else {
                    PreOrderRespone preOrderRespone = new Gson().fromJson(str, PreOrderRespone.class);
                    preOrderListener.onResult(false, preOrderRespone.getResult_msg(), preOrderRespone);
                }
            }

        });
    }

    public synchronized void queryOrder(final QueryOrderRequset payResultRequset, int timeout_secs){
        if (queryOrderListener == null) {
            return;
        }
        queryOrderListener.onProcess("支付结果查询...");
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = client.newBuilder();

        builder.sslSocketFactory(getsslContext().getSocketFactory());
        builder.hostnameVerifier(hostnameVerifier);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(timeout_secs*1000, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        client = builder.build();
        RequestBody requestBody = new FormBody.Builder()
                .add(RequestKey.termfix_no.getValue(), payResultRequset.getTermfix_no())
                .add(RequestKey.mchnt_id.getValue(), payResultRequset.getMchnt_id())
                .add(RequestKey.term_no.getValue(), payResultRequset.getTerm_no())
                .add(RequestKey.term_order_no.getValue(), payResultRequset.getTerm_order_no())
                .add(RequestKey.sign.getValue(), payResultRequset.getSign())
                .build();
        Request request = new Request.Builder()
                .url(RequestUrl.Pay_Result)
                .addHeader("ContenType", "application/x-java-serialized-object")
                .addHeader("ContenType", "application/json;charset=utf-8")
                .addHeader("ContenType", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure:");
                if(e.toString().contains("closed")) {
                    //如果是主动取消的情况下
                    QueryOrderRespone queryOrderRespone = new QueryOrderRespone();
                    queryOrderListener.onResult(false, "用户取消...", queryOrderRespone);
                }
                //如果超时并未超过指定次数，则重新连接
//                if (e.getCause().equals(SocketTimeoutException.class) && queryOrderRequestTimes < maxRequestTimes) {
//                    queryOrderListener.onProcess("重新发起请求" + queryOrderRequestTimes +"...");
//                    queryOrderRequestTimes++;
//                    call.enqueue(this);
//                } else {
//                    QueryOrderRespone queryOrderRespone = new QueryOrderRespone();
//                    queryOrderListener.onResult(false, "请求失败...", queryOrderRespone);
//                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                QueryOrderRespone payResultRespone = new Gson().fromJson(str, QueryOrderRespone.class);
                queryOrderListener.onResult(true, payResultRespone.getResult_msg(), payResultRespone);
            }
        });
    }


    private Request getRequest(String url, RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("ContenType", "application/x-java-serialized-object")
                .addHeader("ContenType", "application/json;charset=utf-8")
                .addHeader("ContenType", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();
        return  request;
    }

    Runnable pollingOrderRunnable = new Runnable() {
        @Override
        public void run() {
//            Log.i(TAG, "userCancel111111:" + userCancel);
            if (userCancel) {
//                Log.i(TAG, "userCancel2:" + userCancel);
                userCancel = false;
                payResultListener.onProcess("用户取消...");
                payResultListener.onResult(false, PayResultResponeCode.USERCANCEL.getCode(), payResultRespone);
                return;
            }
            if (tmo <= 0) {
//                Log.i(TAG, "tmo:" + tmo);
                payResultListener.onProcess("交易超时...");
                payResultListener.onResult(false, PayResultResponeCode.TIMEOUT.getCode(), payResultRespone);
                return;
            }
            if (payResultRespone.getResult_msg() != null && payResultRespone.getResult_msg().toString().length() > 0) {
                if (!payResultRespone.getResult_msg().equals(PayResultResponeCode.NOPAYRESULT.getCode())) {
                    return;
                }
            }
            pollingQueryOrder(queryOrderRequset);
            mHandler.postDelayed(pollingOrderRunnable,3000);
        }
    };

    public synchronized void cancel() {
        userCancel = true;
        tmo = 0;
//        Log.i(TAG, "-------------userCancel-----" + userCancel + " ,tmo:" + tmo);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (payCall != null) {
//                    Log.i(TAG, "-------------payCall----");
                    payCall.cancel();
                }
                if (pollingOrderCall != null) {
//                    Log.i(TAG, "-------------pollingOrderCall----");
                    pollingOrderCall.cancel();
                }
                if (unifiedOrderCall != null) {
//                    Log.i(TAG, "-------------pollingOrderCall----");
                    unifiedOrderCall.cancel();
                }
                if (refundCall != null) {
//                    Log.i(TAG, "-------------pollingOrderCall----");
                    refundCall.cancel();
                }

            }
        }).start();
    }

    private int calLastedTime(Date startDate) {
        long a = new Date().getTime();
        long b = startDate.getTime();
        int c = (int) ((a - b) / 1000);
        return c;
    }

    private boolean checkTimeOut()
    {
        long usedTime = (System.currentTimeMillis()-startTime);
//        Log.d(TAG, "remain: " + (remainTime - usedTime));
        return (remainTime-usedTime)<0;
    }

    public String getCurrentPlatform() {
        return currentPlatform;
    }

    public void setCurrentPlatform(String currentPlatform) {
        this.currentPlatform = currentPlatform;
    }

    public boolean isMightHas_C_B_PayResult() {
        return mightHas_C_B_PayResult;
    }

    public void setMightHas_C_B_PayResult(boolean mightHas_C_B_PayResult) {
        this.mightHas_C_B_PayResult = mightHas_C_B_PayResult;
    }

    public boolean isMightHas_B_C_PayResult() {
        return mightHas_B_C_PayResult;
    }

    public void setMightHas_B_C_PayResult(boolean mightHas_B_C_PayResult) {
        this.mightHas_B_C_PayResult = mightHas_B_C_PayResult;
    }

    public boolean isMightHasRefundResult() {
        return mightHasRefundResult;
    }

    public void setMightHasRefundResult(boolean mightHasRefundResult) {
        this.mightHasRefundResult = mightHasRefundResult;
    }
}
