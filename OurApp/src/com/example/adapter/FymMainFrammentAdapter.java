package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ourapp.R;

public class FymMainFrammentAdapter extends BaseAdapter {

	private int [] data;
	private Context context;
	private String[] name = {"¿∫«Ú","≈‹≤Ω","”√´«Ú","◊„«Ú","”Œ”æ","Ω°…Ì∑ø","∆π≈“«Ú","π´‘∞–›œ–"
			,"≈≈«Ú","≈¿…Ω","∆Ô––","∆‰À˚"};
	public FymMainFrammentAdapter(Context context, int[] data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView == null){
			holder = new Holder();
			convertView = LayoutInflater.from(context).
					inflate(R.layout.fym_item, parent, false);
			holder.fymImage = (ImageView) convertView.findViewById(R.id.fymImage);
			holder.fymText = (TextView) convertView.findViewById(R.id.fymText);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.fymImage.setImageResource(data[position]);
		holder.fymText.setText(name[position]);
		return convertView;
	}

	final class Holder{
		ImageView fymImage;
		TextView fymText;
	}

}
