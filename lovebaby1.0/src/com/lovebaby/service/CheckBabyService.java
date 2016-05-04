
    /**  
    * @Title: CheckBabyService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月23日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.Map;

import com.lovebaby.pojo.Attendance;
import com.lovebaby.pojo.FenyeData;

/**
    * @ClassName: CheckBabyService
    * @Description: 
    * @author likai
    * @date 2015年11月23日
    *
    */

public interface CheckBabyService {
	 Map<String, Object> getCheckBabies(FenyeData fenyeData) throws Exception;
	 Map<String, Object> addCheckRecord(Attendance attendance) throws Exception;
	 Map<String, Object> getClassMonCheckRecord(FenyeData fenyeData) throws Exception;
	 Map<String, Object> getDayCheckRecord(FenyeData fenyeData)throws Exception;
	 Map<String, Object> getBabyMonCheckRecord(FenyeData fenyeData)throws Exception;
	 Map<String, Object> getClassCheckRecord(FenyeData fenyeData) throws Exception;
	 Map<String, Object> getDayCheckRecordMx(FenyeData fenyeData) throws Exception;
}
