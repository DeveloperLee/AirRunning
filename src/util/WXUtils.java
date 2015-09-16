package util;

import android.content.Context;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXUtils implements BaseSNSUtils{
	
	private static final String APP_ID = "wx3bc20ea5203fc174";
	
	private IWXAPI api;
	
	private Context context;
	
	
	public WXUtils(Context context){
		this.context = context;
	}
    
	
	/**
	 * Register this application to wechat 
	 */
	@Override
	public void authorize() {
		
		api = WXAPIFactory.createWXAPI(context, APP_ID);
		
		api.registerApp(APP_ID);
	}
	
	
	/**
	 * Send message to wechat client, remember this method only 
	 * effective when the wechat client app is available on your
	 * smart phone
	 * @param title
	 * @param toTimeline
	 */
	public void shareMessage(String title,boolean toTimeline){
		
		authorize();
		
		WXTextObject send = new WXTextObject();
		send.text = title;
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = send;
		msg.title = title;
		msg.description = "From Air Running";
		
		SendMessageToWX.Req request = new SendMessageToWX.Req();
		if(toTimeline){
		  request.scene = SendMessageToWX.Req.WXSceneTimeline;   //To moments
		}else{ 
		  request.scene = SendMessageToWX.Req.WXSceneSession;  // To a chat session
		}
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = msg;
		api.sendReq(request);
		
	}

}
