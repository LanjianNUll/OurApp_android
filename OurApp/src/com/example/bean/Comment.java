package com.example.bean;

import java.util.Date;

public class Comment {
	//comment type
	public static int comment_type_挑战书 = 0;
	public static int comment_type_发现  = 1;
	public static int comment_type_邀请函 = 2;
	public static int comment_type_求人带= 3;
	//评论ID
	private int comment_id;
	//评论发起者
	private String comment_from_user_name;
	//评论者id
	private int comment_from_user_id;
	//评论者的状态，即活跃程度
	private int user_state;
	//评论发起的时间
	private Date  comment_from_time;
	//评论内容
	private String comment_content;
	//多少人看过
	private int how_many_people_see;
	//多少人评论
	private int how_many_people_comment;
	//多少人赞
	private int how_many_people_praise;
	//comment type
	private int comment_type;
	
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public String getComment_from_user_name() {
		return comment_from_user_name;
	}
	public void setComment_from_user_name(String comment_from_user_name) {
		this.comment_from_user_name = comment_from_user_name;
	}
	public int getComment_from_user_id() {
		return comment_from_user_id;
	}
	public void setComment_from_user_id(int comment_from_user_id) {
		this.comment_from_user_id = comment_from_user_id;
	}
	public int getUser_state() {
		return user_state;
	}
	public void setUser_state(int user_state) {
		this.user_state = user_state;
	}
	public Date getComment_from_time() {
		return comment_from_time;
	}
	public void setComment_from_time(Date comment_from_time) {
		this.comment_from_time = comment_from_time;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public int getHow_many_people_see() {
		return how_many_people_see;
	}
	public void setHow_many_people_see(int how_many_people_see) {
		this.how_many_people_see = how_many_people_see;
	}
	public int getHow_many_people_comment() {
		return how_many_people_comment;
	}
	public void setHow_many_people_comment(int how_many_people_comment) {
		this.how_many_people_comment = how_many_people_comment;
	}
	public int getHow_many_people_praise() {
		return how_many_people_praise;
	}
	public void setHow_many_people_praise(int how_many_people_praise) {
		this.how_many_people_praise = how_many_people_praise;
	}
	public int getComment_type() {
		return comment_type;
	}
	public void setComment_type(int comment_type) {
		this.comment_type = comment_type;
	}	
}
