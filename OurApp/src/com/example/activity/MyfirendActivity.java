package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.MyFriendLisViewAdapter;
import com.example.bean.User;
import com.example.dao.MyFriendGroupDB;
import com.example.httpunit.HttpDoUserMsg;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.example.unti.SharePreferenceUtil;
import com.google.gson.Gson;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyfirendActivity extends Activity {
	
	private TextView edit_friend;
	private RelativeLayout mferrorpage, mfloading;
	private ImageView mfjiazai_image, myfriend_top_comeback;
	private TextView mfjiazai_text;
	private ListView myfriend_listView;
	private List<User> myFriend_Group = new ArrayList<User>();
	private List<User> tempMyFriendGroup = new ArrayList<User>();
	private HttpDoUserMsg httpDoUserMsg;
	private AnimationDrawable anim;
	private MyFriendLisViewAdapter adapter;
	private int userId;
	private boolean isOnEidt = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myfriend_layout);
		initview();
	}
	private void initview() {
		//获取用户的userid
		SharedPreferences sh = getSharedPreferences("user", Context.MODE_PRIVATE);
		String gstr = sh.getString("userJson", null);
		Gson gs = new Gson();
		User user = gs.fromJson(gstr, User.class);
		userId = user.getUserId();
		
		edit_friend = (TextView) findViewById(R.id.edit_friend);
		mferrorpage = (RelativeLayout) findViewById(R.id.mferrorpage);
		mfloading = (RelativeLayout) findViewById(R.id.mfloading);
		mfjiazai_text = (TextView) findViewById(R.id.mfjiazai_text);
		myfriend_top_comeback = (ImageView) findViewById(R.id.myfriend_top_comeback);
		mfjiazai_image = (ImageView) findViewById(R.id.mfjiazai_image);
		anim = (AnimationDrawable) mfjiazai_image.getBackground();
		myfriend_listView = (ListView) findViewById(R.id.myfriend_listView);
		//网络交互
		new GetFrindgroupTask().execute(userId);
		
		myfriend_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, final int position,
					long arg3) {
				Log.v("这里是onItemClick","success");
				//点击进入聊天
				Intent intent = new Intent(MyfirendActivity.this, ChattingActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("userName", myFriend_Group.get(position).getUsername());
				bundle.putInt("userId", myFriend_Group.get(position).getUserId());
				intent.putExtras(bundle);
				MyfirendActivity.this.startActivity(intent);
				MyfirendActivity.this.finish();
			}
		});
		
		edit_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isOnEidt){
					if(myFriend_Group != null){
						Log.v("删除后的好友数",""+myFriend_Group.size());
						myfriend_listView.setAdapter(adapter = new MyFriendLisViewAdapter(MyfirendActivity.this, myFriend_Group, 1));
					}
					edit_friend.setText("完成 ");
					isOnEidt = false;
				}else{
					myfriend_listView.setAdapter(adapter = new MyFriendLisViewAdapter(MyfirendActivity.this, myFriend_Group, 0));
					edit_friend.setText("编辑");
						isOnEidt = true;
						new DeleteFriendTask().execute();
				}
			}
		});
		myfriend_top_comeback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MyfirendActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 3);
				intent.putExtras(bundle);
				MyfirendActivity.this.startActivity(intent);
				MyfirendActivity.this.finish();
			}
		});
	}
	class DeleteFriendTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			httpDoUserMsg = new HttpDoUserMsg();
			httpDoUserMsg.afterDeteFriendGroup(myFriend_Group);
			//更新好友列表
			MyFriendGroupDB mdb = new MyFriendGroupDB(MyfirendActivity.this);
			mdb.delete();
			mdb.AddFriendGroup(myFriend_Group);
			return null;
		}
	}
	class GetFrindgroupTask extends AsyncTask<Integer, Integer, Integer>{
		
		@Override
		protected void onPreExecute() {
			mfloading.setVisibility(View.VISIBLE);
			anim.start();
			mferrorpage.setVisibility(View.GONE);
			myfriend_listView.setVisibility(View.GONE);
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Integer... arg0) {
			httpDoUserMsg = new HttpDoUserMsg();
			MyFriendGroupDB fDB = new MyFriendGroupDB(MyfirendActivity.this);
			ArrayList<User> DbGroup = new ArrayList<User>();
			myFriend_Group = httpDoUserMsg.getMyFriend_Group(arg0[0]);
			DbGroup = fDB.getFriendGroup();
			
//			//用一个临时容器来来保存未做编辑即删除操作前的好友列表
//			tempMyFriendGroup = myFriend_Group;
			//判断是否数据库中的好友列表是否最新
			if(myFriend_Group  != null && DbGroup.size() < myFriend_Group.size()){
				//存储好友列表
				//存储在数据库中
				fDB.AddFriendGroup(myFriend_Group);
//				//存储在SharePreference
//				SharePreferenceUtil spUtil = new SharePreferenceUtil(MyfirendActivity.this, "myFriensGroup");
//				int[] content = new int[myFriend_Group.size()];
//				for(int i = 0; i<myFriend_Group.size();i++)
//					content[i] = myFriend_Group.get(i).getUserId();
//				spUtil.writeTOSharePfInt("myFriendGroupIds", content);
				return 1;
			}
			else if(myFriend_Group  != null && DbGroup.size() >= myFriend_Group.size()){
				myFriend_Group = DbGroup;
				return 1;
			}else{
				return -1;
			}
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				myfriend_listView.setAdapter(adapter = new MyFriendLisViewAdapter(MyfirendActivity.this, myFriend_Group));				
				mfloading.setVisibility(View.GONE);
				mferrorpage.setVisibility(View.GONE);
				myfriend_listView.setVisibility(View.VISIBLE);
			}else{
				mfloading.setVisibility(View.GONE);
				mferrorpage.setVisibility(View.VISIBLE);
				mfjiazai_text.setText("网络异常或你还没有好友");
				myfriend_listView.setVisibility(View.GONE);
			}
			super.onPostExecute(result);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(MyfirendActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem", 3);
			intent.putExtras(bundle);
			MyfirendActivity.this.startActivity(intent);
			MyfirendActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
