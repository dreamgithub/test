
    /**  
    * @Title: DeskTopController.java
    * @Package com.lovebaby.web.controller
    * @Description: 
    * @author likai
    * @date 2015年12月8日
    * @version V1.0  
    */
    
package com.lovebaby.web.controller;

import java.io.IOException;
import java.io.InputStream;
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
import com.lovebaby.pojo.PermissionCheck;
import com.lovebaby.pojo.Picture;
import com.lovebaby.pojo.User;
import com.lovebaby.service.DeskTopService;
import com.lovebaby.service.GetPermissionService;
import com.lovebaby.service.LoginService;
import com.lovebaby.util.AddImgae;
import com.lovebaby.util.GetUuid;

/**
    * @ClassName: DeskTopController
    * @Description: 桌面各个页面打开视图跳转
    * @author likai
    * @date 2015年12月8日
    *
    */
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/desktop")
public class DeskTopController extends PermissionCheck {
	@Autowired
	private GetPermissionService getPermissionService;
	@Autowired
	private DeskTopService deskTopService;
	@Autowired
	private LoginService loginService;
	private static Logger log=Logger.getLogger(DeskTopController.class.getName());
	public DeskTopController() {
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
	@RequestMapping("/todesktop")
	public String todesktop() {
		return "toindex";
	}
	/*  
	    * @Description:跳转到桌面
	    * 
	    * @param:
	    * 用户id：id
	    * 用户所属单位id：tid
	    * 用户类型：type
	    */
	@SuppressWarnings("unchecked")
	@RequestMapping("/index")
	public String desktop(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			List danweis=user.getDanwei();
			if(danweis.size()!=0){	
				Map<String, Object> danwei=(Map<String,Object>)danweis.get(0);
				String tid=(String)danwei.get("tid");
				fenyeData.setTid(tid);
				}
			Properties prop =getProp();
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("desktop");
			fenyeData.setId(user.getId());
			fenyeData.setType(user.getType());
			fenyeData.setFtpurl(ftpurl);
			map=deskTopService.desktop(fenyeData);
			request.setAttribute("result", map);
			return "index";
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}
	}
	/*  
	    * @Description:跳转到主题选择页面
	    * 
	    * @param:
	    * id：用户id
	    * Page_size：每页数据数
	    * type：主题类型：0：默认。1：推荐。2：上传。3：上传图片
	    */
	@RequestMapping("/theme")
	public String theme(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		try {
			User user=getUser(request);
			fenyeData.setId(user.getId());
			Properties prop =getProp();
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("themeaddress");
			fenyeData.setFtpurl(ftpurl);
			Map<String, Object> result=deskTopService.theme(fenyeData);
			request.setAttribute("result", result);
			if (fenyeData.getType().equals("0")) {
				return "theme/defaulttheme";
			}else if (fenyeData.getType().equals("1")) {
				return "theme/introducetheme";
			}else if (fenyeData.getType().equals("2")) {
				return "theme/uploadtheme";
			}else {
				return "theme/uploadimag";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}
	}

	
	/*  
	    * @Description:选择主题
	    * 
	    * @param:
	    * id：用户id
	    * 背景图片名称：name
	    */
	@RequestMapping("/chooseTheme")
	@ResponseBody
	public Map<String, Object> chooseTheme(FenyeData fenyeData,HttpServletRequest request) throws AppException {
		Map< String, Object> map=new HashMap<>();
		try {		
			User user=getUser(request);
			fenyeData.setId(user.getId());
			HttpSession ses=request.getSession();
			Properties prop=getProp();
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
			map=deskTopService.chooseTheme(fenyeData);
			//更新用户信息
			user.setFtpurl(ftpurl);
			Map<String, Object> data=loginService.getUserInfo(user);
			ses.setAttribute("member", data);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}
		
		return map;
	}

	/*  
	    * @Description:删除主题
	    * 
	    * @param:
	    * 背景图片id：id
	    */
	@RequestMapping("/deleteTheme")
	@ResponseBody
	public Map<String, Object> deleteTheme(FenyeData fenyeData,HttpServletRequest request) throws AppException {
		Map< String, Object> map=new HashMap<>();
		try {	
			//操作权限认证
			User user=getUser(request);
			String sql="SELECT COUNT(*) from background WHERE id=? and mid=?";
			Object[] params=new Object[]{
					fenyeData.getId(),
					user.getId()
			};
			if (getPermissionService.getPermission(sql, params)) {				
				map=deskTopService.deleteTheme(fenyeData);
				map.put("flag", "1");
			}else {
				String ip = getIRealIPAddr(request);
				log.error("非法访问，没有操作权限！   ip:"+ip+",tel="+user.getTelephone());
				throw new AppException();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}
		return map;
	}
	
	/*  
	    * @Description:上传主题
	    * 
	    * @param:
	    * 用户id：tid
	    */
	@RequestMapping("/uploadTheme")
	@ResponseBody
	public Map<String, Object> uploadTheme(MultipartHttpServletRequest request,Picture pic) throws AppException {
		Map< String, Object> map=new HashMap<>();
		try {			
			User user=getUser(request);
			pic.setTid(user.getId());
			AddImgae addImgae=new AddImgae();
			MultipartFile file=request.getFile("file");
			if (file.getSize()!=0) {			
				String filename=file.getOriginalFilename();
				String name = GetUuid.getUuid() + filename.substring(filename.lastIndexOf("."));
				if (addImgae.addImgae(file, name, "themeaddress")) {
					//向数据库添加背景图片名称			
					pic.setName(name);
					map=deskTopService.uploadTheme(pic);
				}
			}
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}
		return map;
	}
	
	
	/*  
	    * @Description:桌面工具管理
	    * 
	    * @param:
	    * 用户id：id
	    */
	@RequestMapping("/deskTopTools")
	public String deskTopTools(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			fenyeData.setId(user.getId());
			Properties prop =getProp();
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("desktop");
			fenyeData.setFtpurl(ftpurl);
			map=deskTopService.deskTopTools(fenyeData);
			request.setAttribute("result", map);
			return "desktop/tools";
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}


	}
	
	
	/*  
	    * @Description:从桌面删除图标
	    * 
	    * @param:
	    * 用户id：id
	    * 工具id：tid
	    */
	@RequestMapping("/delFromDeskTop")
	@ResponseBody
	public Map<String, Object> delFromDeskTop(FenyeData fenyeData,HttpServletRequest request) throws AppException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			fenyeData.setId(user.getId());
			map=deskTopService.delFromDeskTop(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}
		return map;
	}
	
	
	/*  
	    * @Description:向桌面添加图标
	    * 
	    * @param:
	    * 用户id：id
	    * 工具id：tid
	    */
	@RequestMapping("/addToDeskTop")
	@ResponseBody
	public Map<String, Object> addToDeskTop(FenyeData fenyeData,HttpServletRequest request) throws AppException {
		Map<String, Object> map=new HashMap<>();
		try {
			User user=getUser(request);
			fenyeData.setId(user.getId());
			map=deskTopService.addToDeskTop(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new AppException();
		}
		return map;
	}
	
	/*  
	    * @Description:跳转到系统设置页面
	    * 
	    * @param:
	    */
	@RequestMapping("/setting")
	public String setting() {
		return "desktop/setting";
	}
	/*  
	    * @Description:跳转到修改资料页面
	    * 
	    * @param:
	    * id：用户id
	    */
	@RequestMapping("/profile")
	public String profile() {
		return "desktop/profile";
	}
	
	
	
	/* 
	    * @Description:获取配置文件
	    * 
	    * @param:
	    *
	    */
	public Properties getProp() {
		Properties prop = new Properties();
		InputStream fis=this.getClass().getResourceAsStream("/ftp.properties");
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
