
    /**  
    * @Title: AddImgae.java
    * @Package com.lovebaby.util
    * @Description: 
    * @author likai
    * @date 2015年12月11日
    * @version V1.0  
    */
    
package com.lovebaby.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

/**
    * @ClassName: AddImgae
    * @Description: 
    * @author likai
    * @date 2015年12月11日
    *
    */

public class AddImgae {
	private FTPClient ftp;
	
	
	
	/* 
	    * @Description:获取ftp链接
	    * 
	    * @param:
	    *
	    */
	public  boolean addImgae(MultipartFile file,String name,String address) {
		Properties prop =getProp();
		try {	
			//上传头像到ftp
			connect(prop.getProperty(address),prop.getProperty("ip"), Integer.parseInt(prop.getProperty("port")), prop.getProperty("username"), prop.getProperty("psw"));  
            ftp.enterLocalPassiveMode();
            InputStream input=file.getInputStream();
            ftp.storeFile(name,input);    
            input.close();   
            ftp.disconnect();   
            return true;
		} catch (Exception e) {
			return false;
		}
	}
	private  boolean connect(String path,String addr,int port,String username,String password) throws Exception {    

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
	private  Properties getProp() {
		Properties prop = new Properties();
		InputStream fis=this.getClass().getResourceAsStream("/ftp.properties");
		try {
			prop.load(fis);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//将属性文件流装载到Properties对象中 
		return prop;
	}
}
