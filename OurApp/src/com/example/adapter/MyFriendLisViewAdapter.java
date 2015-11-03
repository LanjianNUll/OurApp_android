package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.adapter.FindDetaiListViewAdapter.ViewHolder;
import com.example.bean.ChatMessage;
import com.example.bean.User;
import com.example.ourapp.R;
import com.example.unti.SendMsgTask;
import com.google.gson.Gson;

public class MyFriendLisViewAdapter extends BaseAdapter {

	private Context context;
	private List<User> myFriendGroup;
	private int isOnDeteleMode = 0; 
	
	public MyFriendLisViewAdapter(Context context, List<User> myFriendGroup) {
		this.context = context;
		this.myFriendGroup = myFriendGroup;
	}

	public MyFriendLisViewAdapter(Context context, List<User> myFriendGroup,  int isOnDeteleMode) {
		this.context = context;
		this.myFriendGroup = myFriendGroup;
		this.isOnDeteleMode = isOnDeteleMode;
	}

	@Override
	public int getCount() {
		return myFriendGroup.size();
	}

	@Override
	public Object getItem(int arg0) {
		return myFriendGroup.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		Holder holder = null;
		if(arg1 == null){
			holder = new Holder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.myfriend_group_item, arg2, false);
			holder.friendName = (TextView) arg1.findViewById(R.id.friendName);
			holder.friendSignature = (TextView) arg1.findViewById(R.id.friendSignature);
			holder.deteleFriend = (Button) arg1.findViewById(R.id.deteleFriend);
			arg1.setTag(holder);
		}else{
			holder = (Holder) arg1.getTag();
		}
		holder.friendName.setText(myFriendGroup.get(arg0).getUsername());
		holder.friendSignature.setText(myFriendGroup.get(arg0).getMy_user_sign());
		if(isOnDeteleMode == 1){
			//隐藏签名，显示删除按钮
			holder.friendSignature.setVisibility(View.INVISIBLE);
			holder.deteleFriend.setVisibility(View.VISIBLE);
			holder.deteleFriend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {//这里要做一个对话框，防止用户误删除
					myFriendGroup.remove(arg0);
					Log.v("被删除的position",""+arg0);
					MyFriendLisViewAdapter.this.notifyDataSetChanged();
					//发送消息的操作
					SharedPreferences sh = context.getSharedPreferences("user", Context.MODE_PRIVATE);
					String gstr = sh.getString("userJson", null);
					Gson gson = new Gson();
					User user = gson.fromJson(gstr, User.class);
					int myUserId = user.getUserId();
					String myName = user.getUsername();
					int FriendId = myFriendGroup.get(arg0).getUserId();
					String FriendName = myFriendGroup.get(arg0).getUsername();
					//定义消息内容
					String MsgContent = "@#$deleteFriend";
					//封装
					ChatMessage cMsg = new ChatMessage();
					cMsg.setChatMsgContent(MsgContent);
					cMsg.setFromUserId(myUserId);
					cMsg.setFromUserName(myName);
					cMsg.setToUserId(FriendId);
					cMsg.setToUserName(FriendName);
					new SendMsgTask(new Gson().toJson(cMsg)).send();
				}
			});
		}
		else{
			//删除按钮显示  显示签名
			holder.deteleFriend.setVisibility(View.GONE);
			holder.friendSignature.setVisibility(View.VISIBLE);
		}
			
		
		return arg1;
	}
	
	final class Holder{
		TextView friendName, friendSignature;
		Button deteleFriend;
	}

	public void setisOnDeteleMode(int i) {
		isOnDeteleMode = i;
	}
	
	
	
}
