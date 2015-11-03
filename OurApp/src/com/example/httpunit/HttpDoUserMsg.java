package com.example.httpunit;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.bean.User;
import com.example.bean.UserDetailInfo;

public class HttpDoUserMsg {

	//注册用户的
	public int sentUserDetailInfo(UserDetailInfo UDInfo) {
		// 参数   userDetailInfo  
		//返回值
		// 0 注册失败   1注册成功  2用户名已存在 3用户名和密码为空
		if(UDInfo.getUsername().equals("")||UDInfo.getPassWord().equals(""))
			return 3;
		return 1;
	}

	public int checkUserIsRight(String userName, String passWord) {
		
		//验证用户名是否正确
		//参数 userName     passWord
		//返回的参数
		//1 为登录成功    0登录失败   -1 用户名错误  -2 用户密码错误 -3为用户名或密码错误
		//判断是否合法字符
		if(userName.equals("") || passWord.equals(""))
			return -3;
		return 1;
	}

	public int addFriend(Integer cuUserId, Integer UserId) {
		
		// 参数    当前用户Id    cuUserId 
		//     添加friendId  cuUserId
		return 1;
	}

	public UserDetailInfo getUserDetail(int userId) {
		//获取用户详情
		//参数  userId
		return null;
	}

	public List<User> getMyFriend_Group(int userId) {
		// 获取用户的好友列表    
		//参数  userid
		List<User> s = new ArrayList<User>();
		for(int i = 0; i<1; i++){
			User d = new User();
			d.setUserId(5);
			d.setMy_user_sign("运动让世界变成一个朋友圈");
			d.setUsername("运动圈");
			s.add(d);
		}		
		return s;
	}

	public void afterDeteFriendGroup(List<User> tempMyFriendGroup) {
		//现有的userID
		if(tempMyFriendGroup!=null)
			for (User user : tempMyFriendGroup) {
				Log.v("现好友数",""+user.getUserId());
			}
		
	}
}
