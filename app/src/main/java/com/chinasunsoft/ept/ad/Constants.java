package com.chinasunsoft.ept.ad;

import java.io.File;

/**
 * Created by Administrator on 2017/1/16.
 */

public class Constants{
    public static final String FILEDIR = ShareApplication.context
            .getFilesDir()
            + File.separator;
    public static final String FILEDIR_TEMP = ShareApplication.context
            .getExternalCacheDir()
            + File.separator;

    public static final String APK_PATH = ShareApplication.context.getExternalCacheDir() + "/" + "app.apk";
    public static final String EPT_PATH = ShareApplication.context.getExternalCacheDir() + "/" + "ept.zip";
    public final static String SOURCE_ASSETS_NAME = "ept.zip";
    public final static String BASE_URL = "http://chinasunsoft.net:8888/tongjihz/";
    public final static String MAIN_URL_WEB = "http://121.42.28.12:8888/ept";
    public final static String MAIN_URL_LOCAL = "file:///android_asset/ept/index.html";
    public final static String MAIN_URL_LOCAL_SDCARD = "file:///"+FILEDIR+"ept/index.html";



    public final static String PREFER_EPT_VERSION = "PREFER_EPT_VERSION";






    public static final String VERSION = "VERSION";


}
