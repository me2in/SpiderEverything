package com.wong.spider.movie;

import java.util.ArrayList;
import java.util.List;

public class PianyuanUtils {
	
	public static final String FILE_SAVE_BASEDIR = "F:/movieSpiderV2";
	
	public static String fixUrl(String url){
		if(url.indexOf("pianyuan.net")<0){
			return "http://pianyuan.net"+url;
		}
		return url;
	}
	
	public static List<String> fixUrl(List<String> urlist){
		List<String> list = new ArrayList<String>();
		for(String url : urlist){
			url = fixUrl(url);
			list.add(url);
		}
		return list;
	}

}
