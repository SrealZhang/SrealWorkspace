package com.app.sample.chatting.util;

import android.widget.Toast;

import com.app.sample.chatting.MyApplication;

/**
 * Created by androidDEV2 on 2016/7/7.
 */
public class ToastUtil {
    public static void toast(String msg){
        //此方法需要有Application单例并由其管理Activity
        Toast.makeText(MyApplication.getmInstance().getTopActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}
