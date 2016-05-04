
    /**  
    * @Title: AuthenticationService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月16日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.Map;

import com.lovebaby.pojo.FenyeData;

/**
    * @ClassName: AuthenticationService
    * @Description: 
    * @author likai
    * @date 2015年11月16日
    *
    */

public interface AuthenticationService {
	Map<String, Object> getAuthenticationList(FenyeData fenyeData) throws Exception;
	Map<String, Object> AuthenticationCheck(FenyeData fenyeData)throws Exception;
	Map<String, Object> chooseBaby(FenyeData fenyeData)throws Exception;
	Map<String, Object> getAuthenticationNum(FenyeData fenyeData)throws Exception;
}
