package util;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Color;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine.WalkingStep;
import com.rondo.airrunning.R;


/**
 * This is a class used for generating markers on map overlay
 * @author Developer-Lee
 *
 */
public class MarkerGenUtils {
	
	 
	 public static void setStartMarker(BaiduMap map,LatLng point){
		 
		 MarkerOptions option = new MarkerOptions().position(point)
				 .draggable(false)
				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mylocation));
		 
		 map.addOverlay(option);
	 }
	 
	
     public static void setMarkers(BaiduMap map, WalkingRouteLine line, final boolean isFirst){
    	 
    	 map.clear();
    	 
    	 List<LatLng> points = new ArrayList<LatLng>();
    	 
    	 List<WalkingStep> steps = line.getAllStep();
    	 
    	 
         for(int i = 0;i<steps.size()-1;i++){
        	 for(int k = 0;k<steps.get(i).getWayPoints().size();k++){
        		 points.add(steps.get(i).getWayPoints().get(k));
        	 }
    	  
    	 }
         
         for(int j = 0;j<points.size();j++){
        	 
        	 MarkerOptions option = new MarkerOptions().position(points.get(j))
        			 .draggable(false);
           if(j == 1){
        	   option.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mylocation));
           }
        	 
           else{
        	   option.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker));
           }
           
           //Draw a polyline with given points
           PolylineOptions pOption = new PolylineOptions().color(Color.rgb(59, 183, 217))
        		   .dottedLine(false)
        		   .points(points);
           
           map.addOverlay(option);
           map.addOverlay(pOption);
           MapStatus update = new MapStatus.Builder().target(points.get(points.size()-1))
        		   .zoom(18)
        		   .build();
           MapStatusUpdate status = MapStatusUpdateFactory.newMapStatus(update);
           map.animateMapStatus(status, 1000);
         }
         
     }

}
