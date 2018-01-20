package com.sslf.qrcode.utils;

import android.content.Context;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Method: OkHttpUtils
 * Decription: 全局统一使用的OkHttpClient工具，okhttp版本：okhttp3.2
 * Author: raoj
 * Date: 2017/10/23
 **/
public class OkHttpUtils {
    public static final long DEFAULT_READ_TIMEOUT_MILLIS = 15 * 1000;
    public static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 20 * 1000;
    public static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 20 * 1000;
    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private static volatile OkHttpUtils sInstance;
    private static volatile Call call;
    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder builder;

    private OkHttpUtils() {
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        //包含header、body数据
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient();
        builder = mOkHttpClient.newBuilder();
        builder.readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .sslSocketFactory(getsslContext().getSocketFactory())
                .hostnameVerifier(hostnameVerifier);
        mOkHttpClient = builder.build();
//        mOkHttpClient = new OkHttpClient.Builder()
//                .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
//                .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
//                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
//                .sslSocketFactory(getsslContext().getSocketFactory())
//                .hostnameVerifier(hostnameVerifier);
//                //FaceBook 网络调试器，可在Chrome调试网络请求，查看SharePreferences,数据库等
//                .addNetworkInterceptor(new StethoInterceptor())
//                //http数据log，日志中打印出HTTP请求&响应数据
//                .addInterceptor(loggingInterceptor)
    }

    public static OkHttpUtils getInstance() {
        if (sInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (sInstance == null) {
                    sInstance = new OkHttpUtils();
                }
            }
        }
        return sInstance;
    }

    public static Call getCallInstance(Request request) {
        if (call == null) {
            synchronized (OkHttpUtils.class) {
                if (call == null) {
                    call = getInstance().getOkHttpClient().newCall(request);
                }
            }
        }
        return call;
    }

    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return builder;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
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

    public void setCache(Context appContext) {
        final File baseDir = appContext.getApplicationContext().getCacheDir();
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            mOkHttpClient.newBuilder().cache((new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE)));
        }
    }

}
