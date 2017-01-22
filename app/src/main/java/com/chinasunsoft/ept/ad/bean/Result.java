package com.chinasunsoft.ept.ad.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2017/1/19.
 */

public class Result {
    public final static String CODE_SUCCESS = "200";
    public final static String CODE_SEVER_ERROR = "500";
    public final static String CODE_NO_DATA = "0";


    String state = "";
    String info = "";
    JSONObject result;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public boolean isSuccess(){
        return CODE_SUCCESS.equals(state);
    }
}
