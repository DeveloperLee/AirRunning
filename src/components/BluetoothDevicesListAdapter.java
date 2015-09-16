package components;

import java.util.ArrayList;
import java.util.List;

import util.ImagePicker;

import model.BluetoothDeviceListItem;

import com.rondo.airrunning.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BluetoothDevicesListAdapter extends BaseAdapter {
	
	//List of scanned bluetooth devices plus bluetooth classes
	private List<BluetoothDeviceListItem> devices;
	
	private final LayoutInflater inflater;
	
	
	/**
	 * Constructor of this class
	 * @param context -- context object for the initialization
	 * of the layout inflater
	 */
	public BluetoothDevicesListAdapter(final Context context){
		devices = new ArrayList<BluetoothDeviceListItem>();
		this.inflater = LayoutInflater.from(context);
	}
	

	@Override
	public int getCount() {
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_device, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.device_list_icon);
			holder.name = (TextView) convertView.findViewById(R.id.device_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		BluetoothDeviceListItem device = devices.get(position);
		
		holder.icon.setBackgroundResource(ImagePicker.getInstance().getDeviceIcon(device.getDeviceType()));
		holder.name.setText(device.getDeviceName());
		
		return convertView;
	}
	
	/**
	 * Add a new scanned bluetooth device into the list,
	 * the addition would be ignored if the current list
	 * already contains the device.
	 * @param newDevice -- object of newly found device
	 */
	public void addDevice(BluetoothDeviceListItem newDevice){
		if(!this.devices.contains(newDevice)){
			this.devices.add(newDevice);
		}
	}
	
	
	/**
	 * Get a list of scanned devices 
	 * @return
	 */
	public List<BluetoothDeviceListItem> getDeviceSet(){
		return this.devices;
	}
	
	
	 class ViewHolder{
		ImageView icon;
		TextView name;
	}

}
