package com.app.sample.chatting;

import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by neo2 on 2016/7/5.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.getmInstance().addActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getmInstance().removeActivity(this);
    }

    /**
     * 隐藏输入法
     */
    public void RequestFocus(View v) {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            if (currentFocus.getWindowToken() != null) {
                InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            if (currentFocus.getWindowToken() != null) {
                InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
