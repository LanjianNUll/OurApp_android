package com.example.unti;

import com.example.Application.Application;
import com.example.PushServer.BaiduPush;
import com.example.activity.ChattingActivity;
import com.example.bean.Comment;
import com.example.bean.CommentDetailInformation;
import com.example.dao.ChattMessageDB;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class SendMsgTask {
	private BaiduPush mBaiduPush;
	private String cMsg;
	private Handler mHandler;
	private MyAsyncTask mTask;
	private OnSendScuessListener mListener;
	
	public interface OnSendScuessListener
	{
		void sendScuess();
	}
	public void setOnSendScuessListener(OnSendScuessListener listener)
	{
		this.mListener = listener;
	}

	
	public SendMsgTask(String cMsgJson) {
		//构造函数中 得到push服务器
		this.cMsg = cMsgJson;
		this.mBaiduPush = Application.getInstance().getBaiduPush();
		this.mHandler = new Handler();
	}
	//推送透传消息
	public void send() {
		mTask = new MyAsyncTask();
		mTask.execute();	
	}
	//推送通知栏的通知
	public void sendNoti(){
		new SendNotiTask().execute(cMsg);
	}
	//推送通知栏的通知异步
	class SendNotiTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... arg0) {
			CommentDetailInformation comment = 
					new Gson().fromJson(arg0[0], CommentDetailInformation.class);
			//设置通知的内容
			String notifContent = null;
			String notifTitle = null;
			if(comment.getComment_type() == Comment.comment_type_挑战书)
				notifTitle = "挑战书";
			if(comment.getComment_type() == Comment.comment_type_发现)
				notifTitle = "发现";
			if(comment.getComment_type() == Comment.comment_type_求人带)
				notifTitle = "求人带";
			if(comment.getComment_type() == Comment.comment_type_邀请函)
				notifTitle = "邀请函";
			notifContent = comment.getComment_from_user_name()+"在您附近发起了一份"+notifTitle+"赶紧点击看看吧";
			String result = mBaiduPush.PushNotifyAll(notifTitle,notifContent);
//			推送到自定义它的
//			String result = mBaiduPush.PushMessage(cMsg);
			return result;
		}
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			Log.e("send msg result:", result);
			if (result.contains(BaiduPush.SEND_MSG_ERROR)){// 如果消息发送失败，则100ms后重发
				mHandler.postDelayed(reSend, 100);
			} else{
				if (mListener != null)
					mListener.sendScuess();
			}
		}	
		
	}
	///推送透传消息异步
	class MyAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			Log.e("（推送）发送的消息" , cMsg);
			String result = mBaiduPush.PushMessage(cMsg);
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			Log.e("send msg result:", result);
			if (result.contains(BaiduPush.SEND_MSG_ERROR)){// 如果消息发送失败，则100ms后重发
				mHandler.postDelayed(reSend, 100);
			} else{
				if (mListener != null)
					mListener.sendScuess();
			}
		}	
	}
	Runnable reSend = new Runnable(){
		@Override
		public void run(){
			//启动一新的线程来重发
			Log.v("重新发送", "resend msg...");
			send();// 重发
		}
	};
}
