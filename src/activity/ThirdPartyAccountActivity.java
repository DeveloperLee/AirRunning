package activity;

import java.util.ArrayList;
import java.util.List;

import net.BitmapDownloadTask;
import util.BaseSNSUtils;
import util.CacheUtils;
import util.ImagePicker;
import util.QQUtils;
import util.WXUtils;
import util.WeiboUtils;
import model.AbsUser;
import model.ExpandableGroupItem;
import model.WeiboUser;

import com.rondo.airrunning.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ThirdPartyAccountActivity extends Activity {
	
	private static final String TAG = "ThirdPartyAccountActivity";
	
	private ImageButton back_btn;
	private ExpandableListView list;
	
	private List<ExpandableGroupItem> group_items;
	
	private boolean[] regStatus = new boolean[3];
	
	private CacheUtils cache;
	private ImagePicker picker;
	private BaseSNSUtils auther;

 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_thirdp_account);
		super.onCreate(savedInstanceState);
		
		readIntent(getIntent());
		initData();
		initView();
		setUpListeners();
	}
 	
 	private void readIntent(Intent intent){
 		regStatus[0] = intent.getBooleanExtra(SettingsFragment.WEIBO, false);
 		regStatus[1] = intent.getBooleanExtra(SettingsFragment.WEIXIN, false);
 		regStatus[2] = intent.getBooleanExtra(SettingsFragment.QQ, false);
 	}
	
	private void initView(){
		list = (ExpandableListView) findViewById(R.id.third_party_list);
		list.setAdapter(new AccountListListener(this));
		back_btn = (ImageButton) findViewById(R.id.account_back_btn);
	}
	
	private void initData(){
		group_items = new ArrayList<ExpandableGroupItem>();
		
		cache = new CacheUtils(this);
		
		picker = ImagePicker.getInstance();
		
		ExpandableGroupItem _weibo = new ExpandableGroupItem("新浪微博",R.drawable.icon_weibo_1,
				R.drawable.icon_weibo,regStatus[0]);
		ExpandableGroupItem _weixin = new ExpandableGroupItem("腾讯微信",R.drawable.icon_weixin_1,
				R.drawable.icon_weixin,regStatus[1]);
		ExpandableGroupItem _qq = new ExpandableGroupItem("腾讯QQ",R.drawable.icon_qq_1,
				R.drawable.icon_qq,regStatus[2]);
		
		group_items.add(_weibo);
		group_items.add(_weixin);
		group_items.add(_qq);
		
		AbsUser test0 = new AbsUser();
		test0.platform = AbsUser.WEIBO;
		
		AbsUser test1 = new AbsUser();
		test1.platform = AbsUser.WEIXIN;
		AbsUser test2 = new AbsUser();
		test2.platform = AbsUser.QQ;
		group_items.get(0).regUser(test0);
		group_items.get(1).regUser(test1);
		group_items.get(2).regUser(test2); //test
		
		if(regStatus[0]){
			WeiboUser user = cache.readCacheWeiboUser();
			group_items.get(0).regUser(user);
		}
		
	}
	
	private void setUpListeners(){
		back_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ThirdPartyAccountActivity.this,MainActivity.class);
				intent.putExtra("FRAGMENT_NUM", 3);
				startActivity(intent);
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
	
	class AccountListListener extends BaseExpandableListAdapter{
		
		private LayoutInflater inflater;
		
		public AccountListListener(Context context){
			inflater = LayoutInflater.from(context);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return group_items.get(groupPosition).getRegUsers().get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			
			  ImageButton user_head = null;
			  TextView name  = null;
			  convertView = inflater.inflate(R.layout.layout_ex_child, null);
			  name = (TextView) convertView.findViewById(R.id.username);
			  user_head = (ImageButton) convertView.findViewById(R.id.user_head);
			
		 	 final String platform = group_items.get(groupPosition).getRegUsers().get(childPosition).platform;
			
				  
			 if(childPosition != 0){
			    WeiboUser user = (WeiboUser) group_items.get(groupPosition).getRegUsers().get(childPosition);
				convertView.setBackgroundResource(picker.getPlatformBackground(platform));
				String[] params = new String[]{user.profile_image_url,user.id};
			    BitmapDownloadTask task = new BitmapDownloadTask(ThirdPartyAccountActivity.this,user_head);	
			    task.execute(params);
			    name.setText(user.screen_name);
			  }else{
				user_head.setOnClickListener(new View.OnClickListener() {
			       @Override
				   public void onClick(View v) {
			    	   
			    	   Log.i(TAG, String.valueOf(groupPosition));
			    	   
			    	   switch(groupPosition){
			    	      case 0:
			    	        auther = new WeiboUtils(ThirdPartyAccountActivity.this); 
			    	        break;
			    	      case 1:
			    	    	auther = new WXUtils(ThirdPartyAccountActivity.this);
			    	    	break;
			    	      case 2:
			    	    	auther = new QQUtils(ThirdPartyAccountActivity.this);
			    	    	break;
			    	   }
			    	   
			    	   auther.authorize();
	
				  }
				});
			  }
			
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return group_items.get(groupPosition).getRegUsers().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return group_items.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return group_items.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
		    GroupHolder holder = null;
		    if(convertView == null){
		    	holder = new GroupHolder();
		    	convertView = (View) inflater.inflate(R.layout.layout_ex_group, null);
		    	holder.icon = (ImageView) convertView.findViewById(R.id.icon);
		    	holder.name = (TextView) convertView.findViewById(R.id.name);
		    	holder.status = (TextView) convertView.findViewById(R.id.status);
		    	convertView.setTag(holder);
		    }else{
		    	holder = (GroupHolder) convertView.getTag();
		    }
		    
		    holder.icon.setImageResource(group_items.get(groupPosition).getImageResourceId());
	    	holder.name.setText(group_items.get(groupPosition).getPlatform());
	    	holder.status.setText(group_items.get(groupPosition).getRegStatus());
	    	
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		final class GroupHolder{
			ImageView icon;
			TextView name;
			TextView status;
		}
		
		final class ChildHolder{
			TextView name;
			TextView url;
		}
		
	}

}
