package db;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerListener implements OnItemSelectedListener{
	
	public static final String SETTINGS_PREF = "PROGRAME_CONFIG";
	public static final String LOC_INTERVAL = "locate_interval";
	public static final String LOC_PIN = "locate_pin";
	public static final String MAP_REFRESH = "map_refresh";
	public static final String WEAHTER_MODE = "weather_mode";
	
	private final Context context;
	private final String key;
	
	public SpinnerListener(final Context context,final String key){
		this.context = context;
		this.key = key;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		SharedPreferences p = context.getSharedPreferences(SETTINGS_PREF, 0);
	    SharedPreferences.Editor editor = p.edit();
	    editor.putString(key, parent.getItemAtPosition(position).toString());
	    editor.commit();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

}
