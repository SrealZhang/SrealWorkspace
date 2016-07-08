package com.app.sample.chatting.model;

import java.io.Serializable;

public class Friend implements Serializable {
    private long id;
    private String name;
    private String userId;
    private int msgNum;

    public Friend(long id, String name, String userId, int msgNum) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.msgNum = msgNum;
    }

    public Friend(long id, String name, String userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public Friend(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public int getMsgNum() {
        return msgNum;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }
}
