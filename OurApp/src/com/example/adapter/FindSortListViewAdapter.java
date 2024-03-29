package com.example.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.CircleImageView.CircleImageView;
import com.example.Data.defaultPacage;
import com.example.activity.UserDetailInfoActivity;
import com.example.bean.Comment;
import com.example.bean.User;
import com.example.ourapp.MainActivity;
import com.example.ourapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
	private String stateStr = "冒泡";
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
	public View getView(final int positon, View convertView, ViewGroup parent) {
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
				holder.user_define_pic = (CircleImageView) convertView.findViewById(R.id.user_define_pic);
				holder.comment_from_time = (TextView) convertView.findViewById(R.id.comment_from_time);
				holder.comment_address = (TextView) convertView.findViewById(R.id.comment_address);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.user_name.setText(find_list.get(positon).getComment_from_user_name());
			if(find_list.get(positon).getUser_state() == User.state_运动达人)
				stateStr = "运动达人";
			if(find_list.get(positon).getUser_state() == User.state_运动挚友)
				stateStr = "运动挚友";
			if(find_list.get(positon).getUser_state() == User.state_运动狂)
				stateStr = "运动狂";
			if(find_list.get(positon).getUser_state() == User.state_冒泡)
				stateStr = "冒泡";
			if(find_list.get(positon).getUser_state() == User.state_宅客)
				stateStr = "宅客";
			if(find_list.get(positon).getUser_state() == User.state_活跃)
				stateStr = "活跃";
			holder.user_state.setText(stateStr);
			//将地点放在评论内容后面取出来
			String commentContent[] = find_list.get(positon).getComment_content().split("&&@@");
			holder.user_advice.setText(commentContent[0]);
			if(commentContent.length>1)
				holder.comment_address.setText(commentContent[1]);
			holder.advic_counts.setText(find_list.get(positon).getHow_many_people_comment()+"");
			holder.view_counts.setText(find_list.get(positon).getHow_many_people_see()+"");
			holder.comment_from_time.setText(formatTime(find_list.get(positon).getComment_from_time()));
			//用户的头像
			holder.user_define_pic.setImageResource(defaultPacage.headpic[positon%22]);
			//点击用户头像
			holder.user_define_pic.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					Intent intent = new Intent(context, UserDetailInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("userId", find_list.get(positon)
							.getComment_from_user_id());
					bundle.putString("fromwhere", "findSortListView");
					intent.putExtras(bundle);
					context.startActivity(intent);
					((Activity) context).finish();
					((Activity) context).overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
			});
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
			CircleImageView user_define_pic;
			TextView user_name, user_state, user_advice, advic_counts,
			view_counts, comment_from_time, comment_address;
			
			
		}
		//对时间的一波处理
		private CharSequence formatTime(Date comment_from_time) {
			long currentTime = System.currentTimeMillis();
			long commentTimeMil = comment_from_time.getTime();
			long timePassed = currentTime - commentTimeMil;
			long timeIntoFormat;
			String time = ""+-1;
			if (timePassed < ONE_MINUTE) {
				time = "刚刚";
			} else if (timePassed < ONE_HOUR) {
				timeIntoFormat = timePassed / ONE_MINUTE;
				time = timeIntoFormat + "分钟";
			} else if (timePassed < ONE_DAY) {
				timeIntoFormat = timePassed / ONE_HOUR;
				time = timeIntoFormat + "小时"; 
			} else if(timePassed > ONE_DAY){
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				String formatTime = sf.format(comment_from_time);
				String ss = formatTime.replaceFirst("-","年");
				time = ss.replaceAll("-","月")+"日";
			}
			return time;
		}		
}
