package com.chinasunsoft.ept.ad.retrofit2.factory.interceptor;

import okhttp3.Interceptor;

/**
 * Created by Administrator on 2017/1/18.
 */

public class HeaderInterceptorFactory{
    public static Interceptor create() {
        HeaderInterceptor interceptor = new HeaderInterceptor();
        return interceptor;
    }
}
