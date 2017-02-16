package com.wong.spider.util;

import java.util.Date;

import org.joda.time.DateTime;

public class MyDateUtils {
	
//	public static Date parseSpecilDate(String str){
//		if(str.indexOf("秒") || str.indexOf("分钟"))
//	}
	
	public static long getNowTime(){
		DateTime now = new DateTime();
		return now.getMillis()/1000;
	}

}
