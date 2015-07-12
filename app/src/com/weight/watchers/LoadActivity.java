package com.weight.watchers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadActivity extends Activity {
		
	private static final int SPLASH_DELAY = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
			
		//pause on loading screen
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable(){
			
			@Override
			public void run() {
				//Move to next activity
				Intent i = new Intent(LoadActivity.this,MainActivity.class);
				startActivity(i);
			}
		}, SPLASH_DELAY);		
	}
	
	

//		@Override
//		public boolean onCreateOptionsMenu(Menu menu) {
//			// Inflate the menu; this adds items to the action bar if it is present.
//			getMenuInflater().inflate(R.menu.main, menu);
//			return true;
//		}
	//
//		@Override
//		public boolean onOptionsItemSelected(MenuItem item) {
//			// Handle action bar item clicks here. The action bar will
//			// automatically handle clicks on the Home/Up button, so long
//			// as you specify a parent activity in AndroidManifest.xml.
//			int id = item.getItemId();
//			if (id == R.id.action_settings) {
//				return true;
//			}
//			return super.onOptionsItemSelected(item);
//		}
}
