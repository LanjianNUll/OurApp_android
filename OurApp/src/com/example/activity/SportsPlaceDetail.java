package com.example.activity;

import java.util.Arrays;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import com.example.adapter.SPCommentListAdapter;
import com.example.bean.SportPlaceComment;
import com.example.bean.SportPlaceDetailInformation;
import com.example.bean.User;
import com.example.httpunit.HttpGetSportPlaceJson;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;

public class SportsPlaceDetail extends Activity {
	private ImageView detail_sport_top_comeback;
	private SmartImageView detail_sport_place_pic_one, detail_sport_place_pic_two, detail_sport_place_pic_three;
	private TextView detail_sport_place_name, detail_sport_value,
			detail_sport_location, detail_sport_distance,
			detail_sport_place_messagetext, detail_sport_place_recommend_route;
	private int SportPlaceId = -1;
	private String sort_title;
	//所属分类的id
	private int sort_id = -1;
	private HttpGetSportPlaceJson httpgetdata;
	private SportPlaceDetailInformation spd;

	private ImageView jiazai_image;
	private RelativeLayout jiazai, errorpage;
	private ScrollView scrollview;
	private AnimationDrawable frameAnimation;
	//点评的按钮
	private TextView remark;
	private TextView open_time, suitPeople, makeFriendsIndex, deviceinfo; 
	private TextView shopone, shoptwo, shopthree;
	private ListView dianping_for_sport_place;
	private SPCommentListAdapter adapter;
	private Dialog dialog;

