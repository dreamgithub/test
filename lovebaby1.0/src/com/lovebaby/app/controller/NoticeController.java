
    /**  
    * @Title: NoticeController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月11日
    * @version V1.0  
    */
    
package com.lovebaby.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Notice;
import com.lovebaby.service.NoticeService;
import com.lovebaby.util.AddImgae;
import com.lovebaby.util.GetUuid;

/**
    * @ClassName: NoticeController
    * @Description: 公告操作
    * 
    * @author likai
    * @date 2015年11月11日
    *
    */
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/appnotice")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;
	Properties prop=getProp();
	String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("noticeaddress");
	private static Logger log=Logger.getLogger(NoticeController.class.getName());
	
	public NoticeController() {
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
	    * @Description:添加公告
	    * 
	    * @param:
	    * 公告发送者所属单位id:from_id，o,s,c
	    * 公告发送者类型:from_type
	    * 公告标题:title
	    * 公告能容:content
	    * 公告类型:contentType,0一般文本，1html文本
	    * 公告接收者类型：to_type,0所有人，1指定对象
	    * 公告接受对象id组：toids,指定人时为id拼接，全部时为空
	    * 发送人id:uid 
	    */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addnotice")
	@ResponseBody
	public Map<String, Object> addNotice(MultipartHttpServletRequest request,Notice notice) {
		Map<String, Object> map=new HashMap<>();
		AddImgae addImgae=new AddImgae();
		try {
			String filename = "";
			String name = "";
			List pics = new ArrayList<>();
			List<MultipartFile> files=request.getFiles("file");
			if (files.size()!=0) {
				//上传图片
				for (int i = 0; i < files.size(); i++) {		
					filename = files.get(i).getOriginalFilename();
					name = GetUuid.getUuid() + filename.substring(filename.lastIndexOf("."));
					if (addImgae.addImgae(files.get(i), name, "noticeaddress")) {
						pics.add(name);
					}
				}
				notice.setPics(pics);
			}
			map=noticeService.addNotice(notice);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
	}
	
	/* 
	    * @Description:删除公告
	    * 
	    * @param:
	    * 公告id:id
	    */
	@RequestMapping("/deleteNotice")
	@ResponseBody
	public Map<String, Object> deleteNotice(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.deleteNotice(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	
	/* 
	    * @Description:公告置顶
	    * 
	    * @param:
	    * 公告id:id
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/noticeToTop")
	@ResponseBody
	public Map<String, Object> noticeToTop(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.noticeToTop(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/* 
	    * @Description:获取发布公告列表
	    * 
	    * @param:
	    * 用户id:id
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/publishNotice")
	@ResponseBody
	public Map<String, Object> getPublishNotice(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.getPublishNotice(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/* 
	    * @Description:获取公告列表
	    * 
	    * @param:
	    * 用户id:id
	    * 用户类型:type
	    * 公告类型：searchType，o机构，s校园，c班级
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/getNoticeList")
	@ResponseBody
	public Map<String, Object> getNoticeList(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.getNoticeList(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/* 
	    * @Description:获取置顶排序的公告列表
	    * 
	    * @param:
	    * 用户id:id
	    * 用户类型:type
	    * 公告类型：searchType，o机构，s校园，c班级
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/getNoticeToTopList")
	@ResponseBody
	public Map<String, Object> getNoticeToTopList(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.getNoticeToTopList(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/* 
	    * @Description:修改公告
	    * 
	    * @param:
	    * 公告id:id
	    * 公告标题:title
	    * 公告能容:content 
	    * 公告类型:contentType,0一般文本，1html文本
	    */
	@RequestMapping("/updateNotice")
	@ResponseBody
	public Map<String, Object> updateNotice(HttpServletRequest request,Notice notice) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.addNotice(notice);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	//---------------------------------------------------------------------------------------
	
	/* 
	    * @Description:获取已读公告列表
	    * 
	    * @param:
	    * 用户id:id
	    * 公告类型：searchType，o机构，s校园，c班级
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	
	@RequestMapping("/readNotice")
	@ResponseBody
	public Map<String, Object> getReadNotice(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.getReadNotice(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}			
		return map;
	}
	
	/* 
	    * @Description:获取未读公告列表
	    * 
	    * @param:
	    * 用户id：id
	    * 公告发布人所属单位id:oid机构，sid校园，cid班级
	    * 公告类型：searchType，o机构，s校园，c班级
	    * 发送人类别：type，1,2,3,4,5
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/unreadNotice")
	@ResponseBody
	public Map<String, Object> getUnreadNotice(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.getUnreadNotice(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}			
		return map;
	}
	
	/* 
	    * @Description:读取公告内容，添加读取记录
	    * 
	    * @param:
	    * 公告id:nid
	    * 用户id：id
	    */
	@RequestMapping("/read")
	@ResponseBody
	public Map<String, Object> readNotice(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			fenyeData.setFtpurl(ftpurl);
			map=noticeService.readNotice(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}			
		return map;
	}

	

	
	/* 
	    * @Description:园务或园长获取校园所属机构信息
	    * 
	    * @param:
	    * 校园id：sid
	    */
	
	@RequestMapping("/getSchoolType")
	@ResponseBody
	public Map<String, Object> getSchoolType(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.getSchoolType(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}					
		return map;
	}
	

	/* 
	    * @Description:机构获取旗下校园列表信息
	    * 
	    * @param:
	    * 机构id：oid
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	
	@RequestMapping("/getSchools")
	@ResponseBody
	public Map<String, Object> getSchools(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.getSchools(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}					
		return map;
	}
	
	/* 
	    * @Description:学校获取旗下班级列表
	    * 
	    * @param:
	    * 学校id：sid
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	
	@RequestMapping("/getClasses")
	@ResponseBody
	public Map<String, Object> getClasses(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.getClasses(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/* 
	    * @Description:老师获取所属学校信息
	    * 
	    * @param:
	    * 任意一个所属班级id：cid
	    */
	
	@RequestMapping("/getSchool")
	@ResponseBody
	public Map<String, Object> getSchool(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.getSchool(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}			
		return map;
	}
	
	/* 
	    * @Description:获取公告浏览人数信息
	    * 
	    * @param:
	    * 公告id：id
	    */
	
	@RequestMapping("/getNoticeReadInfo")
	@ResponseBody
	public Map<String, Object> getNoticeReadInfo(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=noticeService.getNoticeReadInfo(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}			
		return map;
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
