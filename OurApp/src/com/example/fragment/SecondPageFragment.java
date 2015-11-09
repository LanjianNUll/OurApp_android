package com.example.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.R.string;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.SportsPlaceDetail;
import com.example.activity.SubsSortActivity;
import com.example.adapter.ListViewAdapter;
import com.example.bean.SportPlace;
import com.example.bean.SportPlaceDetailInformation;
import com.example.httpunit.HttpGetSportPlaceJson;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;

public class SecondPageFragment extends Fragment {

	private View view;
	//top��ͼƬ��ť
	private ImageView comeback_main, search_image;
	//����Ĳ���
	private LinearLayout all_sort, distance_sort, people_like_sort;
	//���������
	private TextView all_sort_text, distance_sort_text, people_like_sort_text;
	//�����ͼƬ
	private ImageView all_sort_image, distance_sort_image, people_like_sort_image;
	//Listview
	private ListView list_nearby;
	//����listview
	private ListViewAdapter adapter;
	private  Dialog dialog, dialog1, dialog2;
	/*����һ����־ָ����ǰ���Ǹ�������   
	 * 0   �����з���
	 * 1  �ھ������
	 * 2 ���˶�������*
	 * Ĭ��Ϊ�ھ�����༴  dialog_int= 1*/
	private int dialog_int = 1;
	private int sortId = -1;
	private int distanceId = -1;
	private int sportstyleId = -1;
	//����ID
	private int cityId = -1;
	//����Ի������view
	private TextView all_sort_basktball_text,all_sort_running_text,all_sort_nettennis_text,
	all_sort_football_text,all_sort_swim_text,all_sort_gym_text,
	all_sort_tableball_text,all_sort_part_relax_text,all_sort_volleyball_text,
	all_sort_climb_text,all_sort_part_ride_text,all_sort_other_text;
	private TextView distance_sort_allcity_text,distance_sort_500m_text,distance_sort_3Km_text,
	distance_sort_5Km_text;
	private TextView peoplelike_sort_hardsport_text,peoplelike_sort_quitesport_text,peoplelike_sort_youngsport_text,
	peoplelike_sort_build_bodysport_text,peoplelike_besidesort_sport_text;
	//�����е�����
	private TextView jiazai_text;
	private ImageView jiazai_pic;
	private RelativeLayout loading ,errorpage;
	Handler handler;

	//����http���߰�
	private  HttpGetSportPlaceJson httpgetjson  = new HttpGetSportPlaceJson();

