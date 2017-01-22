package com.chinasunsoft.ept.ad.event;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/1/16.
 */

public class Event {
    public static void post(int type){
        EventBus.getDefault().post(new Event(type));
    }
    public static void post(int type,Object o){
        EventBus.getDefault().post(new Event(type,o));
    }

    int type;
    Object o;
    public Event(int type){
        this.type = type;
    }
    public Event(int type,Object o){
        this.type = type;
        this.o = o;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }
}

