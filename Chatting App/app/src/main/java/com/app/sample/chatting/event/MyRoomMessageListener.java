package com.app.sample.chatting.event;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by Yangbin on 2015/12/25.
 */
public class MyRoomMessageListener implements MessageListener {
    protected static final String TAG = "yangbinMyRoomMessageListener";

    @Override
    public void processMessage(Message message) {
        //与单聊接收处理消息类似，聊天室里所有人(包括发送人自己)发送的消息都会通过此方法进行回调处理
        Log.d(TAG + "您收到消息", message.toString());
        if (TextUtils.isEmpty(message.getBody())) return;
        String bodyJson = "";
        bodyJson = message.getBody();
        Log.d(TAG, bodyJson);
    }
}
