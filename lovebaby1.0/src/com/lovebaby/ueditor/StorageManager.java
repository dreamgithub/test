
    /**  
    * @Title: StorageManager.java
    * @Package com.lovebaby.util
    * @Description: 
    * @author likai
    * @date 2015年11月19日
    * @version V1.0  
    */
    
package com.lovebaby.ueditor;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class StorageManager
{
    
  public static final int BUFFER_SIZE = 8192;
  private static FTPClient ftp;
  
  
  public static State saveFileByInputStream(InputStream is, String path, long maxSize,boolean keepLocalFile)
  {
    State state = null;

    File tmpFile = getTmpFile();

    byte[] dataBuf = new byte[2048];
    BufferedInputStream bis = new BufferedInputStream(is, 8192);
    try
    {
      BufferedOutputStream bos = new BufferedOutputStream(
        new FileOutputStream(tmpFile), 8192);

      int count = 0;
      while ((count = bis.read(dataBuf)) != -1) {
        bos.write(dataBuf, 0, count);
      }
      bos.flush();
      bos.close();

      if (tmpFile.length() > maxSize) {
        tmpFile.delete();
        return new BaseState(false, 1);
      }

    //添加图片到ftp
      state = saveTmpFile(tmpFile, path, keepLocalFile);

      if (!state.isSuccess()) {
        tmpFile.delete();
      }


      return state;
    }
    catch (IOException localIOException) {
    }
    return new BaseState(false, 4);
  }

  public static State saveFileByInputStream(InputStream is, String path,boolean keepLocalFile) {
    State state = null;
    File tmpFile = getTmpFile();

    byte[] dataBuf = new byte[2048];
    BufferedInputStream bis = new BufferedInputStream(is, 8192);
    try
    {
      BufferedOutputStream bos = new BufferedOutputStream(
        new FileOutputStream(tmpFile), 8192);

      int count = 0;
      while ((count = bis.read(dataBuf)) != -1) {
        bos.write(dataBuf, 0, count);
      }
      bos.flush();
      bos.close();
    //添加图片到ftp
      state = saveTmpFile(tmpFile, path, keepLocalFile);

      if (!state.isSuccess()) {
        tmpFile.delete();
      }

      return state;
    } catch (IOException localIOException) {
    }
    return new BaseState(false, 4);
  }

  //涂鸦上传ftp
  private static State saveTmpFile(File tmpFile, String path,boolean keepLocalFile) {
	  State state = null;
      File targetFile = new File(path);
 
      //上传ftp
      try
      {
         Properties prop=getProp();
         if (connect(prop.getProperty("sourcepath"),prop.getProperty("ip"), Integer.parseInt(prop.getProperty("port")), prop.getProperty("username"), prop.getProperty("psw"))) {
      	   String name=path.substring(path.lastIndexOf("/")+1);
      	   ftp.enterLocalPassiveMode();  
      	   InputStream is=new FileInputStream(targetFile);        	   
      	   if ( !ftp.storeFile(name,is) ) {
          	  return new BaseState(false, 4);
			   }
      	   is.close();
         }           
      }catch (Exception e) {
          return new BaseState(false, 4);
      }
      
      try
      {
          if(! keepLocalFile)
          	targetFile.delete();
              //targetFile.delete();
      }catch(Exception e){
          
      }

      state = new BaseState(true);
      state.putInfo("size", targetFile.length());
      state.putInfo("title", targetFile.getName());

      return state;
  }

  
  
  
  
  /**
   * 上传FTP文件
   * @param is
   * @param path
   * @param maxSize
   * @return
   */
  private static File getTmpFile() {
	    File tmpDir = FileUtils.getTempDirectory();
	    double d = Math.random() * 10000.0D;
	    String tmpFileName = String.valueOf(d).replace(".", "");
	    return new File(tmpDir, tmpFileName);
	  }
  private static State valid(File file) {
	    File parentPath = file.getParentFile();

	    if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
	      return new BaseState(false, 3);
	    }

	    if (!parentPath.canWrite()) {
	      return new BaseState(false, 2);
	    }

	    return new BaseState(true);
	  }
  
  public static State saveFtpFileByInputStream(InputStream is, String remoteDir, String path, long maxSize,boolean keepLocalFile)
  {
    State state = null;
	//添加图片到本地
    File tmpFile = getTmpFile();

    byte[] dataBuf = new byte[2048];
    BufferedInputStream bis = new BufferedInputStream(is, 8192);
    try
    {
      BufferedOutputStream bos = new BufferedOutputStream(
        new FileOutputStream(tmpFile), 8192);

      int count = 0;
      while ((count = bis.read(dataBuf)) != -1) {
        bos.write(dataBuf, 0, count);
      }
      bos.flush();
      bos.close();

      if (tmpFile.length() > maxSize) {
        tmpFile.delete();
        return new BaseState(false, 1);
      }
      //添加图片到ftp
      state = saveFtpTmpFile(tmpFile, path, keepLocalFile);

      if (!state.isSuccess()) {
        tmpFile.delete();
      }

      return state;
    }
    catch (IOException localIOException) {
    }
    return new BaseState(false, 4);
  }
  
