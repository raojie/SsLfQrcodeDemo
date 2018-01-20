//package com.sslf.qrcode;
//
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.sslf.qrcode.common.PayResultResponeCode;
//import com.sslf.qrcode.common.PreOrderResponeCode;
//import com.sslf.qrcode.common.RequestKey;
//import com.sslf.qrcode.common.RequestUrl;
//import com.sslf.qrcode.listener.PayResultListener;
//import com.sslf.qrcode.listener.PreOrderListener;
//import com.sslf.qrcode.listener.QueryOrderListener;
//import com.sslf.qrcode.request.PayResultRequest;
//import com.sslf.qrcode.request.PreOrderRequest;
//import com.sslf.qrcode.request.QueryOrderRequset;
//import com.sslf.qrcode.respone.PayResultRespone;
//import com.sslf.qrcode.respone.PreOrderRespone;
//import com.sslf.qrcode.respone.QueryOrderRespone;
//import com.sslf.qrcode.utils.OkHttpUtils;
//import com.sslf.qrcode.utils.SignUtils;
//
//import java.io.IOException;
//import java.net.SocketTimeoutException;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
///**
// * Method: ShenSiQrcodeApi
// * Decription:
// * Author: raoj
// * Date: 2017/9/29
// **/
//public class ShenSiQrcodeApi_Backup {
//
//    private static final String TAG = "ShenSiQrcodeApi";
//    private PreOrderListener preOrderListener;
//    private QueryOrderListener queryOrderListener;
//    private PayResultListener payResultListener;
//    private boolean isRelease = false;
//    private boolean userCancel = false;
//    private static final int maxRequestTimes = 3;
//    private int preOrderRequestTimes;
//    private int queryOrderRequestTimes;
//    private OkHttpClient payClient = null;
//    private OkHttpClient pollingOrderClient = null;
//    private Call payCall = null;
//    private Call pollingOrderCall = null;
//    private int tmo;
//    /**
//     * 请求集合: key=Activity value=Call集合
//     */
//    private static Map<Class<?>,List<Call>> callsMap = new ConcurrentHashMap<Class<?>,List<Call>>();
//
////    private int tmo;
//
//    public ShenSiQrcodeApi_Backup() {}
//
////    public void initialize(PreOrderListener preOrderListener, QueryOrderListener queryOrderListener){
////        if (preOrderListener == null) {
////            this.preOrderListener = preOrderListener;
////        }
////        if (queryOrderListener == null) {
////            this.queryOrderListener = queryOrderListener;
////        }
////    }
//
//    public void setPreOrderListener(PreOrderListener listener) {
//        if (preOrderListener == null) {
//            this.preOrderListener = listener;
//        }
//    }
//
//    public void setQueryOrderListener(QueryOrderListener listener) {
//        if (queryOrderListener == null) {
//            this.queryOrderListener = listener;
//        }
//    }
//
//    public void setPayResultListener(PayResultListener listener) {
//        if (payResultListener == null) {
//            this.payResultListener = listener;
//        }
//    }
//
//    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//        @Override
//        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//        }
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//        }
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            return new X509Certificate[0];
//        }
//    }};
//
//    private HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//        @Override
//        public boolean verify(String hostname, SSLSession session) {
//            return true;
//        }
//    };
//
//    private SSLContext getsslContext(){
//        SSLContext sslContext = null;
//        try {
//            //构造自己的SSLContext
//            sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        return sslContext;
//    }
//
//    private PreOrderRespone preOrderRespone = new PreOrderRespone();
//    private PayResultRespone payResultRespone = new PayResultRespone();
//    public synchronized void pay(final PayResultRequest payResultRequest, final int timeout_secs){
//        try {
//            if (payResultListener == null) {
//                return;
//            }
//            final Date date = new Date();
//            //===============================================
//            final QueryOrderRequset queryOrderRequset = new QueryOrderRequset();
//            queryOrderRequset.setMchnt_id(payResultRequest.getMchnt_id());
//            queryOrderRequset.setTerm_no(payResultRequest.getTerm_no());
//            queryOrderRequset.setTerm_order_no(payResultRequest.getTerm_order_no());
//            queryOrderRequset.setTermfix_no(payResultRequest.getTermfix_no());
//
//            StringBuilder signStr = new StringBuilder();
//            signStr.append(queryOrderRequset.getTerm_order_no())
//                    .append(queryOrderRequset.getTermfix_no());
//            String sign = null;
//            try {
//                sign = SignUtils.encodeSignString(signStr.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            queryOrderRequset.setSign(sign);
//            //===============================================
//            preOrderRequestTimes = 0;
//            payResultListener.onProcess("请求二维码...");
//            payClient = OkHttpUtils.getInstance().getOkHttpClient();
//            Log.i(TAG, "payClient.hashCode:" + payClient.hashCode());
//            RequestBody requestBody = new FormBody.Builder()
//                    .add(RequestKey.termfix_no.getValue(), payResultRequest.getTermfix_no())
//                    .add(RequestKey.mchnt_id.getValue(), payResultRequest.getMchnt_id())
//                    .add(RequestKey.term_no.getValue(), payResultRequest.getTerm_no())
//                    .add(RequestKey.term_order_no.getValue(), payResultRequest.getTerm_order_no())
//                    .add(RequestKey.deal_fee.getValue(), payResultRequest.getDeal_fee())
//                    .add(RequestKey.req_flag.getValue(), payResultRequest.getReq_flag())
//                    .add(RequestKey.vege_count.getValue(), payResultRequest.getVege_count())
//                    .add(RequestKey.vege_info.getValue(), payResultRequest.getVege_info().toString())
//                    .add(RequestKey.sign.getValue(), payResultRequest.getSign())
//                    .build();
////        if (payCall == null) {
////            payCall = payClient.newCall(request);
////        }
////        payCall = OkHttpUtils.getCallInstance(request);
//            Call call = payClient.newCall(getRequest(RequestUrl.Order_Pre, requestBody));
//            payCall = call;
//            Log.i(TAG, "payCall.hashCode:" + payCall.hashCode());
////        if (userCancel) return;
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    //如果超时并未超过指定次数，则重新连接
//                    if (e.toString().contains("closed")) {
//                        //如果是主动取消的情况下
//                        payResultListener.onProcess("用户取消...");
//                        return;
//                    } else {
//                        if (preOrderRequestTimes < maxRequestTimes) {
//                            payResultListener.onProcess("请求二维码失败,重新发起请求" + preOrderRequestTimes + "...");
//                            preOrderRequestTimes++;
//                            onFailure(call, e);
//                        } else {
//                            payResultListener.onProcess("请求二维码失败...");
//                            preOrderRequestTimes = 5;
//                        }
//                    }
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
////                if (userCancel) {
////                    Log.i(TAG, "isUserCancel0000:" + userCancel);
////                    userCancel = false;
////                    call.cancel();
////                    payResultListener.onProcess("用户取消");
////                    return;
////                }
//                    String str = response.body().string();
//                    if (response.code() == 200) {
//                        preOrderRespone = new Gson().fromJson(str, PreOrderRespone.class);
//                        if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.SUCCESS.getKey())) { //业务处理成功，返回二维码
//                            payResultListener.onQrcode(preOrderRespone.getQrcode_url());
//                            calLastedTime(date);
//                            tmo = timeout_secs - calLastedTime(date);
//                            tmo = tmo * 1000;
//                            while (true) {
//                                Log.i(TAG, "userCancel111111:" + userCancel);
//                                if (userCancel) {
//                                    Log.i(TAG, "userCancel2:" + userCancel);
//                                    userCancel = false;
//                                    payResultListener.onResult(false, "用户取消", preOrderRespone);
//                                    break;
//                                }
//                                if (tmo <= 0) {
//                                    break;
//                                }
//                                if (payResultRespone.getResult_msg() != null && payResultRespone.getResult_msg().toString().length() > 0) {
//                                    if (!payResultRespone.getResult_msg().equals(PayResultResponeCode.NOPAYRESULT.getCode())) {
//                                        break;
//                                    }
//                                }
//                                pollingQueryOrder(queryOrderRequset, tmo / 1000);
//                                try {
////                            Log.i(TAG, " tmo:" + tmo);
//                                    Thread.sleep(3000);
//                                    tmo -= 3000;
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.NOQRCODE.getKey())) { //处理失败，未能成功获取二维码
//                            payResultListener.onResult(false, PreOrderResponeCode.NOQRCODE.getCode(), preOrderRespone);
//                        } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.DATACHECKEXP.getKey())) { //数据校验失败
//                            payResultListener.onResult(false, PreOrderResponeCode.DATACHECKEXP.getCode(), preOrderRespone);
//                        } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.DATADEALEXP.getKey())) { //数据处理异常
//                            payResultListener.onResult(false, PreOrderResponeCode.DATADEALEXP.getCode(), preOrderRespone);
//                        } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.DATAEXCEPTION.getKey())) { //预交易下单失败，具体原因见提示信息
//                            payResultListener.onResult(false, PreOrderResponeCode.DATAEXCEPTION.getCode(), preOrderRespone);
//                        }
//
//                    } else {
////                    PayResultRespone payResultRespone = new Gson().fromJson(str, PayResultRespone.class);
////                    payResultListener.onResult(false, payResultRespone.getResult_msg(), payResultRespone);
//                    }
//                }
//
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private synchronized void pollingQueryOrder(final QueryOrderRequset payResultRequset,int timeout_secs){
//        try {
//            if (userCancel) {
//                Log.i(TAG, "userCancel3:" + userCancel);
//                userCancel = false;
//                return;
//            }
//            queryOrderRequestTimes = 0;
//            payResultListener.onProcess("支付结果查询...");
//            pollingOrderClient = OkHttpUtils.getInstance().getOkHttpClient();
//            Log.i(TAG, "pollingOrderClient.hashCode:" + pollingOrderClient.hashCode());
//            RequestBody requestBody = new FormBody.Builder()
//                    .add(RequestKey.termfix_no.getValue(), payResultRequset.getTermfix_no())
//                    .add(RequestKey.mchnt_id.getValue(), payResultRequset.getMchnt_id())
//                    .add(RequestKey.term_no.getValue(), payResultRequset.getTerm_no())
//                    .add(RequestKey.term_order_no.getValue(), payResultRequset.getTerm_order_no())
//                    .add(RequestKey.sign.getValue(), payResultRequset.getSign())
//                    .build();
////            if (pollingOrderCall == null) {
////                pollingOrderCall = pollingOrderClient.newCall(request);
////            }
//            Call call = pollingOrderClient.newCall(getRequest(RequestUrl.Pay_Result, requestBody));
//            putCall(call);
//            pollingOrderCall = call;
////            pollingOrderCall = OkHttpUtils.getCallInstance(request);
//            Log.i(TAG, "pollingOrderCall.hashCode:" + pollingOrderCall.hashCode());
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Log.i(TAG, "pollingQueryOrder--->onFailure:");
////                    if (userCancel) return;
//                    if(e.toString().contains("closed")) {
//                        //如果是主动取消的情况下
//                        payResultListener.onProcess("用户取消...");
//                        return;
//                    } else {
//                        payResultListener.onProcess("支付结果查询失败...");
//                        //如果超时并未超过指定次数，则重新连接
//                        if (queryOrderRequestTimes < maxRequestTimes) {
//                            payResultListener.onProcess("支付结果查询失败,重新发起请求" + payResultListener + "...");
//                            queryOrderRequestTimes++;
//                            onFailure(call, e);
//                        } else {
//                            payResultListener.onProcess("支付结果查询失败...");
//                            queryOrderRequestTimes = 5;
//                        }
//                    }
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
////                    if (userCancel) {
////                        userCancel = false;
////                        payResultListener.onResult(false, "用户取消", payResultRespone);
////                        return;
////                    }
//                    String str = response.body().string();
//                    if (response.code() == 200) {
////                        pollingOrderCall = call;
//                        payResultRespone = new Gson().fromJson(str, PayResultRespone.class);
//                        payResultListener.onResult(true, payResultRespone.getResult_msg(), payResultRespone);
//                    }
//                }
//            });
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
////            pollingOrderCall.cancel();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public synchronized void preOrder(final PreOrderRequest preOrderRequest, int timeout_secs){
//        if (preOrderListener == null) {
//            return;
//        }
//        preOrderRequestTimes = 0;
//        preOrderListener.onProcess("请求二维码...");
//        OkHttpClient client = new OkHttpClient();
//        OkHttpClient.Builder builder = client.newBuilder();
//
//        builder.sslSocketFactory(getsslContext().getSocketFactory());
//        builder.hostnameVerifier(hostnameVerifier);
//        builder.connectTimeout(10, TimeUnit.SECONDS);
//        builder.readTimeout(timeout_secs, TimeUnit.SECONDS);
//        builder.writeTimeout(10, TimeUnit.SECONDS);
//        client = builder.build();
//        RequestBody requestBody = new FormBody.Builder()
//                .add(RequestKey.termfix_no.getValue(), preOrderRequest.getTermfix_no())
//                .add(RequestKey.mchnt_id.getValue(), preOrderRequest.getMchnt_id())
//                .add(RequestKey.term_no.getValue(), preOrderRequest.getTerm_no())
//                .add(RequestKey.term_order_no.getValue(), preOrderRequest.getTerm_order_no())
//                .add(RequestKey.deal_fee.getValue(), preOrderRequest.getDeal_fee())
//                .add(RequestKey.req_flag.getValue(), preOrderRequest.getReq_flag())
//                .add(RequestKey.vege_count.getValue(), preOrderRequest.getVege_count())
//                .add(RequestKey.vege_info.getValue(), preOrderRequest.getVege_info().toString())
//                .add(RequestKey.sign.getValue(), preOrderRequest.getSign())
//                .build();
//        Request request = new Request.Builder()
//                .url(RequestUrl.Order_Pre)
//                .addHeader("ContenType", "application/x-java-serialized-object")
//                .addHeader("ContenType", "application/json;charset=utf-8")
//                .addHeader("ContenType", "application/x-www-form-urlencoded")
//                .post(requestBody)
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //如果超时并未超过指定次数，则重新连接
//                if (e.getCause().equals(SocketTimeoutException.class) && preOrderRequestTimes < maxRequestTimes) {
//                    preOrderListener.onProcess("重新发起请求" + preOrderRequestTimes +"...");
//                    preOrderRequestTimes++;
//                    call.enqueue(this);
//                } else {
//                    PreOrderRespone preOrderRespone = new PreOrderRespone();
//                    preOrderListener.onResult(false, "请求失败...", preOrderRespone);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                String str = response.body().string();
//                if (response.code() == 200) {
//                    PreOrderRespone preOrderRespone = new Gson().fromJson(str, PreOrderRespone.class);
//                    preOrderListener.onResult(true, preOrderRespone.getResult_msg(), preOrderRespone);
//                } else {
//                    PreOrderRespone preOrderRespone = new Gson().fromJson(str, PreOrderRespone.class);
//                    preOrderListener.onResult(false, preOrderRespone.getResult_msg(), preOrderRespone);
//                }
//            }
//
//        });
//    }
//
//    public synchronized void queryOrder(final QueryOrderRequset payResultRequset, int timeout_secs){
//        if (queryOrderListener == null) {
//            return;
//        }
//        queryOrderRequestTimes = 0;
//        queryOrderListener.onProcess("支付结果查询...");
//        OkHttpClient client = new OkHttpClient();
//        OkHttpClient.Builder builder = client.newBuilder();
//
//        builder.sslSocketFactory(getsslContext().getSocketFactory());
//        builder.hostnameVerifier(hostnameVerifier);
//        builder.connectTimeout(10, TimeUnit.SECONDS);
//        builder.readTimeout(timeout_secs*1000, TimeUnit.SECONDS);
//        builder.writeTimeout(10, TimeUnit.SECONDS);
//        client = builder.build();
//        RequestBody requestBody = new FormBody.Builder()
//                .add(RequestKey.termfix_no.getValue(), payResultRequset.getTermfix_no())
//                .add(RequestKey.mchnt_id.getValue(), payResultRequset.getMchnt_id())
//                .add(RequestKey.term_no.getValue(), payResultRequset.getTerm_no())
//                .add(RequestKey.term_order_no.getValue(), payResultRequset.getTerm_order_no())
//                .add(RequestKey.sign.getValue(), payResultRequset.getSign())
//                .build();
//        Request request = new Request.Builder()
//                .url(RequestUrl.Pay_Result)
//                .addHeader("ContenType", "application/x-java-serialized-object")
//                .addHeader("ContenType", "application/json;charset=utf-8")
//                .addHeader("ContenType", "application/x-www-form-urlencoded")
//                .post(requestBody)
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i(TAG, "onFailure:");
//                if(e.toString().contains("closed")) {
//                    //如果是主动取消的情况下
//                    QueryOrderRespone queryOrderRespone = new QueryOrderRespone();
//                    queryOrderListener.onResult(false, "用户取消...", queryOrderRespone);
//                }
//                //如果超时并未超过指定次数，则重新连接
//                if (e.getCause().equals(SocketTimeoutException.class) && queryOrderRequestTimes < maxRequestTimes) {
//                    queryOrderListener.onProcess("重新发起请求" + queryOrderRequestTimes +"...");
//                    queryOrderRequestTimes++;
//                    call.enqueue(this);
//                } else {
//                    QueryOrderRespone queryOrderRespone = new QueryOrderRespone();
//                    queryOrderListener.onResult(false, "请求失败...", queryOrderRespone);
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String str = response.body().string();
//                QueryOrderRespone payResultRespone = new Gson().fromJson(str, QueryOrderRespone.class);
//                queryOrderListener.onResult(true, payResultRespone.getResult_msg(), payResultRespone);
//            }
//        });
//    }
//
//
//    private Request getRequest(String url, RequestBody requestBody) {
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("ContenType", "application/x-java-serialized-object")
//                .addHeader("ContenType", "application/json;charset=utf-8")
//                .addHeader("ContenType", "application/x-www-form-urlencoded")
//                .post(requestBody)
//                .build();
//        return  request;
//    }
//    /**
//     * 保存请求集合
//     * @param call
//     */
//    private void putCall(Call call) {
//        List<Call> callList = null;
//        if (null == callList) {
//            callList = new LinkedList<Call>();
//            callList.add(call);
//        }
//    }
//
//    /**
//     * 取消请求
//     * @param clazz
//     */
//    public static void cancelCall(Class<?> clazz){
//        List<Call> callList = callsMap.get(clazz);
//        if(null != callList){
//            for(Call call : callList){
//                if(!call.isCanceled()) {
//                    call.cancel();
//                    Log.i(TAG, "-------------isCanceled1:" + call);
//                }
//            }
//            callsMap.remove(clazz);
//        }
//    }
//
//    public void cancel() {
//        userCancel = true;
//        Log.i(TAG, "-------------userCancel:" + userCancel);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                tmo = 0;
////                payCall.cancel();
////                pollingOrderCall.cancel();
////                if (payClient != null) {
////                    payClient = null;
////                }
////                if (pollingOrderClient != null) {
////                    pollingOrderClient = null;
////                }
////                if (payCall != null) {
////                    payCall.cancel();
////                }
////                if (pollingOrderCall != null) {
////                    pollingOrderCall.cancel();
////                }
////                Dispatcher payDispatcher = payClient.dispatcher();
////                Dispatcher queryDispatcher = pollingOrderClient.dispatcher();
////                synchronized (payDispatcher){
////                    payCall.cancel();
////                }
////                synchronized (queryDispatcher){
////                    pollingOrderCall.cancel();
////                }
//            }
//        }).start();
//    }
//
//    public void release() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (payClient != null) {
//                    payClient = null;
//                }
//                if (payCall != null) {
//                    payCall.cancel();
//                }
//                if (pollingOrderClient != null) {
//                    pollingOrderClient = null;
//                }
//                if (pollingOrderCall != null) {
//                    pollingOrderCall.cancel();
//                }
//            }
//        }).start();
//    }
//
//    private int calLastedTime(Date startDate) {
//        long a = new Date().getTime();
//        long b = startDate.getTime();
//        int c = (int) ((a - b) / 1000);
//        return c;
//    }
//}
