package components;


import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.rondo.airrunning.R;

public class RunningRouteOverlay extends WalkingRouteOverlay {
	
	private boolean isRunFinished = false;
	
    //Overlay for displaying the route on the original map overlay
	public RunningRouteOverlay(BaiduMap mMap) {
		super(mMap);
	}
    
	@Override
	public BitmapDescriptor getStartMarker() {
		if(!isRunFinished){
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_start_point);
		}
		    return BitmapDescriptorFactory.fromResource(R.drawable.icon_current_location);
	}

	@Override
	public BitmapDescriptor getTerminalMarker() {
		if(!isRunFinished){
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_current_point);
		}
		return BitmapDescriptorFactory.fromResource(R.drawable.icon_finish);
	}

	@Override
	public boolean onRouteNodeClick(int index) {
		return super.onRouteNodeClick(index);
	}

	@Override
	public void setData(WalkingRouteLine data) {
		super.setData(data);
	}
	
	public void setRunFinished(){
		this.isRunFinished = true;
	}

}
