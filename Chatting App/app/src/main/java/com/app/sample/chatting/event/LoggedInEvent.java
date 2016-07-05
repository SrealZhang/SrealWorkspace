package com.app.sample.chatting.event;

/*
* Used for eventbus callback purposes
* */
public class LoggedInEvent {

    private boolean successful;

    private String errorInfo;

    public LoggedInEvent(boolean successful) {
        this.successful = successful;
    }

    public LoggedInEvent(boolean successful, String errorInfo) {
        this.successful = successful;
        this.errorInfo = errorInfo;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
