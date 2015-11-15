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
		SharedPreferences sh = context.getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
		String gstr = sh.getString("userDetail", null);
		Gson gs = new Gson();
		UserDetailInfo user = gs.fromJson(gstr, UserDetailInfo.class);
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
					UserDetailInfo u = new UserDetailInfo();
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
					//ChattMessageDB cdb = new ChattMessageDB(context, CM.getFromUserId());
					//cdb.addMsgToDB(CM);
				}else 
					Log.e("���������",CM.getChatMsgContent());
				
			}
		} catch (Exception e) {
			try {
				CommentDetailInformation comment = gson.fromJson(message,
						CommentDetailInformation.class);
				if(!user.getUsername().equals(comment.getComment_from_user_name())){
					Intent intent = new Intent(context, MainActivity.class);
					Bundle bundle = new Bundle();
			        //���֪ͨ������Ϣ���� ���ֽ���
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
		            //֪ͨ������
					String notifTitle = null;
					if(comment.getComment_type() == Comment.comment_type_��ս��)
						notifTitle = "��ս��";
					if(comment.getComment_type() == Comment.comment_type_����)
						notifTitle = "����";
					if(comment.getComment_type() == Comment.comment_type_���˴�)
						notifTitle = "���˴�";
					if(comment.getComment_type() == Comment.comment_type_���뺯)
						notifTitle = "���뺯";
					notifContent = comment.getComment_from_user_name()+"��������������"+notifTitle+"�Ͻ����������";
					//�Զ���֪ͨ����ʽ
					//���趨RemoteViews  
//		            RemoteViews view_custom = new RemoteViews(context.getPackageName(), R.layout.my_notification);
//					//�����Զ���ı�����
//		            view_custom.setTextViewText(R.id.notiTitle, notifTitle);
//		            view_custom.setTextViewText(R.id.notiContent, notifContent);
//		            view_custom.setImageViewResource(R.id.notiImage, R.drawable.ic_launcher);
//		            view_custom.setTextViewText(R.id.notiTime, System.currentTimeMillis()+"");
//		            mBuilder.setContent(view_custom);
		            mBuilder.setContentIntent(pendingIntent)//����֪ͨ�������ͼ 
		            .setContentTitle(notifTitle)
		            .setContentText(notifContent)
			        .setWhen(System.currentTimeMillis())//֪ͨ������ʱ�䣬����֪ͨ��Ϣ����ʾ��һ����ϵͳ��ȡ����ʱ��  
			        .setPriority(Notification.PRIORITY_DEFAULT) //���ø�֪ͨ���ȼ�  
			        .setAutoCancel(true)//���������־���û��������Ϳ�����֪ͨ���Զ�ȡ��    
			        .setOngoing(false)//ture��������Ϊһ�����ڽ��е�֪ͨ������ͨ����������ʾһ����̨����,�û���������(�粥������)����ĳ�ַ�ʽ���ڵȴ�,���ռ���豸(��һ���ļ�����,ͬ������,������������)  
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setDefaults(Notification.DEFAULT_VIBRATE);//��֪ͨ������������ƺ���Ч������򵥡���һ�µķ�ʽ��ʹ�õ�ǰ���û�Ĭ�����ã�ʹ��defaults���ԣ��������  
		            //����֪ͨ
		            manager.notify(1, mBuilder.build()); 
				}
			} catch (Exception e2) {
				Log.e("���Է�������json�쳣 comment", "����ʧ�ܷ�����Ϣ");
			}
			Log.e("���Է�������json�쳣dfdsfds", "����ʧ�ܺ�����Ϣ");
			Log.e("message������",message+"");
		}
	}

	@Override
	public void onNotificationClicked(Context context, String title,
            String description, String customContentString) {
		//��ҳ������ҳ
        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        //���֪ͨ������Ϣ���� ���ֽ���
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
