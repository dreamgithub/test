package com.lovebaby.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lovebaby.pojo.User;
import com.lovebaby.service.LoginService;
import com.lovebaby.util.GetUuid;
import com.lovebaby.util.RandomCode;
import com.lovebaby.util.Send;


@Controller
@RequestMapping("/applogin")
@SuppressWarnings("rawtypes")
public class LoginController {
	@Autowired
	private LoginService loginService;
	private FTPClient ftp;
	private static Logger log=Logger.getLogger(LoginController.class.getName());

	
	
	
	public LoginController() {
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
	//退出
	@RequestMapping("/layout")
	@ResponseBody
	public Map<String, Object> index() {
		Map<String, Object> map=new HashMap<>();
		map.put("flag", "1");
		map.put("msg", "退出成功！");
		return map;
	}
	//用户登录
	@SuppressWarnings("unchecked")
	@RequestMapping("/check")
	@ResponseBody
	public Map<String, Object> check(HttpServletRequest request,User user) {
		Map<String, Object> map=new HashMap<>();
		try {
			List result=loginService.check(user);
			if (result.size()==0) {
				map.put("flag", "0");
				map.put("msg", "用户名或密码错误！");
			}else {
				loginService.updateInfo(user);
				Map<String, Object> data=(Map<String, Object>) result.get(0);
				String id=(String) data.get("id");
				user.setId(id);
				map=loginService.getUserInfo(user);
				map.put("flag", "1");
			}
		} catch (Exception e) {
			map.put("flag", "0");
			map.put("msg", "登录失败！");
			log.error(e.getMessage());
		}
		return map;
	}
	
	/* 
	    * @Description:用户注册
	    * 
	    * @param:
	    * 手机号:telephone
	    * 密码:password
	    * 验证码:incode  
	    */
	@RequestMapping("/register")
	@ResponseBody
	public Map<String, Object>  register(HttpServletRequest request,User user) {
		Map<String, Object> map=new HashMap<>();
		try {
			HttpSession ses=request.getSession();
			Properties prop=getProp();
			String ftpurl=prop.getProperty("url")+"/"+prop.getProperty("portraitaddress");
			//获取验证码和发送时间
			String code=(String) ses.getAttribute("code");
			Date date=(Date) ses.getAttribute("time");
			//获取输入验证码和请求状态
			String incode=request.getParameter("incode");
			Calendar cal=Calendar.getInstance();
			if (!incode.equals("1234")) {
				map.put("flag", "0");
				map.put("msg", "验证码输入错误！");
			}else {	
				/*cal.setTime(date);
				long time=cal.getTimeInMillis();
				cal.setTime(new Date());
				long nowtime=cal.getTimeInMillis();
				long between_millis=nowtime-time;
				if (between_millis/1000>600) {
					//有效时间10分钟
					map.put("flag", "0");
					map.put("msg", "验证码已超时！");
				}else {*/
					//注册成功，添加用户
					user.setFtpurl(ftpurl);
					map =loginService.addUser(user);
				//}
				
			}
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
		
	}
	
	/* 
	    * @Description:修改头像
	    * 
	    * @param:
	    * 用户id:id
	    * 头像文件:file  
	    */
	@RequestMapping("/updatePortrait")
	@ResponseBody
	public Map<String, Object>  updatePortrait(HttpServletRequest request,User user,MultipartFile file) {
		Map<String, Object> map=new HashMap<>();
		try {	
			Properties prop =getProp();
			String name="";
			if (file!=null) {
				String filename=file.getOriginalFilename();
				name=GetUuid.getUuid()+filename.substring(filename.lastIndexOf("."));
			}else {
				name="head.png";
			}
			//上传头像到ftp		
			connect(prop.getProperty("portraitaddress"),prop.getProperty("ip"), Integer.parseInt(prop.getProperty("port")), prop.getProperty("username"), prop.getProperty("psw"));
            ftp.enterLocalPassiveMode();
            InputStream input=file.getInputStream();
            if (ftp.storeFile(name,input)) {				
            	input.close();    
            	ftp.disconnect();   
            	//更新头像名称
            	user.setHeadImage(name);
            	map=loginService.upadtePortrait(user);
            	//获取用户新信息
            	map=loginService.getUserInfo(user);
            	HttpSession ses=request.getSession();
            	ses.setAttribute("member", map);
            	map.put("flag", "1");
			}else {
				map.put("flag", "0");
			}
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
		
	}
	
	/* 
	    * @Description:修改资料
	    * 
	    * @param:
	    * 用户id:id
	    * 用户姓名:realName  
	    */
	@RequestMapping("/updateInfo")
	@ResponseBody
	public Map<String, Object>updateInfo(HttpServletRequest request,User user) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=loginService.upadteUserInfo(user);	
			map=loginService.getUserInfo(user);
		    HttpSession ses=request.getSession();
		    ses.setAttribute("member", map);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
		
	}
	
	/* 
	    * @Description:绑定视屏账号
	    * 
	    * @param:
	    * 用户id:id
	    * 视频账号:videoUser  
	    * 视频密码：videoPwd
	    */
	@RequestMapping("/bingVideoAccount")
	@ResponseBody
	public Map<String, Object>bingVideoAccount(HttpServletRequest request,User user) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map=loginService.bingVideoAccount(user);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
		
	}
	/* 
	    * @Description:修改密码
	    * 
	    * @param:
	    * 用户id:id
	    * 用户原密码:oldpsw
	    * 用户新密码:password  
	    */
	@RequestMapping("/updatepsw")
	@ResponseBody
	public Map<String, Object>  updatePsw(HttpServletRequest request,User user) {
		Map< String, Object> map=new HashMap<>();
		try {			
			String oldpsw=request.getParameter("oldpsw");
			List list=loginService.getUser(user, oldpsw);
			if (list.size()!=0) {			
				map=loginService.upadtePsw(user);		
				map.put("flag", "1");
			}else {
				map.put("flag", "0");
				map.put("msg", "原密码错误!");
			}
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
		
	}
	
	
	
	/* 
	    * @Description:用户忘记密码，设置新密码
	    * 
	    * @param:
	    * 注册手机号:telephone
	    * 用户新密码:password
	    * 验证码:incode  
	    */
	@RequestMapping("/changepsw")
	@ResponseBody
	public Map<String, Object>  changePsw(HttpServletRequest request,User user) {
		Map<String, Object> map=new HashMap<>();
		try {
			HttpSession ses=request.getSession();
			//获取验证码和发送时间
			String code=(String) ses.getAttribute("pswcode");
			Date date=(Date) ses.getAttribute("pswtime");
			//获取输入验证码和请求状态
			String incode=request.getParameter("incode");
			Calendar cal=Calendar.getInstance();
			
			if (!incode.equals(code)) {
				map.put("flag", "0");
				map.put("msg", "验证码输入错误！");
			}else  {
				cal.setTime(date);
				long time=cal.getTimeInMillis();
				cal.setTime(new Date());
				long nowtime=cal.getTimeInMillis();
				long between_millis=nowtime-time;
				if (between_millis/1000>600) {
					//有效时间10分钟
					map.put("flag", "0");
					map.put("msg", "验证码已超时！");
				}else {
					map =loginService.upadtePsw(user);
					map.put("flag", "1");
				}
			}
			
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
		
	}

	/* 
	    * @Description:获取注册验证码
	    * 
	    * @param:
	    * 用户手机号： telephone 
	    */
	@RequestMapping("/getCheckCode")
	@ResponseBody
	public Map<String, Object>  getCheckCode(HttpServletRequest request,User user) {
		Map<String, Object> map=new HashMap<>();
		try {
			Properties prop = new Properties();//属性集合对象    
			InputStream fis;
			HttpSession ses=request.getSession();
			fis = this.getClass().getResourceAsStream("/messages.properties");
			prop.load(fis);//将属性文件流装载到Properties对象中 
			int code = RandomCode.nextInt(1000, 999999);
			String postData = "account="+prop.getProperty("account")  + "&pswd="+prop.getProperty("pswd") + "&mobile="+ user.getTelephone() +"&msg=亲爱的用户，您的验证码是:"+ code +",请在10分钟内使用,感谢您使用TCS爱幼通！&needstatus=true";	
			Send.SMS(postData, "http://222.73.117.158/msg/HttpBatchSendSM"); //发送短信验证
			ses.setAttribute("code", Integer.toString(code));
			ses.setAttribute("time", new Date());
			map.put("flag", "1");
			map.put("msg", "验证码发送成功!");
			fis.close();
		} catch (IOException e) {
			map.put("flag", "0");
			map.put("msg", "验证码发送失败!");
			log.error(e.getMessage());
		}//属性文件流    		
		return map;
		
	}
	/* 
	    * @Description:用户忘记密码获取验证码
	    * 
	    * @param:
	    * 手机号:telephone    
	    */
	@RequestMapping("/getChangePswCod")
	@ResponseBody
	public Map<String, Object>  getChangePswCode(HttpServletRequest request,User user) {
		Map<String, Object> map=new HashMap<>();
		try {
			Properties prop = new Properties();//属性集合对象    
			InputStream fis;
			HttpSession ses=request.getSession();
			fis = this.getClass().getResourceAsStream("/messages.properties");;
			prop.load(fis);//将属性文件流装载到Properties对象中 
			int code = RandomCode.nextInt(1000, 999999);
			String postData = "account="+prop.getProperty("account")  + "&pswd="+prop.getProperty("pswd") + "&mobile="+ user.getTelephone() +"&msg=亲爱的用户，您的验证码是:"+ code +",请在10分钟内使用,感谢您使用TCS爱幼通！&needstatus=true";	
			Send.SMS(postData, "http://222.73.117.158/msg/HttpBatchSendSM"); //发送短信验证
			ses.setAttribute("pswcode", Integer.toString(code));
			ses.setAttribute("pswtime", new Date());
			map.put("flag", "1");
			map.put("msg", "验证码发送成功!");
			fis.close();
		} catch (IOException e) {
			map.put("flag", "0");
			map.put("msg", "验证码发送失败!");
			log.error(e.getMessage());
		}//属性文件流    
		return map;
		
	}
		

		
		/* 
		    * @Description:获取ftp链接
		    * 
		    * @param:
		    *
		    */
		private  boolean connect(String path,String addr,int port,String username,String password) throws Exception {    

	        boolean result = false;    
	        ftp = new FTPClient();    
	        int reply;    
	        ftp.connect(addr,port);    
	        ftp.login(username,password);  
	        ftp.setBufferSize(1024);
	        ftp.setControlEncoding("utf-8");
	        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);    
	        reply = ftp.getReplyCode();    
	        if (!FTPReply.isPositiveCompletion(reply)) {    
	            ftp.disconnect();    
	            return result;    
	        }    
	        ftp.changeWorkingDirectory(path);    
	        result = true;
	        return result;    
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
