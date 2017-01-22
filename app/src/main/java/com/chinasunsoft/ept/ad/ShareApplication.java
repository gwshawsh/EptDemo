package com.chinasunsoft.ept.ad;

import android.app.Application;
import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by Administrator on 2017/1/16.
 */

public class ShareApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        FileDownloader.init(getApplicationContext());
    }



}
