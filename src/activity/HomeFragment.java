package activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.DataUploadTask;
import net.WeatherSyncTask;
import util.CacheUtils;
import util.DistanceUtils;
import util.ImagePicker;
import util.MarkerGenUtils;
import util.SettingUtils;
import util.SoundPlayer;
import util.TimerUtils;
import model.ARLocation;
import model.DataItem;
import model.RunObj;
import model.WeatherItem;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine.WalkingStep;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.rondo.airrunning.R;

import components.RunningRouteOverlay;
import db.DBHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {
	
	
	 private static final String TAG = "HomeFragment";
	 
	 //UI Widgets
//	 private ImageButton btn_locate;
	 private ImageButton btn_customize;
	 private ImageButton btn_menu;
	 private ImageView icon_weather;
	 private TextView text_temp;
	 private TextView text_pm;
	 private TextView run_duration;
	 private TextView run_distance;
	 private TextView run_cal;
	 private TextView sub_run_duration;
	 private TextView pause_continue;
	 private TextView pause_stop;
	 private RelativeLayout home_map_area;
	 private ImageButton heatmap_btn;
	 private MapView map_home;
	 
	 private LocationClient mLocationClient;
	 private BDLocationListener mListener;
	 private SlideMenuListener sListener;
	 private TimerHandler timeHandler;
	 private SettingUtils setting;
	 
	 //Map 
	 private BaiduMap mMap;
	 private RoutePlanSearch mRoute;
	 private List<WalkingStep> steps;
	 
	 //Identifiers
	 private boolean isPMmapShown = false;
	 private boolean isRunningStart = false;
	 private boolean isStartPointRecord = false;
	 private boolean isFirstLine = true;
	 private boolean getWeather = true;
	 
	 private List<LatLng> data = new ArrayList<LatLng>();
	 private int index = 0;
	 private static final int START = 0;
	 private static final int PAUSE = 1;
	 private static final int FINISH = 2;
	 private int timer_status = FINISH;
	 private static final int REFRESH_TIME = 0;
	 private static final int REFRESH_DISTANCE = 1;
	 private static final int WEIGHT = 60;
	
	 private int duration = 0;
	 private double distance = 0.00d;
	 private String current_city = "";
	 private double cal = 0d;
	 
	 //Data
	 private List<ARLocation> locations;
	 private RouteNode startNode;
	 private DBHelper helper;
	 private SoundPlayer mPlayer;
	 private CacheUtils cacher;
	 
	 
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 
		  setting = new SettingUtils(getActivity()); 
		  cacher = new CacheUtils(getActivity());
		  SDKInitializer.initialize(this.getActivity().getApplicationContext()); 
		  initLocClient();
		  super.onCreate(savedInstanceState);
	 }
	 
	 @Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		 View contentView = inflater.inflate(R.layout.layout_home, container, false);
		 locations = new ArrayList<ARLocation>();
		 steps = new ArrayList<WalkingStep>();
		 timeHandler = new TimerHandler(Looper.getMainLooper());
		 helper = new DBHelper(getActivity());
		 mPlayer = new SoundPlayer(getActivity());
		 initViews(contentView);
		 setUpListeners();
		 getWeatherData();
		 getGPSMagnitude();
		 getLocation();
	  	 return contentView;
	}
	 

	/**
	  * Initialize the client object for the locate process.
	  */
	 private void initLocClient(){
		 mLocationClient = new LocationClient(this.getActivity().getApplicationContext());
		 mListener = new ARLocationListener();
		 mLocationClient.registerLocationListener(mListener);
		 LocationClientOption option = new LocationClientOption();
         option.setLocationMode(setting.getLocateAccuracy());
         option.setCoorType("bd09ll");
         option.setScanSpan(setting.getLocateInterval());
         option.setIsNeedAddress(true);
         option.setNeedDeviceDirect(true);
         mLocationClient.setLocOption(option);
         mLocationClient.start();
         
	 }
	 
	 private void initViews(View contentView){
		 icon_weather = (ImageView) contentView.findViewById(R.id.icon_home_weather);
		 text_temp = (TextView) contentView.findViewById(R.id.text_home_temperature);
		 text_pm = (TextView) contentView.findViewById(R.id.text_home_pm);
		 run_duration = (TextView) contentView.findViewById(R.id.text_home_number);
		 run_distance = (TextView) contentView.findViewById(R.id.num_home_distance);
		 run_cal = (TextView) contentView.findViewById(R.id.num_home_cal);
//		 btn_locate = (ImageButton) contentView.findViewById(R.id.btn_home_locate);
		 btn_customize = (ImageButton) contentView.findViewById(R.id.btn_home_customize);
		 btn_menu = (ImageButton) contentView.findViewById(R.id.btn_home_menu);
		 heatmap_btn = (ImageButton) contentView.findViewById(R.id.heatmap_btn);
		 sub_run_duration = (TextView) contentView.findViewById(R.id.num_home_duration);
		 pause_continue = (TextView) contentView.findViewById(R.id.pause_continue);
		 pause_stop = (TextView) contentView.findViewById(R.id.pause_stop);
		 addMap(contentView);
	 }
	 
	 private void setUpListeners(){
		 
		 btn_menu.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					if(!isRunningStart){
					  sListener.menuBtnClicked(SlideMenuListener.LEFT);
					}else{
					  Toast.makeText(getActivity(), "专心跑步效果更好！",Toast.LENGTH_SHORT).show();	
					}
				}
		 });
		 
