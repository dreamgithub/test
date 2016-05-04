
    /**  
    * @Title: ResouceServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2016年1月15日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lovebaby.dao.JdbcDao;
import com.lovebaby.pojo.FenyeData;

/**
    * @ClassName: ResouceServiceImpl
    * @Description: 
    * @author likai
    * @date 2016年1月15日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="resourcesService")
public class ResourceServiceImpl implements ResourcesService {
	@Autowired
	private JdbcDao jdbcDao;
	private static Logger log=Logger.getLogger(ResourceServiceImpl.class.getName());

	
	public ResourceServiceImpl() {
		super();
		try {
			Properties prop = new Properties();
			InputStream fis=this.getClass().getResourceAsStream("/log4j.properties");
			prop.load(fis);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/* 
	    * @Description:获取配置文件
	    * 
	    * @param:
	    *
	    */
	public Properties getProp(String file) {
		Properties prop = new Properties();
		InputStream fis=this.getClass().getResourceAsStream(file);
		try {
			prop.load(fis);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//将属性文件流装载到Properties对象中 
		return prop;
	}
	
	
	/* 
	* @Description: 获取资源列表
	* 
	* @param fenyeData
	* @return
	* @see com.lovebaby.service.ResoucesService#getResouce(com.lovebaby.pojo.FenyeData)
	*/

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getResouce(FenyeData fenyeData) throws Exception {
		try {
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("sourcepath");
			String resource=prop.getProperty("url")+"/"+prop.getProperty("resource");
			Map<String, Object> map=new HashMap<>();
			String sql;
			Object[] params;
			//获取数据列表
			sql="SELECT *, CONCAT(?,pic) as 'ftpimage', CONCAT(?,url) as 'resource'  from resources WHERE tid=? and type='0' ORDER BY createDate desc LIMIT ?,?";
			params=new Object[]{
					headimgae,
					resource,
					fenyeData.getTid(),
					fenyeData.getPage_num(),
					fenyeData.getPage_size()
			};
			List result1=jdbcDao.queryForList(sql, params);
			//获取数据列表
			sql="SELECT *, CONCAT(?,pic) as 'ftpimage', CONCAT(?,url) as 'resource'  from resources WHERE tid=? and type='1' ORDER BY createDate desc LIMIT ?,?";
			params=new Object[]{
					headimgae,
					"",
					fenyeData.getTid(),
					fenyeData.getPage_num(),
					fenyeData.getPage_size()
			};
			List result2=jdbcDao.queryForList(sql, params);
			result1.addAll(result2);
			//获取数据总数
			sql="select count(*) from resources WHERE tid=?";
			params=new Object[]{
					fenyeData.getTid()
			};
			int count=jdbcDao.queryForInt(sql, params);
			map.put("count", count);
			map.put("result", result1);
			
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 获取资源分组列表
	    * 
	    * @param fenyeData
	    * @return
	    * @throws Exception
	    * @see com.lovebaby.service.ResourcesService#getResouceGroup(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getResouceGroup(FenyeData fenyeData) throws Exception {
		try {
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("sourcepath");
			Map<String, Object> map=new HashMap<>();
			String sql;
			Object[] params;
			//获取数据列表
			sql="SELECT *, CONCAT(?,pic) as 'ftpimage' from resource_group ORDER BY date desc LIMIT ?,?";
			params=new Object[]{
					headimgae,
					fenyeData.getPage_num(),
					fenyeData.getPage_size()
			};
			List result=jdbcDao.queryForList(sql, params);
			//获取数据总数
			sql="SELECT count(*) from resource_group";
			int count=jdbcDao.queryForInt(sql, null);
			map.put("count", count);
			map.put("result", result);
			
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 获取广告图片
	    * 
	    * @param fenyeData
	    * @return
	    * @throws Exception
	    * @see com.lovebaby.service.ResourcesService#getAdvertisements(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getAdvertisements(FenyeData fenyeData) throws Exception {
		try {
			Properties prop=getProp("/ftp.properties");
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("advertisement");
			Map<String, Object> map=new HashMap<>();
			String sql;
			Object[] params;
			//获取数据列表
			sql="SELECT *, CONCAT(?,pic) as 'ftpimage' from advertisement ORDER BY date desc LIMIT 0,7";
			params=new Object[]{
					ftpurl
			};
			List result=jdbcDao.queryForList(sql, params);
			map.put("result", result);
			
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

}
