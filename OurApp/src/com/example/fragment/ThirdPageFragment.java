package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.example.Refresh.RefreshableView;
import com.example.Refresh.RefreshableView.PullToRefreshListener;
import com.example.activity.FindDetailsActivity;
import com.example.activity.WriteCommentActivity;
import com.example.adapter.FindSortListViewAdapter;
import com.example.bean.Comment;
import com.example.httpunit.HttpGetCommentJson;
import com.example.httpunit.HttpGetSportPlaceJson;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;

import android.R.layout;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ThirdPageFragment extends Fragment {
	private View view;
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
	//下拉刷新的View
	private RefreshableView refreshView;
	//加载更多listview的条数
	private int addMoreCount = 10 ;
	private Handler handler;
	private int commentId;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.app_thirdpage, container, false);

		jiazai_pic = (ImageView) view.findViewById(R.id.jiazai_pic);
		jiazai_text = (TextView) view.findViewById(R.id.jiazai_text);
		loading = (RelativeLayout) view.findViewById(R.id.loading);
		//网络异常
		errorpage = (RelativeLayout) view.findViewById(R.id.errorpage);
		//下拉的View
		refreshView = (RefreshableView) view.findViewById(R.id.refreshView);
		refreshView.setVisibility(View.GONE);
		handler = new Handler(){
			int i = 0 ;
			public void handleMessage(Message msg) {
				//处理消息时，需要知道到底是成功的消息，还是失败的消息
				switch (msg.what) {
				case 1:
					loading.setVisibility(View.GONE);
					refreshView.setVisibility(View.VISIBLE);
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
					find_sort_listView.setSelection(addMoreCount+1);
					addMoreCount = addMoreCount +10;
					break;
				case 4 :
					loading.setVisibility(View.GONE);
					errorpage.setVisibility(View.VISIBLE);
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
					}else
						//最新言论数据调用
						Data = httpgetcommentjson.getNewData(302);
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
		initview();
		return view;
	}

	private void initview() {
		
		//给ListView末尾添加一个记载更多的布局
		View footView = LayoutInflater.from(getActivity()).inflate(R.layout.add_more_layout, null, false); 
		TextView addMoreView = (TextView) footView.findViewById(R.id.addMoreView);
		addMoreView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(onHotPage){//加载更多的最热数据的调用
					//301 为最热的调用
					addMoreData(301, addMoreCount);
				}
				else{
					//302 为最新的调用
					addMoreData(301, addMoreCount);
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
				Intent intent = new Intent(getActivity(), WriteCommentActivity.class);
				getActivity().startActivity(intent);
				getActivity().finish();
			}
		});						
		find_sort_hotest.setOnClickListener(new FindSortListener());
		find_sort_newest.setOnClickListener(new FindSortListener());
		//find_sort_listview
		find_sort_listView = (ListView) view.findViewById(R.id.find_sort_listView);
		//将加载更多的布局加载进来
		find_sort_listView.addFooterView(footView);
		//初始化最新的背景
		find_sort_newest.setBackgroundColor(Color.BLACK);
		find_sort_newest_text.setTextColor(Color.WHITE);
		//适配
		//find_sort_listView.setAdapter(adapter = new FindSortListViewAdapter(getActivity(), Data));
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
				//看过加一操作
				new seeCommentDetilAddOneTask();
			}
		});
		//下拉刷新的监听
		refreshView.setOnRefreshListener(new PullToRefreshListener() {
			
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(3000);
					//刷新重置显示条数
					addMoreCount = 10;
					if(onHotPage){
						//302 为最新的调用//301 为最热的调用
						ArrayList<Comment> NewData = new ArrayList<Comment>();
						NewData = httpgetcommentjson.getHotData(301);
						if(NewData != null){
							Data = NewData;
							adapter.notifyDataSetChanged();
						}
					}else{
						ArrayList<Comment> HotData = new ArrayList<Comment>();
						HotData = httpgetcommentjson.getHotData(301);
						if(HotData != null){
							Data = HotData;
							adapter.notifyDataSetChanged();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				refreshView.finishRefreshing();
				
			}
			
		},0);
	}

	protected void addMoreData(final int onwhichPage, final int addMoreCount2) {
		new Thread(){
			public void run(){
				ArrayList<Comment> moreData = new ArrayList<Comment>();
				moreData = httpgetcommentjson.getaddMoreData(onwhichPage , addMoreCount2);
				if(moreData != null)
					for(int i = 0; i<moreData.size(); i++)
						Data.add(moreData.get(i));
				handler.sendEmptyMessage(3);
			};
		}.start();
	}

	class FindSortListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			addMoreCount = 10;
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

	class GetHotCommentTask extends AsyncTask<String , Integer, Integer>{

		@Override
		protected void onPreExecute() {
			//启动时的过渡动画
			loading.setVisibility(View.VISIBLE);
			refreshView.setVisibility(View.GONE);
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
				refreshView.setVisibility(View.VISIBLE);
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
			refreshView.setVisibility(View.GONE);
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
				refreshView.setVisibility(View.VISIBLE);
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
}
