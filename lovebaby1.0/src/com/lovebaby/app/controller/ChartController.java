
    /**  
    * @Title: ChartController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年12月18日
    * @version V1.0  
    */
    
package com.lovebaby.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lovebaby.pojo.FenyeData;
import com.lovebaby.service.ChartService;

/**
    * @ClassName: ChartController
    * @Description: 聊天管理
    * @author likai
    * @date 2015年12月18日
    *
    */
@Controller
@RequestMapping("/chart")
public class ChartController {
	@Autowired
	private ChartService chartService;
	
	private static Logger log=Logger.getLogger(ChartController.class.getName());

	public ChartController() {
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
	    * @Description:获取好友列表
	    * 
	    * @param:
	    * 
	    *  tid：机构为机构id，其他为校园id
	    *  uid:用户id
	    *  用户类型：type
	    */
	@RequestMapping("/getFriendsList")
	@ResponseBody
	public  Map<String, Object> getFriendsList(FenyeData fenyeData) {
		Map<String, Object> map=new HashMap<>();
		try {			
			map=chartService.getFriendsList(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
	}
	
	/* 
	    * @Description:搜索好友
	    * 
	    * @param:
	    * 
	    *  tid：机构为机构id，其他为校园id
	    *  uid:用户id
	    *  name:用户名
	    *  用户类型：type
	    */
	@RequestMapping("/searchFriends")
	@ResponseBody
	public  Map<String, Object> searchFriends(FenyeData fenyeData) {
		Map<String, Object> map=new HashMap<>();
		try {			
			map=chartService.searchFriends(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
	}
	
	/* 
	    * @Description:获取好友信息
	    * 
	    * @param:
	    * 
	    *  tid：机构为机构id，其他为校园id
	    *  id:联系人id
	    */
	@RequestMapping("/getFriendInfo")
	@ResponseBody
	public  Map<String, Object> getFriendInfo(FenyeData fenyeData) {
		Map<String, Object> map=new HashMap<>();
		try {			
			map=chartService.getFriendInfo(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
	}
}
