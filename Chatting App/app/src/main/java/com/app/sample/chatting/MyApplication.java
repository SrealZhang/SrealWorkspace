package com.app.sample.chatting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.app.sample.chatting.bean.NeoUser;
import com.app.sample.chatting.data.Constant;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import greendao.DaoMaster;
import greendao.DaoSession;

/**
 * Created by neo2 on 2016/7/4.
 */
public class MyApplication extends Application {
    private static final String TAG = "nilaiMyApplication";
    private static MyApplication mInstance;
    private static List<Activity> activityList = new LinkedList();
    private static Context mContext;
    private static Toast mToast;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static SQLiteDatabase db;

    public static NeoUser user;

    public static NeoUser getUser() {
        return user == null ? selectUserTxt() : user;
    }

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

    public static Context getTopActivity(){
        return activityList.get(activityList.size()-1);
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
        clearDate();
    }

    public static void clearDate() {
        daoMaster = null;
        daoSession = null;
        db = null;
        user = null;
        SharedPreferences.Editor editor = mContext.getSharedPreferences(Constant.userSPE, Activity.MODE_PRIVATE).edit();
        editor.putString("password", "");
        editor.commit();
    }

    @SuppressLint("InlinedApi")
    public static void restartApp(Context context) {
        finishActivityNokill();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 取得Db
     */
    public static SQLiteDatabase getDb() {
        if (db == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(mContext, Constant.getMYDB(), null);
            db = helper.getWritableDatabase();
        }
        return db;
    }

    /**
     * 取得DaoMaster
     */
    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            daoMaster = new DaoMaster(getDb());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     */
    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static void insertUserTxt(String user, String password) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(Constant.userSPE, Activity.MODE_PRIVATE).edit();
        editor.putString("user", user);
        editor.putString("password", password);
        editor.commit();
    }

    public static NeoUser selectUserTxt() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constant.userSPE, Activity.MODE_PRIVATE);
        NeoUser neoUser = new NeoUser(sharedPreferences.getString("user", ""), sharedPreferences.getString("password", ""));
        return neoUser;
    }

    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public void runThread(Runnable runnable) {
        cachedThreadPool.execute(runnable);
    }
}

