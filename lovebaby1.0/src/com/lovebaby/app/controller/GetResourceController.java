
    /**  
    * @Title: GetResourceController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年12月31日
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
import com.lovebaby.service.ResourcesService;

/**
    * @ClassName: GetResourceController
    * @Description: 获取育儿故事，音乐，广告等资源
    * @author likai
    * @date 2015年12月31日
    *
    */
@Controller
@RequestMapping("/resource")
public class GetResourceController {
	
	@Autowired
	private ResourcesService resourcesService;
	private static Logger log=Logger.getLogger(GetResourceController.class.getName());
	
	
	
	
	
	public GetResourceController() {
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
	    * @Description:获取资源分组列表
	    * 
	    * @param:
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/getResouceGroup")
	@ResponseBody
	public Map<String, Object> getResouceGroup(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=resourcesService.getResouceGroup(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		
		return map;
	}

	/*  
	    * @Description:获取资源
	    * 
	    * @param:
	    * 资源分组id：tid
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/getResouce")
	@ResponseBody
	public Map<String, Object> getResouce(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=resourcesService.getResouce(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		
		return map;
	}
	
	/*  
	    * @Description:获取广告列表
	    * 
	    * @param:
	    * 资源分组id：tid
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/getAdvertisements")
	@ResponseBody
	public Map<String, Object> getAdvertisements(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=resourcesService.getAdvertisements(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		
		return map;
	}
}
