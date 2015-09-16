package net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class ImageDownloader extends Thread{
	
	
	public static Bitmap downloadImage(Context context,String url,String cacheName){
		
		String cache_folder_path = context.getCacheDir().getAbsolutePath();
		Log.e("PATH", cache_folder_path);
		File file = new File(cache_folder_path+"/"+cacheName+".png");
		
		if(!file.exists()){
			
		  HttpClient client = new DefaultHttpClient();
		  
		  HttpGet request = new HttpGet(url);
		  
		  try {
			 HttpResponse response = client.execute(request);
			 
			 if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				final Bitmap bitmap = BitmapFactory.decodeStream(is);
				return bitmap;
			 }else{
				return null;
			 }
			 
		  }catch (ClientProtocolException e) {
			e.printStackTrace();
		  }catch (IOException e) {
		 	e.printStackTrace();
		  }
		}
		else{
		  try {
			FileInputStream fis = new FileInputStream(file);
			final Bitmap bitmap = BitmapFactory.decodeStream(fis);
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	   }
		
		return null;
	}

}