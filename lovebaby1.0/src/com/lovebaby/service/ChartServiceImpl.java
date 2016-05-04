
    /**  
    * @Title: ChartServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年12月18日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    * @ClassName: ChartServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年12月18日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="chartService")
public class ChartServiceImpl implements ChartService {
	@Autowired
	private JdbcDao jdbcDao;
	private static Logger log=Logger.getLogger(ChartServiceImpl.class.getName());
	
	
	/* 
	* @Description: 获取好友列表
	* 
	* @param fenyeData
	* @return
	* @see com.lovebaby.service.ChartService#getFriendsList(com.lovebaby.pojo.FenyeData)
	*/

	public ChartServiceImpl() {
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


	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getFriendsList(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql=null;
			Object[] params=null;
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
			if (fenyeData.getType().equals("1")) {
				//机构获取联系人列表
				//获取机构下所有校园
				sql="SELECT * from schools where oid=?";
				params=new Object[]{
						fenyeData.getTid()
						};
				List result=jdbcDao.queryForList(sql, params);
				//为每个学校添加园长和园务列表
				for (int i = 0; i < result.size(); i++) {
					Map<String, Object> data=(Map<String, Object>) result.get(i);
					String id=(String) data.get("id");
					sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.type in('2','3') and t2.tid=?";
					params=new Object[]{
							headimgae,
							id
							};
					List members=jdbcDao.queryForList(sql, params);
					data.put("members", members);
				}
				map.put("result", result);
			}else if (fenyeData.getType().equals("2")||fenyeData.getType().equals("3")) {
				//园务或园长获取联系人列表
				sql="SELECT oid from schools where id=?";
				params=new Object[]{
						fenyeData.getTid()
				};
				Map<String, Object> oids=jdbcDao.queryForMap(sql, params);
				String oid=(String) oids.get("oid");
				if (!oid.equals("0")) {					
					//获取所属机构
					sql="SELECT * from organization where id=?";
					params=new Object[]{
							oid
					};
					Map<String, Object> org=jdbcDao.queryForMap(sql, params);
					//给机构添加成员
					sql="SELECT t1.* , CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t2.tid=? and t1.type='1'";
					params=new Object[]{
							headimgae,
							(String)org.get("id")
					};
					List orgmembers=jdbcDao.queryForList(sql, params);
					org.put("orgmembers", orgmembers);
					map.put("org", org);
				}
				
				//获取所有园务园长
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t2.tid=? and t1.type in('2','3')";
				params=new Object[]{
						headimgae,
						fenyeData.getTid()
						};
				List schoolmembers=jdbcDao.queryForList(sql, params);
				
				//获取所有老师
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE EXISTS(SELECT t3.id from (SELECT id from classes WHERE schoolId=?) t3 WHERE t2.tid=t3.id) and t2.type='4'";
				params=new Object[]{
						headimgae,
						fenyeData.getTid()
						};
				List teachermembers=jdbcDao.queryForList(sql, params);
				
				//获取所有班级
				sql="SELECT * from classes WHERE schoolId=?";
				params=new Object[]{
						fenyeData.getTid()
						};
				List classes=jdbcDao.queryForList(sql, params);
				//为每个班级添加家长联系人
				for (int i = 0; i < classes.size(); i++) {
					Map<String, Object> data=(Map<String, Object>) classes.get(i);
					String id=(String) data.get("id");
					sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.type='5' and t2.tid=?";
					params=new Object[]{
							headimgae,
							id
							};
					List members=jdbcDao.queryForList(sql, params);
					data.put("members", members);
				}
				map.put("schoolmembers", schoolmembers);
				map.put("teachermembers", teachermembers);
				map.put("classes", classes);
			}else if (fenyeData.getType().equals("4")) {
				//老师获取联系人列表
				//获取所有园务园长
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t2.tid=? and t1.type in('2','3')";
				params=new Object[]{
						headimgae,
						fenyeData.getTid()
						};
				List schoolmembers=jdbcDao.queryForList(sql, params);
				

				//获取所有老师
				
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE EXISTS(SELECT t3.id from (SELECT id from classes WHERE schoolId=?) t3 WHERE t2.tid=t3.id) and t2.type='4'";
				params=new Object[]{
						headimgae,
						fenyeData.getTid()
						};
				List teachermembers=jdbcDao.queryForList(sql, params);
				
				//获取所属班级列表
				sql="SELECT * from classes t1 where EXISTS(SELECT t4.tid from (SELECT t3.tid from members t2 JOIN members_relate t3 on t2.id=t3.uid where t2.id=?) t4 where t1.id=t4.tid)";
				params=new Object[]{
						fenyeData.getUid()
						};
				List classes=jdbcDao.queryForList(sql, params);
				//为每个班级添加家长联系人
				for (int i = 0; i < classes.size(); i++) {
					Map<String, Object> data=(Map<String, Object>) classes.get(i);
					String id=(String) data.get("id");
					sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.type='5' and t2.tid=?";
					params=new Object[]{
							headimgae,
							id
							};
					List members=jdbcDao.queryForList(sql, params);
					data.put("members", members);
				}
				map.put("schoolmembers", schoolmembers);
				map.put("teachermembers", teachermembers);
				map.put("classes", classes);
			}else if (fenyeData.getType().equals("5")) {
				//家长获取联系人列表
				//获取所有园务园长
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t2.tid=? and t1.type in('2','3')";
				params=new Object[]{
						headimgae,
						fenyeData.getTid()
						};
				List schoolmembers=jdbcDao.queryForList(sql, params);
				
				//获取所有老师
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 join members_relate t2 on t1.id=t2.uid where t1.type='4' and EXISTS(SELECT t5.tid from (SELECT t4.tid from members t3 JOIN members_relate t4 on t3.id=t4.uid where t3.id=?) t5 where t2.tid=t5.tid)";
				params=new Object[]{
						headimgae,
						fenyeData.getUid()
						};
				List teachermembers=jdbcDao.queryForList(sql, params);
				
				//获取所属班级列表
				sql="SELECT * from classes t1 where EXISTS(SELECT t4.tid from (SELECT t3.tid from members t2 JOIN members_relate t3 on t2.id=t3.uid where t2.id=?) t4 where t1.id=t4.tid)";
				params=new Object[]{
						fenyeData.getUid()
						};
				List classes=jdbcDao.queryForList(sql, params);
				//为每个班级添加家长联系人
				for (int i = 0; i < classes.size(); i++) {
					Map<String, Object> data=(Map<String, Object>) classes.get(i);
					String id=(String) data.get("id");
					sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.type='5' and t2.tid=? and t1.id!=?";
					params=new Object[]{
							headimgae,
							id,
							fenyeData.getUid()
							};
					List members=jdbcDao.queryForList(sql, params);
					data.put("members", members);
				}
				map.put("schoolmembers", schoolmembers);
				map.put("teachermembers", teachermembers);
				map.put("classes", classes);
			}
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

    /* 
    * @Description: 获取好友列表
    * 
    * @param fenyeData
    * @return
    * @throws Exception
    * @see com.lovebaby.service.ChartService#getFriendInfo(com.lovebaby.pojo.FenyeData)
    */
	    
	@Override
	public Map<String, Object> getFriendInfo(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
			String sql="SELECT *, CONCAT(?,headImage) as 'ftpimage' from members WHERE id=?";
			Object[] params=new Object[]{
					headimgae,
					fenyeData.getId()
			};
			Map<String, Object> result=jdbcDao.queryForMap(sql, params);
			map.put("result", result);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
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
	    * @Description: 搜索好友
	    * 
	    * @param fenyeData
	    * @return
	    * @throws Exception
	    * @see com.lovebaby.service.ChartService#searchFriends(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> searchFriends(FenyeData fenyeData) throws Exception {
		try {
			List match=new ArrayList<>();
			Map<String, Object> map=new HashMap<>();
			String sql=null;
			Object[] params=null;
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
			if (fenyeData.getType().equals("1")) {
				//机构获取联系人列表
				//获取机构下所有校园
				sql="SELECT * from schools where oid=?";
				params=new Object[]{
						fenyeData.getTid()
						};
				List result=jdbcDao.queryForList(sql, params);
				//为每个学校添加园长和园务列表
				for (int i = 0; i < result.size(); i++) {
					Map<String, Object> data=(Map<String, Object>) result.get(i);
					String id=(String) data.get("id");
					sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.type in('2','3') and t2.tid=? and t1.realName like '%"+fenyeData.getName()+"%'";
					params=new Object[]{
							headimgae,
							id
							};
					List members=jdbcDao.queryForList(sql, params);
					match.addAll(members);
				}
				map.put("match", match);
			}else if (fenyeData.getType().equals("2")||fenyeData.getType().equals("3")) {
				//园务或园长获取联系人列表
				sql="SELECT oid from schools where id=?";
				params=new Object[]{
						fenyeData.getTid()
				};
				Map<String, Object> oids=jdbcDao.queryForMap(sql, params);
				String oid=(String) oids.get("oid");
				if (!oid.equals("0")) {					
					//获取所属机构
					sql="SELECT * from organization where id=?";
					params=new Object[]{
							oid
					};
					Map<String, Object> org=jdbcDao.queryForMap(sql, params);
					//给机构添加成员
					sql="SELECT t1.* , CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t2.tid=? and t1.type='1' and t1.realName like '%"+fenyeData.getName()+"%'";
					params=new Object[]{
							headimgae,
							(String)org.get("id")
					};
					List orgmembers=jdbcDao.queryForList(sql, params);
					match.addAll(orgmembers);
				}
				
				//获取所有园务园长
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t2.tid=? and t1.type in('2','3') and t1.realName like '%"+fenyeData.getName()+"%'";
				params=new Object[]{
						headimgae,
						fenyeData.getTid()
						};
				List schoolmembers=jdbcDao.queryForList(sql, params);
				
				//获取所有老师
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE EXISTS(SELECT t3.id from (SELECT id from classes WHERE schoolId=?) t3 WHERE t2.tid=t3.id) and t2.type='4' and t1.realName like '%"+fenyeData.getName()+"%'";
				params=new Object[]{
						headimgae,
						fenyeData.getTid()
						};
				List teachermembers=jdbcDao.queryForList(sql, params);
				
				//获取所有班级
				sql="SELECT * from classes WHERE schoolId=?";
				params=new Object[]{
						fenyeData.getTid()
						};
				List classes=jdbcDao.queryForList(sql, params);
				//为每个班级添加家长联系人
				for (int i = 0; i < classes.size(); i++) {
					Map<String, Object> data=(Map<String, Object>) classes.get(i);
					String id=(String) data.get("id");
					sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.type='5' and t2.tid=? and t1.realName like '%"+fenyeData.getName()+"%'";
					params=new Object[]{
							headimgae,
							id
							};
					List members=jdbcDao.queryForList(sql, params);
					match.addAll(members);
				}
				match.addAll(schoolmembers);
				match.addAll(teachermembers);
			}else if (fenyeData.getType().equals("4")) {
				//老师获取联系人列表
				//获取所有园务园长
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t2.tid=? and t1.type in('2','3') and t1.realName like '%"+fenyeData.getName()+"%'";
				params=new Object[]{
						headimgae,
						fenyeData.getTid()
						};
				List schoolmembers=jdbcDao.queryForList(sql, params);
				

				//获取所有老师
				
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE EXISTS(SELECT t3.id from (SELECT id from classes WHERE schoolId=?) t3 WHERE t2.tid=t3.id) and t2.type='4' and t1.realName like '%"+fenyeData.getName()+"%'";
				params=new Object[]{
						headimgae,
						fenyeData.getTid()
						};
				List teachermembers=jdbcDao.queryForList(sql, params);
				
				//获取所属班级列表
				sql="SELECT * from classes t1 where EXISTS(SELECT t4.tid from (SELECT t3.tid from members t2 JOIN members_relate t3 on t2.id=t3.uid where t2.id=?) t4 where t1.id=t4.tid) ";
				params=new Object[]{
						fenyeData.getUid()
						};
				List classes=jdbcDao.queryForList(sql, params);
				//为每个班级添加家长联系人
				for (int i = 0; i < classes.size(); i++) {
					Map<String, Object> data=(Map<String, Object>) classes.get(i);
					String id=(String) data.get("id");
					sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.type='5' and t2.tid=? and t1.realName like '%"+fenyeData.getName()+"%'";
					params=new Object[]{
							headimgae,
							id
							};
					List members=jdbcDao.queryForList(sql, params);
					match.addAll(members);
				}
				match.addAll(schoolmembers);
				match.addAll(teachermembers);
			}else if (fenyeData.getType().equals("5")) {
				//家长获取联系人列表
				//获取所有园务园长
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t2.tid=? and t1.type in('2','3') and t1.realName like '%"+fenyeData.getName()+"%'";
				params=new Object[]{
						headimgae,
						fenyeData.getTid()
						};
				List schoolmembers=jdbcDao.queryForList(sql, params);
				
				//获取所有老师
				sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 join members_relate t2 on t1.id=t2.uid where t1.type='4' and EXISTS(SELECT t5.tid from (SELECT t4.tid from members t3 JOIN members_relate t4 on t3.id=t4.uid where t3.id=?) t5 where t2.tid=t5.tid) and t1.realName like '%"+fenyeData.getName()+"%'";
				params=new Object[]{
						headimgae,
						fenyeData.getUid()
						};
				List teachermembers=jdbcDao.queryForList(sql, params);
				
				//获取所属班级列表
				sql="SELECT * from classes t1 where EXISTS(SELECT t4.tid from (SELECT t3.tid from members t2 JOIN members_relate t3 on t2.id=t3.uid where t2.id=?) t4 where t1.id=t4.tid)";
				params=new Object[]{
						fenyeData.getUid()
						};
				List classes=jdbcDao.queryForList(sql, params);
				//为每个班级添加家长联系人
				for (int i = 0; i < classes.size(); i++) {
					Map<String, Object> data=(Map<String, Object>) classes.get(i);
					String id=(String) data.get("id");
					sql="SELECT t1.*, CONCAT(?,t1.headImage) as 'ftpimage' from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.type='5' and t2.tid=? and t1.id!=? and t1.realName like '%"+fenyeData.getName()+"%'";
					params=new Object[]{
							headimgae,
							id,
							fenyeData.getUid()
							};
					List members=jdbcDao.queryForList(sql, params);
					match.addAll(members);
				}
				match.addAll(schoolmembers);
				match.addAll(teachermembers);
			}
			map.put("match", match);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	}


	

}