//涂鸦上传
  public static State saveBinaryFile(byte[] data, String path,boolean keepLocalFile)
  {
    File file = new File(path);
    State state = valid(file);
    if (!state.isSuccess()) {
      return state;
    }
    try
    {
      BufferedOutputStream bos = new BufferedOutputStream(
        new FileOutputStream(file));
      bos.write(data);
      bos.flush();
      bos.close();
    } catch (IOException ioe) {
      return new BaseState(false, 4);
    }
    //添加图片到ftp
    state = saveTmpFile(file, path, keepLocalFile);

    if (!state.isSuccess()) {
      file.delete();
    }

    return state;
  }

  
  private static State saveFtpTmpFile(File tmpFile, String path,boolean keepLocalFile) {
        State state = null;
        File targetFile = new File(path);
        if (targetFile.canWrite())
          return new BaseState(false, 2);
        try
        {
          FileUtils.moveFile(tmpFile, targetFile);
        } catch (IOException e) {
          return new BaseState(false, 4);
        }
        //上传ftp
        try
        {
           Properties prop=getProp();
           if (connect(prop.getProperty("sourcepath"),prop.getProperty("ip"), Integer.parseInt(prop.getProperty("port")), prop.getProperty("username"), prop.getProperty("psw"))) {
        	   String name=path.substring(path.lastIndexOf("/")+1);
        	   ftp.enterLocalPassiveMode();  
        	   InputStream is=new FileInputStream(targetFile);        	   
        	   if ( !ftp.storeFile(name,is) ) {
            	  return new BaseState(false, 4);
			   }
        	   is.close();
           }           
        }catch (Exception e) {
            return new BaseState(false, 4);
        }
        
        try
        {
            if(! keepLocalFile)
            	targetFile.delete();
                //targetFile.delete();
        }catch(Exception e){
            
        }

        state = new BaseState(true);
        state.putInfo("size", targetFile.length());
        state.putInfo("title", targetFile.getName());

        return state;
  }
  
  /* 
   * @Description:获取ftp链接
   * 
   * @param:
   *
   */
private static  boolean connect(String path,String addr,int port,String username,String password) throws Exception {    

   boolean result = false;    
   ftp = new FTPClient();    
   int reply;    
   ftp.connect(addr,port);   
   ftp.login(username,password);  
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
public static Properties getProp() {
	Properties prop = new Properties();
	StorageManager storageManager=new StorageManager();
	InputStream fis=storageManager.getClass().getResourceAsStream("/ftp.properties");
	try {
		prop.load(fis);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}//将属性文件流装载到Properties对象中 
	return prop;
}
}
