package com.example.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.ErWeiMaActivity;
import com.example.activity.MyfirendActivity;
import com.example.activity.ScanAndAddfriendActivity;
import com.example.activity.UserDetailInfoActivity;
import com.example.activity.UserLoginActivity;
import com.example.bean.User;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;

public class LastPageFragment extends Fragment {

	private View view;
	private RelativeLayout my_user;
	private ImageView my_user_pic;
	private TextView my_user_sign, my_user_name;
	private RelativeLayout my_friend, my_setting, myerweima_card, scan_addFriend;
	//如果当前用户没有登录则显示去登录按钮
	private Button go_to_login;
	private User user;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(R.layout.app_lastpage, container, false);
		 initview();
		 return view;
	}

	private void initview() {
		
		my_user = (RelativeLayout) view.findViewById(R.id.my_user);
		my_user_pic = (ImageView) view.findViewById(R.id.my_user_pic);
		my_user_sign = (TextView) view.findViewById(R.id.my_user_sign);
		my_user_name = (TextView) view.findViewById(R.id.my_user_name);
		my_friend = (RelativeLayout) view.findViewById(R.id.my_friend);
		my_setting = (RelativeLayout) view.findViewById(R.id.my_setting);
		go_to_login = (Button) view.findViewById(R.id.go_to_login);
		myerweima_card = (RelativeLayout) view.findViewById(R.id.myerweima_card);
		scan_addFriend = (RelativeLayout) view.findViewById(R.id.scan_addFriend);
		
		SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
		String userJson=preferences.getString("userJson", "defaultname");
		Gson gson = new Gson();
		user = gson.fromJson(userJson, User.class);  
        if(user.getUserId()==-1){
        	go_to_login.setVisibility(View.VISIBLE);
        	go_to_login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), UserLoginActivity.class);
					startActivity(intent);
					getActivity().finish();
				}
			});
        }else{
        	//如果当前用户已经登录，则显示切换账号的按钮
        	go_to_login.setVisibility(View.VISIBLE);
        	go_to_login.setText("切换");
        	go_to_login.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), UserLoginActivity.class);
					startActivity(intent);
					getActivity().finish();
				}
			});
        }
		//麻烦 ，如果跳到其他的Activity中，跳回来是很容易发生空指针
//		Bundle bundle = getActivity().getIntent().getExtras();
//		user = (User) bundle.getSerializable("user");
		
		my_user_name.setText(user.getUsername());
		
		//这里跳转到用户详情页面
		my_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(user.getUserId()!=-1){
					Intent intent = new Intent(getActivity(), UserDetailInfoActivity.class);
					//将用户的id传递过去
					Bundle bundle = new Bundle();
					bundle.putInt("userId", user.getUserId());
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
				}else{
					Toast.makeText(getActivity(), "亲，你还没有登录呢", Toast.LENGTH_LONG).show();
				}
			}
		});
		//我的好友界面
		my_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(user.getUserId()!=-1){
					Intent intent = new Intent(getActivity(), MyfirendActivity.class);
					//将用户的id传递过去
					Bundle bundle = new Bundle();
					bundle.putInt("userId", user.getUserId());
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
				}else{
					Toast.makeText(getActivity(), "亲，你还没有登录呢", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		//我的二维码页面
		myerweima_card.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(user.getUserId()!=-1){
					Intent intent = new Intent(getActivity(), ErWeiMaActivity.class);
					//将用户的id传递过去
					Bundle bundle = new Bundle();
					bundle.putInt("userId", user.getUserId());
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
				}else{
					Toast.makeText(getActivity(), "亲，你还没有登录呢", Toast.LENGTH_LONG).show();
				}
			}
		});
		//扫一扫加好友
		scan_addFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(user.getUserId()!=-1){
					Intent intent = new Intent(getActivity(), ScanAndAddfriendActivity.class);
					//将用户的id传递过去
					Bundle bundle = new Bundle();
					bundle.putInt("userId", user.getUserId());
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();	
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
}
