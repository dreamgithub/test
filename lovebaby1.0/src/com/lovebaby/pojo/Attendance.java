
    /**  
    * @Title: Attendance.java
    * @Package com.lovebaby.pojo
    * @Description: 
    * @author likai
    * @date 2015年11月23日
    * @version V1.0  
    */
    
package com.lovebaby.pojo;


    /**
    * @ClassName: Attendance
    * @Description: 考勤记录
    * @author likai
    * @date 2015年11月23日
    *
    */

public class Attendance {
	
	private String id;
	private String classid;
	private String pic;
	private String date;
	private String checkdate;
	private int comenum;
	private int absentnum;
	private int sum;
	private String teacher;
	private String data;
	
	
	public String getCheckdate() {
		return checkdate;
	}
	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassid() {
		return classid;
	}
	public void setClassid(String classid) {
		this.classid = classid;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
	public int getComenum() {
		return comenum;
	}
	public void setComenum(int comenum) {
		this.comenum = comenum;
	}
	public int getAbsentnum() {
		return absentnum;
	}
	public void setAbsentnum(int absentnum) {
		this.absentnum = absentnum;
	}
	
	
}
