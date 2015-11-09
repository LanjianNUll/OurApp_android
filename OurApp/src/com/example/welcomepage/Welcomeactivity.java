package com.example.welcomepage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.Application.Application;
import com.example.bean.User;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;

public class Welcomeactivity extends Activity {
	
	private Button btn;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcomepage);
		//������sharedpreferrnces���ö�������о��� û�о�����ȡֵ,
		//ע ����sharedpreferrnces�в��ܴ�ȡ����ת����json�ַ���
		SharedPreferences preferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
		String userJson=preferences.getString("userJson", null);
		Gson gson = new Gson();
		user = gson.fromJson(userJson, User.class); 
		if(user == null){
			//Log.v("������user�Ƿ�Ϊ��",user.toString()+"123");
			user = new User();
			user.setUsername("δ��¼");
			user.setPassword("123");
			user.setUser_state(-1);
			user.setUserId(-1);
			user.setSexId(-1);
			user.setMy_user_sign("����һ����ʲô��û����");
			user.setLocation_lasetime_login("��λ��...");
		}
		String userJson_to = gson.toJson(user);
		
		SharedPreferences preferences1=getSharedPreferences("user",Context.MODE_PRIVATE);
		Editor editor=preferences.edit();
		editor.putString("userJson", userJson_to);
		editor.commit();
		
		btn = (Button) findViewById(R.id.welcome_btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//��ҳ������ҳ
				Intent intents = new Intent(Welcomeactivity.this,MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", -1);
				//bundle.putSerializable("user", user);
				intents.putExtras(bundle);
				Welcomeactivity.this.startActivity(intents);
				Welcomeactivity.this.finish();				
			}	
		});	
		//��Ϣ����
		PushManager.startWork(Welcomeactivity.this,
	                PushConstants.LOGIN_TYPE_API_KEY,
	                Application.API_KEY);
		Log.v("sfsdfjsff","dsfdsf");		
	}
}
