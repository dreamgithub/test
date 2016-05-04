
    /**  
    * @Title: ResoucesService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2016年1月15日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.Map;

import com.lovebaby.pojo.FenyeData;

/**
    * @ClassName: ResoucesService
    * @Description: 
    * @author likai
    * @date 2016年1月15日
    *
    */

public interface ResourcesService {
	Map<String, Object> getResouce(FenyeData fenyeData) throws Exception;
	Map<String, Object> getResouceGroup(FenyeData fenyeData) throws Exception;
	Map<String, Object> getAdvertisements(FenyeData fenyeData)throws Exception;
}
