package com.example.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.example.bean.ChatMessage;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChattMessageDB extends SQLiteOpenHelper {
	private static final String DBName = "ChatMessage.db";
	private int friendIddb;
	private static int lenthChatHistory = 30;
	public ChattMessageDB(Context context,int friendId) {
		super(context, DBName, null, 1);
		this.friendIddb = friendId;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//建消息记录表
		db.execSQL("CREATE TABLE IF NOT EXISTS ChatMsg" +friendIddb+
				"(chatMsgContent text," +
				"sentMsgContent long," +
				"fromUserName text," +
				"toUserName text," +
				"fromUserId INTEGER NOT NULL," +
				"toUserId INTEGER NOT NULL," +
				"ChatMessageId INTEGER PRIMARY KEY)"
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			db.execSQL("DROP TABLE IF EXISTS ChatMsg");  
	        onCreate(db);
	}
	//查询消息10条
	public List<ChatMessage> getChatMessageList(int friendId){
		List<ChatMessage>  cMsgList = new ArrayList<ChatMessage>();
		SQLiteDatabase db = getWritableDatabase();
		createTable(db);
		Cursor c = db.rawQuery("select * from ChatMsg"+friendId+" where toUserId=?",
					new String[]{friendId+""} );
		while (c.moveToNext())
		{
			ChatMessage Cmsg = new ChatMessage();
			Cmsg.setChatMessageId(c.getInt(c.getColumnIndex("ChatMessageId")));
			Cmsg.setToUserId(c.getInt(c.getColumnIndex("toUserId")));
			Cmsg.setFromUserId(c.getInt(c.getColumnIndex("fromUserId")));
			Cmsg.setToUserName(c.getString(c.getColumnIndex("toUserName")));
			Cmsg.setFromUserName(c.getString(c.getColumnIndex("fromUserName")));
			Cmsg.setChatMsgContent(c.getString(c.getColumnIndex("chatMsgContent")));
			Cmsg.setSentMsgTime(new Date(c.getLong(c.getColumnIndex("sentMsgContent"))));
			cMsgList.add(Cmsg);
		}
		Log.e("数据库中的查询",cMsgList.size()+"");
		c.close();
		db.close();
		//取到最新的十条数据
		//写一个比较器
		Comparator<ChatMessage> comparator = new Comparator<ChatMessage>(){
			@Override
			public int compare(ChatMessage arg0, ChatMessage arg1) {
				if(arg0.getChatMessageId()>arg1.getChatMessageId()){
					return 1;
				}
				return -1;
			}
		};
		if(cMsgList != null && cMsgList.size()>lenthChatHistory){
			List<ChatMessage>  TenCountcMsgList = new ArrayList<ChatMessage>();
			Collections.sort(cMsgList, comparator);
			Collections.reverse(cMsgList);
			for(int i = 0; i<lenthChatHistory; i++)
				TenCountcMsgList.add(cMsgList.get(i));
			Collections.reverse(TenCountcMsgList);
			return TenCountcMsgList;
		}else
			return cMsgList;
		
	}
	
	//添加一个消息记录
	public void addMsgToDB(ChatMessage cMsg){
		//找到对应的表添加消息记录
		SQLiteDatabase db = getWritableDatabase();
		createTable(db);
		db.execSQL("insert into ChatMsg"+cMsg.getToUserId()+" (toUserId," +
				"fromUserId," +
				"toUserName," +
				"fromUserName," +
				"chatMsgContent," +
				"sentMsgContent) values(?,?,?,?,?,?)",
				new Object[]{cMsg.getToUserId(),
				cMsg.getFromUserId(),
				cMsg.getToUserName(),
				cMsg.getFromUserName(),
				cMsg.getChatMsgContent(),
				cMsg.getSentMsgTime().getTime()});
		Log.e("ChatMsg"+cMsg.getToUserId(),"加入成功");
//		db.close();	
	}	
		
	private void createTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS ChatMsg" +friendIddb+
				"(chatMsgContent text," +
				"sentMsgContent long," +
				"fromUserName text," +
				"toUserName text," +
				"fromUserId INTEGER NOT NULL," +
				"toUserId INTEGER NOT NULL," +
				"ChatMessageId INTEGER PRIMARY KEY)"
				);
		
	}
}
