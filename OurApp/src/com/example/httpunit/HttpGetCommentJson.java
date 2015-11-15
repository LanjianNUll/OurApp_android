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
	
	private static final int REQUEST_TIMEOUT = 5*1000;//��������ʱ5����  
	private static final int SO_TIMEOUT = 5*1000;  //���õȴ����ݳ�ʱʱ��5���� 
	private ArrayList<Comment> Data = new ArrayList<Comment>();
	
	//����httpCilent   
	public HttpClient getHttpClient(){  
		    BasicHttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
		    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
		    HttpClient client = new DefaultHttpClient(httpParams);  
		    return client;  
	}  
	public HttpGetCommentJson(){}
	
	//302 Ϊ���µĵ���//301 Ϊ���ȵĵ���
	public ArrayList<Comment> getNewData(int j) {
		//��ȡ�������ۺ��������۵ĵ�ַ
		//String url = "http://10.0.2.2:8080/Ourapp/CommentNewAndHotMsg?onwhichPage="+j;
		
		String url = "http://xiafucheng.duapp.com/webAdroid/server/getCommentDetail?onwhichPage="+j
				+"&addMoreCount=" +10;
		String JsonStr = getHttpJsonString(url);
		Gson gson = new Gson();
		try {
			Data = gson.fromJson(JsonStr, 
					new TypeToken<ArrayList<Comment>>() {}.getType());
			return Data;
		} catch (Exception e) {
			return null;
		}
		
		//�Ƚ�����
		//��ʱ���Ⱥ�����
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
	}
	//���ȵ����ݵ���
	public ArrayList<Comment> getHotData(int j) {
		//��ȡ�������ۺ��������۵ĵ�ַ
		String url = "http://xiafucheng.duapp.com/webAdroid/server/getCommentDetail?onwhichPage="+j
				+"&addMoreCount=" +10;
		String JsonStr = getHttpJsonString(url);
		Gson gson = new Gson();
		try {
			Data = gson.fromJson(JsonStr,  
					new TypeToken<ArrayList<Comment>>() {}.getType());	
			return Data;
		} catch (Exception e) {
			return null;
		}
		
		//�Ƚ�����
		//���������Ӵ�С����
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
	}
	
	//��ȡ��������
	//����һ��Commnet�������
	public ArrayList<Comment> getaddMoreData(int onwhichPage, int addMoreCount) {
		//����      onwhichPage  ����     addMoreCount
		//���ظ���ĵ�ַ
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
	//������������
	//����һ��������
	public CommentDetailInformation getCommentDetalInfoData(int commentId) {				
		//����   commentId	
		//��ȡ������ϸ�ĵ�ַ
		String url = "http://xiafucheng.duapp.com/webAdroid/server/getCommentDetailInfo?commentId="
				+commentId;
		String JsonStr = getHttpJsonString(url);
		CommentDetailInformation CDInfo= new CommentDetailInformation();
		Gson gson = new Gson();
		CDInfo = gson.fromJson(JsonStr, CommentDetailInformation.class);
		return CDInfo;
	}
	//�û�����
	//��commentId�������������޼�һ����������
	public int sentParseAddOne(int commentId) {
		// ����    commentId  
		//     onwhichAddOne= 1    ���޼�һ 
		return parseAndCommentAddOne(commentId,1);
	}
	//�����ļ�һ
	//��commentId����������鿴����һ����������
	public int seeCommentCountAddOne(int commentId) {
		//����     commentId
		//	   onwhichAddOne= 2  ������һ
		return parseAndCommentAddOne(commentId,2);
	}
	/*��һ����������
	 * ���޼�һ      1  
	 * ���ۼ�һ       2
	 * */
	private int parseAndCommentAddOne(int commentId, int i) {
		//����     commentId
	    //	   onwhichAddOne
		//��һ�ĵ�ַ
		//String url = "http://xiafucheng.duapp.com/webAdroid/server/addPraise?commentId="+commentId;
		
		String url = "http://xiafucheng.duapp.com/webAdroid/server/addPraise?commentId="
				+commentId
				+"&onwhichAddOne="+commentId;
		HttpResponse httpResponse = null;
		HttpGet httpRequest = new HttpGet(url); 
		HttpClient httpClient = getHttpClient();
		try {
			httpResponse = httpClient.execute(httpRequest);
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				System.out.println("�����ɹ�");
				return 1;
            }else{
            	System.out.println("����ʧ��");
            	return 0;
            }
            } catch (Exception e) {
            	e.printStackTrace();
            	return 0;
		}
	}
	//sent OtherPeopleComment ��������
	public int sentOPCToInternet(OtherPeopleComment otherPeopleComment) {
		// ����  otherPeopleCommentJson
		// 1 ���ͳɹ�
		// 0 ����ʧ��
		 Gson gson = new Gson();
		 String gson_sent = gson.toJson(otherPeopleComment);
		 //String url ="http://10.0.2.2:8080/Ourapp/SportPlaceComment";
		 String url = "http://xiafucheng.duapp.com/webAdroid/server/addOtherComment";
		 HttpClient httpClient = getHttpClient();
		 
		 HttpPost httpPost = new HttpPost(url);
	        try {
	            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	            nameValuePair.add(new BasicNameValuePair("otherPeopleCommentJson", gson_sent));
	            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
	            System.out.println(gson_sent);
	            if(httpClient.execute(httpPost).getStatusLine().getStatusCode()==200)
	            	return 1;
	            else return 0;
	        }catch(Exception e){
	        	return 0;
	        }
	}
	//�û�����������
	//����������ͣ�����post
	public int updateComment(Comment comment, CommentDetailInformation commentDetailInfo){
		// ����   	commentJson  commentDetailInfoJson
		//���͵����ݿ���
		Gson gson = new Gson();
		String commentJson = gson.toJson(comment);
		String commentDetailInfoJson = gson.toJson(commentDetailInfo);
		//���͵��ĵ�ַ
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
            if(httpClient.execute(httpPost).getStatusLine().getStatusCode()==200)
            	return 1;
            else return 0;
        }catch(Exception e){
            System.out.println("����ʧ��");
            return 0;
        }
}			
	//��ȡ��������ķ�����
	//������url 
	//����ֵ:���緵�ص�һ���ַ�����json��
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
            	System.out.println("���緵����"+httpResponse.getStatusLine().getStatusCode()+strResult);
            }
            } catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}
}