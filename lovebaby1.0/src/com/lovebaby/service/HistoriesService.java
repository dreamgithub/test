
    /**  
    * @Title: HistoriesController.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月20日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.Map;

import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Histories;

/**
    * @ClassName: HistoriesController
    * @Description: 
    * @author likai
    * @date 2015年11月20日
    *
    */

public interface HistoriesService {
	Map<String, Object> gethistoriesYears(FenyeData fenyeData) throws Exception;
	Map<String, Object> addhistories(Histories histories) throws Exception;
	Map<String, Object> gethistories(FenyeData fenyeData)throws Exception;
	Map<String, Object> gethistoriesContent(FenyeData fenyeData)throws Exception;
	Map<String, Object> readhistories(FenyeData fenyeData)throws Exception;
	Map<String, Object> getPublishHistory(FenyeData fenyeData)throws Exception;
	Map<String, Object> deleteHistories(Histories histories)throws Exception;
	Map<String, Object> updateHistories(Histories histories)throws Exception;
}
