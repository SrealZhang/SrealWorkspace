package com.app.sample.chatting.event;

/**
 * Created by neo2 on 2016/7/7.
 */
public class Event_SureChange {
    private boolean successful;

    public Event_SureChange(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}