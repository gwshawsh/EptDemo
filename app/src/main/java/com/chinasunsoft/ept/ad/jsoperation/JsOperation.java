package com.chinasunsoft.ept.ad.jsoperation;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSONObject;
import com.chinasunsoft.ept.ad.ShareApplication;
import com.chinasunsoft.ept.ad.network.UpdateManager;
import com.chinasunsoft.ept.ad.util.LogU;
import com.chinasunsoft.ept.ad.util.Util;

import rx.functions.Action0;

/**
 * Created by Administrator on 2016/10/28.
 */
public class JsOperation {

    public JsOperation(Context context){
        this.context = context;
    }
    Context context;

    @JavascriptInterface
    public void back(){
        if(context instanceof Activity){
            ((Activity)context).finish();
        }

    }

    @JavascriptInterface
    public void exit(){
        if(context instanceof Activity){
            ((Activity)context).finish();
        }

    }

    @JavascriptInterface
    public void update(){
        Util.runOnMainThread(new Action0() {
            @Override
            public void call() {
                UpdateManager.updateApk1(context);
            }
        });

    }
    @JavascriptInterface
    public void updateEpt(){
        Util.runOnMainThread(new Action0() {
            @Override
            public void call() {
                UpdateManager.updateEpt1(context);
            }
        });
    }

    @JavascriptInterface
    public String currentApkVersion(){
        JSONObject json = new JSONObject();
        json.put("",Util.getAppVersionName(ShareApplication.context));
        LogU.e("",json.toJSONString());
        return json.toJSONString();

    }
}
