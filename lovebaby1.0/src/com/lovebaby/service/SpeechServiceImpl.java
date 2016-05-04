
    /**  
    * @Title: SpeechServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月27日
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
import com.lovebaby.pojo.Comment;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Praise;
import com.lovebaby.pojo.Reply;
import com.lovebaby.pojo.Speech;
import com.lovebaby.util.GetUuid;
import io.rong.ApiHttpClient;
import io.rong.models.FormatType;
import io.rong.models.Message;
import net.sf.json.JSONObject;

/**
    * @ClassName: SpeechServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年11月27日
    *
    */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service(value="speechService")
public class SpeechServiceImpl implements SpeechService{


	@Autowired
	private JdbcDao jdbcDao;
	private static Logger log=Logger.getLogger(SpeechServiceImpl.class.getName());
	
		public SpeechServiceImpl() {
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
		    * @Description: 添加说说
		    * 
		    * @param speech
		    * @return
		    * @see com.lovebaby.service.SpeechService#addSpeech(com.lovebaby.dao.Speech)
		    */
		    

		@Override
		public Map<String, Object> addSpeech(Speech speech) throws Exception {
			try {
				Map< String, Object> map=new HashMap<>();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String id=GetUuid.getUuid();
				//插入说说
				String sql="INSERT into speech(id,mid,content,publishDate) VALUES(?,?,?,?)";
				Object[] params=new Object[]{
						id,
						speech.getMid(),
						speech.getContent(),
						format.format(new Date())
				};
				jdbcDao.update(sql, params);
				//插入说说图片
				sql="insert into pictures(id,picname,type,tid) values(?,?,?,?)";
				List list=new ArrayList<>();
				List pics=speech.getPics();
				for (int i = 0; i < pics.size(); i++) {
					list.add(new Object[]{
							GetUuid.getUuid(),
							pics.get(i),
							"1",
							id
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
		    * @Description: 获取朋友圈
		    * 
		    * @param fenyeData
		    * @return
		    * @see com.lovebaby.service.SpeechService#getFriendsCircle(com.lovebaby.pojo.FenyeData)
		    */
		    
		@Override
		public Map<String, Object> getFriendsCircle(FenyeData fenyeData) throws Exception {
			try {
				Properties prop=getProp("/ftp.properties");
				String headimgae=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
				String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("speechaddress");
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Map< String, Object> map=new HashMap<>();
				String sql=null;
				Object[] params=null;
				int count=0;
				List result=null;
				//修改用户最新使用微圈的时间
				sql="UPDATE weiquanstatus set date=? WHERE uid=?";
				params=new Object[]{
						format.format(new Date()),
						fenyeData.getId()
				};
				jdbcDao.update(sql, params);
				//获取用户所有有权查看的说说列表
				if (fenyeData.getType().equals("5")) {
					//家长获取其所在班级成员的所有说说和所在学校的所有说说
					//获取家长所属学校id
					sql="SELECT schoolId from classes where id=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t1.type='5' and t1.id=? GROUP BY t1.id) ";
					params=new Object[]{fenyeData.getId()};
					Map<String, Object> data=jdbcDao.queryForMap(sql, params);
					String schoolid=(String) data.get("schoolId");
					//家长获取其所在班级成员的所有说说
					sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type,t2.realName from speech t1 JOIN members t2 on t1.mid=t2.id where t1.mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t2.type in('2','3') and t2.tid=? ) or (t2.type in('4','5') and t2.tid in (SELECT tid from members_relate WHERE uid=? )))  ORDER BY t1.publishDate DESC LIMIT ?,?";
					params=new Object[]{
							headimgae,
							schoolid,
							fenyeData.getId(),
							fenyeData.getPage_num(),
							fenyeData.getPage_size()
					};
					result=jdbcDao.queryForList(sql, params);
					
					//获取数据总数
					sql="SELECT count(*) from speech where mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t2.type in('2','3') and t2.tid=? ) or (t2.type in('4','5') and t2.tid in (SELECT tid from members_relate WHERE uid=? ))) ";
					params=new Object[]{
							schoolid,
							fenyeData.getId()
					};
					count=jdbcDao.queryForInt(sql, params);
					
					
				}else if (fenyeData.getType().equals("4")) {
					//老师获取其所在班级成员的所有说说和所在学校的所有说说
					//获取老师所属学校id
					sql="SELECT schoolId from classes where id=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t1.type='4' and t1.id=? GROUP BY t1.id) ";
					params=new Object[]{fenyeData.getId()};
					Map<String, Object> data=jdbcDao.queryForMap(sql, params);
					String schoolid=(String) data.get("schoolId");
					
					//获取数据列表
					sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type,t2.realName from speech t1 JOIN members t2 on t1.mid=t2.id WHERE t1.mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t1.type in('2','3') and t2.tid=? ) or (t1.type='4' and t2.tid in(SELECT id from classes where schoolId=?)) or (t2.type='5' and t2.tid in (SELECT tid from members_relate WHERE uid=? )) ) ORDER BY t1.publishDate desc LIMIT ?,?";
					params=new Object[]{
							headimgae,
							schoolid,
							schoolid,
							fenyeData.getId(),
							fenyeData.getPage_num(),
							fenyeData.getPage_size()
					};
					result=jdbcDao.queryForList(sql, params);
					//获取数据总数
					sql="SELECT count(*) from speech WHERE mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t1.type in('2','3') and t2.tid=? ) or (t1.type='4' and t2.tid in(SELECT id from classes where schoolId=?)) or (t2.type='5' and t2.tid in (SELECT tid from members_relate WHERE uid=? )) )";
					params=new Object[]{
							schoolid,
							schoolid,
							fenyeData.getId()
					};
					count=jdbcDao.queryForInt(sql, params);
					
				}else if (fenyeData.getType().equals("2")||fenyeData.getType().equals("3")) {
					//园长和园务获取机构和校园成员说说
					sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type,t2.realName from speech t1 JOIN members t2 on t1.mid=t2.id where t1.mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t1.type='1' and t2.tid=(SELECT oid from schools where id=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? ))) or (t1.type in('2','3') and t2.tid=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=?)) or (t1.type in('4','5') and t2.tid in(SELECT id from classes where schoolId=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and t1.type='2')))) ORDER BY t1.publishDate desc LIMIT ?,?";
					params=new Object[]{
							headimgae,
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getPage_num(),
							fenyeData.getPage_size()
					};
					result=jdbcDao.queryForList(sql, params);
					//获取数据总数
					sql="SELECT count(*) from speech where mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t1.type='1' and t2.tid=(SELECT oid from schools where id=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? ))) or (t1.type in('2','3') and t2.tid=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and t1.type='2')) or (t1.type='4' and t2.tid in(SELECT id from classes where schoolId=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? ))))";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getId()
					};
					count=jdbcDao.queryForInt(sql, params);
				}else if (fenyeData.getType().equals("1")) {
					//机构获取园长,园务和机构说说
					sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type,t2.realName from speech t1 JOIN members t2 on t1.mid=t2.id where t1.mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t1.type='1' and t2.tid=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and t1.type='1')) or (t1.type in('2','3') and t2.tid in(SELECT id from schools where oid=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and t1.type='1')))) ORDER BY t1.publishDate desc LIMIT ?,?";
					params=new Object[]{
							headimgae,
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getPage_num(),
							fenyeData.getPage_size()
					};
					result=jdbcDao.queryForList(sql, params);
					//获取数据总数
					sql="SELECT COUNT(*) from speech where mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t1.type='1' and t2.tid=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and t1.type='1')) or (t1.type in('2','3') and t2.tid in(SELECT id from schools where oid=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and t1.type='1'))))";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId()
					};
					count=jdbcDao.queryForInt(sql, params);
				}
				//为每个说说添加图片列表
				for (int i = 0; i < result.size(); i++) {
					Map< String, Object> data=(Map<String, Object>) result.get(i);
					String sid=(String) data.get("id");
					sql="SELECT CONCAT(?,picname) as ftpimage from pictures where tid=? AND type='1'";
					params=new Object[]{ftpurl,sid};
					List pics=jdbcDao.queryForList(sql, params);
					data.put("pics", pics);
				}
				//为每个说说添加评论列表
				for (int i = 0; i < result.size(); i++) {
					Map< String, Object> data=(Map<String, Object>) result.get(i);
					String sid=(String) data.get("id");
					sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type from `comment` t1 JOIN members t2 on t1.mid=t2.id  where t1.tid=? ORDER BY t1.publishDate";
					params=new Object[]{headimgae,sid};
					List comment=jdbcDao.queryForList(sql, params);
					//判断当前用户是否已经赞过
					sql="SELECT COUNT(*) from praise WHERE mid=? and tid=?";
					params=new Object[]{fenyeData.getId(),sid};
					int state=jdbcDao.queryForInt(sql, params);
					data.put("state", state);
					//为么每个说说添加点赞列表
					sql="SELECT t2.telephone,t2.realName,t2.type from praise t1 JOIN members t2 on t1.mid=t2.id WHERE t1.tid=?";
					params=new Object[]{sid};
					List praise=jdbcDao.queryForList(sql, params);
					data.put("praise", praise);
					//为每个评论添加回复列表
					data.put("comment", comment);
					for (int i1 = 0; i1 < comment.size(); i1++) {
						Map< String, Object> data1=(Map<String, Object>) comment.get(i1);
						String sid1=(String) data1.get("id");
						sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type from  reply t1 JOIN members t2 on t1.fromid=t2.id where t1.tid=? ORDER BY t1.date";
						params=new Object[]{headimgae,sid1};
						List reply=jdbcDao.queryForList(sql, params);
						
						data1.put("reply", reply);
					}
					
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
		    * @Description: 添加评论
		    * 
		    * @param comment
		    * @return
		    * @see com.lovebaby.service.SpeechService#addComment(com.lovebaby.pojo.Comment)
		    */
		    
		@Override
		public Map<String, Object> addComment(Comment comment) throws Exception {
			try {
				Map< String, Object> map=new HashMap<>();
				String sql="INSERT into `comment`(id,mid,content,publishDate,tid,username) VALUES(?,?,?,?,?,?)";
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Object[] params=new Object[]{
						GetUuid.getUuid(),
						comment.getMid(),
						comment.getContent(),
						format.format(new Date()),
						comment.getTid(),
						comment.getUsername()
						
				};
				jdbcDao.update(sql, params);
				//向相关人员发送消息
				Properties prop1 =getProp("/rongyun.properties");
				String key = prop1.getProperty("key");
				String secret = prop1.getProperty("secret");
				JSONObject json=JSONObject.fromObject("{'content':'爱幼通平台通知'}");
				Message msg=new PushMessage(json, "RC:TxtMsg");
				List<String> list=getSpeechMembers(comment.getTid(),comment.getMid());
				try {
					ApiHttpClient.publishSystemMessage(key, secret, "comment", list, msg, "您收到一条新的评论，请及时查看！", null, FormatType.json);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//返回该条说说
				FenyeData fenyeData=new FenyeData();
				fenyeData.setId(comment.getTid());
				map=getSpeechContent(fenyeData);
				return map;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
			
		}


		
		    /* 
		    * @Description: 添加回复
		    * 
		    * @param reply 
		    * @return
		    * @see com.lovebaby.service.SpeechService#addReply(com.lovebaby.pojo.Reply)
		    */
		    
		@Override
		public Map<String, Object> addReply(Reply reply) throws Exception {
			try {
				Map< String, Object> map=new HashMap<>();
				String sql="INSERT into reply(id,tid,toid,fromid,date,toname,fromname,content) VALUES(?,?,?,?,?,?,?,?)";
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Object[] params=new Object[]{
						GetUuid.getUuid(),
						reply.getTid(),
						reply.getToid(),
						reply.getFromid(),
						format.format(new Date()),
						reply.getToname(),
						reply.getFromname(),
						reply.getContent()
						
				};
				jdbcDao.update(sql, params);
				//向相关人员发送消息
				Properties prop1 =getProp("/rongyun.properties");
				String key = prop1.getProperty("key");
				String secret = prop1.getProperty("secret");
				JSONObject json=JSONObject.fromObject("{'content':'爱幼通平台通知'}");
				Message msg=new PushMessage(json, "RC:TxtMsg");
				sql="SELECT tid from `comment` WHERE id=?";
				params=new Object[]{
					reply.getTid()	
				};
				Map< String, Object> data=jdbcDao.queryForMap(sql, params);
				String tid=(String) data.get("tid");
				List<String> list=getSpeechMembers(tid,reply.getFromid());
				try {
					ApiHttpClient.publishSystemMessage(key, secret, "reply", list, msg, "有一条回复与您相关！", null, FormatType.json);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//返回该条说说
				FenyeData fenyeData=new FenyeData();
				fenyeData.setId(tid);
				map=getSpeechContent(fenyeData);
				return map;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
			
		}


		
		    /* 
		    * @Description: 点赞
		    * 
		    * @param praise
		    * @return
		    * @see com.lovebaby.service.SpeechService#addPraise(com.lovebaby.pojo.Praise)
		    */
		    
		@Override
		public Map<String, Object> addPraise(Praise praise) throws Exception {
			try {
				Map< String, Object> map=new HashMap<>();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//判断是否已经点赞
				String sql="SELECT COUNT(*) from praise WHERE mid=? and tid=?";
				Object[] params=new Object[]{
						praise.getMid(),
						praise.getTid()
						};
				int state=jdbcDao.queryForInt(sql, params);
				if (state==0) {					
					sql="INSERT into praise(id,mid,tid,publishDate,username) VALUES(?,?,?,?,?)";
					params=new Object[]{
							GetUuid.getUuid(),
							praise.getMid(),
							praise.getTid(),
							format.format(new Date()),
							praise.getUsername()				
					};
					jdbcDao.update(sql, params);
				}
				//返回点赞人列表
				sql="SELECT t2.telephone,t2.realName,t2.type from praise t1 JOIN members t2 on t1.mid=t2.id WHERE t1.tid=?";
				params=new Object[]{praise.getTid()};
				List praises=jdbcDao.queryForList(sql, params);
				map.put("praise", praises);
				return map;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
			
		}


		
		    /* 
		    * @Description: 获取用户说说列表
		    * 
		    * @param fenyeData
		    * @return
		    * @see com.lovebaby.service.SpeechService#getUserSpeech(com.lovebaby.pojo.FenyeData)
		    */
		    
		@Override
		public Map<String, Object> getUserSpeech(FenyeData fenyeData) throws Exception {
			try {
				Properties prop=getProp("/ftp.properties");
				String headimgae=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
				String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("speechaddress");
				Map< String, Object> map=new HashMap<>();
				String sql=null;
				Object[] params=null;
				int count=0;
				List result=null;
				Map< String, Object> userInfo;
				//获取用户信息
				sql="SELECT * from members where id=?";
				params=new Object[]{fenyeData.getId()};
				userInfo=jdbcDao.queryForMap(sql, params);
				
				//获取说说列表
				sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type,t2.realName from speech t1 JOIN members t2 on t1.mid=t2.id where t1.mid=? ORDER BY t1.publishDate desc LIMIT ?,?";
				params=new Object[]{
						headimgae,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				result=jdbcDao.queryForList(sql, params);
				
				//获取说说总数
				sql="SELECT * from speech where mid=?";
				params=new Object[]{
						fenyeData.getId()
				};
				count=jdbcDao.queryForInt(sql, params);
				
				//为每个说说添加图片列表
				for (int i = 0; i < result.size(); i++) {
					Map< String, Object> data=(Map<String, Object>) result.get(i);
					String sid=(String) data.get("id");
					sql="SELECT CONCAT(?,picname) as ftpimage from pictures where tid=? AND type='1'";
					params=new Object[]{ftpurl,sid};
					List pics=jdbcDao.queryForList(sql, params);
					data.put("pics", pics);
				}
				
				for (int i = 0; i < result.size(); i++) {
					//为每个说说添加评论列表
					Map< String, Object> data=(Map<String, Object>) result.get(i);
					String sid=(String) data.get("id");
					sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type from `comment` t1 JOIN members t2 on t1.mid=t2.id where t1.tid=? ORDER BY t1.publishDate";
					params=new Object[]{headimgae,sid};
					List comment=jdbcDao.queryForList(sql, params);
					//判断当前用户是否已经赞过
					sql="SELECT COUNT(*) from praise WHERE mid=? and tid=?";
					params=new Object[]{fenyeData.getId(),sid};
					int state=jdbcDao.queryForInt(sql, params);
					data.put("state", state);
					//为么每个说说添加点赞列表
					sql="SELECT t2.telephone,t2.realName,t2.type from praise t1 JOIN members t2 on t1.mid=t2.id WHERE t1.tid=?";
					params=new Object[]{sid};
					List praise=jdbcDao.queryForList(sql, params);
					data.put("praise", praise);
					//为每个评论添加回复列表
					data.put("comment", comment);
					for (int i1 = 0; i1 < comment.size(); i1++) {
						Map< String, Object> data1=(Map<String, Object>) comment.get(i1);
						String sid1=(String) data1.get("id");
						sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type from  reply t1 JOIN members t2 on t1.fromid=t2.id where t1.tid=? ORDER BY t1.date";
						params=new Object[]{headimgae,sid1};
						List reply=jdbcDao.queryForList(sql, params);						
						data1.put("reply", reply);
					}
				}
				
				map.put("userInfo", userInfo);
				map.put("result", result);
				map.put("count", count);
				return map;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
			
		}

	    /* 
		    * @Description: 获取某条说说内容
		    * 
		    * @param fenyeData
		    * @return
		    * @throws Exception
		    */
		    
		@Override
		public Map<String, Object> getSpeechContent(FenyeData fenyeData) throws Exception {
			try {
				Properties prop=getProp("/ftp.properties");
				String headimgae=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
				String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("speechaddress");
				Map< String, Object> map=new HashMap<>();
				String sql=null;
				Object[] params=null;
				Map< String, Object> result=null;
				
				//获取说说
				sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type,t2.realName from speech t1 JOIN members t2 on t1.mid=t2.id where t1.id=?";
				params=new Object[]{
						headimgae,
						fenyeData.getId()
				};
				result=jdbcDao.queryForMap(sql, params);
				
				//为说说添加图片列表
				sql="SELECT CONCAT(?,picname) as ftpimage from pictures where tid=? AND type='1'";
				params=new Object[]{ftpurl,fenyeData.getId()};
				List pics=jdbcDao.queryForList(sql, params);
				result.put("pics", pics);
				//为说说添加评论列表
				sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type from `comment` t1 JOIN members t2 on t1.mid=t2.id where t1.tid=? ORDER BY t1.publishDate";
				params=new Object[]{headimgae,fenyeData.getId()};
				List comment=jdbcDao.queryForList(sql, params);
				result.put("comment", comment);
				//判断当前用户是否已经赞过
				sql="SELECT COUNT(*) from praise WHERE mid=? and tid=?";
				params=new Object[]{fenyeData.getUid(),fenyeData.getId()};
				int state=jdbcDao.queryForInt(sql, params);
				result.put("state", state);
				//为么说说添加点赞列表
				sql="SELECT t2.telephone,t2.realName,t2.type from praise t1 JOIN members t2 on t1.mid=t2.id WHERE t1.tid=?";
				params=new Object[]{fenyeData.getId()};
				List praise=jdbcDao.queryForList(sql, params);
				result.put("praise", praise);
				//为每个评论添加回复列表
				for (int i = 0; i< comment.size(); i++) {
					Map< String, Object> data=(Map<String, Object>) comment.get(i);
					String sid=(String) data.get("id");
					sql="SELECT t1.*,CONCAT(?,t2.headImage) as 'ftpimage',t2.type from  reply t1 JOIN members t2 on t1.fromid=t2.id where t1.tid=? ORDER BY t1.date";
					params=new Object[]{headimgae,sid};
					List reply=jdbcDao.queryForList(sql, params);						
					data.put("reply", reply);
				}
				map.put("result", result);
				return map;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
			
		}
		
		    /* 
		    * @Description: 删除说说
		    * 
		    * @param speech
		    * @return
		    * @see com.lovebaby.service.SpeechService#deleteSpeech(com.lovebaby.dao.Speech)
		    */
		    
		@Override
		public Map<String, Object> deleteSpeech(Speech speech) throws Exception {
			try {
				Map< String, Object> map=new HashMap<>();
				String sql=null;
				Object[] params=null;
				List list=new ArrayList<>();
				//获取评论列表
				sql="SELECT id from `comment` where tid=?";
				params=new Object[]{speech.getId()};
				List result=jdbcDao.queryForList(sql, params);				
				//删除说说图片
				sql="DELETE from pictures where tid=? and type='1'";
				jdbcDao.update(sql, params);
				//删除说说回复列表
				sql="DELETE from reply where tid=?";
				for (int i = 0; i < result.size(); i++) {
					list.add(new Object[]{
							result.get(i)
					});
				}
				jdbcDao.batchBySimpleJdbcTemplate(sql, list);	
				//删除评论列表
				sql="DELETE from `comment` where tid=?";
				params=new Object[]{speech.getId()};
				jdbcDao.update(sql, params);	
				//删除点赞列表
				sql="DELETE from praise WHERE tid=?";
				jdbcDao.update(sql, params);
				//删除说说
				sql="DELETE from speech where id=?";
				jdbcDao.update(sql, params);		
				return map;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
			
		}


		
		    /* 
		    * @Description: 判断是否有新的说说动态
		    * 
		    * @param fenyeData
		    * @return
		    * @see com.lovebaby.service.SpeechService#hasNewSpeech(com.lovebaby.pojo.FenyeData)
		    */
		    
		@Override
		public Map<String, Object> hasNewSpeech(FenyeData fenyeData) throws Exception {
			try {
				Map< String, Object> map=new HashMap<>();
				String sql=null;
				Object[] params=null;
				int count=0;
				//获取用户所有有权查看的说说列表
				if (fenyeData.getType().equals("5")) {
					
					//家长朋友圈是否有最新动态
					//获取数据总数
					sql="SELECT * from speech where mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.type in('4','5') and t2.tid in (SELECT t5.tid from members t4 JOIN members_relate t5 on t4.id=t5.uid where t4.id=? and t4.type='5')) AND publishDate>(SELECT date FROM weiquanstatus WHERE uid=?) ";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId()
					};
					count=jdbcDao.queryForInt(sql, params);
					
					
				}else if (fenyeData.getType().equals("4")) {
					//老师朋友圈是否有最新动态
					//获取老师所属学校id
					sql="SELECT schoolId from classes where id=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid WHERE t1.type='4' and t1.id=? GROUP BY t1.id) ";
					params=new Object[]{fenyeData.getId()};
					Map<String, Object> data=jdbcDao.queryForMap(sql, params);
					String schoolid=(String) data.get("schoolId");
					
				
					//获取数据总数
					sql="SELECT count(*) from speech WHERE mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t1.type in('2','3') and t2.tid=? ) or (t1.type in('4','5') and t2.tid in (SELECT t5.tid from members t4 JOIN members_relate t5 on t4.id=t5.uid where t4.id=? and t4.type='5')) ) AND publishDate>(SELECT date FROM weiquanstatus WHERE uid=?)";
					params=new Object[]{
							schoolid,
							fenyeData.getId(),
							fenyeData.getId()
					};
					count=jdbcDao.queryForInt(sql, params);
					
				}else if (fenyeData.getType().equals("3")) {
					//园务朋友圈是否有最新动态
					//获取数据总数
					sql="SELECT count(*) from speech where mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t1.type in('2','3') and t2.tid=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and type='3')) or (t1.type='4' and t2.tid in(SELECT id from classes where schoolId=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and type='3')))) AND publishDate>(SELECT date FROM weiquanstatus WHERE uid=?)";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getId()
					};
					count=jdbcDao.queryForInt(sql, params);
				}else if (fenyeData.getType().equals("2")) {
					//园长朋友圈是否有最新动态
					//获取数据总数
					sql="SELECT count(*) from speech where mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t1.type='1' and t2.tid=(SELECT oid from schools where id=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and type='2'))) or (t1.type in('2','3') and t2.tid=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and type='2')) or (t1.type='4' and t2.tid in(SELECT id from classes where schoolId=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and type='2')))) AND publishDate>(SELECT date FROM weiquanstatus WHERE uid=?)";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getId()
					};
					count=jdbcDao.queryForInt(sql, params);
				}else if (fenyeData.getType().equals("1")) {
					//机构朋友圈是否有最新动态
					//获取数据总数
					sql="SELECT * from speech where mid in(SELECT DISTINCT t1.id from members t1 JOIN members_relate t2 on t1.id=t2.uid where (t1.type='1' and t2.tid=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and type='1')) or (t1.type='2' and t2.tid in(SELECT id from schools where oid=(SELECT t2.tid from members t1 JOIN members_relate t2 on t1.id=t2.uid where t1.id=? and type='1')))) AND publishDate>(SELECT date FROM weiquanstatus WHERE uid=?)";
					params=new Object[]{
							fenyeData.getId(),
							fenyeData.getId(),
							fenyeData.getId()
					};
					count=jdbcDao.queryForInt(sql, params);
				}
				map.put("count", count);	
				return map;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
			
		}


		
		    /* 
		    * @Description: 获取有未读回复的说说列表
		    * 
		    * @param fenyeData
		    * @return
		    * @see com.lovebaby.service.SpeechService#getUnreadReply(com.lovebaby.pojo.FenyeData)
		    */
		    
		@Override
		public Map<String, Object> getUnreadReply(FenyeData fenyeData) throws Exception {
			try {
				Map< String, Object> map=new HashMap<>();
				String sql=null;
				Object[] params=null;
				List result=new ArrayList<>();
				String[] ids=fenyeData.getId().split(",");
				for (int i = 0; i < ids.length; i++) {
					//获取说说
					sql="SELECT * from speech where id=?";
					params=new Object[]{ids[i]};
					Map<String, Object> speech=jdbcDao.queryForMap(sql, params);
					//获取说说下有未读回复的评论
					sql="SELECT t1.* from `comment` t1 LEFT JOIN reply t2 on t1.id=t2.tid WHERE t1.tid=? and (t1.`status`='0' or t2.`status`='0') ORDER BY t1.publishDate";
					List comment=jdbcDao.queryForList(sql, params);
					speech.put("comment", comment);
					
					//修改未读评论状态
					sql="UPDATE `comment` set `status`='1' where tid=?";
					jdbcDao.update(sql, params);
					//为评论添加回复列表
					for (int j = 0; j < comment.size(); j++) {
						Map< String, Object> data=(Map<String, Object>) comment.get(i);
						String commentid=(String) data.get("id");
						sql="SELECT * from reply where tid=? ORDER BY date ";
						params=new Object[]{commentid};
						List reply=jdbcDao.queryForList(sql, params);
						data.put("reply", reply);
						
						//修改未读回复状态
						sql="UPDATE reply set `status`='1' where tid=?";
						jdbcDao.update(sql, params);
					}
					result.add(speech);
				}
				map.put("result", result);
				return map;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
			
		}


		
		    /* 
		    * @Description:获取未读回复数量 和信息
		    * 
		    * @param fenyeData
		    * @return
		    * @see com.lovebaby.service.SpeechService#getUnreadReplyNum(com.lovebaby.pojo.FenyeData)
		    */
		    
		@Override
		public Map<String, Object> getUnreadReplyNum(FenyeData fenyeData) throws Exception {
			try {
				Map< String, Object> map=new HashMap<>();
				String sql=null;
				Object[] params=null;
				int count=0;
				List result=null;
				//获取未读回复数量
				sql="SELECT count(*) from (SELECT t2.* from speech t1 JOIN `comment` t2 on t1.id=t2.tid where t1.mid=?) t1 LEFT JOIN reply t2 on t1.id=t2.tid where t2.`status`='0' or t1.`status`='0'";
				params=new Object[]{fenyeData.getId()};
				count=jdbcDao.queryForInt(sql, params);
				//获取有未读回复的说说id列表
				sql="SELECT t1.sid from (SELECT t2.*,t1.id as sid,t1.publishDate as time from speech t1 JOIN `comment` t2 on t1.id=t2.tid where t1.mid=? ) t1 LEFT JOIN reply t2 on t1.id=t2.tid where t2.`status`='0' or t1.`status`='0' GROUP BY t1.sid ORDER BY t1.time desc";
				result=jdbcDao.queryForList(sql, params);
				map.put("flag", "1");
				map.put("count", count);
				map.put("result", result);
				return map;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
			
		}
		
		    /* 
		    * @Description: 取消点赞
		    * 
		    * @param praise
		    * @return
		    * @throws Exception
		    * @see com.lovebaby.service.SpeechService#deletePraise(com.lovebaby.pojo.Praise)
		    */
		    
		@Override
		public Map<String, Object> deletePraise(Praise praise) throws Exception {
			try {
				Map< String, Object> map=new HashMap<>();
				String sql="DELETE from praise WHERE mid=? and tid=?";
				Object[] params=new Object[]{praise.getMid(),praise.getTid()};
				jdbcDao.update(sql, params);
				//返回点赞人列表
				sql="SELECT t2.telephone,t2.realName,t2.type from praise t1 JOIN members t2 on t1.mid=t2.id WHERE t1.tid=?";
				params=new Object[]{praise.getTid()};
				List praises=jdbcDao.queryForList(sql, params);
				map.put("praise", praises);
				return map;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
		}
		
		 /* 
		    * @Description: 获取某条说说的所有参与人
		    * 
		    * @param praise
		    * @return
		    * @throws Exception
		    * @see com.lovebaby.service.SpeechService#deletePraise(com.lovebaby.pojo.Praise)
		    */
		    
		@Override
		public List<String> getSpeechMembers(String id,String uid) throws Exception {
			try {				
				List<String> list=new ArrayList<>();
				String sql=null;
				Object[] params=null;
				Map< String, Object> result=null;
				
				//获取说说发布人id
				sql="SELECT mid from speech where id=? ";
				params=new Object[]{
						id
				};
				result=jdbcDao.queryForMap(sql, params);
				String mid=(String)result.get("mid");
				if (!mid.equals(uid)) {					
					list.add(mid);
				}
				
				//获取说说评论人id
				sql="SELECT DISTINCT mid from `comment`  where tid=? and mid!=?";
				params=new Object[]{id,uid};
				List comment=jdbcDao.queryForList(sql, params);
				//获取说说评论
				sql="SELECT id from `comment`  where tid=? and mid!=?";
				params=new Object[]{id,uid};
				List comments=jdbcDao.queryForList(sql, params);			
				//为每个评论添加回复列表
				for (int i = 0; i< comments.size(); i++) {
					Map< String, Object> data=(Map<String, Object>) comments.get(i);
					String sid=(String) data.get("id");
					sql="SELECT DISTINCT fromid as mid from  reply  where tid=? and fromid!=?";
					params=new Object[]{sid,uid};
					List reply=jdbcDao.queryForList(sql, params);						
					comment.addAll(reply);
				}
				for (int i = 0; i < comment.size(); i++) {
					Map< String, Object> data=(Map<String, Object>) comment.get(i);
					list.add((String)data.get("mid"));
				}
				HashSet h = new HashSet(list); 
				list.clear();       
				list.addAll(h);        
				return list;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new Exception();
			}
		}

}
