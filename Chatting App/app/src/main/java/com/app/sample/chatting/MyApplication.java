package com.app.sample.chatting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by neo2 on 2016/7/4.
 */
public class MyApplication extends Application {
    private static final String TAG = "nilaiMyApplication";
    private static MyApplication mInstance;
    private static List<Activity> activityList = new LinkedList();
    private static Context mContext;
    private static Toast mToast;

    public static synchronized MyApplication getmInstance() {
        if (mInstance == null) {
            Log.d(TAG, "mInstance is null");
            mInstance = new MyApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = new MyApplication();
        SetmContext(getApplicationContext());
    }

    public static void SetmContext(Context context) {
        mContext = context;
    }

    public static void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(String text, int duration) {
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, duration);
        mToast.show();
    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //从容器中remove
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    //遍历所有Activity并exit应用
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    public static void finishActivityNokill() {
        for (Activity activity : activityList) {
            if (null != activity) {
                activity.finish();
            }
        }
    }

    @SuppressLint("InlinedApi")
    public static void restartApp(Context context) {
        finishActivityNokill();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public void runThread(Runnable runnable) {
        cachedThreadPool.execute(runnable);
    }
}

