package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.bean.User;
import com.example.bean.UserDetailInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
 * �����б�����ݿ����*/
public class MyFriendGroupDB extends SQLiteOpenHelper {

	private static final String DBName = "ourapp.db";
	public MyFriendGroupDB(Context context) {
		super(context, DBName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//�������
		db.execSQL("CREATE table IF NOT EXISTS friendGroup " +
				"(_id INTEGER PRIMARY KEY," +
				"friendId TEXT, " +
				"friendName TEXT," +
				"friendHeadPicType INTEGER," +
				"friendSignal TEXT)");		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 db.execSQL("DROP TABLE IF EXISTS friendGroup");  
	        onCreate(db);
	}
	/*��ѯ����*/
	public UserDetailInfo getFriend(int friendId){
		UserDetailInfo u = new UserDetailInfo();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery("select * from friendGroup where friendId=?",
				new String[] { friendId + "" });
		if (c.moveToNext()){
			u.setUsername(c.getString(c.getColumnIndex("friendName")));
			u.setMy_user_sign(c.getString(c.getColumnIndex("friendSignal")));
			
		} else{
			return null;
		}
		return u;
	}
	/*���Ӻ���*/
	public void AddFriend(UserDetailInfo u){
		if (getFriend(u.getUserId()) != null)
		{
			update(u);
			return;
		}
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(
				"insert into friendGroup (friendId,friendName,friendSignal) values(?,?,?)",
				new Object[] { u.getUserId(), u.getUsername(), u.getMy_user_sign()});
		db.close();
	}
	/*���Ӻ����б�*/
	public void AddFriendGroup(List<UserDetailInfo> friend){
		
		SQLiteDatabase db = getWritableDatabase();
		for (UserDetailInfo user : friend) {
			db.execSQL(
				"insert into friendGroup (friendId,friendName,friendSignal) values(?,?,?)",
				new Object[] { user.getUserId(), user.getUsername(), user.getMy_user_sign()});
		}
		db.close();
	}
	/*���º���*/
	public void update(UserDetailInfo user) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(
				"update friendGroup set friendName=?,friendSignal=? where friendId=?",
				new Object[] { user.getUsername(), user.getMy_user_sign(), user.getUserId()});
		db.close();	
	}
	
	/*��ȡ�����б�*/
	public ArrayList<UserDetailInfo> getFriendGroup()
	{
		SQLiteDatabase db = getWritableDatabase();
		ArrayList<UserDetailInfo> list = new ArrayList<UserDetailInfo>();
		Cursor c = db.rawQuery("select * from friendGroup", null);
		while (c.moveToNext()){
			UserDetailInfo u = new UserDetailInfo();
			u.setUserId(c.getInt(c.getColumnIndex("friendId")));
			u.setUsername(c.getString(c.getColumnIndex("friendName")));
			u.setMy_user_sign(c.getString(c.getColumnIndex("friendSignal")));
			list.add(u);
		}
		c.close();
		db.close();
		return list;
	}
	
	/*ɾ��*/
	public void delUser(UserDetailInfo u){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from friendGroup where friendId=?",
				new Object[] { u.getUserId() });
		db.close();
	}
	//����userIDɾ������
	public void delUserRID(int frinedId){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from friendGroup where friendId=?",
				new Object[] {frinedId });
		db.close();
	}
	/*ɾ�����е�����*/
	public void delete(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from friendGroup where 1=1");
		db.close();
	}	
}
