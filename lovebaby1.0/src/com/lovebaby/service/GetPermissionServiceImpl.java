
    /**  
    * @Title: GetPermissionServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2016年1月19日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lovebaby.dao.JdbcDao;
import com.lovebaby.pojo.User;

/**
    * @ClassName: GetPermissionServiceImpl
    * @Description: 
    * @author likai
    * @date 2016年1月19日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="getPermissionService")
public class GetPermissionServiceImpl implements GetPermissionService {
	@Autowired
	private JdbcDao jdbcDao;
	/* 
	* @Description: 权限验证
	* 
	* @param sql
	* @param params
	* @return
	* @see com.lovebaby.service.GetPermissionService#getPermission(java.lang.String, java.lang.Object[])
	*/

	@Override
	public boolean getPermission(String sql, Object[] params) {
		int count=jdbcDao.queryForInt(sql, params);
		if (count==0) {
			return false;
		}else {
			return true;
		}
	}

	
	    /* 
	    * @Description: 
	    * 
	    * @param sql
	    * @param params
	    * @param list 对比的对象
	    * @param val 对比的属性
	    * @return
	    * @see com.lovebaby.service.GetPermissionService#getPermission(java.lang.String, java.lang.Object[], java.util.List, java.lang.String)
	    */
	    
	@Override
	public boolean getPermission(String sql, Object[] params, List list, String val) {
		Map< String, Object> map= jdbcDao.queryForMap(sql, params);
		String data=(String) map.get(val);
		if (list.toString().contains(data)) {
			return true;
		}else {			
			return false;
		}

	}

	
	    /* 
	    * @Description: 
	    * 
	    * @param sql
	    * @param params
	    * @param to 对比对象
	    * @param val 对比属性
	    * @return
	    * @see com.lovebaby.service.GetPermissionService#getPermission(java.lang.String, java.lang.Object[], java.lang.String, java.lang.String)
	    */
	    
	@Override
	public boolean getPermission(String sql, Object[] params, String to, String val) {
		Map< String, Object> map= jdbcDao.queryForMap(sql, params);
		String data=(String) map.get(val);
		if (data.equals(to)) {
			return true;
		}else {			
			return false;
		}
	}


	
	    /* 
	    * @Description: 
	    * 
	    * @param sql
	    * @param params
	    * @param to
	    * @param val
	    * @param user
	    * @return
	    * @see com.lovebaby.service.GetPermissionService#getPermission(java.lang.String, java.lang.Object[], java.lang.String, java.lang.String, com.lovebaby.pojo.User)
	    */
	    
	@Override
	public boolean getPermission(List list,User user,String id) {
		String sql="SELECT tid,type from authentication WHERE id=?";
		Object[] params=new Object[]{
				id
		};
		Map< String, Object> map= jdbcDao.queryForMap(sql, params);
		String tid=(String) map.get("tid");
		String type=(String) map.get("type");		
		if (tid!=null&&!tid.equals("0")) {	
			if (type.equals("2")||type.equals("3")||type.equals("5")) {
				if (list.toString().contains(tid)) {
					return true;
				}else {			
					return false;
				}
			}else if (type.equals("4")) {
				@SuppressWarnings("unchecked")
				Map< String, Object> map1=(Map<String, Object>) list.get(0);
				String sid=(String) map1.get("tid");
				sql="SELECT id FROM classes WHERE schoolId=?";
				
				params=new Object[]{
						sid
				};
				List classes=jdbcDao.queryForList(sql, params);
				if (classes.toString().contains(tid)) {
					return true;
				}else {			
					return false;
				}
			}else {
				return false;
			}
		}else {
			if (user.getType().equals("6")) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	

}
