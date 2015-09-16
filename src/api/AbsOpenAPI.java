package api;

import android.content.Context;

import android.text.TextUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;

public abstract class AbsOpenAPI {
    private static final String TAG = AbsOpenAPI.class.getName();
    
    protected static final String API_SERVER       = "https://api.weibo.com/2";
    protected static final String HTTPMETHOD_POST  = "POST";
    protected static final String HTTPMETHOD_GET   = "GET";
    protected static final String KEY_ACCESS_TOKEN = "access_token";
    
    protected Oauth2AccessToken mAccessToken;
    protected Context mContext;
    protected String mAppKey;
    
    public AbsOpenAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        mContext = context;
        mAppKey = appKey;
        mAccessToken = accessToken;
    }

    protected void requestAsync(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        if (null == mAccessToken
                || TextUtils.isEmpty(url)
                || null == params
                || TextUtils.isEmpty(httpMethod)
                || null == listener) {
            LogUtil.e(TAG, "Argument error!");
            return;
        }
        
        params.put(KEY_ACCESS_TOKEN, mAccessToken.getToken());
        new AsyncWeiboRunner(mContext).requestAsync(url, params, httpMethod, listener);
    }
    
    protected String requestSync(String url, WeiboParameters params, String httpMethod) {
        if (null == mAccessToken
                || TextUtils.isEmpty(url)
                || null == params
                || TextUtils.isEmpty(httpMethod)) {
            LogUtil.e(TAG, "Argument error!");
            return "";
        }
        
        params.put(KEY_ACCESS_TOKEN, mAccessToken.getToken());
        return new AsyncWeiboRunner(mContext).request(url, params, httpMethod);
    }
}
