
    /**  
    * @Title: RecipesServiceImpl.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月18日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lovebaby.dao.JdbcDao;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.RecipeMenu;
import com.lovebaby.pojo.Recipes;
import com.lovebaby.util.GetUuid;

/**
    * @ClassName: RecipesServiceImpl
    * @Description: 
    * @author likai
    * @date 2015年11月18日
    *
    */
@SuppressWarnings("rawtypes")
@Service(value="recipesService")
public class RecipesServiceImpl implements RecipesService {
	@Autowired
	private JdbcDao jdbcDao;
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger log=Logger.getLogger(RecipesServiceImpl.class.getName());

	
	
	public RecipesServiceImpl() {
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
    * @Description: 添加食谱菜单
    * 
    * @param recipeMenu
    * @return
    * @see com.lovebaby.service.RecipesService#addRecipesMenu(com.lovebaby.pojo.RecipeMenu)
    */
    
	@Override
	public Map<String, Object> addRecipesMenu(RecipeMenu recipeMenu) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//插入食谱菜单
			String sql="insert into menu(id,type,name,schoolId,introduce,date,pic) values(?,?,?,?,?,?,?)";
			Object[] params=new Object[]{
					GetUuid.getUuid(),
					recipeMenu.getType(),
					recipeMenu.getName(),
					recipeMenu.getSchoolId(),
					recipeMenu.getIntroduce(),				
					format.format(new Date()),
					recipeMenu.getPic()
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}

	}
	
	
	  /* 
	    * @Description: 删除食谱菜单
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.RecipesService#deleteRecipesMenu(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> deleteRecipesMenu(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//插入食谱菜单
			String sql="UPDATE menu set state='0' where id=?";
			Object[] params=new Object[]{
					fenyeData.getId()
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

    /* 
	    * @Description: 修改食谱菜单
	    * 
	    * @param recipeMenu
	    * @return
	    * @see com.lovebaby.service.RecipesService#updateRecipesMenu(com.lovebaby.pojo.RecipeMenu)
	    */
	    
