package com.example.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.bean.LeaveWord;
import com.example.ourapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LeaveWordListViewAdapter extends BaseAdapter {
	private Context context;
	private LeaveWord[] leaveWord ;
	private static final long ONE_MINUTE = 60 * 1000;
	private static final long ONE_HOUR = 60 * ONE_MINUTE;
	private static final long ONE_DAY = 24 * ONE_HOUR;
	public LeaveWordListViewAdapter(Context context, LeaveWord[] leaveWord) {
			this.context = context;
			this.leaveWord = leaveWord;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return leaveWord[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		Holder holder = null;
		if(converView == null){
			holder = new Holder();
			converView = LayoutInflater.from(context).inflate(R.layout.leave_word_item, parent, false);
			holder.leave_content = (TextView) converView.findViewById(R.id.leave_content);
			holder.leave_time = (TextView) converView.findViewById(R.id.leave_time);
			holder.leave_user_name = (TextView) converView.findViewById(R.id.leave_user_name);
			converView.setTag(holder);
		}else{
			converView.getTag();
		}
		holder.leave_content.setText(leaveWord[position].getLeaverWordContent());
		holder.leave_user_name.setText(leaveWord[position].getFromUser_name());
		holder.leave_time.setText(timeFormat(leaveWord[position].getFromLeaveWordtime()));
		
		
		return converView;
	}
	final class Holder{
		private  TextView leave_content, leave_time, leave_user_name;
	}
	
	private CharSequence timeFormat(Date fromLeaveWordtime) {
		long currentTime = System.currentTimeMillis();
		long commentTimeMil = fromLeaveWordtime.getTime();
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
			String formatTime = sf.format(fromLeaveWordtime);
			String ss = formatTime.replaceFirst("-","年");
			time = ss.replaceAll("-","月")+"日";
		}
		return time;
	}		
}
