
    /**  
    * @Title: BinaryUploader.java
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
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;



public class BinaryUploader
{
  public BinaryUploader() {}
  
  public static final State save(HttpServletRequest request, Map<String, Object> conf)
  {
     FileItemStream fileStream = null;
     boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;
    
     if (!ServletFileUpload.isMultipartContent(request)) {
       return new BaseState(false, 5);
    }
    
     ServletFileUpload upload = new ServletFileUpload(
       new DiskFileItemFactory());
    
     if (isAjaxUpload) {
       upload.setHeaderEncoding("UTF-8");
    }
    try
    {
       FileItemIterator iterator = upload.getItemIterator(request);
      
       while (iterator.hasNext()) {
         fileStream = iterator.next();
        
         if (!fileStream.isFormField())
          break;
         fileStream = null;
      }
      
       if (fileStream == null) {
         return new BaseState(false, 7);
      }
      
       String savePath = (String)conf.get("savePath");
       String originFileName = fileStream.getName();
       String suffix = FileType.getSuffixByFilename(originFileName);
      
       originFileName = originFileName.substring(0, 
         originFileName.length() - suffix.length());
       savePath = savePath + suffix;
      
       long maxSize = ((Long)conf.get("maxSize")).longValue();
      
       if (!validType(suffix, (String[])conf.get("allowFiles"))) {
         return new BaseState(false, 8);
      }
      
       savePath = PathFormat.parse(savePath, originFileName);
      
       String physicalPath = (String)conf.get("rootPath") + savePath;
      
       InputStream is = fileStream.openStream();
       boolean keepLocalFile = "false".equals(conf.get("keepLocalFile")) ? false : true;
       State storageState = StorageManager.saveFileByInputStream(is, 
         physicalPath, maxSize,keepLocalFile);
       is.close();
      
       if (storageState.isSuccess()) {
        storageState.putInfo("url", PathFormat.format(savePath));
         storageState.putInfo("type", suffix);
        storageState.putInfo("original", originFileName + suffix);
      }
      
       return storageState;
    } catch (FileUploadException e) {
       return new BaseState(false, 6);
    }
    catch (IOException localIOException) {}
     return new BaseState(false, 4);
   }
   
  private static boolean validType(String type, String[] allowTypes) {
    List<String> list = Arrays.asList(allowTypes);
     
     return list.contains(type);
  }
}
