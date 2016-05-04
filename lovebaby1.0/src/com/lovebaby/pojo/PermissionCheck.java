
    /**  
    * @Title: PermissionCheck.java
    * @Package com.lovebaby.pojo
    * @Description: 
    * @author likai
    * @date 2016年1月19日
    * @version V1.0  
    */
    
package com.lovebaby.pojo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import net.sf.json.JSONObject;

/**
    * @ClassName: PermissionCheck
    * @Description: 
    * @author likai
    * @date 2016年1月19日
    *
    */

@SuppressWarnings("rawtypes")
public class PermissionCheck {
	public FTPClient ftp;	
	@SuppressWarnings("unchecked")
	public User getUser(HttpServletRequest request) {
		HttpSession ses=request.getSession();
		Map<String, Object> member=(Map<String, Object>) ses.getAttribute("member");
		Map<String, Object> result=(Map<String,Object>)member.get("result");
		JSONObject jsonObject = JSONObject.fromObject(result); 
		User user=(User) JSONObject.toBean(jsonObject, User.class);
		List schools=(List)member.get("schools");
		List danwei=(List)member.get("danwei");
		List classes=(List)member.get("classes");
		user.setSchools(schools);
		user.setDanwei(danwei);
		user.setClasses(classes);
		return user;
	}
	public String getIRealIPAddr(HttpServletRequest request) {     
		 String ip = request.getHeader("x-forwarded-for");   
		  if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))    {     
		    ip = request.getHeader("Proxy-Client-IP");  
		 }  
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)   || "null".equalsIgnoreCase(ip)) {    
		  ip = request.getHeader("WL-Proxy-Client-IP");  
		 }  
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)    || "null".equalsIgnoreCase(ip)) {  
		  ip = request.getRemoteAddr();   
		 }  
		 return ip;  
		}  
	/* 
	    * @Description:获取ftp链接
	    * 
	    * @param:
	    *
	    */
	public  boolean connect(String path,String addr,int port,String username,String password) throws Exception {    

		  boolean result = false;    
		  ftp = new FTPClient();    
		  int reply;    
		  ftp.connect(addr,port);    
		  ftp.login(username,password);  
		  ftp.setBufferSize(1024);
		  ftp.setControlEncoding("utf-8");
		  ftp.setFileType(FTPClient.BINARY_FILE_TYPE);    
		  reply = ftp.getReplyCode();    
		  if (!FTPReply.isPositiveCompletion(reply)) {    
		      ftp.disconnect();    
		      return result;    
		  }    
		  ftp.changeWorkingDirectory(path);    
		  result = true;
		  return result;    
		}   
	/* 
	    * @Description:获取配置文件
	    * 
	    * @param:
	    *
	    */
	public Properties getProp() {
		Properties prop = new Properties();
		InputStream fis=this.getClass().getResourceAsStream("/ftp.properties");
		try {
			prop.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//将属性文件流装载到Properties对象中 
		return prop;
	}
	
}
