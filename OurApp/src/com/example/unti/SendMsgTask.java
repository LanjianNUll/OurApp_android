package com.example.unti;

import com.example.Application.Application;
import com.example.PushServer.BaiduPush;
import com.example.activity.ChattingActivity;
import com.example.dao.ChattMessageDB;

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

	public void send() {
		mTask = new MyAsyncTask();
		mTask.execute();	
	}
	class MyAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			Log.e("（推送）发送的消息" , cMsg);
			String result = mBaiduPush.PushMessage(cMsg);
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			
			super.onPostExecute(result);
			Log.e("send msg result:", result);
			if (result.contains(BaiduPush.SEND_MSG_ERROR))
			{// 如果消息发送失败，则100ms后重发
				mHandler.postDelayed(reSend, 100);
			} else
			{
				if (mListener != null)
					mListener.sendScuess();
			}
		}	
	}
	Runnable reSend = new Runnable()
	{

		@Override
		public void run()
		{
			//启动一新的线程来重发
			Log.v("重新发送", "resend msg...");
			send();// 重发
		}
	};
	
	
}
