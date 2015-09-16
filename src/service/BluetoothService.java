package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;

public class BluetoothService extends Service {
	
	private static final String TAG = "BluetoothService";
	
	
	private static final int CONNECTED  = 1;
	private static final int DISCONNECTED  = -1;
	private static final int CONNECTING = 0;
	private static int current_status = DISCONNECTED;
	
	public static final String DEVICE_NAME = "device_name";
	public static final String DEVICE_ADDR = "device_addr";
	public static final String DEVICE = "device";
	public static final String READ_DATA = "read_data";
	public static final String EXTRA_DATA = "extra_data";
	
	private static final UUID MX = UUID.fromString("99666375-CF64-5E49-D403-8EF2619CFAB6");
	
	private BluetoothAdapter mAdapter;
	private BluetoothDevice remote_server;
	private BluetoothSocket mSocket;
	
	private String current_address;
    
	/**
	 * This method will be invoked when the activity
	 * calls bindService
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return new LocalBinder();
	}
	
	
	@Override 
	public boolean onUnbind(Intent intent){
		closeService();
		return super.onUnbind(intent);
	}
	
	
	/**
	 * Initiates the bluetooth data transmission service
	 * @return -- true if the serivce is successfully initiated 
	 *            and false vice versa.
	 */
	public boolean initService(){
		
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if(mAdapter == null){
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Build up a connection between the application and the target device
	 * @return
	 */
	public boolean connect(BluetoothDevice target){
		
		if(target == null || mAdapter == null){
			Log.e(TAG, "Target not specified or the bluetooth adapter is not successfully initiated");
			return false;
		}
		
		//If the current address equals to the target device address and the 
		//current socket is available, it means that we're trying to reconnect
		//to a target device.
		if(current_address != null && current_address.equals(target.getAddress())
				&& mSocket != null){
			Log.i(TAG, "Trying to reconnect to device : "+current_address);
			
			try {
				mSocket.connect();
				current_status = CONNECTING;
				return true;
			} catch (IOException e) {
				Log.e(TAG, "Reconnection failed, Socket closed");
				current_status = DISCONNECTED;
				return false;
			}
			
		}
		
		//If we open a new connection, then we creates a new bluetooth socket using the UUID
		try {
			
		    //UUID is retrieved from the bluetooth device object
			//Each bluetooth device wraps a list of UUID that can be used
			//for matching.
			ParcelUuid[] uuids = target.getUuids();
//			Log.i(TAG, "Supported UUID number : "+String.valueOf(uuids.length));
			mSocket = target.createRfcommSocketToServiceRecord(MX);
		} catch (IOException e) {
			Log.e(TAG, "Failed to create the connection");
			return false;
		}
		
		if(mSocket == null){
			Log.e(TAG, "Device not found or not within the range.");
			return false;
		}
		
		//If we reach here, we have got the bluetooth socket successfully.
		//However, the connection hasn't been set up
		
		try {
			mSocket.connect();
			current_address = target.getAddress();
			current_status = CONNECTING;
			Log.i(TAG, "Trying to create a new connection to device : "+target.getAddress());
			return true;
		} catch (IOException e) {
			Log.e(TAG, "Failed to create a new connection to device : "+target.getAddress());
			return false;
		}
		
	}
	
	
	/**
	 * Close the current running connection
	 */
	public void disconnect(){
		if(mAdapter == null || mSocket == null){
			Log.e(TAG, "There is no connection");
		}
		
		try {
			mSocket.close();
			current_status  = DISCONNECTED;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method ensures that when the service is unbinded, resources are 
	 * released properly.
	 */
	public void closeService(){
		if(mSocket == null){
			return ;
		}
		
		try {
			mSocket.close();
			mSocket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Send a broadcast to the activity
	 * @param action
	 * @param data
	 */
	public void broadcast(final String action,String data){
		Intent intent = new Intent(action);
		intent.putExtra(EXTRA_DATA, data);
		sendBroadcast(intent);
	}
	
	
	/**
	 * Begin the data transmission transaction between the client and server
	 */
	public void beginTransaction(){
		DataThread transaction = new DataThread(mSocket);
//		transaction.write("");
		transaction.start();
	}
	
	
	
	/**
	 * Get a binder class object of this service
	 * @author Developer-Lee
	 *
	 */
	public class LocalBinder extends Binder{
		public BluetoothService getServiceIntance(){
			return BluetoothService.this;
		}
	}
	
	
	
	/**
	 * Class for handling the data transmission after the connection is set up.
	 */
	private class DataThread extends Thread{
		
		//This bluetooth socket object is referenced the 
		//one inside the bluetooth service after successfully
		//build a connection.
		private final BluetoothSocket mSocket;
		private InputStream in;
		private OutputStream out;
		
		public DataThread(final BluetoothSocket mSocket){
			
			this.mSocket = mSocket;
			
			try {
				
				out = mSocket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run(){
			read();
		}
		
		/**
		 * Infinite loop for listening the incoming data.
		 */
		public void read(){
			
			byte[] data = new byte[1024];
			int bytes;
			
//			while(true){
				try {
					in = mSocket.getInputStream();
				    if( (bytes = in.read(data)) >0){
				    	byte[] buffer = new byte[bytes];
				    	for(int i = 0; i<bytes;i++){
				    		buffer[i] = data[i];
				    	}
				    	String result = new String(buffer,"UTF-8");
				    	broadcast(READ_DATA,result);
				    }
				} catch (IOException e) {
					e.printStackTrace();
//					Log.e(TAG, "IO Exception during reading the data");
				}
				
//			}
		}
		
	}

}
