package com.sslf.qrcode;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Method: OkHttpCall
 * Decription:
 * Author: raoj
 * Date: 2017/10/23
 **/
public class OkHttpCall implements okhttp3.Call {

    okhttp3.Call realCall;

    public OkHttpCall(okhttp3.Call call) {
        this.realCall = call;
    }

    @Override
    public Request request() {
        return realCall.request();
    }

    @Override
    public Response execute() throws IOException {
        return realCall.execute();
    }

    @Override
    public void enqueue(final Callback responseCallback) {
        realCall.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                MainThreadExecutor.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        responseCallback.onFailure(call, e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                MainThreadExecutor.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            responseCallback.onResponse(call, response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void cancel() {
        realCall.cancel();
    }

    @Override
    public boolean isExecuted() {
        return realCall.isExecuted();
    }

    @Override
    public boolean isCanceled() {
        return realCall.isCanceled();
    }

}
