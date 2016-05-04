
    /**  
    * @Title: Client.java
    * @Package com.lovebaby.test
    * @Description: 
    * @author likai
    * @date 2015年11月24日
    * @version V1.0  
    */
    
package com.lovebaby.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.lovebaby.pojo.Babies;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;

public class Client {

    private Socket s;
    private BufferedReader br;
    //private BufferedReader line;
    private PrintWriter pw;
    private String line = "";
    public Client() {
        try{
            s = new Socket("127.0.0.1",10000);
            pw = new PrintWriter(s.getOutputStream(),true);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        	Babies babies=new Babies();
        	babies.setBabyName("李凯");
        	babies.setNum("1223");
        	JSONValue jsonValue;
        	String jsonStr = null;
			try {
				jsonValue = JSONMapper.toJSON(babies);
				jsonStr = jsonValue.render(false);
			} catch (MapperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
            pw.println(jsonStr);
            line = br.readLine();
            System.out.println(line);

            br.close();
            pw.close();
        }catch(IOException ie){
            ie.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
    	new Client();
    }
}
