
    /**  
    * @Title: GetPermissionService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2016年1月19日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.List;

import com.lovebaby.pojo.User;

/**
    * @ClassName: GetPermissionService
    * @Description: 
    * @author likai
    * @date 2016年1月19日
    *
    */

@SuppressWarnings("rawtypes")
public interface GetPermissionService {
	boolean getPermission(String sql,Object[] params);
	boolean getPermission(String sql,Object[] params,List list,String val);
	boolean getPermission(String sql,Object[] params,String to,String val);
	boolean getPermission(List list,User user,String id);
}
