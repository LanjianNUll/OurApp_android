package com.example.bean;

import java.util.Date;

public class OtherPeopleComment {

	//���۵�id,����
	private int OPCommentId;
	//�û���Id
	private int userId;
	//�û�������
	private String UserName;
	//�û���״̬
	private int userState;
	//����ʱ�� 
	private Date commentTime;
	//���۵���������������id
	private int commentDetailInfoId;
	//����
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
