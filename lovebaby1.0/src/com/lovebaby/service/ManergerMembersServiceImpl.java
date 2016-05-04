
    /**  
    * @Title: ManergerMembersServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年12月31日
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
import com.lovebaby.pojo.Babies;
import com.lovebaby.pojo.Classes;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.util.GetUuid;

/**
    * @ClassName: ManergerMembersServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年12月31日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="manergerMembersService")
public class ManergerMembersServiceImpl implements ManergerMembersService {
	@Autowired
	private JdbcDao jdbcDao;
	private static Logger log=Logger.getLogger(ManergerMembersServiceImpl.class.getName());	
	
	    /* 
	    * @Description: 机构移除旗下院所
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#deleteSchools(com.lovebaby.pojo.FenyeData)
	    */
	    
	public ManergerMembersServiceImpl() {
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

	@Override
	public Map<String, Object> deleteSchools(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql="UPDATE schools set oid=null WHERE id=?";
			Object[] params=new Object[]{
					fenyeData.getId()
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 删除班级
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#deleteClass(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> deleteClass(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//删除班级成员
			String sql="DELETE from members_relate where tid=? and type in('4','5')";
			Object[] params=new Object[]{
					fenyeData.getId()
			};
			jdbcDao.update(sql, params);
			//删除班级宝宝		
			sql="DELETE from babies WHERE classid=?";
			params=new Object[]{
					fenyeData.getId()
			};
			jdbcDao.update(sql, params);
			//删除班级
			sql="DELETE from classes WHERE id=?";
			params=new Object[]{
					fenyeData.getId()
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    

	
	    /* 
	    * @Description: 园务园长获取成员列表
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#getMembers(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getschoolMembers(FenyeData fenyeData) throws Exception {
		try {
			// TODO Auto-generated method stub
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 删除班级成员
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#deleteMember(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> deleteMember(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql="DELETE from members_relate WHERE uid=? and tid=?";
			Object[] params=new Object[]{
					fenyeData.getId(),
					fenyeData.getTid()
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 老师获取成员列表
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#getClassMembers(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getClassMembers(FenyeData fenyeData) throws Exception {
		try {
			// TODO Auto-generated method stub
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}

	}



	
	    /* 
	    * @Description: 修改班级信息
	    * 
	    * @param Classes
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#updateClassInfo(com.lovebaby.pojo.Classes)
	    */
	    
	@Override
	public Map<String, Object> updateClassInfo(Classes classes) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//判断班级是否存在
			String sql="SELECT count(*) from classes WHERE className=? and schoolId=?";
			Object[] params=new Object[]{
					classes.getClassName(),
					classes.getSchoolId()
				
			};
			int count=jdbcDao.queryForInt(sql, params);
			if (count==0) {			
				sql="UPDATE classes set className=? WHERE id=?";
				params=new Object[]{
						classes.getClassName(),
						classes.getId()
				};
				jdbcDao.update(sql, params);
				map.put("flag", "1");
			}else {
				map.put("msg", "该班级已存在");
				map.put("flag", "0");
			}
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}



	
	    /* 
	    * @Description: 宝宝删除
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#deleteBabies(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> deleteBabies(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//删除家长宝宝
			String sql="DELETE FROM members_baby WHERE bid=?";
			Object[] params=new Object[]{
					fenyeData.getId()
			};
			jdbcDao.update(sql, params);
			
			//删除宝宝
			sql="UPDATE babies set state='0' where id=?";
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}

	}



	
	    /* 
	    * @Description: 将宝宝移到其他班级
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#moveBabies(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> moveBabies(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String[] ids=fenyeData.getId().split(",");
			for (int i = 0; i < ids.length; i++) {			
				//获取宝宝信息
				String sql="SELECT t1.*,t2.uid from babies t1 LEFT JOIN members_baby t2 on t1.id=t2.uid WHERE id=?";
				Object[] params=new Object[]{
						ids[i]
				};
				Map<String, Object> data=jdbcDao.queryForMap(sql, params);
				//班级id
				String cid=(String) data.get("classid");
				//家长id
				String uid=(String) data.get("uid");
				
				if (uid!=null) {
					//有家长，迁移家长
					//获取家长在原班级宝宝数
					sql="SELECT count(*) from babies t1 WHERE EXISTS(SELECT t2.bid from (SELECT bid from members_baby  where uid=?) t2 WHERE t1.id=t2.bid) WHERE t1.classid=?";
					params=new Object[]{
							uid,
							cid
					};
					int count=jdbcDao.queryForInt(sql, params);
					if (count!=1) {
						//家长在该班级不知一个宝宝
						//为家长添加新班级
						sql="INSERT into members_relate(id,uid,tid,type) VALUES(?,?,?,'5')";
						params=new Object[]{
								GetUuid.getUuid(),
								uid,
								fenyeData.getTid()
						};
						jdbcDao.update(sql, params);	
					}else {
						//在该班级只有一个宝宝
						//更新家长班级
						sql="UPDATE members_relate set tid=? WHERE uid=? AND tid=?";
						params=new Object[]{
								fenyeData.getTid(),
								uid,
								cid
						};
						jdbcDao.update(sql, params);	
					}
				}
						
				//更新宝宝所属班级
				sql="UPDATE babies set classid=? WHERE id=?";
				params=new Object[]{
						fenyeData.getTid(),
						ids[i]
				};
				jdbcDao.update(sql, params);			
			}
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}

	}



	
	    /* 
	    * @Description: 各单位获取旗下单位列表
	    * 
	    * @param fenyeData 单位id：tid，用户类型：type
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#getDanweiList(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getDanweiList(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql="";
			Object[] params=null;
			List danwei=null;
			if (fenyeData.getType().equals("1")) {
				sql="SELECT * from schools where oid=? limit ?,?";
				params=new Object[]{
						fenyeData.getTid(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				danwei=jdbcDao.queryForList(sql, params);
			}else if (fenyeData.getType().equals("4")) {
				sql="SELECT * from classes t1 WHERE EXISTS(SELECT t2.tid from (SELECT tid FROM members_relate where uid=?) t2 where t1.id=t2.tid)";
				params=new Object[]{
						fenyeData.getId()
				};
				danwei=jdbcDao.queryForList(sql, params);
			}
			map.put("danwei", danwei);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}

	}



	
	    /* 
	    * @Description: 家长和老师获取班级列表 
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#getClasses(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getClasses(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//删除家长宝宝
			String sql="SELECT * from classes t1 WHERE EXISTS(SELECT t2.tid from (SELECT tid FROM members_relate WHERE uid=? and type in('4','5')) t2 WHERE t1.id=t2.tid)";
			Object[] params=new Object[]{
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
	* @Description: 获取宝宝列表
	* 
	* @param fenyeData
	* @return
	* @see com.lovebaby.service.CheckBabyService#getBabies(com.lovebaby.pojo.FenyeData)
	*/

	@Override
	public Map<String, Object> getBabies(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			Properties prop=getProp("/ftp.properties");
			String babyimage=prop.getProperty("url")+"/"+prop.getProperty("babyaddress");
			List result=null;
			int count=0;
			//获取宝宝列表
			String sql="SELECT *,CONCAT(?,pic) as 'ftpimage' from babies WHERE classid=? and state='1' limit ?,?";
			Object[] params=new Object[]{
					babyimage,
					fenyeData.getCid(),
					fenyeData.getPage_num(),
					fenyeData.getPage_size()
			};
			result=jdbcDao.queryForList(sql, params);
			//获取宝宝总数
			sql="SELECT count(*) from babies WHERE classid=? and state='1'";
			params=new Object[]{fenyeData.getCid()};
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
	    * @Description: 获取宝宝信息及家长列表
	    * 
	    * @param babies
	    * @return
	    * @see com.lovebaby.service.ManergerMembersService#getBabyParents(com.lovebaby.pojo.Babies)
	    */
	    
	@Override
	public Map<String, Object> getBabyParents(Babies babies) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			Properties prop=getProp("/ftp.properties");
			String babyimage=prop.getProperty("url")+"/"+prop.getProperty("babyaddress");
			//获取宝宝信息
			String sql="SELECT *,CONCAT(?,pic) as 'ftpimage' from babies WHERE id=? and state='1' ";
			Object[] params=new Object[]{
					babyimage,
					babies.getId()
			};
			Map<String, Object> result=jdbcDao.queryForMap(sql, params);
			//获取宝宝家长列表
			String parentimage=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
			sql="SELECT *,CONCAT(?,headImage) as 'ftpimage' from members t1 WHERE EXISTS(SELECT t2.uid from (SELECT uid from members_baby WHERE bid=?) t2 WHERE t1.id=t2.uid)";
			params=new Object[]{
					parentimage,
					babies.getId()
					};
			List parents=jdbcDao.queryForList(sql, params);
			result.put("parents", parents);
			map.put("result", result);		
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	}

	
	    /* 
	    * @Description: 
	    * 
	    * @param fenyeData
	    * @return
	    * @throws Exception
	    * @see com.lovebaby.service.ManergerMembersService#addAdvice(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> addAdvice(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql="INSERT into advice(id,uid,content,date) VALUES(?,?,?,?)";
			Object[] params=new Object[]{
					GetUuid.getUuid(),
					fenyeData.getId(),
					fenyeData.getContent(),
					format.format(new Date())
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


}
