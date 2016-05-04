
    /**  
    * @Title: Reply.java
    * @Package com.lovebaby.pojo
    * @Description: 
    * @author likai
    * @date 2015年11月30日
    * @version V1.0  
    */
    
package com.lovebaby.pojo;


    /**
    * @ClassName: Reply
    * @Description: 回复类
    * @author likai
    * @date 2015年11月30日
    *
    */

public class Reply {
	private String id;
	private String tid;//评论id
	private String toid;//回复接收人id
	private String fromid;//回复发送人id
	private String date;//回复时间
	private String status;//0未读，1已读
	private String toname;//发送人名称,无名称用账号
	private String fromname;
	private String content;//接收人名称
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getToid() {
		return toid;
	}
	public void setToid(String toid) {
		this.toid = toid;
	}
	public String getFromid() {
		return fromid;
	}
	public void setFromid(String fromid) {
		this.fromid = fromid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getToname() {
		return toname;
	}
	public void setToname(String toname) {
		this.toname = toname;
	}
	public String getFromname() {
		return fromname;
	}
	public void setFromname(String fromname) {
		this.fromname = fromname;
	}
	
	
	
}
