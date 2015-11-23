package com.example.welcomepage;


import java.util.Timer;
import java.util.TimerTask;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.Application.Application;

import com.example.bean.UserDetailInfo;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Welcomeactivity extends Activity {
	
	private Button btn;
	private UserDetailInfo user;
	private RelativeLayout welcomeRe;
	private ObjectAnimator fristanim;
	private boolean isStart = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcomepage);
		welcomeRe = (RelativeLayout) findViewById(R.id.welcomeRe);
		//������sharedpreferrnces���ö�������о��� û�о�����ȡֵ,
		//ע ����sharedpreferrnces�в��ܴ�ȡ����ת����json�ַ���
		
		SharedPreferences userFile = getSharedPreferences("userDetailFile",
				Context.MODE_PRIVATE);
		String userDetailJson = userFile.getString("userDetail", null);
		Gson gson = new Gson();
		user = gson.fromJson(userDetailJson, UserDetailInfo.class); 
		if(user == null){
			user = new UserDetailInfo();
			user.setUserId(-1);
			user.setUsername("�ο�1");
			user.setMy_user_sign("����һ����ʲô��û����");
		}
		String userJson_to = gson.toJson(user);
		Editor editor=userFile.edit();
		editor.putString("userDetail", userJson_to);
		editor.commit();
		
		btn = (Button) findViewById(R.id.welcome_btn);
		btn.setVisibility(View.GONE);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//ͨ����ť������
				isStart = false;
				//��ҳ������ҳ
				Intent intents = new Intent(Welcomeactivity.this,MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", -1);
				//bundle.putSerializable("user", user);
				intents.putExtras(bundle);
				Welcomeactivity.this.startActivity(intents);
				Welcomeactivity.this.finish();
				//������ת�Ķ���
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out); 
			}	
		});
		//��Ϣ����
		PushManager.startWork(Welcomeactivity.this,
	                PushConstants.LOGIN_TYPE_API_KEY,
	                Application.API_KEY);
		Log.v("sfsdfjsff","dsfdsf");
		btn.setVisibility(View.VISIBLE);
		//new ImageChageTask().execute();
		
//		Timer timer = new Timer(); 
//		TimerTask task = new TimerTask() {  
//		    @Override  
//		    public void run() {   
//		    	if(isStart){
//		    		//��ҳ������ҳ
//					Intent intents = new Intent(Welcomeactivity.this,MainActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putInt("CurrentItem", -1);
//					//bundle.putSerializable("user", user);
//					intents.putExtras(bundle);
//					Welcomeactivity.this.startActivity(intents);
//					Welcomeactivity.this.finish();
//					//������ת�Ķ���
//					overridePendingTransition(R.drawable.interface_jump_in,
//							R.drawable.interface_jump_out); 
//		    	}
//		     } 
//
//		 };
//		 timer.schedule(task, 3000); //3���// 
	}
	
	@SuppressLint("NewApi")
	private void StartAnim() {
//		fristanim = ObjectAnimator.ofFloat(fristWelcomeImage, 
//				 "scale", 500f, 0f);
//		AnimatorSet animSet = new AnimatorSet();
//		animSet.play(fristanim);
//		animSet.setStartDelay(2000);
//		animSet.setDuration(1000);
//		animSet.start();
		
	}
	class ImageChageTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			btn.setVisibility(View.VISIBLE);
			welcomeRe.setBackgroundResource(R.drawable.welcome);
			
			super.onPostExecute(result);
		}
		
	}
}
