package com.example.bean;

import java.util.Date;

public class UserDetailInfo {
		//性别
		private static int sex_man = 1;
		private static int sex_women = 2; 
		private static int sex_define = 0;
		//user_state
		public static int state_运动狂 = 10;
		public static int state_运动达人 = 11;
		public static int state_运动挚友 = 12;
		public static int state_活跃 = 13;
		public static int state_冒泡 = 14;
		public static int state_宅客 = 15;
		
		private int userId;
		private String username;
		private String passWord;
		private int sexId;
		//用户的状态
		private int user_state;
		private String my_user_sign;
		private String location_lasetime_login;
		//其他用户的留言（留言板）
		private LeaveWord leaveword[];
		//用户注册时间
		private Date registeTime;
		//用的联系方式
		private String connectPhone;
		//好友列表
		private int[] userFriendId;
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
		public LeaveWord[] getLeaveword() {
			return leaveword;
		}
		public void setLeaveword(LeaveWord[] leaveword) {
			this.leaveword = leaveword;
		}
		public Date getRegisteTime() {
			return registeTime;
		}
		public void setRegisteTime(Date registeTime) {
			this.registeTime = registeTime;
		}
		public String getConnectPhone() {
			return connectPhone;
		}
		public void setConnectPhone(String connectPhone) {
			this.connectPhone = connectPhone;
		}
		public int[] getUserFriendId() {
			return userFriendId;
		}
		public void setUserFriendId(int[] userFriendId) {
			this.userFriendId = userFriendId;
		}
		public String getPassWord() {
			return passWord;
		}
		public void setPassWord(String passWord) {
			this.passWord = passWord;
		}
		
		
		
}
