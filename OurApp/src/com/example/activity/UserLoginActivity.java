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
	//ҳͷ��ʾ
	private TextView textView1;
	//�û���¼
	private EditText user_name, password;
	private RelativeLayout login_body;
	//�û�ע��
	private EditText user_register_name, user_register_password, user_register_password_again, user_register_connectphone;
	private RelativeLayout user_register_msg;
	private RadioGroup user_sex;
	private RadioButton userSexMan, userSexWoman, userSexHide;
	//�û�ѡ��ť
	private Button user_login, user_register;
	//�û�����˵�¼��ť,Ĭ��Ϊ��¼����
	private boolean isclick_login = true;
	//�Ƿ��ڵ�¼����
	private boolean ison_login = true;
	//�Ƿ���ע�����
	private boolean ison_register = false;
	//�û������ע�ᰴť��Ĭ������ע�����
	private Boolean isclick_register = false;
	//�ж��û��Ƿ��������û����������ҷ���Ҫ��
	private boolean isright = false;
	//��¼�û���
	private String userName;
	//��¼����
	private String passWord;
	//ע���û���
	private String username_reg;
	//ע������
	private String password_reg;
	//�ظ�����
	private String password_reg_again;
	//user�����û�������Ϣ
	private User user = new User();
	//�û���ϸ��Ϣ��UserDetailInfo
	private UserDetailInfo userDInfo = new UserDetailInfo();
	//�Ա�
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
		
		//�ж��ڵ�¼����ע�����
		if(isclick_login){
			login_body.setVisibility(View.VISIBLE);
			user_register_msg.setVisibility(View.GONE);
			ison_login = true;
			textView1.setText("�û���¼".toString());
			user_login.setTextColor( Color.parseColor("#ff9900"));
		}
		if(isclick_register){
			login_body.setVisibility(View.GONE);
			user_register_msg.setVisibility(View.VISIBLE);
			ison_register = true;
			textView1.setText("�û�ע��".toString());
			user_register.setTextColor( Color.parseColor("#ff9900"));
		}
		
		user_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(ison_login){
					userName = user_name.getText().toString();
					passWord= password.getText().toString();
					//��֤�û�����¼���
					new CheckUserIsRight().execute(userName,passWord);
					
				}	
				if(!ison_login){
					login_body.setVisibility(View.VISIBLE);
					user_register_msg.setVisibility(View.GONE);
					clearEditText();
					ison_login = true;
					ison_register = false;
					textView1.setText("�û���¼".toString());
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
						
						//�û���ϸ��Ϣ
						userDInfo.setUserId(0);
						userDInfo.setUser_state(User.state_ð��);
						userDInfo.setSexId(userSexId);
						userDInfo.setConnectPhone(user_register_connectphone.getText()+"");
						userDInfo.setLeaveword(null);
						userDInfo.setRegisteTime(new Date());
						userDInfo.setUsername(username_reg);
						userDInfo.setPassWord(password_reg);
						userDInfo.setLocation_lasetime_login("�˴�");
						userDInfo.setMy_user_sign("�Ҿ����ң���ɫ��һ�����̻�");
						//����ע����Ϣ��������
						Log.v("dfsdfs","wo dao ");
						new SentRegisterMsgTask().execute(userDInfo);
					}else{
						Toast.makeText(UserLoginActivity.this,"���벻��Ϊ�ջ�������������벻һ��", 1000).show();
					}					
				}		
				if(!ison_register){
					login_body.setVisibility(View.GONE);
					user_register_msg.setVisibility(View.VISIBLE);
					ison_register = true;
					ison_login = false;
					textView1.setText("�û�ע��".toString());
					user_register.setTextColor(Color.parseColor("#ff9900"));
					user_login.setTextColor(Color.parseColor("#404040"));
				}
			}
		});
		
		comeback_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				user.setUsername("�û�δ��¼");
				Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 3);
//				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				UserLoginActivity.this.startActivity(intent);
				UserLoginActivity.this.finish();
			}
		});
		
		//�Ա�ѡ���ֵļ���
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
    //���EditText������
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
			//��ע�ᰴť��Ϊ����ѡ
			user_register.setEnabled(false);
			Toast.makeText(UserLoginActivity.this, "ע����.....", 1000).show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(UserDetailInfo... arg0) {
			return httpDoUserMsg.sentUserDetailInfo(arg0[0]);
			 
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){//ע��ɹ�
				//ע��ɹ���Ĵ���
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
				Toast.makeText(UserLoginActivity.this, "ע��ɹ�", 1000).show();
			}else if(result == 0){
				Toast.makeText(UserLoginActivity.this, "ע��ʧ��", 1000).show();
			}else if(result == 2){
				Toast.makeText(UserLoginActivity.this, "�û����Ѵ���", 1000).show();
			}else if(result == 3){
				Toast.makeText(UserLoginActivity.this, "�û���������Ϊ��", 1000).show();
			}
			//��ע�ᰴť�ظ���ѡ
			user_register.setEnabled(true);
			super.onPostExecute(result);
		}	
	}
	class CheckUserIsRight extends AsyncTask<String, Integer, Integer>{

		
		@Override
		protected void onPreExecute() {
			Toast.makeText(UserLoginActivity.this, "���ڵ�¼...",2000).show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			return httpDoUserMsg.checkUserIsRight(arg0[0],arg0[1]);
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				//��¼�ɹ����û���Ϣ������SharedPreferences
				user = new User();
				user.setUsername(userName);
				user.setPassword(passWord);
				user.setUser_state(15);
				//��һ�������
				user.setUserId((int) Math.round(Math.random()*100));
				user.setSexId(1);
				user.setMy_user_sign("�Ҿ����ң���ɫ��һ�����̻�");
				user.setLocation_lasetime_login("�˴�");
				
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
				
				//��¼�ɹ��Ĵ���
				Toast.makeText(UserLoginActivity.this,"��¼�ɹ�", 1000).show();
			}else if(result == 0)
				//��¼ʧ�ܵĴ���
				Toast.makeText(UserLoginActivity.this,"��¼ʧ��", 1000).show();
			else if(result == -1)
				Toast.makeText(UserLoginActivity.this, "�û�������", 1000).show();
			else if(result == -2)
				Toast.makeText(UserLoginActivity.this, "�������", 1000).show();
			else if(result == -3)
				Toast.makeText(UserLoginActivity.this, "�û��������벻��Ϊ��", 1000).show();
			super.onPostExecute(result);
		}
	}
	//���η��ؼ�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//����MaimActivity
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
