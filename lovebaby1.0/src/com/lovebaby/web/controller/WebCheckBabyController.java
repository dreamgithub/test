
    /**  
    * @Title: WebCheckBabyController.java
    * @Package com.lovebaby.web.controller
    * @Description: 
    * @author likai
    * @date 2016年1月18日
    * @version V1.0  
    */
    
package com.lovebaby.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.lovebaby.app.controller.CheckBabyController;
import com.lovebaby.excepttion.WebException;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.PermissionCheck;
import com.lovebaby.pojo.User;
import com.lovebaby.service.CheckBabyService;
import com.lovebaby.service.GetPermissionService;
import com.lovebaby.service.ManergerMembersService;
import com.lovebaby.service.NoticeService;

/**
    * @ClassName: WebCheckBabyController
    * @Description: 
    * @author likai
    * @date 2016年1月18日
    *
    */
@Controller
@RequestMapping("webCheckBaby")
@SuppressWarnings("rawtypes")
public class WebCheckBabyController extends PermissionCheck {
	@Autowired
	private GetPermissionService getPermissionService;
	@Autowired
	private CheckBabyService checkBabyService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private ManergerMembersService manergerMembersService;
	private static Logger log=Logger.getLogger(CheckBabyController.class.getName());

	public WebCheckBabyController() {
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
	    * @Description:考勤管理
	    * 
	    * @param:
	    */

	@SuppressWarnings({ "unchecked"})
	@RequestMapping("/check")
	public String check(FenyeData fenyeData,HttpServletRequest request) throws Exception {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			if (user.getType().equals("2")||user.getType().equals("3")) {
				//园务和园长获取班级列表
				Map<String, Object> school=(Map<String, Object>) user.getSchools().get(0);
				String schoolid=(String) school.get("id");
				fenyeData.setSid(schoolid);
				fenyeData.setPage_num(0);
				fenyeData.setPage_size(1000000);
				map=noticeService.getClasses(fenyeData);
			}else if (user.getType().equals("4")) {
				//老师获取班级列表
				fenyeData.setId(user.getId());
				map=manergerMembersService.getClasses(fenyeData);
			}
			request.setAttribute("result", map);
			return "check/check";
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}
	}
	
	
	/*  
	    * @Description:获取考勤宝宝列表
	    * 
	    * @param:
	    * 班级id：id
	    * 考勤日期：date
	    */
	/*@RequestMapping("/getCheckBabies")
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
	}*/
	
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
	/*@RequestMapping("/addCheckRecord")
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
	}*/
	
	/*  
	    * @Description:班级获取考勤记录表
	    * 
	    * @param:
	    * 
	    * 班级id：id
	    */
	@RequestMapping("/getClassCheckRecord")
	public String getClassCheckRecord(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map< String, Object> map=new HashMap<>();
		try {	
			User user=getUser(request);
			List classes=user.getClasses();
			//操作权限判断
			if (classes.toString().contains(fenyeData.getId())) {				
				map=checkBabyService.getClassCheckRecord(fenyeData);
				request.setAttribute("result", map);
				return "check/classCheckRecord";
			}else {
				String ip = getIRealIPAddr(request);
				log.error("非法访问，没有操作权限！   ip:"+ip+",tel="+user.getTelephone());
				throw new WebException();
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}				
	}
	/*  
	    * @Description:获取日考勤记录表
	    * 
	    * @param:
	    * 
	    * 考勤记录id：id
	    */
	@RequestMapping("/getDayCheckRecordMx")
	public String getDayCheckRecordMx(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map< String, Object> map=new HashMap<>();
		try {			
			User user=getUser(request);
			//操作权限判断
			String sql="SELECT classid from attendance WHERE id=?";
			Object[] params=new Object[]{fenyeData.getId()};			
			if (getPermissionService.getPermission(sql, params,user.getClasses() , "classid")) {			
				map=checkBabyService.getDayCheckRecordMx(fenyeData);
				request.setAttribute("result", map);
				request.setAttribute("date", fenyeData.getDate());
				request.setAttribute("checkdate", fenyeData.getDate().substring(0, 7));
				return "check/dayCheckRecord";
			}else {
				String ip = getIRealIPAddr(request);
				log.error("非法访问，没有操作权限！   ip:"+ip+",tel="+user.getTelephone());
				throw new WebException();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}				
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
	public String getBabyMonCheckRecord(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map< String, Object> map=new HashMap<>();
		try {			
			User user=getUser(request);
			List classes=user.getClasses();
			//操作权限判断
			if (classes.toString().contains(fenyeData.getId())) {				
				map=checkBabyService.getBabyMonCheckRecord(fenyeData);
				request.setAttribute("result", map);
				request.setAttribute("checkdate", fenyeData.getDate());
				return "check/babyMonCheck";
				
			}else {
				String ip = getIRealIPAddr(request);
				log.error("非法访问，没有操作权限！   ip:"+ip+",tel="+user.getTelephone());
				throw new WebException();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}				
	}
	
	
	

}
