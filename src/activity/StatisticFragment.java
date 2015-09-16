package activity;

import java.util.ArrayList;

import model.WeiboShareItem;

import util.AccessTokenKeeper;
import util.WeiboShareUtils;
import util.WeiboUtils;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.rondo.airrunning.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import activity.HomeFragment.SlideMenuListener;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class StatisticFragment extends Fragment {
	
	private CombinedChart mWeeklyChart;
	private LineChart mPMChart;
	
	private ImageButton menu_btn,share_btn;
	
	private SlideMenuListener mListener;
	
	private String[] mTime = new String[]{"8AM","12PM","4PM","8PM",
			"12AM","4AM"};
	private String[] mDay = new String[]{"周一","周二","周三",
			"周四","周五","周六"};
	private static final int mCount = 6;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.layout_stat, container, false);
		initViews(contentView);
		initWeeklyChart(contentView);
//		initDailyChart(contentView);
		initPMChart(contentView);
		return contentView;
	}
	
	private void initViews(View contentView){
		menu_btn = (ImageButton) contentView.findViewById(R.id.stat_btn_menu);
		menu_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.menuBtnClicked(SlideMenuListener.LEFT);
			}
		});
		
		share_btn = (ImageButton) contentView.findViewById(R.id.btn_share_stat);
		share_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				shareToSNS();
			}
		});
	}

	private void initWeeklyChart(View contentView){
		mWeeklyChart = (CombinedChart) contentView.findViewById(R.id.weekly_chart);
		mWeeklyChart.setDrawGridBackground(false);
		mWeeklyChart.setDescription("");
		mWeeklyChart.setDrawBarShadow(false);
		mWeeklyChart.setDrawOrder(new DrawOrder[]{
				DrawOrder.BAR,DrawOrder.LINE
		});
		mWeeklyChart.setBackgroundResource(R.drawable.fine_day_light);
		
		YAxis rightAxis =  mWeeklyChart.getAxisRight();
		rightAxis.setEnabled(false);
		YAxis leftAxis = mWeeklyChart.getAxisLeft();
		leftAxis.setDrawGridLines(false);
		leftAxis.setTextColor(Color.WHITE);
		
		XAxis _x = mWeeklyChart.getXAxis();
		_x.setPosition(XAxis.XAxisPosition.BOTTOM);
		_x.setSpaceBetweenLabels(6);
		_x.setTextColor(Color.WHITE);
		
		CombinedData _data = new CombinedData(mDay);
		_data.setData(generateLineData());
		_data.setData(generateBarData());
		mWeeklyChart.setOnChartValueSelectedListener(new ChartListener());
		mWeeklyChart.setData(_data);
		mWeeklyChart.animateXY(600, 1000);
	
	}
	
	private void initPMChart(View contentView){
		mPMChart = (LineChart) contentView.findViewById(R.id.pm_chart);
		mPMChart.setDescription("");
		mPMChart.setDrawGridBackground(false);
		mPMChart.setDragEnabled(true);
		mPMChart.setTouchEnabled(false);
		mPMChart.setBackgroundResource(R.drawable.fine_day_light);
		mPMChart.setData(getData(mCount,100));
		mPMChart.getAxisRight().setEnabled(false);
		mPMChart.getXAxis().setTextColor(Color.WHITE);
		mPMChart.getAxisLeft().setTextColor(Color.WHITE);
		mPMChart.getXAxis().setDrawGridLines(false);
		mPMChart.animateY(800);
	}
	
	private LineData generateLineData(){
		LineData d = new LineData();
		ArrayList<Entry> entries = new ArrayList<Entry>();
		
		 for(int index = 0; index < mCount; index++){
	        entries.add(new Entry(getRandom(15, 10), index));
		 }
	        LineDataSet set = new LineDataSet(entries, "日跑量");
	        set.setColor(Color.WHITE);
	        set.setLineWidth(1f);
	        set.setCircleColor(Color.WHITE);
	        set.setCircleSize(3f);
	        set.setDrawCubic(false);
	        set.setDrawValues(true);
	        set.setValueTextSize(10f);
	        set.setValueTextColor(Color.WHITE);
	        set.setAxisDependency(YAxis.AxisDependency.LEFT);
	        d.addDataSet(set);
		return d;
		
	}
	
	private BarData generateBarData(){
		 BarData d = new BarData();
	     ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
	     for (int index = 0; index < mCount; index++){
	    	float _data = getRandom(15, 30);
	        entries.add(new BarEntry(_data, index));
	     }
	        BarDataSet set = new BarDataSet(entries, "周跑量");
	        set.setDrawValues(false);
//	        set.setColor(Color.rgb(61, 87, 122));
	        d.addDataSet(set);
	        set.setAxisDependency(YAxis.AxisDependency.LEFT);
	        return d;
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	/**
	 * Share the statistics to third-party social networking websites
	 * If user has already authorized then using the existing access token.
	 * If no user is authorized before then we ask the user to authorize their
	 * account.
	 */
	private void shareToSNS(){
		
		Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(getActivity());
		
		if(mAccessToken == null || !mAccessToken.isSessionValid() 
				|| mAccessToken.getUid().equals("")){
			WeiboUtils wbUtils = new WeiboUtils(getActivity());
			wbUtils.authorize();
		}else{
			WeiboShareUtils shareUtils = new WeiboShareUtils(getActivity());
			WeiboShareItem item = new WeiboShareItem();
			item.setShareText(getSharedText());
			item.setShareImage(BitmapFactory.decodeResource(getActivity().getResources(),
					R.drawable.mojitip_16));
			shareUtils.shareMessage(item);
		}
	}
	
    private float getRandom(float range, float startsfrom) {
	  return (float) (Math.random() * range) + startsfrom;
    }
    
    //For test for the time being
    private String getSharedText(){
    	return "TEST FOR SHARE INFORMATION FROM AIR RUNNING ANDROID v1.0 #GetEverybodyRunning";
    }
    
    
    private LineData getData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mTime[i % 12]);
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "PM2.5指数");

        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        return data;
    }
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{ 
			mListener = (SlideMenuListener) activity;
		}catch(ClassCastException e){
			Log.e("INTERFACE_ERROR", "The parent activity must implement SlideMenuListener");
		}
	}
    
    class ChartListener implements OnChartValueSelectedListener{

		@Override
		public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
			mPMChart.setData(getData(mCount,250));
			mPMChart.animateY(800);
		}

		@Override
		public void onNothingSelected() {
			
		}
    	
    }

}
