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
	//动画的加载
	private LinearLayout loading;
	private ImageView jiazai;
	private AnimationDrawable frameAnimation;
	private TextView errorpage;
	private TextView userDetailName, userDetailState, userDetailSex, 
	userDetailNearLocation, usersignDetail;
	private String wherefrom = null;
	//留言板
	private ListView userDetailListView; 
	//用户的userId
	private int userId;
	//用户类
	private UserDetailInfo userDInfo;
	//适配器
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
		//Random 随机数
		int x=1+(int)(Math.random()*20);
		defaultheadCircle.setImageResource(defaultPacage.headpic[x]);
		//获取用户的userId
		Bundle bundle = this.getIntent().getExtras();
		userId = bundle.getInt("userId");
		wherefrom = bundle.getString("fromwhere");
		//留言加载动画
		loading = (LinearLayout) findViewById(R.id.loading);
		jiazai = (ImageView) findViewById(R.id.jiazai);
		errorpage = (TextView) findViewById(R.id.errorpage);
		frameAnimation = (AnimationDrawable) jiazai.getBackground();
		//用户详情view
		userDetailName = (TextView) findViewById(R.id.userDetailName);
		userDetailState = (TextView) findViewById(R.id.userDetailState);
		userDetailSex = (TextView) findViewById(R.id.userDetailSex);
		userDetailNearLocation = (TextView) findViewById(R.id.userDetailNearLocation);
		userDetailListView = (ListView) findViewById(R.id.userDetailListView);
		usersignDetail = (TextView) findViewById(R.id.usersignDetail);
		userDetailComeback = (ImageView) findViewById(R.id.userDetailComeback);
		//网络获取数据
		new GetUserDInfoTask().execute(userId);
		userDetailComeback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = null;
				int w = -1;
				//判断从那来的从那
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
			//判断从那来的从那
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
				Log.v("网络异常","苏侧耳");
				jiazai.setVisibility(View.GONE);
				errorpage.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}	
	}
	public void afterDo() {
		//网络请求成功后，填充数据
		userDetailName.setText(userDInfo.getUsername());
		String userState = null;
		if(userDInfo.getUser_state() == User.state_冒泡)
			userState = "冒泡";
		if(userDInfo.getUser_state() == User.state_宅客)
			userState = "宅客";
		if(userDInfo.getUser_state() == User.state_活跃)
			userState = "活跃";
		if(userDInfo.getUser_state() == User.state_运动挚友)
			userState = "运动挚友";
		if(userDInfo.getUser_state() == User.state_运动达人)
			userState = "运动达人";
		if(userDInfo.getUser_state() == User.state_运动狂)
			userState = "运动狂";
		userDetailState.setText(userState);
		String sex = null;
		if(userDInfo.getSexId() == User.sex_define)
			sex = "未填写";
		if(userDInfo.getSexId() == User.sex_man)
			sex = "男";
		if(userDInfo.getSexId() == User.sex_women)
			sex = "女";
		userDetailSex.setText(sex);
		userDetailNearLocation.setText(userDInfo.getLocation_lasetime_login());
		userDetailListView.setAdapter(adapter = new LeaveWordListViewAdapter(this, userDInfo.getLeaveword()));
		setListViewHeight(userDetailListView);
	}
	 /**  
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题  
     * @param listView  
     */  
    public void setListViewHeight(ListView listView) {    
        // 获取ListView对应的Adapter    
        adapter = (LeaveWordListViewAdapter) listView.getAdapter();    
        if (adapter == null) {    
            return;    
        }    
        int totalHeight = 0;    
        for (int i = 0, len = adapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目    
            View listItem = adapter.getView(i, null, listView);    
            listItem.measure(0, 0); // 计算子项View 的宽高    
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度    
        }    
        ViewGroup.LayoutParams params = listView.getLayoutParams();    
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));    
        listView.setLayoutParams(params);    
    } 
}