	//�����һ������
	private ArrayList<SportPlace> Data = new ArrayList<SportPlace>();
	//���ظ������Ŀ
	private int addMoreCount = 10;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.app_secondpage, container, false);
		//��ȡ����id
		SharedPreferences city= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
		cityId = city.getInt("city_id", -1);
		jiazai_pic = (ImageView) view.findViewById(R.id.jiazai_pic);
		jiazai_text = (TextView) view.findViewById(R.id.jiazai_text);
		loading = (RelativeLayout) view.findViewById(R.id.loading);
		//�����쳣����ʾ�Ľ���
		errorpage = (RelativeLayout) view.findViewById(R.id.errorpage);
		inintview();
		handler = new Handler(){
			int i = 0 ;
			public void handleMessage(Message msg) {
				//������Ϣʱ����Ҫ֪�������ǳɹ�����Ϣ������ʧ�ܵ���Ϣ
				switch (msg.what) {
				case 1:
					loading.setVisibility(View.GONE);
					list_nearby.setAdapter(adapter = new ListViewAdapter(getActivity(), Data));
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
				case 3:
					loading.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
					list_nearby.setSelection(addMoreCount+1);
					addMoreCount = addMoreCount+10;
					break;
				case 4:
					loading.setVisibility(View.VISIBLE);
					break;
				case 5:
					loading.setVisibility(View.GONE);
					errorpage.setVisibility(View.VISIBLE);
					break;
				}
				
			}       	
		};	 
		new Thread(){
			public void run(){
				try {
					 if (getActivity() == null) {  
					        return;  
					    }
					//�����ʱ�������json��json����
					//ģ���ʱ
					sleep(2000);
					Data = httpgetjson.getData(-1,-1);//��������
					if(Data == null)
						handler.sendEmptyMessage(5);
					else
						handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}					
			}	
		}.start();
		//��ʱ�߳�
		new Timer().schedule(new TimerTask() {            
			@Override  
			public void run() {  
				 if (getActivity() == null) {  
				        return;  
				    }
				Message msg = new Message();
				msg.what = 2;            	
				handler.sendMessage(msg);
			}  
		}, 0,100); //����������һ������ʱ��ò�ִ�У��ڶ����Ǹ����ִ��һ�� 

		return view;
	}	
	//��ʼ������
	private void inintview() {

		//��ListViewĩβ���һ�����ظ���Ĳ���
		View footView = LayoutInflater.from(getActivity()).inflate(R.layout.add_more_layout, null, false);  
		TextView addMoreView = (TextView) footView.findViewById(R.id.addMoreView);
		addMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View fv) {
				addMoreData(sortId , distanceId,sportstyleId);
				resetID();
			}
		});


		comeback_main = (ImageView) view.findViewById(R.id.comeback_mainpage_from_second);
		search_image = (ImageView) view.findViewById(R.id.search_nearby);

		all_sort = (LinearLayout) view.findViewById(R.id.all_sort);
		distance_sort = (LinearLayout) view.findViewById(R.id.distance_sort);
		people_like_sort = (LinearLayout) view.findViewById(R.id.people_like_sort);

		all_sort_text = (TextView) view.findViewById(R.id.all_sort_text);
		distance_sort_text = (TextView) view.findViewById(R.id.distance_sort_text);
		people_like_sort_text = (TextView) view.findViewById(R.id.people_like_sort_text);

		all_sort_image = (ImageView) view.findViewById(R.id.all_sort_image);
		distance_sort_image = (ImageView) view.findViewById(R.id.distance_sort_iamge);
		people_like_sort_image = (ImageView) view.findViewById(R.id.people_like_sort_image);

		list_nearby = (ListView) view.findViewById(R.id.show_the_sort);

		//ΪlistView���footview
		list_nearby.addFooterView(footView);

		//������Ӧʱ�����
		all_sort.setOnClickListener(new sortclicklistener());
		distance_sort.setOnClickListener(new sortclicklistener());
		people_like_sort.setOnClickListener(new sortclicklistener());

		Log.v("dfdg","sdfdsfds");
		//����ListView
		list_nearby.setAdapter(adapter = new ListViewAdapter(getActivity(), Data));
		Log.v("����������fdg","sdfdsfds");

		list_nearby.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3) {

				int SportPlaceId = Data.get(position).getSportplaceId();
				Intent intent = new Intent(getActivity(),SportsPlaceDetail.class);
				Bundle bundle = new Bundle();
				String name = "comefromSeconPage";
				bundle.putString("name",name);
				bundle.putInt("SportPlaceId",SportPlaceId);
				intent.putExtras(bundle);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}

	protected void resetID() {
		//���õ�ǰ������ID
		sortId = -1;
		distanceId = -1;
		sportstyleId = -1;		
	}
	protected void addMoreData(final int sortId2, final int distanceId2, final int sportstyleId2) {
		//��һ���߳̽����������ݼ���
		new Thread(){
			public void run(){
				
				//������ʱ����
				ArrayList<SportPlace> MoreData = new ArrayList<SportPlace>();
				MoreData = httpgetjson.getMoreDataData(cityId,sortId2, distanceId2, sportstyleId2, addMoreCount);		
				if(MoreData != null)
					for(int i = 0; i<MoreData.size(); i++)
						Data.add(MoreData.get(i));
				handler.sendEmptyMessage(3);
				Log.v("dsfsdfs","������Ϣ�ɹ�");
				}
		}.start();
		
	}
	
	//��ȡData����

	class sortclicklistener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			//�Ѽ��ظ��������������Ϊ10
			addMoreCount = 10;
			switch (v.getId()) {
			case R.id.all_sort:
				dialog_int = 0;
				dialog = new Dialog(getActivity());
				Window dialogWindow = dialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();//��ȡ�Ի���Ĳ���
				//��ȡ��Ļ��С
				WindowManager windowmanager = getActivity().getWindowManager();
				Display display = windowmanager.getDefaultDisplay();
				DisplayMetrics displayMetrics = new DisplayMetrics();
				display.getMetrics(displayMetrics);
				//ȥ��������
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//�Զ���Ի���layout
				dialog.setContentView(R.layout.dialogitem_allsort);
				////����Ի��������view
				inintall_sortview(dialog);
				// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
				lp.height = displayMetrics.heightPixels/2;
				lp.width = displayMetrics.widthPixels;
				dialogWindow.setAttributes(lp);
				dialog.show();
				//����dialog�����е�view
				all_sort_basktball_text.setOnClickListener(new AllsortClickListener());
				all_sort_running_text.setOnClickListener(new AllsortClickListener());
				all_sort_nettennis_text.setOnClickListener(new AllsortClickListener());
				all_sort_football_text.setOnClickListener(new AllsortClickListener());
				all_sort_swim_text.setOnClickListener(new AllsortClickListener());
				all_sort_gym_text.setOnClickListener(new AllsortClickListener());
				all_sort_tableball_text.setOnClickListener(new AllsortClickListener());
				all_sort_part_relax_text.setOnClickListener(new AllsortClickListener());
				all_sort_volleyball_text.setOnClickListener(new AllsortClickListener());
				all_sort_climb_text.setOnClickListener(new AllsortClickListener());
				all_sort_part_ride_text.setOnClickListener(new AllsortClickListener());
				all_sort_other_text.setOnClickListener(new AllsortClickListener());
				break;
			case R.id.distance_sort:
				dialog_int = 1;
				//Toast.makeText(getActivity(), "�������", 1000).show();
				dialog1 = new Dialog(getActivity());
				Window dialogWindow1 = dialog1.getWindow();
				WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();//��ȡ�Ի���Ĳ���
				//��ȡ��Ļ��С
				WindowManager windowmanager1 = getActivity().getWindowManager();
				Display display1 = windowmanager1.getDefaultDisplay();
				DisplayMetrics displayMetrics1 = new DisplayMetrics();
				display1.getMetrics(displayMetrics1);
				//ȥ��������
				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//�Զ���Ի���layout
				dialog1.setContentView(R.layout.dialogitem_distancesort);
				////����Ի��������view
				inint_distance_sort_view(dialog1);
				// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
				lp1.height = displayMetrics1.heightPixels/2;
				lp1.width = displayMetrics1.widthPixels;
				dialogWindow1.setAttributes(lp1);
				dialog1.show();
				//����dialog�����е�view
				distance_sort_allcity_text.setOnClickListener(new Distance_sort_ClickListener());
				distance_sort_500m_text.setOnClickListener(new Distance_sort_ClickListener());
				distance_sort_3Km_text.setOnClickListener(new Distance_sort_ClickListener());
				distance_sort_5Km_text.setOnClickListener(new Distance_sort_ClickListener());
				break;
			case R.id.people_like_sort:
				dialog_int = 2;
				//Toast.makeText(getActivity(), "���ַ���", 1000).show();
				dialog2 = new Dialog(getActivity());
				Window dialogWindow2 = dialog2.getWindow();
				WindowManager.LayoutParams lp2 = dialogWindow2.getAttributes();//��ȡ�Ի���Ĳ���
				//��ȡ��Ļ��С
				WindowManager windowmanager2 = getActivity().getWindowManager();
				Display display2 = windowmanager2.getDefaultDisplay();
				DisplayMetrics displayMetrics2 = new DisplayMetrics();
				display2.getMetrics(displayMetrics2);
				//ȥ��������
				dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//�Զ���Ի���layout
				dialog2.setContentView(R.layout.dialogitem_peoplelikesort);
				////����Ի��������view
				inint_people_like_sort_view(dialog2);
				// dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
				lp2.height = displayMetrics2.heightPixels/2;
				lp2.width = displayMetrics2.widthPixels;
				dialogWindow2.setAttributes(lp2);
				dialog2.show();
				//����dialog�����е�view
				peoplelike_sort_hardsport_text.setOnClickListener(new PeopleLike_sort_ClickListener());
				peoplelike_sort_quitesport_text.setOnClickListener(new PeopleLike_sort_ClickListener());
				peoplelike_sort_youngsport_text.setOnClickListener(new PeopleLike_sort_ClickListener());
				peoplelike_sort_build_bodysport_text.setOnClickListener(new PeopleLike_sort_ClickListener());
				peoplelike_besidesort_sport_text.setOnClickListener(new PeopleLike_sort_ClickListener());
				break;
			}	
		}			
	}
	private void inintall_sortview(Dialog dialog) {

		all_sort_basktball_text = (TextView) dialog.findViewById(R.id.all_sort_basktball_text);
		all_sort_running_text = (TextView) dialog.findViewById(R.id.all_sort_running_text);
		all_sort_nettennis_text = (TextView) dialog.findViewById(R.id.all_sort_nettennis_text);
		all_sort_football_text = (TextView) dialog.findViewById(R.id.all_sort_football_text);
		all_sort_swim_text = (TextView) dialog.findViewById(R.id.all_sort_swim_text);
		all_sort_gym_text = (TextView) dialog.findViewById(R.id.all_sort_gym_text);
		all_sort_tableball_text = (TextView) dialog.findViewById(R.id.all_sort_tableball_text);
		all_sort_part_relax_text = (TextView) dialog.findViewById(R.id.all_sort_part_relax_text);
		all_sort_volleyball_text = (TextView) dialog.findViewById(R.id.all_sort_volleyball_text);
		all_sort_climb_text = (TextView) dialog.findViewById(R.id.all_sort_climb_text);
		all_sort_part_ride_text = (TextView) dialog.findViewById(R.id.all_sort_part_ride_text);
		all_sort_other_text = (TextView) dialog.findViewById(R.id.all_sort_other_text);
	}

	public void inint_people_like_sort_view(Dialog dialog) {
		peoplelike_sort_hardsport_text = (TextView) dialog.findViewById(R.id.peoplelike_sort_hardsport_text);
		peoplelike_sort_quitesport_text = (TextView) dialog.findViewById(R.id.peoplelike_sort_quitesport_text);
		peoplelike_sort_youngsport_text = (TextView) dialog.findViewById(R.id.peoplelike_sort_youngsport_text);
		peoplelike_sort_build_bodysport_text = (TextView) dialog.findViewById(R.id.peoplelike_sort_build_bodysport_text);
		peoplelike_besidesort_sport_text = (TextView) dialog.findViewById(R.id.peoplelike_besidesort_sport_text);
	}
	public void inint_distance_sort_view(Dialog dialog) {

		distance_sort_allcity_text = (TextView) dialog.findViewById(R.id.distance_sort_allcity_text);
		distance_sort_500m_text = (TextView) dialog.findViewById(R.id.distance_sort_500m_text);
		distance_sort_3Km_text = (TextView) dialog.findViewById(R.id.distance_sort_3Km_text);
		distance_sort_5Km_text = (TextView) dialog.findViewById(R.id.distance_sort_5Km_text);

	}
	class AllsortClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.all_sort_basktball_text:
				senttitle("����", 101, -1, -1);
				sortId = 101;
				break;
			case R.id.all_sort_running_text:
				senttitle("�ܲ�", 102, -1, -1);
				sortId = 102;
				break;
			case R.id.all_sort_nettennis_text:
				senttitle("��ë��", 103, -1, -1);
				sortId = 103;
				break;
			case R.id.all_sort_football_text:
				senttitle("����", 104, -1, -1);
				sortId = 104;
				break;
			case R.id.all_sort_swim_text:
				senttitle("��Ӿ", 105, -1, -1);
				sortId = 105;
				break;
			case R.id.all_sort_gym_text:
				senttitle("����", 106, -1, -1);
				sortId = 106;
				break;
			case R.id.all_sort_tableball_text:
				senttitle("ƹ����", 107, -1, -1);
				sortId = 107;
				break;
			case R.id.all_sort_part_relax_text:
				senttitle("��԰����", 108, -1, -1);
				sortId = 108;
				break;
			case R.id.all_sort_volleyball_text:
				senttitle("����", 109, -1, -1);
				sortId = 109;
				break;
			case R.id.all_sort_climb_text:
				senttitle("��ɽ", 110, -1, -1);
				sortId = 110;
				break;
			case R.id.all_sort_part_ride_text:
				senttitle("����", 111, -1, -1);
				sortId = 111;
				break;
			case R.id.all_sort_other_text:
				senttitle("����", 112, -1, -1);
				sortId = 112;
				break;
			}
		}
	}
	class Distance_sort_ClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.distance_sort_allcity_text:
				distanceId = 11;
				senttitle("allcity", -1, 11, -1);
				break;
			case R.id.distance_sort_500m_text:
				distanceId = 12;
				senttitle("500m", -1, 12, -1);
				break;
			case R.id.distance_sort_3Km_text:
				distanceId = 13;
				senttitle("3km", -1, 13, -1);
				break;
			case R.id.distance_sort_5Km_text:
				distanceId = 14;
				senttitle("5km", -1, 14, -1);
				break;
			}
		}
	}
	class PeopleLike_sort_ClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.peoplelike_sort_hardsport_text:
				sportstyleId = SportPlaceDetailInformation.type_���ż���;
				senttitle("���ż���", -1, -1, sportstyleId);
				break;
			case R.id.peoplelike_sort_quitesport_text:
				sportstyleId = SportPlaceDetailInformation.type_��������;
				senttitle("������ʱ", -1, -1, sportstyleId);
				break;
			case R.id.peoplelike_sort_youngsport_text:
				sportstyleId = SportPlaceDetailInformation.type_����ר��;
				senttitle("����ר��", -1, -1, sportstyleId);
				break;
			case R.id.peoplelike_sort_build_bodysport_text:
				sportstyleId = SportPlaceDetailInformation.type_ǿ����;
				senttitle("ǿ����", -1, -1, sportstyleId);
				break;
			case R.id.peoplelike_besidesort_sport_text:
				sportstyleId = SportPlaceDetailInformation.type_Ұ��̽��;
				senttitle("Ұ��̽��", -1, -1, sportstyleId);	
				break;
			}
		}
	}	
	public void senttitle(String string, final int sort_id, final int distance_id, final int sportstyle_id) {			
		//Ĭ��city_id Ϊһ
		new Thread(){
			public void run(){
				try {
					//�����ʱ�������json��json����
					//ģ���ʱ
					handler.sendEmptyMessage(4);//�����еĶ���
					Thread.sleep(2000);
					Data = httpgetjson.getallData(cityId, sort_id, distance_id, sportstyle_id);
					//���������쳣����Ϣ����
					if(Data == null)
						handler.sendEmptyMessage(5);
					else handler.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
				}					
			}	
		}.start();
		//			HttpGetSportPlaceJson httpgetsportplace1 = new HttpGetSportPlaceJson();
		//			Data = httpgetjson.getallData(city_id,sort_id, distance_id, sportstyle_id);
		//			adapter.notifyDataSetChanged();
		if(dialog_int == 0){
			dialog.dismiss();
			resetother();
			all_sort_text.setTextColor(Color.RED);
			all_sort_text.setText(string);
			all_sort_image.setImageResource(R.drawable.xiala);
		}
		if(dialog_int == 1){
			dialog1.dismiss();
			resetother();
			distance_sort_text.setTextColor(Color.RED);
			distance_sort_text.setText(string);
			distance_sort_image.setImageResource(R.drawable.xiala);
		}
		if(dialog_int == 2){
			dialog2.dismiss();
			resetother();
			people_like_sort_text.setTextColor(Color.RED);
			people_like_sort_text.setText(string);
			people_like_sort_image.setImageResource(R.drawable.xiala);
		}
		Log.v("���½�����","������"+string);
	}
	private void resetother() {

		all_sort_text.setText("���з���");
		distance_sort_text.setText("��������");
		people_like_sort_text.setText("�˶���");
		all_sort_text.setTextColor(Color.BLACK);
		distance_sort_text.setTextColor(Color.BLACK);
		people_like_sort_text.setTextColor(Color.BLACK);
		all_sort_image.setImageResource(R.drawable.shouqi);
		distance_sort_image.setImageResource(R.drawable.shouqi);
		people_like_sort_image.setImageResource(R.drawable.shouqi);
	}
}

