
    /**  
    * @Title: ChartService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年12月18日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.Map;

import com.lovebaby.pojo.FenyeData;

/**
    * @ClassName: ChartService
    * @Description: 
    * @author likai
    * @date 2015年12月18日
    *
    */

public interface ChartService {
	Map<String, Object> getFriendsList(FenyeData fenyeData) throws Exception;
	Map<String, Object> getFriendInfo(FenyeData fenyeData)throws Exception;
	 Map<String, Object> searchFriends(FenyeData fenyeData) throws Exception;
}
