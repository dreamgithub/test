
    /**  
    * @Title: HistoriesController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月20日
    * @version V1.0  
    */
    
package com.lovebaby.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lovebaby.excepttion.AppException;
import com.lovebaby.excepttion.WebException;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Histories;
import com.lovebaby.pojo.PermissionCheck;
import com.lovebaby.pojo.User;
import com.lovebaby.service.GetPermissionService;
import com.lovebaby.service.HistoriesService;

/**
    * @ClassName: HistoriesController
    * @Description: 校史管理
    * @author likai
    * @date 2015年11月20日
    *
    */
@Controller
@RequestMapping("/webhistories")
public class WebHistoriesController extends PermissionCheck{
	@Autowired
	private GetPermissionService getPermissionService;
	@Autowired
	private HistoriesService historiesService;
	private static Logger log=Logger.getLogger(WebHistoriesController.class.getName());
	

	public WebHistoriesController() {
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
	    * @Description:校史
	    * 
	    * @param:
	    *
	    */
	@RequestMapping("/histories")
	public String histories(Histories histories,HttpServletRequest request) {
		return "history/addhistory";
		
	}
	/* 
	    * @Description:查看校史
	    * 
	    * @param:
	    *
	    */
	@RequestMapping("/readhistories")
	public String readhistories(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			map=historiesService.readhistories(fenyeData);
			request.setAttribute("name", request.getParameter("name"));
			request.setAttribute("result", map);
			return "history/history";
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}		
	}
	/* 
	    * @Description:查看校史内容
	    * 
	    * @param:
	    * 
	    *	校史id：id
	    */
	@RequestMapping("/readhistoriescontent")
	public String readhistoriescontent(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			map=historiesService.gethistoriesContent(fenyeData);
			request.setAttribute("result", map);
			return "history/historycontent";
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}				
	}
	/* 
	    * @Description:上传校史
	    * 
	    * @param:
	    * 	
	    *  学校id：schoolId
	    *  校史标题：title
	    *  校史内容：content
	    *  校史日期：date
	    *  图片名称：picname
	    */
	@RequestMapping("/addhistories")
	@ResponseBody
	public Map<String, Object> addhistories(Histories histories,HttpServletRequest request) throws AppException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			@SuppressWarnings("unchecked")
			Map<String, Object> school=(Map<String, Object>) user.getSchools().get(0);
			String sid=(String) school.get("id");
			histories.setSchoolId(sid);
			map=historiesService.addhistories(histories);
			map.put("flag", "1");
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}			
	}
	
	
	/* 
	    * @Description:删除校史
	    * 
	    * @param:
	    * 
	    * 校史id：id
	    */
	@RequestMapping("/deleteHistories")
	@ResponseBody
	public Map<String, Object> deleteHistories(Histories histories,HttpServletRequest request) throws AppException {
		Map<String, Object> map=new HashMap<>();
		try {
			//权限判断
			User user=getUser(request);
			@SuppressWarnings("unchecked")
			Map<String, Object> school=(Map<String, Object>) user.getDanwei().get(0);
			String sid=(String) school.get("tid");
			String sql="SELECT count(*) from histories WHERE id=? AND schoolId=?";
			Object[] params=new Object[]{
					histories.getId(),
					sid
			};
			if (getPermissionService.getPermission(sql, params)) {				
				map=historiesService.deleteHistories(histories);
				map.put("flag", "1");
				return map;
			}else {
				String ip = getIRealIPAddr(request);
				log.error("非法访问，没有操作权限！   ip:"+ip+",tel="+user.getTelephone());
				throw new AppException();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}			
	}
	
	/* 
	    * @Description:跟新校史页面
	    * 
	    * @param:
	    * 
	    * 校史id：id
	    */
	@RequestMapping("/toUpdateHistories")
	public String toUpdateHistories(FenyeData fenyeData,HttpServletRequest request) throws AppException, WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			map=historiesService.gethistoriesContent(fenyeData);
			request.setAttribute("result", map);
			return "history/updatehistory";
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}				

	}
	
	

	/* 
	    * @Description:修改校史
	    * 
	    * @param:
	    * 	
	    *  校史id：id
	    *  校史标题：title
	    *  校史内容：content
	    *  校史日期：date
	    *  图片名称：picname
	    */
	@RequestMapping("/updateHistories")
	@ResponseBody
	public Map<String, Object> updateHistories(Histories histories,HttpServletRequest request) throws AppException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			@SuppressWarnings("unchecked")
			Map<String, Object> school=(Map<String, Object>) user.getSchools().get(0);
			String sid=(String) school.get("id");
			histories.setSchoolId(sid);
			map=historiesService.updateHistories(histories);
			map.put("flag", "1");
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}			
	}
	
	
	/* 
	    * @Description:我的发布
	    * 
	    * @param:
	    * 
	    *	学校id：id
	    */
	@RequestMapping("/getPublishHistory")
	public String getPublishHistory(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			@SuppressWarnings("unchecked")
			Map<String, Object> school=(Map<String, Object>) user.getSchools().get(0);
			String sid=(String) school.get("id");
			fenyeData.setId(sid);
			fenyeData.setPage_size(10000000);
			map=historiesService.getPublishHistory(fenyeData);
			request.setAttribute("result", map);
			return "history/mypublish";
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}				
	}
	
}
