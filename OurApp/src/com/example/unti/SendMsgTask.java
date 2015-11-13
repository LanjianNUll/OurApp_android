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
		//���캯���� �õ�push������
		this.cMsg = cMsgJson;
		this.mBaiduPush = Application.getInstance().getBaiduPush();
		this.mHandler = new Handler();
	}
	//����͸����Ϣ
	public void send() {
		mTask = new MyAsyncTask();
		mTask.execute();	
	}
	//����֪ͨ����֪ͨ
	public void sendNoti(){
		new SendNotiTask().execute(cMsg);
	}
	//����֪ͨ����֪ͨ�첽
	class SendNotiTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... arg0) {
			CommentDetailInformation comment = 
					new Gson().fromJson(arg0[0], CommentDetailInformation.class);
			//����֪ͨ������
			String notifContent = null;
			String notifTitle = null;
			if(comment.getComment_type() == Comment.comment_type_��ս��)
				notifTitle = "��ս��";
			if(comment.getComment_type() == Comment.comment_type_����)
				notifTitle = "����";
			if(comment.getComment_type() == Comment.comment_type_���˴�)
				notifTitle = "���˴�";
			if(comment.getComment_type() == Comment.comment_type_���뺯)
				notifTitle = "���뺯";
			notifContent = comment.getComment_from_user_name()+"��������������һ��"+notifTitle+"�Ͻ����������";
			String result = mBaiduPush.PushNotifyAll(notifTitle,notifContent);
//			���͵��Զ�������
//			String result = mBaiduPush.PushMessage(cMsg);
			return result;
		}
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			Log.e("send msg result:", result);
			if (result.contains(BaiduPush.SEND_MSG_ERROR)){// �����Ϣ����ʧ�ܣ���100ms���ط�
				mHandler.postDelayed(reSend, 100);
			} else{
				if (mListener != null)
					mListener.sendScuess();
			}
		}	
		
	}
	///����͸����Ϣ�첽
	class MyAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			Log.e("�����ͣ����͵���Ϣ" , cMsg);
			String result = mBaiduPush.PushMessage(cMsg);
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result){
			super.onPostExecute(result);
			Log.e("send msg result:", result);
			if (result.contains(BaiduPush.SEND_MSG_ERROR)){// �����Ϣ����ʧ�ܣ���100ms���ط�
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
			//����һ�µ��߳����ط�
			Log.v("���·���", "resend msg...");
			send();// �ط�
		}
	};
}
