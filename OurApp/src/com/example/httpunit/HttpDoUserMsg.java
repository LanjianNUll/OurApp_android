package com.example.httpunit;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.bean.User;
import com.example.bean.UserDetailInfo;

public class HttpDoUserMsg {

	//ע���û���
	public int sentUserDetailInfo(UserDetailInfo UDInfo) {
		// ����   userDetailInfo  
		//����ֵ
		// 0 ע��ʧ��   1ע��ɹ�  2�û����Ѵ��� 3�û���������Ϊ��
		if(UDInfo.getUsername().equals("")||UDInfo.getPassWord().equals(""))
			return 3;
		return 1;
	}

	public int checkUserIsRight(String userName, String passWord) {
		
		//��֤�û����Ƿ���ȷ
		//���� userName     passWord
		//���صĲ���
		//1 Ϊ��¼�ɹ�    0��¼ʧ��   -1 �û�������  -2 �û�������� -3Ϊ�û������������
		//�ж��Ƿ�Ϸ��ַ�
		if(userName.equals("") || passWord.equals(""))
			return -3;
		return 1;
	}

	public int addFriend(Integer cuUserId, Integer UserId) {
		
		// ����    ��ǰ�û�Id    cuUserId 
		//     ���friendId  cuUserId
		return 1;
	}

	public UserDetailInfo getUserDetail(int userId) {
		//��ȡ�û�����
		//����  userId
		return null;
	}

	public List<User> getMyFriend_Group(int userId) {
		// ��ȡ�û��ĺ����б�    
		//����  userid
		List<User> s = new ArrayList<User>();
		for(int i = 0; i<1; i++){
			User d = new User();
			d.setUserId(5);
			d.setMy_user_sign("�˶���������һ������Ȧ");
			d.setUsername("�˶�Ȧ");
			s.add(d);
		}		
		return s;
	}

	public void afterDeteFriendGroup(List<User> tempMyFriendGroup) {
		//���е�userID
		if(tempMyFriendGroup!=null)
			for (User user : tempMyFriendGroup) {
				Log.v("�ֺ�����",""+user.getUserId());
			}
		
	}
}
