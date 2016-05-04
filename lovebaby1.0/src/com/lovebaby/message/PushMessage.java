
    /**  
    * @Title: PushMessage.java
    * @Package com.lovebaby.message
    * @Description: 
    * @author likai
    * @date 2015年12月29日
    * @version V1.0  
    */
    
package com.lovebaby.message;

import io.rong.models.Message;
import net.sf.json.JSONObject;

/**
    * @ClassName: PushMessage
    * @Description: 
    * @author likai
    * @date 2015年12月29日
    *
    */

public class PushMessage extends Message {
	private JSONObject content;
	private String type;
	public JSONObject getContent() {
		return content;
	}
	public void setContent(JSONObject content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public PushMessage(JSONObject content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	
	    /* 
	    * @Description: 
	    * 
	    * @return
	    * @see java.lang.Object#toString()
	    */
	    
	@Override
	public String toString() {
		return content.toString();
	}
		
	
	
}
