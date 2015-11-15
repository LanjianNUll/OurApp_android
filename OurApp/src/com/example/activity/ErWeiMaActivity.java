package com.example.activity;

import com.example.CircleImageView.CircleImageView;
import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ErWeiMaActivity extends Activity {

	private TextView Myerweima_username;
	private ImageView MyErweima_card_pic;
	private ImageView MyErweimaComeback;
	private CircleImageView userPic;
	private SharedPreferences sharedpreferences;
	private UserDetailInfo user;
	private int userId;
	private String erWeiMaMsg = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.erweima_card_layout);
		initview();
	}

	private void initview() {
		userPic = (CircleImageView) findViewById(R.id.userPic);
		
		//获取用户信息
		sharedpreferences = getSharedPreferences("userDetailFile",Context.MODE_PRIVATE);
		String userJson=sharedpreferences.getString("userDetail", null);
		erWeiMaMsg = userJson;
		Gson gson = new Gson();
		user = gson.fromJson(userJson, UserDetailInfo.class);
		//获取用户id
		Bundle bund = getIntent().getExtras();
		userId = bund.getInt("userId");
		
		Myerweima_username = (TextView) findViewById(R.id.Myerweima_username);
		Myerweima_username.setText(user.getUsername());
		
		MyErweimaComeback = (ImageView) findViewById(R.id.MyErweimaComeback);
		MyErweimaComeback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ErWeiMaActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("CurrentItem", 3);
				intent.putExtras(bundle);
				ErWeiMaActivity.this.startActivity(intent);
				ErWeiMaActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		});
		
		MyErweima_card_pic = (ImageView) findViewById(R.id.MyErweima_card_pic);
		Myerweima_username = (TextView) findViewById(R.id.Myerweima_username);
		
		if (!TextUtils.isEmpty(erWeiMaMsg)) {
			try {
				Bitmap bitmap=EncodingHandler.createQRCode(erWeiMaMsg, 150);
				MyErweima_card_pic.setImageBitmap(bitmap);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(ErWeiMaActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("CurrentItem", 3);
			intent.putExtras(bundle);
			ErWeiMaActivity.this.startActivity(intent);
			ErWeiMaActivity.this.finish();
			overridePendingTransition(R.drawable.interface_jump_in,
					R.drawable.interface_jump_out);
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
