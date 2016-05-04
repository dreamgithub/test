
    /**  
    * @Title: Recipes.java
    * @Package com.lovebaby.pojo
    * @Description: 
    * @author likai
    * @date 2015年11月18日
    * @version V1.0  
    */
    
package com.lovebaby.pojo;

/**
    * @ClassName: Recipes
    * @Description: 食谱
    * @author likai
    * @date 2015年11月18日
    *
    */
public class Recipes {
	private String id;
	private String type;//早0中1晚2
	private String name;//食物名称
	private String schoolId;//发布学校
	private String recipeDate;//食谱日期
	private String ids;//食谱菜单id拼接
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getRecipeDate() {
		return recipeDate;
	}
	public void setRecipeDate(String recipeDate) {
		this.recipeDate = recipeDate;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	
	
	
	
	
}
