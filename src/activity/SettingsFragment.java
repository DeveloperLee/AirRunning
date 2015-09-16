package activity;

import util.AccessTokenKeeper;
import util.CacheUtils;

import com.rondo.airrunning.R;

import db.SpinnerListener;

import activity.HomeFragment.SlideMenuListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class SettingsFragment extends Fragment {
	
	private static final String keys[] = new String[]{SpinnerListener.LOC_INTERVAL,
		SpinnerListener.LOC_PIN,SpinnerListener.MAP_REFRESH};
	
	public static final String WEIBO = "weibo";
	public static final String QQ = "qq";
	public static final String WEIXIN = "weixin";
	
	private boolean isWeiboReg = false;
	private boolean isWeixinReg = false;
	private boolean isQqReg = false;
	
	private RelativeLayout weather;
	private TextView cache_size;
	private ImageButton menu_btn;
	private ImageButton weibo;
	private Spinner[] spinners = new Spinner[3];
	
	private SlideMenuListener mListener;
	private CacheUtils cache;

	@Override
	public void onAttach(Activity activity) {
		
		try{
			mListener = (SlideMenuListener) activity;
		}catch(ClassCastException e){
			e.printStackTrace();
		}
		
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		cache = new CacheUtils(getActivity().getApplicationContext());
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View contentView = inflater.inflate(R.layout.layout_settings, null);
		
		initViews(contentView);
		setUpListeners();
		
		return contentView;
	}
	
	
	private void initViews(View parentView){
		
		weather = (RelativeLayout) parentView.findViewById(R.id.settings_weather_mode);
		
		menu_btn = (ImageButton) parentView.findViewById(R.id.settings_btn_back);
		
		cache_size = (TextView) parentView.findViewById(R.id.settings_cache_size);
		cache_size.setText(cache.getCacheSize());
		
		spinners[1] = (Spinner) parentView.findViewById(R.id.set_spinner_locate_pin);
		
		ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.location_pin, R.layout.item_spinner);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinners[1].setAdapter(mAdapter);
		
		spinners[2] = (Spinner) parentView.findViewById(R.id.set_spinner_map_refresh);
		
		ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.map_refresh_frequency, R.layout.item_spinner);
		sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinners[2].setAdapter(sAdapter);
		
		spinners[0] = (Spinner) parentView.findViewById(R.id.set_locate_frequency);
		
		ArrayAdapter<CharSequence> fAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.map_locate_frequency, R.layout.item_spinner);
		fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinners[0].setAdapter(fAdapter);
		
		weibo = (ImageButton) parentView.findViewById(R.id.social_weibo);
		
		displaySocialIcons();
		
	}
	
	private void setUpListeners(){
		
		menu_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mListener.menuBtnClicked(SlideMenuListener.LEFT);
				
			}
		});
		
		for(int i = 0; i<keys.length ; i++){
			spinners[i].setOnItemSelectedListener(new SpinnerListener(getActivity(),keys[i]));
		}
		
		weibo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity(), ThirdPartyAccountActivity.class);
//				Intent intent = new Intent(getActivity(), AuthActivity.class);
				intent.putExtra(WEIBO, isWeiboReg);
				intent.putExtra(WEIXIN, isWeixinReg);
				intent.putExtra(QQ,isQqReg);
				startActivity(intent);
				
			}
		});
		
		weather.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),WeatherSettingsActivity.class);
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
	}
	
	
	private void displaySocialIcons(){
		if(AccessTokenKeeper.readAccessToken(getActivity()) != null){
			weibo.setBackgroundResource(R.drawable.icon_weibo);
			isWeiboReg = true;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	

}
