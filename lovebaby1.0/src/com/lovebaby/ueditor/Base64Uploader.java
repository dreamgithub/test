
    /**  
    * @Title: Base64Uploader.java
    * @Package com.lovebaby.ueditor
    * @Description: 
    * @author likai
    * @date 2015年11月20日
    * @version V1.0  
    */
    
package com.lovebaby.ueditor;
import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.BaseState;
 import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
 import java.util.Map;
 import org.apache.commons.codec.binary.Base64;
 
 
 public final class Base64Uploader
 {
   public Base64Uploader() {}
   
   public static State save(String content, Map<String, Object> conf)
   {
    byte[] data = decode(content);
     
    long maxSize = ((Long)conf.get("maxSize")).longValue();
     
    if (!validSize(data, maxSize)) {
       return new BaseState(false, 1);
     }
     
     String suffix = FileType.getSuffix("JPG");
     
    String savePath = PathFormat.parse((String)conf.get("savePath"), 
      (String)conf.get("filename"));
    boolean keepLocalFile = "false".equals(conf.get("keepLocalFile")) ? false : true;
   savePath = savePath + suffix;
   String physicalPath = (String)conf.get("rootPath") + savePath;
     
  State storageState = StorageManager.saveBinaryFile(data, physicalPath,keepLocalFile);
    if (storageState.isSuccess()) {
      storageState.putInfo("url", PathFormat.format(savePath));
      storageState.putInfo("type", suffix);
      storageState.putInfo("original", "");
     }
     
    return storageState;
   }
   
   private static byte[] decode(String content) {
   return Base64.decodeBase64(content);
   }
   
   private static boolean validSize(byte[] data, long length) {
     return data.length <= length;
   }
 }