package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.WeatherItem;
import model.WeiboUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class CacheUtils {
	
	private Context context;
	
	private File cache_dir;
	
	private static final int KB = 1024;
	private static final int MB = 1024 * 1024;
	private static final String WEATHER_DATA_CACHE = "weather_data";
	
	public static final String USER_PREF_NAME = "weibo_user_pref";
	public static final String WEATHER_JSON_DATA_KEY = "weather_json";
	public static final String UPDATE_TIME = "update_time";
	private static final String DEFAULT_JSON_RESULT = "empty";
	
	
	public CacheUtils(Context context){
		this.context = context;
		cache_dir = context.getCacheDir();
	}
	
	@SuppressLint("SimpleDateFormat")
	public void cacheWeatherData(String entity){
		SharedPreferences sp = context.getSharedPreferences(WEATHER_DATA_CACHE, 0);
		SharedPreferences.Editor editor = sp.edit();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String update_time = sdf.format(new Date(System.currentTimeMillis()));
		editor.putString(WEATHER_JSON_DATA_KEY, entity);
		editor.putString(UPDATE_TIME, update_time);
		editor.commit();
	}
	
	
	public String getCacheSize(){
		
		int size = 0;
		
		File[] cache_files = cache_dir.listFiles();
		
		for(File file:cache_files){
			
			try {
				FileInputStream fis = new FileInputStream(file);
				size += fis.available();
				fis.close();
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
        return getScale(size);
        
	}
	
	/**
	 * Convert the cache size into an appropriate scale form
	 * @param size -- the total space of cache folders
	 * @return converted text
	 */
	private String getScale(int size){
		
		if(size < KB){
			return String.valueOf(size) + " Bytes";
		}else if(size > KB && size < MB /2){
			return String.valueOf(size / KB) + " Kb";
		}else{
			return String.valueOf(size / MB) + " Mb";
		}
	}
	
	public void cacheWeiboUser(WeiboUser user){
		
		SharedPreferences sp = context.getApplicationContext()
				.getSharedPreferences(USER_PREF_NAME, 0);
		
		SharedPreferences.Editor editor = sp.edit();
		
		editor.putString(WeiboUser.NICKNAME, user.screen_name);
		editor.putString(WeiboUser.HEAD_URL,user.profile_image_url);
		editor.putString(WeiboUser.WEIBO_URL, user.profile_url);
		
		editor.commit();
		
	}
	
	public WeiboUser readCacheWeiboUser(){
		
		SharedPreferences sp = context.getApplicationContext()
				.getSharedPreferences(USER_PREF_NAME, 0);
		
		WeiboUser user = new WeiboUser();
		
		user.screen_name = sp.getString(WeiboUser.NICKNAME, "");
	    user.profile_image_url = sp.getString(WeiboUser.HEAD_URL, "");
	    user.profile_url = sp.getString(WeiboUser.WEIBO_URL, "");
	    
	    return user;
	}
	
	public List<WeatherItem> readWeatherInfos(){
		
		List<WeatherItem> weathers = new ArrayList<WeatherItem>();
		SharedPreferences sp = context.getApplicationContext().getSharedPreferences(WEATHER_DATA_CACHE, 0);
		String json_result = sp.getString(WEATHER_JSON_DATA_KEY, DEFAULT_JSON_RESULT);
		
		if(!json_result.equals(DEFAULT_JSON_RESULT)){
			
		  try {
			JSONObject weather_obj = new JSONObject(json_result);
			if(weather_obj.getString("status").equals("success")){
				JSONArray array = weather_obj.getJSONArray("results");
				JSONObject result = array.getJSONObject(0);
				String city = result.getString("currentCity");
				int pm25 = result.getInt("pm25");
				JSONArray results = result.getJSONArray("weather_data");
				for(int i = 0;i<results.length();i++){
					WeatherItem item = new WeatherItem(results.getJSONObject(i).getString("weather"),
							results.getJSONObject(i).getString("date"),
							results.getJSONObject(i).getString("wind"),
							results.getJSONObject(i).getString("temperature"));
					if(i == 0){
						item.setCity(city);
						item.setPm(String.valueOf(pm25));
					}
					weathers.add(item);
					Log.e("WEATHER_ITEM",item.toString());
				}
			}
	  	  } catch (JSONException e) {
			e.printStackTrace();
		  }
		  
		}
		return weathers;
	}
	
	public String readUpdateTime(){
		SharedPreferences sp = context.getApplicationContext().getSharedPreferences(WEATHER_DATA_CACHE, 0);
		String time = sp.getString(UPDATE_TIME, "暂无最后更新日期");
		return "最后更新日期 : "+time;
	}
	
	public WeatherItem readWeatherToday(){
		
		SharedPreferences sp = context.getApplicationContext().getSharedPreferences(WEATHER_DATA_CACHE, 0);
		String json_result = sp.getString(WEATHER_JSON_DATA_KEY, DEFAULT_JSON_RESULT);
		if(!json_result.equals(DEFAULT_JSON_RESULT)){
			
			  try {
				JSONObject weather_obj = new JSONObject(json_result);
				if(weather_obj.getString("status").equals("success")){
					JSONArray array = weather_obj.getJSONArray("results");
					JSONObject result = array.getJSONObject(0);
					JSONArray results = result.getJSONArray("weather_data");
					WeatherItem item = new WeatherItem(results.getJSONObject(0).getString("weather"),
							results.getJSONObject(0).getString("date"),
							results.getJSONObject(0).getString("wind"),
							results.getJSONObject(0).getString("temperature"));
					item.setCity(result.getString("currentCity"));
					item.setPm(String.valueOf(result.getInt("pm25")));
					JSONArray indexs = result.getJSONArray("index");
					for(int i =0;i<indexs.length();i++){
						item.addTipItem(indexs.getJSONObject(i));
					}
					return item;
				  }
				}catch (JSONException e) {
					e.printStackTrace();
				}
	   }
		
		return null;
	}
		

}
