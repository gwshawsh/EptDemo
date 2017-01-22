package com.chinasunsoft.ept.ad.network;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface UpdateService {

    @GET
    Observable<ResponseBody> downloadApk(@Url String url);

    @GET("/resource/example.zip")
    Observable<ResponseBody> downloadWebResources();
}
