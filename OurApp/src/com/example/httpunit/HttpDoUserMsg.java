package com.example.httpunit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.google.gson.Gson;

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
		//String url = "http://xiafucheng.duapp.com/webAdroid/server/registered";
		
		String url = "http://xiafucheng.6655.la:20128/webAdroid/server/registered";
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
		return 1;
	}

	public UserDetailInfo getUserDetail(int userId) {
		//��ȡ�û�����
		//����  userId
		String url = "http://xiafucheng.duapp.com/webAdroid/server/servlet01?" ;
		String result = getInternetConnectAndGetDateGet(url+userId);
		try {
			UserDetailInfo de = new Gson().fromJson(result, UserDetailInfo.class);
			return de;
		} catch (Exception e) {
			return null;
		}
		
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
