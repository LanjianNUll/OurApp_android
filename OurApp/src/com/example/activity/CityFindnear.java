package com.example.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.adapter.SortAdapter;
import com.example.bean.City;
import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.example.unti.CharacterParser;
import com.example.unti.PinyinComparator;
import com.example.viewself.ClearEditText;
import com.example.viewself.SideBar;
import com.google.gson.Gson;

public class CityFindnear extends Activity {
	
	private ListView sortListView;
	private SideBar sideBar;
	private TextView city_name;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	
	/** * 汉字转换成拼音的类*/
	private CharacterParser characterParser;
	private List<City> CityList;
	
	/*** 根据拼音来排列ListView里面的数据类 */ 
	private PinyinComparator pinyinComparator;
	//返回主页
	private ImageView cityfindnear_activity_top_image;
	
	private TextView item_cityname;
	//定位城市名
	private String city;
	//用户
	private UserDetailInfo user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏系统ActionBar
		setContentView(R.layout.city_find_near);
		initview();
	}
	

	private void initview() {
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		Log.v("here ","i am here11111 ");
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar)findViewById(R.id.sidrbar);
		Log.v("here ","i am here222222 ");
		city_name = (TextView)findViewById(R.id.city_name);
		Log.v("here ","i am here222222 ");
		
		sideBar.setTextView(city_name);
		
		//设置右侧触摸监听,根据首字母定位
		sideBar.setOnTouchingLetterChangedListener(new com.example.viewself.SideBar.OnTouchingLetterChangedListener() {			
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
			}
		});
		sortListView = (ListView)findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				
				//view的text获取城市名
				item_cityname = (TextView) v.findViewById(R.id.item_cityname);
				String city_item = item_cityname.getText().toString();
				
				//Toast.makeText(CityFindnear.this, city_item.toString(), 1000).show();
				/**修改User对象中的定位地址*/
				//获取用户对象
				SharedPreferences preferences = getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
				String userJson=preferences.getString("userDetail", "defaultname");
				Gson gson = new Gson();
				
				user = gson.fromJson(userJson, UserDetailInfo.class); 
				//将修改后的值封装到对象user_to_json
				User  user_to_json = new User();
				user_to_json.setUserId(user.getUserId());
				user_to_json.setUsername(user.getUsername());
				user_to_json.setMy_user_sign(user.getMy_user_sign());
				user_to_json.setSexId(user.getSexId());
				user_to_json.setPassword(user.getPassWord());
				user_to_json.setUser_state(user.getUser_state());
				user_to_json.setLocation_lasetime_login(city_item);
				
				String toJson = gson.toJson(user_to_json);
				SharedPreferences preferences_to=getSharedPreferences("userDetailFile",Context.MODE_PRIVATE);
				Editor editor=preferences.edit();
				editor.putString("userDetail", toJson);
				editor.commit();
				
				
				Intent intent = new Intent(CityFindnear.this , MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 0);
				bundle.putString("city_name", city_item);
				intent.putExtras(bundle);
				CityFindnear.this.startActivity(intent);
				CityFindnear.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
	});
		CityList = filledData(getResources().getStringArray(R.array.city));
	
		// 根据a-z进行排序源数据,sort()根据比较器来比较list
		Collections.sort(CityList, pinyinComparator);//自定义比较器
		adapter = new SortAdapter(CityFindnear.this, CityList);
		sortListView.setAdapter(adapter);
	
		mClearEditText = (ClearEditText)  findViewById(R.id.cityfindnear_activity_top_search_text);
		
		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});	
		//返回主页
		cityfindnear_activity_top_image = (ImageView) findViewById(R.id.cityfindnear_activity_top_image);
		
		cityfindnear_activity_top_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(CityFindnear.this , MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem",0);
				intent.putExtras(bundle);
				CityFindnear.this.startActivity(intent);
				CityFindnear.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		});	
	}

	protected void filterData(String filterStr) {
		List<City> filterDateList = new ArrayList<City>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = CityList;
		}else{
			
			//clear等同也removeall
			filterDateList.clear();
			for(City cityModel : CityList){
				String name = cityModel.getCity_name();
				if(name.indexOf(filterStr.toString()) != -1 || 
						characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(cityModel);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		//更新ListView
		adapter.updateListView(filterDateList);
	}		

	private List<City> filledData(String[] stringArray) {
		
		List<City> mSortList = new ArrayList<City>();
		
		for(int i=0; i<stringArray.length; i++){
			City citymodel = new City();
			citymodel.setCity_name(stringArray[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(stringArray[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				citymodel.setSortLetters(sortString.toUpperCase());
			}else{
				citymodel.setSortLetters("#");
			}		
			mSortList.add(citymodel);
		}
		return mSortList;
	}
	
	//屏蔽返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//跳回MaimActivity
			Intent intent = new Intent(CityFindnear.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem",0);
			intent.putExtras(bundle);
			CityFindnear.this.startActivity(intent);
			CityFindnear.this.finish();
			overridePendingTransition(R.drawable.interface_jump_in,
					R.drawable.interface_jump_out);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}