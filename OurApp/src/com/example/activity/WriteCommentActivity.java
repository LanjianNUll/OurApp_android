package com.example.activity;

import java.text.SimpleDateFormat;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bean.Comment;
import com.example.bean.CommentDetailInformation;
import com.example.bean.User;
import com.example.httpunit.HttpGetCommentJson;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;

public class WriteCommentActivity extends Activity{

	private Spinner spinner;
	private Button sent_comment, cancle_sent;
	private EditText write_comment_comment;
	//user��
	private User user;
	//comment��
	private Comment comment;
	//����������
	private CommentDetailInformation commentDInfo;
	//httpGetCommentJson��
	private HttpGetCommentJson httpgetcommentjson = new HttpGetCommentJson();
	//�ж�spinnerѡ����ֵ
	private int comment_type = 0;
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
		SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		String userJson=preferences.getString("userJson", "defaultname");
		Gson gson = new Gson();
		user = gson.fromJson(userJson, User.class);
		
	}


	private void initview() {
		spinner = (Spinner) findViewById(R.id.write_comment_type_spinner);
		sent_comment = (Button) findViewById(R.id.sent_comment);
		cancle_sent = (Button) findViewById(R.id.cancle_comment);
		write_comment_comment = (EditText) findViewById(R.id.write_comment_comment);
		
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
				if(user.getUsername() == "δ��¼"){
					Toast.makeText(WriteCommentActivity.this, "���¼�󣬲��ܷ���Ŷ", 1000).show();
					Intent intent = new Intent(WriteCommentActivity.this, UserLoginActivity.class);
					WriteCommentActivity.this.startActivity(intent);
					WriteCommentActivity.this.finish();	
				}else{
					if(!write_comment_comment.getText().toString().equals("")){
						long currentTime = System.currentTimeMillis();
						Date date = new Date(currentTime);
						//������
						comment = new Comment();
						comment.setComment_type(comment_type);
						comment.setComment_from_time(new Date());
						comment.setComment_content(write_comment_comment.getText().toString().trim());
						comment.setComment_from_user_name(user.getUsername());
						comment.setHow_many_people_comment(0);
						comment.setHow_many_people_see(0);
						comment.setHow_many_people_praise(0);
						//����������
						//����Ҫ��ͼƬ�ϴ����첽������ͼƬ��ַ���õ�
						commentDInfo = new CommentDetailInformation();
						
						
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
			}
		});
		
	}
	class updataCommentTask extends AsyncTask<Void, Integer, Void>{
		//����������첽
		@Override
		protected void onPreExecute() {
			//����ǰ�����Ͱ�ť��ò���ѡ
			sent_comment.setClickable(false);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			httpgetcommentjson.updateComment(comment,commentDInfo);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			//���ͳɹ������ת
			Intent intent = new Intent(WriteCommentActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem", 2);
			intent.putExtras(bundle);
			WriteCommentActivity.this.startActivity(intent);
			WriteCommentActivity.this.finish();
			sent_comment.setClickable(true);
			Toast.makeText(WriteCommentActivity.this, "���ͳɹ�", 1000).show();
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
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
