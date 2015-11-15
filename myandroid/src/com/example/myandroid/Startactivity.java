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
        requestWindowFeature(Window.FEATURE_NO_TITLE); //ȥ������
        setContentView(R.layout.activity_main);
        layout=(LinearLayout) findViewById(R.id.layout); 
        tv=(TextView) findViewById(R.id.textView1); 
        btn1 = (Button) findViewById(R.id.button1);
        btn2= (Button)findViewById(R.id.button2);
        btn1.setAlpha(0.5f);
        //LinearLayout�Ķ��� xmlʵ��
        Animator Linanimator = AnimatorInflater.loadAnimator(Startactivity.this, R.anim.publicanimtions);
        Linanimator.setTarget(layout);
        Linanimator.start();
       
      //��textview�Ķ�������ʵ��
		tv.setScaleY(0f);
      tvanimations();

      //btn1����ʵ�֣���һ������ʵ��
//    btn1.setVisibility(View.GONE);
      btn1animations();
      btn1.setOnClickListener(new OnClickListener() {	
		@Override
		public void onClick(View v) {//������Ϸ
			Intent intent = new Intent(Startactivity.this, MainActivity.class);
		    startActivity(intent) ;
		    Startactivity.this.finish();
		    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);        
		}
	});
      //btn2�Ķ���ʵ�֣�����xml����
      btn2.setTranslationX(-500f);
      Animator animator = AnimatorInflater.loadAnimator(Startactivity.this, R.anim.publicanimtions);
      animator.setTarget(btn2);
      animator.setStartDelay(5000);
      animator.start();
      
      btn2.setOnClickListener(new OnClickListener() {//������ڰ���
		
		@Override
		public void onClick(View v1) {
			Intent intent = new Intent(Startactivity.this, gamehelpActivity.class);
		    startActivity(intent) ;
		    Startactivity.this.finish();
		    overridePendingTransition(R.anim.in_xyto_yx, R.anim.out_to_left);
		}
	});
	}
	//textview �Ķ�������
	@SuppressLint("NewApi") private void tvanimations() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(tv,"scaleY", 0f, 3f,1f);
		anim.setDuration(1500);
		anim.setStartDelay(5000);
		anim.setRepeatCount(3);
		//����repeatModeΪ����Ч�����ظ�ģʽ�����õ�ȡֵ���¡�RESTART�����´�ͷ��ʼִ�С�REVERSE��������ִ��
		anim.setRepeatMode(ValueAnimator.REVERSE);
		anim.start();
		//����������
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
	//btn1�Ķ�������
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
