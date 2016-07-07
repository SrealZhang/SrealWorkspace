package com.app.sample.chatting.event;

/**
 * Created by Yangbin on 2016/3/15.
 */
public class Event_SureShowNum {
    private boolean successful;

    public Event_SureShowNum(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
