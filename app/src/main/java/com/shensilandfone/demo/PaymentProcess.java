package com.shensilandfone.demo;

import android.os.AsyncTask;
import android.os.Message;

/**
 * Method: PaymentProcess
 * Decription:
 * Author: raoj
 * Date: 2017/10/12
 **/
public class PaymentProcess extends AsyncTask<String, Message, Object> {

    public PaymentProcess() {
        super();
    }

    @Override
    protected Object doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
