package model;

public class ARLocation {
	
	private double latitude;
	private double longtitude;
	private String time;
	private String addr;
	private String city;
	
	public ARLocation(double latitude, double longtitude, String time) {
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.time = time;
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
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

}
