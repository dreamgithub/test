
    /**  
    * @Title: SpeechService.java
    * @Package com.lovebaby.service
    * @Description: 
    * @author likai
    * @date 2015年11月27日
    * @version V1.0  
    */
    
package com.lovebaby.service;

import java.util.List;
import java.util.Map;

import com.lovebaby.pojo.Comment;
import com.lovebaby.pojo.FenyeData;
import com.lovebaby.pojo.Praise;
import com.lovebaby.pojo.Reply;
import com.lovebaby.pojo.Speech;

/**
    * @ClassName: SpeechService
    * @Description: 
    * @author likai
    * @date 2015年11月27日
    *
    */

public interface SpeechService {
	Map<String, Object> addSpeech(Speech speech) throws Exception;
	Map<String, Object> getFriendsCircle(FenyeData fenyeData) throws Exception;
	Map<String, Object> addComment(Comment comment) throws Exception;
	Map<String, Object> addReply(Reply reply) throws Exception;
	Map<String, Object> addPraise(Praise praise) throws Exception;
	Map<String, Object> getUserSpeech(FenyeData fenyeData) throws Exception;
	Map<String, Object> getSpeechContent(FenyeData fenyeData) throws Exception;
	Map<String, Object> deleteSpeech(Speech speech) throws Exception;
	Map<String, Object> hasNewSpeech(FenyeData fenyeData) throws Exception;
	Map<String, Object> getUnreadReply(FenyeData fenyeData) throws Exception;
	Map<String, Object> getUnreadReplyNum(FenyeData fenyeData) throws Exception;
	Map<String, Object> deletePraise(Praise praise) throws Exception;
	List<String> getSpeechMembers(String id,String uid) throws Exception;
}
