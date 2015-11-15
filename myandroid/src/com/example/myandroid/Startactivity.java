package com.example.myandroid;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Startactivity extends Activity {
	Button btn1;
	Button btn2;
	TextView tv;
	LinearLayout layout;
	@SuppressLint("NewApi") protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //去除标题
        setContentView(R.layout.activity_main);
        layout=(LinearLayout) findViewById(R.id.layout); 
        tv=(TextView) findViewById(R.id.textView1); 
        btn1 = (Button) findViewById(R.id.button1);
        btn2= (Button)findViewById(R.id.button2);
        btn1.setAlpha(0.5f);
        //LinearLayout的动画 xml实现
        Animator Linanimator = AnimatorInflater.loadAnimator(Startactivity.this, R.anim.publicanimtions);
        Linanimator.setTarget(layout);
        Linanimator.start();
       
      //对textview的动画函数实现
		tv.setScaleY(0f);
      tvanimations();

      //btn1动画实现，用一个函数实现
//    btn1.setVisibility(View.GONE);
      btn1animations();
      btn1.setOnClickListener(new OnClickListener() {	
		@Override
		public void onClick(View v) {//进入游戏
			Intent intent = new Intent(Startactivity.this, MainActivity.class);
		    startActivity(intent) ;
		    Startactivity.this.finish();
		    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);        
		}
	});
      //btn2的动画实现，加载xml动画
      btn2.setTranslationX(-500f);
      Animator animator = AnimatorInflater.loadAnimator(Startactivity.this, R.anim.publicanimtions);
      animator.setTarget(btn2);
      animator.setStartDelay(5000);
      animator.start();
      
      btn2.setOnClickListener(new OnClickListener() {//进入关于帮助
		
		@Override
		public void onClick(View v1) {
			Intent intent = new Intent(Startactivity.this, gamehelpActivity.class);
		    startActivity(intent) ;
		    Startactivity.this.finish();
		    overridePendingTransition(R.anim.in_xyto_yx, R.anim.out_to_left);
		}
	});
	}
	//textview 的动画函数
	@SuppressLint("NewApi") private void tvanimations() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(tv,"scaleY", 0f, 3f,1f);
		anim.setDuration(1500);
		anim.setStartDelay(5000);
		anim.setRepeatCount(3);
		//参数repeatMode为动画效果的重复模式，常用的取值如下。RESTART：重新从头开始执行。REVERSE：反方向执行
		anim.setRepeatMode(ValueAnimator.REVERSE);
		anim.start();
		//动画监听器
		anim.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				tv.setScaleY(1f);
				
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	//btn1的动画函数
	 @SuppressLint("NewApi") private void btn1animations() {
		float curr = btn1.getTranslationX();
//		btn1.setVisibility(View.VISIBLE);
		btn1.setTranslationX(-1000f);
	    ObjectAnimator animator = ObjectAnimator.ofFloat(btn1,
	    		"translationX",-500f, curr,curr-10f,curr);  
	    animator.setDuration(500);
	    animator.setStartDelay(5000);
	    animator.start();
	}

}
