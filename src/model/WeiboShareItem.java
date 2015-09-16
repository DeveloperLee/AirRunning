package model;

import android.graphics.Bitmap;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;

public class WeiboShareItem {
	

	private TextObject txt;
	private ImageObject img;
	private WebpageObject web;
	private MusicObject music;
	private VideoObject video;
	private VoiceObject voice;
	
	public WeiboShareItem(){
		
	}
	
	public void setShareText(String text){
		txt = new TextObject();
		txt.text = text;
	}
	
	public void setShareImage(Bitmap bitmap){
		img = new ImageObject();
		img.setImageObject(bitmap);
	}
	
	
	public TextObject getTxt() {
		return txt;
	}

	public ImageObject getImg() {
		return img;
	}

	public WebpageObject getWeb() {
		return web;
	}

	public MusicObject getMusic() {
		return music;
	}

	public VideoObject getVideo() {
		return video;
	}

	public VoiceObject getVoice() {
		return voice;
	}
	

}
