package com.example.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.CircleImageView.CircleImageView;
import com.example.Data.defaultPacage;
import com.example.activity.FindDetailsActivity;
import com.example.activity.UserDetailInfoActivity;
import com.example.bean.OtherPeopleComment;
import com.example.bean.User;
import com.example.ourapp.R;

public class FindDetaiListViewAdapter extends BaseAdapter {
	
	private static final long ONE_MINUTE = 60 * 1000;
	private static final long ONE_HOUR = 60 * ONE_MINUTE;
	private static final long ONE_DAY = 24 * ONE_HOUR;
	
	private OtherPeopleComment[] otherPeopleComments;
	private Context context;
	public FindDetaiListViewAdapter(FindDetailsActivity findDetailsActivity,
			OtherPeopleComment[] otherPeopleComments) {
		this.context = findDetailsActivity;
		this.otherPeopleComments = otherPeopleComments;
				
	}
	@Override
	public int getCount() {
		if(otherPeopleComments == null)
			return 0;
		return otherPeopleComments.length;
	}

	@Override
	public Object getItem(int arg0) {
		return otherPeopleComments[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null ){
					holder = new ViewHolder();
					convertView = LayoutInflater.from(context).inflate(R.layout.find_detail_list_item, parent,false);
					holder.other_people_name = (TextView) convertView.findViewById(R.id.other_people_name);
					holder.other_people_say = (TextView) convertView.findViewById(R.id.other_people_say);
					holder.other_people_head_pic = (CircleImageView) convertView.findViewById(R.id.other_people_head_pic);
					holder.other_people_ctime = (TextView) convertView.findViewById(R.id.other_people_ctime);
					holder.other_people_state = (TextView) convertView.findViewById(R.id.other_people_state);
					convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			// HeadPic
			holder.other_people_head_pic.setImageResource(defaultPacage.headpic[arg0%22]);
			//点击头像事件
			holder.other_people_head_pic.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, UserDetailInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("userId", otherPeopleComments[arg0].getUserId());
					bundle.putString("fromwhere", "findDetailListView");
					intent.putExtras(bundle);
					context.startActivity(intent);
					((Activity) context).finish();
					((Activity) context).overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
					
				}
			});
			//点击用户名事件
			holder.other_people_name.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, UserDetailInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("userId", otherPeopleComments[arg0].getUserId());
					bundle.putString("fromwhere", "findDetailListView");
					intent.putExtras(bundle);
					context.startActivity(intent);
					((Activity) context).finish();
					((Activity) context).overridePendingTransition(R.drawable.interface_jump_in,
							R.drawable.interface_jump_out);
				}
			});
			holder.other_people_name.setText(otherPeopleComments[arg0].getUserName());
			holder.other_people_say.setText(otherPeopleComments[arg0].getCommentComtent());
			holder.other_people_ctime.setText(formatTime(otherPeopleComments[arg0].getCommentTime()));
			String stateStr = null;
			if(otherPeopleComments[arg0].getUserState() == User.state_运动达人)
				stateStr  = "运动达人";
			if(otherPeopleComments[arg0].getUserState()  == User.state_运动挚友)
				stateStr = "运动挚友";
			if(otherPeopleComments[arg0].getUserState()  == User.state_运动狂)
				stateStr = "运动狂";
			if(otherPeopleComments[arg0].getUserState()  == User.state_冒泡)
				stateStr = "冒泡";
			if(otherPeopleComments[arg0].getUserState()  == User.state_宅客)
				stateStr = "宅客";
			if(otherPeopleComments[arg0].getUserState()  == User.state_活跃)
				stateStr = "活跃";
			if(stateStr == null)
				holder.other_people_state.setText("无状态");
			else
				holder.other_people_state.setText(stateStr);
		return convertView;
	}

	
	private CharSequence formatTime(Date commentTime) {

		long currentTime = System.currentTimeMillis();
		long commentTimeMil = commentTime.getTime();
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
			String formatTime = sf.format(commentTime);
			String ss = formatTime.replaceFirst("-","年");
			time = ss.replaceAll("-","月")+"日";
		}
		return time;
	}


	final class ViewHolder {
		TextView other_people_name, other_people_say, other_people_ctime, other_people_state;
		CircleImageView other_people_head_pic;
		
	}
}
