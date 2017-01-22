package com.chinasunsoft.ept.ad.event;


/**
 * Created by Administrator on 2017/1/18.
 */

public class Download {
    private boolean done = false;
    private long currentFileSize;
    private long totalFileSize;

    public Download(long totalFileSize,long currentFileSize, boolean done){
        this.totalFileSize =totalFileSize;
        this.currentFileSize = currentFileSize;
        this.done = done;
    }

    public long getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(long currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }




}
