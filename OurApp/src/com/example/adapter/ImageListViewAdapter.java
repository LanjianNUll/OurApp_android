package com.example.adapter;

import com.example.activity.FindDetailsActivity;
import com.example.ourapp.R;
import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageListViewAdapter extends BaseAdapter {

	private Context context;
	private String[] picData;
	
	
	
	public ImageListViewAdapter(FindDetailsActivity findDetailsActivity,
			String[] picData) {
		this.context = findDetailsActivity;
		this.picData = picData;			
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return picData.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return picData[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup v) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.commnet_pic_listview, v, false);
			holder.pic_listview_item = (SmartImageView) convertView.findViewById(R.id.pic_listview_item);
			convertView.setTag(holder);
		}else{
			 holder = (ViewHolder) convertView.getTag();
		}
		String Url = "http://xiafucheng.6655.la:20128/webAdroid/image/";
		holder.pic_listview_item.setImageUrl(Url+picData[position]);
		
		return convertView;
	}
	
	final class ViewHolder{
		SmartImageView  pic_listview_item;
	}

}
