
    /**  
    * @Title: ApplyRoleController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月13日
    * @version V1.0  
    */
    
package com.lovebaby.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lovebaby.pojo.Authentication;
import com.lovebaby.pojo.Babies;
import com.lovebaby.pojo.Classes;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.User;
import com.lovebaby.service.ApplyRoleService;
import com.lovebaby.service.LoginService;
import com.lovebaby.util.AddImgae;
import com.lovebaby.util.GetUuid;

/**
    * @ClassName: ApplyRoleController
    * @Description: 角色申请
    * @author likai
    * @date 2015年11月13日
    *
    */
@Controller
@RequestMapping("/appApplyRole")
public class ApplyRoleController {
	@Autowired
	private ApplyRoleService applyRoleService;
	@Autowired
	private LoginService loginService;
	private static Logger log=Logger.getLogger(ApplyRoleController.class.getName());
	
	
	public ApplyRoleController() {
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
	    * @Description:获取相应单位的列表，组织机构，学校，班级
	    * 
	    * @param:
	    * 公告类型：searchType，o机构，s校园，c班级
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/getTypeList")
	@ResponseBody
	public Map<String, Object> getTypeList(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=applyRoleService.getTypeList(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		
		return map;
	}
	
	/*  
	    * @Description:添加认证信息
	    * 
	    * @param:
	    * 申请者提交审批单位的id：tid,机构，学校，班级
	    * 用户id：fromid
	    * 申请对象类型：type
	    * 申请者名称：username
	    * 申请者号码：phone
	    * 宝宝名称：babyname
	    * 区域：area
	    * 包包关系：babyrelate
	    * 申请对象名称：name
	    * 头像文件:file
	    */
	@RequestMapping("/addAuthentication")
	@ResponseBody
	public Map<String, Object> addAuthentication(Authentication authentication,MultipartFile file) {
		Map<String, Object> map=new HashMap<>();
		AddImgae addImgae=new AddImgae();
		try {
			User user=new User();
			//上传头像
			if (file!=null) {			
				String filename=file.getOriginalFilename();
				String name=authentication.getFromid()+filename.substring(filename.lastIndexOf("."));
				if (addImgae.addImgae(file, name, "portraitaddress")) {
					//更新头像名称			
					authentication.setProtrait(name);
					user.setHeadImage(name);
					user.setId(authentication.getFromid());
					loginService.upadtePortrait(user);
				}
			}
			if (authentication.getUsername()!=null||!authentication.getUsername().equals("")) {				
				//修改用户名
				user.setRealName(authentication.getUsername());
				loginService.upadteUserInfo(user);
			}
			//添加认证信息
			applyRoleService.addAuthentication(authentication);
			map=loginService.getUserInfo(user);	
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
	}

	
	/*  
	    * @Description:条件查询
	    * 
	    * @param:
	    * 查询类型searchType:o组织，s学校
	    * 查询输入值：val
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/search")
	@ResponseBody
	public Map<String, Object> search(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=applyRoleService.search(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/*  
	    * @Description:添加班级
	    * 
	    * @param:
	    * 班级名称：className
	    * 所属学校id：schoolId
	    */
	@RequestMapping("/addClasses")
	@ResponseBody
	public Map<String, Object> addClasses(Classes classes) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=applyRoleService.addClasses(classes);
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}			
		return map;
	}
	
	
	
	/*  
	    * @Description:添加宝宝
	    * 
	    * @param:
	    * 宝宝名称：babyName
	    * 宝宝性别：sex
	    * 所属班级id：classid
	    * 宝宝编号：num
	    * 宝宝头像：file
	    */
	@RequestMapping("/addBabies")
	@ResponseBody
	public Map<String, Object> addBabies(Babies babies,MultipartFile file) {
		Map< String, Object> map=new HashMap<>();
		AddImgae addImgae=new AddImgae();
		try {
			//上传头像
			if (file!=null) {			
				String filename=file.getOriginalFilename();
				String name=GetUuid.getUuid()+filename.substring(filename.lastIndexOf("."));
				addImgae.addImgae(file, name, "babyaddress");
				babies.setPic(name);
			}
			map=applyRoleService.addBabies(babies);
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
