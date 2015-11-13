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
	//��ʾ�����listview
	private ListView chatting_listView;
	//���ѵ�name��id
	private int friendUserId;
	private String friendUsername;
	//�Լ���name��id
	private int myUserId;
	private String myName;
	//����һ���洢��Ϣ������
	private List<ChatMessage> chatMsgList = new ArrayList<ChatMessage>();
	//������
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
		//��ȡ����������ֺ�ID
		Bundle bundle = ChattingActivity.this.getIntent().getExtras();
		friendUsername = bundle.getString("userName");
		friendUserId = bundle.getInt("userId");
		//��ȡ�Լ���id
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
		//�����Ϣ������
		OurAppMsgPushReceiver.msgListeners.add(this);
		//������ʷ�����¼
		new AddChatMessageListTask().execute();
		
		chatting_listView.setAdapter(adapter = new ChattingAdapter(ChattingActivity.this, chatMsgList));
		//��ǰ�������
		chatting_username.setText(friendUsername);
		
		//���غ����б�
		chatting_top_comeback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ChattingActivity.this, MyfirendActivity.class);
				ChattingActivity.this.startActivity(intent);
				ChattingActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
				//������ת�Ķ���
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		});
		//������Ϣ
		chatting_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!NetUtil.isNetConnected(getApplicationContext())){
					myName = "netError";
					Log.v("NetUtil","�����쳣");
				}	
				String msg = chattingEdit_msg.getText().toString();
				//��װ���͵���Ϣ
				ChatMessage cMsg = new ChatMessage();
				cMsg.setFromUserId(myUserId);
				cMsg.setFromUserName(myName);
				cMsg.setChatMsgContent(msg);
				cMsg.setToUserId(friendUserId);
				cMsg.setToUserName(friendUsername);
				cMsg.setSentMsgTime(new Date());
				//����Ϣ�Ž�����
				chatMsgList.add(cMsg);
				adapter.notifyDataSetChanged();
				if(chatMsgList != null)//��ListView�ƶ������µ���Ϣ
					chatting_listView.setSelection(chatMsgList.size()-1);
				//������Ϣ�Ĳ���
				new SendMsgTask(new Gson().toJson(cMsg)).send();
				//����������
				chattingEdit_msg.setText("");
				//����Ϣд����Ϣ���ݿ���
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
		if(chatMsgList != null)//��ListView�ƶ������µ���Ϣ
			chatting_listView.setSelection(chatMsgList.size()-1);
	}
	//����Ϣд�����ݿ��еĲ���
	class writeIntoDBTask extends AsyncTask<ChatMessage, Integer, ChatMessage>{

		@Override
		protected ChatMessage doInBackground(ChatMessage... arg0) {
			//Ϊÿ�����Ѵ���һ�����ݿ��
			ChattMessageDB cdb = new ChattMessageDB(ChattingActivity.this, friendUserId);
			cdb.addMsgToDB(arg0[0]);
			Log.e("��Ϣ����ɹ�",arg0[0].getChatMsgContent()+"");
			return null;
		}
	}
	class AddChatMessageListTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			ChattMessageDB cDB = new ChattMessageDB(ChattingActivity.this, friendUserId);
			List<ChatMessage> DBchatMsg = new ArrayList<ChatMessage>();
			DBchatMsg = cDB.getChatMessageList(friendUserId);
			//�жϵ�ǰ�Ƿ�����ʷ�����¼
			if(DBchatMsg != null){
				chatMsgList = DBchatMsg;
				return 1;
			}else{
				Log.e("���ݿ��е���Ϣ��DBchatMsg",DBchatMsg.size()+"");
				Log.e("���ݿ��е���Ϣ��chatMsgList",chatMsgList.size()+"");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				//���غ������ݿ��е������¼
				chatting_listView.setAdapter(adapter = new ChattingAdapter(ChattingActivity.this, chatMsgList));
				if(chatMsgList != null)//��ListView�ƶ������µ���Ϣ
					chatting_listView.setSelection(chatMsgList.size()-1);
			}
			super.onPostExecute(result);
		}	
	}
}
