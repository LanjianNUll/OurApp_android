package com.example.bean;

import java.util.Date;

public class SportPlaceComment {
	
	//���۵�id,����
	private int SPCommentId;
	//�û���Id
	private int userId;
	//�û�������
	private String UserName;
	//����ʱ�� 
	private Date commentTime;
	//���۵ĳ���Id
	private int sportPlaceId;
	//���۸������Ǽ�
	private float startRange;
	//����
	private String commentComtent;
	public int getSPCommentId() {
		return SPCommentId;
	}
	public void setSPCommentId(int sPCommentId) {
		SPCommentId = sPCommentId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public Date getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}
	public int getSportPlaceId() {
		return sportPlaceId;
	}
	public void setSportPlaceId(int sportPlaceId) {
		this.sportPlaceId = sportPlaceId;
	}
	public String getCommentComtent() {
		return commentComtent;
	}
	public void setCommentComtent(String commentComtent) {
		this.commentComtent = commentComtent;
	}
	public float getStartRange() {
		return startRange;
	}
	public void setStartRange(float startRange) {
		this.startRange = startRange;
	}
}
