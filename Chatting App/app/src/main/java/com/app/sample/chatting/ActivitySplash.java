package com.app.sample.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.app.sample.chatting.data.Tools;
import com.app.sample.chatting.event.LoggedInEvent;
import com.app.sample.chatting.service.IMContactServiceHelper;
import com.app.sample.chatting.util.SaveUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import greendao.NeoUser;

public class ActivitySplash extends BaseActivity {
    private static final String TAG = "nilaiActivitySplash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bindLogo();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = null;
                List<NeoUser> user = SaveUtil.selectUser();
                if (user.size() > 0) {
                    IMContactServiceHelper.getmInstance().loginorRegist(ActivitySplash.this, user.get(0).getName(), user.get(0).getPassword(), 0);
                } else {
                    i = new Intent(ActivitySplash.this, ActivityLogin.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        // Show splash screen for 3 seconds
        new Timer().schedule(task, 1000);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    //path:/storage/sdcard1/DCIM/Camera/IMG_20160702_085019.jpg
    //登录返回事件
    public void onEventMainThread(LoggedInEvent event) {
        if (event.isSuccessful()) {
            Log.d(TAG, "Successful login---回调成功");
            startActivity(new Intent(this, ActivityMain.class));
            finish();
        } else {
            MyApplication.showToast("聊天服务器验证失败");
            startActivity(new Intent(this, ActivityLogin.class));
            finish();
        }
    }

    private void bindLogo() {
        // Start animating the image
        final TextView splash = (TextView) findViewById(R.id.splash);
        final AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(1000);
        final AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.2f);
        animation2.setDuration(1000);
        //animation1 AnimationListener
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation2 when animation1 ends (continue)
                splash.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        //animation2 AnimationListener
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                splash.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        splash.startAnimation(animation1);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
