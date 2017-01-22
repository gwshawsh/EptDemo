package com.chinasunsoft.ept.ad.network;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.Toast;

import com.chinasunsoft.ept.ad.Constants;
import com.chinasunsoft.ept.ad.R;
import com.chinasunsoft.ept.ad.bean.Version;
import com.chinasunsoft.ept.ad.event.Event;
import com.chinasunsoft.ept.ad.event.EventType;
import com.chinasunsoft.ept.ad.util.LogU;
import com.chinasunsoft.ept.ad.util.Util;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

public class DownloadService extends IntentService {


    private static final String TAG = "UpdateService";

    private static final int NOTIFICATION_ID = 0;
    private static final int NOTIFICATION_ID_EPT = 1;
    private static final String  ACTION_CLICK = "ACTION_CLICK";
    private static final String ACTION_CANCEL = "ACTION_CANCEL";
    public static final String ACTION_DOWNLOAD = "ACTION_DOWNLOAD";

    private NotificationManager mNotifyManager;
    private Builder mBuilder;


    public DownloadService() {
        super("UpdateService");
    }
    Toast mToast;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if(ACTION_DOWNLOAD.equals(action)){
            Version version = (Version) intent.getSerializableExtra(Constants.VERSION);
            int id = download(version);

            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new Builder(this);
            String appName = getString(getApplicationInfo().labelRes);
            int icon = getApplicationInfo().icon;
            mBuilder.setContentTitle(appName).setSmallIcon(icon);
            Intent click = new Intent(this,DownloadService.class);
            click.putExtra("ID",id);
            click.setAction(ACTION_CLICK);
            PendingIntent clickIntent = PendingIntent.getService(this, 0, click, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent cancel = new Intent(this,DownloadService.class);
            click.putExtra("ID",id);
            click.setAction(ACTION_CANCEL);
            PendingIntent cancelIntent = PendingIntent.getService(this, 0, cancel, PendingIntent.FLAG_UPDATE_CURRENT);
            //setContentInent如果不设置在4.0+上没有问题，在4.0以下会报异常
            mBuilder.setContentIntent(clickIntent);
            mBuilder.setDeleteIntent(cancelIntent);
            
        }else if(ACTION_CLICK.equals(action)){

            int id = intent.getIntExtra("ID",-777);
            log("用户 ACTION_CLICK id :" + id);
            FileDownloader.getImpl().pause(intent.getIntExtra("ID",-777));
            /*if(FileDownloadStatus.paused == FileDownloader.getImpl().getStatusIgnoreCompleted(id)){
                FileDownloader.getImpl().startForeground(id,mBuilder.build());
            }*/
        }else if(ACTION_CANCEL.equals(action)){//cancel貌似在魅族手机监听不到 intent action
            int id = intent.getIntExtra("ID",-777);
            log("用户 ACTION_CANCEL ID:"+intent.getIntExtra("ID",-777));

            //FileDownloader.getImpl().pause(intent.getIntExtra("ID",-777));

        }

    }

    private void updateProgress(int id,int progress) {
        //"正在下载:" + progress + "%"
        mBuilder.setContentText(this.getString(R.string.android_auto_update_download_progress, progress)).setProgress(100, progress, false);
        mNotifyManager.notify(id, mBuilder.build());

    }


    private void installAPk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    private  int download(final Version version){
       return FileDownloader.getImpl().create(version.getURL()).setPath(version.getPath())
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void started(BaseDownloadTask task) {
                        super.started(task);
                        toast("开始下载");
                        LogU.e("download","started");
                    }

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        LogU.e("download","pending");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int id = version.isApk()?NOTIFICATION_ID:NOTIFICATION_ID_EPT;
                        int p = (int)(soFarBytes*100L/totalBytes);
                        updateProgress(id,p);
                        LogU.e("download","progress-->"+p +"  soFarBytes: "+soFarBytes+"   totalBytes: "+totalBytes);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {

                        if(version.isApk()){
                            installAPk(new File(version.getPath()));
                            mNotifyManager.cancel(NOTIFICATION_ID);
                        }else {
                            if(Util.unZipFile(task.getPath(),Constants.FILEDIR,true)){
                                toast("更新成功");
                                version.saveCurrentVersion();
                                Event.post(EventType.UPDATE_EPT_FINISH);
                                mNotifyManager.cancel(NOTIFICATION_ID_EPT);
                            }
                        }
                        LogU.e("download","completed");

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        LogU.e("download","paused");
                        toast("暂停下载");

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        LogU.e("download","error");
                        e.printStackTrace();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        LogU.e("download","warn");
                        toast("正在下载");
                    }
                }).start();
    }

    void toast(final String text){
        AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                if(mToast==null){
                    mToast = Toast.makeText(DownloadService.this,text,Toast.LENGTH_SHORT);
                }
                mToast.setText(text);
                mToast.show();
            }
        });
    }
    void log(final String  text){
        AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                LogU.e("UpdateService",text);
            }
        });
    }
}
