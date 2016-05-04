
    /**  
    * @Title: AuthenticationController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月11日
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
import com.lovebaby.service.AuthenticationService;

/**
    * @ClassName: AuthenticationController
    * @Description: 认证审批
    * @author likai
    * @date 2015年11月11日
    *
    */
@Controller
@RequestMapping("/authentication")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authenticationService;
	private static Logger log=Logger.getLogger(AuthenticationController.class.getName());
	

	public AuthenticationController() {
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
	    * @Description:用户获取旗下认证列表
	    * 
	    * @param:
	    * 用户id：id
	    * 用户类型：type
	    * 用户所在单位id：tid
	    * 认证状态：status 0未审核，1已审核
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/getAuthenticationList")
	@ResponseBody
	public Map<String, Object> getAuthenticationList(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=authenticationService.getAuthenticationList(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
	}
	
	/*  
	    * @Description:用户获取旗下认证请求数
	    * 
	    * @param:
	    * 
	    * 用户id：id
	    * 用户所属单位id：tid
	    * 用户类型：type
	    */
	@RequestMapping("/getAuthenticationNum")
	@ResponseBody
	public Map<String, Object> getAuthenticationNum(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=authenticationService.getAuthenticationNum(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
	}
	
	/*  
	    * @Description:认证审核
	    * 
	    * @param:
	    * 审核记录id：aid
	    * 审核状态：status
	    */
	@RequestMapping("/authenticationCheck")
	@ResponseBody
	public Map<String, Object> AuthenticationCheck(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=authenticationService.AuthenticationCheck(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
	}
	
	/*  
	    * @Description:给家长添加宝宝
	    * 
	    * @param:
	    * 家长id：uid
	    * 宝宝id：bid(多个宝宝则为id拼接)
	    * 与宝宝关系:type
	    */
	@RequestMapping("/chooseBaby")
	@ResponseBody
	public Map<String, Object> chooseBaby(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			 map=authenticationService.chooseBaby(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
	}
}
