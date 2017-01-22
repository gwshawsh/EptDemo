package com.chinasunsoft.ept.ad.retrofit2.subscriber;

import android.content.Context;

import com.chinasunsoft.ept.ad.view.common.MProgressDialog;

import rx.Subscriber;

/**
 * Subscriber 带等待旋转框
 * Created by 7 on 2016/11/1.
 */
public abstract class SubscriberWithDialog<T> extends Subscriber<T> {

    Context context;
    String msg = "请稍候。。。";
    public SubscriberWithDialog(Context context){
        this.context = context;
    }
    public SubscriberWithDialog(Context context, String msg){
        this.context = context;
        this.msg = msg;
    }

    @Override
    public void onStart() {
        super.onStart();
        showDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        closeDialog();
        throwable.printStackTrace();
        ERROR e = ERROR.parse(throwable);
        onFail(e.getCode(),e.getMsg());
    }

    @Override
    public void onCompleted() {
        closeDialog();
    }

    @Override
    public void onNext(T t) {
        closeDialog();
        onSuccess(t);
    }

    private void showDialog(){
        if(isUnsubscribed()){
            return;
        }
        MProgressDialog dialog = MProgressDialog.show(context,msg);
        dialog.setOnBackPressedListener(new MProgressDialog.OnBackPressedListener() {
            @Override
            public void onBackPress() {
                cancel();
            }
        });

    }
    private void cancel(){
        unsubscribe();
        onCancel();
    }
    private void closeDialog(){
        MProgressDialog.closeDialog();
    }
    public void onCancel(){

    }
    abstract public void onFail(String errCode, String info);
    abstract public void onSuccess(T t);

}
