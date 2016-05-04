
    /**  
    * @Title: NoticeController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月11日
    * @version V1.0  
    */
    
package com.lovebaby.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lovebaby.excepttion.AppException;
import com.lovebaby.excepttion.WebException;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Notice;
import com.lovebaby.pojo.PermissionCheck;
import com.lovebaby.pojo.User;
import com.lovebaby.service.GetPermissionService;
import com.lovebaby.service.ManergerMembersService;
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
@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/webnotice")
public class WebNoticeController extends PermissionCheck{
	@Autowired
	private GetPermissionService getPermissionService;
	@Autowired	
	private NoticeService noticeService;
	@Autowired
	private ManergerMembersService manergerMembersService;
	private static Logger log=Logger.getLogger(WebNoticeController.class.getName());	
	public WebNoticeController() {
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
	    * @Description:公告管理
	    * 
	    * @param:
	    * 
	    * 用户id：id
	    * 用户类型：type
	    */

	@RequestMapping("/notice")
	public String notice(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			fenyeData.setId(user.getId());
			fenyeData.setType(user.getType());
			map=noticeService.getUnreadNoticeNum(fenyeData);
			request.setAttribute("result", map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}
		return "notice/notice";
	}
	/* 
	    * @Description:获取公告列表
	    * 
	    * @param:
	    * 用户id:id
	    * 用户类型:type
	    * 公告类型：searchType，o机构，s校园，c班级
	    */
	@RequestMapping("/getNoticeList")
	public String getNoticeList(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			fenyeData.setId(user.getId());
			fenyeData.setType(user.getType());
			fenyeData.setPage_size(1000000000);
			map=noticeService.getNoticeList(fenyeData);
			request.setAttribute("result", map);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}
		return "notice/noticedata";
	}
	/* 
	    * @Description:添加文本公告
	    * 
	    * @param:
	    * 公告发送者所属单位id:from_id
	    * 公告发送者类型:from_type
	    * 公告标题:title
	    * 公告能容:content
	    * 公告类型:contentType,0一般文本，1html文本
	    * 公告接收者类型：to_type,0所有人，1指定对象
	    * 公告接受对象id组：toids,指定人时为id拼接，全部时为空
	    * 发送人id:uid 
	    */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/addTextNotice")
	@ResponseBody
	public Map<String, Object> addTextNotice(MultipartHttpServletRequest request,Notice notice,HttpServletRequest request2) throws AppException  {
		try {
			User user=getUser(request2);
			Map<String, Object> map=new HashMap<>();
			AddImgae addImgae=new AddImgae();
			String filename = "";
			String name = "";
			List pics = new ArrayList<>();
			List<MultipartFile> files=request.getFiles("files");
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
			notice.setUid(user.getId());
			map=noticeService.addNotice(notice);
			map.put("flag", "1");
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}

	}
	
