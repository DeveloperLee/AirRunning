package activity;

import model.WeatherItem;
import util.CacheUtils;
import util.ImagePicker;

import com.rondo.airrunning.R;

import components.SlideMenu;

import activity.HomeFragment.SlideMenuListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements SlideMenuListener {
	
	private SlideMenu slide_menu;
	private TextView side_start_run,side_feedback,side_stat,side_settings;
	private TextView update_time,city,pm_number,weather;
	private ImageView cloud1,cloud2;
	private ImageView[] temperature = new ImageView[4];
	private ImageButton refresh_weather,pm_level;
	
	private RelativeLayout weather_layout;
	private RelativeLayout pm_layout;
	
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private SideMenuHandler sHandler;
	private CacheUtils cacher;
	
	private HomeFragment home_fragment;
	private FeedbackFragment feedback_fragment;
	private StatisticFragment stat_fragment;
	private SettingsFragment settings_fragment;
	
	private WeatherItem today;
	
	private Fragment[] fragments;
	
	private static final int CLOSE_DELAY = 200;
	private static final long MAXIMUM_INTERVAL = 1000L;
	private static int exit_flag = 0;
	private static final int[] resIds = new int[]
			{R.id.temp_num1,R.id.temp_num2,R.id.temp_num3,R.id.temp_num4};
	
	private int fragment_num = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		cacher = new CacheUtils(this);
		initViews();
		setUpListeners();
		initFragments();
		setUpFirstFragment();
	    loadWeatherMenuData();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		loadDaytime();
	}
	
	
	/**
	 * Load the weather data from sharedpreferences file 
	 * then fills to the according widgets
	 */
	private void loadWeatherMenuData() {
		
		update_time.setText(cacher.readUpdateTime());
		today = cacher.readWeatherToday();
		
		if(today != null){
		  city.setText(today.getCity());
		  weather.setText(today.getWeather());
		  
		  ImagePicker picker = ImagePicker.getInstance();
		  int[] numberResId = picker.getPmNumberResId("17");
		  for(int i=0;i<numberResId.length;i++){
			  temperature[i].setBackgroundResource(numberResId[i]);
		  }
		  temperature[numberResId.length].setBackgroundResource(R.drawable.circle);
		  
		  pm_level.setBackgroundResource(picker.getPMLevelResId(today.getPm()));
		  pm_number.setText(today.getPm());
		  
		}
	}	
	
	
	private void loadDaytime() {
		 weather_layout = (RelativeLayout) findViewById(R.id.weather_top);
		 addSunLight(weather_layout);
		 cloud1 = (ImageView) findViewById(R.id.weather_widget_left1);
		 cloud1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.cloud_translation_fast));
		 cloud2 = (ImageView) findViewById(R.id.weather_widget_left2);
		 cloud2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.cloud_translation_slow));
	}
    
	/**
	 * Load the widgets
	 */
	private void initViews(){
		 slide_menu = (SlideMenu) findViewById(R.id.slide_menu);
		 side_feedback = (TextView) findViewById(R.id.side_feedback);
		 side_start_run = (TextView) findViewById(R.id.side_start_running);
		 side_stat = (TextView) findViewById(R.id.side_stat);
		 side_settings = (TextView) findViewById(R.id.side_settings);
		 update_time = (TextView) findViewById(R.id.update_time);
		 city = (TextView) findViewById(R.id.weather_city);
		 pm_number = (TextView) findViewById(R.id.pm_number);
		 weather = (TextView) findViewById(R.id.weather_text);
		 pm_level = (ImageButton) findViewById(R.id.pm_level);
		 refresh_weather = (ImageButton) findViewById(R.id.weather_refresh);
		 pm_layout = (RelativeLayout) findViewById(R.id.pm_level_area);
		 
		 for(int i=0;i<resIds.length;i++){
			 temperature[i] = (ImageView) findViewById(resIds[i]);
		 }
		 
		 sHandler = new SideMenuHandler(getMainLooper(),slide_menu);
	}
	
	private void setUpListeners(){
		 side_start_run.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.fragment_content, fragments[0]);
					fragmentTransaction.commit();
					sHandler.sendEmptyMessageDelayed(0, CLOSE_DELAY);
				}
		});
		 side_feedback.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.fragment_content, fragments[2]);
					fragmentTransaction.commit();
					sHandler.sendEmptyMessageDelayed(0, CLOSE_DELAY);
				}
		 });
		 side_stat.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_content, fragments[1]);
				fragmentTransaction.commit();
				sHandler.sendEmptyMessageDelayed(0, CLOSE_DELAY);
			}
		 });
		 side_settings.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_content, fragments[3]);
				fragmentTransaction.commit();
				sHandler.sendEmptyMessageDelayed(0, CLOSE_DELAY);
				
			}
			 
		 });
		 
		 refresh_weather.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadWeatherMenuData();
			}
		});
		 
		pm_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				WeatherItem display = cacher.readWeatherToday();
				
				if(display != null){
					
			     Intent intent = new Intent(MainActivity.this,WeatherTipsActivity.class);
				 intent.putExtra("TODAY", display);
				 startActivity(intent);
				 MainActivity.this.overridePendingTransition(R.anim.pull_up_in, R.anim.pull_up_out);
				 
				}else{
			      Toast.makeText(getApplicationContext(), 
			    		  "获取今日指数信息失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	private void initFragments(){
		home_fragment = new HomeFragment();
		feedback_fragment = new FeedbackFragment();
		stat_fragment = new StatisticFragment();
		settings_fragment = new SettingsFragment();
		
		fragments = new Fragment[]{home_fragment,stat_fragment,feedback_fragment,settings_fragment};
		
		fragment_num = getIntent().getIntExtra("FRAGMENT_NUM", 0);
	}
	
	private void setUpFirstFragment(){
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_content, fragments[fragment_num]);
		fragmentTransaction.commit();
	}
	
	//Dynamically add a sunlight effect to the right slide menu
	//The position is dynamically accustomed based on the real screen size
	private void addSunLight(RelativeLayout layout){
		 int width = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  //Pre-define the width in measure
         int height = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); //Pre-define the height in measure
         layout.measure(width, height); 
         //The reason why we get the measured width and height here is that it automatically returns 0
         //If we don't manually set the measure specifications before retrieve it.
         int parent_width = layout.getMeasuredWidth(); 
         int parent_height = layout.getMeasuredHeight(); 

		Log.e("SIZE", String.valueOf(parent_width)+" "+String.valueOf(parent_height));
		
		ImageView img = new ImageView(layout.getContext());
		img.setBackgroundResource(R.drawable.sunshine);   //Set the sunshine background image
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(420,420);
		params.leftMargin = 0;    
		params.topMargin = (int) (0.8 * parent_height); //Set the position relative to the top of the screen based on 
		                                                //the measured height, therefore the position could be accustomed.
		img.setLayoutParams(params);
		layout.addView(img);
		
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim); 
		img.startAnimation(anim);  //Start the sunshine rotating animation
	}
	
	/**
	 * If the user click the back button twice within 1s,
	 * the applicatoin exits
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(exit_flag == 1){
			  this.finish();
			  return true;
			}else{
			  Toast.makeText(this, "再点击一次将退出程序", Toast.LENGTH_SHORT).show();
			  exit_flag = 1;
			  new TimerThread().start();
			}
		}
		return false;
	}


	@Override
	public void menuBtnClicked(int direction) {
		  if(slide_menu.isMainScreenShowing()){
			 if(direction == SlideMenuListener.LEFT){
				 slide_menu.openLeftMenu();
			 }else{
				 slide_menu.openRightMenu();
			 }
		   }else{
			 slide_menu.closeMenu();
		  }
	}
	
	class SideMenuHandler extends Handler{
		
		 private SlideMenu menu;
		
		 public SideMenuHandler(Looper looper,SlideMenu menu){
			 super(looper);
			 this.menu = menu;
		 }
		 
		 @Override
		 public void handleMessage(Message msg){
			 menu.closeMenu();
		 }
	}
	
	class TimerThread extends Thread{

		@Override
		public void run() {
			try {
				Thread.sleep(MAXIMUM_INTERVAL);
				exit_flag = 0;
			} catch (InterruptedException e) {
				Log.e("TIMER_THREAD_SLEEP_DISRUPTED", "TIMER_ERROR");
			}
			super.run();
		}
	}

	
	
	
}
