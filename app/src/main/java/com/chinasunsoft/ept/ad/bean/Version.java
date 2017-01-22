package com.chinasunsoft.ept.ad.bean;

import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.chinasunsoft.ept.ad.Constants;
import com.chinasunsoft.ept.ad.ShareApplication;
import com.chinasunsoft.ept.ad.util.Util;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/19.
 */

public class Version implements Serializable{
    public static final String TYPE_APK = "ad";
    public static final String TYPE_EPT = "ept";

    String VER="";
    String URL="";
    String DES="";

    public static String currentApkVersion(){
        return Util.getAppVersionName(ShareApplication.context);
    }
    public static String currentEptVersion(){
        return PreferenceManager.getDefaultSharedPreferences(ShareApplication.context).getString(Constants.PREFER_EPT_VERSION,"0");
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type = TYPE_APK;

    public  String getPath(){
        return  Constants.FILEDIR_TEMP+VER+(TYPE_APK.equals(type) ?".apk" :".zip");
    }

    public boolean isNewVersion(){
        return isApk()? isApkNewVersion() : isEptNewVersion();
    }
    public void saveCurrentVersion(){
        if(!isApk()){
            PreferenceManager.getDefaultSharedPreferences(ShareApplication.context).edit().putString(Constants.PREFER_EPT_VERSION,VER).apply();

        }
    }

    public boolean isApk(){
        return type.equals(TYPE_APK);
    }
    private boolean isApkNewVersion(){
        if(TextUtils.isEmpty(VER)){
            return false;
        }
        String current = currentApkVersion();
        return VER.compareTo(current)>0;
    }
    private boolean isEptNewVersion(){
        if(TextUtils.isEmpty(VER)){
            return false;
        }
        String current = currentEptVersion();
        return VER.compareTo(current)>0;
    }


    public String getVER() {
        return VER;
    }

    public void setVER(String VER) {
        this.VER = VER;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getDES() {
        return DES;
    }

    public void setDES(String DES) {
        this.DES = DES;
    }

    public static  Version fromJson(JSONObject object){
        return JSONObject.toJavaObject(object,Version.class);
    }
}
