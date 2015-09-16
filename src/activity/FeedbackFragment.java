package activity;


import model.BluetoothDeviceListItem;

import com.rondo.airrunning.R;

import components.BluetoothDevicesListAdapter;
import activity.HomeFragment.SlideMenuListener;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FeedbackFragment extends Fragment {

	
	private boolean current_status = false;
	
	private static final String BLUETOOTH_ON = "蓝牙已开启";
	private static final String BLUETOOTH_OFF = "蓝牙已关闭";
	private static final String BLUETOOTH_OPENING = "蓝牙正在打开";
	private static final String BLUETOOTH_CLOSING = "蓝牙正在关闭";
	private static final String BLUETOOTH_NOT_SUPPORT = "不支持蓝牙";
	
	private static final int START_SCANNING = 0;
	private static final int STOP_SCANNING = 1;
	private static final int RESULT_OK = 120;
	
	private ImageButton menu_btn,scan_btn;
	private ToggleButton bluetooth_enable;
	private TextView myDeviceName,myDeviceAddr;
	private TextView bluetooth_state;
	private ImageView scanning_icon;
	private ListView scanned_devices;
	
	private SlideMenuListener sListener;
	
	private BluetoothAdapter mAdapter;
	private BluetoothDevicesListAdapter deviceAdapter;
	
	private UIHandler mHandler;
	
	
	//This is a broadcast receiver which is used for listening
	//several actions send from the android bluetooth service
	//runs in background.
	//This is an asynchronized process, therefore we want to 
	//have a listener to handle these asynchronized events.
	private final BroadcastReceiver mReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			handleEvent(intent);
		}
		
	};

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mHandler = new UIHandler(Looper.myLooper());
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.layout_feedback, container, false);
		initViews(contentView);
		loadBondedDevices();
		setUpListeners();
		return contentView;
	}
	
	
	@Override
	public void onResume() {
		readBasicBluetoothInfos();
		getActivity().registerReceiver(mReceiver, getBroadcastFilter());
		super.onResume();
	}
	
	
	@Override
	public void onPause() {
		mAdapter.cancelDiscovery();
		getActivity().unregisterReceiver(mReceiver);
		super.onPause();
	}

	
	/**
	 * Load the widgets in this fragment
	 * @param contentView
	 */
	private void initViews(View contentView){
		menu_btn = (ImageButton) contentView.findViewById(R.id.feedback_btn_back);
		scan_btn = (ImageButton) contentView.findViewById(R.id.btn_scan);
		bluetooth_enable = (ToggleButton) contentView.findViewById(R.id.bluetooth_enable_toggle);
		myDeviceName = (TextView) contentView.findViewById(R.id.bluetooth_mydevice_name);
		myDeviceAddr = (TextView) contentView.findViewById(R.id.bluetooth_mydevice_address);
		bluetooth_state = (TextView) contentView.findViewById(R.id.bluetooth_enable_text);
		scanning_icon = (ImageView) contentView.findViewById(R.id.scanning_icon);
		scanned_devices = (ListView) contentView.findViewById(R.id.scanned_devices_list);
		deviceAdapter = new BluetoothDevicesListAdapter(getActivity());
		scanned_devices.setAdapter(deviceAdapter);
	}
	
	private void setUpListeners(){
		
		menu_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sListener.menuBtnClicked(SlideMenuListener.LEFT);
			}
		});
		
		bluetooth_enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				changeBluetoothStatus(isChecked);
			}
		});
		
		scan_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				scanBluetoothDevices();
			}
		});
		
		scanned_devices.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//As soon as we want to connect to a remote device, we stop the scan
				mAdapter.cancelDiscovery();
				
				BluetoothDeviceListItem _device = deviceAdapter.getDeviceSet().get(position);
				
				Intent intent = new Intent(getActivity(),DataTransactionActivity.class);
				intent.putExtra(DataTransactionActivity.DEVICE_NAME, _device.getDeviceName());
				intent.putExtra(DataTransactionActivity.DEVICE_ADDR, _device.getDeviceAddr());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
			
		});
		
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			sListener = (SlideMenuListener) activity;
		}catch(ClassCastException e){
			throw new ClassCastException("This activity must implements SlideMenuListener interface.");
		}
	}
	
	/**
	 * Set the style of the bluetooth state area.
	 * If the device doesn't support bluetooth then it's disabled
	 */
	private void setBluetoothStateStyle(){
		
		if(!isBluetoothSupported()){
			bluetooth_enable.setEnabled(false);
			bluetooth_state.setText(BLUETOOTH_NOT_SUPPORT);
		}else{
			if(isBluetoothEnabled()){
				bluetooth_enable.setChecked(true);
				bluetooth_state.setText(BLUETOOTH_ON);
			}else{
				bluetooth_enable.setChecked(false);
				bluetooth_state.setText(BLUETOOTH_OFF);
			}
		}
		
	}
	
	/**
	 * Check whether the current device supports bluetooth functionalities
	 * @return returns true if supported and false vice versa.
	 */
	private boolean isBluetoothSupported(){
		return mAdapter == null ? false : true;
	}
	
	/**
	 * Check whether the bluetooth is available if the device supports
	 * @return returns true if bluetooth is open and false vice versa.
	 */
	private boolean isBluetoothEnabled(){
		
		if(isBluetoothSupported()){
			current_status = mAdapter.isEnabled();
		}
		
		return current_status;
	}
	
	/**
	 * Activates the bluetooth
	 * @param enable -- true to open the bluetooth and false to turn off
	 */
	private void changeBluetoothStatus(boolean enable){
		
		if(enable){
			mAdapter.enable();
			current_status = true;
		}else{
			mAdapter.disable();
			current_status = false;
		}
		
	}
	
	/**
	 * If the device supports bluetooth, then read some basic information
	 * such as name and physical address from bluetooth adapter. Otherwise 
	 * display that the bluetooth service is unsupported by the device.
	 */
	private void readBasicBluetoothInfos(){
		
		setBluetoothStateStyle();
		
		if(isBluetoothSupported()){
			myDeviceName.setText(mAdapter.getName());
			myDeviceAddr.setText(mAdapter.getAddress());
		}else{
			myDeviceName.setText("未知设备");
			myDeviceAddr.setText("未知地址");
		}
	}
	
	
	/**
	 * Judge whether the device can be found by other bluetooth services
	 * @return -- true if the device can be found, false vice versa.
	 */
	private boolean isDeviceScannedEnabled(){
		
		int mode = mAdapter.getScanMode();
		
		if(mode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Start to scan available bluetooth devices
	 * @return boolean -- true if the initiation success, false vice versa.
	 */
	private boolean scanBluetoothDevices(){
		
		if(!isBluetoothSupported()){
			 Toast.makeText(getActivity(), "您的设备不支持蓝牙功能，无法进行搜索",Toast.LENGTH_SHORT).show();
			 return false;
		}
		
		if(isBluetoothEnabled()){
		   
		   if(isDeviceScannedEnabled()){	
		     Message msg = mHandler.obtainMessage(START_SCANNING);
		     mHandler.sendMessage(msg);
		     return mAdapter.startDiscovery();
		   }else{
			 Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			 discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
			 discoverable.putExtra("FRAGMENT_NUM",3);   //Make sure we come back to this fragment
			 startActivityForResult(discoverable, 1);
			 return false;
		   }
		   
		}
		else{
		  Toast.makeText(getActivity(), "您必须先打开蓝牙才可以进行蓝牙设备搜索",Toast.LENGTH_SHORT).show();
		  return false;
		}
		
	}
	
	/**
	 * Load the devices which have already bonded and display them in the listview.
	 */
	private void loadBondedDevices(){
		
//		BluetoothDevice[] bonded = new BluetoothDevice[10];
//		
//		//////
//		
//		if(bonded.length != 0){
//			for(int i = 0;i < bonded.length;i++){
//				BluetoothDeviceListItem item = new BluetoothDeviceListItem(bonded[i],bonded[i].getBluetoothClass());
//				this.deviceAdapter.addDevice(item);
//			}
//			this.deviceAdapter.notifyDataSetChanged();
//		}
		
	}
	
	
	
	/**
	 * Get the intent filters used for registering the broadcast receiver
	 * @return the intent filter object
	 */
	private IntentFilter getBroadcastFilter(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		return filter;
	}
	
	/**
	 * Handle the events send from the android bluetooth service
	 * @param intent -- Intent sends from the bluetooth service
	 * this intent contains different actions such as 
	 * BluetoothAdapter.ACTION_STATE_CHANGED
	 * BluetoothDevice.ACTION_FOUND
	 * and etc.
	 */
	private void handleEvent(Intent intent) {
		
		String action = intent.getAction();
		
		//If the state of the bluetooth service has changed
		if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
			
			int state = mAdapter.getState();
			
			if(state == BluetoothAdapter.STATE_TURNING_ON){
			    bluetooth_state.setText(BLUETOOTH_OPENING);
			    changeToggleButton();
			}
			
			else if(state == BluetoothAdapter.STATE_TURNING_OFF){
				bluetooth_state.setText(BLUETOOTH_CLOSING);
				changeToggleButton();
			}
			
			else if(state == BluetoothAdapter.STATE_OFF || state == BluetoothAdapter.STATE_ON){
				bluetooth_enable.setBackgroundResource(R.drawable.toggle_btn);
				bluetooth_enable.setChecked(current_status);
				bluetooth_enable.setEnabled(true);
				if(current_status){
					bluetooth_state.setText(BLUETOOTH_ON);
				}else{
					bluetooth_state.setText(BLUETOOTH_OFF);
				}
			}
	   }
		
		
	   if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
		    Message msg = mHandler.obtainMessage(STOP_SCANNING);
		    mHandler.sendMessage(msg);
	   }
		
	   //If the bluetooth service finds a new available device
	   //We get the BluetoothDevice object from the intent
	   //sends from the bluetooth service, this intent contains
	   //two attributes: BluetoothDevice.EXTRA_DEVICE  &
	   //BluetoothDevice.EXTRA_CLASS (BluetoothClass object)
	   if(action.equals(BluetoothDevice.ACTION_FOUND)){
		   Log.i("SCAN", "DEVICE_FOUND");
		   BluetoothDevice newDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		   BluetoothClass deviceClass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
		   Log.e("DEVICE_TYPE", String.valueOf(deviceClass.getDeviceClass())); //Prints out the integers
		   this.deviceAdapter.addDevice(new BluetoothDeviceListItem(newDevice,deviceClass));
		   this.deviceAdapter.notifyDataSetChanged();
	   }
		
	}
	
	/**
	 * This method is called when the state of the bluetooth adapter is currently changing.
	 * We change the image of the button and disable the toggle button during the changing
	 * phase.
	 */
	private void changeToggleButton(){
		bluetooth_enable.setBackgroundResource(R.drawable.icon_bluetooth_state_changing);
		bluetooth_enable.setEnabled(false);
	}
	
	
	/**
	 * Private handler class used for handling time consuming UI interactions
	 * @author ALIENWARE
	 *
	 */
    class UIHandler extends Handler{
    	
    	public UIHandler(Looper looper){
    		super(looper);
    	}
    	
    	@Override
    	public void handleMessage(Message msg){
    		
    		switch(msg.what){
    		
    		  case START_SCANNING :
    			 scanning_icon.setVisibility(View.VISIBLE);
    			 scanning_icon.startAnimation(AnimationUtils.loadAnimation
    					 (getActivity(), R.anim.scanning_icon_rotate_anim));
    			 break;
    			 
    		  case STOP_SCANNING :
    			 scanning_icon.clearAnimation();
    			 scanning_icon.setVisibility(View.GONE);
    			 break;
    			 
    		}
    		
    	}
    	
    }
    
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("RESULT_CODE",String.valueOf(resultCode));
		if(resultCode == RESULT_OK){
		  mAdapter.startDiscovery();
		}else{
		  Toast.makeText(getActivity(), "您必须打开设备的可发现性才可以进行设备配对", Toast.LENGTH_SHORT).show();
		}
	}


    

}
