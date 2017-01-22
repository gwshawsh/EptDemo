package com.chinasunsoft.ept.ad.retrofit2.factory.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/18.
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //builder.addHeader("Accept-Encoding", "gzip");  不要添加，OkHttp会自动压缩解压；添加后反而不能自动解压
        Request request = builder.build();
        Response response = chain.proceed(request);
        return response;
    }
}
