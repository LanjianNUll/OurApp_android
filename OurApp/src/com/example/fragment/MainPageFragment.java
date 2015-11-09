package com.example.fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.example.activity.CityFindnear;
import com.example.activity.SubsSortActivity;
import com.example.bean.CityId;
import com.example.bean.User;
import com.example.httpunit.BaiduLocation;
import com.example.ourapp.R;
import com.google.gson.Gson;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainPageFragment extends Fragment {

	 private View view;
	 //top上面的view
	 private LinearLayout main_activity_top_lin_layout;
	 private AutoCompleteTextView main_activity_top_search_text;
	 private TextView main_activity_top_citytext;
	 private ImageView main_activity_to_search_img;
	 //sayhello
	 private TextView main_say_hello;
	 private String[] res_msg = {"在这里总能找到你想要的","运动之路亦是交友之路","也是成功之路"};
	 //各分类的图片
	 private ImageView basketball_imgae,running_iamge,badminto_iamge,
	 						football_iamge,swimming_iamge,gym_imgae,
	 						table_tennis_imgae,park_iamge,volleyball_iamge,
	 						climb_iamge,rideing_iamge,other_imgae;
	 //用户类
	 private User user;
	 //定位城市
	 private int city_id = -1;//默认为-1,即宜春
	 private String city;
	 private String adress;
	//百度定位
	 private LocationClient mLocationClient = null;
	 private MyLocationListenner myListener = new MyLocationListenner();
	 //判断是否首次进入，开始定位
	 private boolean isstartloaction = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.app_mainpage, container, false);
			SharedPreferences startloaction = getActivity().getSharedPreferences("loaction_start", Context.MODE_PRIVATE);
			isstartloaction = startloaction.getBoolean("isstartloaction", true);	
			if(isstartloaction){
				isstartloaction = false;
				Editor editor=startloaction.edit();
				editor.putBoolean("isstartloaction", isstartloaction);
				editor.commit();
				//定位
				mLocationClient = new LocationClient(getActivity());  
				mLocationClient.registerLocationListener(myListener);
				setLocationOption();//设定定位参数
				mLocationClient.start();//开始定位		 
			}
		 initview();
		 return view;
	}
	@SuppressLint("NewApi")
	protected void initTextViewAaim() {
		AnimatorSet animSet = new AnimatorSet();
    	ObjectAnimator text_anim_x = ObjectAnimator.ofFloat(main_say_hello,"scaleX", 1f, 1.5f,1f);
    	ObjectAnimator text_anim_y = ObjectAnimator.ofFloat(main_say_hello,"scaleY", 1f, 1.5f,1f);
		animSet.play(text_anim_x).with(text_anim_y);
		animSet.setDuration(2000);
		animSet.start();		
	}
	private void initview() {
		//sayhello
		main_say_hello = (TextView) view.findViewById(R.id.main_say_hello);
		//top的view
		main_activity_top_lin_layout = (LinearLayout) view.findViewById(R.id.main_activity_top_lin_layout);
		main_activity_top_search_text = (AutoCompleteTextView) view.findViewById(R.id.main_activity_top_search_text);
		main_activity_top_citytext = (TextView) view.findViewById(R.id.main_activity_top_citytext);
		main_activity_to_search_img = (ImageView) view.findViewById(R.id.main_activity_to_search_img);
		//添加事件
		main_activity_top_lin_layout.setOnClickListener(new mainClickListener());
		main_activity_top_search_text.setOnClickListener(new mainClickListener());
		main_activity_top_citytext.setOnClickListener(new mainClickListener());
		main_activity_to_search_img.setOnClickListener(new mainClickListener());

		//各分类的图片
		basketball_imgae = (ImageView) view.findViewById(R.id.basketball_imgae);
		running_iamge = (ImageView) view.findViewById(R.id.running_iamge);
		badminto_iamge = (ImageView) view.findViewById(R.id.badminto_iamge);
		football_iamge = (ImageView) view.findViewById(R.id.football_iamge);
		swimming_iamge = (ImageView) view.findViewById(R.id.swimming_iamge);
		gym_imgae = (ImageView) view.findViewById(R.id.gym_imgae);
		table_tennis_imgae = (ImageView) view.findViewById(R.id.table_tennis_imgae);
		park_iamge = (ImageView) view.findViewById(R.id.park_iamge);
		volleyball_iamge = (ImageView) view.findViewById(R.id.volleyball_iamge);
		climb_iamge = (ImageView) view.findViewById(R.id.climb_iamge);
		rideing_iamge = (ImageView) view.findViewById(R.id.rideing_iamge);
		other_imgae = (ImageView) view.findViewById(R.id.other_imgae);
		//添加事件
		basketball_imgae.setOnClickListener(new mainClickListener());
		running_iamge.setOnClickListener(new mainClickListener());
		badminto_iamge.setOnClickListener(new mainClickListener());
		football_iamge.setOnClickListener(new mainClickListener());
		swimming_iamge.setOnClickListener(new mainClickListener());
		gym_imgae.setOnClickListener(new mainClickListener());
		table_tennis_imgae.setOnClickListener(new mainClickListener());
		park_iamge.setOnClickListener(new mainClickListener());
		volleyball_iamge.setOnClickListener(new mainClickListener());
		climb_iamge.setOnClickListener(new mainClickListener());
		rideing_iamge.setOnClickListener(new mainClickListener());
		other_imgae.setOnClickListener(new mainClickListener());
		
		Bundle bundle = getActivity().getIntent().getExtras();
		if(city == null)
			main_activity_top_citytext.setText("定位中...");
		city = bundle.getString("city_name");
		main_activity_top_citytext.setText(city);
		int cityID = findcity_id(city) ;
		if(cityID != -1){
			SharedPreferences cityidsh = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
			Editor e = cityidsh.edit();
			int cid = findcity_id(city);
			e.putInt("city_id", cid);
			e.commit();
			Log.v("城市Id",""+findcity_id(city));
		}
		Log.v("城市Id",""+findcity_id(city));
		
	}
	class mainClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()){
			case R.id.main_activity_top_search_text:
				Log.v("here ","i am here ");
				Intent intent = new Intent(getActivity(), CityFindnear.class);
				startActivity(intent);
				getActivity().finish();
				break;
			case R.id.main_activity_top_citytext:
				//定位
				mLocationClient = new LocationClient(getActivity());  
				mLocationClient.registerLocationListener(myListener);
				setLocationOption();//设定定位参数
				mLocationClient.start();//开始定位	
				break;
			case R.id.main_activity_to_search_img:
				//Toast.makeText(getActivity(), R.id.main_activity_top_lin_layout, 1000).show();
				break;
			case R.id.basketball_imgae:
				senttitle("篮球", 101);
				break;
			case R.id.running_iamge:
				senttitle("跑步", 102);
				break;
			case R.id.badminto_iamge:
				senttitle("羽毛球", 103);
				break;
			case R.id.football_iamge:
				senttitle("足球",104);
				break;
			case R.id.swimming_iamge:
				senttitle("游泳", 105);
				break;
			case R.id.gym_imgae:
				senttitle("健身房", 106);
				break;
			case R.id.table_tennis_imgae:
				senttitle("乒乓球", 107);
				break;
			case R.id.park_iamge:
				senttitle("公园休闲", 108);
				break;
			case R.id.volleyball_iamge:
				senttitle("排球", 109);
				break;
			case R.id.climb_iamge:
				senttitle("爬山", 110);
				break;
			case R.id.rideing_iamge:
				senttitle("骑行", 111);
				break;
			case R.id.other_imgae:
				senttitle("其他", 112);
				break;
			}
		}
	}

	public void senttitle(String string,int sortInt) {
		Intent intent = new Intent(getActivity(),SubsSortActivity.class);
		Bundle bundle = new Bundle();
		String name = string;
		bundle.putString("name",name);
		bundle.putInt("sort", sortInt);
		intent.putExtras(bundle);
		startActivity(intent);
	//	getActivity().finish();
	}
	
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();  
        option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.setLocOption(option);		
	}
	
	class MyLocationListenner implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			if(arg0 == null){
				
				return;}
			city = arg0.getCity();
			adress = arg0.getAddrStr();
			if(city == null){
				main_activity_top_citytext.setText("定位中".toString());
				Toast.makeText(getActivity(),"定位失败"+arg0.getLocType(), 1000).show();
				Log.v("你所在的详细地址是：",adress+"2");
			}
			else{
				main_activity_top_citytext.setText(city);
				city_id = findcity_id(city);
				putCityinShard(city,city_id);
				mLocationClient.stop();
			}
		}
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
		}	
	}

	public void putCityinShard(String city2, int city_Id) {
		//用sharedpreferences来存储数据
				SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
				String userJson=preferences.getString("userJson", "defaultname");	
				Gson gson = new Gson();
				user = gson.fromJson(userJson, User.class); 
				user.setLocation_lasetime_login(city);
				
				//Toast.makeText(getActivity(), city+"你在那", 1000).show();
				Log.v("这里测试city是否写入了SharedPreferences",city+"");
				Toast.makeText(getActivity(),"城市Id是" +city_id, 1000).show();
				String userJson_to = gson.toJson(user);
				SharedPreferences preferences1 = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
				Editor editor=preferences1.edit();
				editor.putString("userJson", userJson_to);
				editor.putInt("city_id", city_Id);
				editor.commit();	
	}
	public int findcity_id(String city_name) {
		
		String[] cityname_array = getResources().getStringArray(R.array.city);
		for(int i = 0;i < cityname_array.length; i++){
			if((cityname_array[i]+"市").equals(city_name)) 
				return i;
		}
		return -1;
	}
}
