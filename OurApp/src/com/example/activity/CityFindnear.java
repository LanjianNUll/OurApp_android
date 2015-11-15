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
	
	/** * ����ת����ƴ������*/
	private CharacterParser characterParser;
	private List<City> CityList;
	
	/*** ����ƴ��������ListView����������� */ 
	private PinyinComparator pinyinComparator;
	//������ҳ
	private ImageView cityfindnear_activity_top_image;
	
	private TextView item_cityname;
	//��λ������
	private String city;
	//�û�
	private UserDetailInfo user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ����ϵͳActionBar
		setContentView(R.layout.city_find_near);
		initview();
	}
	

	private void initview() {
		//ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();
		Log.v("here ","i am here11111 ");
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar)findViewById(R.id.sidrbar);
		Log.v("here ","i am here222222 ");
		city_name = (TextView)findViewById(R.id.city_name);
		Log.v("here ","i am here222222 ");
		
		sideBar.setTextView(city_name);
		
		//�����Ҳഥ������,��������ĸ��λ
		sideBar.setOnTouchingLetterChangedListener(new com.example.viewself.SideBar.OnTouchingLetterChangedListener() {			
			@Override
			public void onTouchingLetterChanged(String s) {
				//����ĸ�״γ��ֵ�λ��
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
				
				//view��text��ȡ������
				item_cityname = (TextView) v.findViewById(R.id.item_cityname);
				String city_item = item_cityname.getText().toString();
				
				//Toast.makeText(CityFindnear.this, city_item.toString(), 1000).show();
				/**�޸�User�����еĶ�λ��ַ*/
				//��ȡ�û�����
				SharedPreferences preferences = getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
				String userJson=preferences.getString("userDetail", "defaultname");
				Gson gson = new Gson();
				
				user = gson.fromJson(userJson, UserDetailInfo.class); 
				//���޸ĺ��ֵ��װ������user_to_json
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
	
		// ����a-z��������Դ����,sort()���ݱȽ������Ƚ�list
		Collections.sort(CityList, pinyinComparator);//�Զ���Ƚ���
		adapter = new SortAdapter(CityFindnear.this, CityList);
		sortListView.setAdapter(adapter);
	
		mClearEditText = (ClearEditText)  findViewById(R.id.cityfindnear_activity_top_search_text);
		
		//�������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});	
		//������ҳ
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
			
			//clear��ͬҲremoveall
			filterDateList.clear();
			for(City cityModel : CityList){
				String name = cityModel.getCity_name();
				if(name.indexOf(filterStr.toString()) != -1 || 
						characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(cityModel);
				}
			}
		}
		
		// ����a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		//����ListView
		adapter.updateListView(filterDateList);
	}		

	private List<City> filledData(String[] stringArray) {
		
		List<City> mSortList = new ArrayList<City>();
		
		for(int i=0; i<stringArray.length; i++){
			City citymodel = new City();
			citymodel.setCity_name(stringArray[i]);
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(stringArray[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if(sortString.matches("[A-Z]")){
				citymodel.setSortLetters(sortString.toUpperCase());
			}else{
				citymodel.setSortLetters("#");
			}		
			mSortList.add(citymodel);
		}
		return mSortList;
	}
	
	//���η��ؼ�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//����MaimActivity
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