package com.app.sample.chatting.event.chat;

import org.jivesoftware.smack.packet.Message;

import greendao.NeoChatHistory;

/**
 * Created by Yangbin on 2015/12/24.
 */
public class ChatPersonMessageEvent {

    private Message message;
    NeoChatHistory neoChatHistory;


    public ChatPersonMessageEvent(Message message, NeoChatHistory neoChatHistory) {
        this.message = message;
        this.neoChatHistory = neoChatHistory;
    }

    public NeoChatHistory getNeoChatHistory() {
        return neoChatHistory;
    }

    public Message getMessage() {
        return message;
    }

}
