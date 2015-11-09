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

	 private static final int REQUEST_TIMEOUT = 5*1000;//设置请求超时10秒钟  
	 private static final int SO_TIMEOUT = 5*1000;  //设置等待数据超时时间10秒钟 
	
	 private ArrayList<SportPlace> Data = new ArrayList<SportPlace>();

	 //根据城市定位来确定取的数据库资源
	 private String city = "yichun";
	 private SportPlace sportpalce;
	 private List<SportPlace> sportpalce_list;
	//设置httpCilent 
	 public HttpClient getHttpClient(){  
		    BasicHttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
		    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
		    HttpClient client = new DefaultHttpClient(httpParams);  
		    return client;  
		}  
	 public HttpGetSportPlaceJson(){}
	  private ArrayList<SportPlace> getallData() {
		// 请求该定位城市的所有运动场所
		return Data;
	}
	//附近展示
	  //按距离来排序
	 public ArrayList<SportPlace> getData(int city_id, int distance_id) {
		return getallData(city_id, -1, distance_id, -1);
	}

 
	 public ArrayList<SportPlace> getSortData(int city_id, int sort_id) {
			return getallData(city_id, sort_id, -1, -1);
		}

	 //由首页进入分类的界面
	 public ArrayList<SportPlace> getallData(int city_id, int sort_id, int distance_id, int sportstyle_id) {
		 /*篮球 101
		  * 跑步102
		  * 羽毛球103
		  * 足球104
		  * 游泳105
		  * 健身房106
		  * 乒乓球107
		  * 公园休闲108
		  * 排球109
		  * 爬山110
		  * 骑行111
		  * 其他112*/
		 
		 /*紧张激烈 1001
		  * 安静闲时 1002 
		  * 年轻专属 1003
		  * 强身健体 1004
		  * 野外探索 1005*/
		 
		 /*全城11
		  * 500m 12
		  * 3km 13
		  * 5km 14*/
		 
		    String strResult = "";
		    //根据url从网络获取数据
		    strResult = getJsonFromHttp(city_id, sort_id, distance_id, sportstyle_id);
		    System.out.println("为什么是"+strResult+sort_id);
		    
		    //根据url获取的json解析出对象数组
		    Data = getObjectFromJson(strResult);
		    if(Data != null)
		    	//System.out.println("http分类"+Data.toString());
		    	return Data;
		    else {
		    	SportPlace s = new SportPlace();
		    	s.setSportplace_name("无数据");
		    	Data.add(s);
		    	return Data;
		    }
		    	
	 }
	 //获取具体的详情
	 public SportPlaceDetailInformation getSportPlaceDetali(int sportplace_id){
		 //参数  sportPlaceId
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
	            	System.out.println("打印你麻痹啊"+httpResponse.getStatusLine().getStatusCode()+strResult);
	            }
	            } catch (Exception e) {
				e.printStackTrace();
			}
			Log.v("数据",""+strResult);
			Gson gson = new Gson();
		    sp = gson.fromJson(strResult, SportPlaceDetailInformation.class);
		return sp;		 
	 }
	 //发送场地点评到服务器
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
	 
	 //加载更多请求
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
				System.out.println("打印你麻痹啊"+httpResponse.getStatusLine().getStatusCode()+strResult);
	            }
	            } catch (Exception e) {
				e.printStackTrace();
			}	
			Data = getObjectFromJson(strResult);
			
		return Data;
	}
	
	 /*根据url获取的json解析出对象数组*/
	 private ArrayList<SportPlace> getObjectFromJson(String strResult) {
		 Gson gson = new Gson();
		 ArrayList<SportPlace> Data = new ArrayList<SportPlace>();
		 //这里这样的写法有毛病啊   直接等于Data就好了  不过这里我就懒得改了  为了 以后避免而做一个提醒
		 ArrayList<SportPlace> sportpalce_list = gson.fromJson(strResult, new TypeToken<ArrayList<SportPlace>>() {}.getType());  
		 if(sportpalce_list != null){
			for(int i = 0;i < sportpalce_list.size(); i++)
		        Data.add(sportpalce_list.get(i));
		 	return Data;
		 }
		 return null;
	}
	  
	/*从网络获取数据，根据提供的参数，如果没有就为-1*/
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
            	System.out.println("打印你麻痹啊"+httpResponse.getStatusLine().getStatusCode()+strResult);
            }
            } catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	 }
}
