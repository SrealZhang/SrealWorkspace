package com.app.sample.chatting.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.sample.chatting.R;
import com.app.sample.chatting.widget.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Title: LoginFragment
 * Description: (这里用一句话描述这个类的作用)
 * author:  赖创文
 * date:   2016/3/2 15:46
 */
public class LoginFragment extends Fragment implements TextWatcher {


    @BindView(R.id.head_iv)
    ImageButton headIv;
    @BindView(R.id.head_tv1)
    TextView headTv1;
    @BindView(R.id.head_tv2)
    TextView headTv2;
    @BindView(R.id.login_username)
    ClearEditText loginUsername;
    @BindView(R.id.login_password)
    ClearEditText loginPassword;
    @BindView(R.id.login_submit)
    Button loginSubmit;
    @BindView(R.id.login_forgot_password)
    TextView loginForgotPassword;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        setListener();
    }

    /**
     * 监听事件
     */
    private void setListener() {
    }

    /**
     * 初始化
     */
    private void init() {

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    /*    //判断账号是否是邮箱
        if(ereg.emailAstrict(login_username.getText().toString())){
            login_username.setTextColor(getResources().getColor(R.color.black));
        }else {
            login_username.setTextColor(getResources().getColor(R.color.red));
        }*/


    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
