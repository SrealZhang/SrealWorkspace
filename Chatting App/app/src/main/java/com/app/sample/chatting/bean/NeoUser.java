package com.app.sample.chatting.bean;

/**
 * Created by neo2 on 2016/7/7.
 */
public class NeoUser {
    private String user;
    private String password;

    public NeoUser(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
