package net;

import java.io.IOException;

import model.WeatherItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import util.CacheUtils;
import util.ImagePicker;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherSyncTask extends AsyncTask<String,Void,String>{
	
	private Context context;
	private TextView weather,pm;
	private ImageView icon;
	
	private static final String LOCATION = "location";
	private static final String OUTPUT = "output";
	private static final String AK = "ak";
	private static final String APP_KEY = "ARYrXpRsqbuCPSlpsGIlLGN8";
	private static final String TYPE = "json";
	private static final String basic_url = "http://api.map.baidu.com/telematics/v3/weather?";
	private static final String AND = "&";
	private static final String EQUALS = "=";
	private static final String CODE = "mcode";
	private static final String MCODE = "BE:16:98:A3:4B:25:E5:B9:99:67:68:1D:9E:19:A4:EF:07:56:DB:1C;com.rondo.airrunning";
	
	public WeatherSyncTask(Context context,TextView weather,TextView pm,ImageView icon){
		this.context = context;
		this.weather = weather;
		this.pm = pm;
		this.icon = icon;
	}
     
	@Override
	protected String doInBackground(String... params) {
		String url = buildUrl(params[0]);
		Log.i("url", url);
		HttpGet request = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				String entity = EntityUtils.toString(response.getEntity(), "utf-8");
				CacheUtils utils = new CacheUtils(context);
				utils.cacheWeatherData(entity);
				return entity;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		CacheUtils util = new CacheUtils(context);
		
		WeatherItem today = util.readWeatherToday();
		
		if(today != null && result != null){
			
			this.weather.setText(today.getInst_temp()+"°C");
			
			this.pm.setText("PM2.5 "+today.getPm());
			
			this.icon.setBackgroundResource(
					ImagePicker.getInstance().getWeatherIconResId(today.getWeather()));
			
		}
	}



	/**
	 * Build the request URL for retrieving the weather data
	 * @param param -- the current city 
	 * @return
	 */
	
	private String buildUrl(String param){
		StringBuilder url = new StringBuilder();
        url.append(basic_url);
        url.append(LOCATION+EQUALS+param+AND);
        url.append(OUTPUT+EQUALS+TYPE+AND);
        url.append(AK+EQUALS+APP_KEY+AND);
        url.append(CODE+EQUALS+MCODE);
        return url.toString();
	}

}
