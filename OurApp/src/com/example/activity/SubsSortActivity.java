package com.example.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import com.example.adapter.ListViewAdapter;
import com.example.bean.SportPlace;
import com.example.httpunit.HttpGetSportPlaceJson;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SubsSortActivity extends Activity {
    //top��view
	private ImageView come_back_mainpage;
	private TextView sub_sort_name;
	//sub_sort_listview
	private ListView sub_sort_listview;
	//����listview
	private ListViewAdapter adapter;
	//�����һ������
	 private ArrayList<SportPlace> Data= new ArrayList<SportPlace>();
	 //����id
	 private int sort_id;
	 //�����е�����
	 private TextView jiazai_text;
	 private ImageView jiazai_pic;
	 private RelativeLayout loading, errorpage;
	 //����id
	 private int cityId;
	 //ÿ�μ�����Ŀ
	 int addMoreCount = 10;
	 private Handler handler;
	 
	 //getfenlei
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ����ϵͳActionBar
		setContentView(R.layout.subsort_layout);
		initview();
	}
	private void initview() {
		SharedPreferences getCityId = getSharedPreferences("user", Context.MODE_PRIVATE);
		cityId = getCityId.getInt("city_id", -1);
		
		//���ظ����view
		View footView = LayoutInflater.from(SubsSortActivity.this).inflate(R.layout.add_more_layout, null);
		TextView addMoreView = (TextView) footView.findViewById(R.id.addMoreView);
		
		addMoreView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addMoreData();
			}
		});
		
		//���صĶ���
		jiazai_text = (TextView) findViewById(R.id.jiazai_text);
		jiazai_pic = (ImageView) findViewById(R.id.jiazai_pic);
		loading = (RelativeLayout) findViewById(R.id.loading);
		//�����쳣�Ľ���
		errorpage = (RelativeLayout) findViewById(R.id.errorpage);
		 //top��view
		come_back_mainpage = (ImageView) findViewById(R.id.come_back_mainpage);
		sub_sort_name = (TextView) findViewById(R.id.sub_sort_name);
		
		come_back_mainpage.setOnClickListener(new OnClickListener() {
			//������ҳ
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SubsSortActivity.this,MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", -1);
				intent.putExtras(bundle);
				SubsSortActivity.this.startActivity(intent);
				SubsSortActivity.this.finish();
			}
		});
		//��ȡ����Activity�д�������һ�������
		Bundle bundle = this.getIntent().getExtras();
		final String sort_title = bundle.getString("name");
		sort_id = bundle.getInt("sort");	
		sub_sort_name.setText(sort_title);
		
		handler = new Handler(){
			int i = 0 ;
			public void handleMessage(Message msg){
				
				switch (msg.what) {
				case 1:
					loading.setVisibility(View.GONE);
					sub_sort_listview.setAdapter(adapter = new ListViewAdapter(SubsSortActivity.this, Data));
					break;
				case 2 :
					jiazai_pic.setImageResource(R.drawable.jiazai00+i%6);
					if(i%2==0)
						jiazai_text.setText("������...");
					else if(i%3==0)
						jiazai_text.setText("������..");
					else
						jiazai_text.setText("������.");
					i++;
					if(i==100)
						i=0;
					break;
				case 3 :
					loading.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
					sub_sort_listview.setSelection(addMoreCount+1);
					addMoreCount = addMoreCount+10;
					break;
				case 4 :
					loading.setVisibility(View.GONE);
					errorpage.setVisibility(View.VISIBLE);
					break;
				}
			}
		};
		new Thread(){
			public void run(){
				try {
//					sleep(500);
					Log.v("��ʼ��ȡ��������","dsfsdfs");
					HttpGetSportPlaceJson httpgetsportplace = new HttpGetSportPlaceJson();
					//��ȡ����					
					Data = httpgetsportplace.getSortData(cityId, sort_id);
					if(Data == null)
						handler.sendEmptyMessage(4);
					else 
						handler.sendEmptyMessage(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		//��ʱ�߳�
		new Timer().schedule(new TimerTask() {            
            @Override  
            public void run() {  
            	Message msg = new Message();
				msg.what = 2;            	
				handler.sendMessage(msg);
            }  
        }, 0,100); //����������һ������ʱ��ò�ִ�У��ڶ����Ǹ����ִ��һ�� 
		
		
		//sub_sort_listview
		sub_sort_listview = (ListView) findViewById(R.id.sub_sort_listview);
		//���footView���ֵ�ListView
		sub_sort_listview.addFooterView(footView);
		sub_sort_listview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				int SportPlaceId = Data.get(position).getSportplaceId();
				Intent intent = new Intent(SubsSortActivity.this, SportsPlaceDetail.class);
				Bundle bundle = new Bundle();
				bundle.putString("name",sort_title);
				bundle.putInt("SportPlaceId", SportPlaceId);
				intent.putExtras(bundle);
				SubsSortActivity.this.startActivity(intent);
				SubsSortActivity.this.finish();	
			}
		});
	}
	protected void addMoreData() {
		new Thread(){
			public void run(){
				HttpGetSportPlaceJson httpgetjson = new HttpGetSportPlaceJson();
				//������ʱ����
				ArrayList<SportPlace> MoreData = new ArrayList<SportPlace>();
				MoreData = httpgetjson.getMoreDataData(cityId, sort_id, -1, -1, addMoreCount);		
				if(MoreData != null){
					for(int i = 0; i<MoreData.size(); i++)
						Data.add(MoreData.get(i));
				}
				handler.sendEmptyMessage(3);
				Log.v("dsfsdfs","������Ϣ�ɹ�");
				}
		}.start();	
	}	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//����MaimActivity
			Intent intent = new Intent(SubsSortActivity.this,MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem", 0);
			intent.putExtras(bundle);
			SubsSortActivity.this.startActivity(intent);
			SubsSortActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
