package com.chinasunsoft.ept.ad.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinasunsoft.ept.ad.event.Event;
import com.chinasunsoft.ept.ad.event.EventType;
import com.chinasunsoft.ept.ad.jsoperation.JsOperation;
import com.chinasunsoft.ept.ad.util.LogU;
import com.chinasunsoft.ept.ad.util.U;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class WebviewUtil {
    public static String TAG = "WebviewUtil.class";
    public static String[] FILTER;
    public static String RES_FOLDER = "webres";

    public static void initLocalResList(Context context) {
        if (null == FILTER) {
            try {
                FILTER = context.getAssets().list(RES_FOLDER);
                LogU.e(TAG, "FILTER LIST:  " + Arrays.asList(FILTER));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static String getResFileName(String url) {
        for (String s : FILTER) {
            if (url.endsWith(s)) {
                return s;
            }
        }
        return "";
    }

    private static String getResPath(String filename) {
        return RES_FOLDER + File.separator + filename;
    }


    public static String getMimeType(String tag) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(tag);
        return type;
    }

    public static void initWebview(final Context context, final WebView mWebView) {
        initLocalResList(context);
        //mWebView.clearCache(true);
        //scroll bars
        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直显示

        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启Javascript脚本
        mWebSettings.setJavaScriptEnabled(true);

        // 启用localStorage 和 essionStorage
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setLoadsImagesAutomatically(true);

        // 开启应用程序缓存
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        String appCacheDir = context.getApplicationContext()
                .getDir("cache", Context.MODE_PRIVATE).getPath();
        mWebSettings.setAppCachePath(appCacheDir);

        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);


        //mWebSettings.setAppCacheMaxSize(1024 * 1024 * 10);// 设置缓冲大小，我设的是10M
        mWebSettings.setAllowFileAccess(true);

        // 开启插件（对flash的支持）
        //mWebSettings.setPluginsEnabled(true);
        mWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);


        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        //js的支持
        mWebSettings.setJavaScriptEnabled(true);

        //设置WebView可触摸放大缩小

        mWebSettings.setDisplayZoomControls(false); //隐藏webview缩放按钮

        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setDisplayZoomControls(false);


        //重写WebChromeClient是为了能正常播放HTML5视频(但是android webview不支持自动播放,pc上的chrome支持)
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }

        });
        mWebView.setWebViewClient(new WebViewClient() {
            long start = 0;
            long temp = 0;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Event.post(EventType.onPageStarted);
                start = System.nanoTime();
                temp = System.nanoTime();


            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                // LogU.e(TAG,"shouldInterceptRequest "+url +"   " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + "sss");
                /*String fileName = getResFileName(url);
                if (TextUtils.isEmpty(fileName)) {
                    return super.shouldInterceptRequest(view, url);
                }
                try {
                    InputStream is = context.getAssets().open(getResPath(fileName));
                    //LogU.e(TAG,"InterceptRequest "+fileName);
                    return new WebResourceResponse(getMimeType(url), "UTF-8", is);
                } catch (Exception e) {
                    e.printStackTrace();
                    return super.shouldInterceptRequest(view, url);
                }*/

                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);

                LogU.e(TAG, url + ":  " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - temp) + "毫秒");
                temp = System.nanoTime();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                //U.toast("page finished 加载完成");
                Event.post(EventType.onPageFinished);
                LogU.e(TAG, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + "毫秒");
                // mWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                U.toast("加载失败,请检查网络后重试");
                Event.post(EventType.onReceivedError);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }


        });
        mWebView.addJavascriptInterface(new JsOperation(context), "Android");
    }
}
