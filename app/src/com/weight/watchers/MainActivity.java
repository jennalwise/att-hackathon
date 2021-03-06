package com.weight.watchers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private EditText exercise;
	private EditText weight;
	private Button submit;
	
	private String exerciseText;
	private int weightText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//References to edittexts
		exercise = (EditText) findViewById(R.id.mainExerciseEditText);
		weight = (EditText) findViewById(R.id.mainWeightEditText);
	
		
		//Reference to submit button
		submit = (Button) findViewById(R.id.mainStartButton);
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//Save information
				exerciseText = exercise.getText().toString();
				weightText = Integer.parseInt(weight.getText().toString());
				
				//Move to next activity
				Intent i = new Intent(MainActivity.this, RunActivity.class);
				i.putExtra("exercise", exerciseText);
				i.putExtra("weight", weightText);
				startActivity(i);
				
			}
			
		});
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
