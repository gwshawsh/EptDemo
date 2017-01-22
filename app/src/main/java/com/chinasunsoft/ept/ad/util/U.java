package com.chinasunsoft.ept.ad.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.chinasunsoft.ept.ad.R;
import com.chinasunsoft.ept.ad.ShareApplication;

import java.util.Random;

/**
 * Created by Administrator on 2017/1/16.
 */

public class U {
    static Toast toast;
    public  static int bootupImgId = R.drawable.bootup1;
    public static int newBootupImgId(){
        bootupImgId = new Random().nextInt(2)==0 ?R.drawable.bootup1 :R.drawable.bootup2;
        return bootupImgId;
    }
    /**
     * 短toast
     * @param text
     */
    public static void toast(final String text) {

            toast(text, Toast.LENGTH_SHORT);


    }

    /**
     * 长toast
     * @param text
     */
    public static void toastLong(String text){
        toast(text, Toast.LENGTH_LONG);
    }

    public static void toast(String text, int toastDuration) {
        if (!TextUtils.isEmpty(text)) {
            if(toast==null){
                synchronized (U.class){
                    if(toast==null){
                        toast = Toast.makeText(ShareApplication.context,text,toastDuration);
                    }
                }
            }
            toast.setDuration(toastDuration);
            toast.show();
        }
    }
}
