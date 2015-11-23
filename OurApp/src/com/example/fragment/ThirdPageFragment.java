package com.example.fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.CircleImageView.CircleImageView;
import com.example.activity.FindDetailsActivity;
import com.example.activity.UserLoginActivity;
import com.example.activity.WriteCommentActivity;
import com.example.adapter.FindSortListViewAdapter;
import com.example.bean.Comment;
import com.example.bean.User;
import com.example.bean.UserDetailInfo;
import com.example.httpunit.HttpDoUserMsg;
import com.example.httpunit.HttpGetCommentJson;
import com.example.ourapp.R;
import com.google.gson.Gson;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ThirdPageFragment extends Fragment {
	
	//设置一个回调提供未读消息更新的回调
	public interface OnUnReadMessageUpdateListener
	{
		void unReadMessageUpdate(int count);
	}
	//设置未读的消息数
	private int unReadMsgCount;
	private View view;
	private View footView;
	//find sort  最新和最热的分类
	private LinearLayout find_sort_newest,find_sort_hotest;
	private TextView find_sort_newest_text,find_sort_hotest_text;
	//记录在那个页面
	private boolean onHotPage = true;
	//find_sort_list_view
	private ListView find_sort_listView;
	//适配
	private FindSortListViewAdapter adapter;
	/**
	 * 添加数据项显示在界面
	 * 
	 * */
	private  ArrayList<Comment> find_list;
	//加载中的文字
	private TextView jiazai_text;
	private ImageView jiazai_pic;
	private RelativeLayout loading, errorpage;
	//httpgetcommentjson
	private HttpGetCommentJson httpgetcommentjson = new HttpGetCommentJson();
	//定义一个容器
	private ArrayList<Comment> Data = new ArrayList<Comment>();
	//发表的imagaview
	private ImageView write_comment;
	//加载更多listview的条数
	private int addMoreCount = 10;
	private Handler handler;
	private int commentId;
	private ImageView refresh_pic;
	private TextView refreshTip;
	private RelativeLayout refreshloading;
	private ImageView refreshing_pic;
	//浮动按钮是否显示
	private boolean isdisplayFloat = false;
	//浮动按钮
	private  LinearLayout floatRefreshLin, floatSignalLin, FloatWordLin;
	//浮动的动画
	private ObjectAnimator floatRefreshLinanim;
	private ObjectAnimator floatSignalLinanim;
	private ObjectAnimator FloatWordLinanim;
	private ObjectAnimator refresh_picanim;
	private ObjectAnimator floatRefreshLinanimalpha;
	private ObjectAnimator floatSignalLinanimalpha;
	private ObjectAnimator FloatWordLinalpha;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		//模拟网络耗时
//		try {
//			Thread.sleep(3000);
//			unReadMsgCount = 4;
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		

		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.app_thirdpage, container, false);

		jiazai_pic = (ImageView) view.findViewById(R.id.jiazai_pic);
		jiazai_text = (TextView) view.findViewById(R.id.jiazai_text);
		loading = (RelativeLayout) view.findViewById(R.id.loading);
		//refresh anim
		refreshloading = (RelativeLayout) view.findViewById(R.id.refreshloading);
		refreshing_pic = (ImageView) view.findViewById(R.id.refreshing_pic);
		refreshloading.setVisibility(View.GONE);
		//网络异常
		errorpage = (RelativeLayout) view.findViewById(R.id.errorpage);
		loading.setVisibility(View.VISIBLE);
		//find_sort_listview
		find_sort_listView = (ListView) view.findViewById(R.id.find_sort_listViewT);
		refreshTip = (TextView) view.findViewById(R.id.refreshTip);
		//浮动
		floatRefreshLin = (LinearLayout) view.findViewById(R.id.floatRefreshLin);
		floatSignalLin = (LinearLayout) view.findViewById(R.id.floatSignalLin);
		FloatWordLin = (LinearLayout) view.findViewById(R.id.FloatWordLin);
		
		find_sort_listView.setVisibility(View.GONE);
		handler = new Handler(){
			int i = 0 ;
			public void handleMessage(Message msg) {
				//处理消息时，需要知道到底是成功的消息，还是失败的消息
				switch (msg.what) {
				case 1:
					loading.setVisibility(View.GONE);
					find_sort_listView.setVisibility(View.VISIBLE);
					find_sort_listView.setAdapter(adapter = new FindSortListViewAdapter(getActivity(), Data));
					break;
				case 2 :
					jiazai_pic.setImageResource(R.drawable.jiazai00+i%6);
					if(i%2==0)
						jiazai_text.setText("加载中...");
					else if(i%3==0)
						jiazai_text.setText("加载中..");
					else
						jiazai_text.setText("加载中.");
					i++;
					if(i==100)
						i=0;
					break;
				case 3 :
					adapter.notifyDataSetChanged();
					find_sort_listView.setSelection(addMoreCount-10);
					if(Data.size()<addMoreCount){
						Toast.makeText(getActivity(), "亲，已经到底了~", 1000).show();
						footView.setVisibility(View.GONE);
					}
					addMoreCount = addMoreCount + 10;	
					break;
				case 4 :
					loading.setVisibility(View.GONE);
					find_sort_listView.setVisibility(View.GONE);
					errorpage.setVisibility(View.VISIBLE);
					break;
				case 5 :
					refreshing_pic.setImageResource(R.drawable.jiazai00+i%6);
					i++;
					if(i==100)
						i=0;
					break;
				}
			}       	
		};	 
		new Thread(){
			public void run(){
				try {

					//处理耗时网络加载json和json解析
					//模拟耗时
					sleep(3000);
					if(!onHotPage){
						//最热言论数据调用
						//302 为最新的调用//301 为最热的调用
						Data = httpgetcommentjson.getHotData(301);
						addMoreCount=addMoreCount+10;
					}else{
						Data = httpgetcommentjson.getNewData(302);
						addMoreCount=addMoreCount+10;
					}
					//判断从网络获取的Data是否为空，
					if(Data == null)
						handler.sendEmptyMessage(4);
					else
						handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
			}	
		}.start();
		//定时线程
		new Timer().schedule(new TimerTask() {            
			public void run() {  
				Message msg = new Message();
				msg.what = 2;           	
				handler.sendMessage(msg);
			}  
		}, 0,100); //两个参数第一个是延时多久才执行，第二个是隔多久执行一次  
		
		//定时线程
				new Timer().schedule(new TimerTask() {            
					public void run() {  
						Message msg = new Message();
						msg.what = 5;           	
						handler.sendMessage(msg);
					}  
				}, 0,100); //两个参数第一个是延时多久才执行，第二个是隔多久执行一次  
				
		notifyUnReadedMsg();
		initview();
		//浮动消失
		FloatAnimation();
		return view;
	}

	//回调未读消息
	private void notifyUnReadedMsg(){
		if (getActivity() instanceof OnUnReadMessageUpdateListener){
			OnUnReadMessageUpdateListener listener = (OnUnReadMessageUpdateListener) getActivity();
			listener.unReadMessageUpdate(unReadMsgCount);
		}
	}


	private void initview() {
		//刷新按钮
		refresh_pic = (ImageView) view.findViewById(R.id.refresh_pic);
		//隐藏刷新按钮
		//refresh_pic.setVisibility(View.GONE);
		//给ListView末尾添加一个记载更多的布局
		footView = LayoutInflater.from(getActivity()).inflate(R.layout.add_more_layout, null, false); 
		TextView addMoreView = (TextView) footView.findViewById(R.id.addMoreView);
		addMoreView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if(!onHotPage){//加载更多的最热数据的调用
					//301 为最热的调用
					addMoreData(301, addMoreCount);
					//Toast.makeText(getActivity(), addMoreCount+"", 1000).show();
				}
				else{
					//302 为最新的调用
					addMoreData(302, addMoreCount);
					//Toast.makeText(getActivity(), addMoreCount+"", 1000).show();
				}
			}
		});
		//find sort  最新和最热的分类
		find_sort_hotest = (LinearLayout) view.findViewById(R.id.find_sort_hotest);
		find_sort_newest = (LinearLayout) view.findViewById(R.id.find_sort_newest);
		find_sort_hotest_text = (TextView) view.findViewById(R.id.find_sort_hotest_text);
		find_sort_newest_text = (TextView) view.findViewById(R.id.find_sort_newest_text);
		//write_comment
		write_comment = (ImageView) view.findViewById(R.id.write_comment);
		//write_comment的监听
		write_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				SharedPreferences sharedpreferences = getActivity().getSharedPreferences("userDetailFile",Context.MODE_PRIVATE);
				String userJson=sharedpreferences.getString("userDetail", null);
				Gson gson = new Gson();
				UserDetailInfo user = gson.fromJson(userJson, UserDetailInfo.class);
				if(user.getUserId()==-1){//用户没登录，
					AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
					ab.setTitle("");
					ab.setMessage("亲，你还没登录~");
					ab.setPositiveButton("去登录", new DialogInterface.OnClickListener() {						
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(getActivity(), UserLoginActivity.class);
							startActivity(intent);
							getActivity().finish();
							//界面跳转的动画
							getActivity().overridePendingTransition(R.drawable.interface_jump_in,
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
					Intent intent = new Intent(getActivity(), WriteCommentActivity.class);
					startActivity(intent);
					getActivity().finish();
					//界面跳转的动画
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
			}
		});						
		find_sort_hotest.setOnClickListener(new FindSortListener());
		find_sort_newest.setOnClickListener(new FindSortListener());
		
		//将加载更多的布局加载进来
		find_sort_listView.addFooterView(footView);
		//初始化最新的背景
		find_sort_newest.setBackgroundColor(Color.BLACK);
		find_sort_newest_text.setTextColor(Color.WHITE);
		//适配
		//find_sort_listView.setAdapter(adapter = new FindSortListViewAdapter(getActivity(), Data));
		//监听listView滑动的事件
		find_sort_listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView v, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//当滑动到顶部是显示显示refresh按钮
//				if(firstVisibleItem == 0 ){
//					refresh_pic.setVisibility(View.VISIBLE);
//				}else
//					refresh_pic.setVisibility(View.GONE);
//				
			}
		});

		find_sort_listView.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> arg0, View v,
					int position, long arg3) {
								
				commentId = Data.get(position).getComment_id();
				Intent intent = new Intent(getActivity(), FindDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("commentId", commentId);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
				//界面跳转的动画
				getActivity().overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
				//看过加一操作
				new seeCommentDetilAddOneTask();
			}
		});
		refresh_pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FloatAnimation();
			}
		});
		//发表文字按钮
		FloatWordLin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Toast.makeText(getActivity(), "FloatWordLin", 1000).show();
				//浮动消失
				FloatAnimation();
				SharedPreferences sharedpreferences = getActivity().getSharedPreferences("userDetailFile",Context.MODE_PRIVATE);
				String userJson=sharedpreferences.getString("userDetail", null);
				Gson gson = new Gson();
				UserDetailInfo user = gson.fromJson(userJson, UserDetailInfo.class);
				if(user.getUserId()==-1){//用户没登录，
					AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
					ab.setTitle("");
					ab.setMessage("亲，你还没登录~");
					ab.setPositiveButton("去登录", new DialogInterface.OnClickListener() {						
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(getActivity(), UserLoginActivity.class);
							startActivity(intent);
							getActivity().finish();
							//界面跳转的动画
							getActivity().overridePendingTransition(R.drawable.interface_jump_in,
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
					Intent intent = new Intent(getActivity(), WriteCommentActivity.class);
					startActivity(intent);
					getActivity().finish();
					//界面跳转的动画
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
				
				
			}
		});
		//签到按钮
		floatSignalLin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Toast.makeText(getActivity(), "floatSignalLin", 1000).show();
				//浮动消失
				FloatAnimation();
				//获取用户信息  userId
				SharedPreferences shp = getActivity()
						.getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
				String userDetailJson = shp.getString("userDetail", null);
				Gson gson = new Gson();
				UserDetailInfo user = gson.fromJson(userDetailJson, UserDetailInfo.class); 
				if(user == null)
					Toast.makeText(getActivity(), "签到失败", 1000).show();
				if(user.getUserId() == -1){//显示用户未登录
					AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
					ab.setTitle("");
					ab.setMessage("亲，你还没登录~");
					ab.setPositiveButton("去登录", new DialogInterface.OnClickListener() {						
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(getActivity(), UserLoginActivity.class);
							startActivity(intent);
							getActivity().finish();
							//界面跳转的动画
							getActivity().overridePendingTransition(R.drawable.interface_jump_in,
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
				}
				//获取用户上传签到的时间
				SharedPreferences sf = getActivity()
						.getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
				long time = sf.getLong("userLastTimeSiginTime", 0);
				if(time == 0 || System.currentTimeMillis() - time > 24*60*60){
						new UserSignTask().execute(user.getUserId());
						//记录用户签到时间
						Editor e = sf.edit();
						e.putLong("userLastTimeSiginTime", System.currentTimeMillis());
						e.commit();
				}else
					Toast.makeText(getActivity(), "你今天已经签过到，明天再来哦~",
								Toast.LENGTH_LONG).show();
			}
		});
		//刷新按钮
		floatRefreshLin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//浮动消失
				FloatAnimation();
				//刷新动画
				//Startrefreshanim();
				//302 为最新的调用//301 为最热的调用
				addMoreCount = 10;
				if(onHotPage){
					new RefreshNewTask().execute();//刷新最新的异步
				}else{
					new RefreshHotTask().execute();//刷新最热的异步
				}
			}
		});	
	}
	protected void FloatAnimation() {
		if(isdisplayFloat){
			disPlayFLoatAnim();
			isdisplayFloat = false;
		}
		else{
			hideFloatAnim();
			isdisplayFloat = true;
		}
			
		//播放动画
		AnimatorSet animSet = new AnimatorSet();
		animSet.play(refresh_picanim);
		animSet.play(floatRefreshLinanim).with(floatRefreshLinanimalpha);
		animSet.play(floatSignalLinanim).with(floatSignalLinanimalpha);
		animSet.play(FloatWordLinanim).with(FloatWordLinalpha);
		animSet.setDuration(500);
		animSet.start();
		
	}

	protected void hideFloatAnim()  {
		refresh_picanim = ObjectAnimator.ofFloat(refresh_pic, 
				"rotation",  45f, 0);
		floatRefreshLinanim = ObjectAnimator.ofFloat(floatRefreshLin, 
				"translationX", 0.5f, 500f);
		floatRefreshLinanimalpha =ObjectAnimator.ofFloat(floatRefreshLin,
				"alpha", 1f, 0);
		floatSignalLinanim = ObjectAnimator.ofFloat(floatSignalLin, 
				"translationX", 0.5f, 500f);
		floatSignalLinanimalpha =ObjectAnimator.ofFloat(floatSignalLin,
				"alpha", 1f, 0);
		FloatWordLinanim = ObjectAnimator.ofFloat(FloatWordLin,
				"translationX", 0.5f, 500f);
		FloatWordLinalpha =ObjectAnimator.ofFloat(FloatWordLin,
				"alpha", 1f, 0);
	}

	protected void disPlayFLoatAnim() {
		refresh_picanim = ObjectAnimator.ofFloat(refresh_pic, 
				"rotation",  0, 45f);
		floatRefreshLinanim = ObjectAnimator.ofFloat(floatRefreshLin, 
				"translationX", 500f, 0.5f);
		floatRefreshLinanimalpha =ObjectAnimator.ofFloat(floatRefreshLin,
				"alpha", 0 ,1f);
		floatSignalLinanim = ObjectAnimator.ofFloat(floatSignalLin, 
				"translationX", 500f, 0.5f);
		floatSignalLinanimalpha =ObjectAnimator.ofFloat(floatSignalLin,
				"alpha", 0 ,1f);
		FloatWordLinanim = ObjectAnimator.ofFloat(FloatWordLin,
				"translationX", 500f, 0.5f);
		FloatWordLinalpha =ObjectAnimator.ofFloat(FloatWordLin,
				"alpha", 0 ,1f);
		
	}

	protected void addMoreData(final int onwhichPage, final int addMoreCount2) {
		new Thread(){
			public void run(){
				ArrayList<Comment> moreData = new ArrayList<Comment>();
				moreData = httpgetcommentjson.getaddMoreData(onwhichPage , addMoreCount2);
				if(moreData != null){
					for(int i = 0; i<moreData.size(); i++)
						Data.add(moreData.get(i));
				}
				handler.sendEmptyMessage(3);//+10
			};
		}.start();
	}

	class FindSortListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			addMoreCount = 20;
			//显示加载更多的按钮
			footView.setVisibility(View.VISIBLE);
			switch (v.getId()) {
			case R.id.find_sort_hotest:
				onHotPage = false;
				find_sort_newest.setBackgroundColor(Color.WHITE);
				find_sort_newest_text.setTextColor(Color.BLACK);
				find_sort_hotest.setBackgroundColor(Color.BLACK);
				find_sort_hotest_text.setTextColor(Color.WHITE);
				//这里写最热的数据调用
				//启动一个异步 处理网络请求数据
				new GetHotCommentTask().execute();
				break;
			case R.id.find_sort_newest:
				onHotPage = true;
				find_sort_newest.setBackgroundColor(Color.BLACK);
				find_sort_newest_text.setTextColor(Color.WHITE);
				find_sort_hotest.setBackgroundColor(Color.WHITE);
				find_sort_hotest_text.setTextColor(Color.BLACK);
				//这里写最新的数据调用
				//启动一个异步 处理网络请求数据
				new GetNewCommentTask().execute();
				break;
			}
		}
	}
	//最热刷新的异步加载
	class RefreshHotTask extends AsyncTask<Integer, Integer, Integer>{
		
		@Override
		protected void onPreExecute() {
			//display refreshloading Layout
			refreshloading.setVisibility(View.VISIBLE);
			errorpage.setVisibility(View.GONE);
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Integer... arg0) {
			Data = httpgetcommentjson.getHotData(302);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Data == null){
				return 0;
			}
			return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1 ){
				find_sort_listView.setAdapter(adapter = 
						new FindSortListViewAdapter(getActivity(), Data));
				errorpage.setVisibility(View.GONE);
				}
			else{
				Toast.makeText(getActivity(), "刷新失败", 1000).show();
			}
			refreshloading.setVisibility(View.GONE);
			//刷新完回到第一行  无论刷新失败还是成功
			find_sort_listView.setSelection(0);
			//Stoptrefreshanim();
			super.onPostExecute(result);
		}
	}
	//最新刷新的异步加载
	class RefreshNewTask extends AsyncTask<Integer, Integer, Integer>{
		@Override
		protected void onPreExecute() {
			//display refreshloading Layout
			refreshloading.setVisibility(View.VISIBLE);
			errorpage.setVisibility(View.GONE);
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Integer... arg0) {
			Data = httpgetcommentjson.getNewData(302);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Data == null){
				return 0;
			}
			return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1 ){
				find_sort_listView.setAdapter(adapter = 
						new FindSortListViewAdapter(getActivity(), Data));
			}
			else{
				Toast.makeText(getActivity(), "刷新失败", 1000).show();
			}
			refreshloading.setVisibility(View.GONE);
			//刷新完回到第一行  无论刷新失败还是成功
			find_sort_listView.setSelection(0);
			super.onPostExecute(result);
		}
	}
    //获取最热的异步加载
	class GetHotCommentTask extends AsyncTask<String , Integer, Integer>{

		@Override
		protected void onPreExecute() {
			//启动时的过渡动画
			loading.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			//302 为最新的调用//301 为最热的调用
			Data = httpgetcommentjson.getHotData(301);
			if(Data == null)
				return -1;
			else
				return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				loading.setVisibility(View.GONE);
				find_sort_listView.setAdapter(adapter = 
						new FindSortListViewAdapter(getActivity(), Data));
				//adapter.notifyDataSetChanged();
				
			}else{
				loading.setVisibility(View.GONE);
				errorpage.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}		
	}
	//最新的异步
	class GetNewCommentTask extends AsyncTask<String , Integer, Integer>{

		@Override
		protected void onPreExecute() {
			//启动时的过渡动画
			loading.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			//302 为最新的调用//301 为最热的调用
			Data = httpgetcommentjson.getNewData(302);
			if(Data == null)
				return -1;
			else
				return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				loading.setVisibility(View.GONE);
				//adapter.notifyDataSetChanged();
				find_sort_listView.setAdapter(adapter = 
						new FindSortListViewAdapter(getActivity(), Data));
			}else{
				loading.setVisibility(View.GONE);
				errorpage.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}		
	}
	class seeCommentDetilAddOneTask extends AsyncTask<Integer, Integer, String>{

		@Override
		protected String doInBackground(Integer... arg0) {
			//看过加一
			httpgetcommentjson.seeCommentCountAddOne(commentId);
			return null;
		}
	}
	class UserSignTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			HttpDoUserMsg httpDoUserMsg = new HttpDoUserMsg();
			return httpDoUserMsg.userSign(arg0[0]);
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				
				Toast.makeText(getActivity(), "签到成功", 1000).show();
			}else{
				Toast.makeText(getActivity(), "签到失败", 1000).show();
			}
			super.onPostExecute(result);
		}
		
	}
	private void Startrefreshanim(){
		Toast.makeText(getActivity(), "start", 1000).show();
		 ObjectAnimator animator = ObjectAnimator.ofFloat(refresh_pic,
		    		"rotation", 0.0F, 360.0F);  
		    animator.setDuration(1000);
		    animator.start();
		    animator.setRepeatCount(1000);
		    refreshTip.setVisibility(View.VISIBLE);
		    refreshTip.setTextColor(Color.parseColor("#000000"));
	}
	
	@SuppressLint("NewApi")
	private void Stoptrefreshanim(){
		Toast.makeText(getActivity(), "stop", 1000).show();
		 ObjectAnimator animator = ObjectAnimator.ofFloat(refresh_pic,
		    		"rotation",0.0F, 0.0F);  
		    animator.setDuration(100);
		    animator.setRepeatCount(1000);
		    animator.start();
		    refreshTip.setVisibility(View.GONE);
	}
}
