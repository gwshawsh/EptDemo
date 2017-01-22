package com.chinasunsoft.ept.ad.retrofit2.subscriber;

import android.net.ParseException;

import com.alibaba.fastjson.JSONException;
import com.chinasunsoft.ept.ad.retrofit2.adapters.rxjava.HttpException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

/**
 * Created by Administrator on 2016/11/1.
 */
public class ERROR {
    public static final String CODE_HTTP = "ERROR_CODE_HTTP";
    public static final String CODE_JSON = "ERROR_CODE_JSON";
    public static final String CODE_CONNECT = "ERROR_CODE_CONNECT";
    public static final String CODE_SSL = "ERROR_CODE_SSL";
    public static final String CODE_UNKNOWN = "ERROR_CODE_UNKNOWN";
    public static final String CODE_TIMEOUT = "ERROR_CODE_TIMEOUT";
    public static final String CODE_UNKNOWN_HOST = "CODE_UNKNOWN_HOST";


    private String msg = "网络请求失败";

    public String getCode() {
        return code;
    }

    private String code = CODE_HTTP;

    public String getMsg() {
        return msg;
    }

    public ERROR(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static ERROR parse(Throwable e) {
        if(e instanceof BaseException){
            return new ERROR(((BaseException) e).getCode(),((BaseException) e).getMsg());
        }else
        if (e instanceof HttpException) {
            HttpException ex = (HttpException) e;
            return new ERROR(CODE_HTTP, "网络请求失败" + "(" + ex.code() + ")");
        } else if (e instanceof JSONException || e instanceof ParseException) {
            return new ERROR(CODE_JSON, "报文解析失败");
        } else if (e instanceof ConnectException) {
            return new ERROR(CODE_CONNECT, "网络连接失败");
        } else if (e instanceof SSLException) {
            return new ERROR(CODE_SSL, "SSL证书验证失败");
        } else if (e instanceof SocketTimeoutException) {
            return new ERROR(CODE_TIMEOUT, "网络请求超时");
        } else if (e instanceof UnknownHostException) {
            return new ERROR(CODE_UNKNOWN_HOST, "无法解析服务器地址");
        }
        else {
            return new ERROR(CODE_UNKNOWN, "网络请求失败");
        }
    }

}
