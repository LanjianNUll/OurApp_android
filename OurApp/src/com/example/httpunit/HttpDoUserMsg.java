package com.example.httpunit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.example.bean.UserDetailInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HttpDoUserMsg {

    private static final int REQUEST_TIMEOUT = 5*1000;//��������ʱ10����  
    private static final int SO_TIMEOUT = 5*1000;  //���õȴ����ݳ�ʱʱ��10����
    
  //����httpCilent 
  	 public HttpClient getHttpClient(){  
  		    BasicHttpParams httpParams = new BasicHttpParams();  
  		    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
  		    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
  		    HttpClient client = new DefaultHttpClient(httpParams);  
  		    return client;  
  		}  

	//ע���û���
	public int sentUserDetailInfo(UserDetailInfo UDInfo) {
		// ����   UserResigterUserDeinfo  
		//����ֵ
		// 0 ע��ʧ��   1ע��ɹ�  2�û����Ѵ���
		String url = "http://xiafucheng.6655.la:20128/webAdroid/server/registered";
		
		//String url = "http://xiafucheng.duapp.com/webAdroid/server/registered";
		String postStr = null;
		try {
			postStr = new Gson().toJson(UDInfo);
		} catch (Exception e) {
			// TODO: handle exception
		}
		String result = getInternetConnectAndGetDatePost(url, postStr);
		int i;
		try {
			i = Integer.parseInt(result.trim());
			return i;
		} catch (Exception e) {
			return 0;
		}	
	}
	
	//��֤�û���¼��Ϣ
	public int checkUserIsRight(String userName, String passWord){
		
		//��֤�û����Ƿ���ȷ
		//���� userName     passWord
		//���صĲ���
		//1 Ϊ��¼�ɹ�    0��¼ʧ��   -1 �û�������  -2 �û��������

//		String userN = null;
//		String passW = null;
//		try {
//			userN = URLEncoder.encode(userName, "UTF-8");
//			passW = URLEncoder.encode(userName, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//	}
		String url = "http://xiafucheng.duapp.com/webAdroid/server/login?"+
				"userName="+URLEncoder.encode(userName)+
				"&passWord="+URLEncoder.encode(passWord);
		String result = getInternetConnectAndGetDateGet(url);
		int i;
		try {
			i = Integer.parseInt(result.trim());
			return i;
		} catch (Exception e) {
			return 0;
		}		
	}

	public int addFriend(Integer cuUserId, Integer UserId) {
		
		// ����    ��ǰ�û�Id    cuUserId 
		//     ���friendId  cuUserId
		//������userId,friendId  ������ :(��ӳɹ� 1 ���ʧ�� 0  �����Ѿ�����-1)
		
		//http://xiafucheng.duapp.com/webAdroid/server/addFriends
		String url = "http://xiafucheng.duapp.com/webAdroid/server/addFriends?"
					+"userId="+cuUserId
					+"&friendId="+UserId;
		String result = getInternetConnectAndGetDateGet(url);
		int returncode ;
		try {
			returncode = Integer.parseInt(result);
			return returncode;
		} catch (Exception e) {
			return 0;
		}
		
	}

	public UserDetailInfo getUserDetail(int userId) {
		//��ȡ�û�����
		//����  userId
		//String url = "http://xiafucheng.duapp.com/webAdroid/server/getUserDetail";
		String url = "http://xiafucheng.duapp.com/webAdroid/server/getUserDetail?userId="
				+userId;
		Log.e("55555555555555555", userId+"");
		String result = getInternetConnectAndGetDateGet(url);
		try {
			UserDetailInfo de = new Gson().fromJson(result, UserDetailInfo.class);
			return de;
		} catch (Exception e) {
			return null;
		}
	}

	public List<UserDetailInfo> getMyFriend_Group(int userId) {
		// ��ȡ�û��ĺ����б�    
		//����  userid
		List<UserDetailInfo> s = new ArrayList<UserDetailInfo>();
		String url = "http://xiafucheng.duapp.com/webAdroid/server/getUserFriends?userId="+userId;
		String userDetailInfoStr = getInternetConnectAndGetDateGet(url);	
		try {
			s = new Gson().fromJson(userDetailInfoStr,
					new TypeToken<ArrayList<UserDetailInfo>>() {}.getType());
			return s;
		} catch (Exception e) {
			return null;
		}
	}
	public void afterDeteFriendGroup(List<UserDetailInfo> tempMyFriendGroup) {
		//���е�userID
		if(tempMyFriendGroup!=null)
			for (UserDetailInfo user : tempMyFriendGroup) {
				Log.v("�ֺ�����",""+user.getUserId());
			}
	}
	//�û�ǩ��
	public Integer userSign(Integer userId) {
		//http://xiafucheng.6655.la:20128/webAdroid/server/addSignIn
		String url = "http://xiafucheng.duapp.com/webAdroid/server/addSignIn?userId="+userId;
		String result = getInternetConnectAndGetDateGet(url);
		
		int i = 0;
		try {
			i = Integer.parseInt(result);
			return i;
		} catch (Exception e) {
			return 0;
		}
	}
	//ɾ�����ѵ���������
		public Integer deleteFriendHttp(Integer userId, Integer friendId) {
			String url = "http://xiafucheng.duapp.com/webAdroid/server/deleteFriend"+
					"?userId="+userId+
					"&friendId="+friendId;
			String result = getInternetConnectAndGetDateGet(url);
			Log.e("111111115",result);
			int i = 0;
			try {
				i = Integer.parseInt(result);
				return i;
			} catch (Exception e) {
				return 0;
			}
		}
	//get�ķ�ʽ�ύ
	private String getInternetConnectAndGetDateGet(String url){
		HttpGet httpRequest = new HttpGet(url); 
        HttpClient httpClient = getHttpClient();
        HttpResponse httpResponse = null;
        String strResult = null;
		try {
			httpResponse = httpClient.execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }else{
            	System.out.println("��ӡ"+httpResponse.getStatusLine().getStatusCode()+strResult);
            }
            } catch (Exception e) {
			e.printStackTrace();
		}
		//Log.e("1111111111",strResult);
		return strResult;
	}
	//post�ķ�ʽ�ύ
	private String getInternetConnectAndGetDatePost(String url, String postStr) {
		
		 HttpClient httpClient = getHttpClient();
		 HttpPost httpPost = new HttpPost(url);
		 HttpResponse httpResponse = null;
	     String strResult = null;
	        try {
	            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	            nameValuePair.add(new BasicNameValuePair("UserResigterUserDeinfo", postStr));
	            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
	            System.out.println(postStr);
	            httpResponse = httpClient.execute(httpPost);
	            strResult = EntityUtils.toString(httpResponse.getEntity());
	        }catch(Exception e){
	     }
		return strResult;
	}
	

}
