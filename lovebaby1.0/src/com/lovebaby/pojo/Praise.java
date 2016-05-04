
    /**  
    * @Title: Praise.java
    * @Package com.lovebaby.pojo
    * @Description: 
    * @author likai
    * @date 2015年11月30日
    * @version V1.0  
    */
    
package com.lovebaby.pojo;


    /**
    * @ClassName: Praise
    * @Description: 点赞
    * @author likai
    * @date 2015年11月30日
    *
    */

public class Praise {
	
	private String id;
	private String mid;//点赞人id
	private String tid;//说说id
	private String publishDate;
	private String username;//点赞人名称
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
