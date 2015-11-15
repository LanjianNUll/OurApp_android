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
	//ҳͷ��ʾ
	private TextView textView1;
	//�û���¼
	private EditText user_name, password;
	private RelativeLayout login_body;
	private TextView passwordError, userNameError;
	//�û�ע��
	private TextView user_register_textView;
	//�û���¼��ť
	private Button user_login;
	//��¼�û���
	private String userName;
	//��¼����
	private String passWord;
	//user�����û�������Ϣ
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
				//�����ע��textView��
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
						passwordError.setText("����������");
						isallowLogin = false;
					}
					if(userName.equals("")){
						userNameError.setText("�û�������Ϊ��");
						isallowLogin = false;
					}
					if(!userName.equals("")&& passWord.equals("")){
						userNameError.setText("");
						passwordError.setText("����������");
						isallowLogin = false;
					}
					if(userName.equals("")&& !passWord.equals("")){
						userNameError.setText("�û�������Ϊ��");
						passwordError.setText("");
						isallowLogin = false;
					}
					if(!passWord.equals("")&&!userName.equals("")&&isallowLogin){
						//��֤�û�����¼���
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
			Toast.makeText(UserLoginActivity.this, "���ڵ�¼...",1000).show();
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
				//��¼�ɹ��Ĵ���
				Toast.makeText(UserLoginActivity.this,"��¼�ɹ�", 1000).show();
			}else if(result == 0)
				//��¼ʧ�ܵĴ���
				Toast.makeText(UserLoginActivity.this,"��¼ʧ��", 1000).show();
			else if(result == -1)
				userNameError.setText("�û�������");
				//Toast.makeText(UserLoginActivity.this, "�û�������", 1000).show();
			else if(result == -2)
				passwordError.setText("�û������������");
				//Toast.makeText(UserLoginActivity.this, "�������", 1000).show();
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
			overridePendingTransition(R.drawable.interface_jump_in,
					R.drawable.interface_jump_out);
		}
		return super.onKeyDown(keyCode, event);
	}
}