//		 btn_locate.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				if(!(index>=data.size()-1)){
//				getWalkingRoute(data.get(index),data.get(index++));
//				}
//			}
//		 });
		 
		 btn_customize.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(timer_status == START){
					pauseRun();
				}else if(timer_status == PAUSE || timer_status == FINISH){
				    startRun();
					mPlayer.playSingleVoice(SoundPlayer.RUN_START);
				}
			}
		 });
		 
		 icon_weather.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(current_city.equals("")){
				   getWeather = true;  //Will automatically get data from the server next time 
				                       //we receive a location from Baidu location server.
				}else{
					getWeatherFromServer(current_city,false); //Otherwise using the current city directly
				}
				if(!isRunningStart){
				   sListener.menuBtnClicked(SlideMenuListener.RIGHT);
			    }else{
				   Toast.makeText(getActivity(), "专心跑步效果更好！",Toast.LENGTH_SHORT).show();	
				}
			}
		 });
		 
		 heatmap_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(isPMmapShown){
					mMap.setBaiduHeatMapEnabled(false);
					isPMmapShown = false;
					Toast.makeText(getActivity(), "正在关闭PM分布图", Toast.LENGTH_SHORT).show();
				}else{
					mMap.setBaiduHeatMapEnabled(true);
					isPMmapShown = true;
					Toast.makeText(getActivity(), "正在开启PM分布图", Toast.LENGTH_SHORT).show();
				}
			}
		 });
		 
		 pause_continue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startRun();
				mPlayer.playSingleVoice(SoundPlayer.RUN_CONTINUE);
			}
		 });
		 
		 pause_stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endRun();
			}
		});
		 
	 }
	 
	 private void startRun(){
		 mMap.clear();
		 isRunningStart = true;
		 timer_status = START;
		 setVisibility(View.GONE);
		 getLocation();
		 Message msg = timeHandler.obtainMessage();
		 timeHandler.sendMessage(msg);
	 }
	 
	 private void pauseRun(){
	     timer_status = PAUSE;
	     setVisibility(View.VISIBLE);
	     mLocationClient.stop();
	     mPlayer.playSingleVoice(SoundPlayer.RUN_PAUSE);
	 }
	 
	 
	 /**
	  * Call when the current run ends
	  */
	 private void endRun(){
		 isRunningStart = false;
		 isStartPointRecord = false;
		 isFirstLine = true;
		 setVisibility(View.GONE);
		 mLocationClient.stop();
		 run_duration.setText("00:00:00");
		 sub_run_duration.setText("00:00:00");
		 run_distance.setText("0.00");
		 cacheRunData();  //Cache the data
		 mPlayer.playMultiVoice(mPlayer.readTime(duration));
		 helper.a();
		 timer_status = FINISH;
		 Intent intent = new Intent(getActivity(),FinishRunActivity.class); 
		 intent.putExtra("City", current_city);
		 intent.putExtra("Duration", TimerUtils.convertTime(duration));
		 intent.putExtra("Distance",distance);
		 intent.putExtra("Cal", cal);
		 resetData();
		 startActivity(intent);
		 getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	 }
    
	 
	/**
	 * Reset the data to their original values
	 */
	private void resetData() {
		cal = 0d;
		 index = 0;
		 duration = 0;
		 data.clear();
		 steps.clear();
		 locations.clear();
	}
	 
	 /**
	  * Retrieves weather data from sharedpreferences file cached from the server
	  */
	 private void getWeatherData(){
		 
		 List<WeatherItem> items = cacher.readWeatherInfos();
		 
		 if(items.size() == 0){
	    	text_temp.setText("暂无数据");
		    text_pm.setText("");
		 }else{
			 WeatherItem today = items.get(0);
			 
			 text_temp.setText(today.getInst_temp()+"°C");
			 text_pm.setText("PM2.5 "+today.getPm());
			 icon_weather.setBackgroundResource(
					 ImagePicker.getInstance().getWeatherIconResId(today.getWeather()));
		 }
		 
	 }
	 
	 /**
	  * Get the GPS Magnitude from the system
	  */
	 private void getGPSMagnitude(){
		 
	 }
	 
	 /**
	  * Calculates the distance between p1 & p2
	  * @param p1
	  * @param p2
	  */
	 private double calcDistance(LatLng p1,LatLng p2){
		return DistanceUtils.getDistance(p1, p2);
	 }
	 
	 /**
	  * Add the map view dynamically
	  */
	 private void addMap(View contentView){
		 home_map_area = (RelativeLayout) contentView.findViewById(R.id.home_map_area);
		 BaiduMapOptions options = new BaiduMapOptions();
		 options.zoomControlsEnabled(false).zoomGesturesEnabled(true);
		 map_home = new MapView(contentView.getContext(),options);
		 RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				 LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		 mMap = map_home.getMap();
		 mMap.setBuildingsEnabled(true);
		 home_map_area.addView(map_home,0,params); 
	 }
	 
	 
	 /**
	  * Get the current location
	  */
	 private void getLocation(){
         Log.e("LOC_REQUESTED", "mLocationClient = " + mLocationClient + " mLocationClient.isStarted() =" + mLocationClient.isStarted());
         if (mLocationClient != null && mLocationClient.isStarted())
         {
             mLocationClient.requestLocation();
         }
         else
         {
            mLocationClient.start();
            mLocationClient.requestLocation();
         }

	 }
	 
	 /**
	  * Cache the running data into SharedPreferences xml file
	  */
	 private void cacheRunData(){
		 DataUploadTask task = new DataUploadTask();
		 task.execute(new DataItem(111.11,222.22,"2012-01-01 12:00:00",20.0));
		 if(isStartPointRecord){
		  RunObj _run = new RunObj(locations);
		  _run.setDistance(distance);
		  _run.setDuration(TimerUtils.convertTime(duration));
		  _run.setStartAddr(locations.get(0).getAddr());
		  _run.setFinishAddr(locations.get(locations.size()-1).getAddr());
		  _run.setTime(locations.get(0).getTime());
		  helper.insertRun(_run,(int)System.currentTimeMillis()/1000);
		 }
	 }
    
	
	@Override
	public void onDestroy() {
		mLocationClient.stop();
		map_home.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onPause() {
		map_home.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		map_home.onResume();
		super.onResume();
	}
	
	
	/**
	 * Inner listener class, receive the feedback after sending the location request
	 */
	class ARLocationListener implements BDLocationListener{
		@Override
		public void onReceiveLocation(BDLocation location) {
			StringBuffer result = new StringBuffer(256);
			if(location == null){
				result.append("获取定位信息失败");
				return;
			} 
			else{
				ARLocation temp_loc = new ARLocation(location.getLatitude(),
				location.getLongitude(),location.getAddrStr());
				temp_loc.setTime(location.getTime());
				locations.add(temp_loc);
				current_city = location.getCity();  //Get the current city
				
				if(!isRunningStart){ 
					
					//If the getWeather attribute is true it means 
					//that we want to get the weather information automatically
					if(getWeather){  
						getWeatherFromServer(current_city,false);
					}
					animateToLocation(location);
					
				}else{
					LatLng point = new LatLng(location.getLatitude(),location.getLongitude());
					if(!isStartPointRecord){
						
						startNode = new RouteNode();
						startNode.setLocation(point);
//						data.add(point);
						MarkerGenUtils.setStartMarker(mMap, point);
						isStartPointRecord = true;
						
					}else{
						
						if(DistanceUtils.shouldDisplay(data.get(index), point)){
							
						  data.add(point);
						  getWalkingRoute(data.get(index),data.get(index+1));
						  Message msg = timeHandler.obtainMessage(REFRESH_DISTANCE);
						  timeHandler.sendMessage(msg);
						  index++;
						  
						}
						
					}
				}
			}
		}
	}
	
	
	class MyRouteResultListener implements OnGetRoutePlanResultListener{

		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			
		}

		@Override
		public void onGetTransitRouteResult(TransitRouteResult result) {
			
		}

		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult result) {
			WalkingRouteLine line = result.getRouteLines().get(0);
			steps.addAll(line.getAllStep());
			line.setSteps(steps);
			line.setStarting(startNode);
			MarkerGenUtils.setMarkers(mMap, line, isFirstLine);
			isFirstLine = false;
		}
		
	}
	
	/**
	 * This interface is to make sure that the fragments share the same 
	 * behaviors with the parent activity
	 * 
	 * The root activity and all its fragments must implements this interface.
	 * 
	 * @author Developer-Lee
	 *
	 */
	public interface SlideMenuListener{
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
		public void  menuBtnClicked(int direction);
	}
	
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			sListener = (SlideMenuListener) activity;
		}catch(ClassCastException e){
			throw new ClassCastException("This activity must implements SlideMenuListener interface.");
		}
	}
	
	private void animateToLocation(BDLocation location){
		LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
		MapStatus new_status = new MapStatus.Builder().target(myLocation).zoom(16).build();
		MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(new_status);
		mMap.animateMapStatus(update,500);
//		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.point_clickon);
//		OverlayOptions option = new MarkerOptions().draggable(false)
//				                                   .position(myLocation)
//				                                   .icon(bitmap);
//		mMap.addOverlay(option);
	}
	
	/**
	 * Get a walking route between a start position and an end position
	 * @param from  
	 * @param to
	 */
	private void getWalkingRoute(LatLng from,LatLng to){
		mRoute = RoutePlanSearch.newInstance();
		mRoute.setOnGetRoutePlanResultListener(new MyRouteResultListener());
		PlanNode start = PlanNode.withLocation(from);
		PlanNode end = PlanNode.withLocation(to);
		WalkingRoutePlanOption walk_option = new WalkingRoutePlanOption();
		walk_option.from(start);
		walk_option.to(end);
		if(!mRoute.walkingSearch(walk_option)){
			Log.e(TAG, "Failed to retrieve the route");
		}
		distance += calcDistance(data.get(index),data.get(index+1));
		run_cal.setText(String.valueOf((int)distance * WEIGHT));
		run_distance.setText(String.valueOf(distance));
	}
	
	class TimerHandler extends Handler{
		
		public TimerHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			  case REFRESH_TIME:
			   if(timer_status == START){
				 duration++;
				 String time = TimerUtils.convertTime(duration);
				 run_duration.setText(time);
				 sub_run_duration.setText(time);
				 Message nextMsg = timeHandler.obtainMessage(REFRESH_TIME);
				 timeHandler.sendMessageDelayed(nextMsg, 1000L);
			   }
			   break;
			   case REFRESH_DISTANCE:
			   run_distance.setText(new DecimalFormat("*.00").format(distance));
			   run_cal.setText(String.valueOf(distance));
			   break; 
			}
		     super.handleMessage(msg);
		}
	}
	
	
	/**
	 * Set the visibilities of the continue and stop button
	 * @param visibility
	 */
	private void setVisibility(int visibility){
		pause_continue.setVisibility(visibility);
	    pause_stop.setVisibility(visibility);
	}
	
	
