package com.chinasunsoft.ept.ad.network;


import com.chinasunsoft.ept.ad.retrofit2.factory.OkHttpClientFactory;
import com.chinasunsoft.ept.ad.retrofit2.factory.RetrofitFactory;

import okhttp3.OkHttpClient;

public class Network {

     static volatile RequestService service ;
     static volatile UpdateService updateService;

    private  synchronized static RequestService  initService() {
        OkHttpClient client = OkHttpClientFactory.create();
        retrofit2.Retrofit mRetrofit = RetrofitFactory.create(client);
        return mRetrofit.create(RequestService.class);

    }
    private  synchronized static UpdateService initDownloadService() {
        OkHttpClient client = OkHttpClientFactory.create();
        retrofit2.Retrofit mRetrofit = RetrofitFactory.create(client);
        return mRetrofit.create(UpdateService.class);

    }
    public static RequestService getService(){
        if(null==service){
            synchronized(Network.class){
                if(null==service){
                    service = initService();
                }
            }
        }
        return service;
    }

    public static UpdateService getUpdateService(){
        if(null== updateService){
            synchronized(Network.class){
                if(null== updateService){
                    updateService = initDownloadService();
                }
            }
        }
        return updateService;

    }




}
