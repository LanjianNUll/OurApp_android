package com.example.httpunit;

import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class BaiduLocation {
	
	//百度定位
	private boolean issucesslocation = false;
	private int state = 0;
	private String city = null;
	private String adress = null;
	private Context context = null;
	//百度定位
	private LocationClient mLocationClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();
	
	public BaiduLocation (Context context){
		this.context = context;
		location();
		
	};
	private void location() {
		
		
		mLocationClient = new LocationClient(context); 
		
		mLocationClient.registerLocationListener(myListener);
		
		setLocationOption();//设定定位参数
		
		mLocationClient.start();//开始定位
		
	}

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();  
        option.setOpenGps(true);  
        option.setAddrType("all");//返回的定位结果包含地址信息  
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02  
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms  
        option.disableCache(true);//禁止启用缓存定位
        option.setIsNeedLocationPoiList(true);
                 
        mLocationClient.setLocOption(option);  	
		
	}

	class MyLocationListenner implements  BDLocationListener {
		
		@Override
		public void onReceiveLocation(BDLocation location) {
			
			if(location == null)
			{
				return;
			}
			else{
				issucesslocation = true;
				state = location.getLocType();
				adress = location.getAddrStr();
				city = location.getCity();
				mLocationClient.stop();
				Toast.makeText(context, getCity()+"hello", 1000).show();
			}
		}
		public void onReceivePoi(BDLocation arg0) {	
		}	
	}	

	public String getCity(){
		return city;	
	}
	public String getAdress(){
		return adress;
	}
	public int getState(){
		return state;
	}
	public boolean isIssucesslocation() {
		return issucesslocation;
	}
	
}
