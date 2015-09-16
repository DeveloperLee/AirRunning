package net;

import java.lang.ref.WeakReference;

import util.ImageCacher;

import com.rondo.airrunning.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap> {
	
	private final WeakReference<ImageView> img;
	
	private Context context;
	
	private String uid;
	
	public BitmapDownloadTask(Context context,ImageView img){
		this.img = new WeakReference<ImageView>(img);
		this.context = context;
	}

	@Override
	protected Bitmap doInBackground(String... arg0) {
		uid = arg0[1];
		return ImageDownloader.downloadImage(context,arg0[0],arg0[1]);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(Bitmap bitmap){
		if(this.isCancelled() || bitmap == null){
			bitmap = null;
			ImageView view = img.get();
			view.setImageResource(R.drawable.user_not_reg);
		}
		
		if(img!=null){
			ImageView view1 = img.get();
			if(bitmap!=null && view1 != null){
				view1.setBackgroundDrawable(new BitmapDrawable(bitmap));
				ImageCacher.cacheUserAvatar(bitmap, context.getPackageResourcePath(), uid);
			}
		}
	}

}
