package com.weight.watchers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class RunActivity extends Activity{
private static final int SPLASH_DELAY =  3000;
	
	private TextView timerView;
	private TextView repsView;
	private Button stopButton;
	
	private int reps;
	private long timerLength;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run);
		
		//References
		timerView = (TextView)findViewById(R.id.runTimerTextView);
		repsView = (TextView)findViewById(R.id.runRepsTextView);
		
		//Reference to submit button
		stopButton = (Button) findViewById(R.id.runStopExerciseButton);
		stopButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				//Grab intent data 
				Intent temp = RunActivity.this.getIntent();
				String exercise = temp.getStringExtra("exercise");
				int weight = temp.getIntExtra("weight", -1);
				
				//Move to next activity
				Intent i = new Intent(RunActivity.this, RunActivity.class);
				i.putExtra("exercise", exercise);
				i.putExtra("weight", weight);
				i.putExtra("reps", reps);
				i.putExtra("time", timerLength);
				startActivity(i);
				
			}
			
		});
		
	}

}
