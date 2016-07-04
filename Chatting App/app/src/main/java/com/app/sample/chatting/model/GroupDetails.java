package com.app.sample.chatting.model;

import java.io.Serializable;

public class GroupDetails implements Serializable{
	private long id;
	private String date;
	private Friend friend;
	private String content;
	private boolean fromMe;

	public GroupDetails(long id, String date, Friend friend, String content, boolean fromMe) {
		this.id = id;
		this.date = date;
		this.friend = friend;
		this.content = content;
		this.fromMe = fromMe;
	}

	public long getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public Friend getFriend() {
		return friend;
	}

	public String getContent() {
		return content;
	}

	public boolean isFromMe() {
		return fromMe;
	}
}