package com.lovebaby.util;

import java.util.UUID;
/*
 * uuid具有全球唯一性，可用来作为数据库id

*/
public class GetUuid {
	public static String getUuid() {
		String s=UUID.randomUUID().toString();
    	//去掉"-"
    	s=s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
		return s;
	}
	
}
