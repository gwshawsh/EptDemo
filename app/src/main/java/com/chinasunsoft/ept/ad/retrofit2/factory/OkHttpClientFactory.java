package com.chinasunsoft.ept.ad.retrofit2.factory;


import com.chinasunsoft.ept.ad.retrofit2.factory.interceptor.LoggingInterceptorFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


public class OkHttpClientFactory {

    public static OkHttpClient create(Interceptor...interceptors){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(60, TimeUnit.SECONDS);
        for(Interceptor f:interceptors){
            builder.addInterceptor(f);
        }
        builder.addInterceptor(LoggingInterceptorFactory.create());
        return builder.build();
    }
}
