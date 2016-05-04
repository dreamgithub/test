package com.lovebaby.pojo;

import java.util.List;
@SuppressWarnings("rawtypes")
public class User {
	private String id;
    private String telephone;
    private String realName;
    private String headImage;
    private String type;//用户类型
    private String tid;//所属单位id
    private String videoUser;//视屏账户
    private String videoPwd;
    private String createDate;
    private String loginDate;
    private String babyId;
    private String password;
    private String babyrelate;
    private String times;//登陆次数
    private String ip;//客户端ip
    private String registerType;//注册客户端类型
    private String phonetype;//登录手机类型
    private String token;//手机识别号
    private String loginarea;//登陆地点
    private String sex;//性别
    private String birthday;//生日
    private String ftpurl;//ftp地址
    private String chartToken;
    private String backimage;
    private String status;
	private List schools;
    private List danwei;
    private List classes;
    
    
	public List getClasses() {
		return classes;
	}
	public void setClasses(List classes) {
		this.classes = classes;
	}
	public List getSchools() {
		return schools;
	}
	public void setSchools(List schools) {
		this.schools = schools;
	}
	public List getDanwei() {
		return danwei;
	}
	public void setDanwei(List danwei) {
		this.danwei = danwei;
	}
	public String getChartToken() {
		return chartToken;
	}
	public void setChartToken(String chartToken) {
		this.chartToken = chartToken;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getLoginarea() {
		return loginarea;
	}
	public void setLoginarea(String loginarea) {
		this.loginarea = loginarea;
	}

	public String getPhonetype() {
		return phonetype;
	}
	public void setPhonetype(String phonetype) {
		this.phonetype = phonetype;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRegisterType() {
		return registerType;
	}
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
	public String getFtpurl() {
		return ftpurl;
	}
	public void setFtpurl(String ftpurl) {
		this.ftpurl = ftpurl;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getBackimage() {
		return backimage;
	}
	public void setBackimage(String backimage) {
		this.backimage = backimage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBabyId() {
		return babyId;
	}
	public void setBabyId(String babyId) {
		this.babyId = babyId;
	}
	public String getVideoUser() {
		return videoUser;
	}
	public void setVideoUser(String videoUser) {
		this.videoUser = videoUser;
	}
	public String getVideoPwd() {
		return videoPwd;
	}
	public void setVideoPwd(String videoPwd) {
		this.videoPwd = videoPwd;
	}
	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getBabyrelate() {
		return babyrelate;
	}
	public void setBabyrelate(String babyrelate) {
		this.babyrelate = babyrelate;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	
	    /* 
	    * @Description: 
	    * 
	    * @return
	    * @see java.lang.Object#toString()
	    */
	    
	@Override
	public String toString() {
		return "User [id=" + id + ", telephone=" + telephone + ", realName=" + realName + ", headImage=" + headImage
				+ ", type=" + type + ", tid=" + tid + ", videoUser=" + videoUser + ", videoPwd=" + videoPwd
				+ ", createDate=" + createDate + ", loginDate=" + loginDate + ", babyId=" + babyId + ", password="
				+ password + ", babyrelate=" + babyrelate + ", times=" + times + ", ip=" + ip + ", registerType="
				+ registerType + ", phonetype=" + phonetype + ", token=" + token + ", loginarea=" + loginarea + ", sex="
				+ sex + ", birthday=" + birthday + ", ftpurl=" + ftpurl + ", chartToken=" + chartToken + ", backimage="
				+ backimage + ", status=" + status + ", schools=" + schools + ", danwei=" + danwei + ", classes="
				+ classes + "]";
	}
	
	  
	
   
    
}
