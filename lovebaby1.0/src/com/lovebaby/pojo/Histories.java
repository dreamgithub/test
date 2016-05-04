
    /**  
    * @Title: Histories.java
    * @Package com.lovebaby.pojo
    * @Description: 
    * @author likai
    * @date 2015年11月20日
    * @version V1.0  
    */
    
package com.lovebaby.pojo;


    /**
    * @ClassName: Histories
    * @Description: 
    * @author likai
    * @date 2015年11月20日
    *
    */

public class Histories {
	private String id;
	private String content;
	private String title;
	private String schoolId;
	private String publishDate;//发布日期
	private String date;//校史日期
	private String picname;
	
	public String getPicname() {
		return picname;
	}
	public void setPicname(String picname) {
		this.picname = picname;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	
	
}