	@Override
	public Map<String, Object> updateRecipesMenu(RecipeMenu recipeMenu) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//插入食谱菜单
			String sql="UPDATE menu set `name`=?,pic=?,introduce=? where id=?";
			Object[] params=new Object[]{
					recipeMenu.getName(),
					recipeMenu.getPic(),
					recipeMenu.getIntroduce(),
					recipeMenu.getId()
			};
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}

	}

	
	
    /* 
	    * @Description: 获取食谱菜单
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.RecipesService#getRecipesMenu(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getRecipesMenu(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String sql="";
			Object[] params=null;
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("recipesaddress");
			if (fenyeData.getType().equals("0")||fenyeData.getType().equals("2")) {			
				sql="SELECT *,CONCAT(?,pic) as 'ftpimage' FROM menu where type='0' and state='1' ORDER BY rand() LIMIT 4";
				params=new Object[]{
						headimgae
				};
				//获取系统推荐菜单
				List introduce=jdbcDao.queryForList(sql, params);	
				//获取系统推荐菜单总数
				sql="SELECT count(*) from menu where type='0'and state='1'";
				int icount=jdbcDao.queryForInt(sql, null);
				map.put("icount", icount);
				map.put("introduce", introduce);

			}
			if (fenyeData.getType().equals("0")||fenyeData.getType().equals("1")) {			
				//获取学校所有菜单
				sql="SELECT *,CONCAT(?,pic) as 'ftpimage' from menu WHERE schoolId=? AND type='1' and state='1' order by date desc limit ?,?";
				params=new Object[]{
						headimgae,
						fenyeData.getId(),
						fenyeData.getPage_num(),
						fenyeData.getPage_size()
				};
				List own=jdbcDao.queryForList(sql, params);
				//获取学校所有菜单总数
				sql="SELECT count(*) from menu WHERE schoolId=? AND type='1' and state='1'";
				params=new Object[]{
						fenyeData.getId()
				};
				int ocount=jdbcDao.queryForInt(sql, params);		
				map.put("own", own);
				map.put("ocount", ocount);
			}
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	
	}


    /* 
    * @Description: 搜索菜单
    * 
    * @param fenyeData
    * @return
    * @see com.lovebaby.service.RecipesService#searchRecipesMenu(com.lovebaby.pojo.FenyeData)
    */
    
	@Override
	public Map<String, Object> searchRecipesMenu(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//插入食谱菜单
			String sql="";
			Object[] params=null;
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("recipesaddress");
			sql="SELECT *,CONCAT(?,pic) as 'ftpimage' from menu where (type='0' and `name` LIKE '%"+fenyeData.getName()+"%') or (type='1' AND `name` LIKE '%"+fenyeData.getName()+"%' AND schoolId=?) order by date desc limit ?,?";
			params=new Object[]{
					headimgae,
					fenyeData.getId(),
					fenyeData.getPage_num(),
					fenyeData.getPage_size()
			};
			List result=jdbcDao.queryForList(sql, params);
			//获取学校所有菜单总数
			sql="SELECT count(*) from menu where (type='0' and `name` LIKE '%"+fenyeData.getName()+"%') or (type='1' AND `name` LIKE '%"+fenyeData.getName()+"%' AND schoolId=?)";
			params=new Object[]{
					fenyeData.getId()
			};
			int count=jdbcDao.queryForInt(sql, params);		
			map.put("result", result);
			map.put("count", count);

			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}
	
	    /* 
	    * @Description: 添加食谱
	    * 
	    * @param recipes
	    * @return
	    * @see com.lovebaby.service.RecipesService#addRecipes(com.lovebaby.pojo.Recipes)
	    */
	    
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> addRecipes(Recipes recipes) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String[] ids=recipes.getIds().split(",");
			//插入食谱
			String sql="INSERT into recipes(id,recipeDate,type,schoolId,date) VALUES(?,?,?,?,?)";
			Object[] params=null;
			String id=GetUuid.getUuid();
			params=new Object[]{
					id,
					recipes.getRecipeDate(),
					recipes.getType(),
					recipes.getSchoolId(),
					format.format(new Date())
			};
			jdbcDao.update(sql, params);
			//插入食谱关系表
			sql="INSERT into recipes_relate(id,mid,sid) VALUES(?,?,?)";
			List list=new ArrayList<>();
			for (int i = 0; i <ids.length; i++) {
				list.add(new Object[]{
						GetUuid.getUuid(),
						ids[i],
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
	    * @Description: 删除食谱
	    * 
	    * @param recipes
	    * @return
	    * @see com.lovebaby.service.RecipesService#deleteRecipes(com.lovebaby.pojo.Recipes)
	    */
	    
	@Override
	public Map<String, Object> deleteRecipes(Recipes recipes) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//删除食谱
			String sql="DELETE from recipes where id=?";
			Object[] params=new Object[]{
					recipes.getId()
			};
			jdbcDao.update(sql, params);
			//删除关系表
			sql="DELETE from recipes_relate where sid=?";
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
	
	}


	
	    /* 
	    * @Description: 修改食谱
	    * 
	    * @param recipes
	    * @return
	    * @see com.lovebaby.service.RecipesService#updateRecipes(com.lovebaby.pojo.Recipes)
	    */
	    
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> updateRecipes(Recipes recipes) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			String[] ids=recipes.getIds().split(",");
			//删除食谱
			String sql="UPDATE recipes set recipeDate=?,type=? where id=?";
			Object[] params=new Object[]{
					recipes.getRecipeDate(),
					recipes.getType(),
					recipes.getId()
			};
			jdbcDao.update(sql, params);
			//删除关系表
			sql="DELETE from recipes_relate where sid=?";
			jdbcDao.update(sql, params);
			//插入新关系
			sql="INSERT into recipes_relate(id,mid,sid) VALUES(?,?,?)";
			List list=new ArrayList<>();
			for (int i = 0; i <ids.length; i++) {
				list.add(new Object[]{
						GetUuid.getUuid(),
						ids[i],
						recipes.getId()
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
	    * @Description: 获取食谱
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.RecipesService#getRecipes(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getRecipes(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			Properties prop=getProp("/ftp.properties");
			String headimgae=prop.getProperty("url")+"/"+prop.getProperty("recipesaddress");
			//获取食谱列表
			String sql="SELECT * from recipes where schoolId=? and recipeDate=?";
			Object[] params=new Object[]{
					fenyeData.getId(),
					fenyeData.getDate()
			};
			List result=jdbcDao.queryForList(sql, params);
			//给食谱添加菜单列表
			for (int i = 0; i <result.size(); i++) {
				Map<String, Object> data=(Map<String, Object>) result.get(i);
				String id=(String) data.get("id");
				sql="SELECT *,CONCAT(?,t1.pic) as 'ftpimage' from menu t1 where EXISTS(SELECT t2.mid from (SELECT mid from recipes_relate where sid=?) t2 where t1.id=t2.mid)";
				params=new Object[]{
						headimgae,
						id
				};
				List menu=jdbcDao.queryForList(sql, params);
				data.put("menu", menu);
			}
			map.put("result", result);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}


	
	    /* 
	    * @Description: 获取发布食谱列表
	    * 
	    * @param fenyeData
	    * @return
	    * @see com.lovebaby.service.RecipesService#getPublishRecipes(com.lovebaby.pojo.FenyeData)
	    */
	    
	@Override
	public Map<String, Object> getPublishRecipes(FenyeData fenyeData) throws Exception {
		try {
			Map< String, Object> map=new HashMap<>();
			//获取食谱列表
			String sql="DELETE from recipes where id=?";
			Object[] params=new Object[]{
					
			};
			jdbcDao.update(sql, params);
			//删除关系表
			sql="DELETE from recipes_relate where sid=?";
			jdbcDao.update(sql, params);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception();
		}
		
	}

	
	

	
	


}
