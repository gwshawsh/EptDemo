package com.chinasunsoft.ept.ad.view.common;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Administrator on 2017/1/22.
 */

public class MWebView extends WebView {

    private boolean isRendered = false;
    public void setOnDrawListener(OnLoadFinishListener listener){
        this.mOnLoadFinishListener = listener;
    }
    public MWebView(Context context) {
        super(context);
    }
    public interface OnLoadFinishListener {
        void onLoadFinish();
    }
    OnLoadFinishListener mOnLoadFinishListener;
    public MWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*if (!isRendered) {
            LogU.e("MWebView", "getContentHeight():" + getContentHeight());
            isRendered = getContentHeight() > 0;
            if (mOnLoadFinishListener != null) {
                mOnLoadFinishListener.onLoadFinish();
            }
        }*/
    }
}
