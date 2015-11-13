package com.example.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.MessagePush.OurAppMsgPushReceiver;
import com.example.MessagePush.OurAppMsgPushReceiver.onNewMessageListener;
import com.example.adapter.ChattingAdapter;
import com.example.bean.ChatMessage;
import com.example.bean.User;
import com.example.dao.ChattMessageDB;
import com.example.ourapp.R;
import com.example.unti.NetUtil;
import com.example.unti.SendMsgTask;
import com.google.gson.Gson;

public class ChattingActivity extends Activity implements onNewMessageListener {
	private TextView chatting_username;
	private EditText chattingEdit_msg;
	private ImageView chatting_top_comeback;
	private Button chatting_send;
	//显示聊天的listview
	private ListView chatting_listView;
	//好友的name和id
	private int friendUserId;
	private String friendUsername;
	//自己的name和id
	private int myUserId;
	private String myName;
	//设置一个存储消息的容器
	private List<ChatMessage> chatMsgList = new ArrayList<ChatMessage>();
	//适配器
	private ChattingAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chatting_layout);
		initView();
	}
	private void initView() {
		//获取聊天对象名字和ID
		Bundle bundle = ChattingActivity.this.getIntent().getExtras();
		friendUsername = bundle.getString("userName");
		friendUserId = bundle.getInt("userId");
		//获取自己的id
		SharedPreferences sh = getSharedPreferences("user", Context.MODE_PRIVATE);
		String gstr = sh.getString("userJson", null);
		Gson gson = new Gson();
		User user = gson.fromJson(gstr, User.class);
		myUserId = user.getUserId();
		myName = user.getUsername();
		//initView
		chatting_username = (TextView) findViewById(R.id.chatting_username);
		chattingEdit_msg = (EditText) findViewById(R.id.chattingEdit_msg);
		chatting_top_comeback = (ImageView) findViewById(R.id.chatting_top_comeback);
		chatting_send = (Button) findViewById(R.id.chatting_send);
		chatting_listView = (ListView) findViewById(R.id.chatting_listView);
		//添加消息监听器
		OurAppMsgPushReceiver.msgListeners.add(this);
		//加载历史聊天记录
		new AddChatMessageListTask().execute();
		
		chatting_listView.setAdapter(adapter = new ChattingAdapter(ChattingActivity.this, chatMsgList));
		//当前聊天对象
		chatting_username.setText(friendUsername);
		
		//返回好友列表
		chatting_top_comeback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ChattingActivity.this, MyfirendActivity.class);
				ChattingActivity.this.startActivity(intent);
				ChattingActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
				//界面跳转的动画
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		});
		//发送消息
		chatting_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!NetUtil.isNetConnected(getApplicationContext())){
					myName = "netError";
					Log.v("NetUtil","网络异常");
				}	
				String msg = chattingEdit_msg.getText().toString();
				//封装发送的消息
				ChatMessage cMsg = new ChatMessage();
				cMsg.setFromUserId(myUserId);
				cMsg.setFromUserName(myName);
				cMsg.setChatMsgContent(msg);
				cMsg.setToUserId(friendUserId);
				cMsg.setToUserName(friendUsername);
				cMsg.setSentMsgTime(new Date());
				//将消息放进容器
				chatMsgList.add(cMsg);
				adapter.notifyDataSetChanged();
				if(chatMsgList != null)//将ListView移动到最新的消息
					chatting_listView.setSelection(chatMsgList.size()-1);
				//发送消息的操作
				new SendMsgTask(new Gson().toJson(cMsg)).send();
				//发送完毕清空
				chattingEdit_msg.setText("");
				//将消息写入消息数据库中
				new writeIntoDBTask().execute(cMsg);
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(ChattingActivity.this, MyfirendActivity.class);
			
			ChattingActivity.this.startActivity(intent);
			ChattingActivity.this.finish();
			overridePendingTransition(R.drawable.interface_jump_in,
					R.drawable.interface_jump_out);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onNewMessage(ChatMessage message) {
		
		chatMsgList.add(message);
		adapter.notifyDataSetChanged();
		if(chatMsgList != null)//将ListView移动到最新的消息
			chatting_listView.setSelection(chatMsgList.size()-1);
	}
	//把消息写入数据库中的操作
	class writeIntoDBTask extends AsyncTask<ChatMessage, Integer, ChatMessage>{

		@Override
		protected ChatMessage doInBackground(ChatMessage... arg0) {
			//为每个好友创建一个数据库表
			ChattMessageDB cdb = new ChattMessageDB(ChattingActivity.this, friendUserId);
			cdb.addMsgToDB(arg0[0]);
			Log.e("消息加入成功",arg0[0].getChatMsgContent()+"");
			return null;
		}
	}
	class AddChatMessageListTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			ChattMessageDB cDB = new ChattMessageDB(ChattingActivity.this, friendUserId);
			List<ChatMessage> DBchatMsg = new ArrayList<ChatMessage>();
			DBchatMsg = cDB.getChatMessageList(friendUserId);
			//判断当前是否有历史聊天记录
			if(DBchatMsg != null){
				chatMsgList = DBchatMsg;
				return 1;
			}else{
				Log.e("数据库中的消息数DBchatMsg",DBchatMsg.size()+"");
				Log.e("数据库中的消息数chatMsgList",chatMsgList.size()+"");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				//加载好了数据库中的聊天记录
				chatting_listView.setAdapter(adapter = new ChattingAdapter(ChattingActivity.this, chatMsgList));
				if(chatMsgList != null)//将ListView移动到最新的消息
					chatting_listView.setSelection(chatMsgList.size()-1);
			}
			super.onPostExecute(result);
		}	
	}
}
