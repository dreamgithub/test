
    /**  
    * @Title: LoginServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月9日
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
import com.lovebaby.pojo.User;
import com.lovebaby.util.GetUuid;
import com.lovebaby.util.MD5;

import io.rong.ApiHttpClient;
import io.rong.models.FormatType;
import io.rong.models.SdkHttpResult;
import net.sf.json.JSONObject;

/**
    * @ClassName: LoginServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年11月9日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="loginService")
public class LoginServiceImpl implements LoginService {
	@Autowired
	private JdbcDao jdbcDao;
	private static Logger log=Logger.getLogger(LoginServiceImpl.class.getName());

	
	/* 
	* @Description: 更改用户登录次数
	* 
	* @return
	* @see com.lovebaby.service.LoginService#updateTimes()
	*/

	public LoginServiceImpl() {
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
	public void updateInfo(User user) throws Exception {
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql="update members set times=times+1,loginDate=?,phonetype=?,token=? where telephone=?";
			Object[] params=new Object[]{
					format.format(new Date()),
					user.getPhonetype(),
					user.getToken(),
					user.getTelephone()
					};
			jdbcDao.update(sql, params);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	    /* 
	    * @Description: 获取用户信息
	    * 
	    * @param user
	    * @return
	    * @see com.lovebaby.service.LoginService#getUserInfo(com.lovebaby.pojo.User)
	    */
	    
	@Override
	public Map<String, Object> getUserInfo(User user) throws Exception {
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, Object> map=new HashMap<>();
			List schools;
			List classes = null;
			String recordStatus=null;
			String sql=null;
			Object[] params=null;
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
			if (user.getId()==null||user.getId().equals("")) {
				sql="select *, CONCAT(?,headImage) as 'ftpimage' from members where telephone=?";
				params=new Object[]{headimgae,user.getTelephone()};
			}else {
				sql="select *, CONCAT(?,headImage) as 'ftpimage' from members where id=?";
				params=new Object[]{headimgae,user.getId()};
			}
			Map<String, Object> result=jdbcDao.queryForMap(sql, params);
			String uid=(String) result.get("id");
			user.setId(uid);
			sql="select tid from members_relate where uid=?";
			String id=(String) result.get("id");
			params=new Object[]{id};
			List danwei=jdbcDao.queryForList(sql, params);
			//添加用户学校
			String type=(String) result.get("type");
			if (type.equals("1")) {
				//机构获取旗下校园信息
				sql="SELECT * FROM schools t1 WHERE EXISTS(SELECT tid from members_relate t2 WHERE uid=? and t1.oid=t2.tid)";
				params=new Object[]{user.getId()};
				schools=jdbcDao.queryForList(sql, params);
			}else if (type.equals("2")||type.equals("3")) {
				//园务或园长获取校园
				sql="SELECT * FROM schools t1 WHERE EXISTS(SELECT tid from members_relate t2 WHERE uid=? and t1.id=t2.tid)";
				params=new Object[]{user.getId()};
				schools=jdbcDao.queryForList(sql, params);
				//园务或园长获取管辖班级列表
				sql="SELECT * FROM classes WHERE schoolId=(SELECT tid FROM members_relate  where uid=?)";
				classes=jdbcDao.queryForList(sql, params);
			}else {
				//老师，家长，普通游客获取校园
				sql="SELECT * from schools where id=(SELECT t1.schoolId FROM classes t1 WHERE EXISTS(SELECT tid from members_relate t2 WHERE uid=? and t1.id=t2.tid) GROUP BY t1.schoolId)";
				params=new Object[]{user.getId()};
				schools=jdbcDao.queryForList(sql, params);
				//老师和家长获取所辖班级
				sql="SELECT * FROM classes t1 WHERE EXISTS(SELECT t2.tid from (SELECT tid FROM members_relate  where uid=?) t2 WHERE t1.id=t2.tid)";
				classes=jdbcDao.queryForList(sql, params);
			}
			if (type.equals("5")) {
				String babyimage=prop.getProperty("url")+"/"+prop.getProperty("babyaddress");
				//家长获取宝宝列表
				sql="SELECT *,CONCAT(?,pic) as 'ftpimage' from babies t1 where EXISTS(SELECT * from (SELECT bid from members_baby where uid=?) t2 WHERE t1.id=t2.bid)";
				params=new Object[]{
						babyimage,
						user.getId()
						};
				List babies=jdbcDao.queryForList(sql, params);
				map.put("babies", babies);
			}
			//添加用户登录记录
			sql="INSERT into loginrecord(id,mid,logintime,loginarea,phoneType) VALUES(?,?,?,?,?)";
			params=new Object[]{
					GetUuid.getUuid(),
					user.getId(),
					format.format(new Date()),
					user.getLoginarea(),
					user.getPhonetype()
					
			};
			jdbcDao.update(sql, params);
			//添加申请认证记录情况信息
			//先判断用户是否有正在审核的申请记录
			int status=0;
			sql="SELECT count(*) from authentication where fromid=? AND `status`='0'";
			params=new Object[]{user.getId()};
			status=jdbcDao.queryForInt(sql, params);
			if (status==0) {
				//没有正在审核的记录，在判断是否有认证通过的记录
				sql="SELECT count(*) from authentication where fromid=? AND `status`='1'";
				params=new Object[]{user.getId()};
				status=jdbcDao.queryForInt(sql, params);
				if (status==0) {
					//没有通过的记录，在判断是否有被拒绝的记录
					sql="SELECT count(*) from authentication where fromid=? AND `status`='2'";
					params=new Object[]{user.getId()};
					status=jdbcDao.queryForInt(sql, params);
					if (status==0) {
						recordStatus="0";
					}else {
						recordStatus="3";
					}
				}else {
					recordStatus="2";
				}
			}else {
				recordStatus="1";
			}
			map.put("result", result);
			map.put("danwei", danwei);
			map.put("schools", schools);
			map.put("classes", classes);
			map.put("recordStatus", recordStatus);	
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	
	}


	
	    /* 
	    * @Description: 添加用户
	    * 
	    * @param user
	    * @return flag:0失败，1成功
	    * @see com.lovebaby.service.LoginService#addUser(com.lovebaby.pojo.User)
	    */
	    
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> addUser(User user) throws Exception {
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String id=GetUuid.getUuid();
			Map< String, Object> map=new HashMap<>();
			String sql="select * from members where telephone=?";
			Object[] params=new Object[]{user.getTelephone()};
			if (jdbcDao.queryForList(sql, params).size()==0) {
				//申请融云token
				Properties prop=getProp("/ftp.properties");
				String headimgae=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress")+"head.png";
				Properties prop1 =getProp("/rongyun.properties");
				String key = prop1.getProperty("key");
				String secret = prop1.getProperty("secret");
				//gettoken={"code":"200","result":{"code":200,"userId":"402880ef4a","token":"HmMUCZLtKV1P9SfsCd6fJZphuB4rrF4gTycOgrwuEuF/XO6iDrOFZWNsrT8W7bE12xORFZDx3ZFMwCfQA76qYT/XHqnrbW09"}}
				SdkHttpResult result = null;
				try {
					result = ApiHttpClient.getToken(key, secret, id, user.getTelephone(),headimgae, FormatType.json);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (result.getHttpCode()==200) {
					//申请token成功
					String data=result.getResult();
					JSONObject jsonObject = JSONObject.fromObject(data); 
					Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);
					sql="insert into members(id,telephone,password,createdate,registerType,chartToken) values(?,?,?,?,?,?)";
					params=new Object[]{
						id,
						user.getTelephone(),
						MD5.GetMD5Code(user.getPassword()),
						format.format(new Date()),
						user.getRegisterType(),
						(String)mapJson.get("token")
					};
					jdbcDao.update(sql, params);
					//获取用户信息
					user.setId(id);
					map=getUserInfo(user);
					map.put("flag", "1");
				}else {
					map.put("flag", "0");
					map.put("msg", "申请融云token失败！");
				}
				
				
			}else {
				map.put("flag", "0");
				map.put("msg", "该手机号已注册，请直接登录！");
			}
			
			
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 修改头像
	    * 
	    * @param name
	    * @return
	    * @see com.lovebaby.service.LoginService#upadtePortrait(java.lang.String)
	    */
	    
	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> upadtePortrait(User user) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql="update members set headImage=? where id=?";
			Object[] params=new Object[]{
					user.getHeadImage(),
					user.getId()
			};
			jdbcDao.update(sql, params);
			//获取用户
			sql="SELECT telephone,realName from members where id=?";
			params=new Object[]{
					user.getId()
			};
			Map< String, Object> member=jdbcDao.queryForMap(sql, params);
			String tel=(String) member.get("telephone");
			String name=(String) member.get("realName");
			//更新融云头像
			Properties prop1 =getProp("/rongyun.properties");
			String key = prop1.getProperty("key");
			String secret = prop1.getProperty("secret");
			//gettoken={"code":"200","result":{"code":200,"userId":"402880ef4a","token":"HmMUCZLtKV1P9SfsCd6fJZphuB4rrF4gTycOgrwuEuF/XO6iDrOFZWNsrT8W7bE12xORFZDx3ZFMwCfQA76qYT/XHqnrbW09"}}
			SdkHttpResult result = null;
			try {
				result = ApiHttpClient.refreshUser(key, secret, user.getId(),name==null?tel:name,user.getHeadImage(), FormatType.json);
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
	    * @Description: 修改姓名
	    * 
	    * @param user
	    * @return
	    * @see com.lovebaby.service.LoginService#upadteName(com.lovebaby.pojo.User)
	    */
	    
	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> upadteUserInfo(User user) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql="update members set realName=?,sex=?,birthday=? where id=?";
			Object[] params=new Object[]{
					user.getRealName(),				
					user.getSex(),
					user.getBirthday(),
					user.getId()
			};
			jdbcDao.update(sql, params);
			//获取用户
			sql="SELECT telephone,headImage from members where id=?";
			params=new Object[]{
					user.getId()
			};
			Map< String, Object> member=jdbcDao.queryForMap(sql, params);
			String tel=(String) member.get("telephone");
			String image=(String) member.get("headImage");
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress")+image;
			//更新融云名称
			Properties prop1 =getProp("/rongyun.properties");
			String key = prop1.getProperty("key");
			String secret = prop1.getProperty("secret");
			//gettoken={"code":"200","result":{"code":200,"userId":"402880ef4a","token":"HmMUCZLtKV1P9SfsCd6fJZphuB4rrF4gTycOgrwuEuF/XO6iDrOFZWNsrT8W7bE12xORFZDx3ZFMwCfQA76qYT/XHqnrbW09"}}
			SdkHttpResult result = null;
			try {
				result = ApiHttpClient.refreshUser(key, secret, user.getId(),user.getRealName()==null||user.getRealName().equals("")?tel:user.getRealName(),headimgae, FormatType.json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 更新密码
	    * 
	    * @param oldpsw
	    * @param newpsw
	    * @return
	    * @see com.lovebaby.service.LoginService#upadtePsw(java.lang.String, java.lang.String)
	    */
	    
	@Override
	public Map<String, Object> upadtePsw(User user) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql="update members set password=? where id=?";
			Object[] params=new Object[]{
					MD5.GetMD5Code(user.getPassword()),
					user.getId()
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	
	}


	
	    /* 
	    * @Description: 根据旧密码获取用户，来验证密码是否正确
	    * 
	    * @param user
	    * @param oldpsw
	    * @return
	    * @see com.lovebaby.service.LoginService#getUser(com.lovebaby.pojo.User, java.lang.String)
	    */
	    
	@Override
	public List getUser(User user, String oldpsw) throws Exception {
		try {
			String sql="select * from members where id=? and password=?";
			Object[] params=new Object[]{
					user.getId(),
					MD5.GetMD5Code(oldpsw)
			};
			List list=jdbcDao.queryForList(sql, params);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	
	}


	
	    /* 
	    * @Description: 用户验证
	    * 
	    * @param user
	    * @return
	    * @see com.lovebaby.service.LoginService#check(com.lovebaby.pojo.User)
	    */
	    
	@Override
	public List check(User user) throws Exception {
		try {
			
			String sql="SELECT * from members where telephone=? and `password`=?";
			Object[] params=new Object[]{
					user.getTelephone(),
					MD5.GetMD5Code(user.getPassword())
			};
			List result=jdbcDao.queryForList(sql, params);
			
			return result;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}
	

    /* 
    * @Description: 绑定视频账号
    * 
    * @param user
    * @return
    * @throws Exception
    * @see com.lovebaby.service.LoginService#bingVideoAccount(com.lovebaby.pojo.User)
    */
    
	@Override
	public Map<String, Object> bingVideoAccount(User user) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql="UPDATE members set videoUser=?,videoPwd=? WHERE id=?";
			Object[] params=new Object[]{
					user.getVideoUser(),
					user.getVideoPwd(),
					user.getId()
			};
			jdbcDao.update(sql, params);			
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
