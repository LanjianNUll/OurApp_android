package com.example.unti;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtil {
	public static boolean isNetConnected(Context context) {
		boolean isNetConnected;
		// ����������ӷ���
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			isNetConnected = true;
		} else {
			Log.v("����״̬", "û�п�������");
			isNetConnected = false;
		}
		return isNetConnected;
	}
}
