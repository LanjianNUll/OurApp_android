package com.example.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CircleImageView.CircleImageView;
import com.example.Data.defaultPacage;
import com.example.activity.ErWeiMaActivity;
import com.example.activity.MyfirendActivity;
import com.example.activity.ScanAndAddfriendActivity;
import com.example.activity.UserDetailInfoActivity;
import com.example.activity.UserLoginActivity;
import com.example.bean.UserDetailInfo;
import com.example.httpunit.HttpDoUserMsg;
import com.example.ourapp.R;
import com.google.gson.Gson;

public class LastPageFragment extends Fragment {

	private View view;
	private RelativeLayout my_user;
	private CircleImageView my_user_pic;
	private TextView my_user_sign, my_user_name;
	private RelativeLayout my_friend, my_setting, myerweima_card, scan_addFriend;
	//�����ǰ�û�û�е�¼����ʾȥ��¼��ť
	private Button go_to_login;
	private UserDetailInfo userDe =null;
	private int userId;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.app_lastpage, container, false);
		 initview();
		 return view;
	}

	private void initview() {
		
		my_user = (RelativeLayout) view.findViewById(R.id.my_user);
		my_user_pic = (CircleImageView) view.findViewById(R.id.my_user_pic);
		my_user_sign = (TextView) view.findViewById(R.id.my_user_sign);
		my_user_name = (TextView) view.findViewById(R.id.my_user_name);
		my_friend = (RelativeLayout) view.findViewById(R.id.my_friend);
		my_setting = (RelativeLayout) view.findViewById(R.id.my_setting);
		go_to_login = (Button) view.findViewById(R.id.go_to_login);
		myerweima_card = (RelativeLayout) view.findViewById(R.id.myerweima_card);
		scan_addFriend = (RelativeLayout) view.findViewById(R.id.scan_addFriend);
		
		//random headPic
		//Random �����
		int x=1+(int)(Math.random()*20);
		my_user_pic.setImageResource(defaultPacage.headpic[x]);
		
		//�õ�useid
		Bundle bundle = getActivity().getIntent().getExtras();
		if(bundle.getInt("fromLogin") == 1)
			userId = bundle.getInt("userId");
		else{
			SharedPreferences file = getActivity().getSharedPreferences("userDetailFile",
				Context.MODE_PRIVATE);
			String s = file.getString("userDetail", null);
			try {
				userId = new Gson().fromJson(s, UserDetailInfo.class).getUserId();
			} catch (Exception e) {
		}
		}
		
		//�û���һ�ν�����ô�дӵ�¼ҳ�����userId
		//�������δ�ǵ�¼ʱ
		if(userId == -1){
			SharedPreferences preferences = getActivity().getSharedPreferences("userDetailFile",
					Context.MODE_PRIVATE);
			my_user_name.setText(preferences.getString("UserName", "�ο�"));
			my_user_sign.setText(preferences.getString("UserSign", "ʲô��û����"));
			
			//��ʾ��¼��ť
        	go_to_login.setVisibility(View.VISIBLE);
        	go_to_login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), UserLoginActivity.class);
					startActivity(intent);
					getActivity().finish();
					//������ת�Ķ���
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
			});
        }else{
        	new GetUserDetailTask().execute(userId);
        	//�����ǰ�û��Ѿ���¼������ʾ�л��˺ŵİ�ť
        	go_to_login.setVisibility(View.VISIBLE);
        	go_to_login.setText("�л�");
        	go_to_login.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), UserLoginActivity.class);
					startActivity(intent);
					getActivity().finish();
					//������ת�Ķ���
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
			});
        }
		//������ת���û�����ҳ��
		my_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userId!=-1){
					Intent intent = new Intent(getActivity(), UserDetailInfoActivity.class);
					//���û���id���ݹ�ȥ
					Bundle bundle = new Bundle();
					bundle.putInt("userId", userId);
					bundle.putString("fromwhere", "LastPageFrament");
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
					//������ת�Ķ���
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					Toast.makeText(getActivity(), "�ף��㻹û�е�¼��", Toast.LENGTH_LONG).show();
				}
			}
		});
		//�ҵĺ��ѽ���
		my_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userId!=-1){
					Intent intent = new Intent(getActivity(), MyfirendActivity.class);
					//���û���id���ݹ�ȥ
					Bundle bundle = new Bundle();
					bundle.putInt("userId", userId);
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
					//������ת�Ķ���
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					Toast.makeText(getActivity(), "�ף��㻹û�е�¼��", Toast.LENGTH_LONG).show();
				}
			}
		});
		//�ҵĶ�ά��ҳ��
		myerweima_card.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userId!=-1){
					Intent intent = new Intent(getActivity(), ErWeiMaActivity.class);
					//���û���id���ݹ�ȥ
					Bundle bundle = new Bundle();
					bundle.putInt("userId", userId);
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
					//������ת�Ķ���
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					Toast.makeText(getActivity(), "�ף��㻹û�е�¼��", Toast.LENGTH_LONG).show();
				}
			}
		});
		//ɨһɨ�Ӻ���
		scan_addFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userId!=-1){
					Intent intent = new Intent(getActivity(), ScanAndAddfriendActivity.class);
					//���û���id���ݹ�ȥ
					Bundle bundle = new Bundle();
					bundle.putInt("userId", userId);
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
					//������ת�Ķ���
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					Toast.makeText(getActivity(), "�ף��㻹û�е�¼��", Toast.LENGTH_LONG).show();
				}
			}
		});
		//���ð�ť
		my_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	class GetUserDetailTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			HttpDoUserMsg httpDoUserMsg = new HttpDoUserMsg();
			userDe = httpDoUserMsg.getUserDetail(arg0[0]);
			if(userDe == null){
				return 0 ;
			}
			return 1;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				my_user_name.setText(userDe.getUsername());
				my_user_sign.setText(userDe.getMy_user_sign());
				SharedPreferences p = getActivity().getSharedPreferences("userDetailFile",
						Context.MODE_PRIVATE);
				Editor e = p.edit();
				e.putString("UserName", userDe.getUsername());
				e.putString("UserSign", userDe.getMy_user_sign());
				e.commit();
				//��ȡ�û��ļ�
				SharedPreferences userFile = getActivity().getSharedPreferences("userDetailFile",
						Context.MODE_PRIVATE);
				Editor userF = userFile.edit();
				userF.putString("userDetail", new Gson().toJson(userDe));
				userF.commit();
			}
			super.onPostExecute(result);
		}
		
	}
}
