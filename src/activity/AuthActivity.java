package activity;

import model.WeiboShareItem;
import util.WeiboShareUtils;
import util.WeiboUtils;

import com.rondo.airrunning.R;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class AuthActivity extends Activity {
	
	private ImageButton auth_btn, share_btn;
	
	private WeiboUtils wb;
	private WeiboShareUtils share;
	
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_auth);
		
		wb = new WeiboUtils(this);
		share = new WeiboShareUtils(this);
		
		initViews();
		setUpListeners();
 
	}
	
	
	private void initViews(){
		auth_btn = (ImageButton) findViewById(R.id.auth_btn);
		share_btn = (ImageButton) findViewById(R.id.share_btn);
	}
	
	private void setUpListeners(){
		auth_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				wb.authorize();
			}
		});
		
		share_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WeiboShareItem item = new WeiboShareItem();
				item.setShareText("I'm using Air Running Android V1.5 #GetEveryBodyRunning");
				item.setShareImage(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
				share.shareMessage(item);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
	

}
