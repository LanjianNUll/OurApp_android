package com.example.myandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class gamehelpActivity extends Activity {
	Button btn1;
	Button btn2;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //去除标题
        setContentView(R.layout.gamehelp);
      btn1 = (Button) findViewById(R.id.button1);
      btn1.setOnClickListener(new OnClickListener() {	
		@Override
		public void onClick(View v) {//返回Startactivity页面
			Intent intent = new Intent(gamehelpActivity.this, Startactivity.class);
		    startActivity(intent) ;
		    gamehelpActivity.this.finish();
		    overridePendingTransition(R.anim.out_to_bottom, R.anim.in_from_top);
		}
	});
      btn2= (Button)findViewById(R.id.button2);
      btn2.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v1) {//难度按钮
			Toast.makeText(gamehelpActivity.this, "天才，地狱级的难度还在开发中，请稍候....", Toast.LENGTH_LONG).show(); 
			
		}
	});
	}

}
