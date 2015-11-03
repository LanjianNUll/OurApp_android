package com.example.MessagePush;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.example.Application.Application;
import com.example.activity.ChattingActivity;
import com.example.bean.ChatMessage;
import com.example.bean.User;
import com.example.dao.ChattMessageDB;
import com.example.dao.MyFriendGroupDB;
import com.example.ourapp.MainActivity;
import com.example.unti.SharePreferenceUtil;
import com.example.welcomepage.Welcomeactivity;
import com.google.gson.Gson;
public class OurAppMsgPushReceiver extends FrontiaPushMessageReceiver{

	public static ArrayList<onNewMessageListener> msgListeners = new ArrayList<onNewMessageListener>();
		
	public static interface onNewMessageListener
	{
		public abstract void onNewMessage(ChatMessage message);
	}
	
	@Override
	public void onBind(final Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		Log.v("返回的信息", responseString);
			//消息推送
			PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, Application.API_KEY);
		
			Log.v("BindState","onBind");
		
	}

	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2,
			String arg3) {
		// TODO Auto-generated method stub
		
	}
	/**
     * 接收透传消息的函数。
     * 
     * @param context
     *            上下文
     * @param message
     *            推送的消息
     * @param customContentString
     *            自定义内容,为空或者json字符串
     */
	@Override
	public void onMessage(Context context, String message, String customContentString) {
		//获取用户的userid
		SharedPreferences sh = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		String gstr = sh.getString("userJson", null);
		Gson gs = new Gson();
		User user = gs.fromJson(gstr, User.class);
		int myUserId = user.getUserId();
		Log.e("msg服务端的消息", message);
		//对消息解读
		ChatMessage CM = null;
		Gson gson = new Gson();
		try {
			CM = gson.fromJson(message, ChatMessage.class);	
			Log.e("gson解析成功","");
			if(CM != null && CM.getChatMsgContent().equals("@#$deleteFriend")){//判断是否为删除好友的消息
				/*处理删除好友的操作*/
				MyFriendGroupDB myFdb = new MyFriendGroupDB(context);
				//判断是否发给自己的删除消息
				if(CM.getToUserId() == myUserId){
					myFdb.delUserRID(CM.getFromUserId());
					Log.d("删除好友","success");
				}
				Log.e("删除好友","");
			}
			else if(CM != null && CM.getChatMsgContent().equals("@#$addFriend")){//判断是否加好友的消息
				MyFriendGroupDB myFdb = new MyFriendGroupDB(context);
				//判断是否发给自己的加好友消息
				if(CM.getToUserId() == myUserId){//取到第三个字符串
					User u = new User();
					u.setUserId(CM.getFromUserId());
					u.setUsername(CM.getFromUserName());
					u.setMy_user_sign("这里需要从服务器更新");
					myFdb.AddFriend(u);
					Log.e("添加好友","success");
				}	
				Log.e("添加好友","");
			}else{//普通消息
				//判断是否你的好友，默认是false
				boolean isYourFirend = false;
				MyFriendGroupDB myFdb = new MyFriendGroupDB(context);
				//判断是否发给自己的消息和判断是否是你好友
				if(myFdb.getFriend(CM.getFromUserId())!= null)
					isYourFirend = true;
	//			SharePreferenceUtil spUtil = new SharePreferenceUtil(context, "myFriensGroup");
	//			int[] myFGId = spUtil.readForSharePfInt("myFriendGroupIds");
	//			boolean isYourFirend = false;
	//			for (int i : myFGId)
	//				if(i == CM.getFromUserId())
	//					isYourFirend = true;
	
				if(CM != null && isYourFirend){
					if (msgListeners.size() > 0){// 有监听的时候，传递下去
						for (int i = 0; i < msgListeners.size(); i++)
							msgListeners.get(i).onNewMessage(CM);
					}
					//将好友消息放入数据库
					ChattMessageDB cdb = new ChattMessageDB(context, CM.getFromUserId());
					cdb.addMsgToDB(CM);
				}else 
					Log.e("不是你好友",CM.getChatMsgContent());
				
			}
		} catch (Exception e) {
			Log.e("来自服务器的json异常", "解析失败");
			Log.e("message的内容",message+"");
		}
	}

	@Override
	public void onNotificationClicked(Context context, String arg1, String arg2,
			String arg3) {
		//是页面在首页
        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("CurrentItem", 3);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
	}	

	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUnbind(Context arg0, int arg1, String arg2) {
		Log.v("BindState","onUnbind");
	}
}
