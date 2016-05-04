
    /**  
    * @Title: Notice.java
    * @Package com.lovebaby.pojo
    * @Description: 
    * @author likai
    * @date 2015年11月11日
    * @version V1.0  
    */
    
package com.lovebaby.pojo;

import java.util.List;

/**
    * @ClassName: Notice
    * @Description: 
    * @author likai
    * @date 2015年11月11日
    *
    */
@SuppressWarnings("rawtypes")
public class Notice {
	private String id;//公告id
	private String title;//公告标题
	private String content;//公告内容
	private String to_type;//公告接收者类型
	private String from_id;//公告发送者所属单位id
	private String from_type;//公告发送者所属单位类型
	private String publishDate;//公告发送时间
	private String toids;//公告接收者id数组
	private String uid;//发送人id
	private String contentType;//发送人id
	private String picname;
	private List  pics;
	private String toTop="0";
	private String textContent;
	
	public String getToTop() {
		return toTop;
	}
	public void setToTop(String toTop) {
		this.toTop = toTop;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public String getPicname() {
		return picname;
	}
	public void setPicname(String picname) {
		this.picname = picname;
	}
	public List getPics() {
		return pics;
	}
	public void setPics(List pics) {
		this.pics = pics;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTo_type() {
		return to_type;
	}
	public void setTo_type(String to_type) {
		this.to_type = to_type;
	}
	public String getFrom_id() {
		return from_id;
	}
	public void setFrom_id(String from_id) {
		this.from_id = from_id;
	}
	public String getFrom_type() {
		return from_type;
	}
	public void setFrom_type(String from_type) {
		this.from_type = from_type;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	
	    
	public String getToids() {
		return toids;
	}
	public void setToids(String toids) {
		this.toids = toids;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	    /* 
	    * @Description: 
	    * 
	    * @return
	    * @see java.lang.Object#toString()
	    */
	    
	@Override
	public String toString() {
		return "Notice [id=" + id + ", title=" + title + ", content=" + content + ", to_type=" + to_type + ", from_id="
				+ from_id + ", from_type=" + from_type + ", publishDate=" + publishDate + ", toids=" + toids + ", uid="
				+ uid + ", contentType=" + contentType + ", picname=" + picname + ", pics=" + pics + ", toTop=" + toTop
				+ ", textContent=" + textContent + "]";
	}
	
	
	
	
}
