package com.wong.spider.util;

public class MyStringUtils {
	
	public static String converunctuationEng2Chinese(String str){
		str = str.replace(":", "：");
		str = str.replace("<", "《");
		str = str.replace(">", "》");
		str = str.replace("?", "？");
		str = str.replace("\"", "“");
		str = str.replace("\\\\", "");
		return str;
	}
	
	public static String tidyText(String badStr){
		if(badStr == null || badStr.equals("")){
			return badStr;
		}
		badStr = badStr.trim();
		badStr = badStr.replaceAll(" <>", ",").replace("\n", "");
		if(badStr.lastIndexOf(",")>0){
			badStr = badStr.substring(0, badStr.lastIndexOf(","));
		}
		return badStr;
	}

}
