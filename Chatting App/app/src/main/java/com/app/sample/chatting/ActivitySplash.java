package com.app.sample.chatting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sample.chatting.data.Constant;
import com.app.sample.chatting.data.Tools;

import java.util.Timer;
import java.util.TimerTask;

public class ActivitySplash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bindLogo();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // go to the main activity
                Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
                startActivity(i);
                // kill current activity
                finish();
            }
        };
        // Show splash screen for 3 seconds
        new Timer().schedule(task, 3000);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void bindLogo(){
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
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationStart(Animation arg0) {}
        });

        //animation2 AnimationListener
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                splash.startAnimation(animation1);
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationStart(Animation arg0) {}
        });

        splash.startAnimation(animation1);
    }
}
