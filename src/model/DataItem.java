package model;

public class DataItem {
	
	private double latitude;
	private double longtitude;
	private String time;
	private double pm;
	
	public DataItem(double latitude, double longtitude, String time, double pm) {
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.time = time;
		this.pm = pm;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public double getPm() {
		return pm;
	}
	public void setPm(double pm) {
		this.pm = pm;
	}

	

}
