
    /**  
    * @Title: HistoriesServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月20日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lovebaby.dao.JdbcDao;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Histories;
import com.lovebaby.util.GetUuid;

/**
    * @ClassName: HistoriesServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年11月20日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="historiesService")
public class HistoriesServiceImpl implements HistoriesService {

	@Autowired
	private JdbcDao jdbcDao;
	private static Logger log=Logger.getLogger(HistoriesServiceImpl.class.getName());
	
	

	public HistoriesServiceImpl() {
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
	* @Description: 添加校史
	* 
	* @param fenyeData
	* @return
	* @see com.lovebaby.service.HistoriesService#addhistories(com.lovebaby.pojo.FenyeData)
	*/

	@Override
	public Map<String, Object> addhistories(Histories histories) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql="insert into histories(id,content,title,schoolId,publishDate,date,picname) values(?,?,?,?,?,?,?)";
			Object[] params=new Object[]{
					GetUuid.getUuid(),
					histories.getContent(),
					histories.getTitle(),
					histories.getSchoolId(),
					format.format(new Date()),
					histories.getDate(),
					histories.getPicname()
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	    /* 
	    * @Description: 获取校史
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.HistoriesService#gethistories(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> gethistories(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql=null;
			Object[] params=null;
			List result=null;
			int count=0;
			Properties prop=getProp("/ftp.properties");
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("sourcepath");
			sql="SELECT *,SUBSTR(date,6,2) as month,CONCAT(?,picname) as 'ftpimage'  from histories where schoolId=? and SUBSTR(date,1,4)=? order by date desc limit ?,?";
			params=new Object[]{
					ftpurl,
					fenyeData.getTid(),
					fenyeData.getStartDate(),
					fenyeData.getPage_num(),
					fenyeData.getPage_size()	
			};
			result=jdbcDao.queryForList(sql, params);
			
			//获取数据总数
			sql="SELECT count(*) from histories where schoolId=? and SUBSTR(date,1,4)=?";
			params=new Object[]{
					fenyeData.getTid(),
					fenyeData.getStartDate()
			};
			count=jdbcDao.queryForInt(sql, params);
		
			map.put("result", result);
			map.put("count", count);
			
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 获取校史内容
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.HistoriesService#gethistoriesContent(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> gethistoriesContent(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql="SELECT * from histories WHERE id=?";
			Object[] params=new Object[]{fenyeData.getId()};
			Map<String, Object> result=jdbcDao.queryForMap(sql, params);
			map.put("result", result);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}

	}


	
	    /* 
	    * @Description: 获取校史年份列表
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.HistoriesService#gethistoriesYears(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> gethistoriesYears(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql="SELECT SUBSTR(date,1,4) as year from histories where schoolId=? group by year ORDER BY date desc ";
			Object[] params=new Object[]{fenyeData.getId()};
			List result=jdbcDao.queryForList(sql, params);
			map.put("result", result);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}

	}


	
	    /* 
	    * @Description: 获取网页展示校史
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.HistoriesService#readhistoriesContent(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> readhistories(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql="SELECT id,title,SUBSTR(date,1,4) as year,SUBSTR(date,6,2) as month from histories where schoolId=? ORDER BY date desc LIMIT 0,21";
			Object[] params=new Object[]{fenyeData.getId()};
			List result=jdbcDao.queryForList(sql, params);
			map.put("result", result);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}

	}

	
	    /* 
	    * @Description: 获取发布校史列表
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.HistoriesService#getPublishHistory(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getPublishHistory(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql=null;
			Object[] params=null;
			List result=null;
			int count=0;
			Properties prop=getProp("/ftp.properties");
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("sourcepath");
			sql="SELECT *,CONCAT(?,picname) as 'ftpimage'  from histories where schoolId=?  order by publishDate desc limit ?,?";
			params=new Object[]{
					ftpurl,
					fenyeData.getId(),
					fenyeData.getPage_num(),
					fenyeData.getPage_size()	
			};
			result=jdbcDao.queryForList(sql, params);
			
			//获取数据总数
			sql="SELECT count(*) from histories where schoolId=?";
			params=new Object[]{
					fenyeData.getTid()
			};
			count=jdbcDao.queryForInt(sql, params);
		
			map.put("result", result);
			map.put("count", count);
			
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	    /* 
	    * @Description: 删除校史
	    * 
	    * @param histories
	    * @return
	    * @see com.lovebaby.service.HistoriesService#deleteHistories(com.lovebaby.pojo.Histories)
	    */
	    
	@Override
	public Map<String, Object> deleteHistories(Histories histories) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql="DELETE FROM histories where id=?";
			Object[] params=new Object[]{histories.getId()};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	    /* 
	    * @Description: 修改校史
	    * 
	    * @param histories
	    * @return
	    * @see com.lovebaby.service.HistoriesService#updateHistories(com.lovebaby.pojo.Histories)
	    */
	    
	@Override
	public Map<String, Object> updateHistories(Histories histories) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql="UPDATE histories set content=?,title=?,publishDate=?,date=?,picname=? where id=?";
			Object[] params=new Object[]{
					histories.getContent(),
					histories.getTitle(),
					format.format(new Date()),
					histories.getDate(),
					histories.getPicname(),
					histories.getId()
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}
	
	
}
