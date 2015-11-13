package com.example.httpunit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

import com.example.bean.Comment;
import com.example.bean.CommentDetailInformation;
import com.example.bean.OtherPeopleComment;
import com.example.bean.SportPlace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HttpGetCommentJson {
	
	private static final int REQUEST_TIMEOUT = 5*1000;//设置请求超时5秒钟  
	private static final int SO_TIMEOUT = 5*1000;  //设置等待数据超时时间5秒钟 
	private ArrayList<Comment> Data = new ArrayList<Comment>();
	
	//设置httpCilent   
	public HttpClient getHttpClient(){  
		    BasicHttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
		    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
		    HttpClient client = new DefaultHttpClient(httpParams);  
		    return client;  
	}  
	public HttpGetCommentJson(){}
	
	//302 为最新的调用//301 为最热的调用
	public ArrayList<Comment> getNewData(int j) {
		//获取最新评论和最热评论的地址
		//String url = "http://10.0.2.2:8080/Ourapp/CommentNewAndHotMsg?onwhichPage="+j;
		
		String url = "http://xiafucheng.duapp.com/webAdroid/server/getCommentDetail?onwhichPage="+j
				+"&addMoreCount=" +10;
		String JsonStr = getHttpJsonString(url);
		Gson gson = new Gson();
		Data = gson.fromJson(JsonStr,  new TypeToken<ArrayList<Comment>>() {}.getType());	
		//比较排序
		//按时间先后排序
//		Comparator<Comment> comparator = new Comparator<Comment>(){
//			@Override
//			public int compare(Comment arg0, Comment arg1) {
//				arg0.getComment_from_time().getTime();
//				if(arg0.getComment_from_time().getTime()<arg1.getComment_from_time().getTime()){
//					return 1;
//				}
//				return -1;
//			}
//		};
//		if(Data != null)
//			Collections.sort(Data,comparator);
		return Data;
	}
	//最热的数据调用
	public ArrayList<Comment> getHotData(int j) {
		//获取最新评论和最热评论的地址

		String url = "http://xiafucheng.duapp.com/webAdroid/server/getCommentDetail?onwhichPage="+j
				+"&addMoreCount=" +10;
		String JsonStr = getHttpJsonString(url);
		Gson gson = new Gson();
		Data = gson.fromJson(JsonStr,  new TypeToken<ArrayList<Comment>>() {}.getType());	
		//比较排序
		//按评论数从大到小排序
//		Comparator<Comment> comparator = new Comparator<Comment>(){
//			@Override
//			public int compare(Comment arg0, Comment arg1) {
//				if(arg0.getHow_many_people_comment()<arg1.getHow_many_people_comment()){
//					return 1;
//				}
//				return -1;
//			}
//		};
//		if(Data != null)
//			Collections.sort(Data,comparator);
		return Data;
	}
	
	//获取更多数据
	//返回一个Commnet类的数组
	public ArrayList<Comment> getaddMoreData(int onwhichPage, int addMoreCount) {
		//参数      onwhichPage  参数     addMoreCount
		//加载更多的地址
//		String url ="http://xiafucheng.6655.la:20128/webAdroid/server/getCommentDetail?" +
//				"onwhichPage="+onwhichPage+"&addMoreCount="+addMoreCount;
		String url ="http://xiafucheng.duapp.com/webAdroid/server/getCommentDetail?" +
				"onwhichPage="+onwhichPage+"&addMoreCount="+addMoreCount;
		String JsonStr = getHttpJsonString(url);
		Gson gson = new Gson();
		ArrayList<Comment> MoreData = new ArrayList<Comment>();
		try {
			MoreData = gson.fromJson(JsonStr,
					new TypeToken<ArrayList<Comment>>(){}.getType());
			return MoreData;
		} catch (Exception e) {
			return null;
		}
	}
	//请求评论详情
	//返回一个详情类
	public CommentDetailInformation getCommentDetalInfoData(int commentId) {				
		//参数   commentId	
		//获取评论详细的地址
		String url = "http://xiafucheng.duapp.com/webAdroid/server/getCommentDetailInfo?commentId="
				+commentId;
		String JsonStr = getHttpJsonString(url);
		CommentDetailInformation CDInfo= new CommentDetailInformation();
		Gson gson = new Gson();
		CDInfo = gson.fromJson(JsonStr, CommentDetailInformation.class);
		return CDInfo;
	}
	//用户点赞
	//在commentId这个评论详情点赞加一的网络请求
	public void sentParseAddOne(int commentId) {
		// 参数    commentId  
		//     onwhichAddOne= 1    点赞加一 
		parseAndCommentAddOne(commentId,1);
	}
	//看过的加一
	//在commentId这个评论详情看过加一的网络请求
	public void seeCommentCountAddOne(int commentId) {
		//参数     commentId
		//	   onwhichAddOne= 2  看过加一
		parseAndCommentAddOne(commentId,2);
	}
	/*加一的网络请求
	 * 点赞加一      1  
	 * 评论加一       2
	 * */
	private void parseAndCommentAddOne(int commentId, int i) {
		//参数     commentId
	    //	   onwhichAddOne
		//加一的地址
		//String url = "http://xiafucheng.duapp.com/webAdroid/server/addPraise?commentId="+commentId;
		
		String url = "http://xiafucheng.duapp.com/webAdroid/server/getCommentDetailInfo?commentId="
				+commentId
				+"&onwhichAddOne="+commentId;
		HttpResponse httpResponse = null;
		HttpGet httpRequest = new HttpGet(url); 
		HttpClient httpClient = getHttpClient();
		try {
			httpResponse = httpClient.execute(httpRequest);
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				System.out.println("操作成功");
            }else{
            	System.out.println("操作失败");
            }
            } catch (Exception e) {
            	e.printStackTrace();
		}
	}
	//sent OtherPeopleComment 到服务器
	public void sentOPCToInternet(OtherPeopleComment otherPeopleComment) {
		// 参数  otherPeopleCommentJson
		 Gson gson = new Gson();
		 String gson_sent = gson.toJson(otherPeopleComment);
		 //String url ="http://10.0.2.2:8080/Ourapp/SportPlaceComment";
		 String url = "http://xiafucheng.duapp.com/webAdroid/server/otherPeopleCommentJson";
		 HttpClient httpClient = getHttpClient();
		 HttpPost httpPost = new HttpPost(url);
	        try {
	            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	            nameValuePair.add(new BasicNameValuePair("otherPeopleCommentJson", gson_sent));
	            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
	            System.out.println(gson_sent);
	            httpClient.execute(httpPost);
	        }catch(Exception e){
	        	
	        }
	}
	//用户发表发现言论
	//向服务器发送，利用post
	public void updateComment(Comment comment, CommentDetailInformation commentDetailInfo){
		// 参数   	commentJson  commentDetailInfoJson
		//发送到数据库中
		Gson gson = new Gson();
		String commentJson = gson.toJson(comment);
		String commentDetailInfoJson = gson.toJson(commentDetailInfo);
		//发送到的地址
		//String url ="http://10.0.2.2:8080/Ourapp/SportPlaceComment";
		
		//String url = "http://xiafucheng.6655.la:20128/webAdroid/server/addCommentDetail";
		String url = "http://xiafucheng.duapp.com/webAdroid/server/addCommentDetail";
		HttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
        try {
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("commentJson", commentJson));
            nameValuePair.add(new BasicNameValuePair("commentDetailInfoJson", commentDetailInfoJson));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
            System.out.println(commentJson);
            System.out.println(commentDetailInfoJson);
            httpClient.execute(httpPost);
        }catch(Exception e){
            System.out.println("操作失败");
        }
}			
	//获取网络请求的方法，
	//参数：url 
	//返回值:网络返回的一个字符串（json）
	private String getHttpJsonString(String url){
		String strResult = null;
		HttpGet httpRequest = new HttpGet(url); 
        HttpClient httpClient = getHttpClient();
        HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpRequest);
			if(httpResponse.getStatusLine().getStatusCode() == 200){
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }else{
            	System.out.println("网络返回码"+httpResponse.getStatusLine().getStatusCode()+strResult);
            }
            } catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}
}