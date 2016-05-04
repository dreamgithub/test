
    /**  
    * @Title: TestDemo.java
    * @Package com.lovebaby.test
    * @Description: 
    * @author likai
    * @date 2015年11月11日
    * @version V1.0  
    */
    
package com.lovebaby.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.lovebaby.dao.JdbcDao;

/**
    * @ClassName: TestDemo
    * @Description: 
    * @author likai
    * @date 2015年11月11日
    *
    */

public class TestDemo {
	
	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
	JdbcDao jdbcDao=(JdbcDao) ctx.getBean("jdbcDao");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void name() {
		/*String sql="SELECT id from members WHERE id in("+"'1'"+",'2')";
		List list=new ArrayList<>();
		list.add("1");
		list.add("2");
		String[] a=new String[]{"1","2"};
		@SuppressWarnings("unused")
		Object[] parma=new Object[]{a.toString()};
		List re=jdbcDao.queryForList(sql, null);
		System.out.println(re.toString());*/
		String[] ids=new String[]{"1","2","4","5"};
		String in="(";
		for (int i = 0; i <ids.length-1; i++) {
			in=in+"'"+ids[i]+"',";
		}
		in=in+"'"+ids[ids.length-1]+"')";
		System.out.println(in);
	}
}
