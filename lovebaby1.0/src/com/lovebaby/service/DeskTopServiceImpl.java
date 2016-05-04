
    /**  
    * @Title: DeskTopServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年12月15日
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
import com.lovebaby.pojo.Picture;
import com.lovebaby.util.GetUuid;

/**
    * @ClassName: DeskTopServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年12月15日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="deskTopService")
public class DeskTopServiceImpl implements DeskTopService {
	@Autowired
	private JdbcDao jdbcDao;
	private static Logger log=Logger.getLogger(DeskTopServiceImpl.class.getName());
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public DeskTopServiceImpl() {
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
	* @Description: 获取主题列表
	* 
	* @param user
	* @return
	* @see com.lovebaby.service.DeskTopService#theme(com.lovebaby.pojo.User)
	*/



	@Override
	public Map<String, Object> theme(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql=null;
			Object[] params=null;
			List result=null;
			int count=0;
			int pagenum=0;

			if (fenyeData.getType().equals("0")) {
				//默认主题
				sql="SELECT *,CONCAT(?,name) as 'ftpimage' from background WHERE  type=0 order by date desc";
				params=new Object[]{
						fenyeData.getFtpurl()
				};
				result=jdbcDao.queryForList(sql, params);
			}else if (fenyeData.getType().equals("1")) {
				//获取推荐主题列表
				sql="SELECT *,CONCAT(?,name) as 'ftpimage' from background WHERE  type=1 order by date desc";			
				params=new Object[]{
						fenyeData.getFtpurl()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取推荐主题总数
				sql="SELECT count(*) from background WHERE  type=1";
				count=jdbcDao.queryForInt(sql, null);
				//获取总页数
				pagenum=(int) Math.ceil(((double)count)/fenyeData.getPage_size());
			}else if (fenyeData.getType().equals("2")) {
				//获取用户上传主题列表
				sql="SELECT *,CONCAT(?,name) as 'ftpimage' from background WHERE mid=? and  type=2 order by date desc";
				params=new Object[]{
						fenyeData.getFtpurl(),
						fenyeData.getId(),
				};
				result=jdbcDao.queryForList(sql, params);
				//获取用户上传主题总数
				sql="SELECT count(*) from background WHERE mid=? and  type=2 ";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
				//获取总页数
				pagenum=(int) Math.ceil(((double)count)/fenyeData.getPage_size());
			}

			map.put("result", result);
			map.put("pagenum", pagenum);
			map.put("pagesize", fenyeData.getPage_size());
			
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	    /* 
	    * @Description: 选择主题
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.DeskTopService#chooseTheme(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> chooseTheme(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//获取推荐主题列表
			String sql="UPDATE members set backimage=? where id=?";		
			Object[] params=new Object[]{
					fenyeData.getName(),
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
	    * @Description: 删除上传的图片
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.DeskTopService#deleteTheme(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> deleteTheme(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//获取推荐主题列表
			String sql="DELETE from background WHERE id=?";		
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
	    * @Description: 上传图片
	    * 
	    * @param pic
	    * @return
	    * @see com.lovebaby.service.DeskTopService#uploadTheme(com.lovebaby.pojo.Picture)
	    */
	    
	@Override
	public Map<String, Object> uploadTheme(Picture pic) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//获取推荐主题列表
			String sql="INSERT INTO background(id,name,type,mid,date) VALUES(?,?,?,?,?)";		
			Object[] params=new Object[]{
					GetUuid.getUuid(),
					pic.getName(),
					"2",
					pic.getTid(),
					format.format(new Date())
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 桌面图标管理
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.DeskTopService#deskTopTools(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> deskTopTools(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql=null;
			List result=null;
			sql="SELECT CONCAT(?,t1.img) as 'ftpimage',t1.* from tools t1 where not EXISTS(SELECT t2.tid from tools_use t2 where t1.id=t2.tid and t2.mid=? ) AND t1.type='2'";
			Object[] params=new Object[]{
					fenyeData.getFtpurl(),
					fenyeData.getId()
			};
			result=jdbcDao.queryForList(sql, params);
			map.put("result", result);
			return map;	
			/*if (fenyeData.getPictype().equals("1")) {				
				//获取业务图标列表
				//sql="SELECT CONCAT(?,t1.img) as 'ftpimage',t1.*,t2.state from tools t1 LEFT JOIN tools_unuse t2 on t1.id=t2.tid WHERE t1.type='1'  t2.mid=? and t1.permission like "+"'%"+fenyeData.getType()+"%'";
				sql="SELECT CONCAT(?,t1.img) as 'ftpimage',t1.* from tools t1 join tools_unuse t2 on t1.id=t2.tid where t2.mid=? AND t1.type='1'";
				Object[] params=new Object[]{
						fenyeData.getFtpurl(),
						fenyeData.getId()
				};
				result=jdbcDao.queryForList(sql, params);
				sql="SELECT COUNT(*) FROM tools WHERE type='1' and t2.mid=? permission LIKE "+"'%"+fenyeData.getType()+"%'";
				count=jdbcDao.queryForInt(sql, null);
				//获取总页数
				pagenum=(int) Math.ceil(((double)count)/fenyeData.getPage_size());
				
			}else if (fenyeData.getPictype().equals("2")) {
				sql="SELECT CONCAT(?,t1.img) as 'ftpimage',t1.* from tools t1 join tools_unuse t2 on t1.id=t2.tid where t2.mid=? AND t1.type='2'";
				Object[] params=new Object[]{
						fenyeData.getFtpurl(),
						fenyeData.getId()
				};
				result=jdbcDao.queryForList(sql, params);
				sql="SELECT COUNT(*) FROM tools WHERE type='2'";		
				count=jdbcDao.queryForInt(sql, null);
				//获取总页数
				pagenum=(int) Math.ceil(((double)count)/fenyeData.getPage_size());
			}*/
			
			//map.put("pagenum", pagenum);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
		
	}


	
	    /* 
	    * @Description: 删除桌面图标
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.DeskTopService#delFromDeskTop(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> delFromDeskTop(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//获取推荐主题列表
			String sql="DELETE from tools_use WHERE tid=? and mid=?";		
			Object[] params=new Object[]{
					fenyeData.getTid(),			
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
	    * @Description: 
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.DeskTopService#addToDeskTop(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> addToDeskTop(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//获取推荐主题列表
			String sql="INSERT INTO tools_use(id,mid,tid) VALUES(?,?,?)";		
			Object[] params=new Object[]{
					GetUuid.getUuid(),
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
	    * @Description: 
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.DeskTopService#desktop(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> desktop(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//未审核申请数
			int applycount=0;
			//未读公告数
			int noticecount=0;
			//获取工具列表
			String sql="SELECT CONCAT(?,t1.img) as 'ftpimage',t1.* from tools t1 where ( EXISTS(SELECT t2.tid from tools_use t2 where t1.id=t2.tid and t2.mid=? ) AND t1.type='2') or (t1.type='1' and t1.permission  like "+"'%"+fenyeData.getType()+"%') order by t1.type";		
			Object[] params=new Object[]{
					fenyeData.getFtpurl(),
					fenyeData.getId()
			};
			List result=jdbcDao.queryForList(sql, params);
			if (!fenyeData.getType().equals("5")||!fenyeData.getType().equals("0")) {
				//机构，园长，园务，老师，管理员获取认证申请数
				if (fenyeData.getType().equals("1")) {			
					//获取数据总数
					sql="select count(*) from authentication where type=2 and tid=? and status=0";
					params=new Object[]{
							fenyeData.getTid()
							};
					applycount=jdbcDao.queryForInt(sql, params);
				}else if (fenyeData.getType().equals("2")) {			
					//获取数据总数
					sql="select count(*) from authentication t1 where ((t1.type='3' and t1.tid=?) or (t1.type='4' and EXISTS(SELECT t2.id FROM (SELECT id from classes WHERE schoolId=?) t2 where t1.tid=t2.id))) and t1.status='0'";
					params=new Object[]{
							fenyeData.getTid(),
							fenyeData.getTid()
							};
					applycount=jdbcDao.queryForInt(sql, params);
					
				}else if (fenyeData.getType().equals("3")) {
					
					sql="select count(*) from authentication t1 where t1.type='4' and EXISTS(SELECT t2.id FROM (SELECT id from classes WHERE schoolId=?) t2 where t1.tid=t2.id) and t1.status='0'";
					params=new Object[]{
							fenyeData.getTid()
							};
					applycount=jdbcDao.queryForInt(sql, params);
				}else if (fenyeData.getType().equals("4")) {
					
					sql="select count(*) from authentication t1 where EXISTS(SELECT t5.tid from (SELECT t3.tid from members t2 JOIN members_relate t3 on t2.id=t3.uid where t2.id=?) t5 WHERE t5.tid=t1.tid) and t1.type='5' and status='0' ";
					params=new Object[]{
							fenyeData.getId()
							};
					applycount=jdbcDao.queryForInt(sql, params);
				}else if (fenyeData.getType().equals("6")) {			
					sql="select count(*) from authentication where  type='1' and status=0";
					applycount=jdbcDao.queryForInt(sql, null);
				}
			}
			if (!fenyeData.getType().equals("0")) {
				//获取未读公告数
				if ((fenyeData.getType().equals("2")||fenyeData.getType().equals("3"))) {				
					//园长和园务获取组织机构发布公告数
					sql="SELECT  COUNT(*) FROM notice t1 where ((t1.from_id=(SELECT oid from schools where id=(SELECT tid from members_relate  WHERE uid=?)) and t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and t2.uid=?)))  and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and t2.uid=?) AND t1.from_type='1' ";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getId()
					};
					noticecount=jdbcDao.queryForInt(sql, params);
					//园长和园务获取校园发布公告数
					sql="SELECT COUNT(*) FROM notice t1 WHERE t1.from_id=(SELECT tid from members_relate  WHERE uid=?) and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?) and t1.from_type in('2','3')";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId()
					};
					noticecount=jdbcDao.queryForInt(sql, params)+noticecount;
					//园长和园务获取班级发布公告数
					sql="SELECT COUNT(*) from notice t1 WHERE EXISTS(SELECT t2.id from (SELECT id from classes WHERE schoolId=(SELECT tid from members_relate  WHERE uid=?)) t2 where t2.id=t1.from_id) and t1.from_type='4' and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?)";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId()
					};
					noticecount=jdbcDao.queryForInt(sql, params)+noticecount;	
				}else if (fenyeData.getType().equals("1")) {				
					//机构获取校园公告数
					sql="SELECT COUNT(*) from notice t1 where EXISTS(SELECT t2.id from (SELECT id from schools where oid=(SELECT tid from members_relate where uid=?)) t2 where t1.from_id=t2.id) and not EXISTS(SELECT t3.tid from notice_read t3 where t1.id=t3.tid and uid=?) and t1.from_type in('2','3')";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId()
					};
					noticecount=jdbcDao.queryForInt(sql, params);
				}else if (fenyeData.getType().equals("4")) {		
					//老师获取校园公告
					sql="SELECT COUNT(*) FROM notice t1 WHERE t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1)) and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?) and t1.from_type in('2','3')";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId()
							
					};
					noticecount=jdbcDao.queryForInt(sql, params);
					//老师获取班级公告
					sql="SELECT COUNT(DISTINCT(t1.id)) FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4' and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?)) t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?))";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getTid(),
					};
					noticecount=jdbcDao.queryForInt(sql, params)+noticecount;
				}else if (fenyeData.getType().equals("5")) {
					//家长获取校园公告
					sql="SELECT COUNT(*) FROM notice t1 WHERE t1.to_type='0' and t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1)) and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?) and t1.from_type in('2','3') ";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId()
					};
					noticecount=jdbcDao.queryForInt(sql, params);
					
					//家长获取班级公告
					sql="SELECT COUNT(DISTINCT(t1.id)) FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4' and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?)) t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getId()
					};
					noticecount=jdbcDao.queryForInt(sql, params)+noticecount;
				}
			}	
			map.put("result", result);
			map.put("applycount", applycount);
			map.put("noticecount", noticecount);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}
	
}
