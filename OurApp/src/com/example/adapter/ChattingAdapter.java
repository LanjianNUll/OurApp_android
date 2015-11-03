package com.example.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.bean.ChatMessage;
import com.example.bean.User;
import com.example.ourapp.R;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChattingAdapter extends BaseAdapter {

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Context context;
	private List<ChatMessage> chatMsgList;
	private int myUserId;
	private SharedPreferences sh;
	public ChattingAdapter(Context context, List<ChatMessage> chatMsgList) {
		this.chatMsgList = chatMsgList;
		this.context = context;
		sh = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		String gstr = sh.getString("userJson", null);
		Gson gson = new Gson();
		User user = gson.fromJson(gstr, User.class);
		myUserId = user.getUserId();		
	}

	@Override
	public int getCount() {
		if(chatMsgList == null){
			Log.v("消息是否为空哦","是");
			return 0;
		}
			
		else
			return chatMsgList.size();
	}

	@Override
	public Object getItem(int arg0) {
		if(chatMsgList == null)
			return null;
		else
			return chatMsgList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ChatMessage chatMessage = chatMsgList.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			Log.e("自己的id",myUserId+"");
			if (chatMessage.getFromUserId() != myUserId)
			{
				Log.e("firend发来的消息","");
				//firend发来的消息
				convertView = LayoutInflater.from(context).inflate(R.layout.message_from_friens_item, parent, false);
				viewHolder.createDate = (TextView) convertView
						.findViewById(R.id.chat_from_createDate);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.chat_from_content);
				viewHolder.nickname = (TextView) convertView
						.findViewById(R.id.chat_from_name);
				convertView.setTag(viewHolder);
			} else{
				//自己发送的消息
				Log.e("自己发送的消息","");
				convertView =LayoutInflater.from(context).inflate(R.layout.messag_from_myself_item,null);
				viewHolder.createDate = (TextView) convertView
						.findViewById(R.id.chat_send_createDate);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.chat_send_content);
				convertView.setTag(viewHolder);
				viewHolder.nickname = (TextView) convertView
						.findViewById(R.id.netError);
				convertView.setTag(viewHolder);
			}
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.content.setText(chatMessage.getChatMsgContent());
		viewHolder.createDate.setText(df.format(chatMessage.getSentMsgTime()));
		viewHolder.nickname.setVisibility(View.GONE);
		//判断是否是自己的消息  是自己的则不显示name
		if(chatMessage.getFromUserId() != myUserId){
			viewHolder.nickname.setVisibility(View.VISIBLE);
			viewHolder.nickname.setText(chatMessage.getFromUserName());
		}
		if(chatMessage.getFromUserName().equals("netError")){
			viewHolder.nickname.setVisibility(View.INVISIBLE);
			viewHolder.nickname.setText("网络异常,发送失败");
		}
		return convertView;
	}
	private class ViewHolder
	{
		public TextView createDate;
		public TextView nickname;
		public TextView content;
	}
		
}
