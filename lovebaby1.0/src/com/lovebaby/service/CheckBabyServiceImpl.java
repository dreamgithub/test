
    /**  
    * @Title: CheckBabyServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月23日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lovebaby.dao.JdbcDao;
import com.lovebaby.pojo.Attendance;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.util.GetUuid;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
    * @ClassName: CheckBabyServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年11月23日
    *
    */

@SuppressWarnings("rawtypes")
@Service(value="checkBabyService")
public class CheckBabyServiceImpl implements CheckBabyService {
	@Autowired
	private JdbcDao jdbcDao;
	private static Logger log=Logger.getLogger(CheckBabyServiceImpl.class.getName());


	public CheckBabyServiceImpl() {
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
    * @Description: 获取考勤宝宝列表
    * 
    * @param fenyeData
    * @return
    * @see com.lovebaby.service.CheckBabyService#getCheckBabies(com.lovebaby.pojo.FenyeData)
    */
    
	@Override
	public Map<String, Object> getCheckBabies(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			List result=null;
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("babyaddress");
			//判断今日是否已考勤
			String sql="SELECT count(*) FROM attendance WHERE checkdate=? and classid=?";
			Object[] params=new Object[]{
					fenyeData.getDate(),
					fenyeData.getId()
			};
			int count=jdbcDao.queryForInt(sql, params);
			if (count==0) {
				//今日没有考勤
				map.put("state", "0");
				//获取宝宝列表
				sql="SELECT *,CONCAT(?,pic) as 'ftpimage','0' as 'checkstate' from babies WHERE classid=? and state='1'";
				params=new Object[]{
						headimgae,
						fenyeData.getId()
				};
			}else{
				//今日已有考勤
				map.put("state", "1");
				//获取宝宝列表
				sql="SELECT t1.*,t2.state as 'checkstate',CONCAT(?,t1.pic) as 'ftpimage' from babies t1 RIGHT  JOIN (SELECT bid,state,tid from check_record WHERE tid=(SELECT id FROM attendance WHERE checkdate=? and classid=?)) t2 on t1.id=t2.bid";
				params=new Object[]{
						headimgae,
						fenyeData.getDate(),
						fenyeData.getId()
				};
			}
			result=jdbcDao.queryForList(sql, params);
			map.put("result", result);		
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}
	
	
	
	    /* 
	    * @Description: 添加考勤记录
	    * 
	    * @param attendance
	    * @param file
	    * @return
	    * @see com.lovebaby.service.CheckBabyService#addCheckRecord(com.lovebaby.pojo.Attendance, org.springframework.web.multipart.MultipartFile)
	    */
	    
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> addCheckRecord(Attendance attendance) throws Exception {
		try {
			SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map< String, Object> map=new HashMap<>();	
			//判断今日是否已考勤
			String sql="SELECT * FROM attendance WHERE checkdate=? and classid=?";
			Object[] params=new Object[]{
						attendance.getCheckdate(),
						attendance.getClassid()
				};
			List  records=jdbcDao.queryForList(sql, params);
			if (records.size()==0) {
				//没有当日考勤，插入新纪录
				//添加考勤记录
				sql="INSERT INTO attendance(id,classid,pic,date,comenum,absentnum,checkdate,teacher,sum) VALUES(?,?,?,?,?,?,?,?,?)";
				String id=GetUuid.getUuid();
				params=new Object[]{
						id,
						attendance.getClassid(),
						attendance.getPic(),
						format2.format(new Date()),
						attendance.getComenum(),
						attendance.getAbsentnum(),
						attendance.getCheckdate(),
						attendance.getTeacher(),
						attendance.getSum()
				};
				jdbcDao.update(sql, params);
				//添加考勤宝宝数据
				String data=attendance.getData();
				List<Map<String, Object>> datalist=(List<Map<String, Object>>) JSONArray.toList(JSONArray.fromObject(data), new HashMap<>(), new JsonConfig());
				sql="INSERT into check_record(id,bid,tid,date,state) VALUES(?,?,?,?,?)";
				List list=new ArrayList<>();
				for (int i = 0; i < datalist.size(); i++) {
					list.add(new Object[]{
							GetUuid.getUuid(),
							datalist.get(i).get("bid"),
							id,
							format2.format(new Date()),
							datalist.get(i).get("state")
							});
				}
				jdbcDao.batchBySimpleJdbcTemplate(sql, list);		
			}else {
				//已有当日考勤，覆盖旧的记录
				Map< String, Object> data1=(Map<String, Object>) records.get(0);
				String id=(String) data1.get("id");
				//跟新考勤记录
				sql="UPDATE attendance set pic=?,date=?,comenum=?,absentnum=?,checkdate=?,teacher=?,sum=? WHERE id=?";
				params=new Object[]{
						attendance.getPic(),
						format2.format(new Date()),
						attendance.getComenum(),
						attendance.getAbsentnum(),
						attendance.getCheckdate(),
						attendance.getTeacher(),
						attendance.getSum(),
						id
				};
				jdbcDao.update(sql, params);
				
				//删除原有考勤记录
				sql="DELETE from check_record where tid=?";
				params=new Object[]{
						id
				};
				jdbcDao.update(sql, params);
				
				//插入新的考勤记录
				String data=attendance.getData();
				List<Map<String, Object>> datalist=(List<Map<String, Object>>) JSONArray.toList(JSONArray.fromObject(data), new HashMap<>(), new JsonConfig());
				sql="INSERT into check_record(id,bid,tid,date,state) VALUES(?,?,?,?,?)";
				List list=new ArrayList<>();
				for (int i = 0; i < datalist.size(); i++) {
					list.add(new Object[]{
							GetUuid.getUuid(),
							datalist.get(i).get("bid"),
							id,
							format2.format(new Date()),
							datalist.get(i).get("state")
							});
				}
				jdbcDao.batchBySimpleJdbcTemplate(sql, list);	
			}
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	

    /* 
    * @Description: 班级获取月度考勤记录表
    * 
    * @param fenyeData
    * @return
    * @see com.lovebaby.service.CheckBabyService#getClassCheckRecord(com.lovebaby.pojo.FenyeData)
    */
    
@Override
public 	 Map<String, Object> getClassMonCheckRecord(FenyeData fenyeData) throws Exception {
	try {
		Map< String, Object> map=new HashMap<>();
		Properties prop=getProp("/ftp.properties");
		String headimgae=prop.getProperty("url")+"/"+prop.getProperty("checkaddress");
		//获取数据列表
		String sql="SELECT *,CONCAT(?,pic) as 'ftpimage' FROM attendance where classid=? and checkdate>=? AND checkdate<=? order by checkdate";
		Object[] params=new Object[]{
				headimgae,
				fenyeData.getId(),
				fenyeData.getDate(),
				fenyeData.getDate()+"-31",
		};
		List result=jdbcDao.queryForList(sql, params);
		map.put("result", result);
		return map;
	} catch (Exception e) {
		log.error(e.getMessage());
		throw new Exception();
	}
	
}



	
	    /* 
	    * @Description: 获取日考勤记录表
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.CheckBabyService#getDayCheckRecord(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getDayCheckRecord(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("babyaddress");
			//获取数据列表
			String sql="SELECT t1.*,t2.tid, t2.state as checkstate,CONCAT(?,pic) as 'ftpimage' from babies t1 RIGHT  JOIN (SELECT bid,state,tid from check_record WHERE tid=(SELECT id FROM attendance WHERE checkdate=? and classid=?)) t2 on t1.id=t2.bid";
			Object[] params=new Object[]{
					headimgae,
					fenyeData.getDate(),
					fenyeData.getId()
			};
			List result=jdbcDao.queryForList(sql, params);
			map.put("result", result);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}



	
	    /* 
	    * @Description: 获取宝宝月度考勤列表
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.CheckBabyService#getBabyMonCheckRecord(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getBabyMonCheckRecord(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("babyaddress");
			//获取数据列表
			String sql="SELECT t1.checkdate,t2.state FROM attendance t1 LEFT JOIN check_record t2 on t1.id=t2.tid where t1.classid=? and t1.checkdate>=? AND t1.checkdate<=? AND t2.bid=? order by t1.checkdate";
			Object[] params=new Object[]{
					fenyeData.getId(),
					fenyeData.getDate(),
					fenyeData.getDate()+"-31",
					fenyeData.getBid()
			};
			List result=jdbcDao.queryForList(sql, params);
			//获取宝宝信息
			sql="SELECT *,CONCAT(?,pic) as 'ftpimage' from babies WHERE id=?";
			params=new Object[]{
					headimgae,
					fenyeData.getBid()
			};
			Map<String, Object> baby=jdbcDao.queryForMap(sql, params);
			//获取考勤记录总天数数
			sql="SELECT count(*) from attendance where classid=? and checkdate>=? AND checkdate<=?";
			params=new Object[]{
					fenyeData.getId(),
					fenyeData.getDate(),
					fenyeData.getDate()+"-31"
			};
			int daycount=jdbcDao.queryForInt(sql, params);
			//获取宝宝缺勤总次数
			sql="SELECT count(*) FROM attendance t1 LEFT JOIN check_record t2 on t1.id=t2.tid where t1.classid=? and t1.checkdate>=? AND t1.checkdate<=? AND t2.bid=? and t2.state='0'";
			params=new Object[]{
					fenyeData.getId(),
					fenyeData.getDate(),
					fenyeData.getDate()+"-31",
					fenyeData.getBid()
			};
			int absentnum=jdbcDao.queryForInt(sql, params);
			//实到总次数
			int comenum=daycount-absentnum;
			map.put("result", result);
			map.put("baby", baby);
			map.put("daycount", daycount);
			map.put("absentnum", absentnum);
			map.put("comenum", comenum);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}



	
	    /* 
	    * @Description: 班级获取全部考勤记录
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.CheckBabyService#getClassCheckRecord(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getClassCheckRecord(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("checkaddress");
			//获取数据列表
			String sql="SELECT *,CONCAT(?,pic) as 'ftpimage' FROM attendance where classid=?  order by checkdate desc";
			Object[] params=new Object[]{
					headimgae,
					fenyeData.getId()
			};
			List result=jdbcDao.queryForList(sql, params);
			map.put("result", result);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}



	
	    /* 
	    * @Description: 获取日考勤记录明细
	    * 
	    * @param fenyeData
	    * @return
	    * @throws Exception
	    * @see com.lovebaby.service.CheckBabyService#getDayCheckRecordMx(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getDayCheckRecordMx(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("babyaddress");
			//获取数据列表
			String sql="SELECT t1.*,t2.tid, t2.state as checkstate,CONCAT(?,pic) as 'ftpimage' from babies t1 RIGHT  JOIN (SELECT bid,state,tid from check_record WHERE tid=?) t2 on t1.id=t2.bid";
			Object[] params=new Object[]{
					headimgae,
					fenyeData.getId()
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
