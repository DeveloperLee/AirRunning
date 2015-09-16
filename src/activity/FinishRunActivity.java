package activity;

import model.WeiboShareItem;
import util.AccessTokenKeeper;
import util.BaseSNSUtils;
import util.WXUtils;
import util.WeiboShareUtils;
import util.WeiboUtils;

import com.rondo.airrunning.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class FinishRunActivity extends Activity {
	
	private ImageButton back;
	private TextView description,duration,distance,distance2,cal;
	private ImageButton wechat,weibo,timeline;
	private String dis,dur,calo,des;
	
	private BaseSNSUtils share_util;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_finish);
		loadData();
		initViews();
		setUpListeners();
	}
	
	private void loadData(){
		Intent data = getIntent();
		des = "本次跑步结束@"+data.getStringExtra("City");
		dis = String.valueOf(data.getDoubleExtra("Distance", 0d))+"Km";
		dur = data.getStringExtra("Duration");
		calo = String.valueOf(data.getDoubleExtra("Cal", 0))+"KCal";
		
	}
	
	private void initViews(){
		back = (ImageButton) findViewById(R.id.finish_back);
		description = (TextView) findViewById(R.id.finish_description);
		duration = (TextView) findViewById(R.id.finish_duration);
		distance = (TextView) findViewById(R.id.finish_distance);
		distance2 = (TextView) findViewById(R.id.distance);
		cal = (TextView) findViewById(R.id.finish_cal);
		wechat = (ImageButton) findViewById(R.id.share_to_wechat);
		weibo = (ImageButton) findViewById(R.id.share_to_weibo);
		timeline = (ImageButton) findViewById(R.id.share_to_timeline);
		
		description.setText(des);
		distance2.setText(dis);
		distance.setText(dis);
		duration.setText(dur);
		cal.setText(calo);
		
	}
	
	
	private void setUpListeners(){
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FinishRunActivity.this,MainActivity.class);
				intent.putExtra("FRAGMENT_NUM", 0);
				startActivity(intent);
				FinishRunActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		
		weibo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				shareToWeibo();
			}
		});
		
		wechat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				shareToWechat(false);
			}
		});
		
		timeline.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				shareToWechat(true);
			}
		});
		
		
	}
	
	
	/**
	 * Share the current accomplished run to Sina weibo 
	 */
	private void shareToWeibo(){
		
		Oauth2AccessToken aToken = AccessTokenKeeper.readAccessToken(this);
		if(aToken == null){
			share_util = new WeiboUtils(this);
			share_util.authorize();
		}
		WeiboShareUtils wb = new WeiboShareUtils(this);
		WeiboShareItem item = new WeiboShareItem();
		item.setShareText("我使用Air Running完成了一次跑步！总用时"+dur+",总距离"+dis+
					"消耗卡路里"+calo);
		item.setShareImage(BitmapFactory.decodeResource(getResources(), R.drawable.asd));
		wb.shareMessage(item);
	}
	
	/**
	 * Share the current accomplished run to wechat
	 * @param toTimeline -- if set to true, share to moments 
	 *                   -- if set to false, share to a chat session
	 */
	private void shareToWechat(boolean toTimeline){
		
		WXUtils wechat = new WXUtils(this);
		wechat.shareMessage("我使用Air Running完成了一次跑步！总用时"+dur+",总距离"+dis+
					"消耗卡路里"+calo, toTimeline);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	

}
