package com.example.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.CircleImageView.CircleImageView;
import com.example.Data.defaultPacage;
import com.example.adapter.FindDetaiListViewAdapter;
import com.example.adapter.ImageListViewAdapter;
import com.example.bean.CommentDetailInformation;
import com.example.bean.OtherPeopleComment;
import com.example.bean.User;
import com.example.httpunit.HttpGetCommentJson;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FindDetailsActivity extends Activity {

	private static final long ONE_MINUTE = 60 * 1000;
	private static final long ONE_HOUR = 60 * ONE_MINUTE;
	private static final long ONE_DAY = 24 * ONE_HOUR;
	private ImageView find_detail_top_comeback;
	//用户相关
	private TextView find_detail_user_name, find_detail_user_state;
	private CircleImageView find_detail_user_define_pic;
	//评论正文
	private TextView find_detail_content;
	//评论正文的图片组
	private ListView find_detail_pic_listView;
	//评论的一些相关属性
	private TextView commentNow, find_detail_comment_count_de, parseNow,
	find_detail_parse_count, find_detail_time;
	//其他用户评论组
	private ListView find_detail_comment_list_view;
	//适配
	private FindDetaiListViewAdapter otherPCadapter;
	//评论详情
	private CommentDetailInformation CDInfoData = new CommentDetailInformation();
	//请求一个网络连接的对象
	private HttpGetCommentJson httpGetCommentJson = new HttpGetCommentJson();
	//定义一个booleal记录用户是否点击了赞
	private boolean isClickParse = false;
	//定义当前用户是否登录
	private boolean isLogin = false;
	//评论的Id
	private int commentId;
	//评论照片墙的listView
	private ImageListViewAdapter picAdapter;
	private ScrollView scrollview;
	//从文件中获取用户信息
	private SharedPreferences sharedpreferences;
	private User user;
	//网络异常
	private RelativeLayout errorpage;
	//布局
	private RelativeLayout user_name_about, find_detail_pic_layout, 
					find_detail_parse_comment;
	private LinearLayout commentabout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.find_detail_page);
		initview();
	}

	private void initview() {//获取用户信息
		sharedpreferences = getSharedPreferences("user",Context.MODE_PRIVATE);
		String userJson=sharedpreferences.getString("userJson", null);
		Gson gson = new Gson();
		user = gson.fromJson(userJson, User.class);
		//获取评论id
		Bundle bund = getIntent().getExtras();
		commentId = bund.getInt("commentId");
		//初始化view
		find_detail_top_comeback = (ImageView) findViewById(R.id.find_detail_top_comeback);
		find_detail_user_name = (TextView) findViewById(R.id.find_detail_user_name);
		find_detail_user_state = (TextView) findViewById(R.id.find_detail_user_state);
		find_detail_user_define_pic = (CircleImageView) findViewById(R.id.find_detail_user_define_pic);
		find_detail_content = (TextView) findViewById(R.id.find_detail_content);
		find_detail_pic_listView = (ListView) findViewById(R.id.find_detail_pic_listView);
		commentNow = (TextView) findViewById(R.id.commentNow);
		find_detail_comment_count_de = (TextView) findViewById(R.id.find_detail_comment_count_de);
		parseNow = (TextView) findViewById(R.id.parseNow);
		find_detail_parse_count = (TextView) findViewById(R.id.find_detail_parse_count);
		find_detail_time = (TextView) findViewById(R.id.find_detail_time);
		find_detail_comment_list_view = (ListView) findViewById(R.id.find_detail_comment_list_view);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		
		user_name_about = (RelativeLayout) findViewById(R.id.user_name_about);
		find_detail_pic_layout = (RelativeLayout) 
				findViewById(R.id.find_detail_pic_layout);
		find_detail_parse_comment = (RelativeLayout) 
				findViewById(R.id.find_detail_parse_comment);
		commentabout = (LinearLayout) findViewById(R.id.commentabout);
		//网络异常
		errorpage = (RelativeLayout) findViewById(R.id.errorpage);
		//返回发现页面
		find_detail_top_comeback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent1 = new Intent(FindDetailsActivity.this, MainActivity.class);
				Bundle bundle_comeback = new Bundle();
				bundle_comeback.putInt("CurrentItem", 2);
				intent1.putExtras(bundle_comeback);
				FindDetailsActivity.this.startActivity(intent1);
				FindDetailsActivity.this.finish();
				overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
			}
		});
