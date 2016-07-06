package com.app.sample.chatting.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.sample.chatting.ActivityLogin;
import com.app.sample.chatting.ActivityMain;
import com.app.sample.chatting.MyApplication;
import com.app.sample.chatting.R;
import com.app.sample.chatting.event.LoggedInEvent;
import com.app.sample.chatting.service.IMContactServiceHelper;
import com.app.sample.chatting.widget.ClearEditText;

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
public class RegisterFragment extends Fragment {

    private static final String TAG = "nilaiRegisterFragment";
    @BindView(R.id.ibtn_back)
    ImageButton ibtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edt_username)
    ClearEditText edtUsername;
    @BindView(R.id.edt_password1)
    ClearEditText edtPassword1;
    @BindView(R.id.edt_password2)
    ClearEditText edtPassword2;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        tvTitle.setText("注册");//改变标题
        return view;
    }

    @OnClick({R.id.btn_submit, R.id.ibtn_back})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                clickRegist();
                break;
            case R.id.ibtn_back:
                ((ActivityLogin) getActivity()).applyRotation(true, new LoginFragment(), 0, 90);
                ((ActivityLogin) getActivity()).isRegist = false;
                break;
            default:
                break;
        }

    }

    //注册
    public void clickRegist() {
        ((ActivityLogin) getActivity()).RequestFocus(null);
        if (TextUtils.isEmpty(edtUsername.getText())) {
            edtUsername.setError("输入账号");
            return;
        }
        if (TextUtils.isEmpty(edtPassword1.getText())) {
            edtPassword1.setError("输入密码");
            return;
        }
        if (TextUtils.isEmpty(edtPassword2.getText())) {
            edtPassword2.setError("输入密码");
            return;
        }
        if (!edtPassword2.getText().toString().equals(edtPassword1.getText().toString())) {
            MyApplication.showToast("两次输入密码不一致");
            edtPassword2.setError("输入密码不一致");
            return;
        }
        try {
            btnSubmit.setText("正在注册...");
            IMContactServiceHelper.getmInstance().loginorRegist(getActivity(), edtUsername.getText().toString(), edtPassword1.getText().toString(), 1);
            btnSubmit.setClickable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //注册返回事件
    public void onEventMainThread(LoggedInEvent event) {
        if (event.isSuccessful()) {
            Log.d(TAG, "Successful ---注册成功");
            ((ActivityLogin) getActivity()).applyRotation(true, new LoginFragment(), 0, 90);
            ((ActivityLogin) getActivity()).isRegist = false;
            MyApplication.showToast(event.getErrorInfo());
        } else {
            Log.d(TAG, "Successful ---注册失败");
            btnSubmit.setClickable(true);
            btnSubmit.setText("注册");
            if (event.getErrorInfo() != null) {
                MyApplication.showToast(event.getErrorInfo());
            } else {
                MyApplication.showToast("聊天服务器注册失败");
                Log.d(TAG, "聊天服务器注册失败");
            }
        }
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
