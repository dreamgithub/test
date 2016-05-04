package com.lovebaby.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateforDay {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
	private static Calendar calendar = new GregorianCalendar();
	public static int getday(String source){
  	    try {
  	      Date date = format.parse(source);
  	      calendar.setTime(date);   
  	    } catch (Exception e) {
  	      e.printStackTrace();
  	    }
  	   return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
}
