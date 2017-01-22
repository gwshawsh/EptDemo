package com.chinasunsoft.ept.ad.retrofit2.subscriber;

/**
 * Created by Administrator on 2017/1/19.
 */

public class BaseException extends RuntimeException {
    public BaseException(String code,String msg){
       this.code = code;
        this.msg = msg;
    }
    private String msg = "网络请求失败";

    public String getCode() {
        return code;
    }

    private String code = "";

    public String getMsg() {
        return msg;
    }
}
