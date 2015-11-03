package com.example.activity;

import com.example.bean.ChatMessage;
import com.example.bean.User;
import com.example.dao.MyFriendGroupDB;
import com.example.httpunit.HttpDoUserMsg;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.example.unti.SendMsgTask;
import com.google.gson.Gson;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScanAndAddfriendActivity extends Activity {
	private TextView Addfriendsname, AddFirendState, AddFriendSex, AddFriendSign, AddFriendLocation;
	private TextView failScanText;
	private Button AddFriendBtn;
	//ɨ�赽�ĺ���
	private User user;
	private ImageView AddfriendComeback;
	private LinearLayout showFailInf;
	private RelativeLayout showSucesssInf;
	private HttpDoUserMsg httpDoUSermsg = new HttpDoUserMsg();
	//��ǰ�û��Լ���id
	private int userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_friends_layout);
		
		//ȡ����ǰ�û���userId
		SharedPreferences shpf = getSharedPreferences("user", Context.MODE_PRIVATE);
		String ugs = shpf.getString("userJson", null);
		Gson gso = new Gson();
		User u = new User();
		u = gso.fromJson(ugs, User.class);
		userId = u.getUserId();
		
		//�Ӻ��ѵİ�ť
		AddFriendBtn = (Button) findViewById(R.id.AddFriendBtn);
		AddFriendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//��ǰ�û�id   ��ӵ��û�id
				new AddFriendsTask().execute(userId,user.getUserId());
				//��������ӵ��б�
				MyFriendGroupDB mdb = new MyFriendGroupDB(ScanAndAddfriendActivity.this);
				mdb.AddFriend(user);
				//��Ӻ���ͨ���ٶ�������
				addfriendTask();
			}
		});
		//��ʾʧ�ܵĽ���ͳɹ��Ľ���
		
		showFailInf = (LinearLayout) findViewById(R.id.showFailInf);
		showSucesssInf = (RelativeLayout) findViewById(R.id.showSucesssInf);
		showFailInf.setVisibility(View.INVISIBLE);
		showSucesssInf.setVisibility(View.VISIBLE);
		AddfriendComeback = (ImageView) findViewById(R.id.AddfriendComeback);
		AddfriendComeback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ScanAndAddfriendActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 3);
				intent.putExtras(bundle);
				ScanAndAddfriendActivity.this.startActivity(intent);
				ScanAndAddfriendActivity.this.finish();
				
			}
		});
		Intent intent=new Intent(ScanAndAddfriendActivity.this, CaptureActivity.class);
		ScanAndAddfriendActivity.this.startActivityForResult(intent,0);
	}
	private void addfriendTask() {
		//������Ӻ��ѵ���Ϣ�Ĳ���
		//�õ���ǰ�û�����Ϣ
		SharedPreferences sh = getSharedPreferences("user", Context.MODE_PRIVATE);
		String gstr = sh.getString("userJson", null);
		Gson gson = new Gson();
		User us = gson.fromJson(gstr, User.class);
		int myUserId = us.getUserId();
		String myName = us.getUsername();
		//��ӵĺ�����Ϣ
		int FriendId = user.getUserId();
		String FriendName = user.getUsername();
		//������Ϣ����
		String MsgContent = "@#$addFriend";
		//��װ
		ChatMessage cMsg = new ChatMessage();
		cMsg.setChatMsgContent(MsgContent);
		cMsg.setFromUserId(myUserId);
		cMsg.setFromUserName(myName);
		cMsg.setToUserId(FriendId);
		cMsg.setToUserName(FriendName);
		//������Ϣ
		new SendMsgTask(new Gson().toJson(cMsg)).send();
	}
	private void initview() {
		Addfriendsname = (TextView) findViewById(R.id.Addfriendsname);
		AddFirendState = (TextView) findViewById(R.id.AddFirendState);
		AddFriendSex = (TextView) findViewById(R.id.AddFriendSex);
		AddFriendSign = (TextView) findViewById(R.id.AddFriendSign);
		AddFriendLocation = (TextView) findViewById(R.id.AddFriendLocation);
	}
	//ɨ��Ļش�
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==0&&data != null) {
			 initview();
			 Gson gson = new Gson();
			 Bundle bundle = data.getExtras();
			 String result=bundle.getString("result");
			 if (!TextUtils.isEmpty(result)) {
				 try{
					 user = gson.fromJson(result, User.class);
					 } catch (Exception e) {
						 failScanText = (TextView) findViewById(R.id.failScanText);
						 failScanText.setText("�ף���ɨ��Ĳ������ǵĶ�ά��!!!"+result);
						 showFailInf.setVisibility(View.VISIBLE);
						 showSucesssInf.setVisibility(View.INVISIBLE);
					}
				 if(user != null){
					 Addfriendsname.setText(user.getUsername());
					 String userState = null;
					 if(user.getUser_state() == User.state_ð��)
						 userState = "ð��";
					 if(user.getUser_state() == User.state_լ��)
						 userState = "լ��";
					 if(user.getUser_state() == User.state_��Ծ)
						 userState = "��Ծ";
					 if(user.getUser_state() == User.state_�˶�ֿ��)
						 userState = "�˶�ֿ��";
					 if(user.getUser_state() == User.state_�˶���)
						 userState = "�˶���";
					 if(user.getUser_state() == User.state_�˶�����)
						 userState = "�˶�����";
					 AddFirendState.setText(userState);
					 String sexStr = null;
					 if(user.getSexId() == User.sex_man)
						 sexStr = "��";
					 if(user.getSexId() == User.sex_women)
						 sexStr = "Ů";
					 if(user.getSexId() == User.sex_define)
						 sexStr = "����";
					 AddFriendSex.setText(sexStr);
					 AddFriendSign.setText(user.getMy_user_sign());
					 AddFriendLocation.setText(user.getLocation_lasetime_login()); 
					 //��ʾɨ��ɹ��Ľ���
					 showFailInf.setVisibility(View.INVISIBLE);
					 showSucesssInf.setVisibility(View.VISIBLE);
				 }else{
					//��ʾʧ�ܵĽ���
					 showFailInf.setVisibility(View.VISIBLE);
					 showSucesssInf.setVisibility(View.INVISIBLE);
				 }
			 } else {
				 //��ʾʧ�ܵĽ���
				 showFailInf.setVisibility(View.VISIBLE);
				 showSucesssInf.setVisibility(View.INVISIBLE);
			}
		}else{
			 //��ʾʧ�ܵĽ���
			 showFailInf.setVisibility(View.VISIBLE);
			 showSucesssInf.setVisibility(View.INVISIBLE);
		}
		
	}
	class AddFriendsTask extends AsyncTask<Integer, Integer, Integer>{
		
		@Override
		protected Integer doInBackground(Integer... arg0) {
			//@params ��ǰ�û�id   ��ӵ��û�id
			httpDoUSermsg.addFriend(arg0[0], arg0[1]);
			
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			//��ӳɹ�
			Toast.makeText(ScanAndAddfriendActivity.this, "��Ӻ��ѳɹ�", 1000).show();
			Intent intent = new Intent(ScanAndAddfriendActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem", 3);
			intent.putExtras(bundle);
			ScanAndAddfriendActivity.this.startActivity(intent);
			ScanAndAddfriendActivity.this.finish();
			super.onPostExecute(result);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//����MaimActivity
			Intent intent = new Intent(ScanAndAddfriendActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem", 3);
			intent.putExtras(bundle);
			ScanAndAddfriendActivity.this.startActivity(intent);
			ScanAndAddfriendActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
