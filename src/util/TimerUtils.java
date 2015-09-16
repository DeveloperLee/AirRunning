package util;

import java.util.HashMap;
import java.util.Map;

public class TimerUtils {
	
	private static final int HOUR_SEC = 3600;
	private static final int MINUTE_SEC = 60;
	
	public static final String HOUR = "hour";
	public static final String MINUTE = "minute";
	public static final String SECOND = "second";
	
	/**
	 * Converts a given int into a readable string time.
	 * @param seconds -- seconds in number
	 * @return 
	 */
	public static String convertTime(int seconds){
		int hour = (int) Math.floor(seconds/HOUR_SEC);
		int minute = (int) Math.floor((seconds-hour*HOUR_SEC)/MINUTE_SEC);
		int second = seconds-hour*HOUR_SEC-minute*MINUTE_SEC;
		String sHour = String.valueOf(hour);
	    String sMinute = String.valueOf(minute);
	    String sSecond = String.valueOf(second);
		if(hour<10){
			sHour = "0"+hour;
		}
		if(minute<10){
			sMinute = "0"+minute;
		}
		if(second<10){
			sSecond = "0"+second;
		}
		return sHour+":"+sMinute+":"+sSecond;
	}
	
	/**
	 * Seperate hour,minute, provide a map object to sound pool
	 * so as to read 
	 * @param seconds
	 * @return
	 */
	public static Map<String,Integer> splitTime(int seconds){
		Map<String,Integer> map = new HashMap<String,Integer>();
		int hour = (int) Math.floor(seconds/HOUR_SEC);
		int minute = (int) Math.floor((seconds-hour*HOUR_SEC)/MINUTE_SEC);
		int second = seconds-hour*HOUR_SEC-minute*MINUTE_SEC;
		map.put(HOUR, hour);
		map.put(MINUTE, minute);
		map.put(SECOND, second);
		return map;
	}

}
