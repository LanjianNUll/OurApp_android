package com.example.activity;

import java.util.Date;

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
	//用户注册
	private EditText user_register_name, user_register_password, user_register_password_again, user_register_connectphone;
	private RelativeLayout user_register_msg;
	private RadioGroup user_sex;
	private RadioButton userSexMan, userSexWoman, userSexHide;
	//用户选择按钮
	private Button user_login, user_register;
	//用户点击了登录按钮,默认为登录界面
	private boolean isclick_login = true;
	//是否在登录界面
	private boolean ison_login = true;
	//是否在注册界面
	private boolean ison_register = false;
	//用户点击了注册按钮，默认隐藏注册界面
	private Boolean isclick_register = false;
	//判断用户是否输入了用户名和密码且符合要求
	private boolean isright = false;
	//登录用户名
	private String userName;
	//登录密码
	private String passWord;
	//注册用户名
	private String username_reg;
	//注册密码
	private String password_reg;
	//重复密码
	private String password_reg_again;
	//user对象，用户基本信息
	private User user = new User();
	//用户详细信息，UserDetailInfo
	private UserDetailInfo userDInfo = new UserDetailInfo();
	//性别
	private int userSexId = User.sex_define;

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
		
		user_register_name = (EditText) findViewById(R.id.user_register_name);
		user_register_password = (EditText) findViewById(R.id.user_register_password);
		user_register_password_again = (EditText) findViewById(R.id.user_register_password_again);
		user_register_msg = (RelativeLayout) findViewById(R.id.user_register_msg);
		user_register_connectphone = (EditText) findViewById(R.id.user_register_connectphone);
		user_sex = (RadioGroup) findViewById(R.id.user_sex);
		userSexMan = (RadioButton) findViewById(R.id.userSexMan);
		userSexWoman = (RadioButton) findViewById(R.id.userSexWoman);
		userSexHide = (RadioButton) findViewById(R.id.userSexHide);
		
		user_login = (Button) findViewById(R.id.user_login);
		user_register = (Button) findViewById(R.id.user_register);
		
		//判断在登录还是注册界面
		if(isclick_login){
			login_body.setVisibility(View.VISIBLE);
			user_register_msg.setVisibility(View.GONE);
			ison_login = true;
			textView1.setText("用户登录".toString());
			user_login.setTextColor( Color.parseColor("#ff9900"));
		}
		if(isclick_register){
			login_body.setVisibility(View.GONE);
			user_register_msg.setVisibility(View.VISIBLE);
			ison_register = true;
			textView1.setText("用户注册".toString());
			user_register.setTextColor( Color.parseColor("#ff9900"));
		}
		
		user_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(ison_login){
					userName = user_name.getText().toString();
					passWord= password.getText().toString();
					//验证用户名登录结果
					new CheckUserIsRight().execute(userName,passWord);
					
				}	
				if(!ison_login){
					login_body.setVisibility(View.VISIBLE);
					user_register_msg.setVisibility(View.GONE);
					clearEditText();
					ison_login = true;
					ison_register = false;
					textView1.setText("用户登录".toString());
					user_login.setTextColor( Color.parseColor("#ff9900"));
					user_register.setTextColor( Color.parseColor("#404040"));
					}
			}
		});
		
		user_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ison_register){
					username_reg = user_register_name.getText().toString();
					password_reg = user_register_password.getText().toString();
					password_reg_again = user_register_password_again.getText().toString();
					
					if(password_reg != "" && password_reg.equals(password_reg_again)){
						
						//用户详细信息
						userDInfo.setUserId(0);
						userDInfo.setUser_state(User.state_冒泡);
						userDInfo.setSexId(userSexId);
						userDInfo.setConnectPhone(user_register_connectphone.getText()+"");
						userDInfo.setLeaveword(null);
						userDInfo.setRegisteTime(new Date());
						userDInfo.setUsername(username_reg);
						userDInfo.setPassWord(password_reg);
						userDInfo.setLocation_lasetime_login("宜春");
						userDInfo.setMy_user_sign("我就是我，颜色不一样的烟火");
						//发送注册消息给服务器
						Log.v("dfsdfs","wo dao ");
						new SentRegisterMsgTask().execute(userDInfo);
					}else{
						Toast.makeText(UserLoginActivity.this,"密码不能为空或两次输入的密码不一致", 1000).show();
					}					
				}		
				if(!ison_register){
					login_body.setVisibility(View.GONE);
					user_register_msg.setVisibility(View.VISIBLE);
					ison_register = true;
					ison_login = false;
					textView1.setText("用户注册".toString());
					user_register.setTextColor(Color.parseColor("#ff9900"));
					user_login.setTextColor(Color.parseColor("#404040"));
				}
			}
		});
		
		comeback_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				user.setUsername("用户未登录");
				Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 3);
