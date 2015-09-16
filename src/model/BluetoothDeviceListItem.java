package model;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

public class BluetoothDeviceListItem {
	
	private BluetoothDevice device;
	
	private BluetoothClass clz;
	
	
	/**
	 * Create with a given BluetoothDevice object if no 
	 * corresponding BluetoothClass object available
	 * @param device
	 */
	public BluetoothDeviceListItem(BluetoothDevice device){
		this.device = device;
	}
	
	
	/**
	 * Create with a given BluetoothDevice object and a BluetoothClass object
	 * @param device
	 * @param clz
	 */
	public BluetoothDeviceListItem(BluetoothDevice device,BluetoothClass clz){
		this.device = device;
		this.clz = clz;
	}
	
	
	/**
	 * Returns the device name of scanned device, if the name is unavailable
	 * then returns "Unknown device"
	 * @return device name
	 */
	public String getDeviceName(){
		if(device == null){
			return "未知设备";
		}else{
			return device.getName();
		}
	}
	
	
	/**
	 * Returns the mac address of scanned device, if the address is unavailable
	 * then returns "Unknown address"
	 * @return device mac address
	 */
	public String getDeviceAddr(){
		if(device == null){
			return "未知地址";
		}else{
			return device.getAddress();
		}
	}
	
	/**
	 * Returns the bond state between the your device and the scanned device.
	 * The result integers are listed in BluetoothDevice class
	 * Returns BluetoothDevice.BONE_NONE if the scanned device is unavailable.
	 * @return
	 */
	public int getBondState(){
		if(device == null){
			return BluetoothDevice.BOND_NONE;
		}else{
			return device.getBondState();
		}
	}
	
	
	/**
	 * Returns the class type of the device.
	 * The result integers are listed in BluetoothClass.Device
	 * Returns -1 if the type integer is unavailable.
	 * @return
	 */
	public int getDeviceType(){
		if(clz == null){
			return -1;
		}else{
			return clz.getDeviceClass();
		}
	}
	
	/**
	 * Get the bluetooth device object associated with this class
	 * @return
	 */
	public BluetoothDevice getDevice(){
		return this.device;
	}
	
	
	/**
	 * Get the bluetooth class object associated with this class
	 * @return
	 */
	public BluetoothClass getDeviceClass(){
		return this.clz;
	}
	
	
	/**
	 * Judge whether the parameter object equals to the current object
	 * The method only returns true if two object has a same address
	 * attribute.
	 */
	@Override
	public boolean equals(Object other){
		
		if(!(other instanceof BluetoothDeviceListItem)){
			return false;
		}
		
		BluetoothDeviceListItem temp = (BluetoothDeviceListItem) other; 
		
		if(temp.getDeviceAddr().equals(this.getDeviceAddr())){
			return true;
		}
		
		return false;
		
	}

}
