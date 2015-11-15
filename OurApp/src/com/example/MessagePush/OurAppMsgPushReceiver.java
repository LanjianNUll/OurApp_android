package com.example.MessagePush;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.example.Application.Application;
import com.example.activity.ChattingActivity;
import com.example.bean.ChatMessage;
import com.example.bean.Comment;
import com.example.bean.CommentDetailInformation;
import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.example.dao.ChattMessageDB;
import com.example.dao.MyFriendGroupDB;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
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
		SharedPreferences sh = context.getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
		String gstr = sh.getString("userDetail", null);
		Gson gs = new Gson();
		UserDetailInfo user = gs.fromJson(gstr, UserDetailInfo.class);
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
					UserDetailInfo u = new UserDetailInfo();
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
					//ChattMessageDB cdb = new ChattMessageDB(context, CM.getFromUserId());
					//cdb.addMsgToDB(CM);
				}else 
					Log.e("不是你好友",CM.getChatMsgContent());
				
			}
		} catch (Exception e) {
			try {
				CommentDetailInformation comment = gson.fromJson(message,
						CommentDetailInformation.class);
				if(!user.getUsername().equals(comment.getComment_from_user_name())){
					Intent intent = new Intent(context, MainActivity.class);
					Bundle bundle = new Bundle();
			        //点击通知栏的消息进入 发现界面
			        bundle.putInt("CurrentItem", 2);
			        intent.putExtras(bundle);
			        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        intent.setAction(Intent.ACTION_MAIN);
			        intent.addCategory(Intent.CATEGORY_LAUNCHER);
					PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
					NotificationManager manager = (NotificationManager)
							 context.getSystemService(Context.NOTIFICATION_SERVICE); 
					NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
				    
		            String notifContent = null;
		            //通知栏标题
					String notifTitle = null;
					if(comment.getComment_type() == Comment.comment_type_挑战书)
						notifTitle = "挑战书";
					if(comment.getComment_type() == Comment.comment_type_发现)
						notifTitle = "发现";
					if(comment.getComment_type() == Comment.comment_type_求人带)
						notifTitle = "求人带";
					if(comment.getComment_type() == Comment.comment_type_邀请函)
						notifTitle = "邀请函";
					notifContent = comment.getComment_from_user_name()+"在您附近发起了"+notifTitle+"赶紧点击看看吧";
					//自定义通知栏样式
					//先设定RemoteViews  
//		            RemoteViews view_custom = new RemoteViews(context.getPackageName(), R.layout.my_notification);
//					//设置自定义的标题栏
//		            view_custom.setTextViewText(R.id.notiTitle, notifTitle);
//		            view_custom.setTextViewText(R.id.notiContent, notifContent);
//		            view_custom.setImageViewResource(R.id.notiImage, R.drawable.ic_launcher);
//		            view_custom.setTextViewText(R.id.notiTime, System.currentTimeMillis()+"");
//		            mBuilder.setContent(view_custom);
		            mBuilder.setContentIntent(pendingIntent)//设置通知栏点击意图 
		            .setContentTitle(notifTitle)
		            .setContentText(notifContent)
			        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
			        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级  
			        .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消    
			        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)  
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setDefaults(Notification.DEFAULT_VIBRATE);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合  
		            //启动通知
		            manager.notify(1, mBuilder.build()); 
				}
			} catch (Exception e2) {
				Log.e("来自服务器的json异常 comment", "解析失败法相消息");
			}
			Log.e("来自服务器的json异常dfdsfds", "解析失败好友消息");
			Log.e("message的内容",message+"");
		}
	}

	@Override
	public void onNotificationClicked(Context context, String title,
            String description, String customContentString) {
		//是页面在首页
        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        //点击通知栏的消息进入 发现界面
        bundle.putInt("CurrentItem", 2);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
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
