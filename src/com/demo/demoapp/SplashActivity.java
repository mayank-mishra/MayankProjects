package com.demo.demoapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;

import com.demo.utils.AppUtils;
import com.google.gson.Gson;

public class SplashActivity extends ActionBarActivity{
	Gson gson = new Gson();
	ProgressBar bar;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		bar = (ProgressBar)findViewById(R.id.progressbar);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

					Intent i = new Intent(SplashActivity.this,LoginActivity1.class);
					i.putExtra(AppUtils.INTENT_KEY,	LoginActivity1.class.getName());
					startActivity(i);
					finish();
			}
		}, 3000);

	}
	
	

	
}
