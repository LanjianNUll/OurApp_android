package com.example.bean;

import java.util.Date;

public class SportPlaceComment {
	
	//评论的id,主键
	private int SPCommentId;
	//用户的Id
	private int userId;
	//用户的姓名
	private String UserName;
	//评论时间 
	private Date commentTime;
	//评论的场地Id
	private int sportPlaceId;
	//评论给出的星级
	private float startRange;
	//评论
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
