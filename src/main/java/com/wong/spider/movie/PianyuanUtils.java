package com.wong.spider.movie;

import java.util.ArrayList;
import java.util.List;

import com.wong.spider.Request;

public class PianyuanUtils {
	
	public static final String FILE_SAVE_BASEDIR = "F:/movieSpiderV3";
	
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
	
	public static Request fixRequest(String url){
		return Request.RequestHtml(fixUrl(url));
	}
	
	public static List<Request> fixRequest(List<String> urlist){
		List<Request> list = new ArrayList<Request>();
		for(String url : urlist){
			Request request = Request.RequestHtml(fixUrl(url));
			list.add(request);
		}
		return list;
	}

}
