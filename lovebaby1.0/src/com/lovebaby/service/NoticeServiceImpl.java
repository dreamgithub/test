
    /**  
    * @Title: NoticeServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月11日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lovebaby.dao.JdbcDao;
import com.lovebaby.message.PushMessage;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Notice;
import com.lovebaby.util.GetUuid;

import io.rong.ApiHttpClient;
import io.rong.models.FormatType;
import io.rong.models.Message;
import net.sf.json.JSONObject;

/**
    * @ClassName: NoticeServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年11月11日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="noticeService")
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	private JdbcDao jdbcDao;
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger log=Logger.getLogger(NoticeServiceImpl.class.getName());

	
	public NoticeServiceImpl() {
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
	* @Description: 添加公告
	* 
	* @param notice
	* @return
	* @see com.lovebaby.service.NoticeService#addNotice(com.lovebaby.pojo.Notice)
	*/
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> addNotice(Notice notice) throws Exception {
		try {
			List toids=new ArrayList<>();
			List tuisong = null;
			String[] ids=null;
			Map<String, Object> map=new HashMap<>();		
			String sql=null;
			Object[] params=null;
			String id=GetUuid.getUuid();
			if (notice.getContentType()==null||notice.getContentType().equals("")) {
				notice.setContentType("0");
			}
			//插入公告	
			if (notice.getPics()!=null) {	
				//有图片
				sql="insert into notice(id,title,content,to_type,from_id,from_type,publishDate,from_uid,contentType,pic,toTopDate) values(?,?,?,?,?,?,?,?,?,?,?)";
				//(id,title,content,to_type,from_id,from_type,publishDate)
				params=new Object[]{
						id,
						notice.getTitle(),
						notice.getContent(),
						notice.getTo_type(),
						notice.getFrom_id(),
						notice.getFrom_type(),
						format.format(new Date()),
						notice.getUid(),
						notice.getContentType(),
						notice.getPics().get(0),
						notice.getToTop().equals("1")?format.format(new Date()):null
				};
				jdbcDao.update(sql, params);
				//公告添加图片列表
				sql="insert into pictures(id,picname,type,tid) values(?,?,?,?)";
				List pics=notice.getPics();
				List list1=new ArrayList<>();
				for (int i = 0; i < pics.size(); i++) {
					list1.add(new Object[]{
							GetUuid.getUuid(),
							pics.get(i),
							"2",
							id
					});
				}	
				jdbcDao.batchBySimpleJdbcTemplate(sql, list1);	
			}else {
				//无图片
				sql="insert into notice(id,title,content,to_type,from_id,from_type,publishDate,from_uid,contentType,pic,textContent,toTopDate) values(?,?,?,?,?,?,?,?,?,?,?,?)";
				//(id,title,content,to_type,from_id,from_type,publishDate)
				params=new Object[]{
						id,
						notice.getTitle(),
						notice.getContent(),
						notice.getTo_type(),
						notice.getFrom_id(),
						notice.getFrom_type(),
						format.format(new Date()),
						notice.getUid(),
						notice.getContentType(),
						notice.getPicname()==null?"notice.png":notice.getPicname(),
						notice.getTextContent(),
						notice.getToTop().equals("1")?format.format(new Date()):null
				};
				jdbcDao.update(sql, params);
			}	
					
			//只有机构人或者老师时，且发送对象为指定对象，才添加接收人列表		
			if ((notice.getTo_type().equals("1")&&notice.getFrom_type().equals("1"))||notice.getTo_type().equals("1")&&notice.getFrom_type().equals("4")) {
				ids=notice.getToids().split(",");
				List list2=new ArrayList<>();
				sql="insert into notice_unread(id,uid,tid) values(?,?,?)";
				for (int i = 0; i < ids.length; i++) {
					list2.add(new Object[]{
							GetUuid.getUuid(),
							ids[i],
							id
							});
				}
				jdbcDao.batchBySimpleJdbcTemplate(sql, list2);
			}
			
			//推送信息
			//获取接收人列表
			if (notice.getFrom_type().equals("1")) {
				//获取机构推送人列表
				if (notice.getTo_type().equals("0")) {
					//推送所有人
					sql="SELECT t1.uid from members_relate t1 WHERE EXISTS(SELECT t2.id FROM (SELECT id from schools WHERE oid=?) t2 WHERE t1.tid=t2.id ) AND t1.type in('2','3')";
					params=new Object[]{
							notice.getFrom_id()
					};
					tuisong=jdbcDao.queryForList(sql, params);
				}else {
					//推送给指定人
					String in="(";
					for (int i = 0; i <ids.length-1; i++) {
						in=in+"'"+ids[i]+"',";
					}
					in=in+"'"+ids[ids.length-1]+"')";
					sql="SELECT t1.uid from members_relate t1 WHERE EXISTS(SELECT t2.id FROM (SELECT id from schools WHERE id in"+in+") t2 WHERE t1.tid=t2.id ) AND t1.type in('2','3')";
					tuisong=jdbcDao.queryForList(sql, null);
				}
			}else if (notice.getFrom_type().equals("2")||notice.getFrom_type().equals("3")) {
				//获取校园公告推送人列表
				if (notice.getTo_type().equals("0")) {
					//推送所有人
					sql="SELECT t1.uid from members_relate t1 WHERE (EXISTS (SELECT t2.id from (SELECT id from classes WHERE schoolId=?) t2 WHERE t1.tid=t2.id) and t1.type in('4','5')) or (tid=? AND type in('2','3') AND uid!=?) or (t1.tid=(SELECT oid from schools WHERE id=?) and t1.type='1')";
					params=new Object[]{
							notice.getFrom_id(),
							notice.getFrom_id(),
							notice.getUid(),
							notice.getFrom_id()
					};
					tuisong=jdbcDao.queryForList(sql, params);
				}else {
					//推送给指定人
					sql="SELECT t1.uid from members_relate t1 WHERE (EXISTS (SELECT t2.id from (SELECT id from classes WHERE schoolId=?) t2 WHERE t1.tid=t2.id) and t1.type='4')) or (tid=? AND type in('2','3') AND uid!=?) or (t1.tid=(SELECT oid from schools WHERE id=?) and t1.type='1')";
					params=new Object[]{
							notice.getFrom_id(),
							notice.getFrom_id(),
							notice.getUid(),
							notice.getFrom_id()
					};
					tuisong=jdbcDao.queryForList(sql, params);
				}
			}else if (notice.getFrom_type().equals("4")) {
				//获取班级公告推送人列表
				if (notice.getTo_type().equals("0")) {
					//推送所有人
					sql="SELECT t1.uid from members_relate t1 WHERE (EXISTS (SELECT t2.tid from (SELECT tid from members_relate WHERE uid=(SELECT uid from members_relate WHERE tid=? AND type='4')) t2 where t1.tid=t2.tid) and t1.type in('4','5') and t1.uid!=?) or (t1.tid=(SELECT schoolId from classes WHERE id=?) and t1.type in('2','3'))";
					params=new Object[]{
							notice.getFrom_id(),
							notice.getUid(),
							notice.getFrom_id()
					};
					tuisong=jdbcDao.queryForList(sql, params);
				}else {
					//推送给指定人
					String in="(";
					for (int i = 0; i <ids.length-1; i++) {
						in=in+"'"+ids[i]+"',";
					}
					in=in+"'"+ids[ids.length-1]+"')";
					sql="SELECT * from members_relate t1 WHERE (t1.tid=(SELECT schoolId from classes WHERE id=?) and t1.type in('2','3')) or (t1.type in('4','5') and t1.tid in"+in+")";
					tuisong=jdbcDao.queryForList(sql, null);
				}
			}
			for (int i = 0; i < tuisong.size(); i++) {
				Map< String, Object> data=(Map<String, Object>) tuisong.get(i);
				String uid=(String) data.get("uid");
				toids.add(uid);
			}
			//去重
			HashSet h = new HashSet(toids); 
			toids.clear();       
			toids.addAll(h);  
			//向审批人发送系统消息
			Properties prop1 =getProp("/rongyun.properties");
			String key = prop1.getProperty("key");
			String secret = prop1.getProperty("secret");
			JSONObject json=JSONObject.fromObject("{'content':'爱幼通平台通知'}");
			Message msg=new PushMessage(json, "RC:TxtMsg");
			try {
				ApiHttpClient.publishSystemMessage(key, secret, notice.getFrom_id(), toids, msg, "您收到一条新的公告，请及时查看！", null, FormatType.json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	    /* 
	    * @Description:获取用户发布公告列表
	    * 
	    * @param notice
	    * @return
	    * @see com.lovebaby.service.NoticeService#getPublishNotice(com.lovebaby.pojo.Notice)
	    */
	    

	@Override
	public Map<String, Object> getPublishNotice(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql;
			Object[] params;
			//获取数据列表
			sql="select * from notice where from_uid=? order by publishDate desc  limit ?,?";
			params=new Object[]{
					fenyeData.getId(),
					fenyeData.getPage_num(),
					fenyeData.getPage_size()
			};
			List result=jdbcDao.queryForList(sql, params);
			//获取数据总数
			sql="select count(*) from notice where from_uid=? order by publishDate desc";
			params=new Object[]{
					fenyeData.getId()
			};
			int count=jdbcDao.queryForInt(sql, params);
			map.put("count", count);
			map.put("result", result);
			
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	
	}

	
	    /* 
	    * @Description: 获取用户已读公告列表
	    * 
	    * @param user
	    * @return
	    * @see com.lovebaby.service.NoticeService#getReadNotice(com.lovebaby.pojo.User)
	    */
	    
	@Override
	public Map<String, Object> getReadNotice(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String searchType=fenyeData.getSearchType();
			String sql;
			Object[] params;
			List result=null;
			int count=0;
			//组织机构发布公告
			if (searchType.equals("o")) {				
				sql="select t1.*,t2.readDate from notice t1 RIGHT JOIN notice_read t2 ON t1.id=t2.tid where t2.uid=? and t1.from_type=1 order by t2.readDate  desc limit ?,?";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="select count(*) from notice t1 RIGHT JOIN notice_read t2 ON t1.id=t2.tid where t2.uid=? and t1.from_type=1 ";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")) {
				//校园公告
				sql="select t1.*,t2.readDate from notice t1 RIGHT JOIN notice_read t2 ON t1.id=t2.tid where t2.uid=? and t1.from_type in(2,3) order by t2.readDate  desc limit ?,?";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="select count(*) from notice t1 RIGHT JOIN notice_read t2 ON t1.id=t2.tid where t2.uid=? and t1.from_type in(2,3)  ";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("c")) {
				try {
					
				} catch (Exception e) {
					log.error(e.getMessage());
					throw new Exception();
				}
				//班级公告
				sql="select t1.*,t2.readDate from notice t1 RIGHT JOIN notice_read t2 ON t1.id=t2.tid where t2.uid=? and t1.from_type=4 order by t2.readDate  desc limit ?,?";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="select count(*) from notice t1 RIGHT JOIN notice_read t2 ON t1.id=t2.tid where t2.uid=? and t1.from_type=4 ";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}
			map.put("result", result);
			map.put("count", count);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	    /* 
	    * @Description: 获取用户未读公告列表
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#getUnreadNotice(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getUnreadNotice(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String searchType=fenyeData.getSearchType();
			String type=fenyeData.getType();
			String sql;
			Object[] params;
			List result=null;
			int count=0;
			//园长和园务获取组织机构发布公告
			if (searchType.equals("o")&&(type.equals("2")||type.equals("3"))) {				
				sql="select t3.* from (select t1.* from (SELECT * from notice WHERE from_id=? and from_type=1) t1 LEFT  JOIN notice_unread t2 ON t1.id=t2.tid where t1.to_type=0  or (t2.uid=?  AND t1.to_type=1 )) t3 where t3.id NOT in(SELECT tid from notice_read where uid=?) ORDER BY t3.publishDate desc LIMIT ?,?";
				params=new Object[]{
						fenyeData.getOid(),
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="select count(*) from (select t1.* from (SELECT * from notice WHERE from_id=? and from_type=1) t1 LEFT  JOIN notice_unread t2 ON t1.id=t2.tid where t1.to_type=0  or (t2.uid=?  AND t1.to_type=1 )) t3 where t3.id NOT in(SELECT tid from notice_read where uid=?) ";
				params=new Object[]{
						fenyeData.getOid(),
						fenyeData.getId(),
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&(type.equals("2")||type.equals("3"))) {
				//园务或园长获取校园公告
				sql="SELECT * from notice WHERE from_uid!=? and from_id=? AND from_type in(2,3) and id NOT in(SELECT tid from notice_read where uid=?)  ORDER BY publishDate desc LIMIT ?,?";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getSid(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT count(*) from notice WHERE from_uid!=? and from_id=? AND from_type in(2,3) and id NOT in(SELECT tid from notice_read where uid=?) ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getSid(),
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("c")&&(type.equals("2")||type.equals("3"))) {
				//园务或园长获取班级公告
				sql="SELECT * from notice WHERE  from_id=? AND from_type=4 and id NOT in(SELECT tid from notice_read where uid=?) ORDER BY publishDate desc LIMIT ?,?";
				params=new Object[]{
						fenyeData.getCid(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size(),					
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT count(*) from notice WHERE  from_id=? AND from_type=4 and id NOT in(SELECT tid from notice_read where uid=?) ";
				params=new Object[]{
						fenyeData.getCid(),
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&type.equals("1")) {
				//机构获取校园公告
				sql="SELECT * from notice WHERE  from_id=? AND from_type in(2,3) and id NOT in(SELECT tid from notice_read where uid=?) ORDER BY publishDate desc LIMIT ?,?";
				params=new Object[]{
						fenyeData.getSid(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT count(*) from notice WHERE  from_id=? AND from_type in(2,3) and id NOT in(SELECT tid from notice_read where uid=?)";
				params=new Object[]{
						fenyeData.getSid(),
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&type.equals("4")) {
				//老师获取校园的公告
				sql="SELECT * from notice WHERE from_id=? and from_type in(2,3)  and id NOT in(SELECT tid from notice_read where uid=?)  ORDER BY publishDate desc LIMIT ?,?";
				params=new Object[]{
						fenyeData.getSid(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT count(*) from notice WHERE from_id=? and from_type in(2,3) and to_type=0 and id NOT in(SELECT tid from notice_read where uid=?) ";
				params=new Object[]{
						fenyeData.getSid(),
						fenyeData.getId()
						
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("c")&&type.equals("4")) {
				//老师获取班级公告
				sql="SELECT * from notice t1 LEFT join notice_unread t2 on t1.id=t2.tid where t1.from_uid!=? and t1.from_type=4 and t1.id NOT in(SELECT tid from notice_read where uid=?) and  "
						+ "((t1.to_type=0 and ? in(SELECT t4.tid from members t3 RIGHT JOIN members_relate t4 on t3.id=t4.uid where t3.type=4 and t3.id=t1.from_uid )) or (t1.to_type=1 and t2.uid=?)) ORDER BY t1.publishDate desc LIMIT ?,?";
				//sql="SELECT * from notice WHERE from_uid!=? and from_id=? AND from_type=4 and id NOT in(SELECT tid from notice_read) ORDER BY publishDate desc LIMIT ?,?";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getCid(),
						fenyeData.getCid(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT count(*) from notice t1 LEFT join notice_unread t2 on t1.id=t2.tid where t1.from_uid!=? and t1.from_type=4 and t1.id NOT in(SELECT tid from notice_read where uid=?) and  "
						+ "((t1.to_type=0 and ? in(SELECT t4.tid from members t3 RIGHT JOIN members_relate t4 on t3.id=t4.uid where t3.type=4 and t3.id=t1.from_uid )) or (t1.to_type=1 and t2.uid=?)) ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getCid(),
						fenyeData.getCid(),
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("c")&&type.equals("5")) {
				//家长获取班级公告
				sql="SELECT * from notice WHERE  from_id=? AND from_type=4 and id NOT in(SELECT tid from notice_read where uid=?) ORDER BY publishDate desc LIMIT ?,?";
				params=new Object[]{
						fenyeData.getCid(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT count(*) from notice WHERE  from_id=? AND from_type=4 and id NOT in(SELECT tid from notice_read where uid=?) ";
				params=new Object[]{
						fenyeData.getCid(),
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&type.equals("5")) {
				//家长获取校园公告
				sql="SELECT * from notice WHERE from_id=(SELECT schoolId from classes where id=?) and from_type in(2,3)  and id NOT in(SELECT tid from notice_read where uid=?)  ORDER BY publishDate desc LIMIT ?,?";
				params=new Object[]{
						fenyeData.getCid(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT count(*) from notice WHERE from_id=(SELECT schoolId from classes where id=?) and from_type in(2,3)  and id NOT in(SELECT tid from notice_read where uid=?)";
				params=new Object[]{
						fenyeData.getCid(),
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}			
			map.put("result", result);
			map.put("count", count);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	
	}

	
	    /* 
	    * @Description: 读取公告
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#readNotice(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> readNotice(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql;
			Object[] params;
			
			//获取公告读取记录
			sql="SELECT count(*) FROM notice_read where tid=? and uid=?";
			params=new Object[]{
					fenyeData.getNid(),
					fenyeData.getId()
			};
			int count=jdbcDao.queryForInt(sql, params);
			if (count==0) {
				//用户未读取过该公告		
				//更新访问次数，加1
				sql="update notice set times=times+1 where id=?";
				params=new Object[]{fenyeData.getNid()};
				jdbcDao.update(sql, params);
				//添加已读公告
				sql="insert into notice_read(id,uid,tid,readDate) values(?,?,?,?)";	
				params=new Object[]{
						GetUuid.getUuid(),
						fenyeData.getId(),
						fenyeData.getNid(),
						format.format(new Date())
				};
				jdbcDao.update(sql, params);
				//删除未读公告
				sql="delete from notice_unread where tid=? and uid=?";	
				params=new Object[]{
						fenyeData.getNid(),
						fenyeData.getId()
				};
				jdbcDao.update(sql, params);

			}
			//获取公告内容
			sql="select * from notice where id=?";
			params=new Object[]{fenyeData.getNid()};
			Map<String, Object> result=jdbcDao.queryForMap(sql, params);
			//添加公告图片
			Properties prop=getProp("/ftp.properties");
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("noticeaddress");
			sql="SELECT CONCAT(?,picname) as 'ftpimage' from pictures where tid=? AND type='2'";
			params=new Object[]{ftpurl,fenyeData.getNid()};
			List pics=jdbcDao.queryForList(sql, params);
			result.put("pics", pics);
			map.put("result", result);	
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	
	}

	
	    /* 
	    * @Description: 园务或园长获取校园所属机构信息
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#getSchoolType(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getSchoolType(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql="SELECT t2.* from schools t1 LEFT JOIN organization t2 on t1.oid=t2.id where t1.id=?";
			Object[] params=new Object[]{fenyeData.getSid()};
			List result=jdbcDao.queryForList(sql, params);
			map.put("result", result.get(0));
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	
	}

	
	    /* 
	    * @Description: 机构获取旗下校园列表信息
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#getSchools(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getSchools(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			int count=0;
			String sql="SELECT * from schools WHERE oid=? limit ?,?";
			Object[] params=new Object[]{
					fenyeData.getOid(),
					fenyeData.getPage_num(),
					fenyeData.getPage_size()
					};
			List result=jdbcDao.queryForList(sql, params);
			//获取校园总数
			sql="SELECT count(*) from schools WHERE oid=?";
			params=new Object[]{
					fenyeData.getOid()
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
	    * @Description:学校获取旗下班级列表
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#getClasses(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getClasses(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			int count=0;
			String sql="SELECT * from classes where schoolId=? order by createDate limit ?,?";
			Object[] params=new Object[]{
					fenyeData.getSid(),
					fenyeData.getPage_num(),
					fenyeData.getPage_size()
					};
			List result=jdbcDao.queryForList(sql, params);
			//获取班级总数
			sql="SELECT count(*) from classes where schoolId=? ";
			params=new Object[]{
					fenyeData.getSid()
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
	    * @Description: 老师获取所属学校信息
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#getSchool(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getSchool(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql="SELECT t2.* FROM classes t1 LEFT JOIN schools t2 on t1.schoolId=t2.id where t1.id=?";
			Object[] params=new Object[]{fenyeData.getCid()};
			List result=jdbcDao.queryForList(sql, params);
			map.put("result", result.get(0));
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	
	
	    /* 
	    * @Description: 获取用户各种未读公告数
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#getUnreadNoticeNum(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getUnreadNoticeNum(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql;
			Object[] params;
			int ocount=0;
			int scount=0;
			int ccount=0;
			if (fenyeData.getType().equals("1")) {
				//组织机构获取未读校园公告数
				sql="SELECT COUNT(*) from notice t1 where EXISTS(SELECT t2.id from (SELECT id from schools where oid=(SELECT tid from members_relate where uid=?)) t2 where t1.from_id=t2.id) and not EXISTS(SELECT t3.tid from notice_read t3 where t1.id=t3.tid and uid=?) and t1.from_type in('2','3')";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				scount=jdbcDao.queryForInt(sql, params);
			}else if (fenyeData.getType().equals("2")||fenyeData.getType().equals("3")) {
				//园务或园长获取机构公告数
				sql="SELECT  COUNT(*) FROM notice t1 where ((t1.from_id=(SELECT oid from schools where id=(SELECT tid from members_relate  WHERE uid=?)) and t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and t2.uid=?)))  and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and t2.uid=?) AND t1.from_type='1' ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getId()
				};
				ocount=jdbcDao.queryForInt(sql, params);
				//园务或园长获取校园公告数
				sql="SELECT COUNT(*) FROM notice t1 WHERE t1.from_id=(SELECT tid from members_relate  WHERE uid=?) and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?) and t1.from_type in('2','3')";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				scount=jdbcDao.queryForInt(sql, params);
				//园务或园长获取班级公告数
				sql="SELECT COUNT(*) from notice t1 WHERE EXISTS(SELECT t2.id from (SELECT id from classes WHERE schoolId=(SELECT tid from members_relate  WHERE uid=?)) t2 where t2.id=t1.from_id) and t1.from_type='4' and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?)";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				ccount=jdbcDao.queryForInt(sql, params);
			}else if (fenyeData.getType().equals("4")) {
				//老师获取校园公告数
				sql="SELECT COUNT(*) FROM notice t1 WHERE t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1)) and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?) and t1.from_type in('2','3') ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				scount=jdbcDao.queryForInt(sql, params);
				
				//老师获取班级公告数
				sql="SELECT COUNT(DISTINCT(t1.id)) FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4' and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?)) t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getId()
				};
				ccount=jdbcDao.queryForInt(sql, params);
			}else if (fenyeData.getType().equals("5")) {
				//家长获取校园公告数
				sql="SELECT COUNT(*) FROM notice t1 WHERE t1.to_type='0' and t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1)) and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?) and t1.from_type in('2','3') ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				scount=jdbcDao.queryForInt(sql, params);
				
				//家长获取班级公告数
				sql="SELECT COUNT(DISTINCT(t1.id)) FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4' and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?)) t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getId()
				};
				ccount=jdbcDao.queryForInt(sql, params);
			}
			
			map.put("ocount", ocount);
			map.put("scount", scount);
			map.put("ccount", ccount);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	    /* 
	    * @Description: 获取未读公告数据
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#getUnreadNoticeData(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getUnreadNoticeData(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String searchType=fenyeData.getSearchType();
			String type=fenyeData.getType();
			String sql;
			Object[] params;
			List result=null;
			//园长和园务获取组织机构发布公告
			if (searchType.equals("o")&&(type.equals("2")||type.equals("3"))) {				
				//园务或园长获取机构公告数
				sql="SELECT  * FROM notice t1 where (t1.from_id=(SELECT oid from schools where id=(SELECT tid from members_relate  WHERE uid=?)) and t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?) AND from_type='1'";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getId()
				};
				result=jdbcDao.queryForList(sql, params);
			}else if (searchType.equals("s")&&(type.equals("2")||type.equals("3"))) {
				//园务或园长获取校园公告
				sql="SELECT * FROM notice t1 WHERE t1.from_id=(SELECT tid from members_relate  WHERE uid=?) and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?) and t1.from_type in('2','3') and t1.from_uid!=?";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getId()
				};
				result=jdbcDao.queryForList(sql, params);
			}else if (searchType.equals("c")&&(type.equals("2")||type.equals("3"))) {
				//园务或园长获取班级公告
				sql="SELECT * from notice t1 WHERE EXISTS(SELECT t2.id from (SELECT id from classes WHERE schoolId=(SELECT tid from members_relate  WHERE uid=?)) t2 where t2.id=t1.from_id) and t1.from_type='4' and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?)";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				result=jdbcDao.queryForList(sql, params);
				
			}else if (searchType.equals("s")&&type.equals("1")) {
				//机构获取校园公告
				sql="SELECT * from notice t1 where EXISTS(SELECT t2.id from (SELECT id from schools where oid=(SELECT tid from members_relate where uid=?)) t2 where t1.from_id=t2.id) and not EXISTS(SELECT t3.tid from notice_read t3 where t1.id=t3.tid and uid=?) and t1.from_type in('2','3')";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				result=jdbcDao.queryForList(sql, params);
				
			}else if (searchType.equals("s")&&type.equals("4")) {
				//老师获取校园的公告
				sql="SELECT * FROM notice t1 WHERE t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1)) and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?) and t1.from_type in('2','3') ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				result=jdbcDao.queryForList(sql, params);
			}else if (searchType.equals("c")&&type.equals("4")) {
				//老师获取班级公告
				sql="SELECT * FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4' and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?)) t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getId()
				};
				result=jdbcDao.queryForList(sql, params);
				
			}else if (searchType.equals("c")&&type.equals("5")) {
				//家长获取班级公告
				sql="SELECT * FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4' and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?)) t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getId()
				};
				result=jdbcDao.queryForList(sql, params);

			}else if (searchType.equals("s")&&type.equals("5")) {
				//家长获取校园公告
				sql="SELECT * FROM notice t1 WHERE t1.to_type='0' and t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1)) and not EXISTS(SELECT t2.tid from notice_read t2 where t1.id=t2.tid and uid=?) and t1.from_type in('2','3') ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				result=jdbcDao.queryForList(sql, params);
			}			
			map.put("result", result);	
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	    /* 
	    * @Description: 获取全部公告列表
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#getNoticeList(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getNoticeList(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String searchType=fenyeData.getSearchType();
			String type=fenyeData.getType();
			String sql;
			Object[] params;
			List result=null;
			int count=0;
			Properties prop=getProp("/ftp.properties");
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("noticeaddress");
			//园长和园务获取组织机构发布公告
			if (searchType.equals("o")&&(type.equals("2")||type.equals("3"))) {				
				//园务或园长获取机构公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id where (t1.from_id=(SELECT oid from schools where id=(SELECT tid from members_relate  WHERE uid=?)) and t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?))  AND from_type='1' GROUP BY t1.id ORDER BY t1.publishDate DESC LIMIT ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT  count(*) FROM notice t1  where (t1.from_id=(SELECT oid from schools where id=(SELECT tid from members_relate  WHERE uid=?)) and t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?))  AND from_type='1'" ;
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&(type.equals("2")||type.equals("3"))) {
				//园务或园长获取校园公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id WHERE t1.from_id=(SELECT tid from members_relate  WHERE uid=?)  and t1.from_type in('2','3') GROUP BY t1.id ORDER BY t1.publishDate DESC LIMIT ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT  count(*) FROM notice t1  WHERE t1.from_id=(SELECT tid from members_relate  WHERE uid=?)  and t1.from_type in('2','3') ";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("c")&&(type.equals("2")||type.equals("3"))) {
				//园务或园长获取班级公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id WHERE EXISTS(SELECT t2.id from (SELECT id from classes WHERE schoolId=(SELECT tid from members_relate  WHERE uid=?)) t2 where t2.id=t1.from_id) and t1.from_type='4' GROUP BY t1.id ORDER BY t1.publishDate DESC LIMIT ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT   count(*) FROM notice t1 WHERE EXISTS(SELECT t2.id from (SELECT id from classes WHERE schoolId=(SELECT tid from members_relate  WHERE uid=?)) t2 where t2.id=t1.from_id) and t1.from_type='4'";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&type.equals("1")) {
				//机构获取校园公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id where t1.from_id=?  and t1.from_type in('2','3') GROUP BY t1.id ORDER BY t1.publishDate DESC LIMIT ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT  count(*) FROM notice t1 where t1.from_id=?  and t1.from_type in('2','3')";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("o")&&type.equals("1")) {
				//机构获取机构公告
				//机构获取校园公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id where t1.from_id=(SELECT tid from members_relate WHERE uid=?) and t1.from_type='1' GROUP BY t1.id ORDER BY t1.publishDate DESC LIMIT ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT  count(*) FROM notice t1  where t1.from_id=(SELECT tid from members_relate WHERE uid=?) and t1.from_type='1' ";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&type.equals("4")) {
				//老师获取校园的公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id WHERE t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1))  and t1.from_type in('2','3') GROUP BY t1.id ORDER BY t1.publishDate DESC LIMIT ?,? ";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT  count(*) FROM notice t1 WHERE t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1))  and t1.from_type in('2','3') ";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("c")&&type.equals("4")) {
				//老师获取班级公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM (SELECT * FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4') t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ORDER BY t1.publishDate DESC LIMIT ?,?) t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id GROUP BY t1.id";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT count(*) FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4') t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("c")&&type.equals("5")) {
				//家长获取班级公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM (SELECT * FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4') t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ORDER BY t1.publishDate DESC LIMIT ?,?) t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id  GROUP BY t1.id";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);

				sql="SELECT count(*) FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4') t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&type.equals("5")) {
				//家长获取校园公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id  WHERE t1.to_type='0' and t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1)) and t1.from_type in('2','3') GROUP BY t1.id ORDER BY t1.publishDate DESC LIMIT ?,? ";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT count(*) FROM notice t1  WHERE t1.to_type='0' and t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1)) and t1.from_type in('2','3')";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}			
			map.put("result", result);	
			map.put("count", count);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	    /* 
	    * @Description: 获取公告读取状态
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#getNoticeReadInfo(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getNoticeReadInfo(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			Properties prop=getProp("/ftp.properties");
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
			String sql="SELECT t2.*,CONCAT(?,t2.headImage) as 'ftpimage' from notice_read t1 LEFT JOIN members t2 on t1.uid=t2.id  WHERE tid=?";
			Object[] params=new Object[]{
					ftpurl,
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
	    * @Description: 公告置顶
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#noticeToTop(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> noticeToTop(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql="";
			Object[] params=null;
			if (fenyeData.getType().equals("1")) {
				sql="UPDATE notice set toTopDate=? where id=?";
				params=new Object[]{
						format.format(new Date()),
						fenyeData.getId()
				};
				jdbcDao.update(sql, params);
			}else if (fenyeData.getType().equals("0")) {
				sql="UPDATE notice set toTopDate=null where id=?";
				params=new Object[]{
						fenyeData.getId()
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
	    * @Description: 
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#getNoticeToTopList(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getNoticeToTopList(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String searchType=fenyeData.getSearchType();
			String type=fenyeData.getType();
			String sql;
			Object[] params;
			List result=null;
			int count=0;
			Properties prop=getProp("/ftp.properties");
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("noticeaddress");
			//园长和园务获取组织机构发布公告
			if (searchType.equals("o")&&(type.equals("2")||type.equals("3"))) {				
				//园务或园长获取机构公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id where (t1.from_id=(SELECT oid from schools where id=(SELECT tid from members_relate  WHERE uid=?)) and t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?))  AND from_type='1' GROUP BY t1.id ORDER BY t1.toTopDate desc,t1.publishDate desc LIMIT ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT  count(*) FROM notice t1  where (t1.from_id=(SELECT oid from schools where id=(SELECT tid from members_relate  WHERE uid=?)) and t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?))  AND from_type='1'";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId(),
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&(type.equals("2")||type.equals("3"))) {
				//园务或园长获取校园公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id WHERE t1.from_id=(SELECT tid from members_relate  WHERE uid=?)  and t1.from_type in('2','3') GROUP BY t1.id ORDER BY t1.toTopDate desc,t1.publishDate desc LIMIT ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="SELECT  count(*) FROM notice t1  WHERE t1.from_id=(SELECT tid from members_relate  WHERE uid=?)  and t1.from_type in('2','3')";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("c")&&(type.equals("2")||type.equals("3"))) {
				//园务或园长获取班级公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id WHERE EXISTS(SELECT t2.id from (SELECT id from classes WHERE schoolId=(SELECT tid from members_relate  WHERE uid=?)) t2 where t2.id=t1.from_id) and t1.from_type='4' GROUP BY t1.id ORDER BY t1.toTopDate desc,t1.publishDate desc LIMIT ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT   count(*) FROM notice t1 WHERE EXISTS(SELECT t2.id from (SELECT id from classes WHERE schoolId=(SELECT tid from members_relate  WHERE uid=?)) t2 where t2.id=t1.from_id) and t1.from_type='4'";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&type.equals("1")) {
				//机构获取校园公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id where t1.from_id=?  and t1.from_type in('2','3') GROUP BY t1.id order by t1.toTopDate desc,t1.publishDate desc  LIMIT ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT  count(*) FROM notice t1 where t1.from_id=?  and t1.from_type in('2','3')";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("o")&&type.equals("1")) {
				//机构获取机构公告
				//机构获取校园公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id where t1.from_id=(SELECT tid from members_relate WHERE uid=?) and t1.from_type='1' GROUP BY t1.id ORDER BY t1.toTopDate desc,t1.publishDate desc LIMIT ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT  count(*) FROM notice t1  where t1.from_id=(SELECT tid from members_relate WHERE uid=?) and t1.from_type='1'";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&type.equals("4")) {
				//老师获取校园的公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id WHERE t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1))  and t1.from_type in('2','3') GROUP BY t1.id ORDER BY t1.toTopDate desc,t1.publishDate desc LIMIT ?,? ";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT  count(*) FROM notice t1 WHERE t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1))  and t1.from_type in('2','3')";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("c")&&type.equals("4")) {
				//老师获取班级公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM (SELECT * FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4') t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ORDER BY t1.toTopDate desc,t1.publishDate desc LIMIT ?,?) t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id GROUP BY t1.id";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT count(*) FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4') t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("c")&&type.equals("5")) {
				//家长获取班级公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM (SELECT * FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4') t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?)) ORDER BY t1.toTopDate desc,t1.publishDate desc LIMIT ?,?) t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id GROUP BY t1.id ";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);

				sql="SELECT count(*) FROM (SELECT t1.*,t2.tid from notice t1 RIGHT JOIN members_relate t2 on t1.from_uid=t2.uid where t2.uid=? and t1.from_type='4') t1 WHERE (t1.to_type='0') or (t1.to_type='1' and EXISTS(SELECT t2.tid from notice_unread t2  where t1.id=t2.tid and uid=?))";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}else if (searchType.equals("s")&&type.equals("5")) {
				//家长获取校园公告
				sql="SELECT  t1.*,t4.realName,t3.state,CONCAT(?,t1.pic) as 'ftpimage' FROM notice t1 LEFT JOIN notice_read t3 on t1.id=t3.tid LEFT JOIN members t4 on t1.from_uid=t4.id  WHERE t1.to_type='0' and t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1)) and t1.from_type in('2','3') GROUP BY t1.id  ORDER BY t1.toTopDate desc,t1.publishDate desc LIMIT ?,? ";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				sql="SELECT count(*) FROM notice t1  WHERE t1.to_type='0' and t1.from_id=(SELECT schoolId from classes where id=(SELECT tid from members_relate where uid=? LIMIT 0,1)) and t1.from_type in('2','3')";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
			}			
			map.put("result", result);	
			map.put("count", count);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	
	    /* 
	    * @Description: 删除公告
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.NoticeService#deleteNotice(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> deleteNotice(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			//删除公告表数据
			String sql="DELETE from notice WHERE id=?";
			Object[] params=new Object[]{
					fenyeData.getId()
			};
			jdbcDao.update(sql, params);
			//删除未读公告表数据
			sql="DELETE from notice_unread where tid=?";
			jdbcDao.update(sql, params);
			//删除已读公告表数据
			sql="DELETE from notice_read where tid=?";
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}

	}


	
	    /* 
	    * @Description: 更新公告
	    * 
	    * @param notice
	    * @return
	    * @throws Exception
	    * @see com.lovebaby.service.NoticeService#updateTxtNotice(com.lovebaby.pojo.Notice)
	    */
	    
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> updateNotice(Notice notice) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();		
			String sql=null;
			Object[] params=null;
			String id=GetUuid.getUuid();
			if (notice.getContentType()==null||notice.getContentType().equals("")) {
				notice.setContentType("0");
			}
			//插入公告	
			if (notice.getPics()!=null) {	
				//有图片,文本公告
				//跟新公告
				sql="update notice set title=?,content=?,to_type=?,from_id=?,from_type=?,publishDate=?,from_uid=?,contentType=?,pic=?,toTopDate=? WHERE id=?";
				//(id,title,content,to_type,from_id,from_type,publishDate)
				params=new Object[]{
						notice.getTitle(),
						notice.getContent(),
						notice.getTo_type(),
						notice.getFrom_id(),
						notice.getFrom_type(),
						format.format(new Date()),
						notice.getUid(),
						notice.getContentType(),
						notice.getPics().get(0),
						notice.getToTop().equals("1")?format.format(new Date()):null,
						notice.getId()
				};
				jdbcDao.update(sql, params);
				//删除原公告图片
				sql="DELETE from pictures where tid=? and type='2'";
				params=new Object[]{
						notice.getId()
				};
				jdbcDao.update(sql, params);
				//公告添加图片列表
				sql="insert into pictures(id,picname,type,tid) values(?,?,?,?)";
				List pics=notice.getPics();
				List list1=new ArrayList<>();
				for (int i = 0; i < pics.size(); i++) {
					list1.add(new Object[]{
							GetUuid.getUuid(),
							pics.get(i),
							"2",
							id
					});
				}	
				jdbcDao.batchBySimpleJdbcTemplate(sql, list1);	
			}else {
				//无图片，html公告
				sql="update notice set title=?,content=?,to_type=?,from_id=?,from_type=?,publishDate=?,from_uid=?,contentType=?,pic=?,textContent=?,toTopDate=? WHERE id=?";
				//(id,title,content,to_type,from_id,from_type,publishDate)
				params=new Object[]{
						notice.getTitle(),
						notice.getContent(),
						notice.getTo_type(),
						notice.getFrom_id(),
						notice.getFrom_type(),
						format.format(new Date()),
						notice.getUid(),
						notice.getContentType(),
						notice.getPicname()==null?"notice.png":notice.getPicname(),
						notice.getTextContent(),
						notice.getToTop().equals("1")?format.format(new Date()):null,
						notice.getId()
				};
				jdbcDao.update(sql, params);
			}	
			//删除读取记录
			sql="DELETE from notice_unread where tid=?";
			params=new Object[]{
					notice.getId()
			};
			jdbcDao.update(sql, params);
			sql="DELETE from notice_read WHERE tid=?";
			jdbcDao.update(sql, params);
			//只有机构人或者老师时，且发送对象为指定对象，才添加接收人列表		
			if ((notice.getTo_type().equals("1")&&notice.getFrom_type().equals("1"))||notice.getTo_type().equals("1")&&notice.getFrom_type().equals("4")) {
				String[] ids=notice.getToids().split(",");
				List list2=new ArrayList<>();
				sql="insert into notice_unread(id,uid,tid) values(?,?,?)";
				for (int i = 0; i < ids.length; i++) {
					list2.add(new Object[]{
							GetUuid.getUuid(),
							ids[i],
							id
							});
				}
				jdbcDao.batchBySimpleJdbcTemplate(sql, list2);
			}
			
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	}

	
	  
}
