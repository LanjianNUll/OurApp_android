package com.example.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.activity.FindDetailsActivity;
import com.example.bean.OtherPeopleComment;
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
	public View getView(int arg0, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null ){
					holder = new ViewHolder();
					convertView = LayoutInflater.from(context).inflate(R.layout.find_detail_list_item, parent,false);
					holder.other_people_name = (TextView) convertView.findViewById(R.id.other_people_name);
					holder.other_people_say = (TextView) convertView.findViewById(R.id.other_people_say);
					holder.other_people_head_pic = (ImageView) convertView.findViewById(R.id.other_people_head_pic);
					holder.other_people_ctime = (TextView) convertView.findViewById(R.id.other_people_ctime);
					convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.other_people_name.setText(otherPeopleComments[arg0].getUserName());
			holder.other_people_say.setText(otherPeopleComments[arg0].getCommentComtent());
			holder.other_people_ctime.setText(formatTime(otherPeopleComments[arg0].getCommentTime()));
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
		TextView other_people_name, other_people_say, other_people_ctime;
		ImageView other_people_head_pic;
		
	}
	
}
