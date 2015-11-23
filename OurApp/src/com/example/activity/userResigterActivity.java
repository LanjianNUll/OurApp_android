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
	//�û�ע��
	private EditText user_registerNname, user_register_password,
							user_register_password_again, user_register_connectphone;
	private TextView registerPasswordError, userRegisterNameError, 
								phoneNumberError, userPasswordIsNull;
	private RadioGroup user_sex;
	private RadioButton userSexMan, userSexWoman, userSexHide;
	private Button user_register;
	//ע���û���
	private String username_reg;
	//ע������
	private String password_reg;
	//�ظ�����
	private String password_reg_again;
	//�Ա�
	private int userSexId = User.sex_define;
	//�û���ϸ��Ϣ��UserDetailInfo
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
						userRegisterNameError.setText("�û�������Ϊ��");
						allowResgiter = false;
						userPasswordIsNull.setText("");
					}
					if(password_reg.equals("") && !username_reg.equals("")){
						allowResgiter = false;
						userPasswordIsNull.setText("���벻��Ϊ��");
						userRegisterNameError.setText("");
						registerPasswordError.setText("");
					}
					if(password_reg.equals("") && username_reg.equals("")){
						allowResgiter = false;
						userPasswordIsNull.setText("���벻��Ϊ��");
						userRegisterNameError.setText("�û�������Ϊ��");
						registerPasswordError.setText("");
					}
					if(!password_reg.equals("") && username_reg.equals("")){
						allowResgiter = false;
						userPasswordIsNull.setText("");
						userRegisterNameError.setText("�û�������Ϊ��");
						registerPasswordError.setText("");
					}
					if(!password_reg.equals(password_reg_again)&& !username_reg.equals("")){
						userRegisterNameError.setText("");
						userPasswordIsNull.setText("");
						allowResgiter = false;
						registerPasswordError.setText("������������벻һ��");
					}
					String phoneNumber = user_register_connectphone.getText().toString();
					//�ֻ������������ʽ
					String regExp = "^[1][358][0-9]{9}$"; 
					if(phoneNumber.equals(""))
						phoneNumberError.setText("");
					if(!phoneNumber.equals("") && !Pattern.compile(regExp).
							matcher(phoneNumber).matches()){
						phoneNumberError.setText("�ֻ������ʽ����ȷ");
						allowResgiter = false;
					}
					if(!password_reg.equals("") && password_reg.equals(password_reg_again)&& 
							!username_reg.equals("")&& allowResgiter){
						clear();
						//�û���ϸ��Ϣ
						userDInfo.setUser_state(User.state_լ��);
						userDInfo.setSexId(userSexId);
						userDInfo.setConnectPhone(user_register_connectphone.getText()+"");
						userDInfo.setRegisteTime(new Date());
						userDInfo.setUsername(username_reg);
						userDInfo.setPassWord(password_reg);
						userDInfo.setLocation_lasetime_login("�˴�");
						userDInfo.setMy_user_sign("�Ҿ����ң���ɫ��һ�����̻�");
						//����ע����Ϣ��������
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
			//��ע�ᰴť��Ϊ����ѡ
			user_register.setEnabled(false);
			Toast.makeText(userResigterActivity.this, "ע����.....", 1000).show();
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
				Toast.makeText(userResigterActivity.this, "ע��ɹ�", 1000).show();
			}else if(result == 0){
				Toast.makeText(userResigterActivity.this, "����������������~ע��ʧ��,",
						Toast.LENGTH_LONG).show();
			}else if(result == 2){
				userRegisterNameError.setText("�û����Ѵ���");
				//Toast.makeText(userResigterActivity.this, "�û����Ѵ���", 1000).show();
			}
			//��ע�ᰴť�ظ���ѡ
			user_register.setEnabled(true);
			super.onPostExecute(result);
		}	
	}
	//���η��ؼ�
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_BACK){
				//����MaimActivity
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
