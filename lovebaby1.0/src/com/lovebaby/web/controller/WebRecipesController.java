
    /**  
    * @Title: RecipesController.java
    * @Package com.lovebaby.app.controller
    * @Description: 
    * @author likai
    * @date 2015年11月18日
    * @version V1.0  
    */
    
package com.lovebaby.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.PermissionCheck;
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
@RequestMapping("/webrecipes")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WebRecipesController extends PermissionCheck{

private FTPClient ftp;
@Autowired
private RecipesService recipesService;

}
