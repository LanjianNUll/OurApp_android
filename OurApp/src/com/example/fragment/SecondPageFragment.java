package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.R.string;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.SportsPlaceDetail;
import com.example.activity.SubsSortActivity;
import com.example.adapter.ListViewAdapter;
import com.example.bean.SportPlace;
import com.example.bean.SportPlaceDetailInformation;
import com.example.httpunit.HttpGetSportPlaceJson;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;

public class SecondPageFragment extends Fragment {

	private View view;
	//top的图片按钮
	private ImageView comeback_main, search_image;
	//分类的布局
	private LinearLayout all_sort, distance_sort, people_like_sort;
	//分类的文字
	private TextView all_sort_text, distance_sort_text, people_like_sort_text;
	//分类的图片
	private ImageView all_sort_image, distance_sort_image, people_like_sort_image;
	//Listview
	private ListView list_nearby;
	//适配listview
	private ListViewAdapter adapter;
	private  Dialog dialog, dialog1, dialog2;
	/*设置一个标志指明当前在那个分类下   
	 * 0   在所有分类
	 * 1  在距离分类
	 * 2 在运动风格分类*
	 * 默认为在距离分类即  dialog_int= 1*/
	private int dialog_int = 1;
	private int sortId = -1;
	private int distanceId = -1;
	private int sportstyleId = -1;
	//城市ID
	private int cityId = -1;
	//分类对话框里的view
	private TextView all_sort_basktball_text,all_sort_running_text,all_sort_nettennis_text,
	all_sort_football_text,all_sort_swim_text,all_sort_gym_text,
	all_sort_tableball_text,all_sort_part_relax_text,all_sort_volleyball_text,
	all_sort_climb_text,all_sort_part_ride_text,all_sort_other_text;
	private TextView distance_sort_allcity_text,distance_sort_500m_text,distance_sort_3Km_text,
	distance_sort_5Km_text;
	private TextView peoplelike_sort_hardsport_text,peoplelike_sort_quitesport_text,peoplelike_sort_youngsport_text,
	peoplelike_sort_build_bodysport_text,peoplelike_besidesort_sport_text;
	//加载中的文字
	private TextView jiazai_text;
	private ImageView jiazai_pic;
	private RelativeLayout loading ,errorpage;
	Handler handler;

	//加载http工具包
	private  HttpGetSportPlaceJson httpgetjson  = new HttpGetSportPlaceJson();

