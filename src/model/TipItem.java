package model;

import java.io.Serializable;

public class TipItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String type;
	private String content;
	private String description;
	
	public TipItem(){
		
	}
	
	public TipItem(String type,String content,String description){
		this.type = type;
		this.content = content;
		this.description = description;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
