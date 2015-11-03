package com.example.myandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class answerActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE); //È¥³ý±êÌâ
	    setContentView(R.layout.answerright);
	    new Handler().postDelayed(new Runnable() {
            public void run() {
                    Intent mainIntent = new Intent(answerActivity.this,
                                    MainActivity.class);
                    answerActivity.this.startActivity(mainIntent);
                    answerActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
    }, 5000);
	}
}
