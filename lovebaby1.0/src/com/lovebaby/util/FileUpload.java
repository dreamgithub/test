package com.lovebaby.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUpload {
	public static void upload(File file,String path){
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			@SuppressWarnings("resource")
			FileOutputStream fos = new FileOutputStream(path);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error:" + e.getMessage());
		}
	}
}
