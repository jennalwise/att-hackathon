package com.weight.watchers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RunActivity extends Activity{
private static final int SPLASH_DELAY =  3000;
	
	private TextView timerView;
	private TextView repsView;
	private Button stopButton;
	
	private int reps;
	private long timerLength;
	
	private ArrayList<JSONObject> oldData = new ArrayList<JSONObject>();

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
				
				//Save data to json file
				File jsonFile = new File(RunActivity.this.getFilesDir(), exercise + ".json");
				    if (jsonFile.exists()) {
				        //parse file to read in data
				    	try {
				    		JsonReader reader = new JsonReader(new FileReader(jsonFile));
				        
							reader.beginObject();
							while (reader.hasNext()) {
								String name = reader.nextName();
						
								if (name.equals("response")) {
									reader.beginObject();
									JSONObject newObject = new JSONObject();
									while(reader.hasNext()) {
										String name2 = reader.nextName();
										if (name2.equals("exercise")) {
											newObject.put(name2, reader.nextString());
										} else if (name2.equals("weight")) {
											newObject.put(name2, reader.nextInt());
										} else if (name2.equals("reps")) {
											newObject.put(name2, reader.nextInt());
										} else if (name2.equals("time")) {
											newObject.put(name2, reader.nextLong());
										} else {
											reader.skipValue();
										}
									}
									oldData.add(newObject);
									reader.endObject();
								} else {
									reader.skipValue();
								}
							}
							reader.endObject();
							reader.close();
						} catch (IOException e) {
							System.out.println(e.getMessage());
						} catch (JSONException e) {
							System.out.println(e.getMessage());
						}
				    	
				    	//overwrite the file with old data and new data
				    	try {
				    		JsonWriter writer = new JsonWriter(new FileWriter(jsonFile));
				    		writer.beginObject();
				    			for (int i = 0; i < oldData.size(); i++) {	
				    				JSONObject object = oldData.get(i);
				    				writer.name("response")
				    						.beginObject()
				    							.name("exercise")
				    							.value(object.getString("exercise"))
				    							.name("weight")
				    							.value(object.getInt("weight"))
				    							.name("reps")
				    							.value(object.getInt("reps"))
				    							.name("time")
				    							.value(object.getLong("time"))
				    						.endObject();
				    			}
				    		 writer.endObject();
					    	writer.close();
				    	} catch (IOException e) {
				    		System.out.println(e.getMessage());
				    	} catch (JSONException e) {
				    		System.out.println(e.getMessage());
				    	}
				    } else {
				    	//write out to new file
				    	try {
				    		JsonWriter writer = new JsonWriter(new FileWriter(jsonFile));
				    		writer.beginObject()
				    				.name("response")
				    				.beginObject()
				    					.name("exercise")
				    					.value(exercise)
				    					.name("weight")
				    					.value(weight)
				    					.name("reps")
				    					.value(reps)
				    					.name("time")
				    					.value(timerLength)
				    				.endObject()
				    			  .endObject();
				    		writer.close();
				    					
				    	} catch (IOException e) {
				    		System.out.println(e.getMessage());
				    	}		
				    }	
				    
				//Move to next activity
				Intent i = new Intent(RunActivity.this, RunActivity.class);
				startActivity(i);
				
			}
			
		});
		
	}

}
