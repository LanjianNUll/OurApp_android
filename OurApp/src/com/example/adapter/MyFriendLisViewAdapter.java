package com.example.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.CircleImageView.CircleImageView;
import com.example.Data.defaultPacage;
import com.example.activity.UserLoginActivity;
import com.example.adapter.FindDetaiListViewAdapter.ViewHolder;
import com.example.bean.ChatMessage;
import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.example.httpunit.HttpDoUserMsg;
import com.example.ourapp.R;
import com.example.unti.SendMsgTask;
import com.google.gson.Gson;

public class MyFriendLisViewAdapter extends BaseAdapter {

	private Context context;
	private List<UserDetailInfo> myFriendGroup;
	private int isOnDeteleMode = 0; 
	
	public MyFriendLisViewAdapter(Context context, List<UserDetailInfo> myFriendGroup) {
		this.context = context;
		this.myFriendGroup = myFriendGroup;
	}

	public MyFriendLisViewAdapter(Context context, List<UserDetailInfo> myFriendGroup,  int isOnDeteleMode) {
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
			holder.firendHeadPic = (CircleImageView) arg1.findViewById(R.id.firendHeadPic);
			arg1.setTag(holder);
		}else{
			holder = (Holder) arg1.getTag();
		}
		holder.firendHeadPic.setImageResource(defaultPacage.headpic[15]);
		holder.friendName.setText(myFriendGroup.get(arg0).getUsername());
		holder.friendSignature.setText(myFriendGroup.get(arg0).getMy_user_sign());
		if(isOnDeteleMode == 1){
			//隐藏签名，显示删除按钮
			holder.friendSignature.setVisibility(View.INVISIBLE);
			holder.deteleFriend.setVisibility(View.VISIBLE);
			holder.deteleFriend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {//这里要做一个对话框，防止用户误删除
					
					AlertDialog.Builder ab = new AlertDialog.Builder(context);
					ab.setTitle("");
					ab.setMessage("确定删除"+myFriendGroup.get(arg0).getUsername());
					ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {						
						public void onClick(DialogInterface d, int ar1) {
							
							//发送消息的操作
							SharedPreferences sh = context.getSharedPreferences("userDetailFile",
									Context.MODE_PRIVATE);
							String gstr = sh.getString("userDetail", null);
							Gson gson = new Gson();
							UserDetailInfo user = gson.fromJson(gstr, UserDetailInfo.class);
							int myUserId = user.getUserId();
							String myName = user.getUsername();
							int FriendId = myFriendGroup.get(arg0).getUserId();
							String FriendName = myFriendGroup.get(arg0).getUsername();
							//删除好友的异步网络请求
							new DeleteFriendTask().execute(myUserId, FriendId, arg0);
							myFriendGroup.remove(arg0);
							Log.v("被删除的position",""+arg0);
							MyFriendLisViewAdapter.this.notifyDataSetChanged();
							//定义消息内容
							String MsgContent = "@#$deleteFriend";
							//封装
							ChatMessage cMsg = new ChatMessage();
							cMsg.setChatMsgContent(MsgContent);
							cMsg.setFromUserId(myUserId);
							cMsg.setFromUserName(myName);
							cMsg.setToUserId(FriendId);
							cMsg.setToUserName(FriendName);
							//发送删除消息的按钮
							//new SendMsgTask(new Gson().toJson(cMsg)).send();
							//删好友的等/*/*/
							/*
							 * 
							 * 
							 * 
							 * 
							 * *
							 * 
							 * 
							 * 
							 * 
							 * */
						}
				});
				ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface a, int i) {
						a.cancel();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = ab.create();
				// show it
				alertDialog.show();	
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
		CircleImageView firendHeadPic;
	}

	public void setisOnDeteleMode(int i) {
		isOnDeteleMode = i;
	}
	//删除好友的异步
	class DeleteFriendTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			HttpDoUserMsg httpDoUserMsg = new HttpDoUserMsg();
			int i = httpDoUserMsg.deleteFriendHttp(arg0[0], arg0[1]);
			if(i == 1)
				return arg0[2];
			else
				return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result != -1){
				//Toast.makeText(context, ""+result, 1000).show();
				//删除操作
//				myFriendGroup.remove(result);
//				Log.v("被删除的position",""+result);
//				MyFriendLisViewAdapter.this.notifyDataSetChanged();
			}else{
				Toast.makeText(context, "网络异常，删除好友失败", 1000).show();
			}
			super.onPostExecute(result);
		}
		
	}
}
