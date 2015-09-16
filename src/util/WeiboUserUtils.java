package util;

import model.WeiboUser;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import android.app.Activity;
import api.ThirdPartyConstants;
import api.UsersAPI;

public class WeiboUserUtils {
	
	private CacheUtils cache;
	
	private Oauth2AccessToken mToken;
	
	private UsersAPI mUsersAPI;
	
	public WeiboUserUtils(Activity activity){
		
		mToken = AccessTokenKeeper.readAccessToken(activity);
		
		mUsersAPI = new UsersAPI(activity,ThirdPartyConstants.WEIBO_APPKEY,mToken);
		
		cache = new CacheUtils(activity);
		
	}
	
	public void getUserInfo(){
		long uid = Long.parseLong(mToken.getUid());
		mUsersAPI.show(uid, mListener);
	}
	
	private RequestListener mListener = new RequestListener(){

		@Override
		public void onComplete(String response) {
			if(!response.equals("")){
				WeiboUser user = WeiboUser.parse(response);
				if(user != null){
					cache.cacheWeiboUser(user);
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			
		}
		
	};

}
