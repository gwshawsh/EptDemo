package com.chinasunsoft.ept.ad.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by Administrator on 2017/1/17.
 */

public class Util {
    public final static String TAG = Util.class.getName();


    public static String getAppVersionName(Context context)
    {
        PackageManager pm;
        PackageInfo info;
        String versionName = "unkown";
        pm = context.getPackageManager();
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogU.e("Util","当前版本"+versionName);
        return versionName;
    }

    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }


    public static boolean unZipFile(String inputFilePath, String outputDirectory, boolean isReWrite) {
        // 打开压缩文件
        File file = new File(inputFilePath);
        try {
            if(!file.isFile()){
                throw new IOException("target is not file");
            }
            FileInputStream inputStream = new FileInputStream(file);
            unZip(inputStream,outputDirectory,isReWrite);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;


    }
    /**
     * 解压assets的zip压缩文件到指定目录
     * @param context 上下文对象
     * @param assetName 压缩文件名
     * @param outputDirectory 输出目录
     * @param isReWrite 是否覆盖
     * @throws IOException
     */
    public static boolean unZipAssets(Context context, String assetName, String outputDirectory, boolean isReWrite){
        // 打开压缩文件
        try {
            InputStream inputStream = context.getAssets().open(assetName);
            unZip(inputStream,outputDirectory,isReWrite);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public static void unZip(InputStream inputStream, String outputDirectory, boolean isReWrite) throws IOException {
        LogU.e(TAG, "unZip");
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            // 如果是一个目录
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者是文件不存在
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者文件不存在，则解压文件
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
            }
            // 定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();

    }

    public static void runOnMainThread(Action0 action){
        AndroidSchedulers.mainThread().createWorker().schedule(action);
    }
}
