
    /**  
    * @Title: AuthenticationServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月16日
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
import com.lovebaby.util.GetUuid;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
/**
    * @ClassName: AuthenticationServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年11月16日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private JdbcDao jdbcDao;
	private static Logger log=Logger.getLogger(AuthenticationServiceImpl.class.getName());

	
	/* 
	* @Description: 获取认证列表
	* 
	* @param fenyeData
	* @return
	* @see com.lovebaby.service.AuthenticationService#getAuthenticationList(com.lovebaby.pojo.FenyeData)
	*/


	public AuthenticationServiceImpl() {
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


	@Override
	public Map<String, Object> getAuthenticationList(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql=null;
			Object[] params=null;
			List result=null;
			int count=0;
			Properties prop=getProp();
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
			
			if (fenyeData.getType().equals("1")) {
				//组织获取非独立园所园长认证列表
				sql="select *,CONCAT(?,portrait) as 'ftpimage' from authentication where type=2 and tid=? and status=? order by date desc limit ?,? ";
				params=new Object[]{
						ftpurl,
						fenyeData.getTid(),
						fenyeData.getStatus(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
						};
				result=jdbcDao.queryForList(sql, params);
				
				//获取数据总数
				sql="select count(*) from authentication where type=2 and tid=? and status=?";
				params=new Object[]{
						fenyeData.getTid(),
						fenyeData.getStatus()
						};
				count=jdbcDao.queryForInt(sql, params);
			}else if (fenyeData.getType().equals("2")) {
				//园长获取园务和老师认证列表
				sql="select *,CONCAT(?,portrait) as 'ftpimage' from authentication t1 where ((t1.type='3' and t1.tid=?) or (t1.type='4' and EXISTS(SELECT t2.id FROM (SELECT id from classes WHERE schoolId=?) t2 where t1.tid=t2.id))) and t1.status=? order by t1.date desc limit ?,?;";
				params=new Object[]{
						ftpurl,
						fenyeData.getTid(),
						fenyeData.getTid(),
						fenyeData.getStatus(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
						};
				result=jdbcDao.queryForList(sql, params);
				
				//获取数据总数
				sql="select count(*) from authentication t1 where ((t1.type='3' and t1.tid=?) or (t1.type='4' and EXISTS(SELECT t2.id FROM (SELECT id from classes WHERE schoolId=?) t2 where t1.tid=t2.id))) and t1.status=?";
				params=new Object[]{
						fenyeData.getTid(),
						fenyeData.getTid(),
						fenyeData.getStatus()
						};
				count=jdbcDao.queryForInt(sql, params);
				
			}else if (fenyeData.getType().equals("3")) {
				//园务获取老师认证列表
				sql="select *,CONCAT(?,portrait) as 'ftpimage' from authentication t1 where t1.type='4' and EXISTS(SELECT t2.id FROM (SELECT id from classes WHERE schoolId=?) t2 where t1.tid=t2.id) and t1.status=? order by t1.date desc limit ?,?;";
				params=new Object[]{
						ftpurl,
						fenyeData.getTid(),
						fenyeData.getStatus(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
						};
				result=jdbcDao.queryForList(sql, params);
				
				sql="select count(*) from authentication t1 where t1.type='4' and EXISTS(SELECT t2.id FROM (SELECT id from classes WHERE schoolId=?) t2 where t1.tid=t2.id) and t1.status=?";
				params=new Object[]{
						fenyeData.getTid(),
						fenyeData.getStatus()
						};
				count=jdbcDao.queryForInt(sql, params);
			}else if (fenyeData.getType().equals("4")) {
				//老师认证所有班级家长列表
				sql="select *,CONCAT(?,portrait) as 'ftpimage' from authentication t1 where EXISTS(SELECT t5.tid from (SELECT t3.tid from members t2 JOIN members_relate t3 on t2.id=t3.uid where t2.id=?) t5 WHERE t5.tid=t1.tid) and t1.type='5' and status=? order by date desc  limit ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getId(),
						fenyeData.getStatus(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
						};
				result=jdbcDao.queryForList(sql, params);
				sql="select count(*) from authentication t1 where EXISTS(SELECT t5.tid from (SELECT t3.tid from members t2 JOIN members_relate t3 on t2.id=t3.uid where t2.id=?) t5 WHERE t5.tid=t1.tid) and t1.type='5' and status=?";
				params=new Object[]{
						fenyeData.getId(),
						fenyeData.getStatus()
						};
				count=jdbcDao.queryForInt(sql, params);
			}else if (fenyeData.getType().equals("6")) {
				//系统管理员获取机构和独立园所审核列表
				sql="select *,CONCAT(?,portrait) as 'ftpimage' from authentication where  tid='0' and status=? order by date desc limit ?,?";
				params=new Object[]{
						ftpurl,
						fenyeData.getStatus(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
						};
				result=jdbcDao.queryForList(sql, params);
				sql="select count(*) from authentication where  type='1' and status=?";
				params=new Object[]{
						fenyeData.getStatus()
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
	    * @Description: 认证审批
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.AuthenticationService#AuthenticationCheck(com.lovebaby.pojo.FenyeData)
	    */
	    
	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> AuthenticationCheck(FenyeData fenyeData) throws Exception {
		try {
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//无声调设置
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);//转换后全小写输出
		Map<String, Object> map=new HashMap<>();
		String sql=null;
		Object[] params=null;		
			if (fenyeData.getStatus().equals("1")) {
				//通过审核，获取认证信息
				sql="SELECT * from authentication where id=?";
				params=new Object[]{fenyeData.getAid()};
				Map<String, Object> data=jdbcDao.queryForMap(sql, params);
				String tid=(String)data.get("tid");
				//申请类型
				String type=(String)data.get("type");
				//申请人id
				String uid=(String)data.get("fromid");
				String pinyin=null;
				//获取审核状态
				String status=(String) data.get("status");
				if (!status.equals("1")) {
					//未被审核通过
					//通过审核,修改认证信息
					sql="update authentication set status=1 where id=?";
					params=new Object[]{fenyeData.getAid()};						
					jdbcDao.update(sql, params);
					if (!type.equals("5")) {
						if (type.equals("1")) {
							try {
								pinyin = PinyinHelper.toHanyuPinyinString((String)data.get("name"), format, "");
							} catch (BadHanyuPinyinOutputFormatCombination e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//机构审批
							//向机构表插入机构数据				
							sql="insert into organization(id,name,area,createDate,pinyin) values(?,?,?,?,?)";
							String id=GetUuid.getUuid();
							params=new Object[]{
									id,
									(String)data.get("name"),
									(String)data.get("area"),
									dateformat.format(new Date()),
									pinyin
							};
							jdbcDao.update(sql, params);
							//添加机构成功，修改用户信息
							sql="UPDATE members SET type=1 where id=?";
							params=new Object[]{
									uid
							};
							jdbcDao.update(sql, params);
							//判断关系是否存在
							sql="SELECT count(*) from members_relate where uid=? and tid=?";
							params=new Object[]{
									uid,
									id
							};
							int count=jdbcDao.queryForInt(sql, params);
							//为用户添加关系列表数据
							if (count==0) {							
								sql="insert into members_relate(id,uid,tid,type) values(?,?,?,?)";
								params=new Object[]{
										GetUuid.getUuid(),
										uid,
										id,
										type
								};
								jdbcDao.update(sql, params);		
							}
						}else if (type.equals("2")) {
							try {
								pinyin = PinyinHelper.toHanyuPinyinString((String)data.get("name"), format, "");
							} catch (BadHanyuPinyinOutputFormatCombination e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//园所审批
							//向校园表插入校园数据
							sql="insert into schools(id,schoolName,area,oid,createDate,pinyin) values(?,?,?,?,?,?)";
							String id=GetUuid.getUuid();
							params=new Object[]{
									id,
									(String)data.get("name"),
									(String)data.get("area"),
									tid,
									dateformat.format(new Date()),
									pinyin
							};
							jdbcDao.update(sql, params);
							//添加校园成功，修改用户信息
							sql="UPDATE members SET type=2 where id=?";
							params=new Object[]{
									uid
							};
							jdbcDao.update(sql, params);
							//判断关系是否存在
							sql="SELECT count(*) from members_relate where uid=? and tid=?";
							params=new Object[]{
									uid,
									id
							};
							int count=jdbcDao.queryForInt(sql, params);
							//为用户添加关系列表数据
							if (count==0) {							
								sql="insert into members_relate(id,uid,tid,type) values(?,?,?,?)";
								params=new Object[]{
										GetUuid.getUuid(),
										uid,
										id,
										type
								};
								jdbcDao.update(sql, params);																		
							}
						}else if (type.equals("3")||type.equals("4")) {
							//园务审批,老师审批
							//修改用户信息
							sql="UPDATE members SET type=? where id=?";
							params=new Object[]{
									type,
									uid
							};
							jdbcDao.update(sql, params);
							//判断关系是否存在
							sql="SELECT count(*) from members_relate where uid=? and tid=?";
							params=new Object[]{
									uid,
									tid
							};
							int count=jdbcDao.queryForInt(sql, params);
							//为用户添加关系列表数据
							if (count==0) {							
								sql="insert into members_relate(id,uid,tid,type) values(?,?,?,?)";
								params=new Object[]{
										GetUuid.getUuid(),
										uid,
										tid,
										type
								};
								jdbcDao.update(sql, params);								
							}
						}
					}else if (type.equals("5")) {
						//家长审批
						//修改用户信息
						sql="UPDATE members SET type=5 where id=?";
						params=new Object[]{
								uid
						};
						jdbcDao.update(sql, params);
						//判断关系是否存在
						sql="SELECT count(*) from members_relate where uid=? and tid=?";
						params=new Object[]{
								uid,
								tid
						};
						int count=jdbcDao.queryForInt(sql, params);
						//为用户添加关系列表数据
						if (count==0) {						
							sql="insert into members_relate(id,uid,tid,type) values(?,?,?,?)";
							params=new Object[]{
									GetUuid.getUuid(),
									uid,
									tid,
									type
							};
							jdbcDao.update(sql, params);							
						}
					}
				}
			}else if (fenyeData.getStatus().equals("2")) {
				//审核拒绝
				sql="update authentication set status='2' where id=?";
				params=new Object[]{fenyeData.getAid()};
				jdbcDao.update(sql, params);
			}		
		return map;
	}


    /* 
    * @Description: 获取未认证请求数
    * 
    * @param fenyeData
    * @return
    * @throws Exception
    * @see com.lovebaby.service.AuthenticationService#getAuthenticationNum(com.lovebaby.pojo.FenyeData)
    */
    
	@Override
	public Map<String, Object> getAuthenticationNum(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//未审核申请数
			int applycount=0;
			String sql="";		
			Object[] params=null;
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
			map.put("applycount", applycount);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}
		
	    /* 
	    * @Description: 给家长添加宝宝
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.AuthenticationService#chooseBaby(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> chooseBaby(FenyeData fenyeData) throws Exception {
		try {
			Map<String, Object> map=new HashMap<>();
			String sql=null;
			Object[] params=null;
			//获取宝宝id列表
			String[] bids=fenyeData.getBid().split(",");
			//给家长添加宝宝
			for (int i = 0; i < bids.length; i++) {
				//判断该宝宝是否已添加
				String bid=bids[i];
				sql="SELECT count(*) from members_baby WHERE bid=? and type='1' and uid=? ";
				params=new Object[]{
						fenyeData.getBid(),
						bid
				};	
				int count=jdbcDao.queryForInt(sql, params);
				//未添加，则添加宝宝
				if (count==0) {
					sql="insert into members_baby(id,bid,uid,babyrelate)";
					params=new Object[]{
							GetUuid.getUuid(),
							bid,
							fenyeData.getUid(),
							fenyeData.getType()
					};	
					jdbcDao.update(sql, params);	
				}
			}
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
	public Properties getProp() {
		Properties prop = new Properties();
		InputStream fis = this.getClass().getResourceAsStream("/ftp.properties");
		try {
			prop.load(fis);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 将属性文件流装载到Properties对象中
		return prop;
	}


	
}
