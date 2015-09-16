package util;

import com.baidu.location.LocationClientOption.LocationMode;

import db.SpinnerListener;
import android.content.Context;
import android.content.SharedPreferences;

public class SettingUtils {
	
	private Context context;
	
	private static final int CORRELATION = 1000;
	
	public SettingUtils(Context context){
		this.context = context;
	}
	
	public int getLocateInterval(){
		
		int interval = 0 ;
		
		SharedPreferences p = context.getSharedPreferences(SpinnerListener.SETTINGS_PREF, 0);
		
		if(p == null){
			interval = 15000;
			return interval;
		}
		
		String result = p.getString(SpinnerListener.LOC_INTERVAL, "15秒");

        interval = Integer.parseInt(result.substring(0, 2)) * CORRELATION;
		
        return interval;
		
	}
	
	public LocationMode getLocateAccuracy(){
		
		SharedPreferences p = context.getSharedPreferences(SpinnerListener.SETTINGS_PREF, 0);
		
		String mode = p.getString(SpinnerListener.LOC_PIN, "高精度");
		
		if(mode.equals("高精度")){
			return LocationMode.Hight_Accuracy;
		}else if(mode.equals("GPS")){
			return LocationMode.Device_Sensors;
		}else{
			return LocationMode.Battery_Saving;
		}
		
	}
	
	/**
	 * Write the weather config values to the sp file
	 * @param key
	 * @param enable
	 */
	public void writeWeatherConfigs(String key,boolean enable){
		
		SharedPreferences p = context.getSharedPreferences(SpinnerListener.SETTINGS_PREF, 0);
		
		SharedPreferences.Editor editor = p.edit();
		
	    editor.putBoolean(key, enable);
		
		editor.commit();
	}
	
	/**
	 * Read the boolean values of weather configurations
	 * @param key -- key of the value stores in map
	 * @return
	 */
	public boolean readWeahterConfigsBoolean(String key){
		
		SharedPreferences p = context.getSharedPreferences(SpinnerListener.SETTINGS_PREF, 0);
		
		return p.getBoolean(key, false);
	}
	
	
	public String readWeatherConfigsString(String key){
		
	    SharedPreferences p = context.getSharedPreferences(SpinnerListener.SETTINGS_PREF, 0);
		
		return p.getString(key, "");
	}

}
