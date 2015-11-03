package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	List<Fragment> list = new ArrayList<Fragment>();
	public ViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	public Fragment getItem(int arg0) {
		
		return list.get(arg0);
	}

	public int getCount() {
		return list.size();
	}
}