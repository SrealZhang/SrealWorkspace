package com.app.sample.chatting.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.sample.chatting.ActivityLogin;
import com.app.sample.chatting.ActivityMain;
import com.app.sample.chatting.MyApplication;
import com.app.sample.chatting.R;
import com.app.sample.chatting.data.Constant;
import com.app.sample.chatting.event.LoggedInEvent;
import com.app.sample.chatting.service.IMContactServiceHelper;
import com.app.sample.chatting.util.SaveUtil;
import com.app.sample.chatting.widget.ClearEditText;
import com.app.sample.chatting.widget.TextURLView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import greendao.NeoUser;


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
        List<NeoUser> user = SaveUtil.selectUser();
        if (user != null && user.size() > 0) {
            edtAccount.setText(user.get(0).getName());
            edtPassword.setText(user.get(0).getPassword());
        }
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
        ((ActivityLogin) getActivity()).RequestFocus(null);
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

    //path:/storage/sdcard1/DCIM/Camera/IMG_20160702_085019.jpg
    //登录返回事件
    public void onEventMainThread(LoggedInEvent event) {
        if (event.isSuccessful()) {
            Log.d(TAG, "Successful login---回调成功");
            startActivity(new Intent(getActivity(), ActivityMain.class));
            getActivity().finish();

//            Drawable bitmapDrawable = new BitmapDrawable(IMContactServiceHelper.getmInstance().getUserImage());
//            Log.d("yangbin", bitmapDrawable + "");
//            ivLoginPicture.setBackgroundDrawable(bitmapDrawable);
//            String path = "/storage/sdcard1/com.xxAssistant/users/yy1250211588/user_small_head_img.jpg";
//            try {
//                IMContactServiceHelper.getmInstance().changeImage(path);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


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

    /**
     * 打开相册
     */
    public void openAlbum() {
        Intent takePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {// 相机被卸载时不会崩溃
            startActivityForResult(takePictureIntent, 2);
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
        Log.d(TAG, "onStart");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.d(TAG, "onStop");
        super.onStop();
    }

}
