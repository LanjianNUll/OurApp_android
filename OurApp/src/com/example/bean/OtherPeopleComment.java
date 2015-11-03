package com.example.bean;

import java.util.Date;

public class OtherPeopleComment {

	//评论的id,主键
	private int OPCommentId;
	//用户的Id
	private int userId;
	//用户的姓名
	private String UserName;
	//用户的状态
	private int userState;
	//评论时间 
	private Date commentTime;
	//评论的所属的评论详情id
	private int commentDetailInfoId;
	//评论
	private String commentComtent;
	public int getOPCommentId() {
		return OPCommentId;
	}
	public void setOPCommentId(int oPCommentId) {
		OPCommentId = oPCommentId;
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
	public int getUserState() {
		return userState;
	}
	public void setUserState(int userState) {
		this.userState = userState;
	}
	public Date getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}
	public int getCommentDetailInfoId() {
		return commentDetailInfoId;
	}
	public void setCommentDetailInfoId(int commentDetailInfoId) {
		this.commentDetailInfoId = commentDetailInfoId;
	}
	public String getCommentComtent() {
		return commentComtent;
	}
	public void setCommentComtent(String commentComtent) {
		this.commentComtent = commentComtent;
	}
}
