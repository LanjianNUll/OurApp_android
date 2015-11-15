package com.example.activity;

import java.util.Date;

import org.apache.http.auth.UsernamePasswordCredentials;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.example.httpunit.HttpDoUserMsg;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;

public class UserLoginActivity extends Activity {
	private ImageView comeback_image;
	//页头显示
	private TextView textView1;
	//用户登录
	private EditText user_name, password;
	private RelativeLayout login_body;
	private TextView passwordError, userNameError;
	//用户注册
	private TextView user_register_textView;
	//用户登录按钮
	private Button user_login;
	//登录用户名
	private String userName;
	//登录密码
	private String passWord;
	//user对象，用户基本信息
	private User user = new User();
	private HttpDoUserMsg httpDoUserMsg = new HttpDoUserMsg();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.userlogin);
		initview();
	}

	private void initview() {
		comeback_image = (ImageView) findViewById(R.id.comeback_image);
		textView1 = (TextView) findViewById(R.id.textView1);
		
		user_name = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);
		login_body = (RelativeLayout) findViewById(R.id.login_body);
		user_register_textView = (TextView) findViewById(R.id.user_register_textView);
		passwordError = (TextView) findViewById(R.id.passwordError);
		userNameError = (TextView) findViewById(R.id.userNameError);
		
		user_login = (Button) findViewById(R.id.user_login);
		
		user_register_textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//点击了注册textView的
				Intent intent = new Intent(UserLoginActivity.this, userResigterActivity.class);
				UserLoginActivity.this.startActivity(intent);
				UserLoginActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		});
		
		user_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					boolean isallowLogin = true;
					userName = user_name.getText().toString();
					passWord= password.getText().toString();
					if(passWord.equals("")){
						passwordError.setText("请输入密码");
						isallowLogin = false;
					}
					if(userName.equals("")){
						userNameError.setText("用户名不能为空");
						isallowLogin = false;
					}
					if(!userName.equals("")&& passWord.equals("")){
						userNameError.setText("");
						passwordError.setText("请输入密码");
						isallowLogin = false;
					}
					if(userName.equals("")&& !passWord.equals("")){
						userNameError.setText("用户名不能为空");
						passwordError.setText("");
						isallowLogin = false;
					}
					if(!passWord.equals("")&&!userName.equals("")&&isallowLogin){
						//验证用户名登录结果
						new CheckUserIsRight().execute(userName,passWord);
						passwordError.setText("");
						userNameError.setText("");
					}
			}
		});
		comeback_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 3);
//				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				UserLoginActivity.this.startActivity(intent);
				UserLoginActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		});
	}
	class CheckUserIsRight extends AsyncTask<String, Integer, Integer>{

		@Override
		protected void onPreExecute() {
			Toast.makeText(UserLoginActivity.this, "正在登录...",1000).show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			return httpDoUserMsg.checkUserIsRight(arg0[0],arg0[1]);
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result != 0 && result != -1 && result != -2){
				
				Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem",3 );
				bundle.putInt("userId", result);
				bundle.putInt("fromLogin", 1);
				intent.putExtras(bundle);
				UserLoginActivity.this.startActivity(intent);
				UserLoginActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
				//登录成功的处理
				Toast.makeText(UserLoginActivity.this,"登录成功", 1000).show();
			}else if(result == 0)
				//登录失败的处理
				Toast.makeText(UserLoginActivity.this,"登录失败", 1000).show();
			else if(result == -1)
				userNameError.setText("用户不存在");
				//Toast.makeText(UserLoginActivity.this, "用户不存在", 1000).show();
			else if(result == -2)
				passwordError.setText("用户名或密码错误");
				//Toast.makeText(UserLoginActivity.this, "密码错误", 1000).show();
			super.onPostExecute(result);
		}
	}
	//屏蔽返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//跳回MaimActivity
			Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem", 3);
			intent.putExtras(bundle);
			UserLoginActivity.this.startActivity(intent);
			UserLoginActivity.this.finish();
			overridePendingTransition(R.drawable.interface_jump_in,
					R.drawable.interface_jump_out);
		}
		return super.onKeyDown(keyCode, event);
	}
}
