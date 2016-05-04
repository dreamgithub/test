
    /**  
    * @Title: Speech.java
    * @Package com.lovebaby.dao
    * @Description: 
    * @author likai
    * @date 2015年11月27日
    * @version V1.0  
    */
    
package com.lovebaby.pojo;

import java.util.ArrayList;
import java.util.List;

/**
    * @ClassName: Speech
    * @Description: 
    * @author likai
    * @date 2015年11月27日
    *
    */
@SuppressWarnings("rawtypes")
public class Speech {
	private String id;
	private String mid;
	private String content;
	private String publishDate;
	private List pics=new ArrayList<>();
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public List getPics() {
		return pics;
	}
	public void setPics(List pics) {
		this.pics = pics;
	}
	
	
	
}
