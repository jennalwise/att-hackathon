package com.weight.watchers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class RunActivity extends Activity{
	private static final int SPLASH_DELAY =  3000;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 2;
	
	/**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;


    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;
	
	private TextView timerView;
	private TextView repsView;
	private Button stopButton;
	
	private int reps;
	private long timerLength;
	
	private ArrayList<JSONObject> oldData = new ArrayList<JSONObject>();
	BluetoothAdapter mBluetoothAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run);
		
		// Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);
        
        /* BT */
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			RunActivity.this.onBluetoothEnable();
		}
		
		//References
		timerView = (TextView)findViewById(R.id.runTimerTextView);
		repsView = (TextView)findViewById(R.id.runNumRepsTextView);
		
		//Reference to submit button
		stopButton = (Button) findViewById(R.id.runStopExerciseButton);
		stopButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				RunActivity.this.repsCount = 0;
				RunActivity.this.repsView.setText("0");
//				//Grab intent data 
//				Intent temp = RunActivity.this.getIntent();
//				String exercise = temp.getStringExtra("exercise");
//				int weight = temp.getIntExtra("weight", -1);
//				
//				//Set loading screen
//				LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout1);
//				ll.setVisibility(View.GONE);
//				
//				
//				
//				//Save data to json file
//				File jsonFile = new File(RunActivity.this.getFilesDir(), exercise + ".json");
//				    if (jsonFile.exists()) {
//				        //parse file to read in data
//				    	try {
//				    		JsonReader reader = new JsonReader(new FileReader(jsonFile));
//				        
//							reader.beginObject();
//							while (reader.hasNext()) {
//								String name = reader.nextName();
//						
//								if (name.equals("response")) {
//									reader.beginObject();
//									JSONObject newObject = new JSONObject();
//									while(reader.hasNext()) {
//										String name2 = reader.nextName();
//										if (name2.equals("exercise")) {
//											newObject.put(name2, reader.nextString());
//										} else if (name2.equals("weight")) {
//											newObject.put(name2, reader.nextInt());
//										} else if (name2.equals("reps")) {
//											newObject.put(name2, reader.nextInt());
//										} else if (name2.equals("time")) {
//											newObject.put(name2, reader.nextLong());
//										} else {
//											reader.skipValue();
//										}
//									}
//									oldData.add(newObject);
//									reader.endObject();
//								} else {
//									reader.skipValue();
//								}
//							}
//							reader.endObject();
//							reader.close();
//						} catch (IOException e) {
//							System.out.println(e.getMessage());
//						} catch (JSONException e) {
//							System.out.println(e.getMessage());
//						}
//				    	
//				    	//overwrite the file with old data and new data
//				    	try {
//				    		JsonWriter writer = new JsonWriter(new FileWriter(jsonFile));
//				    		writer.beginObject();
//				    			for (int i = 0; i < oldData.size(); i++) {	
//				    				JSONObject object = oldData.get(i);
//				    				writer.name("response")
//				    						.beginObject()
//				    							.name("exercise")
//				    							.value(object.getString("exercise"))
//				    							.name("weight")
//				    							.value(object.getInt("weight"))
//				    							.name("reps")
//				    							.value(object.getInt("reps"))
//				    							.name("time")
//				    							.value(object.getLong("time"))
//				    						.endObject();
//				    			}
//				    		 writer.endObject();
//					    	writer.close();
//				    	} catch (IOException e) {
//				    		System.out.println(e.getMessage());
//				    	} catch (JSONException e) {
//				    		System.out.println(e.getMessage());
//				    	}
//				    } else {
//				    	//write out to new file
//				    	try {
//				    		JsonWriter writer = new JsonWriter(new FileWriter(jsonFile));
//				    		writer.beginObject()
//				    				.name("response")
//				    				.beginObject()
//				    					.name("exercise")
//				    					.value(exercise)
//				    					.name("weight")
//				    					.value(weight)
//				    					.name("reps")
//				    					.value(reps)
//				    					.name("time")
//				    					.value(timerLength)
//				    				.endObject()
//				    			  .endObject();
//				    		writer.close();
//				    					
//				    	} catch (IOException e) {
//				    		System.out.println(e.getMessage());
//				    	}		
//				    }	
//				    
//				
//				//Move to next activity
//				Intent i = new Intent(RunActivity.this, RunActivity.class);
//				startActivity(i);
//				
			}
			
		});
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == this.RESULT_OK){
			switch(requestCode){
			case REQUEST_ENABLE_BT:
				RunActivity.this.onBluetoothEnable();
				break;
			case REQUEST_CONNECT_DEVICE_SECURE:
				connectDevice(data, true);
				break;
			}			
		}

	}
	
	/**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }
	
	//Once bluetooth is enabled
	private void onBluetoothEnable(){
		
		// Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
		
//		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//		// If there are paired devices
//		if (pairedDevices.size() > 0) {
//		    // Loop through paired devices
//		    for (BluetoothDevice device : pairedDevices) {
//		    	device.get
//		        // Add the name and address to an array adapter to show in a ListView
//		        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//		    }
//		}
	}
	
	/**
     * The Handler that gets information back from the BluetoothChatService
     */
	int repsCount = 0;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = RunActivity.this;
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            
                            
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if(readMessage.contains("BT")){
                    	RunActivity.this.sendMessage("1\n");
                    	try {
							Thread.sleep(267);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	RunActivity.this.sendMessage("1\n");
                    	try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	RunActivity.this.sendMessage("2\n");
                    	try {
							Thread.sleep(321);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	RunActivity.this.sendMessage("4\n");
                    } else {
                    	repsCount++;
                    	repsView.setText(String.valueOf(repsCount));
                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
	
    
    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "NOT CONNECTED!", Toast.LENGTH_SHORT).show();
            return;
        } else Toast.makeText(this, "msg sent!", Toast.LENGTH_SHORT).show();

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }
	

}
