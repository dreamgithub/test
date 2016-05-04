
    /**  
    * @Title: ApplyRoleService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月13日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.Map;

import com.lovebaby.pojo.Authentication;
import com.lovebaby.pojo.Babies;
import com.lovebaby.pojo.Classes;
import com.lovebaby.pojo.FenyeData;

/**
    * @ClassName: ApplyRoleService
    * @Description: 
    * @author likai
    * @date 2015年11月13日
    *
    */

public interface ApplyRoleService {
	Map<String, Object> getTypeList(FenyeData fenyeData) throws Exception;
	Map<String, Object> addAuthentication(Authentication authentication) throws Exception;
	Map<String, Object> search(FenyeData fenyeData)throws Exception;
	Map<String, Object> addClasses(Classes classes)throws Exception;
	Map<String, Object> addBabies(Babies babies)throws Exception;
}
