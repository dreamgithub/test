
    /**  
    * @Title: RecipesService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月18日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.Map;

import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.RecipeMenu;
import com.lovebaby.pojo.Recipes;

/**
    * @ClassName: RecipesService
    * @Description: 
    * @author likai
    * @date 2015年11月18日
    *
    */

public interface RecipesService {

	Map<String, Object> addRecipesMenu(RecipeMenu recipeMenu) throws Exception;
	Map<String, Object> deleteRecipesMenu(FenyeData fenyeData) throws Exception;
	Map<String, Object> updateRecipesMenu(RecipeMenu recipeMenu) throws Exception;
	Map<String, Object> getRecipesMenu(FenyeData fenyeData) throws Exception;
	Map<String, Object> addRecipes(Recipes recipes) throws Exception;
	 Map<String, Object> deleteRecipes(Recipes recipes) throws Exception;
	 Map<String, Object> updateRecipes(Recipes recipes) throws Exception;
	 Map<String, Object> getRecipes(FenyeData fenyeData) throws Exception;
	 Map<String, Object> getPublishRecipes(FenyeData fenyeData) throws Exception;
	 Map<String, Object> searchRecipesMenu(FenyeData fenyeData) throws Exception;
}
