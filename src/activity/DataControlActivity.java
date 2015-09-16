package activity;

import com.rondo.airrunning.R;

import service.BluetoothService;
import service.BluetoothService.LocalBinder;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class DataControlActivity extends Activity {
	
	private static final String TAG = "DataControlActivity";
	
	
	private TextView name,addr;
	private TextView display;
	private ImageButton btn_back;
	
	
	private BluetoothService mService;
	private BluetoothDevice device;
	
	
	
	/**
	 * This service connection object manages the lifecycle of the binded service
	 */
	private final ServiceConnection connection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			 
			 mService = ((LocalBinder) service).getServiceIntance();
			 
			 if(!mService.initService()){
				 Log.e(TAG, "Failed to initialize the bluetooth service");
			 }
			 
			 Log.i(TAG, "Service Initalized");
			 mService.connect(device);
			 mService.beginTransaction();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			
			Log.e(TAG, "Service disconnected");
			
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_data);
		initViews();
		setUpListeners();
		readExtras(getIntent());
	}
	
	/**
	 * Initiates the UI widgets
	 */
	private void initViews(){
		name = (TextView) findViewById(R.id.data_device_name);
		addr = (TextView) findViewById(R.id.data_device_addr);
		display = (TextView) findViewById(R.id.data_display);
		btn_back = (ImageButton) findViewById(R.id.data_btn_back);
	}
	
	
	
	private void setUpListeners(){
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DataControlActivity.this,MainActivity.class);
				intent.putExtra("FRAGMENT_NUM", 3);
				startActivity(intent);
				overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
			}
		});
	}
	
	
	/**
	 * Read the intent extras send from the entry activity
	 * @param intent
	 */
	private void readExtras(Intent intent){
		
		device = intent.getParcelableExtra(BluetoothService.DEVICE);
		String device_name = device.getName();
		String device_addr = device.getAddress();
		
		name.setText(device_name);
		addr.setText(device_addr);
	}

	@Override
	protected void onResume() {
		registerService();
		super.onResume();
	}

	@Override
	protected void onPause() {
		unRegisterService();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	/**
	 * Register a service to this activity
	 */
	private void registerService(){
		Intent bindService = new Intent(this.getApplicationContext(),BluetoothService.class);
		this.bindService(bindService, connection, Context.BIND_AUTO_CREATE);
		this.registerReceiver(mReceiver, getIntentFilters());
	}
	
	/**
	 * Unregister the service that is previously registered in this activity.
	 * This method should be called when the activity is brought to background 
	 * in order to avoid memory leaks.
	 */
	private void unRegisterService(){
		this.unbindService(connection);
		this.unregisterReceiver(mReceiver);
	}
	
	
	final BroadcastReceiver mReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			
			if(action.equals(BluetoothService.READ_DATA)){
				String data = intent.getStringExtra(BluetoothService.EXTRA_DATA);
			    display.setText(data);
			}
			
		}
		
	};
	
	
	/**
	 * Get the intent filter for the broadcast receiver in this class
	 * @return
	 */
	private IntentFilter getIntentFilters(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothService.READ_DATA);
		return filter;
	}
	

}
