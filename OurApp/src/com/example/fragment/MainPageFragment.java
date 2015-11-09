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
	 //top�����view
	 private LinearLayout main_activity_top_lin_layout;
	 private AutoCompleteTextView main_activity_top_search_text;
	 private TextView main_activity_top_citytext;
	 private ImageView main_activity_to_search_img;
	 //sayhello
	 private TextView main_say_hello;
	 private String[] res_msg = {"�����������ҵ�����Ҫ��","�˶�֮·���ǽ���֮·","Ҳ�ǳɹ�֮·"};
	 //�������ͼƬ
	 private ImageView basketball_imgae,running_iamge,badminto_iamge,
	 						football_iamge,swimming_iamge,gym_imgae,
	 						table_tennis_imgae,park_iamge,volleyball_iamge,
	 						climb_iamge,rideing_iamge,other_imgae;
	 //�û���
	 private User user;
	 //��λ����
	 private int city_id = -1;//Ĭ��Ϊ-1,���˴�
	 private String city;
	 private String adress;
	//�ٶȶ�λ
	 private LocationClient mLocationClient = null;
	 private MyLocationListenner myListener = new MyLocationListenner();
	 //�ж��Ƿ��״ν��룬��ʼ��λ
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
				//��λ
				mLocationClient = new LocationClient(getActivity());  
				mLocationClient.registerLocationListener(myListener);
				setLocationOption();//�趨��λ����
				mLocationClient.start();//��ʼ��λ		 
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
		//top��view
		main_activity_top_lin_layout = (LinearLayout) view.findViewById(R.id.main_activity_top_lin_layout);
		main_activity_top_search_text = (AutoCompleteTextView) view.findViewById(R.id.main_activity_top_search_text);
		main_activity_top_citytext = (TextView) view.findViewById(R.id.main_activity_top_citytext);
		main_activity_to_search_img = (ImageView) view.findViewById(R.id.main_activity_to_search_img);
		//����¼�
		main_activity_top_lin_layout.setOnClickListener(new mainClickListener());
		main_activity_top_search_text.setOnClickListener(new mainClickListener());
		main_activity_top_citytext.setOnClickListener(new mainClickListener());
		main_activity_to_search_img.setOnClickListener(new mainClickListener());

		//�������ͼƬ
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
		//����¼�
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
			main_activity_top_citytext.setText("��λ��...");
		city = bundle.getString("city_name");
		main_activity_top_citytext.setText(city);
		int cityID = findcity_id(city) ;
		if(cityID != -1){
			SharedPreferences cityidsh = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
			Editor e = cityidsh.edit();
			int cid = findcity_id(city);
			e.putInt("city_id", cid);
			e.commit();
			Log.v("����Id",""+findcity_id(city));
		}
		Log.v("����Id",""+findcity_id(city));
		
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
				//��λ
				mLocationClient = new LocationClient(getActivity());  
				mLocationClient.registerLocationListener(myListener);
				setLocationOption();//�趨��λ����
				mLocationClient.start();//��ʼ��λ	
				break;
			case R.id.main_activity_to_search_img:
				//Toast.makeText(getActivity(), R.id.main_activity_top_lin_layout, 1000).show();
				break;
			case R.id.basketball_imgae:
				senttitle("����", 101);
				break;
			case R.id.running_iamge:
				senttitle("�ܲ�", 102);
				break;
			case R.id.badminto_iamge:
				senttitle("��ë��", 103);
				break;
			case R.id.football_iamge:
				senttitle("����",104);
				break;
			case R.id.swimming_iamge:
				senttitle("��Ӿ", 105);
				break;
			case R.id.gym_imgae:
				senttitle("����", 106);
				break;
			case R.id.table_tennis_imgae:
				senttitle("ƹ����", 107);
				break;
			case R.id.park_iamge:
				senttitle("��԰����", 108);
				break;
			case R.id.volleyball_iamge:
				senttitle("����", 109);
				break;
			case R.id.climb_iamge:
				senttitle("��ɽ", 110);
				break;
			case R.id.rideing_iamge:
				senttitle("����", 111);
				break;
			case R.id.other_imgae:
				senttitle("����", 112);
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
        option.setLocationMode(LocationMode.Hight_Accuracy);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
        option.setCoorType("bd09ll");//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
        int span=1000;
        option.setScanSpan(0);//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
        option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
        option.setOpenGps(true);//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
        option.setLocationNotify(true);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
        option.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
        option.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
        option.setIgnoreKillProcess(false);//��ѡ��Ĭ��false����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ��ɱ��
        option.SetIgnoreCacheException(false);//��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
        option.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
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
				main_activity_top_citytext.setText("��λ��".toString());
				Toast.makeText(getActivity(),"��λʧ��"+arg0.getLocType(), 1000).show();
				Log.v("�����ڵ���ϸ��ַ�ǣ�",adress+"2");
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
		//��sharedpreferences���洢����
				SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
				String userJson=preferences.getString("userJson", "defaultname");	
				Gson gson = new Gson();
				user = gson.fromJson(userJson, User.class); 
				user.setLocation_lasetime_login(city);
				
				//Toast.makeText(getActivity(), city+"������", 1000).show();
				Log.v("�������city�Ƿ�д����SharedPreferences",city+"");
				Toast.makeText(getActivity(),"����Id��" +city_id, 1000).show();
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
			if((cityname_array[i]+"��").equals(city_name)) 
				return i;
		}
		return -1;
	}
}
