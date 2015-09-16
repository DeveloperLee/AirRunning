package activity;

import java.util.List;

import util.ImagePicker;

import model.TipItem;
import model.WeatherItem;

import com.rondo.airrunning.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherTipsActivity extends Activity {
	
	private ImageButton btn_back;
	private TextView[] indexs = new TextView[5];
	private TextView[] description = new TextView[5];
	private ImageView pm_level;
	private ImageView[] pm_number = new ImageView[3];
	
	private int[] ind_resid = new int[]{R.id.cloth_text,R.id.washcar_text,
			R.id.fever_text,R.id.sport_text,R.id.ultra_text};
	private int[] des_resid = new int[]{R.id.cloth_des,R.id.washcar_des,
			R.id.fever_des,R.id.sport_des,R.id.ultra_des};
	private int[] pm_num_resid = new int[]{R.id.num1,R.id.num2,R.id.num3};
	
	private WeatherItem today;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_indexs);
		today = (WeatherItem) getIntent().getSerializableExtra("TODAY");
		initViews();
		setUpListeners();
		fillData();
	}
	
	private void initViews() {
		btn_back = (ImageButton) findViewById(R.id.tip_down);
		pm_level = (ImageView) findViewById(R.id.pm_level);
		for(int i=0;i<indexs.length;i++){
			indexs[i] = (TextView) findViewById(ind_resid[i]);
			description[i] = (TextView) findViewById(des_resid[i]);
		}
		for(int i=0;i<pm_num_resid.length;i++){
			pm_number[i] = (ImageView) findViewById(pm_num_resid[i]);
		}
	}


	private void setUpListeners() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WeatherTipsActivity.this,MainActivity.class);
				intent.putExtra("FRAGMENT_NUM", 0);
				startActivity(intent);
				WeatherTipsActivity.this.overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
			}
		});
	}
	
    /**
     * Fill the loaded data into their coordinate UI widgets
     */
	private void fillData() {
		List<TipItem> tips = today.getTips();
		for(int i=0;i<5;i++){
			indexs[i].setText(tips.get(i).getContent());
			description[i].setText(tips.get(i).getDescription());
		}
		String pm = today.getPm();
		pm_level.setBackgroundResource(ImagePicker.getInstance().getPMLevelResId(pm));
		int[] numbers_id = ImagePicker.getInstance().getPmNumberResId(pm);
		for(int j=0;j<numbers_id.length;j++){
			pm_number[j].setBackgroundResource(numbers_id[j]);
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	

}
