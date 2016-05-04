package com.lovebaby.util;
import java.io.File;  
import java.io.FileInputStream;  
import org.apache.commons.net.ftp.FTPClient;  
import org.apache.commons.net.ftp.FTPReply;  
  
public class Test {    
     
    private  FTPClient ftp;    
    /** 
     *  
     * @param path 上传到ftp服务器哪个路径下    
     * @param addr 地址 
     * @param port 端口号 
     * @param username 用户名 
     * @param password 密码 
     * @return 
     * @throws Exception 
     */  
    public  boolean connect(String path,String addr,int port,String username,String password) throws Exception {    

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
    /** 
     *  
     * @param file 上传的文件或文件夹 
     * @throws Exception 
     */  
    public void upload(File file) throws Exception{    
    	 
        if(file.isDirectory()){       
            ftp.makeDirectory(file.getName());              
            ftp.changeWorkingDirectory(file.getName());    
            String[] files = file.list();    
            System.out.println(file.toString());
            for (int i = 0; i < files.length; i++) {    
                File file1 = new File(file.getPath()+"\\"+files[i] );    
                if(file1.isDirectory()){    
                    upload(file1);    
                    ftp.changeToParentDirectory();    
                }else{                  
                    File file2 = new File(file.getPath()+"\\"+files[i]);    
                    FileInputStream input = new FileInputStream(file2);    
                    ftp.storeFile(file2.getName(), input);    
                    input.close();                          
                }               
            }    
        }else{       
            FileInputStream input = new FileInputStream(file); 
            ftp.enterLocalPassiveMode();
            System.out.println("sssss");
            ftp.storeFile(new String(file.getName().getBytes(), "gbk"),input);    
            input.close();      
        }    
    }    
   public static void main(String[] args) throws Exception{  
     Test t = new Test();  
    //t.connect("lovebaby", "140.206.158.164", 21, "lovebaby", "Jsd1407");  
     t.connect("source/lovebaby/advertisement/", "192.168.140.129", 21, "lovebaby", "jsd1407");
     System.out.println(t.connect("source/lovebaby/advertisement/", "192.168.140.129", 21, "lovebaby", "jsd1407"));
      //t.connect("/xxtest", "115.28.147.188", 21, "root", "cqmyg123");  
     // File file = new File("F:/li.txt");  
     File file = new File("C:/Users/jo/Desktop/lovebaby_web/WebContent/images/arcadian.png");
      t.upload(file);  
      System.out.println("ok");
      
   }  
}  