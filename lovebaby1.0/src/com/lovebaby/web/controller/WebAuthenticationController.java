
    /**  
    * @Title: AuthenticationController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月11日
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lovebaby.excepttion.AppException;
import com.lovebaby.excepttion.WebException;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.PermissionCheck;
import com.lovebaby.pojo.User;
import com.lovebaby.service.AuthenticationService;
import com.lovebaby.service.GetPermissionService;

/**
    * @ClassName: AuthenticationController
    * @Description: 认证审批
    * @author likai
    * @date 2015年11月11日
    *
    */
@Controller
@RequestMapping("/webauthentication")
@SuppressWarnings("rawtypes")
public class WebAuthenticationController extends PermissionCheck {
	@Autowired
	private GetPermissionService getPermissionService;
	@Autowired
	private AuthenticationService authenticationService;
	private static Logger log=Logger.getLogger(WebAuthenticationController.class.getName());
	
	

	public WebAuthenticationController() {
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
	    * @Description:用户获取旗下认证列表
	    * 
	    * @param:
	    * 用户id：id
	    * 用户类型：type
	    * 用户所在单位id：tid
	    * 认证状态：status 0未审核，1已审核
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getAuthenticationList")
	public String getAuthenticationList(FenyeData fenyeData,HttpServletRequest request) throws WebException {
		try {
			//添加参数
			User user=getUser(request);
			List danweis=(List)user.getDanwei();
			String tid="";
			if(danweis.size()!=0){	
			Map<String, Object> danwei=(Map<String,Object>)danweis.get(0);
			tid=(String)danwei.get("tid");
			}
			fenyeData.setId(user.getId());
			fenyeData.setTid(tid);
			fenyeData.setType(user.getType());
			fenyeData.setPage_num(0);
			fenyeData.setPage_size(1000000000);
			//获取数据
			Map<String, Object> map=authenticationService.getAuthenticationList(fenyeData);
			request.setAttribute("result", map);			
			if (fenyeData.getStatus().equals("0")) {			
				return "authentication/authentication";
			}else {
				return "authentication/authenticationread";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}
	}
	
	/*  
	    * @Description:认证审核
	    * 
	    * @param:
	    * 审核记录id：aid
	    * 审核状态：status
	    */
	@RequestMapping("/authenticationCheck")
	@ResponseBody
	public Map<String, Object> AuthenticationCheck(FenyeData fenyeData,HttpServletRequest request) throws Exception {
		Map< String, Object> map=new HashMap<>();
		try {		
			//权限认证
			User user=getUser(request);
			List danweis=(List)user.getDanwei();
			if (getPermissionService.getPermission(danweis, user,fenyeData.getAid())) {			
				map=authenticationService.AuthenticationCheck(fenyeData);
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
	    * @Description:重名宝宝选择宝宝
	    * 
	    * @param:
	    * 审核记录id：aid
	    * 宝宝id：bid
	    */
	@RequestMapping("/chooseBaby")
	@ResponseBody
	public Map<String, Object> chooseBaby(FenyeData fenyeData) throws WebException {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=authenticationService.AuthenticationCheck(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new WebException();
		}
		return map;
	}
}
