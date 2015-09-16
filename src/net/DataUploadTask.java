package net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import model.DataItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class DataUploadTask extends AsyncTask<DataItem, Integer, String> {
	
	private static final String SINGLE_UPLOAD_URL = "http://air.developeryang.com/api/sensor/data/upload";
	private static final String MULTI_UPLOAD_URL = "http://air.developeryang.com/api/sensor/data/mutil_upload";
	private static final String TOKEN = "www.developeryang.com";

	@Override
	protected String doInBackground(DataItem...items) {
		if(items.length == 1){
		   try {
		 	 HttpPost post = new HttpPost(SINGLE_UPLOAD_URL);
		 	 int request_id = (int) System.currentTimeMillis() / 1000;
		 	 
		 	 List<BasicNameValuePair> paramsList = new ArrayList<BasicNameValuePair>();
		 	 paramsList.add(new BasicNameValuePair("lat",String.valueOf(items[0].getLatitude())));
		 	 paramsList.add(new BasicNameValuePair("lng",String.valueOf(items[0].getLongtitude())));
		 	 paramsList.add(new BasicNameValuePair("time",String.valueOf(items[0].getTime())));
		 	 paramsList.add(new BasicNameValuePair("pm25",String.valueOf(items[0].getPm())));
		 	 paramsList.add(new BasicNameValuePair("request_id",String.valueOf(request_id)));
		 	 paramsList.add(new BasicNameValuePair("token",TOKEN));
		 	 
		 	 post.setEntity(new UrlEncodedFormEntity(paramsList,HTTP.UTF_8));
		 	 
		     HttpClient client = new DefaultHttpClient();
		     HttpResponse response = client.execute(post);
		     
		     if(response.getStatusLine().getStatusCode() == 200){
		       String _entity = EntityUtils.toString(response.getEntity());
		       Log.e("RESPONSE_ENTITY", _entity);
		       JSONObject _obj = new JSONObject(_entity);
		       Log.e("RESPONSE", _obj.getString("status"));
		       return _obj.getString("status");
		     }
		     
		   } catch (JSONException e) {
			 e.printStackTrace();
		   } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		   } catch (ClientProtocolException e) {
		 	e.printStackTrace();
		   } catch (IOException e) {
			e.printStackTrace();
		   }
		   
		}else{
			
			JSONObject json_entity = new JSONObject();
			JSONArray _array = new JSONArray();
			int request_id = (int) System.currentTimeMillis() / 1000;
			
			try{
				
			 for(int index = 0;index<items.length;index++){
				JSONObject obj = new JSONObject();
				obj.put("lat", items[index].getLatitude());
				obj.put("lng", items[index].getLongtitude());
				obj.put("time",items[index].getTime());
				obj.put("pm25",items[index].getPm());
				_array.put(obj);
	    	   }
			 
			json_entity.put("data", _array);
			json_entity.put("token", TOKEN);
			json_entity.put("request_id", request_id);
			StringEntity _entity = new StringEntity(json_entity.toString());
			
			HttpPost request = new HttpPost(MULTI_UPLOAD_URL);
			request.setEntity(_entity);
			HttpResponse response = new DefaultHttpClient().execute(request);
			
			if(response.getStatusLine().getStatusCode() == 200){
				String _response = EntityUtils.toString(response.getEntity());
				JSONObject response_json = new JSONObject(_response);
				Log.i("RESPONSE_ENTITY", _response);
				return response_json.getString("status");
			}
			}catch(JSONException e){
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		return "error";
	}

}
