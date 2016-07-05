package com.app.sample.chatting.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.sample.chatting.ActivityLogin;
import com.app.sample.chatting.R;
import com.app.sample.chatting.util.Ereg;


/**
 * Title: LoginFragment
 * Description: (这里用一句话描述这个类的作用)
 * author:  赖创文
 * date:   2016/3/2 15:46
 */
public class RegisterFragment extends android.support.v4.app.Fragment implements View.OnClickListener, TextWatcher {
    private ImageButton register_head_iv;
    private TextView register_head_tv1;
    private Button register_submit;//注册提交按钮
    private EditText register_username, register_password1, register_password2;//输入用户名，密码，确定密码
    private Ereg ereg = new Ereg();//正则表达式工具类

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        findViewById(view);
        return view;
    }

    /**
     * 查找控件
     *
     * @param view
     */
    private void findViewById(View view) {
        register_head_iv = (ImageButton) view.findViewById(R.id.head_iv);
        register_head_tv1 = (TextView) view.findViewById(R.id.head_tv1);
        register_submit = (Button) view.findViewById(R.id.register_submit);
        register_username = (EditText) view.findViewById(R.id.register_username);
        register_password1 = (EditText) view.findViewById(R.id.register_password1);
        register_password2 = (EditText) view.findViewById(R.id.register_password2);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        setListener();
    }

    /**
     * 设置监听事件
     */
    private void setListener() {
        register_head_iv.setOnClickListener(this);
        register_submit.setOnClickListener(this);
        register_username.addTextChangedListener(this);
        register_password1.addTextChangedListener(this);
        register_password2.addTextChangedListener(this);


    }

    /**
     * 初始化数据
     */
    private void init() {
        register_head_tv1.setText("注册");//改变标题
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_submit:
                break;
            case R.id.head_iv:
                ((ActivityLogin) getActivity()).applyRotation(true, new LoginFragment(), 0, 90);
                ((ActivityLogin) getActivity()).isRegist = false;
                break;
            default:
                break;
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
   /*     //判断账号是否是邮箱
        if(ereg.emailAstrict(register_username.getText().toString())){
            register_username.setTextColor(getResources().getColor(R.color.black));
        }else {
            register_username.setTextColor(getResources().getColor(R.color.red));
        }
        //判断密码是不是以字母开头的6-20数据
          if(ereg.passwordAstrict(register_password1.getText().toString())){
              register_password1.setTextColor(getResources().getColor(R.color.black));
          }else {
              register_password1.setTextColor(getResources().getColor(R.color.red));

          }
        //判断密码是不是以字母开头的6-20数据
        if(ereg.passwordAstrict(register_password2.getText().toString())){
            register_password2.setTextColor(getResources().getColor(R.color.black));
        }else {
            register_password2.setTextColor(getResources().getColor(R.color.red));

        }
        //两个密码是否一样
        if(register_password2.getText().toString().equals(register_password1.getText().toString())){
            register_password2.setTextColor(getResources().getColor(R.color.black));
        }else {
            register_password2.setTextColor(getResources().getColor(R.color.red));

        }
*/

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
