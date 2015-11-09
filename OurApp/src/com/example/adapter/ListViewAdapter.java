package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.bean.SportPlace;
import com.example.ourapp.R;
import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	private Context context;
	private  ArrayList<SportPlace> list;
	
	public ListViewAdapter(Activity activity,  ArrayList<SportPlace> arrayList) {
		this.context = activity;	
		this.list = arrayList;
	}
	@Override
	public int getCount() {
		if(list == null)
			return 0;
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder = null; 
			if (convertView == null) {
				Log.v("sdfsd","dsfdsfdsfdfds");
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.list_sort_item, parent, false);  
				holder.sport_place_image = (SmartImageView) convertView.findViewById(R.id.sport_place_image);
				holder.sport_place_name = (TextView) convertView.findViewById(R.id.sport_place_name);
				holder.values = (TextView) convertView.findViewById(R.id.values);
				holder.place_position = (TextView) convertView.findViewById(R.id.place_position);
				holder.distance = (TextView) convertView.findViewById(R.id.distance);
				holder.content_text = (TextView) convertView.findViewById(R.id.content_text);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			//ÍøÂçÍ¼Æ¬
			Log.v( "ÍøÂçÍ¼Æ¬µØÖ·", ""+list.get(position).getSportplace_imageUrl());
			String url = "http://xiafucheng.6655.la:20128/webAdroid/image/";
			holder.sport_place_image.setImageUrl(url+list.get(position).getSportplace_imageUrl());
			holder.sport_place_name.setText(list.get(position).getSportplace_name());
			holder.values.setText(list.get(position).getSportplace_value());
			holder.place_position.setText(list.get(position).getSportplace_location());
			holder.distance.setText(list.get(position).getSportplace_distance()+"m");
			holder.content_text.setText(list.get(position).getSportplace_discrb());
			return convertView;
		}

		private final class ViewHolder {
			SmartImageView sport_place_image;
			TextView sport_place_name,values,place_position,distance, content_text;
			
		}


}