	/* 
	    * @Description:添加html公告
	    * 
	    * @param:
	    * 公告发送者所属单位id:from_id
	    * 公告发送者类型:from_type
	    * 公告标题:title
	    * 公告能容:content
	    * 公告类型:contentType,0一般文本，1html文本
	    * 公告接收者类型：to_type,0所有人，1指定对象
	    * 公告接受对象id组：toids,指定人时为id拼接，全部时为空
	    * 发送人id:uid 
	    */
	@RequestMapping("/addHtmlNotice")
	@ResponseBody
	public Map<String, Object> addHtmlNotice(Notice notice,HttpServletRequest request) throws AppException  {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			notice.setUid(user.getId());
			map=noticeService.addNotice(notice);
			map.put("flag", "1");
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}

	}
	
	
	/* 
	    * @Description:发布公告页面
	    * 
	    * @param:
	    */
	@RequestMapping("/publishNotice")
	public String publishNotice(FenyeData fenyeData,HttpServletRequest request) {
		return "notice/publishnotice";
	}
	/* 
	    * @Description:选择发布公告类型
	    * 
	    * @param:
	    * 发布公告类型：type，0文本公告，1html公告
	    */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/chooseNoticeType")
	public String chooseNoticeType(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			HttpSession ses=request.getSession();
			Map<String, Object> member=(Map<String, Object>) ses.getAttribute("member");
			Map<String, Object> result=(Map<String,Object>)member.get("result");
			List danweis=(List) member.get("danwei");
			Map<String, Object> danwei=(Map<String,Object>)danweis.get(0);
			String tid=(String) danwei.get("tid");
			String id=(String)result.get("id");
			String type=(String)result.get("type");
			fenyeData.setId(id);
			fenyeData.setTid(tid);
			fenyeData.setType(type);
			fenyeData.setPage_size(1000000000);
			map=manergerMembersService.getDanweiList(fenyeData);
			request.setAttribute("result", map);
			if (fenyeData.getSearchType().equals("1")) {				
				return "notice/publishHtmlNotice";
			} else {
				return "notice/publishTextNotice";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}
	}

	/* 
	    * @Description:获取发布公告列表
	    * 
	    * @param:
	    * 用户id:id
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/getPublishNotice")
	public String getPublishNotice(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			fenyeData.setId(user.getId());
			fenyeData.setPage_size(1000000000);
			map=noticeService.getPublishNotice(fenyeData);
			request.setAttribute("result", map);
			return "notice/mypublish";
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}

	}
	

	/* 
	    * @Description:获取发布公告列表
	    * 
	    * @param:
	    * 用户id:id
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/toUpdateNotice")
	public String toUpdateNotice(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			fenyeData.setId(user.getId());
			map=noticeService.readNotice(fenyeData);
			request.setAttribute("result", map);
			if (fenyeData.getType().equals("0")) {
				return "notice/updateTxtNotice";
			}else {
				return "notice/updateHtmlNotice";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}

	}
	
	
	/* 
	    * @Description:删除公告
	    * 
	    * @param:
	    * 公告id:id
	    */
	@RequestMapping("/deleteNotice")
	@ResponseBody
	public Map<String, Object> deleteNotice(HttpServletRequest request,FenyeData fenyeData) throws AppException {
		try {
			User user=getUser(request);
			//权限判断
			String sql="SELECT count(*) from notice WHERE from_uid=? and id=?";
			Object[] params=new Object[]{
					user.getId(),
					fenyeData.getId()
			};
			if (getPermissionService.getPermission(sql, params)) {
				Map<String, Object> map=new HashMap<>();
				map=noticeService.deleteNotice(fenyeData);
				map.put("flag", "1");
				return map;			
			}else {
				String ip = getIRealIPAddr(request);
				log.error("非法访问，没有操作权限！   ip:"+ip+",tel="+user.getTelephone());
				throw new AppException();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}

		
	}
	
	/* 
	    * @Description:修改文本公告
	    * 
	    * @param:
	    * 公告id:id
	    * 公告标题:title
	    * 公告能容:content 
	    * 公告类型:contentType,0一般文本，1html文本
	    */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateTxtNotice")
	@ResponseBody
	public Map<String, Object> updateTxtNotice(MultipartHttpServletRequest request,Notice notice) throws AppException {
		try {
			User user=getUser(request);
			//权限判断
			String sql="SELECT count(*) from notice WHERE from_uid=? and id=?";
			Object[] params=new Object[]{
					user.getId(),
					notice.getId()
			};
			if (getPermissionService.getPermission(sql, params)) {		
				Map<String, Object> map=new HashMap<>();
				AddImgae addImgae=new AddImgae();
					String filename = "";
					String name = "";
					List pics = new ArrayList<>();
					List<MultipartFile> files=request.getFiles("files");
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
					map=noticeService.updateNotice(notice);
					map.put("flag", "1");
					return map;
			}else {
				String ip = getIRealIPAddr(request);
				log.error("非法访问，没有操作权限！   ip:"+ip+",tel="+user.getTelephone());
				throw new AppException();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}
	}
	
	/* 
	    * @Description:修改html公告
	    * 
	    * @param:
	    * 公告id:id
	    * 公告发送者所属单位id:from_id
	    * 公告发送者类型:from_type
	    * 公告标题:title
	    * 公告能容:content
	    * 公告类型:contentType,0一般文本，1html文本
	    * 公告接收者类型：to_type,0所有人，1指定对象
	    * 公告接受对象id组：toids,指定人时为id拼接，全部时为空
	    * 发送人id:uid 
	    */
	@RequestMapping("/updateHtmlNotice")
	@ResponseBody
	public Map<String, Object> updateHtmlNotice(HttpServletRequest request,Notice notice) throws AppException {
		try {
			User user=getUser(request);
			//权限判断
			String sql="SELECT count(*) from notice WHERE from_uid=? and id=?";
			Object[] params=new Object[]{
					user.getId(),
					notice.getId()
			};
			if (getPermissionService.getPermission(sql, params)) {		
				Map<String, Object> map;
				map=noticeService.updateNotice(notice);
				map.put("flag", "1");
				return map;
			}else {
				String ip = getIRealIPAddr(request);
				log.error("非法访问，没有操作权限！   ip:"+ip+",tel="+user.getTelephone());
				throw new AppException();
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}

		
	}
	
	/* 
	    * @Description:读取公告内容，添加读取状态
	    * 
	    * @param:
	    * 公告id：nid
	    * 用户id:id 
	    */
	@RequestMapping("/read")
	public String readNotice(HttpServletRequest request,FenyeData fenyeData) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			//权限判断
			String sql="SELECT count(*) from notice WHERE from_uid=? and id=?";
			Object[] params=new Object[]{
					user.getId(),
					fenyeData.getNid()
			};
			if (getPermissionService.getPermission(sql, params)) {	
				fenyeData.setId(user.getId());
				map=noticeService.readNotice(fenyeData);
				request.setAttribute("result", map);
				if (fenyeData.getType().equals("0")) {
					return "notice/noticeText";
				}else {
					return "notice/noticeHtml";
				}
			}else {
				String ip = getIRealIPAddr(request);
				log.error("非法访问，没有操作权限！   ip:"+ip+",tel="+user.getTelephone());
				throw new WebException();
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}
	}


	
	
	
}
