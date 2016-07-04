package com.app.sample.chatting.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title: Ereg
 * Description: 正则表达式
 * author:  赖创文
 * date:   2016/4/25 15:57
 */
public class Ereg {
    /**
     * 判断是不是邮箱
     * @param userName
     * @return
     */
    public boolean emailAstrict(String userName) {
        String regEx = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(userName);
        return matcher.matches();
    }

    /**
     * 判断是不是字母开头 中间可以是字母数字_ 限制数字6-20位
     * @param userPassword
     * @return
     */
    public boolean passwordAstrict(String userPassword) {
        String regEx = "^[a-zA-Z]\\w{5,17}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(userPassword);
        return matcher.matches();
    }



}
