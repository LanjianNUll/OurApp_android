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

    private static final int REQUEST_TIMEOUT = 5*1000;//设置请求超时10秒钟  
    private static final int SO_TIMEOUT = 5*1000;  //设置等待数据超时时间10秒钟
    
  //设置httpCilent 
  	 public HttpClient getHttpClient(){  
  		    BasicHttpParams httpParams = new BasicHttpParams();  
  		    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
  		    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
  		    HttpClient client = new DefaultHttpClient(httpParams);  
  		    return client;  
  		}  

	//注册用户的
	public int sentUserDetailInfo(UserDetailInfo UDInfo) {
		// 参数   UserResigterUserDeinfo  
		//返回值
		// 0 注册失败   1注册成功  2用户名已存在
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
	
	//验证用户登录信息
	public int checkUserIsRight(String userName, String passWord){
		
		//验证用户名是否正确
		//参数 userName     passWord
		//返回的参数
		//1 为登录成功    0登录失败   -1 用户名错误  -2 用户密码错误

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
		
		// 参数    当前用户Id    cuUserId 
		//     添加friendId  cuUserId
		//参数：userId,friendId  返回码 :(添加成功 1 添加失败 0  好友已经存在-1)
		
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
		//获取用户详情
		//参数  userId
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
		// 获取用户的好友列表    
		//参数  userid
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
		//现有的userID
		if(tempMyFriendGroup!=null)
			for (UserDetailInfo user : tempMyFriendGroup) {
				Log.v("现好友数",""+user.getUserId());
			}
	}
	//用户签到
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
	//删除好友的网络请求
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
	//get的方式提交
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
            	System.out.println("打印"+httpResponse.getStatusLine().getStatusCode()+strResult);
            }
            } catch (Exception e) {
			e.printStackTrace();
		}
		//Log.e("1111111111",strResult);
		return strResult;
	}
	//post的方式提交
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