//				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				UserLoginActivity.this.startActivity(intent);
				UserLoginActivity.this.finish();
			}
		});
		
		//性别单选布局的监听
		user_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup rv, int checkId) {
				if(checkId == userSexMan.getId()){
					userSexMan.setTextColor(Color.BLACK);
					userSexWoman.setTextColor(Color.parseColor("#cdc9c9"));
					userSexHide.setTextColor(Color.parseColor("#cdc9c9"));
					userSexId = User.sex_man;
				}
				if(checkId == userSexWoman.getId()){
					userSexWoman.setTextColor(Color.BLACK);
					userSexMan.setTextColor(Color.parseColor("#cdc9c9"));
					userSexHide.setTextColor(Color.parseColor("#cdc9c9"));
					userSexId = User.sex_women;
				}
				if(checkId == userSexHide.getId()){
					userSexHide.setTextColor(Color.BLACK);
					userSexMan.setTextColor(Color.parseColor("#cdc9c9"));
					userSexWoman.setTextColor(Color.parseColor("#cdc9c9"));
					userSexId = User.sex_define;
				}			
			}
		});
	}
    //清空EditText的数据
	protected void clearEditText() {
		user_name.setText("");
		password.setText("");
		user_register_name.setText("");
		user_register_password.setText("");
		user_register_password_again.setText("");
	}

	class SentRegisterMsgTask extends AsyncTask<UserDetailInfo, Integer, Integer>{
		
		@Override
		protected void onPreExecute() {
			//将注册按钮变为不可选
			user_register.setEnabled(false);
			Toast.makeText(UserLoginActivity.this, "注册中.....", 1000).show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(UserDetailInfo... arg0) {
			return httpDoUserMsg.sentUserDetailInfo(arg0[0]);
			 
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){//注册成功
				//注册成功后的处理
				Gson gson = new Gson();
				String userJson = gson.toJson(user);
				SharedPreferences preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
				Editor editor=preferences.edit();
				editor.putString("userJson", userJson);
				editor.commit();
				
				Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem",3 );
//				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				UserLoginActivity.this.startActivity(intent);
				UserLoginActivity.this.finish();
				Toast.makeText(UserLoginActivity.this, "注册成功", 1000).show();
			}else if(result == 0){
				Toast.makeText(UserLoginActivity.this, "注册失败", 1000).show();
			}else if(result == 2){
				Toast.makeText(UserLoginActivity.this, "用户名已存在", 1000).show();
			}else if(result == 3){
				Toast.makeText(UserLoginActivity.this, "用户名或密码为空", 1000).show();
			}
			//将注册按钮回复可选
			user_register.setEnabled(true);
			super.onPostExecute(result);
		}	
	}
	class CheckUserIsRight extends AsyncTask<String, Integer, Integer>{

		
		@Override
		protected void onPreExecute() {
			Toast.makeText(UserLoginActivity.this, "正在登录...",2000).show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			return httpDoUserMsg.checkUserIsRight(arg0[0],arg0[1]);
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				//登录成功的用户信息放在了SharedPreferences
				user = new User();
				user.setUsername(userName);
				user.setPassword(passWord);
				user.setUser_state(15);
				//用一个随机数
				user.setUserId((int) Math.round(Math.random()*100));
				user.setSexId(1);
				user.setMy_user_sign("我就是我，颜色不一样的烟火");
				user.setLocation_lasetime_login("宜春");
				
				Gson gson = new Gson();
				String userJson = gson.toJson(user);
				
				SharedPreferences preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
				Editor editor=preferences.edit();
				editor.putString("userJson", userJson);
				editor.commit();
				
				Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem",3 );
				intent.putExtras(bundle);
				UserLoginActivity.this.startActivity(intent);
				UserLoginActivity.this.finish();
				
				//登录成功的处理
				Toast.makeText(UserLoginActivity.this,"登录成功", 1000).show();
			}else if(result == 0)
				//登录失败的处理
				Toast.makeText(UserLoginActivity.this,"登录失败", 1000).show();
			else if(result == -1)
				Toast.makeText(UserLoginActivity.this, "用户不存在", 1000).show();
			else if(result == -2)
				Toast.makeText(UserLoginActivity.this, "密码错误", 1000).show();
			else if(result == -3)
				Toast.makeText(UserLoginActivity.this, "用户名或密码不能为空", 1000).show();
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
		}
		return super.onKeyDown(keyCode, event);
	}
}
