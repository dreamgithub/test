
    /**  
    * @Title: ManergerMembersService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年12月31日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.Map;

import com.lovebaby.pojo.Babies;
import com.lovebaby.pojo.Classes;
import com.lovebaby.pojo.FenyeData;

/**
    * @ClassName: ManergerMembersService
    * @Description: 
    * @author likai
    * @date 2015年12月31日
    *
    */

public interface ManergerMembersService {
	Map<String, Object> deleteSchools(FenyeData fenyeData) throws Exception;
	Map<String, Object> deleteClass(FenyeData fenyeData) throws Exception;
	Map<String, Object> updateClassInfo(Classes classes) throws Exception;
	Map<String, Object> getschoolMembers(FenyeData fenyeData) throws Exception;
	Map<String, Object> deleteMember(FenyeData fenyeData) throws Exception;
	Map<String, Object> getClassMembers(FenyeData fenyeData) throws Exception;
	Map<String, Object> deleteBabies(FenyeData fenyeData) throws Exception;
	Map<String, Object> moveBabies(FenyeData fenyeData) throws Exception;
	Map<String, Object> getDanweiList(FenyeData fenyeData) throws Exception;
	Map<String, Object> getClasses(FenyeData fenyeData) throws Exception;
	Map<String, Object> getBabies(FenyeData fenyeData) throws Exception;
	Map<String, Object> getBabyParents(Babies babies) throws Exception;
	Map<String, Object> addAdvice(FenyeData fenyeData) throws Exception;
}
