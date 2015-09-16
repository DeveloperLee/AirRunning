package util;

import java.util.HashMap;
import java.util.Map;

import model.AbsUser;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass;

import com.rondo.airrunning.R;

@SuppressLint("UseSparseArrays")
public class ImagePicker {
	
	private Map<String,Integer> weather_ic_map;
	private Map<String,Integer> number_map;
	private Map<Integer,Integer> device_class_map;
	private Map<String,Integer> platform_bg_map;
	
	//The sole instance of ImagePicker class
	private static ImagePicker instance = null ; 
    
	
	public static ImagePicker getInstance(){
		if(instance == null){
			instance = new ImagePicker();
			return instance;
		}else{
			return instance;
		}
	}
	
	public int getWeatherIconResId(String weather){
		if(weather.equals("����") || weather.equals("��")){
			return weather_ic_map.get(weather);
		}else if(weather.contains("��")){
			return weather_ic_map.get("��");
		}else if(weather.contains("��")){
			return weather_ic_map.get("��");
		}else if(weather.contains("ѩ")){
			return weather_ic_map.get("ѩ");
		}else{
			return R.drawable.dust;
		}
	}
	
	/**
	 * Get the resource id of the pm level picture according to the parameter 
	 * @param pm 
	 * @return
	 */
	public int getPMLevelResId(String pm){
		int index = Integer.parseInt(pm);
		if(index>0 && index<=50){
			return R.drawable.notif_level1;
		}else if(index>50 && index<=100){
			return R.drawable.notif_level2;
		}else if(index>100 && index<=150){
			return R.drawable.notif_level3;
		}else if(index>150 && index<=200){
			return R.drawable.notif_level4;
		}else if(index>200 && index<=300){
			return R.drawable.notif_level5;
		}else{
			return R.drawable.notif_level6;
		}
	}
	
	/**
	 * Get an array of resource id which is used for retrieving
	 * bitmaps from the application resource folders
	 * @param pm -- pm value in String type
	 * @return -- array of resource ids 
	 */
	public int[] getPmNumberResId(String pm){
		char[] c = pm.toCharArray();
		int[] id = new int[c.length];
		for(int i=0;i<id.length;i++){
			id[i] = number_map.get(String.valueOf(c[i]));
		}
		return id;
	}
	
	/**
	 * Get the drawable id according to the type id provided by the bluetooth device
	 * if the device type is unknown, then returns a unique unknown device resource id.
	 * @param deviceType -- the id of the device class type
	 * @return the drawable resource id relates to the specified type
	 */
	public int getDeviceIcon(int deviceType){
		
		if(device_class_map.containsKey(deviceType)){
		   return device_class_map.get(Integer.valueOf(deviceType));
		}
		
		else{
		   return R.drawable.unknown_device;
		}
	}
	
	/**
	 * Get the background related to the platform name
	 * @param platform
	 * @return
	 */
	public int getPlatformBackground(String platform){
		return this.platform_bg_map.get(platform);
	}
	
	/**
	 * Private construcotr of ImagePicker
	 */
	private ImagePicker(){
		weather_ic_map = new HashMap<String,Integer>();
		weather_ic_map.put("����", R.drawable.cloudy);
		weather_ic_map.put("��", R.drawable.sunny);
		weather_ic_map.put("��", R.drawable.mostly_cloudy);
		weather_ic_map.put("��", R.drawable.light_rain);
		weather_ic_map.put("ѩ", R.drawable.light_snow);
		number_map = new HashMap<String,Integer>();
		number_map.put("0", R.drawable.t0);
		number_map.put("1", R.drawable.t1);
		number_map.put("2", R.drawable.t2);
		number_map.put("3", R.drawable.t3);
		number_map.put("4", R.drawable.t4);
		number_map.put("5", R.drawable.t5);
		number_map.put("6", R.drawable.t6);
		number_map.put("7", R.drawable.t7);
		number_map.put("8", R.drawable.t8);
		number_map.put("9", R.drawable.t9);
		device_class_map = new HashMap<Integer,Integer>();
		device_class_map.put(BluetoothClass.Device.COMPUTER_LAPTOP, R.drawable.icon_laptop);
		device_class_map.put(BluetoothClass.Device.PHONE_SMART, R.drawable.icon_smartphone);
		platform_bg_map = new HashMap<String,Integer>();
		platform_bg_map.put(AbsUser.WEIBO, R.drawable.bg_weibo);
		platform_bg_map.put(AbsUser.WEIXIN, R.drawable.bg_weixin);
		platform_bg_map.put(AbsUser.QQ, R.drawable.bg_qq);
	}
	

}
