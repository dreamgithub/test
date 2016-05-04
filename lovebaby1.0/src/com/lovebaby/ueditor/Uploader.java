
    /**  
    * @Title: Uploader.java
    * @Package com.lovebaby.util
    * @Description: 
    * @author likai
    * @date 2015年11月19日
    * @version V1.0  
    */
    
package com.lovebaby.ueditor;
import com.baidu.ueditor.define.State;
import com.lovebaby.ueditor.Base64Uploader;
import com.lovebaby.ueditor.BinaryUploader;

import java.util.Map;
import com.lovebaby.ueditor.FtpUploader;
import javax.servlet.http.HttpServletRequest;

public class Uploader {
    
      private HttpServletRequest request = null;
      private Map<String, Object> conf = null;

      public Uploader(HttpServletRequest request, Map<String, Object> conf) {
        this.request = request;
        this.conf = conf;
      }

      public final State doExec() {
        String filedName = (String)this.conf.get("fieldName");
        State state = null;
        /*if ("true".equals(this.conf.get("useFtpUpload"))) {
        	 state = FtpUploader.save(request, conf);
		}else if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName), 
		            this.conf);
		}else {
			state = BinaryUploader.save(this.request, this.conf);
		}*/
        //保留原有逻辑,在json.config中加入是否使用FTP上传配置项
        if ("true".equals(this.conf.get("isBase64"))){
          state = Base64Uploader.save(this.request.getParameter(filedName), 
            this.conf);
        }else {
          if("true".equals(this.conf.get("useFtpUpload"))){
              state = FtpUploader.save(request, conf);
          
          }else{
            state = BinaryUploader.save(this.request, this.conf);
          }
        }

        return state;
      }
}
