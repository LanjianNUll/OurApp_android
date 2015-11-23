package com.example.fragment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.example.activity.CityFindnear;
import com.example.activity.MyfirendActivity;
import com.example.activity.SubsSortActivity;
import com.example.activity.UserLoginActivity;
import com.example.adapter.FindSortListViewAdapter;
import com.example.adapter.FymMainFrammentAdapter;
import com.example.bean.Comment;
import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.example.fragment.ThirdPageFragment.UserSignTask;
import com.example.httpunit.HttpDoUserMsg;
import com.example.httpunit.HttpGetCommentJson;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainPageFragment extends Fragment {
	
	 private ImageView changguan,hotcomment,qiandao,Smyfriend;
	 private  FindSortListViewAdapter hotCommentadapter;
	 private ArrayList<Comment> hotComment = new ArrayList<Comment>();
	 private View view;
	 //top上面的view
	 private LinearLayout main_activity_top_lin_layout;
	 private AutoCompleteTextView main_activity_top_search_text;
	 private TextView main_activity_top_citytext;
	 private ImageView main_activity_to_search_img;
	 //sayhello
	 private TextView main_say_hello;
	 //各分类的图片
	 private ImageView basketball_imgae,running_iamge,badminto_iamge,
	 						football_iamge,swimming_iamge,gym_imgae,
	 						table_tennis_imgae,park_iamge,volleyball_iamge,
	 						climb_iamge,rideing_iamge,other_imgae;
	 //用户类
	 private UserDetailInfo user;
	 //定位城市
	 private int city_id = -1;//默认为-1,即宜春
	 private String city;
	 private String adress;
	//百度定位
	 private LocationClient mLocationClient = null;
	 private MyLocationListenner myListener = new MyLocationListenner();
	 //第二套布局
	 private ListView fym_listView,hotcomment_listView;
	 private FymMainFrammentAdapter adapter;
	 private String[] name = {"篮球","跑步","羽毛球","足球","游泳","健身房","乒乓球","公园休闲"
				,"排球","爬山","骑行","其他"};
	 private int[] Data = {R.drawable.fym_basketball, R.drawable.fym_run, R.drawable.fym_badminton,
			 R.drawable.fym_footvall,R.drawable.fym_swimming, R.drawable.fym_gym,
			 R.drawable.fym_table_tennis,R.drawable.fym_park, R.drawable.fym_volleyball,
			 R.drawable.fym_climb_mountain, R.drawable.fym_ride,R.drawable.fym_baseball};
	 private ScrollView scrollview;
	 private int userId = -1;
//	 //判断是否首次进入，开始定位
//	 private boolean isstartloaction = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
			view = inflater.inflate(R.layout.app_mainpage, container, false);
//			SharedPreferences startloaction = getActivity().getSharedPreferences("loaction_start", Context.MODE_PRIVATE);
//			isstartloaction = startloaction.getBoolean("isstartloaction", true);	
//			if(isstartloaction){
//				isstartloaction = false;
//				Editor editor=startloaction.edit();
//				editor.putBoolean("isstartloaction", isstartloaction);
//				editor.commit();
			//}
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
		
		SharedPreferences file = getActivity().getSharedPreferences("userDetailFile",
				Context.MODE_PRIVATE);
			String s = file.getString("userDetail", null);
			try {
				userId = new Gson().fromJson(s, UserDetailInfo.class).getUserId();
			} catch (Exception e) {
		}
		
		changguan = (ImageView) view.findViewById(R.id.changguan);
		hotcomment = (ImageView) view.findViewById(R.id.hotcomment);
		qiandao = (ImageView) view.findViewById(R.id.qiandao);
		Smyfriend = (ImageView) view.findViewById(R.id.Smyfriend);
		
		changguan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 1);
				intent.putExtras(bundle);
				startActivity(intent);
				getActivity().finish();
				//界面跳转的动画
				getActivity().overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		});
		
		hotcomment.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getActivity(), MainActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("CurrentItem", 2);
						intent.putExtras(bundle);
						startActivity(intent);
						getActivity().finish();
						//界面跳转的动画
						getActivity().overridePendingTransition(R.drawable.interface_jump_in,
								R.drawable.interface_jump_out);
					}
				});
		
		//qian dao 
		qiandao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userId == -1){//显示用户未登录
					AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
					ab.setTitle("");
					ab.setMessage("亲，你还没登录~");
					ab.setPositiveButton("去登录", new DialogInterface.OnClickListener() {						
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(getActivity(), UserLoginActivity.class);
							startActivity(intent);
							getActivity().finish();
							//界面跳转的动画
							getActivity().overridePendingTransition(R.drawable.interface_jump_in,
									R.drawable.interface_jump_out);
						}
					});
					ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.cancel();
						}
					});
					// create alert dialog
					AlertDialog alertDialog = ab.create();
					// show it
					alertDialog.show();
				}
				//获取用户上传签到的时间
				SharedPreferences sf = getActivity()
						.getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
				long time = sf.getLong("userLastTimeSiginTime", 0);
				if(time == 0 || System.currentTimeMillis() - time > 24*60*60){
						new UserSignTask().execute(userId);
						//记录用户签到时间
						Editor e = sf.edit();
						e.putLong("userLastTimeSiginTime", System.currentTimeMillis());
						e.commit();
				}else
					Toast.makeText(getActivity(), "你今天已经签过到，明天再来哦~",
								Toast.LENGTH_LONG).show();
				
			}
		});
		// wo de pengy liebiao 
		Smyfriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userId!=-1){
					Intent intent = new Intent(getActivity(), MyfirendActivity.class);
					//将用户的id传递过去
					Bundle bundle = new Bundle();
					bundle.putInt("userId", userId);
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
					//界面跳转的动画
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
					ab.setTitle("");
					ab.setMessage("亲，你还没登录~");
					ab.setPositiveButton("去登录", new DialogInterface.OnClickListener() {						
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(getActivity(), UserLoginActivity.class);
							startActivity(intent);
							getActivity().finish();
							//界面跳转的动画
							getActivity().overridePendingTransition(R.drawable.interface_jump_in,
									R.drawable.interface_jump_out);
						}
					});
					ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.cancel();
						}
					});
					// create alert dialog
					AlertDialog alertDialog = ab.create();
					// show it
					alertDialog.show();
				}
			}
		});
		
		//fym
		fym_listView = (ListView) view.findViewById(R.id.fym_listView);
		hotcomment_listView = (ListView) view.findViewById(R.id.hotcomment_listView);
		
		new HotCommentTask().execute();
		
		scrollview = (ScrollView) view.findViewById(R.id.scrollview);
		
		fym_listView.setAdapter(adapter =
				new FymMainFrammentAdapter(getActivity(), Data));
		//设置fym_listView的高度
		setListViewHeight(fym_listView, adapter);
		fym_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				senttitle(name[arg2], 101+arg2);
			}
		});
		scrollview.scrollTo(0, 0);
		
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
		city = bundle.getString("city_name");
		if(city == null){
			main_activity_top_citytext.setText("定位中...");
			//百度定位
			mLocationClient = new LocationClient(getActivity());  
			mLocationClient.registerLocationListener(myListener);
			setLocationOption();//设定定位参数
			mLocationClient.start();//开始定位
		}
		else
			main_activity_top_citytext.setText(city);
		//记录城市ID
		int cityID = findcity_id(city);
		if(cityID != -1){
			SharedPreferences cityidsh = getActivity().getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
			Editor e = cityidsh.edit();
			int cid = findcity_id(city);
			e.putInt("city_id", cid);
			e.commit();
			Log.v("城市Id",""+findcity_id(city));
		}
		Log.v("城市Id",""+findcity_id(city));
		//to first line
		
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
				//界面跳转的动画
				getActivity().overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out); 
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
		getActivity().finish();
		//界面跳转的动画
		getActivity().overridePendingTransition(R.drawable.interface_jump_in,
				R.drawable.interface_jump_out);
	}
	
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();  
        option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1001);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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
				//Toast.makeText(getActivity(),"定位失败"+arg0.getLocType(), 1000).show();
				//Log.v("你所在的详细地址是：",adress+"2");
			}
			else{
				main_activity_top_citytext.setText(city);
				city_id = findcity_id(city);
				putCityinShard(city,city_id);
				//把详细地点写到文件中
				writeLocationDetailToFile(arg0);
				mLocationClient.stop();
			}
		}
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
		}	
	}

	public void putCityinShard(String city2, int city_Id) {
		//用sharedpreferences来存储数据
				SharedPreferences preferences = getActivity().getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
				String userJson=preferences.getString("userDetail", null);	
				Gson gson = new Gson();
				user = gson.fromJson(userJson, UserDetailInfo.class); 
				user.setLocation_lasetime_login(city2);
				//Toast.makeText(getActivity(), city+"你在那", 1000).show();
				Log.v("这里测试city是否写入了SharedPreferences",city+"");
				//Toast.makeText(getActivity(),"城市Id是" +city_id, 1000).show();
				String userJson_to = gson.toJson(user);
				SharedPreferences preferences1 = getActivity().getSharedPreferences("userDetailFile",Context.MODE_PRIVATE);
				Editor editor=preferences1.edit();
				editor.putString("userDetail", userJson_to);
				editor.putInt("city_id", city_Id);
				editor.commit();	
	}
	//把详细地点写到文件中
	public void writeLocationDetailToFile(BDLocation arg0) {
		String LocationDetail = arg0.getCity()//城市
				+arg0.getDistrict()//县区
				+arg0.getStreet();//街道号
				
		if(arg0.getBuildingID()!= null)
			LocationDetail = LocationDetail+arg0.getBuildingID();//获取楼层信息，
//		Toast.makeText(getActivity(),
//				(float) arg0.getLatitude()+"%%"
//				+(float) arg0.getLongitude(), 1000).show();
//		Toast.makeText(getActivity(), LocationDetail, 1000).show();
		SharedPreferences LocationDetailSharePf = 
				getActivity().getSharedPreferences("userDetailFile",
						Context.MODE_PRIVATE);
		Editor ed = LocationDetailSharePf.edit();
		ed.putString("LocationDetail", LocationDetail);
		//获取经纬度
		ed.putFloat("Latitude", (float) arg0.getLatitude());//获取纬度坐标
		ed.putFloat("Latitude", (float) arg0.getLongitude());//getLongitude()
		ed.commit();
		
		
	}
	public int findcity_id(String city_name) {
		
		String[] cityname_array = getResources().getStringArray(R.array.city);
		for(int i = 0;i < cityname_array.length; i++){
			if((cityname_array[i]+"市").equals(city_name)) 
				return i;
		}
		return -1;
	}
	
	
	private void setListViewHeight(ListView listView, BaseAdapter adapter) {
		adapter = (BaseAdapter) listView.getAdapter();    
        if (adapter == null) {    
            return;    
        }    
        int totalHeight = 0;    
        for (int i = 0, len = adapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目    
            View listItem = adapter.getView(i, null, listView);    
            listItem.measure(0, 0); // 计算子项View 的宽高    
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度    
	        }    
	        
	        ViewGroup.LayoutParams params = listView.getLayoutParams();    
	        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));    
	        listView.setLayoutParams(params);
	}
	class HotCommentTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			HttpGetCommentJson http = new HttpGetCommentJson();
			ArrayList<Comment> Comment = new ArrayList<Comment>();
			 Comment = http.getHotData(301);
			 if(Comment != null){
				 int lenth = 3;
				 if(Comment.size() < 3)
					 lenth = Comment.size();
				 for(int i = 0; i < lenth; i++)
					hotComment.add(Comment.get(i));
				 return 1;
			 }
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1)
				hotcomment_listView.setAdapter(hotCommentadapter = 
				new FindSortListViewAdapter(getActivity(), hotComment));
			setListViewHeight(hotcomment_listView, hotCommentadapter);
			
			super.onPostExecute(result);
		}
	}
	class UserSignTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			HttpDoUserMsg httpDoUserMsg = new HttpDoUserMsg();
			return httpDoUserMsg.userSign(arg0[0]);
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				Toast.makeText(getActivity(), "签到成功", 1000).show();
			}else{
				Toast.makeText(getActivity(), "签到失败", 1000).show();
			}
			super.onPostExecute(result);
		}
		
	}
}
