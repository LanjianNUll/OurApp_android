package com.example.fragment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.example.Refresh.RefreshableView;
import com.example.Refresh.RefreshableView.PullToRefreshListener;
import com.example.activity.FindDetailsActivity;
import com.example.activity.UserLoginActivity;
import com.example.activity.WriteCommentActivity;
import com.example.adapter.FindSortListViewAdapter;
import com.example.bean.Comment;
import com.example.bean.User;
import com.example.httpunit.HttpGetCommentJson;
import com.example.httpunit.HttpGetSportPlaceJson;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;
import com.google.gson.Gson;

import android.R.layout;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
	private View footView;
	//find sort  ���º����ȵķ���
	private LinearLayout find_sort_newest,find_sort_hotest;
	private TextView find_sort_newest_text,find_sort_hotest_text;
	//��¼���Ǹ�ҳ��
	private boolean onHotPage = true;
	//find_sort_list_view
	private ListView find_sort_listView;
	//����
	private FindSortListViewAdapter adapter;
	/**
	 * �����������ʾ�ڽ���
	 * 
	 * */
	private  ArrayList<Comment> find_list;
	//�����е�����
	private TextView jiazai_text;
	private ImageView jiazai_pic;
	private RelativeLayout loading, errorpage;
	//httpgetcommentjson
	private HttpGetCommentJson httpgetcommentjson = new HttpGetCommentJson();
	//����һ������
	private ArrayList<Comment> Data = new ArrayList<Comment>();
	//�����imagaview
	private ImageView write_comment;
	//����ˢ�µ�View
	private RefreshableView refreshView;
	//���ظ���listview������
	private int addMoreCount = 10;
	private Handler handler;
	private int commentId;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.app_thirdpage, container, false);

		jiazai_pic = (ImageView) view.findViewById(R.id.jiazai_pic);
		jiazai_text = (TextView) view.findViewById(R.id.jiazai_text);
		loading = (RelativeLayout) view.findViewById(R.id.loading);
		//�����쳣
		errorpage = (RelativeLayout) view.findViewById(R.id.errorpage);
		//������View
		refreshView = (RefreshableView) view.findViewById(R.id.refreshView);
		refreshView.setVisibility(View.GONE);
		handler = new Handler(){
			int i = 0 ;
			public void handleMessage(Message msg) {
				//������Ϣʱ����Ҫ֪�������ǳɹ�����Ϣ������ʧ�ܵ���Ϣ
				switch (msg.what) {
				case 1:
					loading.setVisibility(View.GONE);
					refreshView.setVisibility(View.VISIBLE);
					find_sort_listView.setAdapter(adapter = new FindSortListViewAdapter(getActivity(), Data));
					break;
				case 2 :
					jiazai_pic.setImageResource(R.drawable.jiazai00+i%6);
					if(i%2==0)
						jiazai_text.setText("������...");
					else if(i%3==0)
						jiazai_text.setText("������..");
					else
						jiazai_text.setText("������.");
					i++;
					if(i==100)
						i=0;
					break;
				case 3 :
					adapter.notifyDataSetChanged();
					find_sort_listView.setSelection(addMoreCount-10);
					if(Data.size()<addMoreCount){
						Toast.makeText(getActivity(), "�ף��Ѿ�������~", 1000).show();
						footView.setVisibility(View.GONE);
					}
					addMoreCount = addMoreCount + 10;	
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

					//�����ʱ�������json��json����
					//ģ���ʱ
					sleep(3000);
					if(!onHotPage){
						//�����������ݵ���
						//302 Ϊ���µĵ���//301 Ϊ���ȵĵ���
						Data = httpgetcommentjson.getHotData(301);
						addMoreCount=addMoreCount+10;
					}else{
						Data = httpgetcommentjson.getNewData(302);
						addMoreCount=addMoreCount+10;
					}
						
					//�жϴ������ȡ��Data�Ƿ�Ϊ�գ�
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
		//��ʱ�߳�
		new Timer().schedule(new TimerTask() {            
			public void run() {  
				Message msg = new Message();
				msg.what = 2;            	
				handler.sendMessage(msg);
			}  
		}, 0,100); //����������һ������ʱ��ò�ִ�У��ڶ����Ǹ����ִ��һ��  
		initview();
		return view;
	}

	private void initview() {
		
		//��ListViewĩβ���һ�����ظ���Ĳ���
		footView = LayoutInflater.from(getActivity()).inflate(R.layout.add_more_layout, null, false); 
		TextView addMoreView = (TextView) footView.findViewById(R.id.addMoreView);
		addMoreView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!onHotPage){//���ظ�����������ݵĵ���
					//301 Ϊ���ȵĵ���
					addMoreData(301, addMoreCount);
					//Toast.makeText(getActivity(), addMoreCount+"", 1000).show();
				}
				else{
					//302 Ϊ���µĵ���
					addMoreData(302, addMoreCount);
					//Toast.makeText(getActivity(), addMoreCount+"", 1000).show();
				}
			}
		});
		//find sort  ���º����ȵķ���
		find_sort_hotest = (LinearLayout) view.findViewById(R.id.find_sort_hotest);
		find_sort_newest = (LinearLayout) view.findViewById(R.id.find_sort_newest);
		find_sort_hotest_text = (TextView) view.findViewById(R.id.find_sort_hotest_text);
		find_sort_newest_text = (TextView) view.findViewById(R.id.find_sort_newest_text);
		//write_comment
		write_comment = (ImageView) view.findViewById(R.id.write_comment);
		//write_comment�ļ���
		write_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				SharedPreferences sharedpreferences = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
				String userJson=sharedpreferences.getString("userJson", null);
				Gson gson = new Gson();
				User user = gson.fromJson(userJson, User.class);
				if(user.getUserId()==-1){//�û�û��¼��
					AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
					ab.setTitle("");
					ab.setMessage("�ף��㻹û��¼~");
					ab.setPositiveButton("ȥ��¼", new DialogInterface.OnClickListener() {						
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(getActivity(), UserLoginActivity.class);
							startActivity(intent);
							getActivity().finish();
							//������ת�Ķ���
							getActivity().overridePendingTransition(R.drawable.interface_jump_in,
									R.drawable.interface_jump_out);
						}
					});
					ab.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
					//�û���¼�˲�����
					Intent intent = new Intent(getActivity(), WriteCommentActivity.class);
					startActivity(intent);
					getActivity().finish();
					//������ת�Ķ���
					getActivity().overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
			}
		});						
		find_sort_hotest.setOnClickListener(new FindSortListener());
		find_sort_newest.setOnClickListener(new FindSortListener());
		//find_sort_listview
		find_sort_listView = (ListView) view.findViewById(R.id.find_sort_listView);
		//�����ظ���Ĳ��ּ��ؽ���
		find_sort_listView.addFooterView(footView);
		//��ʼ�����µı���
		find_sort_newest.setBackgroundColor(Color.BLACK);
		find_sort_newest_text.setTextColor(Color.WHITE);
		//����
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
				//������ת�Ķ���
				getActivity().overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
				//������һ����
				new seeCommentDetilAddOneTask();
			}
		});
		//����ˢ�µļ���
		refreshView.setOnRefreshListener(new PullToRefreshListener() {
			
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(3000);
					//ˢ��������ʾ����
					addMoreCount = 10;
					if(onHotPage){
						//302 Ϊ���µĵ���//301 Ϊ���ȵĵ���
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
			//��ʾ���ظ���İ�ť
			footView.setVisibility(View.VISIBLE);
			switch (v.getId()) {
			case R.id.find_sort_hotest:
				onHotPage = false;
				find_sort_newest.setBackgroundColor(Color.WHITE);
				find_sort_newest_text.setTextColor(Color.BLACK);
				find_sort_hotest.setBackgroundColor(Color.BLACK);
				find_sort_hotest_text.setTextColor(Color.WHITE);
				//����д���ȵ����ݵ���
				//����һ���첽 ����������������
				new GetHotCommentTask().execute();
				break;
			case R.id.find_sort_newest:
				onHotPage = true;
				find_sort_newest.setBackgroundColor(Color.BLACK);
				find_sort_newest_text.setTextColor(Color.WHITE);
				find_sort_hotest.setBackgroundColor(Color.WHITE);
				find_sort_hotest_text.setTextColor(Color.BLACK);
				//����д���µ����ݵ���
				//����һ���첽 ����������������
				new GetNewCommentTask().execute();
				break;
			}
		}
	}

	class GetHotCommentTask extends AsyncTask<String , Integer, Integer>{

		@Override
		protected void onPreExecute() {
			//����ʱ�Ĺ��ɶ���
			loading.setVisibility(View.VISIBLE);
			refreshView.setVisibility(View.GONE);
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			//302 Ϊ���µĵ���//301 Ϊ���ȵĵ���
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
	//���µ��첽
	class GetNewCommentTask extends AsyncTask<String , Integer, Integer>{


		@Override
		protected void onPreExecute() {
			//����ʱ�Ĺ��ɶ���
			loading.setVisibility(View.VISIBLE);
			refreshView.setVisibility(View.GONE);
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			//302 Ϊ���µĵ���//301 Ϊ���ȵĵ���
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
			//������һ
			httpgetcommentjson.seeCommentCountAddOne(commentId);
			return null;
		}
	}
}
