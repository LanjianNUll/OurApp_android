package com.example.bean;

import java.util.Date;

public class ChatMessage {

	private int ChatMessageId;
	//������
	private int toUserId;
	//������
	private int fromUserId;
	//������name
	private String toUserName;
	//������name
	private String fromUserName;
	//��Ϣ����
	private String chatMsgContent;
	//������Ϣʱ��
	private Date sentMsgTime;
	public int getChatMessageId() {
		return ChatMessageId;
	}
	public void setChatMessageId(int chatMessageId) {
		ChatMessageId = chatMessageId;
	}
	public int getToUserId() {
		return toUserId;
	}
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	public int getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getChatMsgContent() {
		return chatMsgContent;
	}
	public void setChatMsgContent(String chatMsgContent) {
		this.chatMsgContent = chatMsgContent;
	}
	public Date getSentMsgTime() {
		return sentMsgTime;
	}
	public void setSentMsgTime(Date sentMsgTime) {
		this.sentMsgTime = sentMsgTime;
	}
}
