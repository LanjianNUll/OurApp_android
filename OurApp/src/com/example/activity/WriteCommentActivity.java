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
	//user类
	private User user;
	//comment类
	private Comment comment;
	//评论详情类
	private CommentDetailInformation commentDInfo;
	//httpGetCommentJson类
	private HttpGetCommentJson httpgetcommentjson = new HttpGetCommentJson();
	//判断spinner选定的值
	private int comment_type = 0;
	//是否推送到附近的人
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
		//获取用户对象
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
		pullToOther = (CheckBox)findViewById(R.id.pullToOther);
		//是否发送个周围的人
		pullToOther.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton v, boolean arg1) {
				v.setChecked(arg1);
				pushToother = arg1;
				
			}
		});
		//spinner监听
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
		//发送comment
		sent_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(user.getUsername() == "未登录"){
					Toast.makeText(WriteCommentActivity.this, "请登录后，才能发表哦", 1000).show();
					Intent intent = new Intent(WriteCommentActivity.this, UserLoginActivity.class);
					WriteCommentActivity.this.startActivity(intent);
					WriteCommentActivity.this.finish();
					overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					if(!write_comment_comment.getText().toString().equals("")){
						long currentTime = System.currentTimeMillis();
						Date date = new Date(currentTime);
//						//评论类
//						comment = new Comment();
//						comment.setComment_type(comment_type);
//						comment.setComment_from_time(new Date());
//						comment.setComment_content(write_comment_comment.getText().toString().trim());
//						comment.setComment_from_user_name(user.getUsername());
//						comment.setHow_many_people_comment(0);
//						comment.setHow_many_people_see(0);
//						comment.setHow_many_people_praise(0);
						//评论详情类
						//后期要做图片上传等异步操作，图片地址设置等
						commentDInfo = new CommentDetailInformation();
						commentDInfo.setComment_type(comment_type);
						commentDInfo.setComment_from_time(new Date());
						commentDInfo.setComment_content(write_comment_comment.getText().toString().trim());
						commentDInfo.setComment_from_user_name(user.getUsername());
						commentDInfo.setHow_many_people_comment(0);
						commentDInfo.setHow_many_people_see(0);
						commentDInfo.setHow_many_people_praise(0);
						if(pushToother)
							//将你的消息推送到附近的人
							new SendMsgTask(new Gson().toJson(commentDInfo)).sendNoti();
						//发送到网络
						new updataCommentTask().execute();
	
					}else{
						Toast.makeText(WriteCommentActivity.this, "你并没有说什么哦。。。", 1000).show();
					}
				}
			}
		});
		//取消发送comment
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
	class updataCommentTask extends AsyncTask<Void, Integer, Void>{
		//访问网络的异步
		@Override
		protected void onPreExecute() {
			//发送前将发送按钮变得不可选
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
			//发送成功后的跳转
			Intent intent = new Intent(WriteCommentActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem", 2);
			intent.putExtras(bundle);
			WriteCommentActivity.this.startActivity(intent);
			WriteCommentActivity.this.finish();
			overridePendingTransition(R.drawable.interface_jump_in,
					R.drawable.interface_jump_out);
			sent_comment.setClickable(true);
			Toast.makeText(WriteCommentActivity.this, "发送成功", 1000).show();
			super.onPostExecute(result);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//跳回MaimActivity
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
