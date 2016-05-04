
    /**  
    * @Title: FenyeData.java
    * @Package com.lovebaby.pojo
    * @Description: 
    * @author likai
    * @date 2015年11月13日
    * @version V1.0  
    */
    
package com.lovebaby.pojo;


    /**
    * @ClassName: FenyeData
    * @Description: 
    * @author likai
    * @date 2015年11月13日
    *
    */

public class FenyeData {
	private String ftpurl;//ftp地址
	//用户信息
	private String id;//用户id
	private String type;//用户类型，1，2,3,4,5

    
    //公告信息
    private String nid;//公告id
    
    //审核记录
    private String aid;//审核id
    private String status;//审核状态
    
    //机构信息
    private String name;
	private String area;
	
	//校史
	private String content;//校史内容
	
	//考勤
	 private String date;//日期
	 private int absentnum=0;//未到次数
	 
    //查询条件
    private String searchType;//o组织，s学校，c班级
    private String val;//查询输入值
    private String uid;//申请人id
    private String bid;//宝宝id
    private String tid;//用户所在单位id
    private String oid;//组织id
    private String sid;//校园id
    private String cid;//班级id
    private String startDate;//起始日期
    private String endDate;//终止日期
    
    //分页信息
	private int page_size=15;//每页数据量
	private int page_num=0;//当前所在页
    private int page_total;//总页数
    
    private String pictype;//图片类型
    
	public String getPictype() {
		return pictype;
	}
	public void setPictype(String pictype) {
		this.pictype = pictype;
	}
	public String getFtpurl() {
		return ftpurl;
	}
	public void setFtpurl(String ftpurl) {
		this.ftpurl = ftpurl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPage_size() {
		return page_size;
	}
	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}
	public int getPage_num() {
		return page_num;
	}
	public void setPage_num(int page_num) {
		this.page_num = page_num;
	}
	public int getPage_total() {
		return page_total;
	}
	public void setPage_total(int page_total) {
		this.page_total = page_total;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getAbsentnum() {
		return absentnum;
	}
	public void setAbsentnum(int absentnum) {
		this.absentnum = absentnum;
	}
    
    
}
