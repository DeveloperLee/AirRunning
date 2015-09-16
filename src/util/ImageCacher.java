package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;


public class ImageCacher {
	
	public static void cacheUserAvatar(Bitmap bitmap, String path, String pic_name){
		
		File file = new File(path, pic_name+".jpeg");
		
		if(!file.exists()){
			try {
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
