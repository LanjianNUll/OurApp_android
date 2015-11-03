package com.example.myandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class WelcomeActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //È¥³ý±êÌâ
        setContentView(R.layout.welcomepage);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                    Intent mainIntent = new Intent(WelcomeActivity.this,
                    		Startactivity.class);
                    WelcomeActivity.this.startActivity(mainIntent);
                    WelcomeActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
    },1500);
	}
	
}
