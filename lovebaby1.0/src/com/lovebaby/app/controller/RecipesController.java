
    /**  
    * @Title: RecipesController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月18日
    * @version V1.0  
    */
    
package com.lovebaby.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.RecipeMenu;
import com.lovebaby.pojo.Recipes;
import com.lovebaby.service.RecipesService;
import com.lovebaby.util.GetUuid;


/**
    * @ClassName: RecipesController
    * @Description: 食谱管理
    * @author likai
    * @date 2015年11月18日
    *
    */
@Controller
@RequestMapping("/recipes")
@SuppressWarnings({ })
public class RecipesController {

	private FTPClient ftp;
	@Autowired
	private RecipesService recipesService;
	private static Logger log=Logger.getLogger(RecipesController.class.getName());
	
	public RecipesController() {
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
	    * @Description:添加食谱菜单
	    * 
	    * @param:
	    * 
	    * 食谱类型：type,0系统推荐，1学校上传
	    * 食物名称：name
	    * 食谱简介：introduce
	    * 发布学校id：schoolId
	    * 图片资源：file
	    * 图片名称：pic
	    */
	@RequestMapping("/addRecipesMenu")
	@ResponseBody
	public Map<String, Object> addRecipesMenu(MultipartFile file,RecipeMenu recipeMenu) {
		Map<String, Object> map=new HashMap<>(); 
		try {	
			String filename="";
			String name="";
			Properties prop=getProp();
			if (file!=null) {
				filename=file.getOriginalFilename();
				name=GetUuid.getUuid()+filename.substring(filename.lastIndexOf("."));
				connect(prop.getProperty("recipesaddress"),prop.getProperty("ip"), Integer.parseInt(prop.getProperty("port")), prop.getProperty("username"), prop.getProperty("psw"));  
				ftp.enterLocalPassiveMode();
				InputStream input=file.getInputStream();
				ftp.storeFile(name,input);    
				input.close(); 
				ftp.disconnect();     
			}else {
				//没有上传图片，则使用默认图片
				if (recipeMenu.getPic()!=null) {					
					name="recipes.png";
				}
			}
			//添加食谱信息
			recipeMenu.setPic(name);
			map=recipesService.addRecipesMenu(recipeMenu);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("flag", "0");
		}
		return map;
	}
	
	
	/* 
	    * @Description:删除食谱菜单
	    * 
	    * @param:
	    * 
	    * 食谱菜单id：id
	    */
	@RequestMapping("/deleteRecipesMenu")
	@ResponseBody
	public Map<String, Object> deleteRecipesMenu(FenyeData fenyeData) {
		Map<String, Object> map=new HashMap<>(); 
		try {	
			map=recipesService.deleteRecipesMenu(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("flag", "0");
		}
		return map;
	}
	
	
	/* 
	    * @Description:修改食谱菜单
	    * 
	    * @param:
	    * 
	    * 菜单id：id
	    * 食物名称：name
	    * 食谱简介：introduce
	    * 图片资源：file
	    */
	@RequestMapping("/updateRecipesMenu")
	@ResponseBody
	public Map<String, Object> updateRecipesMenu(MultipartFile file,RecipeMenu recipeMenu) {
		Map<String, Object> map=new HashMap<>(); 
		try {	
			String filename="";
			String name="";
			Properties prop=getProp();
			if (file!=null) {
				filename=file.getOriginalFilename();
				name=GetUuid.getUuid()+filename.substring(filename.lastIndexOf("."));
				connect(prop.getProperty("recipesaddress"),prop.getProperty("ip"), Integer.parseInt(prop.getProperty("port")), prop.getProperty("username"), prop.getProperty("psw"));  
				ftp.enterLocalPassiveMode();
				InputStream input=file.getInputStream();
				ftp.storeFile(name,input);    
				input.close(); 
				ftp.disconnect();      
			}else {
				//没有上传图片，则使用默认图片
				name="recipes.png";
			}
			//添加食谱信息
			recipeMenu.setPic(name);
			map=recipesService.updateRecipesMenu(recipeMenu);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("flag", "0");
		}
		return map;
	}
	
	/* 
	    * @Description:获取食谱菜单
	    * 
	    * @param:
	    * 
	    * 学校id：id
	    * 食谱类型：type，0全部，1学校，2系统
	    * 分页所在页：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/getRecipesMenu")
	@ResponseBody
	public Map<String, Object> getRecipesMenu(FenyeData fenyeData) {
		Map<String, Object> map=new HashMap<>(); 
		try {			
			map=recipesService.getRecipesMenu(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("flag", "0");
		}
		return map;
	}
	
	/* 
	    * @Description:搜索食谱菜单
	    * 
	    * @param:
	    * 
	    * 学校id：id
	    * 菜名：name
	    * 分页位置：page_num
	    * 每页数据数：page_size
	    */
	@RequestMapping("/searchRecipesMenu")
	@ResponseBody
	public Map<String, Object> searchRecipesMenu(FenyeData fenyeData) {
		Map<String, Object> map=new HashMap<>(); 
		try {			
			map=recipesService.searchRecipesMenu(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("flag", "0");
		}
		return map;
	}
	
	
	/* 
	    * @Description:添加食谱
	    * 
	    * @param:
	    * 
	    * 食谱时间：recipeDate
	    * 食谱类型：type，0早餐，1午餐
	    * 学校id：schoolId
	    * 食谱菜单id：ids(为菜单id拼接)
	    */
	@RequestMapping("/addRecipes")
	@ResponseBody
	public Map<String, Object> addRecipes(Recipes recipes) {
		Map<String, Object> map=new HashMap<>(); 
		try {				
			map=recipesService.addRecipes(recipes);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("flag", "0");
		}
		return map;
	}
	
	
	/* 
	    * @Description:删除食谱
	    * 
	    * @param:
	    * 
	    * 食谱id:id
	    */
	@RequestMapping("/deleteRecipes")
	@ResponseBody
	public Map<String, Object> deleteRecipes(Recipes recipes) {
		Map<String, Object> map=new HashMap<>(); 
		try {				
			map=recipesService.deleteRecipes(recipes);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("flag", "0");
		}
		return map;
	}
	
	
	/* 
	    * @Description:修改食谱
	    * 
	    * @param:
	    * 
	    * 食谱id：id
	    * 食谱时间：recipeDate
	    * 食谱类型：type，0早餐，1午餐
	    * 食谱菜单id：ids
	    */
	@RequestMapping("/updateRecipes")
	@ResponseBody
	public Map<String, Object> updateRecipes(Recipes recipes) {
		Map<String, Object> map=new HashMap<>(); 
		try {				
			map=recipesService.updateRecipes(recipes);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("flag", "0");
		}
		return map;
	}
	
	
	/* 
	    * @Description获取某一天食谱
	    * 
	    * @param:
	    * 
	    * 学校id：id
	    * 食谱日期：date
	    */
	@RequestMapping("/getRecipes")
	@ResponseBody
	public Map<String, Object> getRecipes(FenyeData fenyeData) {
		Map<String, Object> map=new HashMap<>(); 
		try {			
			map=recipesService.getRecipes(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("flag", "0");
		}
		return map;
	}
	
	/* 
	    * @Description:获取学校发布食谱列表
	    * 
	    * @param:
	    * 
	    * 学校id：id
	    */
	@RequestMapping("/getPublishRecipes")
	@ResponseBody
	public Map<String, Object> getPublishRecipes(FenyeData fenyeData) {
		Map<String, Object> map=new HashMap<>(); 
		try {			
			map=recipesService.getPublishRecipes(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("flag", "0");
		}
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
