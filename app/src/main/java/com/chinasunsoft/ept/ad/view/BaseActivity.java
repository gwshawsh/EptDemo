package com.chinasunsoft.ept.ad.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chinasunsoft.ept.ad.R;
import com.chinasunsoft.ept.ad.event.Download;

/**
 * Created by Administrator on 2017/1/16.
 */

public class BaseActivity extends Activity {
    public <T extends View> T $(int id){
        return (T) super.findViewById(id);
    }
    public <T extends View> T $(View view,int id){
        return (T) view.findViewById(id);
    }
    protected ProgressDialog mDownloadProgressDialog;

    protected void updateDownLoadProgress(Download download){
        if(mDownloadProgressDialog!=null && mDownloadProgressDialog.isShowing()){

        }
    }

    /** * 设置状态栏颜色 * * @param activity 需要设置的activity * @param color 状态栏颜色值 */
    public  void setStatusBar( int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }
    /** * 生成一个和状态栏大小相同的矩形条 * * @param activity 需要设置的activity * @param color 状态栏颜色值 * @return 状态栏矩形条 */
    private  View createStatusView(int color) {
        // 获得状态栏高度
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar(getResources().getColor(R.color.colorToolBar));
    }
}
