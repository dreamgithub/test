
    /**  
    * @Title: CheckBabyController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月23日
    * @version V1.0  
    */
    
package com.lovebaby.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lovebaby.pojo.Attendance;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.service.CheckBabyService;
import com.lovebaby.util.GetUuid;

/**
    * @ClassName: CheckBabyController
    * @Description: 宝宝考勤
    * 
    * @author likai
    * @date 2015年11月23日
    *
    */
@Controller
@RequestMapping("appCheckBaby")
public class CheckBabyController {
	@Autowired
	private CheckBabyService checkBabyService;
	private FTPClient ftp;	
	private static Logger log=Logger.getLogger(CheckBabyController.class.getName());

	public CheckBabyController() {
		super();
		try {
			Properties prop = new Properties();
			InputStream fis=this.getClass().getResourceAsStream("/log4j.properties");
			prop.load(fis);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*  
	    * @Description:获取考勤宝宝列表
	    * 
	    * @param:
	    * 班级id：id
	    * 考勤日期：date
	    */
	@RequestMapping("/getCheckBabies")
	@ResponseBody
	public Map<String, Object> getCheckBabies(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=checkBabyService.getCheckBabies(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
	}
	
	/*  
	    * @Description:添加，修改考勤记录
	    * 
	    * @param:
	    * 
	    * 班级id：classid
	    * 实到人数：comenum
	    * 未到人数：absentnum
	    * 总人数：sum
	    * 考勤日期：checkdate
	    * 考勤宝宝数据：data(为json数组字符串{{"bid":"1","state":"0"}},0未到，1已到)
	    * 考勤老师：teacher
	    * 图片文件：file
	    * 
	    */
	@RequestMapping("/addCheckRecord")
	@ResponseBody
	public Map<String, Object> addCheckRecord(Attendance attendance,MultipartFile file) {
		Map<String, Object> map=new HashMap<>();
		try {
			if (file!=null) {
				Properties prop =getProp();
				String filename=file.getOriginalFilename();
				String name=GetUuid.getUuid()+filename.substring(filename.lastIndexOf("."));
				attendance.setPic(name);
				//上传图片到ftp
				try {
					connect(prop.getProperty("checkaddress"),prop.getProperty("ip"), Integer.parseInt(prop.getProperty("port")), prop.getProperty("username"), prop.getProperty("psw"));
					 ftp.enterLocalPassiveMode();
			         InputStream input=file.getInputStream();
			         ftp.storeFile(name,input);    
			         input.close();   
			         ftp.disconnect();   
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
			}
			
			//添加考勤记录
			map=checkBabyService.addCheckRecord(attendance);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		
		return map;
	}
	
	/*  
	    * @Description:班级获取月度考勤记录表
	    * 
	    * @param:
	    * 
	    * 班级id：id
	    * 日期：date（年-月）
	    */
	@RequestMapping("/getClassMonCheckRecord")
	@ResponseBody
	public Map<String, Object> getClassMonCheckRecord(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=checkBabyService.getClassMonCheckRecord(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/*  
	    * @Description:获取日考勤记录表
	    * 
	    * @param:
	    * 
	    * 班级id：id
	    * 日期：date（年-月-日）
	    */
	@RequestMapping("/getDayCheckRecord")
	@ResponseBody
	public Map<String, Object> getDayCheckRecord(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=checkBabyService.getDayCheckRecord(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/*  
	    * @Description:获取宝宝月度考勤列表
	    * 
	    * @param:
	    * 
	    * 班级id：id
	    * 宝宝id：bid
	    * 日期：date（年-月）
	    */
	@RequestMapping("/getBabyMonCheckRecord")
	@ResponseBody
	public Map<String, Object> getBabyMonCheckRecord(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=checkBabyService.getBabyMonCheckRecord(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/* 
	    * @Description:获取ftp链接
	    * 
	    * @param:
	    *
	    */
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
	public Properties getProp() {
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
