package util;



import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class DistanceUtils {
	
	private static final double _thresh = 0.000150;
	private static double distance = 0d;
	
	public static double getDistance(LatLng p1,LatLng p2){
//		double distance =  DistanceUtil.getDistance(p1, p2) * 100;
		distance += 0.32;
		return distance;
	}
	
	/**
	 * Judge whether a newly located should be displayed on the map
	 * @param p1 the latest point be located
	 * @param p2 the current located point
	 * @return 
	 */
	public static boolean shouldDisplay(LatLng p1, LatLng p2){
		
		if(Math.abs(p1.latitude - p2.latitude) < _thresh && 
				Math.abs(p1.longitude - p2.longitude) < _thresh){
			return false;
		}
		
		return true;
		
	}

}