//	/**
//	 * Display the dialog after finishing a run
//	 */
//	private void dialog(){
//		
//		DataUploadDialog.Builder builder = new 	DataUploadDialog.Builder(getActivity()).
//				 setDescription("是否要将数据上传到服务器?")
//				 .setPosiviteBtnListener(new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						cacheRunData();
//					}
//				})
//				.setNegativeBtnListener(new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//		
//		  DataUploadDialog dialog = builder.build();
//		  
//		  dialog.show();
//		        
//	}
	
	
	/**
	 * Get the weather information from server.
	 * #This method is a anti-syncronized task, it doesn't block the main thread.
	 * @param city   -- the name of the location city
	 * @param next   -- if this parameter is set to true, we allow the application
	 *               -- to get the information automatically next time.
	 */
	private void getWeatherFromServer(String city,boolean next){
		WeatherSyncTask wTask = new WeatherSyncTask(getActivity(),
				text_temp,text_pm,icon_weather);
		wTask.execute(current_city);
		getWeather = next;
	}
	
	/*
	private void simuData(){
		this.data.add(new LatLng(41.7708410000,123.4260070000));
		this.data.add(new LatLng(41.7708070000,123.4232620000));
		this.data.add(new LatLng(41.7704310000,123.4196690000));
		this.data.add(new LatLng(41.7712710000,123.4194170000));
		this.data.add(new LatLng(41.7722940000,123.4200460000));
		this.data.add(new LatLng(41.7728050000,123.4211420000));
		this.data.add(new LatLng(41.7722670000,123.4230820000));
		this.data.add(new LatLng(41.7725360000,123.4234060000));
		this.data.add(new LatLng(41.7728250000,123.4235180000));
	}*/
	
	 
}
