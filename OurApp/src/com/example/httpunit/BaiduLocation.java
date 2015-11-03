package com.example.httpunit;

import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class BaiduLocation {
	
	//�ٶȶ�λ
	private boolean issucesslocation = false;
	private int state = 0;
	private String city = null;
	private String adress = null;
	private Context context = null;
	//�ٶȶ�λ
	private LocationClient mLocationClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();
	
	public BaiduLocation (Context context){
		this.context = context;
		location();
		
	};
	private void location() {
		
		
		mLocationClient = new LocationClient(context); 
		
		mLocationClient.registerLocationListener(myListener);
		
		setLocationOption();//�趨��λ����
		
		mLocationClient.start();//��ʼ��λ
		
	}

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();  
        option.setOpenGps(true);  
        option.setAddrType("all");//���صĶ�λ���������ַ��Ϣ  
        option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02  
        option.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ5000ms  
        option.disableCache(true);//��ֹ���û��涨λ
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
