package com.app.sample.chatting.event;

/**
 * Created by neo2 on 2016/7/7.
 */
public class Event_FriendUpdate {
    private boolean successful;

    public Event_FriendUpdate(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
