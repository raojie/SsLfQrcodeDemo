package com.shensilandfone.demo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sslf.qrcode.ShenSiQrcodeApi;
import com.sslf.qrcode.common.PayResultResponeCode;
import com.sslf.qrcode.common.PayType;
import com.sslf.qrcode.common.PreOrderResponeCode;
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
import com.sslf.qrcode.request.UnifiedOrderRequest;
import com.sslf.qrcode.request.VegeInfoBean;
import com.sslf.qrcode.respone.PayResultRespone;
import com.sslf.qrcode.respone.QueryOrderRespone;
import com.sslf.qrcode.respone.PreOrderRespone;
import com.sslf.qrcode.respone.RefundRespone;
import com.sslf.qrcode.respone.UnifiedOrderRespone;
import com.sslf.qrcode.respone.UnifiedOrderResultRespone;
import com.sslf.qrcode.utils.OrderNumberUtils;
import com.sslf.qrcode.utils.RandomNumberUtils;
import com.sslf.qrcode.utils.SignUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //商户号
    private static final String MerchantNo = "301053148161998";
    //终端号
    private static final String TerminalNo = "62841967";
    //硬件编号
    private static final String HardwareNo = "2C3803661994";

    private ShenSiQrcodeApi shenSiQrcodeApi;

    private AsyncTask<String, String, String> countDownTask = null;//倒计时异步任务

    private TextView showContentTextView;
    private String[] showContent;
    private ImageView imgQrcode;
    private Button preOrderBtn;
    private Button queryOrderBtn;
    private Button payBtn;
    private Button preBtn;
    private Button refundBtn;
    private Button cancelBtn;
    private EditText amountEdt;
    private EditText merchantNoEdt;
    private EditText terminalNoEdt;
    private EditText hardwareNoEdt;
    private TextView countDownTextView;

    private String orderNumber;
    private String rufundMchnt_id = "";
    private String rufundTerm_no = "";
    private String rufundTerm_order_no = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindEvent();
        initialize();
    }

    private void initView() {
        showContent = new String[5];
        showContentTextView = (TextView) findViewById(R.id.showContentTextView);
        countDownTextView = (TextView) findViewById(R.id.countDownTextView);
        preOrderBtn = (Button) findViewById(R.id.preOrderBtn);
        queryOrderBtn = (Button) findViewById(R.id.queryOrderBtn);
        payBtn = (Button) findViewById(R.id.payBtn);
        preBtn = (Button) findViewById(R.id.preBtn);
        refundBtn = (Button) findViewById(R.id.refundBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        imgQrcode = (ImageView) findViewById(R.id.imgQrcode);
        amountEdt = (EditText) findViewById(R.id.amountEdt);
        amountEdt.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        merchantNoEdt = (EditText) findViewById(R.id.merchantNoEdt);
        terminalNoEdt = (EditText) findViewById(R.id.terminalNoEdt);
        hardwareNoEdt = (EditText) findViewById(R.id.hardwareNoEdt);
        countDownTextView.setVisibility(View.GONE);
    }


    private void bindEvent() {
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateQrcode(null);
                        orderNumber = RandomNumberUtils.getRandomStringByLength(2) + OrderNumberUtils.getOrderNo();
                        shenSiQrcodeApi.setPayResultListener(payResultListener);
                        shenSiQrcodeApi.setCurrentPlatform(RequestUrl.TEST);

                        PayResultRequest payResultRequest = new PayResultRequest();

                        payResultRequest.setTermfix_no(hardwareNoEdt.getText().toString());
                        payResultRequest.setMchnt_id(merchantNoEdt.getText().toString());
                        payResultRequest.setTerm_no(terminalNoEdt.getText().toString());
                        payResultRequest.setTerm_order_no(orderNumber);
                        String fee = amountEdt.getText().toString();
                        System.out.println("金额:" + fee);
                        payResultRequest.setDeal_fee(fee);
                        payResultRequest.setReq_flag("2");
                        payResultRequest.setVege_count("1");
                        VegeInfoBean vegeInfoBean = new VegeInfoBean();
                        vegeInfoBean.setVege_name("10001");
                        vegeInfoBean.setVege_tracode("xxxxxf");
                        vegeInfoBean.setVege_weight("1000");
                        vegeInfoBean.setVege_price("100");
                        vegeInfoBean.setVege_fee("100");
                        List<VegeInfoBean> vegeInfoList = new ArrayList<VegeInfoBean>();
                        vegeInfoList.add(vegeInfoBean);

                        String vegeInfoGson = new Gson().toJson(vegeInfoList);
                        Log.i(TAG, "vegeInfoGson:" + vegeInfoGson);
                        payResultRequest.setVege_info(vegeInfoGson);
                        StringBuilder signStr = new StringBuilder();
                        signStr.append(payResultRequest.getTermfix_no())
                                .append(payResultRequest.getTerm_order_no())
                                .append(payResultRequest.getDeal_fee())
                                .append(payResultRequest.getVege_count());
                        String sign = null;
                        try {
                            sign = SignUtils.encodeSignString(signStr.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        payResultRequest.setSign(sign);

                        rufundMchnt_id = payResultRequest.getMchnt_id();
                        rufundTerm_no = payResultRequest.getTerm_no();
                        rufundTerm_order_no = payResultRequest.getTerm_order_no();
                        shenSiQrcodeApi.pay(payResultRequest,60);
                    }
                }).start();

            }
        });

        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateQrcode(null);
                        orderNumber = RandomNumberUtils.getRandomStringByLength(2) + OrderNumberUtils.getOrderNo();
                        shenSiQrcodeApi.setUnifiedOrderListener(unifiedOrderListener);
                        shenSiQrcodeApi.setCurrentPlatform(RequestUrl.TEST);

                        UnifiedOrderRequest unifiedOrderRequest = new UnifiedOrderRequest();
                        unifiedOrderRequest.setMchnt_id(merchantNoEdt.getText().toString());
                        unifiedOrderRequest.setTerm_no(terminalNoEdt.getText().toString());
                        unifiedOrderRequest.setTerm_order_no(orderNumber);
                        String fee = amountEdt.getText().toString();
                        System.out.println("金额:" + fee);
                        unifiedOrderRequest.setDeal_fee(fee);
                        unifiedOrderRequest.setReq_flag("2");
                        unifiedOrderRequest.setPay_Code("288413889711083271");
                        unifiedOrderRequest.setGoods_name("橡胶");

                        StringBuilder signStr = new StringBuilder();
                        signStr.append(unifiedOrderRequest.getTerm_order_no())
                                .append(unifiedOrderRequest.getMchnt_id())
                                .append(unifiedOrderRequest.getTerm_no())
                                .append(unifiedOrderRequest.getDeal_fee())
                                .append(unifiedOrderRequest.getPay_Code());
                        String sign = null;
                        try {
                            sign = SignUtils.encodeSignString(signStr.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        unifiedOrderRequest.setSign(sign);
                        rufundMchnt_id = unifiedOrderRequest.getMchnt_id();
                        rufundTerm_no = unifiedOrderRequest.getTerm_no();
                        rufundTerm_order_no = unifiedOrderRequest.getTerm_order_no();
                        shenSiQrcodeApi.unifiedOrder(unifiedOrderRequest, 60);
                    }
                }).start();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shenSiQrcodeApi.cancel();
                cancelCountDownTask();
                updateQrcode(null);
            }
        });

        preOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateQrcode(null);
                        orderNumber = RandomNumberUtils.getRandomStringByLength(2) + OrderNumberUtils.getOrderNo();
                        shenSiQrcodeApi.setPreOrderListener(preOrderListener);
                        PreOrderRequest preOrderRequest = new PreOrderRequest();

                        preOrderRequest.setTermfix_no(hardwareNoEdt.getText().toString());
                        preOrderRequest.setMchnt_id(merchantNoEdt.getText().toString());
                        preOrderRequest.setTerm_no(terminalNoEdt.getText().toString());
                        preOrderRequest.setTerm_order_no(orderNumber);
                        String fee = amountEdt.getText().toString();
                        System.out.println("金额:" + fee);
                        preOrderRequest.setDeal_fee(fee);
                        preOrderRequest.setReq_flag("2");
                        preOrderRequest.setVege_count("1");
                        VegeInfoBean vegeInfoBean = new VegeInfoBean();
                        vegeInfoBean.setVege_name("10001");
                        vegeInfoBean.setVege_tracode("xxxxxf");
                        vegeInfoBean.setVege_weight("1000");
                        vegeInfoBean.setVege_price("100");
                        vegeInfoBean.setVege_fee("100");
                        List<VegeInfoBean> vegeInfoList = new ArrayList<VegeInfoBean>();
                        vegeInfoList.add(vegeInfoBean);

                        String vegeInfoGson = new Gson().toJson(vegeInfoList);
                        Log.i(TAG, "vegeInfoGson:" + vegeInfoGson);
                        preOrderRequest.setVege_info(vegeInfoGson);
                        StringBuilder signStr = new StringBuilder();
                        signStr.append(preOrderRequest.getTermfix_no())
                                .append(preOrderRequest.getTerm_order_no())
                                .append(preOrderRequest.getDeal_fee())
                                .append(preOrderRequest.getVege_count());
                        String sign = null;
                        try {
                            sign = SignUtils.encodeSignString(signStr.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        preOrderRequest.setSign(sign);
                        shenSiQrcodeApi.preOrder(preOrderRequest, 60);
                    }
                }).start();
            }
        });

        refundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateQrcode(null);
                        shenSiQrcodeApi.setRefundListener(refundListener);
                        RefundRequest refundRequest = new RefundRequest();

                        refundRequest.setMchnt_id(rufundMchnt_id);
                        refundRequest.setTerm_no(rufundTerm_no);
                        refundRequest.setTerm_order_no(rufundTerm_order_no);
                        StringBuilder signStr = new StringBuilder();
                        signStr.append(refundRequest.getTerm_order_no())
                                .append(refundRequest.getMchnt_id())
                                .append(refundRequest.getTerm_no());
                        String sign = null;
                        try {
                            sign = SignUtils.encodeSignString(signStr.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        refundRequest.setSign(sign);
                        shenSiQrcodeApi.refund(refundRequest, 60);
                    }
                }).start();
            }
        });

        queryOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        shenSiQrcodeApi.setQueryOrderListener(queryOrderListener);

                        QueryOrderRequset queryOrderRequset = new QueryOrderRequset();
                        queryOrderRequset.setTermfix_no(hardwareNoEdt.getText().toString());
                        queryOrderRequset.setMchnt_id(merchantNoEdt.getText().toString());
                        queryOrderRequset.setTerm_no(terminalNoEdt.getText().toString());
                        queryOrderRequset.setTerm_order_no(orderNumber);

                        StringBuilder signStr = new StringBuilder();
                        signStr.append(queryOrderRequset.getTerm_order_no())
                                .append(queryOrderRequset.getTermfix_no());
                        String sign = null;
                        try {
                            sign = SignUtils.encodeSignString(signStr.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        queryOrderRequset.setSign(sign);
                        shenSiQrcodeApi.queryOrder(queryOrderRequset, 60);
                    }
                }).start();
            }
        });
    }

    private void initialize() {
        iniContentShow();
        shenSiQrcodeApi = new ShenSiQrcodeApi(getApplicationContext());
    }

    private void iniContentShow() {
        String result = "";
        for (int i = 0; i < showContent.length; i++) {
            showContent[i] = "";
            result = result + (i + 1) + "." + showContent[i] + "\n";
            showContentTextView.setText(result);
        }

    }

    private void updateContentTV() {
        String result = "";
        for (int i = 0; i < showContent.length; i++) {
            result = result + showContent[i] + "\n";
        }
        updateUI(result);
    }

    private void updateUI(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showContentTextView.setText(string);
            }
        });
    }

    private void updateQrcode(final Bitmap qrCodeBitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imgQrcode.setImageBitmap(qrCodeBitmap);
            }
        });
    }

    private void updateCountDownTextView(final int visibility) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countDownTextView.setVisibility(visibility);
            }
        });
    }

    private void cancelCountDownTask() {
        if (countDownTask!=null) {
            countDownTask.cancel(true);
        }
    }

    private void countDownTask() {
        countDownTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                return ticks(params[0]);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                onEnd();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                countDownTextView.setText(values[0]);
            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
                onEnd();
            }

            private void onEnd() {
                updateQrcode(null);
                updateCountDownTextView(View.GONE);
            }

            private String ticks(String v) {
                this.publishProgress("");
                int cnt = 60 * 1000; //60秒
                while (cnt > 0) {
                    this.publishProgress("" + (cnt / 1000));
                    cnt -= 500;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                return "";
            }
        };
        countDownTask.execute("");
    }

    private PreOrderListener preOrderListener = new PreOrderListener() {
        @Override
        public void onProcess(String message) {
            Log.i(TAG, "onProcess:" + message);
            showContent[0] = message;
            updateContentTV();
        }

        @Override
        public void onResult(boolean result, String message, PreOrderRespone preOrderRespone) {
            if (result) {
                Log.i(TAG, "result:" + result + " ,message:" + message + " ,preOrderRespone:" + preOrderRespone.toString());
                showContent[0] = message;
                updateContentTV();
                //生成二维码
                Bitmap qrCodeBitmap;
                try {
                    qrCodeBitmap = EncodingHandler.createQRCode(preOrderRespone.getQrcode_url(), 260);
                    updateQrcode(qrCodeBitmap);
                } catch (Exception e) {//WriterException
                    e.printStackTrace();
                }
                updateCountDownTextView(View.VISIBLE);
                countDownTask();
            } else {
                Log.i(TAG, "result:" + result);
                showContent[0] = "" + result;
                updateContentTV();
            }
        }
    };

    private QueryOrderListener queryOrderListener = new QueryOrderListener() {
        @Override
        public void onProcess(String message) {
            Log.i(TAG, "onProcess:" + message);
            showContent[0] = message;
            updateContentTV();
        }

        @Override
        public void onResult(boolean result, String message, QueryOrderRespone payResultRespone) {
            if (result) {
                Log.i(TAG, "result:" + result + " ,message:" + message + " ,payResultRespone:" + payResultRespone.toString());
                String payType = "";
                if (payResultRespone.getPay_type().equals(PayType.WECHAT.getKey())) {
                    payType = PayType.WECHAT.getValue();
                } else if (payResultRespone.getPay_type().equals(PayType.ALI.getKey())) {
                    payType = PayType.ALI.getValue();
                } else if (payResultRespone.getPay_type().equals(PayType.SCAN.getKey())) {
                    payType = PayType.SCAN.getValue();
                } else if (payResultRespone.getPay_type().equals(PayType.QQWALLET.getKey())) {
                    payType = PayType.QQWALLET.getValue();
                }
                showContent[0] = "支付结果:" + message;
                showContent[1] = "支付方式:" + payType;
                updateContentTV();
                cancelCountDownTask();
            } else {
                Log.i(TAG, "result:" + result);
                showContent[0] = "" + message;
                updateContentTV();
            }
        }
    };

    private PayResultListener payResultListener = new PayResultListener() {
        @Override
        public void onProcess(String message) {
            Log.i(TAG, "message:" + message);
            showContent[0] = "" + message;
            updateContentTV();
        }

        @Override
        public void onQrcode(String qrCode) {
            Log.i(TAG, "qrCode:" + qrCode);
            //生成二维码
            Bitmap qrCodeBitmap;
            try {
                qrCodeBitmap = EncodingHandler.createQRCode(qrCode, 260);
                updateQrcode(qrCodeBitmap);
            } catch (Exception e) {//WriterException
                e.printStackTrace();
            }
            updateCountDownTextView(View.VISIBLE);
            countDownTask();
        }

        @Override
        public void onQrcodeResult(boolean result, String message, PreOrderRespone preOrderRespone) {
            if (result) {

            } else {
                if (message.equals(PreOrderResponeCode.USERCANCEL.getCode())) {
                    showContent[1] = "用户取消";
                } else if (message.equals(PreOrderResponeCode.NOQRCODE.getCode())){
                    showContent[1] = "" + PreOrderResponeCode.NOQRCODE.getCode();
                    showContent[2] = "" + PreOrderResponeCode.NOQRCODE.getValue();
                    updateContentTV();
                } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.NOQRCODE.getKey())) { //处理失败，未能成功获取二维码
                    showContent[1] = "" + PreOrderResponeCode.NOQRCODE.getCode();
                    showContent[2] = "" + PreOrderResponeCode.NOQRCODE.getValue();
                    updateContentTV();
                } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.DATACHECKEXP.getKey())) { //数据校验失败
                    showContent[1] = "" + PreOrderResponeCode.DATACHECKEXP.getCode();
                    showContent[2] = "" + PreOrderResponeCode.DATACHECKEXP.getValue();
                    updateContentTV();
                } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.DATADEALEXP.getKey())) { //数据处理异常
                    showContent[1] = "" + PreOrderResponeCode.DATADEALEXP.getCode();
                    showContent[2] = "" + PreOrderResponeCode.DATADEALEXP.getValue();
                    updateContentTV();
                } else if (Integer.valueOf(preOrderRespone.getResult_code()).equals(PreOrderResponeCode.DATAEXCEPTION.getKey())) { //预交易下单失败，具体原因见提示信息
                    showContent[1] = "" + PreOrderResponeCode.DATAEXCEPTION.getCode();
                    showContent[2] = "" + PreOrderResponeCode.DATAEXCEPTION.getValue();
                    updateContentTV();
                }
            }
        }

        @Override
        public void onQueryResult(String message, PayResultRespone payResultRespone) {
            Log.i(TAG, "message:" + message + " ,payResultRespone:" + payResultRespone.toString());
            if (Integer.valueOf(payResultRespone.getResult_code()).equals(PayResultResponeCode.FAIL.getKey())) { //支付失败 获取支付结果错误 ，请重试
                showContent[1] = "支付结果:" + message;
                showContent[2] = "平台返回:" + PayResultResponeCode.FAIL.getValue();
                updateContentTV();
            } else if (Integer.valueOf(payResultRespone.getResult_code()).equals(PayResultResponeCode.NOORDER.getKey())) {//查询不到原始订单
                showContent[1] = "支付结果:" + message;
                showContent[2] = "平台返回:" + PayResultResponeCode.NOORDER.getValue();
                updateContentTV();
            } else if (Integer.valueOf(payResultRespone.getResult_code()).equals(PayResultResponeCode.NOPAYRESULT.getKey())) {//没有支付结果
                showContent[1] = "支付结果:" + message;
                showContent[2] = "平台返回:" + PayResultResponeCode.NOPAYRESULT.getValue();
                updateContentTV();
            } else if (Integer.valueOf(payResultRespone.getResult_code()).equals(PayResultResponeCode.DATACHECKEXP.getKey())) {//数据校验失败
                showContent[1] = "支付结果:" + message;
                showContent[2] = "平台返回:" + PayResultResponeCode.NOPAYRESULT.getValue();
                updateContentTV();
            }
        }

        @Override
        public void onResult(boolean result, String message, PayResultRespone payResultRespone) {
            if (result) {
                Log.i(TAG, "result:" + result + " ,message:" + message + " ,payResultRespone:" + payResultRespone.toString());
                String payType = "";
                if (message.equals(PayResultResponeCode.SUCCESS.getCode())) { //支付成功
                    if (Integer.valueOf(payResultRespone.getPay_type()).equals(PayType.WECHAT.getKey())) {
                        payType = PayType.WECHAT.getValue();
                    } else if (Integer.valueOf(payResultRespone.getPay_type()).equals(PayType.ALI.getKey())) {
                        payType = PayType.ALI.getValue();
                    } else if (Integer.valueOf(payResultRespone.getPay_type()).equals(PayType.SCAN.getKey())) {
                        payType = PayType.SCAN.getValue();
                    } else if (Integer.valueOf(payResultRespone.getPay_type()).equals(PayType.QQWALLET.getKey())) {
                        payType = PayType.QQWALLET.getValue();
                    }
                    showContent[1] = "支付结果:" + message;
                    showContent[2] = "支付方式:" + payType;
                    updateContentTV();
                    cancelCountDownTask();
                    Log.i(TAG, "支付方式:" + payType);
                }
            } else {
                if (message.equals(PayResultResponeCode.USERCANCEL.getCode())) {
                    showContent[1] = "支付结果:" + message;
                    showContent[2] = "平台返回:" + PayResultResponeCode.USERCANCEL.getValue();
                    updateContentTV();
                }
            }
        }
    };

    private UnifiedOrderListener unifiedOrderListener = new UnifiedOrderListener() {
        @Override
        public void onProcess(String message) {
            Log.i(TAG, "message:" + message);
            showContent[0] = "" + message;
            updateContentTV();
        }

        @Override
        public void onUnifiedResult(boolean result, String message, UnifiedOrderRespone respone) {
            Log.i(TAG, "onUnifiedResult:" + result + " ,message:" + message + " ,unifiedOrderRespone:" + respone.toString());
            if (result) {
                if (message.equals(UnifiedOrderResponseCode.SUCCESS.getCode())) { //统一下单成功
                    showContent[1] = "统一下单结果:" + message;
                    showContent[2] = "平台返回:" + UnifiedOrderResponseCode.SUCCESS.getValue();
                    updateContentTV();
                }
            } else {
                if (message.equals(UnifiedOrderResponseCode.USERCANCEL.getCode())) { //统一下单成功
                    showContent[1] = "统一下单结果:" + message;
                    showContent[2] = "平台返回:" + UnifiedOrderResponseCode.USERCANCEL.getValue();
                    updateContentTV();
                } else if (message.equals(UnifiedOrderResponseCode.TIMEOUT.getCode())) { //统一下单成功
                    showContent[1] = "统一下单结果:" + message;
                    showContent[2] = "平台返回:" + UnifiedOrderResponseCode.TIMEOUT.getValue();
                    updateContentTV();
                }
            }
        }

        @Override
        public void onQueryResult(boolean result, String message, UnifiedOrderResultRespone respone) {
            Log.i(TAG, "onQueryResult:" + result + " ,message:" + message + " ,unifiedOrderResultRespone:" + respone.toString());
            if (result) {
                showContent[1] = "统一下单支付结果:" + message;
                showContent[2] = "平台返回:" + respone.getResult_msg();
                updateContentTV();
            } else {
                showContent[1] = "统一下单支付结果:" + message;
                showContent[2] = "平台返回:" + respone.getResult_msg();
                updateContentTV();
            }
        }
    };

    private RefundListener refundListener = new RefundListener() {
        @Override
        public void onProcess(String message) {
            Log.i(TAG, "message:" + message);
            showContent[0] = "" + message;
            updateContentTV();
        }

        @Override
        public void onRefundResult(boolean result, String message, RefundRespone respone) {
            Log.i(TAG, "onRefundResult:" + result + " ,message:" + message + " ,refundRespone:" + respone.toString());
//            Log.i(TAG, "onUnifiedResult:" + result + " ,message:" + message);
            if (result) {
                showContent[0] = "";
                showContent[1] = "退款结果:" + message;
                showContent[2] = "平台返回:" + respone.getResult_msg();
                updateContentTV();
            } else {
                showContent[0] = "";
                showContent[1] = "退款结果:" + message;
                showContent[2] = "平台返回:" + respone.getResult_msg();
                updateContentTV();
            }
        }
    };
}