//		//屏蔽在find_detail_pic_listView中的scrollview的点击事件
//		find_detail_pic_listView.setOnTouchListener(new OnTouchListener() { 
//			        @Override public boolean onTouch(View arg0, MotionEvent arg1) { 
//			        	scrollview.requestDisallowInterceptTouchEvent(true); 
//					 return false; 
//		                  }	
//		            });
		//屏蔽在find_detail_comment_list_view中的scrollview的点击事件
		find_detail_comment_list_view.setOnTouchListener(new OnTouchListener() { 
	        @Override public boolean onTouch(View arg0, MotionEvent arg1) { 
	        	scrollview.requestDisallowInterceptTouchEvent(true); 
			 return false; 
                  }	
            });
		
		//加载网络数据进来
		new CDInfoDataTask().execute();
		//点击comment的处理
		commentNow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//判断用户是否已经登录
				if(user.getUserId()==-1){
					AlertDialog.Builder ab = new AlertDialog.Builder(FindDetailsActivity.this);
					ab.setTitle("");
					ab.setMessage("亲，登录后才能评论哦~");
					ab.setPositiveButton("去登录", new DialogInterface.OnClickListener() {						
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(FindDetailsActivity.this, UserLoginActivity.class);
							FindDetailsActivity.this.startActivity(intent);
							FindDetailsActivity.this.finish();
							overridePendingTransition(R.drawable.interface_jump_in,
									R.drawable.interface_jump_out);
						}
					});
					ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.cancel();
						}
					});
					// create alert dialog
					AlertDialog alertDialog = ab.create();
					// show it
					alertDialog.show();
				}else{
					//用户登录了并评论
					displayCommentDailog();
				}
			}
		});
		//点击赞的处理
		parseNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//判断用户是否已经点赞
				if(isClickParse)
					Toast.makeText(FindDetailsActivity.this, "你已经赞过了", 1000).show();
				else{				
					parseNow.setText("已赞");
					;
					find_detail_parse_count.setText(Integer.parseInt(find_detail_parse_count.getText().toString())+1+"");
					isClickParse = true;
					new SentParseAddOneTask();	
				}
			}
		});			
	}
	protected void displayCommentDailog() {
		final Dialog dialog = new Dialog(this);
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
		dialog.setContentView(R.layout.dialogitem_find_sent_comment);
		
		lp2.height = displayMetrics2.heightPixels;
		lp2.width = displayMetrics2.widthPixels;		
	    dialogWindow2.setAttributes(lp2);
		dialog.show();
		//初始化dailog的布局
		Button cancle_sent_find_comment = (Button) dialog.findViewById(R.id.cancle_sent_find_comment);
		Button confirm_sent_find_comment = (Button) dialog.findViewById(R.id.confirm_sent_find_comment);
		final EditText edit_find_comment_content = (EditText) dialog.findViewById(R.id.edit_find_comment_content);
		//取消评论的操作
		cancle_sent_find_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.cancel();
			}
		});
		confirm_sent_find_comment.setOnClickListener(new OnClickListener() {
			//发送评论网络请求
			@Override
			public void onClick(View arg0) {
				//先new一个评论详情类
				OtherPeopleComment oPc = new OtherPeopleComment();
				oPc.setUserId(user.getUserId());
				oPc.setCommentTime(new Date());
				oPc.setOPCommentId(-1);
				oPc.setUserState(user.getUser_state());
				oPc.setCommentDetailInfoId(commentId);
				oPc.setCommentComtent(edit_find_comment_content.getText().toString().trim());
				//开启一个网络线程来发送
				new SetFindCommentTask().execute(oPc);
				find_detail_comment_count_de.setText(Integer.parseInt(find_detail_comment_count_de.getText().toString())+1+"");
				dialog.cancel();
			}
		});
	}
	//数据加载完初始化界面
	public void afterDo() {
		//各个listview的一波操作
		//显示多图的find_detail_pic_listView的处理
		if(CDInfoData.getImageUrl()[0] != "")//如果没拿到图片，就不显示
			find_detail_pic_listView.setAdapter(picAdapter = 
			new ImageListViewAdapter(FindDetailsActivity.this, CDInfoData.getImageUrl()));
		else{
			find_detail_pic_listView.setVisibility(View.GONE);
		}
		setListViewHeight(find_detail_pic_listView, picAdapter);
		
		//显示用户评论find_detail_comment_list_view的处理\
		find_detail_comment_list_view.setAdapter(otherPCadapter = new FindDetaiListViewAdapter(FindDetailsActivity.this, CDInfoData.getOtherPc()));
		//setListViewHeight(find_detail_comment_list_view, otherPCadapter);
		//显示用户头像
		//Random 随机数
		int x=1+(int)(Math.random()*21);
		find_detail_user_define_pic.setImageResource(defaultPacage.headpic[x]);
		find_detail_user_name.setText(CDInfoData.getComment_from_user_name());
		String stateStr = null;
		if(CDInfoData.getUser_state() == User.state_运动达人)
			stateStr = "运动达人";
		if(CDInfoData.getUser_state() == User.state_运动挚友)
			stateStr = "运动挚友";
		if(CDInfoData.getUser_state() == User.state_运动狂)
			stateStr = "运动狂";
		if(CDInfoData.getUser_state() == User.state_冒泡)
			stateStr = "冒泡";
		if(CDInfoData.getUser_state() == User.state_宅客)
			stateStr = "宅客";
		if(CDInfoData.getUser_state() == User.state_活跃)
			stateStr = "活跃";
		if(stateStr == null)
			find_detail_user_state.setText("无状态");
		else
			find_detail_user_state.setText(stateStr);
		find_detail_content.setText(CDInfoData.getComment_content());
		find_detail_time.setText(formatTime(CDInfoData.getComment_from_time()));
		Log.v("dfsdfds",CDInfoData.getHow_many_people_comment()+"");
		find_detail_comment_count_de.setText(""+CDInfoData.getHow_many_people_comment());
		find_detail_parse_count.setText(""+CDInfoData.getHow_many_people_praise());
		//让Scrollview显示在头部
		scrollview.scrollTo(0, 0);
		
	}	
	private CharSequence formatTime(Date commentTime) {
		//格式化时间的处理
		
		long currentTime = System.currentTimeMillis();
		long commentTimeMil = commentTime.getTime();
		long timePassed = currentTime - commentTimeMil;
		long timeIntoFormat;
		String time = ""+-1;
		if (timePassed < ONE_MINUTE) {
			time = "刚刚";
		} else if (timePassed < ONE_HOUR) {
			timeIntoFormat = timePassed / ONE_MINUTE;
			time = timeIntoFormat + "分钟前";
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			time = timeIntoFormat + "小时前"; 
		} else if(timePassed > ONE_DAY){
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String formatTime = sf.format(commentTime);
			String ss = formatTime.replaceFirst("-","年");
			time = ss.replaceAll("-","月")+"日";
		}
		return time;
		
	}
	//动态设置ListView的高度，
	private void setListViewHeight(ListView listView, BaseAdapter adapter) {
		adapter = (BaseAdapter) listView.getAdapter();    
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
	class CDInfoDataTask extends AsyncTask<String, Integer, Integer>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(String... arg0) {
			
			CDInfoData = httpGetCommentJson.getCommentDetalInfoData(commentId);
			if(CDInfoData == null )
				return -1;
			else 
				return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1)
				afterDo();
			else{
				errorpage.setVisibility(View.VISIBLE);
				user_name_about.setVisibility(View.GONE);
				find_detail_pic_layout.setVisibility(View.GONE); 
				find_detail_parse_comment.setVisibility(View.GONE);
				commentabout.setVisibility(View.GONE);
			}
			super.onPostExecute(result);
		}	
	}
	class SentParseAddOneTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... arg0) {
			httpGetCommentJson.sentParseAddOne(commentId);
			return null;
		}	
	}
	class SetFindCommentTask extends AsyncTask<OtherPeopleComment, Integer, Integer>{

		@Override
		protected Integer doInBackground(OtherPeopleComment... arg0) {
			httpGetCommentJson.sentOPCToInternet(arg0[0]);
			return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			Toast.makeText(FindDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
	}
	
	//屏蔽返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent1 = new Intent(FindDetailsActivity.this, MainActivity.class);
			Bundle bundle_comeback = new Bundle();
			bundle_comeback.putInt("CurrentItem", 2);
			intent1.putExtras(bundle_comeback);
			FindDetailsActivity.this.startActivity(intent1);
			FindDetailsActivity.this.finish();
			overridePendingTransition(R.drawable.interface_jump_in,
					R.drawable.interface_jump_out);
		}
		return super.onKeyDown(keyCode, event);
	}

}