	private EditText editYourComment ;
	private RatingBar ratingBar;
	private Button cancel_sent, sent_comment;
	//用户类
	private User user = new User();
	//评论类
	private SportPlaceComment sPC = new SportPlaceComment();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sport_place_detail);
		initview();
	}

	private void initview() {
		open_time = (TextView) findViewById(R.id.open_time);
		suitPeople = (TextView) findViewById(R.id.suitPeople);
		makeFriendsIndex = (TextView) findViewById(R.id.makeFriendsIndex);
		deviceinfo = (TextView) findViewById(R.id.deviceinfo);
		shopone = (TextView) findViewById(R.id.shopone);
		shoptwo = (TextView) findViewById(R.id.shoptwo);
		shopthree = (TextView) findViewById(R.id.shopthree);
		dianping_for_sport_place = (ListView) findViewById(R.id.dianping_for_sport_place);
				
		// 从文件中取出cityID
		SharedPreferences getCityId = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		int cityID = getCityId.getInt("city_id", -1);
		//加载
		jiazai_image = (ImageView) findViewById(R.id.jiazai_image);
		jiazai = (RelativeLayout) findViewById(R.id.jiazai);
		//网络异常
		errorpage = (RelativeLayout) findViewById(R.id.errorpage);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		scrollview.setVisibility(View.INVISIBLE);
		//动画
		frameAnimation = (AnimationDrawable) jiazai_image.getBackground();
		detail_sport_top_comeback = (ImageView) findViewById(R.id.detail_sport_top_comeback);
		// 获取你原来的那一类详情页的name
		Bundle bundle = SportsPlaceDetail.this.getIntent().getExtras();
		sort_title = bundle.getString("name");
		SportPlaceId = bundle.getInt("SportPlaceId");
		sort_id = bundle.getInt("sort");
		//异步加载数据
		new Task().execute();
		detail_sport_top_comeback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 判断是从subSortActivity进来还是从nearby进入该详情的
				if (sort_title.equals("comefromSeconPage")) {
					Intent intent1 = new Intent(SportsPlaceDetail.this,
							MainActivity.class);
					Bundle bundle_comeback = new Bundle();
					bundle_comeback.putInt("CurrentItem", 1);
					intent1.putExtras(bundle_comeback);
					SportsPlaceDetail.this.startActivity(intent1);
					SportsPlaceDetail.this.finish();
					overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				} else {
					// 返回到进入详情页时的子类界面
					Intent intent = new Intent(SportsPlaceDetail.this,
							SubsSortActivity.class);
					Bundle bundle_comeback = new Bundle();
					bundle_comeback.putInt("sort", sort_id);
					bundle_comeback.putString("name", sort_title);
					intent.putExtras(bundle_comeback);
					SportsPlaceDetail.this.startActivity(intent);
					SportsPlaceDetail.this.finish();
					overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
			}
		});

		detail_sport_place_name = (TextView) findViewById(R.id.detail_sport_place_name);
		detail_sport_value = (TextView) findViewById(R.id.detail_sport_value);
		detail_sport_location = (TextView) findViewById(R.id.detail_sport_location);
		detail_sport_distance = (TextView) findViewById(R.id.detail_sport_distance);
		detail_sport_place_messagetext = (TextView) findViewById(R.id.detail_sport_place_messagetext);
		detail_sport_place_recommend_route = (TextView) findViewById(R.id.detail_sport_place_recommend_route);
		detail_sport_place_pic_one = (SmartImageView) findViewById(R.id.detail_sport_place_pic_one);
		detail_sport_place_pic_two = (SmartImageView) findViewById(R.id.detail_sport_place_pic_two);
		detail_sport_place_pic_three = (SmartImageView) findViewById(R.id.detail_sport_place_pic_three);
		//评论场地，发送场地评论
		remark = (TextView) findViewById(R.id.remark);
		remark.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//取到用户的id和name
				SharedPreferences preferences = SportsPlaceDetail.this.getSharedPreferences("user", Context.MODE_PRIVATE);
				String userJson=preferences.getString("userJson", null);
				Gson gson = new Gson();
				user = gson.fromJson(userJson, User.class);
				sPC.setSportPlaceId(SportPlaceId);
				sPC.setUserId(user.getUserId());
				sPC.setUserName(user.getUsername());
				displayDailog();
				
			}
		});
	}

	protected void displayDailog() {
		//Toast.makeText(getActivity(), "评分分类", 1000).show();
		dialog = new Dialog(this);
		Window dialogWindow2 = dialog.getWindow();
        WindowManager.LayoutParams lp2 = dialogWindow2.getAttributes();//获取对话框的参数
        //获取屏幕大小
        WindowManager windowmanager2 = getWindowManager();
        Display display2 = windowmanager2.getDefaultDisplay();
		DisplayMetrics displayMetrics2 = new DisplayMetrics();
		display2.getMetrics(displayMetrics2);
        //去除标题栏
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//自定义对话框layout
		dialog.setContentView(R.layout.dialogitem_sent_comment);
		
		lp2.height = displayMetrics2.heightPixels;
		lp2.width = displayMetrics2.widthPixels;		
	    dialogWindow2.setAttributes(lp2);
		dialog.show();
		
		editYourComment = (EditText) dialog.findViewById(R.id.editYourComment);
		ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
		cancel_sent = (Button) dialog.findViewById(R.id.cancel_sent_comment);
		sent_comment = (Button) dialog.findViewById(R.id.sent_sport_comment);
		sent_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				sPC.setStartRange(ratingBar.getRating());
				sPC.setCommentTime(new Date());
				sPC.setCommentComtent(editYourComment.getText().toString());
				new sentCommentContentTask().execute();
				//更新评论列表
				new notififindDetailinfoTask().execute();
				dialog.cancel();
			}
		});
		cancel_sent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				editYourComment.setText("");
				dialog.cancel();
			}
		});	
	}

	protected void filldatatoview() {
		
		detail_sport_place_pic_one.setImageUrl(HttpGetSportPlaceJson.RootURL+spd.getMoreImageUrl()[0]);
		detail_sport_place_pic_two.setImageUrl(HttpGetSportPlaceJson.RootURL+spd.getMoreImageUrl()[1]);
		detail_sport_place_pic_three.setImageUrl(HttpGetSportPlaceJson.RootURL+spd.getMoreImageUrl()[2]);
		
		detail_sport_place_name.setText(spd.getSportplace_name());
		detail_sport_value.setText(spd.getSportplace_value());
		detail_sport_location.setText(spd.getSportplace_location());
		detail_sport_distance.setText(spd.getSportplace_distance());
		detail_sport_place_messagetext.setText(spd.getSportplace_discrb());
		detail_sport_place_recommend_route.setText(spd.getBus_route());
		
		open_time.setText(spd.getOpenTime());
		suitPeople.setText(spd.getPeopleLimit());
		makeFriendsIndex.setText(""+spd.getMakeFriendIndex());
		deviceinfo.setText(""+spd.getDeivceRequest());
		shopone.setText(spd.getSurrondingShop()[0]);
		shoptwo.setText(spd.getSurrondingShop()[1]);
		shopthree.setText(spd.getSurrondingShop()[2]);
		//加载完将scrollview置于页面开始状态
		scrollview.scrollTo(0, 0);  
		scrollview.smoothScrollTo(0, 0);  
	}
	
	public class sentCommentContentTask extends AsyncTask<Void, Integer, String >{

		@Override
		protected void onPreExecute() {
			Toast.makeText(SportsPlaceDetail.this, "发送中...", 1000).show();			
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			
			httpgetdata = new HttpGetSportPlaceJson();
			httpgetdata.sentHttpSportPlaceComment(sPC);  
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			if (SportsPlaceDetail.this == null)   
		        return;  
			Toast.makeText(SportsPlaceDetail.this, "发送成功", 1000).show();
			super.onPostExecute(result);
		}
}
	public class Task extends AsyncTask<String , Integer, Integer>{

		@Override
		protected void onPreExecute() {
			jiazai.setVisibility(View.VISIBLE);
			frameAnimation.start();
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(String... arg0) {
			httpgetdata = new HttpGetSportPlaceJson();
			spd = httpgetdata.getSportPlaceDetali(SportPlaceId);
			if(spd == null)
				return -1;
			else
				return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				jiazai.setVisibility(View.GONE);
				scrollview.setVisibility(View.VISIBLE);
				dianping_for_sport_place.setAdapter(adapter = 
						new SPCommentListAdapter(SportsPlaceDetail.this,spd.getSPcomment()));
				setListViewHeight(dianping_for_sport_place);
				filldatatoview();
			}else{
				jiazai.setVisibility(View.GONE);
				errorpage.setVisibility(View.VISIBLE);
			}
			Log.v("onPosetExecute","iamhere");
			super.onPostExecute(result);
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			Log.v("pnProgress","iamhere");
		}	
	}
	//点评后更新详情界面评论页面
	public class notififindDetailinfoTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			httpgetdata = new HttpGetSportPlaceJson();
			spd = httpgetdata.getSportPlaceDetali(SportPlaceId);
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dianping_for_sport_place.setAdapter(adapter = 
					new SPCommentListAdapter(SportsPlaceDetail.this,spd.getSPcomment()));
			super.onPostExecute(result);
		}
	}

    /**  
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题  
     * @param listView  
     */  
    public void setListViewHeight(ListView listView) {    
            
        // 获取ListView对应的Adapter    
        
        adapter = (SPCommentListAdapter) listView.getAdapter();    
        
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
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//跳回MaimActivity
			// 判断是从subSortActivity进来还是从nearby进入该详情的
			if (sort_title.equals("comefromSeconPage")) {
				Intent intent1 = new Intent(SportsPlaceDetail.this,
						MainActivity.class);
				Bundle bundle_comeback = new Bundle();
				bundle_comeback.putInt("CurrentItem", 1);
				intent1.putExtras(bundle_comeback);
				SportsPlaceDetail.this.startActivity(intent1);
				SportsPlaceDetail.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			} else {
				// 返回到进入详情页时的子类界面
				Intent intent = new Intent(SportsPlaceDetail.this,
						SubsSortActivity.class);
				Bundle bundle_comeback = new Bundle();
				bundle_comeback.putString("name", sort_title);
				intent.putExtras(bundle_comeback);
				SportsPlaceDetail.this.startActivity(intent);
				SportsPlaceDetail.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
