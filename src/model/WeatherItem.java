package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String SUNNY = "sunny";
	public static final String CLOUDY = "cloudy";
	public static final String RAINY = "rainy";
	public static final String WINDY = "windy";
	public static final String STORM = "storm";
	
	private String weather;
	private String date;
	private String wind;
	private String temp_range;
	private String city;
	private String pm;
	private List<TipItem> tips;
	
	
	public WeatherItem(String weather, String date, String wind,
			String temp_range) {
		this.weather = weather;
		this.date = date;
		this.wind = wind;
		this.temp_range = temp_range;
		tips = new ArrayList<TipItem>();
	}
	
	
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getInst_temp() {
		int length = date.toCharArray().length;
		String sub = date.substring(length - 4, length - 2);
		return sub;
	}
	public String getTemp_range() {
		return temp_range;
	}
	public void setTemp_range(String temp_range) {
		this.temp_range = temp_range;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPm() {
		return pm;
	}
	public void setPm(String pm) {
		this.pm = pm;
	}
	
	public void addTipItem(JSONObject obj){
		
		try {
			TipItem item = new TipItem(obj.getString("title"),
					obj.getString("zs"),obj.getString("des"));
			this.tips.add(item);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public List<TipItem> getTips(){
		return this.tips;
	}
	
	@Override
	public String toString(){
		return this.pm+" "+this.temp_range+" "+this.weather+" "+this.wind;
	}

}
