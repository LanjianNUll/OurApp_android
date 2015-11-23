package com.example.activity;

import java.util.Date;
import java.util.regex.Pattern;

import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.example.httpunit.HttpDoUserMsg;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class userResigterActivity extends Activity {
	private ImageView comeback_image;
	//用户注册
	private EditText user_registerNname, user_register_password,
							user_register_password_again, user_register_connectphone;
	private TextView registerPasswordError, userRegisterNameError, 
								phoneNumberError, userPasswordIsNull;
	private RadioGroup user_sex;
	private RadioButton userSexMan, userSexWoman, userSexHide;
	private Button user_register;
	//注册用户名
	private String username_reg;
	//注册密码
	private String password_reg;
	//重复密码
	private String password_reg_again;
	//性别
	private int userSexId = User.sex_define;
	//用户详细信息，UserDetailInfo
	private UserDetailInfo userDInfo = new UserDetailInfo();
	private HttpDoUserMsg httpDoUserMsg = new HttpDoUserMsg();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usrresigter);
		initview();
}

	private void initview() {
		comeback_image = (ImageView) findViewById(R.id.comeback_image);
		user_registerNname = (EditText) findViewById(R.id.user_registerNname);
		user_register_password = (EditText) findViewById(R.id.user_register_password);
		user_register_password_again = (EditText) findViewById(R.id.user_register_password_again);
		user_register_connectphone = (EditText) findViewById(R.id.user_register_connectphone);
		
		registerPasswordError = (TextView) findViewById(R.id.registerPasswordError);
		phoneNumberError = (TextView) findViewById(R.id.phoneNumberError);
		userRegisterNameError = (TextView) findViewById(R.id.userRegisterNameError);
		userPasswordIsNull = (TextView) findViewById(R.id.userPasswordIsNull);
		
		user_sex = (RadioGroup) findViewById(R.id.user_sex);
		userSexMan = (RadioButton) findViewById(R.id.userSexMan);
		userSexWoman = (RadioButton) findViewById(R.id.userSexWoman);
		userSexHide = (RadioButton) findViewById(R.id.userSexHide);
		
		user_register = (Button) findViewById(R.id.user_register);
		
		user_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				    boolean allowResgiter = true;
					username_reg = user_registerNname.getText().toString();
					password_reg = user_register_password.getText().toString();
					password_reg_again = user_register_password_again.getText().toString();
					if(username_reg.equals("")&& !password_reg.equals("")){
						userRegisterNameError.setText("用户名不能为空");
						allowResgiter = false;
						userPasswordIsNull.setText("");
					}
					if(password_reg.equals("") && !username_reg.equals("")){
						allowResgiter = false;
						userPasswordIsNull.setText("密码不能为空");
						userRegisterNameError.setText("");
						registerPasswordError.setText("");
					}
					if(password_reg.equals("") && username_reg.equals("")){
						allowResgiter = false;
						userPasswordIsNull.setText("密码不能为空");
						userRegisterNameError.setText("用户名不能为空");
						registerPasswordError.setText("");
					}
					if(!password_reg.equals("") && username_reg.equals("")){
						allowResgiter = false;
						userPasswordIsNull.setText("");
						userRegisterNameError.setText("用户名不能为空");
						registerPasswordError.setText("");
					}
					if(!password_reg.equals(password_reg_again)&& !username_reg.equals("")){
						userRegisterNameError.setText("");
						userPasswordIsNull.setText("");
						allowResgiter = false;
						registerPasswordError.setText("两次输入的密码不一致");
					}
					String phoneNumber = user_register_connectphone.getText().toString();
					//手机号码的正则表达式
					String regExp = "^[1][358][0-9]{9}$"; 
					if(phoneNumber.equals(""))
						phoneNumberError.setText("");
					if(!phoneNumber.equals("") && !Pattern.compile(regExp).
							matcher(phoneNumber).matches()){
						phoneNumberError.setText("手机号码格式不正确");
						allowResgiter = false;
					}
					if(!password_reg.equals("") && password_reg.equals(password_reg_again)&& 
							!username_reg.equals("")&& allowResgiter){
						clear();
						//用户详细信息
						userDInfo.setUser_state(User.state_宅客);
						userDInfo.setSexId(userSexId);
						userDInfo.setConnectPhone(user_register_connectphone.getText()+"");
						userDInfo.setRegisteTime(new Date());
						userDInfo.setUsername(username_reg);
						userDInfo.setPassWord(password_reg);
						userDInfo.setLocation_lasetime_login("宜春");
						userDInfo.setMy_user_sign("我就是我，颜色不一样的烟火");
						//发送注册消息给服务器
						Log.v("dfsdfs","wo dao ");
						new SentRegisterMsgTask().execute(userDInfo);
					}			
			}

			private void clear() {
				userPasswordIsNull.setText("");
				userRegisterNameError.setText("");
				registerPasswordError.setText("");
				phoneNumberError.setText("");
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
		
		comeback_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(userResigterActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 3);
//				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				userResigterActivity.this.startActivity(intent);
				userResigterActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		});
	}
	class SentRegisterMsgTask extends AsyncTask<UserDetailInfo, Integer, Integer>{
		
		@Override
		protected void onPreExecute() {
			//将注册按钮变为不可选
			user_register.setEnabled(false);
			Toast.makeText(userResigterActivity.this, "注册中.....", 1000).show();
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
				User user = new User();
				user.setUsername(userDInfo.getUsername());
				user.setLocation_lasetime_login(userDInfo.getLocation_lasetime_login());
				user.setMy_user_sign(userDInfo.getMy_user_sign());
				Gson gson = new Gson();
				String userJson = gson.toJson(user);
				SharedPreferences preferences=getSharedPreferences("userDetailFile",Context.MODE_PRIVATE);
				Editor editor=preferences.edit();
				editor.putString("userDetail", userJson);
				editor.commit();
				
				Intent intent = new Intent(userResigterActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem",3 );
//				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				userResigterActivity.this.startActivity(intent);
				userResigterActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
				Toast.makeText(userResigterActivity.this, "注册成功", 1000).show();
			}else if(result == 0){
				Toast.makeText(userResigterActivity.this, "服务器出问题啦，~注册失败,",
						Toast.LENGTH_LONG).show();
			}else if(result == 2){
				userRegisterNameError.setText("用户名已存在");
				//Toast.makeText(userResigterActivity.this, "用户名已存在", 1000).show();
			}
			//将注册按钮回复可选
			user_register.setEnabled(true);
			super.onPostExecute(result);
		}	
	}
	//屏蔽返回键
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_BACK){
				//跳回MaimActivity
				Intent intent = new Intent(userResigterActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 3);
				intent.putExtras(bundle);
				userResigterActivity.this.startActivity(intent);
				userResigterActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
			return super.onKeyDown(keyCode, event);
		}
}
