package com.app.sample.chatting.event;

import android.app.Notification;
import android.app.NotificationManager;
import android.text.TextUtils;
import android.util.Log;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

public class MyChatMessageListener implements ChatMessageListener {

    protected static final String TAG = "nilaiMyChatMessageListener";

    @Override
    public void processMessage(Chat chat, Message message) {
        if (TextUtils.isEmpty(message.getBody())) return;
        String bodyJson = "";
        bodyJson = message.getBody();
        Log.d(TAG, bodyJson);
    }

}
