package model;

import java.util.List;

public class RunObj {
	
	private List<ARLocation> locations;
	private String time,startAddr,finishAddr,duration;
	private double distance;
	
	public RunObj(){
		
	}
	
	public RunObj(List<ARLocation> locations){
		this.locations = locations;
	}
	
	public List<ARLocation> getLocations() {
		return locations;
	}
	public void setLocations(List<ARLocation> locations) {
		this.locations = locations;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getStartAddr() {
		return startAddr;
	}
	public void setStartAddr(String startAddr) {
		this.startAddr = startAddr;
	}
	public String getFinishAddr() {
		return finishAddr;
	}
	public void setFinishAddr(String finishAddr) {
		this.finishAddr = finishAddr;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

}
