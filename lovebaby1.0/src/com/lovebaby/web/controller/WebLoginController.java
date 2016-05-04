package com.lovebaby.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.lovebaby.pojo.PermissionCheck;
import com.lovebaby.pojo.User;
import com.lovebaby.service.LoginService;



@Controller
@RequestMapping("/weblogin")
public class WebLoginController extends PermissionCheck{
	@Autowired
	private LoginService loginService;
	private static Logger log=Logger.getLogger(WebLoginController.class.getName());
	
	
	public WebLoginController() {
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



	@RequestMapping("")
	public String indexa(HttpServletRequest request,HttpServletResponse response) {
		return "login";
	}
	
	
	
	//退出
	@RequestMapping("/layout")
	public String layout() {

		return "home";
	}
	
	
	//用户登录
	@RequestMapping("/check")
	public String check(HttpServletRequest request,User user) {
		Map<String, Object> map=new HashMap<>();
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String status=request.getParameter("status");
			HttpSession ses=request.getSession();
			Properties prop=getProp();
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");			
			if (principal instanceof UserDetails) {
				user.setTelephone(((UserDetails)principal).getUsername());
			} else {
				user.setTelephone(principal.toString());
			}
			if (status.equals("ok")) {
				//身份验证成功
				user.setFtpurl(ftpurl);
				map=loginService.getUserInfo(user);
				ses.setAttribute("member", map);
				String backimage=prop.getProperty("url")+"/"+prop.getProperty("themeaddress");
				ses.setAttribute("themeurl", backimage);
				return "toindex";
			}else if (status.equals("fail")) {
				request.setAttribute("msg", "用户名或密码错误！");
				return "login";
			} else {
				request.setAttribute("msg", "用户没有访问权限！");
				return "login";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return "erro";
		}		
		
	}
	
	
	
	
	

		
		
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
