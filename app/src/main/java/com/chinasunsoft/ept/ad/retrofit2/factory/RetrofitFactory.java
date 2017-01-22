package com.chinasunsoft.ept.ad.retrofit2.factory;


import com.chinasunsoft.ept.ad.Constants;
import com.chinasunsoft.ept.ad.retrofit2.adapters.fastjson.FastJsonConverterFactory;
import com.chinasunsoft.ept.ad.retrofit2.adapters.rxjava.RxJavaCallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import rx.schedulers.Schedulers;


public class RetrofitFactory {

    public static Retrofit create(OkHttpClient client){
        return new Retrofit.Builder()
                //基础url
                .baseUrl(Constants.BASE_URL)
                .client(client)
                //用于Json数据的转换,需要compile 'com.squareup.retrofit2:converter-gson:2.0.0',非必须
                .addConverterFactory(FastJsonConverterFactory.create())
                //用于返回Rxjava调用,需要 compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0',非必须
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }
}
