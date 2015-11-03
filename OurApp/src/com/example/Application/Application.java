package com.example.Application;

import com.baidu.frontia.FrontiaApplication;
import com.example.PushServer.BaiduPush;



public class Application extends FrontiaApplication {
	

	/**
	 * API_KEY
	 */
	public final static String API_KEY = "XneBYWwVGAFGM2ftqP7V5X7K";
	/**
	 * SECRET_KEY
	 */
	public final static String SECRIT_KEY = "BZQm2ZkR2GA5Opx2knuGpu4A3H9i8CLm";
	public static final String SP_FILE_NAME = "push_msg_sp";
	//µ¥Àý
	public static Application mApplication;
	
	public synchronized static Application getInstance()
	{
		if(mApplication == null)
			mApplication = new Application();
		return mApplication;
	}
	
	private BaiduPush mBaiduPushServer;
	
	public synchronized BaiduPush getBaiduPush()
	{
		if (mBaiduPushServer == null)
			mBaiduPushServer = new BaiduPush(BaiduPush.HTTP_METHOD_POST,
					SECRIT_KEY, API_KEY);
		return mBaiduPushServer;
	}
}
