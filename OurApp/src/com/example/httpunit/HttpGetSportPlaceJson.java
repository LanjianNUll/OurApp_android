package com.example.httpunit;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
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
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.example.bean.SportPlace;
import com.example.bean.SportPlaceComment;
import com.example.bean.SportPlaceDetailInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HttpGetSportPlaceJson {

	 private static final int REQUEST_TIMEOUT = 5*1000;//��������ʱ10����  
	 private static final int SO_TIMEOUT = 5*1000;  //���õȴ����ݳ�ʱʱ��10���� 
	
	 private ArrayList<SportPlace> Data = new ArrayList<SportPlace>();

	 //���ݳ��ж�λ��ȷ��ȡ�����ݿ���Դ
	 private String city = "yichun";
	 private SportPlace sportpalce;
	 private List<SportPlace> sportpalce_list;
	//����httpCilent 
	 public HttpClient getHttpClient(){  
		    BasicHttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
		    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
		    HttpClient client = new DefaultHttpClient(httpParams);  
		    return client;  
		}  
	 public HttpGetSportPlaceJson(){}
	  private ArrayList<SportPlace> getallData() {
		// ����ö�λ���е������˶�����
		return Data;
	}
	//����չʾ
	  //������������
	 public ArrayList<SportPlace> getData(int city_id, int distance_id) {
		return getallData(city_id, -1, distance_id, -1);
	}

 
	 public ArrayList<SportPlace> getSortData(int city_id, int sort_id) {
			return getallData(city_id, sort_id, -1, -1);
		}

	 //����ҳ�������Ľ���
	 public ArrayList<SportPlace> getallData(int city_id, int sort_id, int distance_id, int sportstyle_id) {
		 /*���� 101
		  * �ܲ�102
		  * ��ë��103
		  * ����104
		  * ��Ӿ105
		  * ����106
		  * ƹ����107
		  * ��԰����108
		  * ����109
		  * ��ɽ110
		  * ����111
		  * ����112*/
		 
		 /*���ż��� 1001
		  * ������ʱ 1002 
		  * ����ר�� 1003
		  * ǿ���� 1004
		  * Ұ��̽�� 1005*/
		 
		 /*ȫ��11
		  * 500m 12
		  * 3km 13
		  * 5km 14*/
		 
		    String strResult = "";
		    //����url�������ȡ����
		    strResult = getJsonFromHttp(city_id, sort_id, distance_id, sportstyle_id);
		    System.out.println("Ϊʲô��"+strResult+sort_id);
		    
		    //����url��ȡ��json��������������
		    Data = getObjectFromJson(strResult);
		    if(Data != null)
		    	//System.out.println("http����"+Data.toString());
		    	return Data;
		    else {
		    	SportPlace s = new SportPlace();
		    	s.setSportplace_name("������");
		    	Data.add(s);
		    	return Data;
		    }
		    	
	 }
	 //��ȡ���������
	 public SportPlaceDetailInformation getSportPlaceDetali(int sportplace_id){
		 //����  sportPlaceId
		 SportPlaceDetailInformation sp = new SportPlaceDetailInformation();
		 String strResult = null;
		 String url = "http://xiafucheng.6655.la:20128/webAdroid/server/servlet02?sportPlaceId="+sportplace_id;
	        HttpGet httpRequest = new HttpGet(url);
	       
	        HttpClient httpClient = getHttpClient();
	        
	        HttpResponse httpResponse = null;
			try {
				httpResponse = httpClient.execute(httpRequest);
				
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					
	                strResult = EntityUtils.toString(httpResponse.getEntity());
	            }else{
	            	System.out.println("��ӡ����԰�"+httpResponse.getStatusLine().getStatusCode()+strResult);
	            }
	            } catch (Exception e) {
				e.printStackTrace();
			}
			Log.v("����",""+strResult);
			Gson gson = new Gson();
		    sp = gson.fromJson(strResult, SportPlaceDetailInformation.class);
		return sp;		 
	 }
	 //���ͳ��ص�����������
	 public void sentHttpSportPlaceComment(SportPlaceComment spc){
		 
		 Gson gson = new Gson();
		 String gson_sent = gson.toJson(spc);
		 //String url ="http://10.0.2.2:8080/Ourapp/SportPlaceComment";
		 String url = "http://xiafucheng.6655.la:20128/webAdroid/server/addSPComment";
		 HttpClient httpClient = getHttpClient();
		 HttpPost httpPost = new HttpPost(url);
	        try {
	            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	            nameValuePair.add(new BasicNameValuePair("SportPlaceCommentGson", gson_sent));
	            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,HTTP.UTF_8));
	            System.out.println(gson_sent);
	            httpClient.execute(httpPost);
	        }catch(Exception e){
	        }
	 }
	 
	 //���ظ�������
	public ArrayList<SportPlace> getMoreDataData(int city_id, int sort_id,
			int distance_id, int sport_style_id, int addMoreCount) {

		 String strResult = null;
		
		 	 String url = "http://10.0.2.2:8080/Ourapp/Sportsplcacservlet?city_id="+city_id+
		 			 "&sort_id="+sort_id
				 	 +"&distance_id="+distance_id
				 	 +"&sport_style_id="+sport_style_id
				 	 +"&addMoreCount="+addMoreCount;
	        HttpGet httpRequest = new HttpGet(url); 
	        HttpClient httpClient = getHttpClient();
	        HttpResponse httpResponse = null;
			try {
				httpResponse = httpClient.execute(httpRequest);
				
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					
	                strResult = EntityUtils.toString(httpResponse.getEntity());
	            }else{
				System.out.println("��ӡ����԰�"+httpResponse.getStatusLine().getStatusCode()+strResult);
	            }
	            } catch (Exception e) {
				e.printStackTrace();
			}	
			Data = getObjectFromJson(strResult);
			
		return Data;
	}
	
	 /*����url��ȡ��json��������������*/
	 private ArrayList<SportPlace> getObjectFromJson(String strResult) {
		 Gson gson = new Gson();
		 ArrayList<SportPlace> Data = new ArrayList<SportPlace>();
		 //����������д����ë����   ֱ�ӵ���Data�ͺ���  ���������Ҿ����ø���  Ϊ�� �Ժ�������һ������
		 ArrayList<SportPlace> sportpalce_list = gson.fromJson(strResult, new TypeToken<ArrayList<SportPlace>>() {}.getType());  
		 if(sportpalce_list != null){
			for(int i = 0;i < sportpalce_list.size(); i++)
		        Data.add(sportpalce_list.get(i));
		 	return Data;
		 }
		 return null;
	}
	  
	/*�������ȡ���ݣ������ṩ�Ĳ��������û�о�Ϊ-1*/
	 private String getJsonFromHttp(int city_id, int sort_id , int distance_id,  int sport_style_id){
		 
		String strResult = null;
		String url = "http://xiafucheng.6655.la:20128/webAdroid/server/servlet01?" +
				"city_id="+1
				+"&sort_id="+sort_id
				+"&distance_id="+distance_id
				+"&sport_style_id="+sport_style_id
				+"&addMoreCount=10";
		
        HttpGet httpRequest = new HttpGet(url); 
        HttpClient httpClient = getHttpClient();
        HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }else{
            	System.out.println("��ӡ����԰�"+httpResponse.getStatusLine().getStatusCode()+strResult);
            }
            } catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	 }
}
