package com.example.activity;

import java.util.Random;

import com.example.CircleImageView.CircleImageView;
import com.example.Data.defaultPacage;
import com.example.adapter.LeaveWordListViewAdapter;
import com.example.adapter.SPCommentListAdapter;
import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.example.httpunit.HttpDoUserMsg;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class UserDetailInfoActivity extends Activity {
	private ImageView userDetailComeback;
	private CircleImageView defaultheadCircle;
	//�����ļ���
	private LinearLayout loading;
	private ImageView jiazai;
	private AnimationDrawable frameAnimation;
	private TextView errorpage;
	private TextView userDetailName, userDetailState, userDetailSex, 
	userDetailNearLocation, usersignDetail;
	private String wherefrom = null;
	//���԰�
	private ListView userDetailListView; 
	//�û���userId
	private int userId;
	//�û���
	private UserDetailInfo userDInfo;
	//������
	private LeaveWordListViewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_detail_information);
		initview();
	}
	private void initview() {
		//headPic
		defaultheadCircle = (CircleImageView) findViewById(R.id.defaultheadCircle);
		//Random �����
		int x=1+(int)(Math.random()*20);
		defaultheadCircle.setImageResource(defaultPacage.headpic[x]);
		//��ȡ�û���userId
		Bundle bundle = this.getIntent().getExtras();
		userId = bundle.getInt("userId");
		wherefrom = bundle.getString("fromwhere");
		//���Լ��ض���
		loading = (LinearLayout) findViewById(R.id.loading);
		jiazai = (ImageView) findViewById(R.id.jiazai);
		errorpage = (TextView) findViewById(R.id.errorpage);
		frameAnimation = (AnimationDrawable) jiazai.getBackground();
		//�û�����view
		userDetailName = (TextView) findViewById(R.id.userDetailName);
		userDetailState = (TextView) findViewById(R.id.userDetailState);
		userDetailSex = (TextView) findViewById(R.id.userDetailSex);
		userDetailNearLocation = (TextView) findViewById(R.id.userDetailNearLocation);
		userDetailListView = (ListView) findViewById(R.id.userDetailListView);
		usersignDetail = (TextView) findViewById(R.id.usersignDetail);
		userDetailComeback = (ImageView) findViewById(R.id.userDetailComeback);
		//�����ȡ����
		new GetUserDInfoTask().execute(userId);
		userDetailComeback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = null;
				int w = -1;
				//�жϴ������Ĵ���
				if(!wherefrom.equals("findDetailListView")){
					if(wherefrom.equals("LastPageFrament"))
						w = 3;
					if(wherefrom.equals("findSortListView"))
						w = 2;
				intent = new Intent(UserDetailInfoActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", w);
				intent.putExtras(bundle);
				UserDetailInfoActivity.this.startActivity(intent);
				UserDetailInfoActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
				}
				if(wherefrom.equals("findDetailListView")){
					intent = new Intent(UserDetailInfoActivity.this, FindDetailsActivity.class);
					Bundle bundle = new Bundle();
					intent.putExtras(bundle);
					UserDetailInfoActivity.this.startActivity(intent);
					UserDetailInfoActivity.this.finish();
					overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = null;
			int w = -1;
			//�жϴ������Ĵ���
			if(!wherefrom.equals("findDetailListView")){
				if(wherefrom.equals("LastPageFrament"))
					w = 3;
				if(wherefrom.equals("findSortListView"))
					w = 2;
			intent = new Intent(UserDetailInfoActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem", w);
			intent.putExtras(bundle);
			UserDetailInfoActivity.this.startActivity(intent);
			UserDetailInfoActivity.this.finish();
			overridePendingTransition(R.drawable.interface_jump_in,
					R.drawable.interface_jump_out);
			}
			if(wherefrom.equals("findDetailListView")){
				intent = new Intent(UserDetailInfoActivity.this, FindDetailsActivity.class);
				Bundle bundle = new Bundle();
				intent.putExtras(bundle);
				UserDetailInfoActivity.this.startActivity(intent);
				UserDetailInfoActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	class GetUserDInfoTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected void onPreExecute() {
			frameAnimation.start();
			jiazai.setVisibility(View.VISIBLE);
			userDetailListView.setVisibility(View.GONE);
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {
			HttpDoUserMsg httpDoUserMsg = new HttpDoUserMsg();
			userDInfo = httpDoUserMsg.getUserDetail(arg0[0]);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(userDInfo == null)
				return -1;
			else 
				return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				loading.setVisibility(View.GONE);
				userDetailListView.setVisibility(View.VISIBLE);
				afterDo();
			}else{
				Log.v("�����쳣","�ղ��");
				jiazai.setVisibility(View.GONE);
				errorpage.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}	
	}
	public void afterDo() {
		//��������ɹ����������
		userDetailName.setText(userDInfo.getUsername());
		String userState = null;
		if(userDInfo.getUser_state() == User.state_ð��)
			userState = "ð��";
		if(userDInfo.getUser_state() == User.state_լ��)
			userState = "լ��";
		if(userDInfo.getUser_state() == User.state_��Ծ)
			userState = "��Ծ";
		if(userDInfo.getUser_state() == User.state_�˶�ֿ��)
			userState = "�˶�ֿ��";
		if(userDInfo.getUser_state() == User.state_�˶�����)
			userState = "�˶�����";
		if(userDInfo.getUser_state() == User.state_�˶���)
			userState = "�˶���";
		userDetailState.setText(userState);
		String sex = null;
		if(userDInfo.getSexId() == User.sex_define)
			sex = "δ��д";
		if(userDInfo.getSexId() == User.sex_man)
			sex = "��";
		if(userDInfo.getSexId() == User.sex_women)
			sex = "Ů";
		userDetailSex.setText(sex);
		userDetailNearLocation.setText(userDInfo.getLocation_lasetime_login());
		userDetailListView.setAdapter(adapter = new LeaveWordListViewAdapter(this, userDInfo.getLeaveword()));
		setListViewHeight(userDetailListView);
	}
	 /**  
     * ���¼���ListView�ĸ߶ȣ����ScrollView��ListView����View���й�����Ч������Ƕ��ʹ��ʱ���ͻ������  
     * @param listView  
     */  
    public void setListViewHeight(ListView listView) {    
        // ��ȡListView��Ӧ��Adapter    
        adapter = (LeaveWordListViewAdapter) listView.getAdapter();    
        if (adapter == null) {    
            return;    
        }    
        int totalHeight = 0;    
        for (int i = 0, len = adapter.getCount(); i < len; i++) { // listAdapter.getCount()�������������Ŀ    
            View listItem = adapter.getView(i, null, listView);    
            listItem.measure(0, 0); // ��������View �Ŀ��    
            totalHeight += listItem.getMeasuredHeight(); // ͳ������������ܸ߶�    
        }    
        ViewGroup.LayoutParams params = listView.getLayoutParams();    
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));    
        listView.setLayoutParams(params);    
    } 
}
