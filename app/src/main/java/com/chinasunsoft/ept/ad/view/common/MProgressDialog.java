/**
 * 
 */
package com.chinasunsoft.ept.ad.view.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;


public class MProgressDialog extends ProgressDialog {
	private OnBackPressedListener listener;
	private static MProgressDialog mDialog;
	public interface OnBackPressedListener{
		void onBackPress();
	}
	Context context;
	@Override
	protected void onStop() {
		super.onStop();
		mDialog = null;
	}

	@Override
	public void onBackPressed() {
		Log.e("MProgressDialog", "onBackPressed");
		if(null!=listener){
			listener.onBackPress();
		}
		super.onBackPressed();
		mDialog = null;
	}
	/**
	 * @param context
	 */
	public MProgressDialog(Context context) {
		super(context,ProgressDialog.THEME_HOLO_LIGHT);
		
	}
	public MProgressDialog(Context context, int theme){
		super(context,theme);
	}
    
	public void setOnBackPressedListener(OnBackPressedListener listener){
		this.listener = listener;
	}


    public static MProgressDialog show(Context context, String message){
		Log.e("MProgressDialog", "show");
		closeDialog();
		mDialog= new MProgressDialog(context) ;
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setMessage(message); ;
		mDialog.show() ;
         return mDialog ;
    }
	public static void closeDialog(){
		if(null!=mDialog && mDialog.isShowing()){
			Log.e("MProgressDialog", "closeDialog");
			mDialog.dismiss();
			mDialog = null;
		}
	}
	public static boolean isShown(){
		return null!= mDialog && mDialog.isShowing();
	}

	public static void msg(final String msg){
		AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
			@Override
			public void call() {
				if(isShown()){
					mDialog.setMessage(msg);
				}
			}
		});


	}


}
