package util;

import model.WeiboShareItem;
import android.app.Activity;
import android.os.Bundle;
import api.ThirdPartyConstants;

import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;


/**
 * This class is a util class for sharing messages to 
 * SINA WEIBO
 * @author Developer-Lee
 *
 */
public class WeiboShareUtils {
	
	private IWeiboShareAPI shareAPI = null;
	
	private Activity parent;
	
	
	public WeiboShareUtils(Activity parent){
		
		this.parent = parent;
		
		shareAPI = WeiboShareSDK.createWeiboAPI(parent, ThirdPartyConstants.WEIBO_APPKEY);
		shareAPI.registerApp();
		
	}
	
	
	
	/**
	 * Share a message to Sina Weibo.
	 * @param item -- The object that holds the shared resources 
	 *             --  such as text, image, url and etc.
	 */
	public void shareMessage(WeiboShareItem item){
		
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		
		weiboMessage.textObject = item.getTxt();
		weiboMessage.imageObject = item.getImg();
		
		SendMultiMessageToWeiboRequest sRequest = new SendMultiMessageToWeiboRequest();
		sRequest.transaction = String.valueOf(System.currentTimeMillis());
		sRequest.multiMessage = weiboMessage;
		
		AuthInfo authInfo = new AuthInfo(parent, ThirdPartyConstants.WEIBO_APPKEY,
				ThirdPartyConstants.WEIBO_REDIRECT,ThirdPartyConstants.SCOPE);
		Oauth2AccessToken aToken = AccessTokenKeeper.readAccessToken(parent);
		String token = "";
		
		if(aToken != null){
			token = aToken.getToken();
		}
		
		shareAPI.sendRequest(parent, sRequest, authInfo, token, new WeiboAuthListener(){

			@Override
			public void onCancel() {
				
			}

			@Override
			public void onComplete(Bundle arg0) {
				 Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(arg0); //Newly get token
                 AccessTokenKeeper.writeAccessToken(parent, newToken);
			}

			@Override
			public void onWeiboException(WeiboException arg0) {
				
			}
		});
				
	}
	

}
