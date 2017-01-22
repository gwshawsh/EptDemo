package com.chinasunsoft.ept.ad.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chinasunsoft.ept.ad.Constants;
import com.chinasunsoft.ept.ad.R;
import com.chinasunsoft.ept.ad.event.Event;
import com.chinasunsoft.ept.ad.event.EventType;
import com.chinasunsoft.ept.ad.util.U;
import com.chinasunsoft.ept.ad.network.WebviewUtil;
import com.chinasunsoft.ept.ad.view.common.MWebView;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {

    MWebView mWebView;
    WebSettings mWebSettings;
    ProgressBar progressbar;
    boolean isInit = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValues();
        EventBus.getDefault().register(this);
        mWebView.loadUrl(Constants.MAIN_URL_LOCAL_SDCARD);
        //mWebView.loadUrl("http://www.baidu.com");
        mWebView.setVisibility(View.INVISIBLE);
        ((ImageView)$(R.id.backgroud)).setImageResource(U.bootupImgId);

    }

    public void onEventMainThread(Event event) {
        switch (event.getType()){
            case EventType.UPDATE_EPT_FINISH:
                mWebView.reload();
                break;
            case EventType.onPageStarted:
                showProgressBar();
                break;
            case EventType.onPageFinished:
                if(isInit){
                    isInit = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            $(R.id.backgroud).setVisibility(View.GONE);
                            mWebView.setVisibility(View.VISIBLE);
                        }
                    },2000);

                }
                hideProgressBar();
                break;
            case EventType.onReceivedError:
                hideProgressBar();
                break;
            case EventType.WEBVIEW_DRAW_FINISH:

                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(mWebView.getUrl().endsWith("main.html")){
            super.onBackPressed();
        }else
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else {
            super.onBackPressed();
        }

    }

    void showProgressBar(){
        if(progressbar!=null){
            progressbar.setVisibility(View.VISIBLE);
        }
    }
    void hideProgressBar(){
       if(progressbar!=null){
            progressbar.setVisibility(View.GONE);
        }
    }
    void initValues() {
        progressbar = $(R.id.progressbar);
        mWebView = $(R.id.webview);
        WebviewUtil.initWebview(this,mWebView);
        mWebSettings = mWebView.getSettings();


    }
    @Override
    protected void onStop() {
        super.onStop();
        mWebSettings.setJavaScriptEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebSettings.setJavaScriptEnabled(true);
    }

}
