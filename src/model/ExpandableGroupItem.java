package model;

import java.util.ArrayList;
import java.util.List;

public class ExpandableGroupItem {
	
	private String platform;
	private int resId_unreg, resId_reg;
	private boolean isReg;
	
	private List<AbsUser> users;
	
	public ExpandableGroupItem(){
		
		users = new ArrayList<AbsUser>();
	}
	
	public ExpandableGroupItem(String platform,int resId_unreg, int resId_reg, boolean isReg){
		this.platform = platform;
		this.resId_reg = resId_reg;
		this.resId_unreg = resId_unreg;
		this.isReg = isReg;
		
		users = new ArrayList<AbsUser>();
	}
	
	
	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public boolean isReg() {
		return isReg;
	}
	
	public String getRegStatus(){
		if(isReg){
			return "已绑定";
		}else{
			return "未绑定";
		}
	}
	
 	public int getImageResourceId(){
		if(isReg){
			return resId_reg;
		}else{
			return resId_unreg;
		}
	}
 	
 	public void regUser(AbsUser user){
 		this.users.add(user);
 	}
 	
 	public List<AbsUser> getRegUsers(){
 		return this.users;
 	}

}
