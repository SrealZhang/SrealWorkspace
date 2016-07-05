package com.app.sample.chatting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.app.sample.chatting.fragment.LoginFragment;
import com.app.sample.chatting.fragment.RegisterFragment;
import com.app.sample.chatting.util.UtilRoat3D;


/**
 * Description: 登录acticity 用于装fragment
 * author:  赖创文
 * FragmentActivity 3.0以前要的，否则找不到getSupportFragmentManager();
 * date:   2016/3/2 15:17
 */
public class ActivityLogin extends BaseActivity {
    public boolean isRegist = false;
    FrameLayout fragment_login_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        LoginFragment mLoginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_login_content, mLoginFragment).commit();
    }

    private void findViewById() {
        //获取布局
        fragment_login_content = (FrameLayout) findViewById(R.id.fragment_login_content);
    }


    public void applyRotation(final boolean zheng, final Fragment fragment,
                              final float start, final float end) {
        // Find the center of the container
        final float centerX = fragment_login_content.getWidth() / 2.0f;
        final float centerY = fragment_login_content.getHeight() / 2.0f;
        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final UtilRoat3D rotation = new UtilRoat3D(
                start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(300);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(zheng, fragment));// 添加监听执行现实内容的切换
        fragment_login_content.startAnimation(rotation);// 执行上半场翻转动画
    }

    private final class DisplayNextView implements Animation.AnimationListener {
        private final boolean mPosition;
        private final Fragment mfragment;

        private DisplayNextView(boolean zheng, Fragment fragment) {
            mPosition = zheng;
            mfragment = fragment;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            fragment_login_content.post(new SwapViews(mPosition, mfragment));// 添加新的View
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private final class SwapViews implements Runnable {
        private final boolean mPosition;
        private final Fragment mfragment;

        public SwapViews(boolean position, Fragment fragment) {
            mPosition = position;
            mfragment = fragment;
        }

        public void run() {
            final float centerX = fragment_login_content.getWidth() / 2.0f;
            final float centerY = fragment_login_content.getHeight() / 2.0f;
            UtilRoat3D rotation;
            FragmentTransaction tration = getSupportFragmentManager()
                    .beginTransaction();
            tration.replace(R.id.fragment_login_content, mfragment);
            if (mPosition) {
                rotation = new UtilRoat3D(-90, 0, centerX, centerY,
                        310.0f, false);
            } else {
                rotation = new UtilRoat3D(90, 0, centerX, centerY,
                        310.0f, false);
            }
            tration.commit();
            rotation.setDuration(300);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            fragment_login_content.startAnimation(rotation);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return true;
    }

    //退出
    private void exit() {
        if (isRegist) {
            applyRotation(true, new LoginFragment(), 0, 90);
            isRegist = false;
        } else {
            finish();
        }
    }

}


