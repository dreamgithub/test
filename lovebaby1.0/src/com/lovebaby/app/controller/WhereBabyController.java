
    /**  
    * @Title: WhereBabyController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月24日
    * @version V1.0  
    */
    
package com.lovebaby.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lovebaby.pojo.FenyeData;

/**
    * @ClassName: WhereBabyController
    * @Description: 宝宝在哪儿
    * @author likai
    * @date 2015年11月24日
    *
    */
@Controller
@RequestMapping("appWhereBaby")
public class WhereBabyController {

	/*  
	    * @Description:获取宝宝位置
	    * 
	    * @param:
	    * 
	    * 宝宝id:bid
	    */
	@RequestMapping("/getBabyLocation")
	@ResponseBody
	public Map<String, Object> getBabyLocation(FenyeData fenyeData) {
		Map<String, Object> map=new HashMap<>();
		
			return map;
	}
	
}
