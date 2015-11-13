package com.example.ourapp;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.ViewPagerAdapter;
import com.example.fragment.LastPageFragment;
import com.example.fragment.MainPageFragment;
import com.example.fragment.SecondPageFragment;
import com.example.fragment.ThirdPageFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	//定义底部布局
	private LinearLayout bottom_mainPage,bottom_secondPage,bottom_thirdPage,bottom_lastPage;
	//定义底部Image
	private ImageView bottom_mainPage_imge,bottom_secondPage_imge,bottom_thirdPage_image,bottom_lastPage_image;
	//定义底部的Textview
	private TextView bottom_mainPage_text,bottom_secondPage_text,bottom_thirdPage_text,bottom_lastPage_text;
	//滑动的线
	private ImageView mTabLine;
	//屏幕的四分之一
	private int widthScreen1_4;
	
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAapter;
	
	private String city;
	private TextView main_activity_top_citytext;
	//记录第一次点击back键的时间
	long exitTime = 0;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏系统ActionBar
		setContentView(R.layout.main_layout);

		initview();
		//初始化Tabline的布局
		initTabLine();	
	}
	
	private void initview() {
		//底部滑条
		mTabLine = (ImageView) findViewById(R.id.bottom_tabline);
		//底部布局
		bottom_mainPage = (LinearLayout) findViewById(R.id.bottom_mainpage_lin_layout);
		bottom_secondPage = (LinearLayout) findViewById(R.id.bottom_secondPage_lin_layout);
		bottom_thirdPage = (LinearLayout) findViewById(R.id.bottom_thirdPage_lin_layout);
		bottom_lastPage = (LinearLayout) findViewById(R.id.bottom_lastPage_lin_layout);
		//底部Image
		bottom_mainPage_imge = (ImageView) findViewById(R.id.bottom_mainpage_image);
		bottom_secondPage_imge = (ImageView) findViewById(R.id.bottom_secondPage_iamge);
		bottom_thirdPage_image = (ImageView) findViewById(R.id.bottom_thirdPage_iamge);
		bottom_lastPage_image = (ImageView) findViewById(R.id.bottom_lastPage_image);
		//底部Text
		bottom_mainPage_text = (TextView) findViewById(R.id.bottom_mainpage_text);
		bottom_secondPage_text = (TextView) findViewById(R.id.bottom_secondPage_text);
		bottom_thirdPage_text = (TextView) findViewById(R.id.bottom_thirdPage_text);
		bottom_lastPage_text = (TextView) findViewById(R.id.bottom_lastPage_text);
		//view的获取
		viewPager = (ViewPager) findViewById(R.id.id_viewpager);
		//用把各个Fragment放在list容器
		List<Fragment> list = new ArrayList<Fragment>();
		list.add(new MainPageFragment());
		list.add(new SecondPageFragment());
		list.add(new ThirdPageFragment());
		list.add(new LastPageFragment());
		//适配
		viewPagerAapter = new ViewPagerAdapter(getSupportFragmentManager(),list); 
		viewPager.setAdapter(viewPagerAapter);
		clearcolor();
		//初始化第一页
		viewPager.setCurrentItem(0,true);
		bottom_mainPage_imge.setImageResource(R.drawable.home1);
		bottom_mainPage_text.setTextColor(Color.parseColor("#008000"));
		//重其他界面跳过了是回到滑动的第几页
		Bundle bundle = MainActivity.this.getIntent().getExtras();
		int currentItem = bundle.getInt("CurrentItem");
		if(currentItem==1){
			String city_name = bundle.getString("city_name");
			//Toast.makeText(this, "城市名"+city_name, 1000).show();
			viewPager.setCurrentItem(1,true);
			//首页的滑块去颜色
			clearcolor();
			//当前页上色
			bottom_secondPage_imge.setImageResource(R.drawable.nearby1);
			bottom_secondPage_text.setTextColor(Color.parseColor("#008000"));
		}
		if(currentItem==2){	
			viewPager.setCurrentItem(2,true);
			//首页的滑块去颜色
			clearcolor();
			//当前页上色
			bottom_thirdPage_image.setImageResource(R.drawable.find1);
			bottom_thirdPage_text.setTextColor(Color.parseColor("#008000"));
		}
		if(currentItem==3){	
			viewPager.setCurrentItem(3,true);
			//首页的滑块去颜色
			clearcolor();
			//当前页上色
			bottom_lastPage_image.setImageResource(R.drawable.me1);
			bottom_lastPage_text.setTextColor(Color.parseColor("#008000"));
		}
		//监听滑动
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				resetTextViewColor();
				resetImageViewSrc();
				switch (position) {
				case 0:
					bottom_mainPage_imge.setImageResource(R.drawable.home1);
					bottom_mainPage_text.setTextColor(Color.parseColor("#008000"));
					break;
				case 1:
					bottom_secondPage_imge.setImageResource(R.drawable.nearby1);
					bottom_secondPage_text.setTextColor(Color.parseColor("#008000"));
					break;
				case 2:
					bottom_thirdPage_image.setImageResource(R.drawable.find1);
					bottom_thirdPage_text.setTextColor(Color.parseColor("#008000"));
					break;
				case 3:
					bottom_lastPage_image.setImageResource(R.drawable.me1);
					bottom_lastPage_text.setTextColor(Color.parseColor("#008000"));
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffPx) {
				/**屏幕滑动时，tabLine的布局  */
				LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
						.getLayoutParams();
				//mTabLine.setBackgroundColor(Color.parseColor("#145554"));
				lp.leftMargin = (int) ((position + positionOffset) * widthScreen1_4);//控制滑动时，离左边的边距
				mTabLine.setLayoutParams(lp);				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
			//重置Image的颜色
			private void resetImageViewSrc() {
				bottom_mainPage_imge.setImageResource(R.drawable.home);
				bottom_secondPage_imge.setImageResource(R.drawable.nearby);
				bottom_thirdPage_image.setImageResource(R.drawable.find);
				bottom_lastPage_image.setImageResource(R.drawable.me);
			}
			//重置text的颜色
			private void resetTextViewColor() {
				bottom_mainPage_text.setTextColor(Color.parseColor("#A6A6A6"));
				bottom_secondPage_text.setTextColor(Color.parseColor("#A6A6A6"));
				bottom_thirdPage_text.setTextColor(Color.parseColor("#A6A6A6"));
				bottom_lastPage_text.setTextColor(Color.parseColor("#A6A6A6"));
			}
		});
		//底部布局的监听,自定义监听类
			BottomLayoutListener listener = new BottomLayoutListener();
			bottom_mainPage.setOnClickListener(listener);
			bottom_secondPage.setOnClickListener(listener);
			bottom_thirdPage.setOnClickListener(listener);
			bottom_lastPage.setOnClickListener(listener);		
	}
	private void clearcolor() {
		bottom_mainPage_imge.setImageResource(R.drawable.home);
		bottom_secondPage_imge.setImageResource(R.drawable.nearby);
		bottom_thirdPage_image.setImageResource(R.drawable.find);
		bottom_lastPage_image.setImageResource(R.drawable.me);
		
		bottom_mainPage_text.setTextColor(Color.parseColor("#A6A6A6"));
		bottom_secondPage_text.setTextColor(Color.parseColor("#A6A6A6"));
		bottom_thirdPage_text.setTextColor(Color.parseColor("#A6A6A6"));
		bottom_lastPage_text.setTextColor(Color.parseColor("#A6A6A6"));
	}
	class BottomLayoutListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bottom_mainpage_lin_layout:
				viewPager.setCurrentItem(0, true);				
				break;
			case R.id.bottom_secondPage_lin_layout:
				viewPager.setCurrentItem(1, true);
				break;
			case R.id.bottom_thirdPage_lin_layout:
				viewPager.setCurrentItem(2, true);
				break;
			case R.id.bottom_lastPage_lin_layout:
				viewPager.setCurrentItem(3, true);
				break;
			}
		}
	}
	
	private void initTabLine() {
		Display display = getWindow().getWindowManager().getDefaultDisplay();//获取屏幕的大小
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		widthScreen1_4 = displayMetrics.widthPixels / 4;//整个屏幕分为四部分；注意这里的1/4并不是TabLine的宽度
		LayoutParams lp = mTabLine.getLayoutParams();
		lp.width = widthScreen1_4;
		mTabLine.setLayoutParams(lp);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
//        	//程序退出是将定位的布尔值改为true
//			SharedPreferences startloaction = getSharedPreferences("loaction_start", Context.MODE_PRIVATE);
//			Editor editor=startloaction.edit();
//			editor.putBoolean("isstartloaction", true);
//			editor.commit();
//            MainActivity.this.finish();
            System.exit(0);
        }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
