package com.chinasunsoft.ept.ad.network;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.chinasunsoft.ept.ad.Constants;
import com.chinasunsoft.ept.ad.ShareApplication;
import com.chinasunsoft.ept.ad.bean.Result;
import com.chinasunsoft.ept.ad.bean.Version;
import com.chinasunsoft.ept.ad.event.Event;
import com.chinasunsoft.ept.ad.event.EventType;
import com.chinasunsoft.ept.ad.retrofit2.subscriber.BaseException;
import com.chinasunsoft.ept.ad.retrofit2.subscriber.SubscriberWithDialog;
import com.chinasunsoft.ept.ad.util.*;
import com.chinasunsoft.ept.ad.view.common.MProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/1/17.
 */

public class UpdateManager {
    public final static String TAG = UpdateManager.class.getName();
    Context mContext;
    boolean isSlent;

    public UpdateManager(Context context, boolean isSlient) {
        this.mContext = context;
        this.isSlent = isSlient;
    }

    public static void check() {
        Context context = ShareApplication.context;
        boolean firstInstall = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("FirstInstall", true);
        if (firstInstall) {
             boolean i = Util.unZipAssets(context, Constants.SOURCE_ASSETS_NAME, Constants.FILEDIR, true);
            if(i){
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("FirstInstall", false).apply();
            }
        }
    }


    private static Version mEptVersion;

