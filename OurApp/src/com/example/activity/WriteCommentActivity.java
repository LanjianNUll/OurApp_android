package com.example.activity;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bean.Comment;
import com.example.bean.CommentDetailInformation;
import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.example.httpunit.HttpGetCommentJson;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.example.unti.SendMsgTask;
import com.google.gson.Gson;

public class WriteCommentActivity extends Activity{

	private Spinner spinner;
	private Button sent_comment, cancle_sent;
	private EditText write_comment_comment;
	private CheckBox pullToOther;
	//user��
	private UserDetailInfo user;
	//comment��
	private Comment comment;
	//����������
	private CommentDetailInformation commentDInfo;
	//httpGetCommentJson��
	private HttpGetCommentJson httpgetcommentjson = new HttpGetCommentJson();
	//�ж�spinnerѡ����ֵ
	private int comment_type = 0;
	//�Ƿ����͵���������
	private boolean pushToother = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wirte_comment);
		initmsg();
		initview();
	}

	private void initmsg() {
		//��ȡ�û�����
		SharedPreferences preferences = getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
		String userJson=preferences.getString("userDetail", null);
		Gson gson = new Gson();
		user = gson.fromJson(userJson, UserDetailInfo.class);
	}

	private void initview() {
		spinner = (Spinner) findViewById(R.id.write_comment_type_spinner);
		sent_comment = (Button) findViewById(R.id.sent_comment);
		cancle_sent = (Button) findViewById(R.id.cancle_comment);
		write_comment_comment = (EditText) findViewById(R.id.write_comment_comment);
		pullToOther = (CheckBox)findViewById(R.id.pullToOther);
		//�Ƿ��͸���Χ����
		pullToOther.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton v, boolean arg1) {
				v.setChecked(arg1);
				pushToother = arg1;
				
			}
		});
		//spinner����
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				comment_type = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		//����comment
		sent_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(user.getUserId()== -1){
					Toast.makeText(WriteCommentActivity.this, "���¼�󣬲��ܷ���Ŷ", 1000).show();
					Intent intent = new Intent(WriteCommentActivity.this, UserLoginActivity.class);
					WriteCommentActivity.this.startActivity(intent);
					WriteCommentActivity.this.finish();
					overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					if(!write_comment_comment.getText().toString().equals("")){
						long currentTime = System.currentTimeMillis();
						Date date = new Date(currentTime);
//						//������
//						comment = new Comment();
//						comment.setComment_type(comment_type);
//						comment.setComment_from_time(new Date());
//						comment.setComment_content(write_comment_comment.getText().toString().trim());
//						comment.setComment_from_user_name(user.getUsername());
//						comment.setHow_many_people_comment(0);
//						comment.setHow_many_people_see(0);
//						comment.setHow_many_people_praise(0);
						//����������
						//����Ҫ��ͼƬ�ϴ����첽������ͼƬ��ַ���õ�
						commentDInfo = new CommentDetailInformation();
						commentDInfo.setComment_from_user_id(user.getUserId());
						commentDInfo.setComment_from_user_name(user.getUsername());
						commentDInfo.setUser_state(user.getUser_state());
						commentDInfo.setComment_type(comment_type);
						commentDInfo.setComment_from_time(new Date());
						commentDInfo.setComment_content(write_comment_comment.getText().toString().trim());
						commentDInfo.setComment_from_user_name(user.getUsername());
						commentDInfo.setHow_many_people_comment(0);
						commentDInfo.setHow_many_people_see(0);
						commentDInfo.setHow_many_people_praise(0);
						if(pushToother)
							//�������Ϣ���͵���������
							new SendMsgTask(new Gson().toJson(commentDInfo)).sendNoti();
						//���͵�����
						new updataCommentTask().execute();
	
					}else{
						Toast.makeText(WriteCommentActivity.this, "�㲢û��˵ʲôŶ������", 1000).show();
					}
				}
			}
		});
		//ȡ������comment
		cancle_sent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(WriteCommentActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 2);
				intent.putExtras(bundle);
				WriteCommentActivity.this.startActivity(intent);
				WriteCommentActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		});
		
	}
	class updataCommentTask extends AsyncTask<Integer, Integer, Integer>{
		//����������첽
		@Override
		protected void onPreExecute() {
			//����ǰ�����Ͱ�ť��ò���ѡ
			sent_comment.setClickable(false);
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Integer... arg0) {
			return httpgetcommentjson.updateComment(comment,commentDInfo);
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				//���ͳɹ������ת
				Intent intent = new Intent(WriteCommentActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 2);
				intent.putExtras(bundle);
				WriteCommentActivity.this.startActivity(intent);
				WriteCommentActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
				sent_comment.setClickable(true);
				Toast.makeText(WriteCommentActivity.this, "���ͳɹ�", 1000).show();
			}
			else{
				Toast.makeText(WriteCommentActivity.this, "��Ŷ~������ʧ�ܣ������������״��", 
						Toast.LENGTH_LONG).show();
			}
			
			super.onPostExecute(result);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//����MaimActivity
			Intent intent = new Intent(WriteCommentActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem", 2);
			intent.putExtras(bundle);
			WriteCommentActivity.this.startActivity(intent);
			WriteCommentActivity.this.finish();
			overridePendingTransition(R.drawable.interface_jump_in,
					R.drawable.interface_jump_out);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
