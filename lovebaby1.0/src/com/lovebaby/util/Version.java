package com.lovebaby.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Version {
	
	public static void writeProperties(String parameterName,
			String parameterValue) {
		Properties prop = new Properties();
		try {
			InputStream fis = new FileInputStream("version.properties");
			// ���������ж�ȡ�����б?���Ԫ�ضԣ�
			prop.load(fis);
			// ���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�
			// ǿ��Ҫ��Ϊ���Եļ��ֵʹ���ַ�����ֵ�� Hashtable ���� put �Ľ��
			OutputStream fos = new FileOutputStream("version.properties");
			prop.setProperty(parameterName, parameterValue);
			// ���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��
			// ���� Properties ���е������б?���Ԫ�ضԣ�д�������
			prop.store(fos,parameterName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	  
	public static String readValue(String key) {
		Properties props = new Properties();
		try {
			//InputStream in = new BufferedInputStream (new FileInputStream("lovebaby/version.properties"));
			InputStream in = Version.class.getResourceAsStream("/version.properties");
			props.load(in);
		    String value = props.getProperty (key);
		    return value;
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	return null;
		    }
	}
}
