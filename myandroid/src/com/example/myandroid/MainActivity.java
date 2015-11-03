package com.example.myandroid;

import java.util.Random;

import com.example.myandroid.MainActivity;

import android.os.Build;
import android.os.Bundle;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class MainActivity extends Activity {
	
	 Button confirm;
	 Button restart;
	 Button answer;
	 Button exit;
	 LinearLayout llGroup ;
	 AutoCompleteTextView number;
	 static String ranstr=getRandom(); 
	 int i=0;
	 @SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.hahh);
		 confirm=(Button) findViewById(R.id.button1);
		 restart=(Button) findViewById(R.id.button2);
		 answer=(Button) findViewById(R.id.button3);
		 exit=(Button) findViewById(R.id.button4);
		 
		 float confirmx = confirm.getTranslationX();
		 float confirmy = confirm.getTranslationY();
		 float restartx = restart.getScaleY();
		 float restarty = restart.getScaleY();
		 float answerx = answer.getScaleX();
		 float answery = answer.getScaleY();
		 float exitx = exit.getScaleX();
		 float exity = exit.getScaleY();
		 
		 confirm.setTranslationX(-5000f);
		 confirm.setTranslationY(500f);
		 restart.setScaleX(5f);
		 restart.setScaleY(5f);
		 answer.setScaleX(5f);
		 answer.setScaleY(5f);
		 exit.setScaleX(5f);
		 exit.setScaleY(5f);
		 
		 ObjectAnimator confirmtransx = ObjectAnimator.ofFloat(confirm, "translationX", -500f, confirmx);
		 ObjectAnimator confirmtransy = ObjectAnimator.ofFloat(confirm, "translationY", 500f, confirmy);
		 ObjectAnimator restartanimy = ObjectAnimator.ofFloat(restart, "scaleY", 5f,restartx);
		 ObjectAnimator restartanimx = ObjectAnimator.ofFloat(restart, "scaleX", 5f,restarty);
		 ObjectAnimator answeranimx =ObjectAnimator.ofFloat(answer,"scaleX",5f,answerx);
		 ObjectAnimator answeranimy =ObjectAnimator.ofFloat(answer,"scaleY",5f,answery);
		 ObjectAnimator exitanimx =ObjectAnimator.ofFloat(exit,"scaleX",5f,exitx);
		 ObjectAnimator exitanimy =ObjectAnimator.ofFloat(exit,"scaleY",5f,exity);

		 
		 AnimatorSet animSet = new AnimatorSet();
		 animSet.play(confirmtransx).with(confirmtransy)
		 .after(restartanimy).with(restartanimx)
		 .after(answeranimx).with(answeranimy)
		 .after(exitanimx).with(exitanimy);
		 animSet.setDuration(2500);
		 animSet.start();
		 
		 
		 
		 llGroup= (LinearLayout) findViewById(R.id.lineralayout);
		 number=(AutoCompleteTextView) findViewById(R.id.editText1);
	 
        TextView tv = new TextView(MainActivity.this);
        tv.setText("电脑已经想好了，你可以开始了,猜中有丰厚奖励哦，你懂的。。。嘿嘿");
        llGroup.addView(tv);
        
        confirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v1) {//确定按钮
		        String num=number.getText().toString();
		        if(i<7){
		        	if(isOk(num)){
		        	i++; 
		        	TextView tv = new TextView(MainActivity.this);
			    	number.setText("");
					String result=test(num,ranstr);
						//tv.setTextColor(22);
						tv.setText("    第"+i+"次提示:  "+num+"   ----"+result);
						// 把textview对象添加到linearlayout中
						llGroup.addView(tv);
				    	if(num.equals(ranstr)){
			    			Toast.makeText(MainActivity.this, "恭喜你，你猜对了", Toast.LENGTH_LONG).show(); 
	                        Intent mainIntent = new Intent(MainActivity.this,
	                        		answerActivity.class);
	                        MainActivity.this.startActivity(mainIntent);
	                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
	                        ranstr=getRandom();
		           
				    	}else
				        	 Toast.makeText(MainActivity.this, "不对，你可以参考下提示"+ranstr, Toast.LENGTH_LONG).show();
						}
			        else{
					  Toast.makeText(MainActivity.this, "你够了，请大哥输入正确的数字  OK?", Toast.LENGTH_LONG).show(); 
					  number.setText("");
			        }
		        	}
		        else
		        {
				  Toast.makeText(MainActivity.this, "承认吧骚年，你智商不够", Toast.LENGTH_LONG).show(); 
				  llGroup.removeAllViews();
				  TextView tv = new TextView(MainActivity.this);
		          tv.setText("唉，天才和你差距还很大，不过天才都是历经磨难的，只要有信心，人永远被战败！重新开始吧！COME ON");
				  llGroup.addView(tv);  
		        }
		    }   
			private boolean isOk(String num) {
				if(num.length()!=4)
					return false;
				else{
					int[] a=new int[10];
					int count=0;
					a[(int)num.charAt(0)-48]=1;
					a[(int)num.charAt(1)-48]=1;
					a[(int)num.charAt(2)-48]=1;
					a[(int)num.charAt(3)-48]=1;
					for(int i=0;i<a.length;i++)
						if(a[i]==1)
						count++;
					if(count==4)
						return true;
					else 
						return false;
				}
			}    	
			}); 
        
        restart.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {//重新开始
                ranstr=getRandom();
				i=0;
		    	llGroup.removeAllViews();
		    	TextView tv = new TextView(MainActivity.this);
		        tv.setText("电脑已经想好了，你可以开始了,猜中有丰厚奖励哦，你懂的。。。嘿嘿");
		        llGroup.addView(tv);
			}
		});
        answer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v3) {//公布答案
				llGroup.removeAllViews();
				TextView tv = new TextView(MainActivity.this);
				tv.setText("  答案：------ "+ranstr+"----");
				llGroup.addView(tv);
				ranstr=getRandom();
			}
		});
        exit.setOnClickListener(new OnClickListener() {//退出游戏
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, Startactivity.class);
			    startActivity(intent) ;
			    MainActivity.this.finish();
			    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}
		});
	 }
	private static String getRandom() {
		
		int a[]={1,2,3,4,5,6,7,8,9,0};
		int b[]=new int[10];
		int ss[]=new int[4];
		int r=new Random(1).nextInt(10);
		for(int i=0;i<4;i++){
			r=new Random().nextInt(10);
			if(b[r]==0){
				b[r]=-1;
				ss[i]=a[r];	
			}
			else i--;
		}
		String u=""+ss[0]+ss[1]+ss[2]+ss[3];
		return u;
 }
		private String test(String num,String ranstr) {
			int a=0;
			int b=0;
			System.out.println(ranstr);
			for(int i=0;i<num.length();i++)
				for(int j=0;j<ranstr.length();j++)
				{
					if(num.charAt(i)==ranstr.charAt(j)){
						if(i==j)
							a++;
						b++;	
					}
				}
			String resultstr=b+"A"+a+"B";		
			return resultstr;
			
		}
}

