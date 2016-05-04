
    /**  
    * @Title: LoginService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月9日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.List;
import java.util.Map;

import com.lovebaby.pojo.User;

/**
    * @ClassName: LoginService
    * @Description: 
    * @author likai
    * @date 2015年11月9日
    *
    */
@SuppressWarnings("rawtypes")
public interface LoginService {
	List check(User user) throws Exception;
	void updateInfo(User user) throws Exception;
	Map<String, Object> getUserInfo(User user) throws Exception;
	Map<String, Object> addUser(User user) throws Exception;
	Map<String, Object> upadtePortrait(User user) throws Exception;
	Map<String, Object> upadteUserInfo(User user) throws Exception;
	Map<String, Object> upadtePsw(User user) throws Exception;
	List getUser(User user,String oldpsw) throws Exception;
	Map<String, Object>bingVideoAccount(User user) throws Exception;
}
