package com.chinasunsoft.ept.ad.retrofit2.factory.interceptor;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2017/1/18.
 */

public class LoggingInterceptorFactory{
    public static Interceptor create() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }
}
