package com.example.bean;

import java.io.Serializable;

public class User  implements Serializable{
	//性别
	public static int sex_man = 1;
	public static int sex_women = 2; 
	public static int sex_define = 0;
	//user_state
	public static int state_运动狂 = 10;
	public static int state_运动达人 = 11;
	public static int state_运动挚友 = 12;
	public static int state_活跃 = 13;
	public static int state_冒泡 = 14;
	public static int state_宅客 = 15;

	private int userId;
	private String username;
	private int sexId;
	private String password;
	private int user_state;
	private String my_user_sign;
	private String location_lasetime_login;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getSexId() {
		return sexId;
	}
	public void setSexId(int sexId) {
		this.sexId = sexId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUser_state() {
		return user_state;
	}
	public void setUser_state(int user_state) {
		this.user_state = user_state;
	}
	public String getMy_user_sign() {
		return my_user_sign;
	}
	public void setMy_user_sign(String my_user_sign) {
		this.my_user_sign = my_user_sign;
	}
	public String getLocation_lasetime_login() {
		return location_lasetime_login;
	}
	public void setLocation_lasetime_login(String location_lasetime_login) {
		this.location_lasetime_login = location_lasetime_login;
	}
	
	
	
	
}
