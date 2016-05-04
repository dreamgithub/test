
    /**  
    * @Title: AddNoticeService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月11日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.Map;

import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Notice;

/**
    * @ClassName: AddNoticeService
    * @Description: 
    * @author likai
    * @date 2015年11月11日
    *
    */

public interface NoticeService {
	Map<String, Object> addNotice(Notice notice) throws Exception;
	Map<String, Object> getPublishNotice(FenyeData fenyeData) throws Exception;
	Map<String, Object> getReadNotice(FenyeData fenyeData) throws Exception;
	Map<String, Object> getUnreadNotice(FenyeData fenyeData) throws Exception;
	Map<String, Object> readNotice(FenyeData fenyeData) throws Exception;
	Map<String, Object> getSchoolType(FenyeData fenyeData) throws Exception;
	Map<String, Object> getSchools(FenyeData fenyeData) throws Exception;
	Map<String, Object> getClasses(FenyeData fenyeData) throws Exception;
	Map<String, Object> getSchool(FenyeData fenyeData) throws Exception;
	Map<String, Object> getUnreadNoticeNum(FenyeData fenyeData) throws Exception;
	Map<String, Object> getUnreadNoticeData(FenyeData fenyeData) throws Exception;
	Map<String, Object> getNoticeList(FenyeData fenyeData) throws Exception;
	Map<String, Object> getNoticeReadInfo(FenyeData fenyeData) throws Exception;
	Map<String, Object> noticeToTop(FenyeData fenyeData) throws Exception;
	Map<String, Object> getNoticeToTopList(FenyeData fenyeData) throws Exception;
	Map<String, Object> updateNotice(Notice notice) throws Exception;
	Map<String, Object> deleteNotice(FenyeData fenyeData) throws Exception;
}
