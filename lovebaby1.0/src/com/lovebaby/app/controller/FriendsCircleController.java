
/**  
* @Title: SpeechController.java
* @Package com.lovebaby.app.controller
* @Description: 
* @author likai
* @date 2015年11月26日
* @version V1.0  
*/

package com.lovebaby.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lovebaby.pojo.Comment;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Praise;
import com.lovebaby.pojo.Reply;
import com.lovebaby.pojo.Speech;
import com.lovebaby.service.SpeechService;
import com.lovebaby.util.GetUuid;

/**
 * @ClassName: SpeechController
 * @Description: 朋友圈管理
 * @author likai
 * @date 2015年11月26日
 *
 */
@Controller
@RequestMapping("/friendsCircle")
@SuppressWarnings("rawtypes")
public class FriendsCircleController {

	private FTPClient ftp;
	@Autowired
	private SpeechService speechService;
	private static Logger log=Logger.getLogger(FriendsCircleController.class.getName());

	public FriendsCircleController() {
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
	 * @Description:获取朋友圈
	 * 
	 * @param: 用户id：id 用户类型：type 分页所在页：page_num 每页数据数：page_size
	 */
	@RequestMapping("/getFriendsCircle")
	@ResponseBody
	public Map<String, Object> getFriendsCircle(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.getFriendsCircle(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
	}

	/*
	 * @Description:获取用户说说列表
	 * 
	 * @param: 用户id：id 分页所在页：page_num 每页数据数：page_size
	 */
	@RequestMapping("/getUserSpeech")
	@ResponseBody
	public Map<String, Object> getUserSpeech(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.getUserSpeech(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		

		return map;
	}
	
	/*
	 * @Description:获取某条说说内容
	 * 
	 * @param: 
	 * 
	 * 用户id:uid
	 * 说说id:id
	 */
	@RequestMapping("/getSpeechContent")
	@ResponseBody
	public Map<String, Object> getSpeechContent(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.getSpeechContent(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		

		return map;
	}

	/*
	 * @Description:删除说说
	 * 
	 * @param:
	 * 
	 * 说说id：id
	 */
	@RequestMapping("/deleteSpeech")
	@ResponseBody
	public Map<String, Object> deleteSpeech(Speech speech) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.deleteSpeech(speech);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		return map;
	}

	/*
	 * @Description:发布说说
	 * 
	 * @param: 用户id：mid 说说内容：content 图片集合：files
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addSpeech")
	@ResponseBody
	public Map<String, Object> addSpeech(MultipartHttpServletRequest request, Speech speech) throws Exception {
		Map<String, Object> map = new HashMap<>();
		try {
			String filename = "";
			String name = ""; 
			List pics = new ArrayList<>();
			Properties prop = getProp();
			List<MultipartFile> files = request.getFiles("files");
			if (files!=null&&files.size()!=0) {
				for (int i = 0; i < files.size(); i++) {
					filename = files.get(i).getOriginalFilename();
					name = GetUuid.getUuid() + filename.substring(filename.lastIndexOf("."));
					pics.add(name);
					// 上传图片到ftp
					connect(prop.getProperty("speechaddress"), prop.getProperty("ip"),
							Integer.parseInt(prop.getProperty("port")), prop.getProperty("username"),
							prop.getProperty("psw"));
					ftp.enterLocalPassiveMode();
					InputStream input = files.get(i).getInputStream();
					ftp.storeFile(name, input);
					input.close();
					ftp.disconnect();   
				}
				// 添加说说
				speech.setPics(pics);
				map = speechService.addSpeech(speech);				
			} else {
				// 没有上传图片，则直接上传说说
				map = speechService.addSpeech(speech);
			}
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}
		return map;
	}

	/*
	 * @Description:评论说说
	 * 
	 * @param:
	 * 
	 * 评论人id:mid 评论内容:content 说说id:tid 评论人名称:username
	 */
	@RequestMapping("/addComment")
	@ResponseBody
	public Map<String, Object> addComment(Comment comment) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.addComment(comment);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		/*
		 * 添加推送代码
		 */

		return map;
	}

	/*
	 * @Description:回复说说
	 * 
	 * @param:
	 * 
	 * 评论id：tid 回复人id：fromid 接收人id：toid 回复内容:content 回复人名称：toname 接收人名称：toname
	 * 评论人名称:username
	 */
	@RequestMapping("/addReply")
	@ResponseBody
	public Map<String, Object> addReply(Reply reply) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.addReply(reply);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}		
		/*
		 * 添加推送代码
		 */
		return map;
	}

	/*
	 * @Description:点赞
	 * 
	 * @param:
	 * 
	 * 点赞人id：id 说说id：tid 点赞人名称：username
	 */
	@RequestMapping("/addPraise")
	@ResponseBody
	public Map<String, Object> addPraise(Praise praise) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.addPraise(praise);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}	
		return map;
	}

	/*
	 * @Description:取消点赞
	 * 
	 * @param:
	 * 
	 * 用户id：mid 
	 * 说说id：tid 
	 */
	@RequestMapping("/deletePraise")
	@ResponseBody
	public Map<String, Object> deletePraise(Praise praise) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.deletePraise(praise);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}	
		return map;
	}
	
	/*
	 * @Description:判断是否有新的说说动态
	 * 
	 * @param:
	 * 
	 * 用户id：id 用户类型：type
	 */
	@RequestMapping("/hasNewSpeech")
	@ResponseBody
	public Map<String, Object> hasNewSpeech(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.hasNewSpeech(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}	
		return map;
	}

	/*
	 * @Description:获取有未读回复的说说列表
	 * 
	 * @param:
	 * 
	 * 说说id：id(未读说说id拼接)
	 */
	@RequestMapping("/getUnreadReply")
	@ResponseBody
	public Map<String, Object> getUnreadReply(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.getUnreadReply(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}	
		return map;
	}

	/*
	 * @Description:获取未读回复数量和信息
	 * 
	 * @param:
	 * 
	 * 用户id：id
	 */
	@RequestMapping("/getUnreadReplyNum")
	@ResponseBody
	public Map<String, Object> getUnreadReplyNum(FenyeData fenyeData) {
		Map< String, Object> map=new HashMap<>();
		try {			
			map = speechService.getUnreadReplyNum(fenyeData);
			map.put("flag", "1");
		} catch (Exception e) {
			map.put("flag", "0");
			log.error(e.getMessage());
		}	
		return map;
	}

	/*
	 * @Description:获取ftp链接
	 * 
	 * @param:
	 *
	 */
	private boolean connect(String path, String addr, int port, String username, String password) throws Exception {

		boolean result = false;
		ftp = new FTPClient();
		int reply;
		ftp.connect(addr, port);
		ftp.login(username, password);
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
		InputStream fis = this.getClass().getResourceAsStream("/ftp.properties");
		try {
			prop.load(fis);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 将属性文件流装载到Properties对象中
		return prop;
	}
}
