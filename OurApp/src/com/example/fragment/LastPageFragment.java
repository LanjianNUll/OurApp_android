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
	//如果当前用户没有登录则显示去登录按钮
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
		//Random 随机数
		int x=1+(int)(Math.random()*20);
		my_user_pic.setImageResource(defaultPacage.headpic[x]);
		
		//拿到useid
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
		
		//用户第一次进入是么有从登录页面过来userId
		//无网络或未登登录时
		if(userId == -1){
			SharedPreferences preferences = getActivity().getSharedPreferences("userDetailFile",
					Context.MODE_PRIVATE);
			my_user_name.setText(preferences.getString("UserName", "游客"));
			my_user_sign.setText(preferences.getString("UserSign", "什么都没留下"));
			
			//显示登录按钮
        	go_to_login.setVisibility(View.VISIBLE);
        	go_to_login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), UserLoginActivity.class);
					startActivity(intent);
					getActivity().finish();
					//界面跳转的动画
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
			});
        }else{
        	new GetUserDetailTask().execute(userId);
        	//如果当前用户已经登录，则显示切换账号的按钮
        	go_to_login.setVisibility(View.VISIBLE);
        	go_to_login.setText("切换");
        	go_to_login.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), UserLoginActivity.class);
					startActivity(intent);
					getActivity().finish();
					//界面跳转的动画
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
			});
        }
		//这里跳转到用户详情页面
		my_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userId!=-1){
					Intent intent = new Intent(getActivity(), UserDetailInfoActivity.class);
					//将用户的id传递过去
					Bundle bundle = new Bundle();
					bundle.putInt("userId", userId);
					bundle.putString("fromwhere", "LastPageFrament");
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
					//界面跳转的动画
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					Toast.makeText(getActivity(), "亲，你还没有登录呢", Toast.LENGTH_LONG).show();
				}
			}
		});
		//我的好友界面
		my_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userId!=-1){
					Intent intent = new Intent(getActivity(), MyfirendActivity.class);
					//将用户的id传递过去
					Bundle bundle = new Bundle();
					bundle.putInt("userId", userId);
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
					//界面跳转的动画
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					Toast.makeText(getActivity(), "亲，你还没有登录呢", Toast.LENGTH_LONG).show();
				}
			}
		});
		//我的二维码页面
		myerweima_card.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userId!=-1){
					Intent intent = new Intent(getActivity(), ErWeiMaActivity.class);
					//将用户的id传递过去
					Bundle bundle = new Bundle();
					bundle.putInt("userId", userId);
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
					//界面跳转的动画
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					Toast.makeText(getActivity(), "亲，你还没有登录呢", Toast.LENGTH_LONG).show();
				}
			}
		});
		//扫一扫加好友
		scan_addFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(userId!=-1){
					Intent intent = new Intent(getActivity(), ScanAndAddfriendActivity.class);
					//将用户的id传递过去
					Bundle bundle = new Bundle();
					bundle.putInt("userId", userId);
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
					//界面跳转的动画
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}else{
					Toast.makeText(getActivity(), "亲，你还没有登录呢", Toast.LENGTH_LONG).show();
				}
			}
		});
		//设置按钮
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
				//存取用户文件
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
