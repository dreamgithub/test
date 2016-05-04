
    /**  
    * @Title: Organization.java
    * @Package com.lovebaby.pojo
    * @Description: 
    * @author likai
    * @date 2015年11月13日
    * @version V1.0  
    */
    
package com.lovebaby.pojo;


    /**
    * @ClassName: Organization
    * @Description: 
    * @author likai
    * @date 2015年11月13日
    *
    */

public class Organization {
	private String id;
	private String name;
	private String area;
	private String createDate;
	private String updateDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	
}
