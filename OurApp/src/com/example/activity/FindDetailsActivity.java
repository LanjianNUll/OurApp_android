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
	//�û����
	private TextView find_detail_user_name, find_detail_user_state;
	private CircleImageView find_detail_user_define_pic;
	//��������
	private TextView find_detail_content;
	//�������ĵ�ͼƬ��
	private ListView find_detail_pic_listView;
	//���۵�һЩ�������
	private TextView commentNow, find_detail_comment_count_de, parseNow,
	find_detail_parse_count, find_detail_time;
	//�����û�������
	private ListView find_detail_comment_list_view;
	//����
	private FindDetaiListViewAdapter otherPCadapter;
	//��������
	private CommentDetailInformation CDInfoData = new CommentDetailInformation();
	//����һ���������ӵĶ���
	private HttpGetCommentJson httpGetCommentJson = new HttpGetCommentJson();
	//����һ��booleal��¼�û��Ƿ�������
	private boolean isClickParse = false;
	//���嵱ǰ�û��Ƿ��¼
	private boolean isLogin = false;
	//���۵�Id
	private int commentId;
	//������Ƭǽ��listView
	private ImageListViewAdapter picAdapter;
	private ScrollView scrollview;
	//���ļ��л�ȡ�û���Ϣ
	private SharedPreferences sharedpreferences;
	private User user;
	//�����쳣
	private RelativeLayout errorpage;
	//����
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

	private void initview() {//��ȡ�û���Ϣ
		sharedpreferences = getSharedPreferences("user",Context.MODE_PRIVATE);
		String userJson=sharedpreferences.getString("userJson", null);
		Gson gson = new Gson();
		user = gson.fromJson(userJson, User.class);
		//��ȡ����id
		Bundle bund = getIntent().getExtras();
		commentId = bund.getInt("commentId");
		//��ʼ��view
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
		//�����쳣
		errorpage = (RelativeLayout) findViewById(R.id.errorpage);
		//���ط���ҳ��
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
//		//������find_detail_pic_listView�е�scrollview�ĵ���¼�
//		find_detail_pic_listView.setOnTouchListener(new OnTouchListener() { 
//			        @Override public boolean onTouch(View arg0, MotionEvent arg1) { 
//			        	scrollview.requestDisallowInterceptTouchEvent(true); 
//					 return false; 
//		                  }	
//		            });
		//������find_detail_comment_list_view�е�scrollview�ĵ���¼�
		find_detail_comment_list_view.setOnTouchListener(new OnTouchListener() { 
	        @Override public boolean onTouch(View arg0, MotionEvent arg1) { 
	        	scrollview.requestDisallowInterceptTouchEvent(true); 
			 return false; 
                  }	
            });
		
		//�����������ݽ���
		new CDInfoDataTask().execute();
		//���comment�Ĵ���
		commentNow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//�ж��û��Ƿ��Ѿ���¼
				if(user.getUserId()==-1){
					AlertDialog.Builder ab = new AlertDialog.Builder(FindDetailsActivity.this);
					ab.setTitle("");
					ab.setMessage("�ף���¼���������Ŷ~");
					ab.setPositiveButton("ȥ��¼", new DialogInterface.OnClickListener() {						
						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(FindDetailsActivity.this, UserLoginActivity.class);
							FindDetailsActivity.this.startActivity(intent);
							FindDetailsActivity.this.finish();
							overridePendingTransition(R.drawable.interface_jump_in,
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
					displayCommentDailog();
				}
			}
		});
		//����޵Ĵ���
		parseNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//�ж��û��Ƿ��Ѿ�����
				if(isClickParse)
					Toast.makeText(FindDetailsActivity.this, "���Ѿ��޹���", 1000).show();
				else{				
					parseNow.setText("����");
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
        WindowManager.LayoutParams lp2 = dialogWindow2.getAttributes();//��ȡ�Ի���Ĳ���
        //��ȡ��Ļ��С
        WindowManager windowmanager2 = getWindowManager();
        Display display2 = windowmanager2.getDefaultDisplay();
		DisplayMetrics displayMetrics2 = new DisplayMetrics();
		display2.getMetrics(displayMetrics2);
        //ȥ��������
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//�Զ���Ի���layout
		dialog.setContentView(R.layout.dialogitem_find_sent_comment);
		
		lp2.height = displayMetrics2.heightPixels;
		lp2.width = displayMetrics2.widthPixels;		
	    dialogWindow2.setAttributes(lp2);
		dialog.show();
		//��ʼ��dailog�Ĳ���
		Button cancle_sent_find_comment = (Button) dialog.findViewById(R.id.cancle_sent_find_comment);
		Button confirm_sent_find_comment = (Button) dialog.findViewById(R.id.confirm_sent_find_comment);
		final EditText edit_find_comment_content = (EditText) dialog.findViewById(R.id.edit_find_comment_content);
		//ȡ�����۵Ĳ���
		cancle_sent_find_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.cancel();
			}
		});
		confirm_sent_find_comment.setOnClickListener(new OnClickListener() {
			//����������������
			@Override
			public void onClick(View arg0) {
				//��newһ������������
				OtherPeopleComment oPc = new OtherPeopleComment();
				oPc.setUserId(user.getUserId());
				oPc.setCommentTime(new Date());
				oPc.setOPCommentId(-1);
				oPc.setUserState(user.getUser_state());
				oPc.setCommentDetailInfoId(commentId);
				oPc.setCommentComtent(edit_find_comment_content.getText().toString().trim());
				//����һ�������߳�������
				new SetFindCommentTask().execute(oPc);
				find_detail_comment_count_de.setText(Integer.parseInt(find_detail_comment_count_de.getText().toString())+1+"");
				dialog.cancel();
			}
		});
	}
	//���ݼ������ʼ������
	public void afterDo() {
		//����listview��һ������
		//��ʾ��ͼ��find_detail_pic_listView�Ĵ���
		if(CDInfoData.getImageUrl()[0] != "")//���û�õ�ͼƬ���Ͳ���ʾ
			find_detail_pic_listView.setAdapter(picAdapter = 
			new ImageListViewAdapter(FindDetailsActivity.this, CDInfoData.getImageUrl()));
		else{
			find_detail_pic_listView.setVisibility(View.GONE);
		}
		setListViewHeight(find_detail_pic_listView, picAdapter);
		
		//��ʾ�û�����find_detail_comment_list_view�Ĵ���\
		find_detail_comment_list_view.setAdapter(otherPCadapter = new FindDetaiListViewAdapter(FindDetailsActivity.this, CDInfoData.getOtherPc()));
		//setListViewHeight(find_detail_comment_list_view, otherPCadapter);
		//��ʾ�û�ͷ��
		//Random �����
		int x=1+(int)(Math.random()*21);
		find_detail_user_define_pic.setImageResource(defaultPacage.headpic[x]);
		find_detail_user_name.setText(CDInfoData.getComment_from_user_name());
		String stateStr = null;
		if(CDInfoData.getUser_state() == User.state_�˶�����)
			stateStr = "�˶�����";
		if(CDInfoData.getUser_state() == User.state_�˶�ֿ��)
			stateStr = "�˶�ֿ��";
		if(CDInfoData.getUser_state() == User.state_�˶���)
			stateStr = "�˶���";
		if(CDInfoData.getUser_state() == User.state_ð��)
			stateStr = "ð��";
		if(CDInfoData.getUser_state() == User.state_լ��)
			stateStr = "լ��";
		if(CDInfoData.getUser_state() == User.state_��Ծ)
			stateStr = "��Ծ";
		if(stateStr == null)
			find_detail_user_state.setText("��״̬");
		else
			find_detail_user_state.setText(stateStr);
		find_detail_content.setText(CDInfoData.getComment_content());
		find_detail_time.setText(formatTime(CDInfoData.getComment_from_time()));
		Log.v("dfsdfds",CDInfoData.getHow_many_people_comment()+"");
		find_detail_comment_count_de.setText(""+CDInfoData.getHow_many_people_comment());
		find_detail_parse_count.setText(""+CDInfoData.getHow_many_people_praise());
		//��Scrollview��ʾ��ͷ��
		scrollview.scrollTo(0, 0);
		
	}	
	private CharSequence formatTime(Date commentTime) {
		//��ʽ��ʱ��Ĵ���
		
		long currentTime = System.currentTimeMillis();
		long commentTimeMil = commentTime.getTime();
		long timePassed = currentTime - commentTimeMil;
		long timeIntoFormat;
		String time = ""+-1;
		if (timePassed < ONE_MINUTE) {
			time = "�ո�";
		} else if (timePassed < ONE_HOUR) {
			timeIntoFormat = timePassed / ONE_MINUTE;
			time = timeIntoFormat + "����ǰ";
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			time = timeIntoFormat + "Сʱǰ"; 
		} else if(timePassed > ONE_DAY){
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String formatTime = sf.format(commentTime);
			String ss = formatTime.replaceFirst("-","��");
			time = ss.replaceAll("-","��")+"��";
		}
		return time;
		
	}
	//��̬����ListView�ĸ߶ȣ�
	private void setListViewHeight(ListView listView, BaseAdapter adapter) {
		adapter = (BaseAdapter) listView.getAdapter();    
        if (adapter == null) {    
            return;    
        }    
        int totalHeight = 0;    
        for (int i = 0, len = adapter.getCount(); i < len; i++) { // listAdapter.getCount()�������������Ŀ    
            View listItem = adapter.getView(i, null, listView);    
            listItem.measure(0, 0); // ��������View �Ŀ��    
            totalHeight += listItem.getMeasuredHeight(); // ͳ������������ܸ߶�    
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
			Toast.makeText(FindDetailsActivity.this, "���۳ɹ�", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
	}
	
	//���η��ؼ�
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

