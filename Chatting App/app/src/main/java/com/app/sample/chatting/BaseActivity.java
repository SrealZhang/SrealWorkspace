package com.app.sample.chatting;

import android.support.v7.app.AppCompatActivity;

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
}
