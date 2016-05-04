
    /**  
    * @Title: ManergerMemberConstroller.java
    * @Package com.lovebaby.app.controller
    * @Description: 成员管理
    * @author likai
    * @date 2015年12月31日
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

import com.lovebaby.pojo.Babies;
import com.lovebaby.pojo.Classes;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.service.ManergerMembersService;

/**
    * @ClassName: ManergerMemberConstroller
    * @Description: 各单位旗下用户管理
    * @author likai
    * @date 2015年12月31日
    *
    */
@Controller
@RequestMapping("/manergerMembers")
public class ManergerMembersConstroller {
	@Autowired
	private ManergerMembersService manergerMembersService;
	private static Logger log=Logger.getLogger(ManergerMembersConstroller.class.getName());
	public ManergerMembersConstroller() {
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
	
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//机构管理成员
	
	
	/* 
	    * @Description:机构删除旗下园所
	    * 
	    * @param:
	    * 学校id:id
	    */
	
	@RequestMapping("/deleteSchools")
	@ResponseBody
	public Map<String, Object> deleteSchools(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.deleteSchools(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//园务园长管理成员
	
	
	
	
	/* 
	    * @Description:园务园长删除校园班级
	    * 
	    * @param:
	    * 班级id:id
	    */
	
	@RequestMapping("/deleteClass")
	@ResponseBody
	public Map<String, Object> deleteClass(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.deleteClass(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	

	/*  
	    * @Description:修改班级信息
	    * 
	    * @param:
	    * 班级名称：className
	    * 学校id：schoolId
	    * 班级id：id
	    */
	
	@RequestMapping("/updateClassInfo")
	@ResponseBody
	public Map<String, Object> updateClassInfo(Classes classes) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.updateClassInfo(classes);
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/* 
	    * @Description:园务园长获取成员列表
	    * 
	    * @param:
	    * 学校id:id
	    * 用户类型：type
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	
	@RequestMapping("/getschoolMembers")
	@ResponseBody
	public Map<String, Object> getschoolMembers(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.getschoolMembers(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/* 
	    * @Description:删除成员
	    * 
	    * @param:
	    * 成员id:id
	    * 删除单位id：tid
	    */
	
	@RequestMapping("/deleteMember")
	@ResponseBody
	public Map<String, Object> deleteMember(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.deleteMember(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	
	/* 
	    * @Description:将宝宝移到其他班级
	    * 
	    * @param:
	    * 宝宝id:id(为所有宝宝id拼接)
	    * 班级id：tid
	    */
	
	@RequestMapping("/moveBabies")
	@ResponseBody
	public Map<String, Object> moveBabies(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.moveBabies(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		//老师管理成员
	
	/* 
	    * @Description:老师获取家长列表
	    * 
	    * @param:
	    * 班级id:id
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	
	@RequestMapping("/getClassMembers")
	@ResponseBody
	public Map<String, Object> getClassMembers(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.getClassMembers(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	
	//删除成员同上接口
	
	/* 
	    * @Description:删除宝宝
	    * 
	    * @param:
	    * 宝宝id:id
	    */
	
	@RequestMapping("/deleteBabies")
	@ResponseBody
	public Map<String, Object> deleteBabies(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.deleteBabies(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/* 
	    * @Description:家长和老师获取所属班级列表
	    * 
	    * @param:
	    * 用户id:id
	    */
	
	@RequestMapping("/getClasses")
	@ResponseBody
	public Map<String, Object> getClasses(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.getClasses(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}				
		return map;
	}
	
	/*  
	    * @Description:获取宝宝列表
	    * 
	    * @param:
	    * 班级id：cid
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/getBabies")
	@ResponseBody
	public Map<String, Object> getBabies(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.getBabies(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
	}
	
	/*  
	    * @Description:获取宝宝信息及家长列表
	    * 
	    * @param:
	    * 宝宝id：id
	    */
	@RequestMapping("/getBabyParents")
	@ResponseBody
	public Map<String, Object> getBabyParents(Babies babies) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.getBabyParents(babies);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
	}
	
	/*  
	    * @Description:意见反馈
	    * 
	    * @param:
	    * 用户id：id
	    * 内容：content
	    */
	@RequestMapping("/addAdvice")
	@ResponseBody
	public Map<String, Object> addAdvice(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=manergerMembersService.addAdvice(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
	}
}
