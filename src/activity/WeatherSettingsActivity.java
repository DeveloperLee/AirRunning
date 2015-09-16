package activity;

import util.SettingUtils;

import com.rondo.airrunning.R;

import db.SpinnerListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class WeatherSettingsActivity extends Activity {
	
	private ImageButton btn_back;
	private ToggleButton btn_cache,btn_background;
	
	private Spinner mode;
	private ArrayAdapter<CharSequence> mAdapter;
	
	private SettingUtils sUtil = new SettingUtils(this);
	
	public static final String WEATHER_CACHE_ENABLE = "enable_weather_cache";
	public static final String WEATHER_BACKGROUND_ENABLE = "enable_background_data";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weather_settings);
		initViews();
		loadWeahterConfigs();
		setUpListeners();
	}

	private void initViews() {
		btn_back = (ImageButton) findViewById(R.id.weather_settings_btn_back);
		
		btn_cache = (ToggleButton) findViewById(R.id.weather_cache_switch);
		btn_background = (ToggleButton) findViewById(R.id.weather_background_switch);
		
		mode = (Spinner) findViewById(R.id.weather_mode_spinner);
		
		mAdapter = ArrayAdapter.createFromResource(this,
				R.array.weather_mode, R.layout.item_spinner);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		mode.setAdapter(mAdapter);
	}
	
	
	/**
	 * Load the configurations for rendering the UI
	 */
	private void loadWeahterConfigs(){
		
		btn_cache.setChecked(sUtil.readWeahterConfigsBoolean(WEATHER_CACHE_ENABLE));
		
		btn_background.setChecked(sUtil.readWeahterConfigsBoolean(WEATHER_BACKGROUND_ENABLE));
		
		int position = mAdapter.getPosition(sUtil.readWeatherConfigsString
				(SpinnerListener.WEAHTER_MODE));
		
		mode.setSelection(position, true);
		
	}

	
	
	
	private void setUpListeners() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WeatherSettingsActivity.this,
						MainActivity.class);
				intent.putExtra("FRAGMENT_NUM", 3);
				startActivity(intent);
				WeatherSettingsActivity.this.overridePendingTransition(R.anim.push_right_in, 
						R.anim.push_right_out);
			}
		});
		
		btn_cache.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sUtil.writeWeatherConfigs(WEATHER_CACHE_ENABLE, isChecked);
			}
		});
		
        btn_background.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sUtil.writeWeatherConfigs(WEATHER_BACKGROUND_ENABLE, isChecked);
			}
		});
        
        mode.setOnItemSelectedListener(new SpinnerListener(this,SpinnerListener.WEAHTER_MODE));
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	

}