    public static void updateEpt(final Context context) {

        Network.getService().getVersion("ept")
                .map(new Func1<Result, Version>() {
                    @Override
                    public Version call(Result result) {
                        if (!result.isSuccess()) {
                            throw new BaseException(result.getState(), result.getInfo());
                        }
                        return JSONObject.toJavaObject(result.getResult().getJSONObject("ept"),Version.class);
                    }
                })
                .map(new Func1<Version, String>() {
                    @Override
                    public String call(Version version) {
                        if(!isEptNewVersion(version.getVER())){
                            throw new BaseException("", "当前已是最新版本");
                        }
                        mEptVersion = version;
                        return version.getURL();
                    }
                })
                .flatMap(new Func1<String, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(String s) {
                        if(TextUtils.isEmpty(s)){
                            throwException("获取下载地址失败");
                        }
                        //s = "http://nj02all01.baidupcs.com/file/433d3f0c29b026518994c77b7846a9bd?bkt=p3-1400433d3f0c29b026518994c77b7846a9bd8155eefd0000000077aa&fid=1006787002-250528-1053183090044704&time=1484891636&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-eDLgdgvr6W7Lk7ObtBU0jZyf4lY%3D&to=nj2hb&fm=Nan,B,U,ny&sta_dx=30634&sta_cs=&sta_ft=zip&sta_ct=0&sta_mt=0&fm2=Nanjing02,B,U,ny&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=1400433d3f0c29b026518994c77b7846a9bd8155eefd0000000077aa&sl=79953999&expires=8h&rt=pr&r=152174951&mlogid=454095117148700410&vuk=1006787002&vbdid=1648859030&fin=ept.zip&fn=ept.zip&slt=pm&uta=0&rtype=1&iv=0&isw=0&dp-logid=454095117148700410&dp-callid=0.1.1&csl=283&csign=474N3rRih1RBO4Px3upiFO4cq2E%3D";
                        MProgressDialog.msg("下载文件，请稍候。。。");
                        return Network.getUpdateService().downloadApk(s);
                    }
                })
                .map(new Func1<ResponseBody, String>() {
                    @Override
                    public String call(ResponseBody responseBody) {
                        if(!writeResponseBodyToDisk(responseBody,Constants.EPT_PATH)){
                            throw new BaseException("", "保存文件失败");
                        }
                        return Constants.EPT_PATH;
                    }
                })
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String path) {
                        MProgressDialog.msg("解压安装，请稍候。。。");
                        return Util.unZipFile(path, Constants.FILEDIR, true);
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberWithDialog<Boolean>(context,"检查最新版本。。。") {
                    @Override
                    public void onFail(String errCode, String info) {
                        U.toast(info);
                    }

                    @Override
                    public void onSuccess(Boolean b) {
                        if(b){
                            U.toast("更新成功");
                            saveEptVersion();
                            Event.post(EventType.UPDATE_EPT_FINISH);
                        }
                    }
                });

    }

    public static void updateApk(final Context context) {


        Network.getService().getVersion("ad")
                .map(new Func1<Result, Version>() {
                    @Override
                    public Version call(Result result) {
                        if (!result.isSuccess()) {
                            throwException(result.getState(), result.getInfo());
                        }
                        return JSONObject.toJavaObject(result.getResult().getJSONObject("ad"),Version.class);
                    }
                })
                .map(new Func1<Version, String>() {
                    @Override
                    public String call(Version version) {
                        if(!isApkNewVersion(version.getVER())){
                            throwException("当前已是最新版本");
                        }
                        return version.getURL();
                    }
                })
                .flatMap(new Func1<String, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(String s) {
                        MProgressDialog.msg("下载文件，请稍候。。。");
                        if(TextUtils.isEmpty(s)){
                            throwException("获取下载地址失败");
                            //s= "http://huajun2.onlinedown.net/down/mark.tralor-1.3.1-20160510.apk";
                        }
                        return Network.getUpdateService().downloadApk(s);
                    }
                })
                .map(new Func1<ResponseBody, String>() {
                    @Override
                    public String call(ResponseBody responseBody) {
                        if(!writeResponseBodyToDisk(responseBody,Constants.APK_PATH)){
                            throwException("保存文件失败");
                        }
                        return Constants.APK_PATH;
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberWithDialog<String>(context,"检查最新版本。。。") {
                    @Override
                    public void onFail(String errCode, String info) {
                        U.toast(info);
                    }

                    @Override
                    public void onSuccess(String s) {
                        U.toast("下载完成");
                        installApk(context,s);
                    }
                });

    }
    private static void startDownload(Context context, Version v) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(Constants.VERSION, v);
        intent.setAction(DownloadService.ACTION_DOWNLOAD);
        context.startService(intent);
    }

    public static void updateEpt1(final Context context) {
        final String type = Version.TYPE_EPT;
        Network.getService().getVersion(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberWithDialog<Result>(context,"检查最新版本。。。") {
                    @Override
                    public void onFail(String errCode, String info) {
                        U.toast(info);
                    }

                    @Override
                    public void onSuccess(Result result) {
                        if (!result.isSuccess()) {
                            U.toast(result.getInfo());
                            return;
                        }

                        Version version = JSONObject.toJavaObject(result.getResult().getJSONObject(type),Version.class);
                        version.setType(type);
                        if(!version.isNewVersion()){
                            U.toast("当前已是最新版本");
                            return;
                        }
                        if(TextUtils.isEmpty(version.getURL())){
                            U.toast("当前已是最新版本");
                            return;
                        }

                        //String url = "http://shcm09.baidupcs.com/file/433d3f0c29b026518994c77b7846a9bd?bkt=p3-1400433d3f0c29b026518994c77b7846a9bd8155eefd0000000077aa&fid=1006787002-250528-1053183090044704&time=1484924392&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-OE7HSbnG%2FGuXJllROeHQfJjM8m8%3D&to=sh09vb&fm=Nan,B,M,mn&sta_dx=30634&sta_cs=1&sta_ft=zip&sta_ct=0&sta_mt=0&fm2=Nanjing02,B,M,mn&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=1400433d3f0c29b026518994c77b7846a9bd8155eefd0000000077aa&sl=79364174&expires=8h&rt=pr&r=957292046&mlogid=462888149722546401&vuk=1006787002&vbdid=1303506473&fin=ept.zip&fn=ept.zip&slt=pm&uta=0&rtype=1&iv=0&isw=0&dp-logid=462888149722546401&dp-callid=0.1.1&csl=100&csign=LbgzZVga1ZAOwNItmvyLazExnDE%3D";

                        //version.setURL(url);
                        startDownload(context,version);
                    }
                });



    }
    public static void updateApk1(final Context context) {
        final String type = Version.TYPE_APK;
        Network.getService().getVersion(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberWithDialog<Result>(context,"检查最新版本。。。") {
                    @Override
                    public void onFail(String errCode, String info) {
                        U.toast(info);
                    }

                    @Override
                    public void onSuccess(Result result) {
                        if (!result.isSuccess()) {
                            U.toast(result.getInfo());
                            return;
                        }
                        Version version = JSONObject.toJavaObject(result.getResult().getJSONObject(type),Version.class);
                        version.setType(type);
                        if(!version.isNewVersion()){
                            U.toast("当前已是最新版本");
                            return;
                        }
                        if(TextUtils.isEmpty(version.getURL())){
                            U.toast("当前已是最新版本");
                            return;
                        }
                        //String url = "http://dldir1.qq.com/weixin/android/weixin654android1000.apk";
                        //version.setVER("123123");
                        //String url= "http://huajun2.onlinedown.net/down/mark.tralor-1.3.1-20160510.apk";
                        //version.setURL(url);
                        startDownload(context,version);
                    }
                });
    }

    private static void throwException(String code,String s){
        throw new BaseException(code, s);
    }
    private static void throwException(String s){
        throw new BaseException("", s);
    }

    private static boolean isEptNewVersion(String version){
        if(TextUtils.isEmpty(version)){
            return false;
        }
        String current = PreferenceManager.getDefaultSharedPreferences(ShareApplication.context).getString(Constants.PREFER_EPT_VERSION,"0");
        return version.compareTo(current)>0;
    }
    private static boolean isApkNewVersion(String version){
        if(TextUtils.isEmpty(version)){
            return false;
        }
        String current = Util.getAppVersionName(ShareApplication.context);
        return version.compareTo(current)>0;
    }
    private static void saveEptVersion(){
        PreferenceManager.getDefaultSharedPreferences(ShareApplication.context).edit().putString(Constants.PREFER_EPT_VERSION,mEptVersion.getVER());
    }
    private static boolean writeResponseBodyToDisk(ResponseBody body,String filePath) {
        try {
            File futureStudioIconFile = new File(filePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    LogU.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 安装下载的apk文件
     *
     * @param filePath 文件全路径
     */
    private static void installApk(Context context
                                   ,String filePath) {
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            LogU.w(TAG, "cannot find apk to install");
            return;
        }

        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击打开会崩溃
    }



}