	//定义的一个容器
	private ArrayList<SportPlace> Data = new ArrayList<SportPlace>();
	//加载更多的数目
	private int addMoreCount = 10;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.app_secondpage, container, false);
		//获取城市id
		SharedPreferences city= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
		cityId = city.getInt("city_id", -1);
		jiazai_pic = (ImageView) view.findViewById(R.id.jiazai_pic);
		jiazai_text = (TextView) view.findViewById(R.id.jiazai_text);
		loading = (RelativeLayout) view.findViewById(R.id.loading);
		//网络异常的显示的界面
		errorpage = (RelativeLayout) view.findViewById(R.id.errorpage);
		inintview();
		handler = new Handler(){
			int i = 0 ;
			public void handleMessage(Message msg) {
				//处理消息时，需要知道到底是成功的消息，还是失败的消息
				switch (msg.what) {
				case 1:
					loading.setVisibility(View.GONE);
					list_nearby.setAdapter(adapter = new ListViewAdapter(getActivity(), Data));
					break;	
				case 2 :
					jiazai_pic.setImageResource(R.drawable.jiazai00+i%6);
					if(i%2==0)
						jiazai_text.setText("加载中...");
					else if(i%3==0)
						jiazai_text.setText("加载中..");
					else
						jiazai_text.setText("加载中.");
					i++;
					if(i==100)
						i=0;
					break;
				case 3:
					loading.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
					list_nearby.setSelection(addMoreCount+1);
					addMoreCount = addMoreCount+10;
					break;
				case 4:
					loading.setVisibility(View.VISIBLE);
					break;
				case 5:
					loading.setVisibility(View.GONE);
					errorpage.setVisibility(View.VISIBLE);
					break;
				}
				
			}       	
		};	 
		new Thread(){
			public void run(){
				try {
					 if (getActivity() == null) {  
					        return;  
					    }
					//处理耗时网络加载json和json解析
					//模拟耗时
					sleep(2000);
					Data = httpgetjson.getData(-1,-1);//距离优先
					if(Data == null)
						handler.sendEmptyMessage(5);
					else
						handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}					
			}	
		}.start();
		//定时线程
		new Timer().schedule(new TimerTask() {            
			@Override  
			public void run() {  
				 if (getActivity() == null) {  
				        return;  
				    }
				Message msg = new Message();
				msg.what = 2;            	
				handler.sendMessage(msg);
			}  
		}, 0,100); //两个参数第一个是延时多久才执行，第二个是隔多久执行一次 

		return view;
	}	
	//初始化界面
	private void inintview() {

		//给ListView末尾添加一个记载更多的布局
		View footView = LayoutInflater.from(getActivity()).inflate(R.layout.add_more_layout, null, false);  
		TextView addMoreView = (TextView) footView.findViewById(R.id.addMoreView);
		addMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View fv) {
				addMoreData(sortId , distanceId,sportstyleId);
				resetID();
			}
		});


		comeback_main = (ImageView) view.findViewById(R.id.comeback_mainpage_from_second);
		search_image = (ImageView) view.findViewById(R.id.search_nearby);

		all_sort = (LinearLayout) view.findViewById(R.id.all_sort);
		distance_sort = (LinearLayout) view.findViewById(R.id.distance_sort);
		people_like_sort = (LinearLayout) view.findViewById(R.id.people_like_sort);

		all_sort_text = (TextView) view.findViewById(R.id.all_sort_text);
		distance_sort_text = (TextView) view.findViewById(R.id.distance_sort_text);
		people_like_sort_text = (TextView) view.findViewById(R.id.people_like_sort_text);

		all_sort_image = (ImageView) view.findViewById(R.id.all_sort_image);
		distance_sort_image = (ImageView) view.findViewById(R.id.distance_sort_iamge);
		people_like_sort_image = (ImageView) view.findViewById(R.id.people_like_sort_image);

		list_nearby = (ListView) view.findViewById(R.id.show_the_sort);

		//为listView添加footview
		list_nearby.addFooterView(footView);

		//分类响应时间监听
		all_sort.setOnClickListener(new sortclicklistener());
		distance_sort.setOnClickListener(new sortclicklistener());
		people_like_sort.setOnClickListener(new sortclicklistener());

		Log.v("dfdg","sdfdsfds");
		//适配ListView
		list_nearby.setAdapter(adapter = new ListViewAdapter(getActivity(), Data));
		Log.v("哈哈哈哈哈fdg","sdfdsfds");

		list_nearby.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3) {

				int SportPlaceId = Data.get(position).getSportplaceId();
				Intent intent = new Intent(getActivity(),SportsPlaceDetail.class);
				Bundle bundle = new Bundle();
				String name = "comefromSeconPage";
				bundle.putString("name",name);
				bundle.putInt("SportPlaceId",SportPlaceId);
				intent.putExtras(bundle);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}

	protected void resetID() {
		//重置当前的类型ID
		sortId = -1;
		distanceId = -1;
		sportstyleId = -1;		
	}
	protected void addMoreData(final int sortId2, final int distanceId2, final int sportstyleId2) {
		//开一个线程进行网络数据加载
		new Thread(){
			public void run(){
				
				//定义临时容器
				ArrayList<SportPlace> MoreData = new ArrayList<SportPlace>();
				MoreData = httpgetjson.getMoreDataData(cityId,sortId2, distanceId2, sportstyleId2, addMoreCount);		
				if(MoreData != null)
					for(int i = 0; i<MoreData.size(); i++)
						Data.add(MoreData.get(i));
				handler.sendEmptyMessage(3);
				Log.v("dsfsdfs","发送消息成功");
				}
		}.start();
		
	}
	
	//获取Data数据

	class sortclicklistener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			//把加载更多的总条数重置为10
			addMoreCount = 10;
			switch (v.getId()) {
			case R.id.all_sort:
				dialog_int = 0;
				dialog = new Dialog(getActivity());
				Window dialogWindow = dialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();//获取对话框的参数
				//获取屏幕大小
				WindowManager windowmanager = getActivity().getWindowManager();
				Display display = windowmanager.getDefaultDisplay();
				DisplayMetrics displayMetrics = new DisplayMetrics();
				display.getMetrics(displayMetrics);
				//去除标题栏
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//自定义对话框layout
				dialog.setContentView(R.layout.dialogitem_allsort);
				////分类对话框里面的view
				inintall_sortview(dialog);
				// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
				lp.height = displayMetrics.heightPixels/2;
				lp.width = displayMetrics.widthPixels;
				dialogWindow.setAttributes(lp);
				dialog.show();
				//监听dialog上所有的view
				all_sort_basktball_text.setOnClickListener(new AllsortClickListener());
				all_sort_running_text.setOnClickListener(new AllsortClickListener());
				all_sort_nettennis_text.setOnClickListener(new AllsortClickListener());
				all_sort_football_text.setOnClickListener(new AllsortClickListener());
				all_sort_swim_text.setOnClickListener(new AllsortClickListener());
				all_sort_gym_text.setOnClickListener(new AllsortClickListener());
				all_sort_tableball_text.setOnClickListener(new AllsortClickListener());
				all_sort_part_relax_text.setOnClickListener(new AllsortClickListener());
				all_sort_volleyball_text.setOnClickListener(new AllsortClickListener());
				all_sort_climb_text.setOnClickListener(new AllsortClickListener());
				all_sort_part_ride_text.setOnClickListener(new AllsortClickListener());
				all_sort_other_text.setOnClickListener(new AllsortClickListener());
				break;
			case R.id.distance_sort:
				dialog_int = 1;
				//Toast.makeText(getActivity(), "距离分类", 1000).show();
				dialog1 = new Dialog(getActivity());
				Window dialogWindow1 = dialog1.getWindow();
				WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();//获取对话框的参数
				//获取屏幕大小
				WindowManager windowmanager1 = getActivity().getWindowManager();
				Display display1 = windowmanager1.getDefaultDisplay();
				DisplayMetrics displayMetrics1 = new DisplayMetrics();
				display1.getMetrics(displayMetrics1);
				//去除标题栏
				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//自定义对话框layout
				dialog1.setContentView(R.layout.dialogitem_distancesort);
				////分类对话框里面的view
				inint_distance_sort_view(dialog1);
				// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
				lp1.height = displayMetrics1.heightPixels/2;
				lp1.width = displayMetrics1.widthPixels;
				dialogWindow1.setAttributes(lp1);
				dialog1.show();
				//监听dialog上所有的view
				distance_sort_allcity_text.setOnClickListener(new Distance_sort_ClickListener());
				distance_sort_500m_text.setOnClickListener(new Distance_sort_ClickListener());
				distance_sort_3Km_text.setOnClickListener(new Distance_sort_ClickListener());
				distance_sort_5Km_text.setOnClickListener(new Distance_sort_ClickListener());
				break;
			case R.id.people_like_sort:
				dialog_int = 2;
				//Toast.makeText(getActivity(), "评分分类", 1000).show();
				dialog2 = new Dialog(getActivity());
				Window dialogWindow2 = dialog2.getWindow();
				WindowManager.LayoutParams lp2 = dialogWindow2.getAttributes();//获取对话框的参数
				//获取屏幕大小
				WindowManager windowmanager2 = getActivity().getWindowManager();
				Display display2 = windowmanager2.getDefaultDisplay();
				DisplayMetrics displayMetrics2 = new DisplayMetrics();
				display2.getMetrics(displayMetrics2);
				//去除标题栏
				dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//自定义对话框layout
				dialog2.setContentView(R.layout.dialogitem_peoplelikesort);
				////分类对话框里面的view
				inint_people_like_sort_view(dialog2);
				// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
				lp2.height = displayMetrics2.heightPixels/2;
				lp2.width = displayMetrics2.widthPixels;
				dialogWindow2.setAttributes(lp2);
				dialog2.show();
				//监听dialog上所有的view
				peoplelike_sort_hardsport_text.setOnClickListener(new PeopleLike_sort_ClickListener());
				peoplelike_sort_quitesport_text.setOnClickListener(new PeopleLike_sort_ClickListener());
				peoplelike_sort_youngsport_text.setOnClickListener(new PeopleLike_sort_ClickListener());
				peoplelike_sort_build_bodysport_text.setOnClickListener(new PeopleLike_sort_ClickListener());
				peoplelike_besidesort_sport_text.setOnClickListener(new PeopleLike_sort_ClickListener());
				break;
			}	
		}			
	}
	private void inintall_sortview(Dialog dialog) {

		all_sort_basktball_text = (TextView) dialog.findViewById(R.id.all_sort_basktball_text);
		all_sort_running_text = (TextView) dialog.findViewById(R.id.all_sort_running_text);
		all_sort_nettennis_text = (TextView) dialog.findViewById(R.id.all_sort_nettennis_text);
		all_sort_football_text = (TextView) dialog.findViewById(R.id.all_sort_football_text);
		all_sort_swim_text = (TextView) dialog.findViewById(R.id.all_sort_swim_text);
		all_sort_gym_text = (TextView) dialog.findViewById(R.id.all_sort_gym_text);
		all_sort_tableball_text = (TextView) dialog.findViewById(R.id.all_sort_tableball_text);
		all_sort_part_relax_text = (TextView) dialog.findViewById(R.id.all_sort_part_relax_text);
		all_sort_volleyball_text = (TextView) dialog.findViewById(R.id.all_sort_volleyball_text);
		all_sort_climb_text = (TextView) dialog.findViewById(R.id.all_sort_climb_text);
		all_sort_part_ride_text = (TextView) dialog.findViewById(R.id.all_sort_part_ride_text);
		all_sort_other_text = (TextView) dialog.findViewById(R.id.all_sort_other_text);
	}

	public void inint_people_like_sort_view(Dialog dialog) {
		peoplelike_sort_hardsport_text = (TextView) dialog.findViewById(R.id.peoplelike_sort_hardsport_text);
		peoplelike_sort_quitesport_text = (TextView) dialog.findViewById(R.id.peoplelike_sort_quitesport_text);
		peoplelike_sort_youngsport_text = (TextView) dialog.findViewById(R.id.peoplelike_sort_youngsport_text);
		peoplelike_sort_build_bodysport_text = (TextView) dialog.findViewById(R.id.peoplelike_sort_build_bodysport_text);
		peoplelike_besidesort_sport_text = (TextView) dialog.findViewById(R.id.peoplelike_besidesort_sport_text);
	}
	public void inint_distance_sort_view(Dialog dialog) {

		distance_sort_allcity_text = (TextView) dialog.findViewById(R.id.distance_sort_allcity_text);
		distance_sort_500m_text = (TextView) dialog.findViewById(R.id.distance_sort_500m_text);
		distance_sort_3Km_text = (TextView) dialog.findViewById(R.id.distance_sort_3Km_text);
		distance_sort_5Km_text = (TextView) dialog.findViewById(R.id.distance_sort_5Km_text);

	}
	class AllsortClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.all_sort_basktball_text:
				senttitle("篮球", 101, -1, -1);
				sortId = 101;
				break;
			case R.id.all_sort_running_text:
				senttitle("跑步", 102, -1, -1);
				sortId = 102;
				break;
			case R.id.all_sort_nettennis_text:
				senttitle("羽毛球", 103, -1, -1);
				sortId = 103;
				break;
			case R.id.all_sort_football_text:
				senttitle("足球", 104, -1, -1);
				sortId = 104;
				break;
			case R.id.all_sort_swim_text:
				senttitle("游泳", 105, -1, -1);
				sortId = 105;
				break;
			case R.id.all_sort_gym_text:
				senttitle("健身房", 106, -1, -1);
				sortId = 106;
				break;
			case R.id.all_sort_tableball_text:
				senttitle("乒乓球", 107, -1, -1);
				sortId = 107;
				break;
			case R.id.all_sort_part_relax_text:
				senttitle("公园休闲", 108, -1, -1);
				sortId = 108;
				break;
			case R.id.all_sort_volleyball_text:
				senttitle("排球", 109, -1, -1);
				sortId = 109;
				break;
			case R.id.all_sort_climb_text:
				senttitle("爬山", 110, -1, -1);
				sortId = 110;
				break;
			case R.id.all_sort_part_ride_text:
				senttitle("骑行", 111, -1, -1);
				sortId = 111;
				break;
			case R.id.all_sort_other_text:
				senttitle("其他", 112, -1, -1);
				sortId = 112;
				break;
			}
		}
	}
	class Distance_sort_ClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.distance_sort_allcity_text:
				distanceId = 11;
				senttitle("allcity", -1, 11, -1);
				break;
			case R.id.distance_sort_500m_text:
				distanceId = 12;
				senttitle("500m", -1, 12, -1);
				break;
			case R.id.distance_sort_3Km_text:
				distanceId = 13;
				senttitle("3km", -1, 13, -1);
				break;
			case R.id.distance_sort_5Km_text:
				distanceId = 14;
				senttitle("5km", -1, 14, -1);
				break;
			}
		}
	}
	class PeopleLike_sort_ClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.peoplelike_sort_hardsport_text:
				sportstyleId = SportPlaceDetailInformation.type_紧张激烈;
				senttitle("紧张激烈", -1, -1, sportstyleId);
				break;
			case R.id.peoplelike_sort_quitesport_text:
				sportstyleId = SportPlaceDetailInformation.type_安静闲适;
				senttitle("安静闲时", -1, -1, sportstyleId);
				break;
			case R.id.peoplelike_sort_youngsport_text:
				sportstyleId = SportPlaceDetailInformation.type_年轻专属;
				senttitle("年轻专属", -1, -1, sportstyleId);
				break;
			case R.id.peoplelike_sort_build_bodysport_text:
				sportstyleId = SportPlaceDetailInformation.type_强身健体;
				senttitle("强身健体", -1, -1, sportstyleId);
				break;
			case R.id.peoplelike_besidesort_sport_text:
				sportstyleId = SportPlaceDetailInformation.type_野外探索;
				senttitle("野外探索", -1, -1, sportstyleId);	
				break;
			}
		}
	}	
	public void senttitle(String string, final int sort_id, final int distance_id, final int sportstyle_id) {			
		//默认city_id 为一
		new Thread(){
			public void run(){
				try {
					//处理耗时网络加载json和json解析
					//模拟耗时
					handler.sendEmptyMessage(4);//加载中的动画
					Thread.sleep(2000);
					Data = httpgetjson.getallData(cityId, sort_id, distance_id, sportstyle_id);
					//处理网络异常的消息处理
					if(Data == null)
						handler.sendEmptyMessage(5);
					else handler.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
				}					
			}	
		}.start();
		//			HttpGetSportPlaceJson httpgetsportplace1 = new HttpGetSportPlaceJson();
		//			Data = httpgetjson.getallData(city_id,sort_id, distance_id, sportstyle_id);
		//			adapter.notifyDataSetChanged();
		if(dialog_int == 0){
			dialog.dismiss();
			resetother();
			all_sort_text.setTextColor(Color.RED);
			all_sort_text.setText(string);
			all_sort_image.setImageResource(R.drawable.xiala);
		}
		if(dialog_int == 1){
			dialog1.dismiss();
			resetother();
			distance_sort_text.setTextColor(Color.RED);
			distance_sort_text.setText(string);
			distance_sort_image.setImageResource(R.drawable.xiala);
		}
		if(dialog_int == 2){
			dialog2.dismiss();
			resetother();
			people_like_sort_text.setTextColor(Color.RED);
			people_like_sort_text.setText(string);
			people_like_sort_image.setImageResource(R.drawable.xiala);
		}
		Log.v("更新结束了","这里是"+string);
	}
	private void resetother() {

		all_sort_text.setText("所有分类");
		distance_sort_text.setText("距离优先");
		people_like_sort_text.setText("运动风");
		all_sort_text.setTextColor(Color.BLACK);
		distance_sort_text.setTextColor(Color.BLACK);
		people_like_sort_text.setTextColor(Color.BLACK);
		all_sort_image.setImageResource(R.drawable.shouqi);
		distance_sort_image.setImageResource(R.drawable.shouqi);
		people_like_sort_image.setImageResource(R.drawable.shouqi);
	}
}

