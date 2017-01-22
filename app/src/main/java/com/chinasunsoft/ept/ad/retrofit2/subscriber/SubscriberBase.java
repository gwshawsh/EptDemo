package com.chinasunsoft.ept.ad.retrofit2.subscriber;

import rx.Subscriber;

/**
 * Subscriber 带等待旋转框
 * Created by 7 on 2016/11/1.
 */
public abstract class SubscriberBase<T> extends Subscriber<T> {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        ERROR e = ERROR.parse(throwable);
        onFail(e.getCode(),e.getMsg());
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }


    public void cancel(){
        unsubscribe();
        onCancel();
    }
    public void onCancel(){

    }
    abstract public void onFail(String errCode, String info);
    abstract public void onSuccess(T t);

}
