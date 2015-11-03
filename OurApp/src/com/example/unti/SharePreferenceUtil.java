package com.example.unti;

import android.content.Context;
import android.content.SharedPreferences;
/*
 * 自己写的SharedPreferences的工具类*/
public class SharePreferenceUtil {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private Context context;
	private String file;
	
	public SharePreferenceUtil(Context context, String file)
	{
		this.context = context;
		this.file = file;
	}
	public void writeTOSharePfString(String key, String content[]){
		StringBuilder sb = new StringBuilder();
		for (String string : content) {
			sb.append(string);
			sb.append(":");
		}
		sb.deleteCharAt(sb.length()-1);
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
		editor.putString(key, sb.toString());
		editor.commit();
		}
	public void writeTOSharePfInt(String key, int content[]){
		StringBuilder sb = new StringBuilder();
		for (int i : content) {
			sb.append(i);
			sb.append(":");
		}
		sb.deleteCharAt(sb.length()-1);
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
		editor.putString(key, sb.toString());
		editor.commit();
		}
	public String[] readForSharePfString(String key){
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		String[] sge = sp.getString(key, null).split(":");
		return sge;
	}
	public int[] readForSharePfInt(String key){
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		String[] ints = sp.getString(key, null).split(":");
		int[] intsf = new int[ints.length];
		for (int i = 0 ; i < ints.length; i++)
			intsf[i] = Integer.parseInt(ints[i]);
		return intsf;
	}	
}
