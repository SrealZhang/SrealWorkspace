package com.app.sample.chatting.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.sample.chatting.ActivityLogin;
import com.app.sample.chatting.ActivityMain;
import com.app.sample.chatting.MyApplication;
import com.app.sample.chatting.R;
import com.app.sample.chatting.event.LoggedInEvent;
import com.app.sample.chatting.service.IMContactServiceHelper;
import com.app.sample.chatting.widget.ClearEditText;
import com.app.sample.chatting.widget.TextURLView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


/**
 * Title: LoginFragment
 * Description: (这里用一句话描述这个类的作用)
 * author:  赖创文
 * date:   2016/3/2 15:46
 */

public class LoginFragment extends Fragment {
    private final String TAG = "NILAILoginFragment";
    @BindView(R.id.iv_login_picture)
    ImageView ivLoginPicture;
    @BindView(R.id.edt_account)
    ClearEditText edtAccount;
    @BindView(R.id.edt_password)
    ClearEditText edtPassword;
    @BindView(R.id.ll_user_info)
    LinearLayout llUserInfo;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.rl_user)
    RelativeLayout rlUser;
    @BindView(R.id.tv_forget_password)
    TextURLView tvForgetPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        init();
        ButterKnife.bind(this, view);
        return view;
    }

    private void init() {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.login_anim);
        anim.setFillAfter(true);
        tvForgetPassword.setText(R.string.forget_password);
        rlUser.startAnimation(anim);
    }

    //登录
    @OnClick(R.id.btn_login)
    public void clickLogin(View view) {
        RequestFocus(null);
        if (TextUtils.isEmpty(edtAccount.getText())) {
            edtAccount.setError("输入账号");
            return;
        }
        if (TextUtils.isEmpty(edtPassword.getText())) {
            edtPassword.setError("输入密码");
            return;
        }
        try {
            btnLogin.setText("正在登陆...");
            IMContactServiceHelper.getmInstance().loginorRegist(getActivity(), edtAccount.getText().toString(), edtPassword.getText().toString(), 0);
            btnLogin.setClickable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //登录返回事件
    public void onEventMainThread(LoggedInEvent event) {
        if (event.isSuccessful()) {
            Log.d(TAG, "Successful login---回调成功");
            startActivity(new Intent(getActivity(), ActivityMain.class));
            getActivity().finish();
        } else {
            btnLogin.setClickable(true);
            btnLogin.setText("登陆");
            if (event.getErrorInfo() != null) {
                MyApplication.showToast(event.getErrorInfo());
            } else {
                MyApplication.showToast("聊天服务器验证失败");
                Log.d(TAG, "聊天服务器验证失败");
            }
        }
    }

    @OnClick(R.id.btn_register)
    public void clickRegist(View view) {
        ((ActivityLogin) getActivity()).applyRotation(true, new RegisterFragment(), 0, 90);
        ((ActivityLogin) getActivity()).isRegist = true;
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

    public void RequestFocus(View v) {
        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus != null) {
            Log.d(TAG, "currentFocus is not null");
            hideKeybard(currentFocus.getWindowToken());
            if (currentFocus.getWindowToken() != null) {
                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 隐藏输入法
     */
    protected void hideKeybard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
