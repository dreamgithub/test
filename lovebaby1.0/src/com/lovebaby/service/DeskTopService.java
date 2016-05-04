
    /**  
    * @Title: DeskTopService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年12月14日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.Map;

import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Picture;

/**
    * @ClassName: DeskTopService
    * @Description: 
    * @author likai
    * @date 2015年12月14日
    *
    */

public interface DeskTopService {
	 Map<String, Object>desktop(FenyeData fenyeData)throws Exception;
	 Map<String, Object> theme(FenyeData fenyeData) throws Exception;
	 Map<String, Object> chooseTheme(FenyeData fenyeData)throws Exception;
	 Map<String, Object> deleteTheme(FenyeData fenyeData)throws Exception;
	 Map<String, Object> uploadTheme(Picture pic)throws Exception;
	 Map<String, Object> deskTopTools(FenyeData fenyeData)throws Exception;
	 Map<String, Object> delFromDeskTop(FenyeData fenyeData)throws Exception;
	 Map<String, Object> addToDeskTop(FenyeData fenyeData)throws Exception;
}
