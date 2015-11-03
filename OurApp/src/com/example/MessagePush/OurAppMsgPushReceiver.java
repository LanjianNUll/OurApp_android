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
		Log.v("���ص���Ϣ", responseString);
			//��Ϣ����
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
     * ����͸����Ϣ�ĺ�����
     * 
     * @param context
     *            ������
     * @param message
     *            ���͵���Ϣ
     * @param customContentString
     *            �Զ�������,Ϊ�ջ���json�ַ���
     */
	@Override
	public void onMessage(Context context, String message, String customContentString) {
		//��ȡ�û���userid
		SharedPreferences sh = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		String gstr = sh.getString("userJson", null);
		Gson gs = new Gson();
		User user = gs.fromJson(gstr, User.class);
		int myUserId = user.getUserId();
		Log.e("msg����˵���Ϣ", message);
		//����Ϣ���
		ChatMessage CM = null;
		Gson gson = new Gson();
		try {
			CM = gson.fromJson(message, ChatMessage.class);	
			Log.e("gson�����ɹ�","");
			if(CM != null && CM.getChatMsgContent().equals("@#$deleteFriend")){//�ж��Ƿ�Ϊɾ�����ѵ���Ϣ
				/*����ɾ�����ѵĲ���*/
				MyFriendGroupDB myFdb = new MyFriendGroupDB(context);
				//�ж��Ƿ񷢸��Լ���ɾ����Ϣ
				if(CM.getToUserId() == myUserId){
					myFdb.delUserRID(CM.getFromUserId());
					Log.d("ɾ������","success");
				}
				Log.e("ɾ������","");
			}
			else if(CM != null && CM.getChatMsgContent().equals("@#$addFriend")){//�ж��Ƿ�Ӻ��ѵ���Ϣ
				MyFriendGroupDB myFdb = new MyFriendGroupDB(context);
				//�ж��Ƿ񷢸��Լ��ļӺ�����Ϣ
				if(CM.getToUserId() == myUserId){//ȡ���������ַ���
					User u = new User();
					u.setUserId(CM.getFromUserId());
					u.setUsername(CM.getFromUserName());
					u.setMy_user_sign("������Ҫ�ӷ���������");
					myFdb.AddFriend(u);
					Log.e("��Ӻ���","success");
				}	
				Log.e("��Ӻ���","");
			}else{//��ͨ��Ϣ
				//�ж��Ƿ���ĺ��ѣ�Ĭ����false
				boolean isYourFirend = false;
				MyFriendGroupDB myFdb = new MyFriendGroupDB(context);
				//�ж��Ƿ񷢸��Լ�����Ϣ���ж��Ƿ��������
				if(myFdb.getFriend(CM.getFromUserId())!= null)
					isYourFirend = true;
	//			SharePreferenceUtil spUtil = new SharePreferenceUtil(context, "myFriensGroup");
	//			int[] myFGId = spUtil.readForSharePfInt("myFriendGroupIds");
	//			boolean isYourFirend = false;
	//			for (int i : myFGId)
	//				if(i == CM.getFromUserId())
	//					isYourFirend = true;
	
				if(CM != null && isYourFirend){
					if (msgListeners.size() > 0){// �м�����ʱ�򣬴�����ȥ
						for (int i = 0; i < msgListeners.size(); i++)
							msgListeners.get(i).onNewMessage(CM);
					}
					//��������Ϣ�������ݿ�
					ChattMessageDB cdb = new ChattMessageDB(context, CM.getFromUserId());
					cdb.addMsgToDB(CM);
				}else 
					Log.e("���������",CM.getChatMsgContent());
				
			}
		} catch (Exception e) {
			Log.e("���Է�������json�쳣", "����ʧ��");
			Log.e("message������",message+"");
		}
	}

	@Override
	public void onNotificationClicked(Context context, String arg1, String arg2,
			String arg3) {
		//��ҳ������ҳ
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
