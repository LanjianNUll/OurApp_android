package com.example.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.example.bean.Comment;
import com.example.bean.User;
import com.example.ourapp.R;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FindSortListViewAdapter extends BaseAdapter {

	private ArrayList<Comment> find_list;
	private Context context;
	private static final long ONE_MINUTE = 60 * 1000;
	private static final long ONE_HOUR = 60 * ONE_MINUTE;
	private static final long ONE_DAY = 24 * ONE_HOUR;
	private String stateStr;
	public FindSortListViewAdapter(FragmentActivity activity,
			ArrayList<Comment> find_list) {
			this.context = activity;
			this.find_list = find_list;
		}

	@Override
	public int getCount() {
		return find_list.size();
	}

	@Override
	public Object getItem(int position) {
		return find_list.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int positon, View convertView, ViewGroup parent) {
		 ViewHolder holder = null; 
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.find_sort_list_item, parent, false);  
				holder.sport_place_pic = (ImageView) convertView.findViewById(R.id.sport_place_pic);
				holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
				holder.user_state = (TextView) convertView.findViewById(R.id.user_state);
				holder.user_advice = (TextView) convertView.findViewById(R.id.user_advice);
				holder.advic_counts = (TextView) convertView.findViewById(R.id.advic_counts);
				holder.view_counts = (TextView) convertView.findViewById(R.id.view_counts);
				holder.comment_from_time = (TextView) convertView.findViewById(R.id.comment_from_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.user_name.setText(find_list.get(positon).getComment_from_user_name());
			if(find_list.get(positon).getUser_state() == User.state_�˶�����)
				stateStr = "�˶�����";
			if(find_list.get(positon).getUser_state() == User.state_�˶�ֿ��)
				stateStr = "�˶�ֿ��";
			if(find_list.get(positon).getUser_state() == User.state_�˶���)
				stateStr = "�˶���";
			if(find_list.get(positon).getUser_state() == User.state_ð��)
				stateStr = "ð��";
			if(find_list.get(positon).getUser_state() == User.state_լ��)
				stateStr = "լ��";
			if(find_list.get(positon).getUser_state() == User.state_��Ծ)
				stateStr = "��Ծ";
			holder.user_state.setText(stateStr);
			
			holder.user_advice.setText(find_list.get(positon).getComment_content());
			holder.advic_counts.setText(find_list.get(positon).getHow_many_people_comment()+"");
			holder.view_counts.setText(find_list.get(positon).getHow_many_people_see()+"");
			holder.comment_from_time.setText(formatTime(find_list.get(positon).getComment_from_time()));
			
			if(find_list.get(positon).getComment_type()==0)
				holder.sport_place_pic.setImageResource(R.drawable.comment_type0);
			if(find_list.get(positon).getComment_type()==1)
				holder.sport_place_pic.setImageResource(R.drawable.comment_type1);
			if(find_list.get(positon).getComment_type()==2)
				holder.sport_place_pic.setImageResource(R.drawable.comment_type2);
			if(find_list.get(positon).getComment_type()==3)
				holder.sport_place_pic.setImageResource(R.drawable.comment_type3);	
			return convertView;
		}


		private final class ViewHolder {
			ImageView sport_place_pic;
			TextView user_name, user_state, user_advice, advic_counts, view_counts, comment_from_time;
			
			
		}
		//��ʱ���һ������
		private CharSequence formatTime(Date comment_from_time) {
			long currentTime = System.currentTimeMillis();
			long commentTimeMil = comment_from_time.getTime();
			long timePassed = currentTime - commentTimeMil;
			long timeIntoFormat;
			String time = ""+-1;
			if (timePassed < ONE_MINUTE) {
				time = "�ո�";
			} else if (timePassed < ONE_HOUR) {
				timeIntoFormat = timePassed / ONE_MINUTE;
				time = timeIntoFormat + "����";
			} else if (timePassed < ONE_DAY) {
				timeIntoFormat = timePassed / ONE_HOUR;
				time = timeIntoFormat + "Сʱ"; 
			} else if(timePassed > ONE_DAY){
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				String formatTime = sf.format(comment_from_time);
				String ss = formatTime.replaceFirst("-","��");
				time = ss.replaceAll("-","��")+"��";
			}
			return time;
		}		
}
