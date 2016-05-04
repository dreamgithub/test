
    /**  
    * @Title: IndexController.java
    * @Package com.lovebaby.web.controller
    * @Description: 
    * @author likai
    * @date 2015年12月8日
    * @version V1.0  
    */
    
package com.lovebaby.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
    * @ClassName: IndexController
    * @Description: 首页展示控制器
    * @author likai
    * @date 2015年12月8日
    *
    */
@Controller
@RequestMapping("/view")
public class IndexController {
	@RequestMapping("")
	public String index() {
		return "home";
	}
	@RequestMapping("/aboutus")
	public String home() {
		return "aboutus";
	}
	@RequestMapping("/news")
	public String news() {
		return "news";
	}
	@RequestMapping("/contactus")
	public String contactus() {
		return "contactus";
	}
	@RequestMapping("/news_content")
	public String news_content() {
		return "news_content";
	}
	@RequestMapping("/join")
	public String join() {
		return "join";
	}
}
