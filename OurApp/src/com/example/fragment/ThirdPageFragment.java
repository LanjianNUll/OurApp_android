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
	
	//����һ���ص��ṩδ����Ϣ���µĻص�
	public interface OnUnReadMessageUpdateListener
	{
		void unReadMessageUpdate(int count);
	}
	//����δ������Ϣ��
	private int unReadMsgCount;
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
	//���ظ���listview������
	private int addMoreCount = 10;
	private Handler handler;
	private int commentId;
	private ImageView refresh_pic;
	private TextView refreshTip;
	private RelativeLayout refreshloading;
	private ImageView refreshing_pic;
	//������ť�Ƿ���ʾ
	private boolean isdisplayFloat = false;
	//������ť
	private  LinearLayout floatRefreshLin, floatSignalLin, FloatWordLin;
	//�����Ķ���
	private ObjectAnimator floatRefreshLinanim;
	private ObjectAnimator floatSignalLinanim;
	private ObjectAnimator FloatWordLinanim;
	private ObjectAnimator refresh_picanim;
	private ObjectAnimator floatRefreshLinanimalpha;
	private ObjectAnimator floatSignalLinanimalpha;
	private ObjectAnimator FloatWordLinalpha;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		//ģ�������ʱ
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
		//�����쳣
		errorpage = (RelativeLayout) view.findViewById(R.id.errorpage);
		loading.setVisibility(View.VISIBLE);
		//find_sort_listview
		find_sort_listView = (ListView) view.findViewById(R.id.find_sort_listViewT);
		refreshTip = (TextView) view.findViewById(R.id.refreshTip);
		//����
		floatRefreshLin = (LinearLayout) view.findViewById(R.id.floatRefreshLin);
		floatSignalLin = (LinearLayout) view.findViewById(R.id.floatSignalLin);
		FloatWordLin = (LinearLayout) view.findViewById(R.id.FloatWordLin);
		
		find_sort_listView.setVisibility(View.GONE);
		handler = new Handler(){
			int i = 0 ;
			public void handleMessage(Message msg) {
				//������Ϣʱ����Ҫ֪�������ǳɹ�����Ϣ������ʧ�ܵ���Ϣ
				switch (msg.what) {
				case 1:
					loading.setVisibility(View.GONE);
					find_sort_listView.setVisibility(View.VISIBLE);
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
		
		//��ʱ�߳�
				new Timer().schedule(new TimerTask() {            
					public void run() {  
						Message msg = new Message();
						msg.what = 5;           	
						handler.sendMessage(msg);
					}  
				}, 0,100); //����������һ������ʱ��ò�ִ�У��ڶ����Ǹ����ִ��һ��  
				
		notifyUnReadedMsg();
		initview();
		//������ʧ
		FloatAnimation();
		return view;
	}

	//�ص�δ����Ϣ
	private void notifyUnReadedMsg(){
		if (getActivity() instanceof OnUnReadMessageUpdateListener){
			OnUnReadMessageUpdateListener listener = (OnUnReadMessageUpdateListener) getActivity();
			listener.unReadMessageUpdate(unReadMsgCount);
		}
	}


	private void initview() {
		//ˢ�°�ť
		refresh_pic = (ImageView) view.findViewById(R.id.refresh_pic);
		//����ˢ�°�ť
		//refresh_pic.setVisibility(View.GONE);
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
				
				SharedPreferences sharedpreferences = getActivity().getSharedPreferences("userDetailFile",Context.MODE_PRIVATE);
				String userJson=sharedpreferences.getString("userDetail", null);
				Gson gson = new Gson();
				UserDetailInfo user = gson.fromJson(userJson, UserDetailInfo.class);
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
		
		//�����ظ���Ĳ��ּ��ؽ���
		find_sort_listView.addFooterView(footView);
		//��ʼ�����µı���
		find_sort_newest.setBackgroundColor(Color.BLACK);
		find_sort_newest_text.setTextColor(Color.WHITE);
		//����
		//find_sort_listView.setAdapter(adapter = new FindSortListViewAdapter(getActivity(), Data));
		//����listView�������¼�
		find_sort_listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView v, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//����������������ʾ��ʾrefresh��ť
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
				//������ת�Ķ���
				getActivity().overridePendingTransition(R.drawable.interface_jump_in,
						R.drawable.interface_jump_out);
				//������һ����
				new seeCommentDetilAddOneTask();
			}
		});
		refresh_pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FloatAnimation();
			}
		});
		//�������ְ�ť
		FloatWordLin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Toast.makeText(getActivity(), "FloatWordLin", 1000).show();
				//������ʧ
				FloatAnimation();
				SharedPreferences sharedpreferences = getActivity().getSharedPreferences("userDetailFile",Context.MODE_PRIVATE);
				String userJson=sharedpreferences.getString("userDetail", null);
				Gson gson = new Gson();
				UserDetailInfo user = gson.fromJson(userJson, UserDetailInfo.class);
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
		//ǩ����ť
		floatSignalLin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Toast.makeText(getActivity(), "floatSignalLin", 1000).show();
				//������ʧ
				FloatAnimation();
				//��ȡ�û���Ϣ  userId
				SharedPreferences shp = getActivity()
						.getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
				String userDetailJson = shp.getString("userDetail", null);
				Gson gson = new Gson();
				UserDetailInfo user = gson.fromJson(userDetailJson, UserDetailInfo.class); 
				if(user == null)
					Toast.makeText(getActivity(), "ǩ��ʧ��", 1000).show();
				if(user.getUserId() == -1){//��ʾ�û�δ��¼
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
				}
				//��ȡ�û��ϴ�ǩ����ʱ��
				SharedPreferences sf = getActivity()
						.getSharedPreferences("userDetailFile", Context.MODE_PRIVATE);
				long time = sf.getLong("userLastTimeSiginTime", 0);
				if(time == 0 || System.currentTimeMillis() - time > 24*60*60){
						new UserSignTask().execute(user.getUserId());
						//��¼�û�ǩ��ʱ��
						Editor e = sf.edit();
						e.putLong("userLastTimeSiginTime", System.currentTimeMillis());
						e.commit();
				}else
					Toast.makeText(getActivity(), "������Ѿ�ǩ��������������Ŷ~",
								Toast.LENGTH_LONG).show();
			}
		});
		//ˢ�°�ť
		floatRefreshLin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//������ʧ
				FloatAnimation();
				//ˢ�¶���
				//Startrefreshanim();
				//302 Ϊ���µĵ���//301 Ϊ���ȵĵ���
				addMoreCount = 10;
				if(onHotPage){
					new RefreshNewTask().execute();//ˢ�����µ��첽
				}else{
					new RefreshHotTask().execute();//ˢ�����ȵ��첽
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
			
		//���Ŷ���
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
	//����ˢ�µ��첽����
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
				Toast.makeText(getActivity(), "ˢ��ʧ��", 1000).show();
			}
			refreshloading.setVisibility(View.GONE);
			//ˢ����ص���һ��  ����ˢ��ʧ�ܻ��ǳɹ�
			find_sort_listView.setSelection(0);
			//Stoptrefreshanim();
			super.onPostExecute(result);
		}
	}
	//����ˢ�µ��첽����
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
				Toast.makeText(getActivity(), "ˢ��ʧ��", 1000).show();
			}
			refreshloading.setVisibility(View.GONE);
			//ˢ����ص���һ��  ����ˢ��ʧ�ܻ��ǳɹ�
			find_sort_listView.setSelection(0);
			super.onPostExecute(result);
		}
	}
    //��ȡ���ȵ��첽����
	class GetHotCommentTask extends AsyncTask<String , Integer, Integer>{

		@Override
		protected void onPreExecute() {
			//����ʱ�Ĺ��ɶ���
			loading.setVisibility(View.VISIBLE);
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
	class UserSignTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... arg0) {
			HttpDoUserMsg httpDoUserMsg = new HttpDoUserMsg();
			return httpDoUserMsg.userSign(arg0[0]);
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1){
				
				Toast.makeText(getActivity(), "ǩ���ɹ�", 1000).show();
			}else{
				Toast.makeText(getActivity(), "ǩ��ʧ��", 1000).show();
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
