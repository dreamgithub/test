
    /**  
    * @Title: ApplyRoleServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月13日
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
import com.lovebaby.pojo.Authentication;
import com.lovebaby.pojo.Babies;
import com.lovebaby.pojo.Classes;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.util.GetUuid;

import io.rong.ApiHttpClient;
import io.rong.models.FormatType;
import io.rong.models.Message;
import net.sf.json.JSONObject;

/**
    * @ClassName: ApplyRoleServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年11月13日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="applyRoleService")
public class ApplyRoleServiceImpl implements ApplyRoleService {
	@Autowired
	private JdbcDao jdbcDao;
	private static Logger log=Logger.getLogger(ApplyRoleServiceImpl.class.getName());
	
	
	
	/* 
	* @Description: 获取单位列表
	* 
	* @param fenyeData
	* @return
	* @see com.lovebaby.service.ApplyRoleService#getTypeList(com.lovebaby.pojo.FenyeData)
	*/


	public ApplyRoleServiceImpl() {
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
	public Map<String, Object> getTypeList(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql=null;
			List result=null;
			int count=0;
			Object[] params=new Object[]{
					fenyeData.getPage_num(),
					fenyeData.getPage_size()
			};
			if (fenyeData.getSearchType().equals("o")) {
				//获取数据列表
				sql="select * from organization limit ?,?";
				result=jdbcDao.queryForList(sql, params);
				//获取数据总数
				sql="select count(*) from organization";
				count=jdbcDao.queryForInt(sql, null);
			}else if (fenyeData.getSearchType().equals("s")) {
				sql="select * from schools limit ?,?";
				result=jdbcDao.queryForList(sql, params);
				sql="select count(*) from schools";
				count=jdbcDao.queryForInt(sql, null);
			}else if (fenyeData.getSearchType().equals("c")) {
				sql="select * from classes limit ?,?";
				result=jdbcDao.queryForList(sql, params);
				sql="select count(*) from classes";
				count=jdbcDao.queryForInt(sql, null);
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
	    * @Description: 添加认证信息
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.ApplyRoleService#addAuthentication(com.lovebaby.pojo.FenyeData)
	    */
	    
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> addAuthentication(Authentication authentication) throws Exception {
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List shenpi=null;
			Map< String, Object> map=new HashMap<>();
			String sql=null;
			Object[] params=null;
			if (authentication.getTid()==null||authentication.getTid().equals("")) {
				authentication.setTid("0");
			}
			//判断是否已经申请相同角色
			sql="SELECT count(*) from authentication WHERE fromid=? and type=? and tid=?";
			params=new Object[]{
					authentication.getFromid(),
					authentication.getType(),
					authentication.getTid()
			};
			int count=jdbcDao.queryForInt(sql, params);
			if (count==0) {				
				if (authentication.getType().equals("1")||authentication.getType().equals("2")||authentication.getType().equals("3")) {
					
					//机构，园长，园务申请
					sql="insert into authentication(id,tid,fromid,type,username,phone,date,area,name,portrait) values(?,?,?,?,?,?,?,?,?,?)";
					params=new Object[]{
							GetUuid.getUuid(),
							authentication.getTid(),
							authentication.getFromid(),
							authentication.getType(),
							authentication.getUsername(),
							authentication.getPhone(),
							format.format(new Date()),
							authentication.getArea(),
							authentication.getName(),
							authentication.getProtrait()
					};
					jdbcDao.update(sql, params);
				}else if (authentication.getType().equals("4")) {
					//老师申请
					sql="insert into authentication(id,tid,fromid,type,username,phone,date,name,portrait) values(?,?,?,?,?,?,?,?,?)";
					String[] ids=authentication.getTid().split(",");
					String[] names=authentication.getName().split(",");
					List<Object[]> list=new ArrayList<>();
					for (int i = 0; i < ids.length; i++) {
						list.add(new Object[]{
								GetUuid.getUuid(),
								ids[i],
								authentication.getFromid(),
								authentication.getType(),
								authentication.getUsername(),
								authentication.getPhone(),
								format.format(new Date()),	
								names[i],
								authentication.getProtrait()
						});
					}
					jdbcDao.batchBySimpleJdbcTemplate(sql, list);
					
				}else if (authentication.getType().equals("5")) {
					//家长申请			
					sql="insert into authentication(id,tid,fromid,type,username,phone,date,babyname,babyrelate,name,portrait) values(?,?,?,?,?,?,?,?,?,?,?)";
					params=new Object[]{
							GetUuid.getUuid(),
							authentication.getTid(),
							authentication.getFromid(),
							authentication.getType(),
							authentication.getUsername(),
							authentication.getPhone(),
							format.format(new Date()),
							authentication.getBabyname(),
							authentication.getBabyrelate(),
							authentication.getName(),
							authentication.getProtrait()
					};
					jdbcDao.update(sql, params);
					
					//获取审批人列表
					if (authentication.getTid()==null||authentication.getTid().equals("")) {
						//获取机构或独立园所审批人列表
						sql="SELECT id from members where type='6'";
						shenpi=jdbcDao.queryForList(sql, null);
					}else if (authentication.getType().equals("2")&&(authentication.getTid()!=null||!authentication.getTid().equals(""))) {
						//获取非独立园长审批人列表
						sql="SELECT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where t2.tid=? and t1.type='1'";
						params=new Object[]{
								authentication.getTid()
						};
						shenpi=jdbcDao.queryForList(sql, params);
					}else if (authentication.getType().equals("3")) {
						//园务获取审批人列表
						sql="SELECT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where t2.tid=? and t1.type='2'";
						params=new Object[]{
								authentication.getTid()
						};
						shenpi=jdbcDao.queryForList(sql, params);
					}else if (authentication.getType().equals("4")) {
						//老师获取审批人列表
						sql="SELECT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where t2.tid=(SELECT schoolId from classes WHERE id=?) and t1.type in('2','3')";
						params=new Object[]{
								authentication.getTid()
						};
						shenpi=jdbcDao.queryForList(sql, params);
					}else if (authentication.getType().equals("5")) {
						//家长获取审批人列表
						sql="SELECT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where t2.tid=? and t1.type='4'";
						params=new Object[]{
								authentication.getTid()
						};
						shenpi=jdbcDao.queryForList(sql, params);
					}
					List toids=new ArrayList<>();
					for (int i = 0; i < shenpi.size(); i++) {
						Map< String, Object> data=(Map<String, Object>) shenpi.get(i);
						String id=(String) data.get("id");
						toids.add(id);
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
						ApiHttpClient.publishSystemMessage(key, secret,"apply", toids, msg, "您有新的认证未处理，请及时认证！", null, FormatType.json);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 查询
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.ApplyRoleService#search(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> search(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql=null;
			Object[] params=null;
			List reslut=null;
			int count=0;
			if (fenyeData.getSearchType().equals("o")) {
				//机构列表查询
				sql="select * from organization where name like '%"+fenyeData.getVal()+"%' or pinyin like '%"+fenyeData.getVal()+"%' limit ?,?";
				params=new Object[]{
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				reslut=jdbcDao.queryForList(sql, params);
				
				//获取数据总数
				sql="select count(*) from organization where name like '%"+fenyeData.getVal()+"%' or pinyin like '%"+fenyeData.getVal()+"%'";
				count=jdbcDao.queryForInt(sql, null);
			}else if (fenyeData.getSearchType().equals("s")) {
				sql="select * from schools where schoolName like '%"+fenyeData.getVal()+"%' or pinyin like '%"+fenyeData.getVal()+"%' limit ?,?";
				params=new Object[]{
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				reslut=jdbcDao.queryForList(sql, params);
				
				sql="select count(*) from schools where schoolName like '%"+fenyeData.getVal()+"%' or pinyin like '%"+fenyeData.getVal()+"%'";
				count=jdbcDao.queryForInt(sql, null);
			}
			map.put("result", reslut);
			map.put("count", count);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	}



	
	    /* 
	    * @Description: 添加班级
	    * 
	    * @param classes
	    * @return
	    * @see com.lovebaby.service.ApplyRoleService#addClasses(com.lovebaby.pojo.Classes)
	    */
	    
	@Override
	public Map<String, Object> addClasses(Classes classes) throws Exception {
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String[] classNames=classes.getClassName().split(",");
			String schoolId=classes.getSchoolId();
			Map< String, Object> map=new HashMap<>();
			List<Object[]> list=new ArrayList<>();
			String sql="";
			String msg="";
			for (int i = 0; i < classNames.length; i++) {
				//判断班级是否存在
				sql="SELECT count(*) from classes WHERE className=? and schoolId=?";
				Object[] params=new Object[]{
						classNames[i],
						schoolId
				};
				int count=jdbcDao.queryForInt(sql, params);
				if (count==0) {			
					list.add(
							new Object[]{
									GetUuid.getUuid(),
									classNames[i],
									schoolId,
									format.format(new Date())
							});	
				map.put("flag", "1");
				}else {
					msg=msg+"["+classNames[i]+"]"+"已存在，请修改班级名!   ";
					map.put("flag", "0");
					map.put("msg", msg);
				}
			}
			sql="insert into classes(id,className,schoolId,createDate) values(?,?,?,?)";
			jdbcDao.batchBySimpleJdbcTemplate(sql, list);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	}



	
	    /* 
	    * @Description: 
	    * 
	    * @param babies
	    * @return
	    * @see com.lovebaby.service.ApplyRoleService#addBabies(com.lovebaby.pojo.Babies)
	    */
	    
	@Override
	public Map<String, Object> addBabies(Babies babies) throws Exception {
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String[] babyNames=babies.getBabyName().split(",");
			String[] sexes=babies.getSex().split(",");
			String[] nums=babies.getNum().split(",");
			String classid=babies.getClassid();
			Map< String, Object> map=new HashMap<>();
			List<Object[]> list=new ArrayList<>();
			String sql="insert into babies(id,babyName,sex,classid,num,createdate,pic) values(?,?,?,?,?,?,?)";;
			for (int i = 0; i < babyNames.length; i++) {
				list.add(
						new Object[]{
						GetUuid.getUuid(),
						babyNames[i],
						sexes[i],
						classid,
						nums[i],
						format.format(new Date()),
						babies.getPic()==null?"baby.png":babies.getPic()
				});			
			}
			jdbcDao.batchBySimpleJdbcTemplate(sql, list);
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



}
