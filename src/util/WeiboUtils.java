package util;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import api.ThirdPartyConstants;

public class WeiboUtils implements BaseSNSUtils{
	
	private Activity parent;
	
	private AuthInfo mAuthInfo;
	
	private Oauth2AccessToken mAccessToken;
	
	private SsoHandler mSsoHandler;
	
	private WeiboUserUtils userUtils;
	
	/**
	 * Construtor of the class
	 * @param context -- activity context
	 */
	public WeiboUtils(Activity parent){
		
		this.parent = parent;
		
		mAuthInfo = new AuthInfo(parent,ThirdPartyConstants.WEIBO_APPKEY,ThirdPartyConstants.WEIBO_REDIRECT
				,ThirdPartyConstants.SCOPE);
		
		mSsoHandler = new SsoHandler(parent,mAuthInfo);
		
		userUtils = new WeiboUserUtils(parent);
		
	}
	
	/**
	 * Send a request to weibo server to retrieve the access token
	 */
	@Override
	public void authorize(){
		mSsoHandler.authorize(new AuthListener());
	}
	
	
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
			
		}

		@Override
		public void onComplete(Bundle bundle) {
			
			mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
			if(mAccessToken.isSessionValid()){
			   AccessTokenKeeper.writeAccessToken(parent, mAccessToken);
			   userUtils.getUserInfo();
			}else{
			   String code = bundle.getString("code");
			   Log.e("AUTH", "AUTH_FAILED Code:"+code);
			}
			
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Log.e("AUTH", "AUTH_ERROR : "+e.getMessage());
		}
		
	}

}
