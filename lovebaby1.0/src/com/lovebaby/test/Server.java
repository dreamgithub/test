
    /**  
    * @Title: Server.java
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
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket ss;
    private Socket s;
    private BufferedReader br;
    private PrintWriter pw;
    public Server() {
        try {
                ss = new ServerSocket(10000);
                System.out.println("Server is starting...");
                while (true) {
                	s = ss.accept();
                	br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    pw = new PrintWriter(s.getOutputStream(),true);
                    String line = br.readLine();
                    System.out.println(line);
                    pw.println("your word is:" + line);
                    //pw.println("helloworld");
                    br.close();
					
                    pw.close();
				}
                
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        new Server();
    }
}
