
    /**  
    * @Title: HistoriesController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月20日
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
import com.lovebaby.service.HistoriesService;

/**
    * @ClassName: HistoriesController
    * @Description: 校史管理
    * @author likai
    * @date 2015年11月20日
    *
    */
@Controller
@RequestMapping("/apphistories")
public class HistoriesController {
	
	@Autowired
	private HistoriesService historiesService;
	private static Logger log=Logger.getLogger(HistoriesController.class.getName());
	
	public HistoriesController() {
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
	    * @Description:获取校史年份列表
	    * 
	    * @param:
	    * 学校id:id
	    */
	@RequestMapping("/gethistoriesYears")
	@ResponseBody
	public Map<String, Object> gethistoriesYears(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=historiesService.gethistoriesYears(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
	}
	
	
	/*  
	    * @Description:获取校史
	    * 
	    * @param:
	    * 学校id：tid
	    * 校史年份：startDate
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/gethistories")
	@ResponseBody
	public Map<String, Object> gethistories(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=historiesService.gethistories(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
	}
	
	/*  
	    * @Description:获取校史内容
	    * 
	    * @param:
	    * 
	    * 校史id：id
	    */
	@RequestMapping("/gethistoriesContent")
	@ResponseBody
	public Map<String, Object> gethistoriesContent(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=historiesService.gethistories(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
	}
}
