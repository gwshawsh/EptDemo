package com.chinasunsoft.ept.ad.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chinasunsoft.ept.ad.R;
import com.chinasunsoft.ept.ad.network.UpdateManager;
import com.chinasunsoft.ept.ad.util.LogU;
import com.chinasunsoft.ept.ad.util.U;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;


/**
 * Created by Administrator on 2017/1/19.
 */

public class BootUpActivity extends BaseActivity{
    private static final int LOAD_DISPLAY_TIME = 1500;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.activity_bootup);
        LogU.e("BootUpActivity","BootUpActivity onCreate");

        ((ImageView)$(R.id.bootup_img)).setImageResource(U.newBootupImgId());

    }

    @Override
    protected void onStart() {
        super.onStart();
        UpdateManager.check();
        startMainActivity();
        //initTbs();
    }
    QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished(boolean arg0) {
            LogU.i("initTbs","onViewInitFinished is " + arg0);
            startMainActivity();
        }

        @Override
        public void onCoreInitFinished() {
            LogU.i("initTbs","onCoreInitFinished");
        }
    };

    void startMainActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(BootUpActivity.this, MainActivity.class);
                BootUpActivity.this.startActivity(mainIntent);
                BootUpActivity.this.finish();
            }
        },500);

    }
    void initTbs(){

        LogU.e("BootUpActivity","initTbs");
        if(QbSdk.isTbsCoreInited()){
            QbSdk.preInit(getApplicationContext(), cb);
        }else {
            startMainActivity();
        }


        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                LogU.i("initTbs","onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                LogU.i("initTbs","onInstallFinish");
            }

            @Override
            public void onDownloadProgress(int i) {
                LogU.i("initTbs","onDownloadProgress:" + i);
            }
        });

        //QbSdk.initX5Environment(getApplicationContext(), cb);


    }

    void showProgressDialog(){
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("正在下载...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        progressDialog.show();
    }
}
