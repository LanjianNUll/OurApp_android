package com.example.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.activity.SportsPlaceDetail;
import com.example.bean.SportPlaceComment;
import com.example.ourapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SPCommentListAdapter extends BaseAdapter {


	private static final long ONE_MINUTE = 60 * 1000;
	private static final long ONE_HOUR = 60 * ONE_MINUTE;
	private static final long ONE_DAY = 24 * ONE_HOUR;


	private SportPlaceComment[] sPcomment;
	private Context context;
	
	public SPCommentListAdapter(SportsPlaceDetail sportsPlaceDetail,
			SportPlaceComment[] sPcomment) {
		this.context = sportsPlaceDetail;
		this.sPcomment = sPcomment;
	}

	@Override
	public int getCount() {
		return sPcomment.length;
	}

	@Override
	public Object getItem(int arg0) {
		return sPcomment[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		 ViewHolder holder = null; 
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.sport_place_comment_item, parent, false);  
				holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
				holder.content_text = (TextView) convertView.findViewById(R.id.content_text);
				holder.comment_time = (TextView) convertView.findViewById(R.id.comment_time);
				holder.comment_range = (TextView) convertView.findViewById(R.id.comment_range);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}	
			holder.user_name.setText(sPcomment[position].getUserName());
			holder.content_text.setText(sPcomment[position].getCommentComtent());
			holder.comment_range.setText(sPcomment[position].getStartRange()+"");
			String Time = formatTimeUnit(sPcomment[position].getCommentTime());
			holder.comment_time.setText(Time);
			return convertView;
		}
	
		
		private final class ViewHolder {
			TextView user_name, content_text, comment_time, comment_range;		
		}
			
		private String formatTimeUnit(Date commentTime) {
			long currentTime = System.currentTimeMillis();
			long commentTimeMil = commentTime.getTime();
			long timePassed = currentTime - commentTimeMil;
			long timeIntoFormat;
			String time = ""+-1;
			if (timePassed < ONE_MINUTE) {
				time = "刚刚";
			} else if (timePassed < ONE_HOUR) {
				timeIntoFormat = timePassed / ONE_MINUTE;
				time = timeIntoFormat + "分钟前";
			} else if (timePassed < ONE_DAY) {
				timeIntoFormat = timePassed / ONE_HOUR;
				time = timeIntoFormat + "小时前"; 
			} else if(timePassed > ONE_DAY){
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				String formatTime = sf.format(commentTime);
				String ss = formatTime.replaceFirst("-","年");
				time = ss.replaceAll("-","月")+"日";
			}
			return time;
		}		
}
