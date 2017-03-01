package com.wong.spider.movie;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wong.spider.common.Request;
import com.wong.spider.util.MyStringUtils;

public class PianyuanUtils {
	
	public static String FILE_SAVE_BASEDIR ;
	
	private static Logger logger = LoggerFactory.getLogger(PianyuanUtils.class);
	
	static {
		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/movie-spider.properties");
		try {
			prop.load(in);
			FILE_SAVE_BASEDIR = prop.getProperty("file.save.basedir").trim();
			if(MyStringUtils.isEmpty(FILE_SAVE_BASEDIR)){
				FILE_SAVE_BASEDIR = "data/movie";
				logger.info("未设置文件保存路径，默认存放{}",FILE_SAVE_BASEDIR); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String fixUrl(String url){
		if(url.indexOf("http")<0){
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
	
	public static Request prepare4Request(Request request){
		String url = request.getUrl();
		if(url.lastIndexOf("http")>0 || url.lastIndexOf("ed2k")>0){
			request.setUrl(url.substring("http://pianyuan.net".length()));
		}
		return request;
	}
	
	public static String getPosterSavePath(String movieName,String posterName){
		return FILE_SAVE_BASEDIR+File.separator+movieName+File.separator+posterName;
	}
	
	public static String getTorrentSavePath(String movieName,String type,String torrentName){
		return FILE_SAVE_BASEDIR+File.separator+movieName+File.separator+type+File.separator+torrentName+".torrent";
	}
	
	public static String getMovieBaseDir(String movieName){
		return FILE_SAVE_BASEDIR+File.separator+movieName;
	}
	
	public static void main(String[] args) {
		String url = "http://pianyuan.nethttp://|file|A.Midsummer.Nights.Dream.1999.720p.BLURAY.x264-HD.mkv|4683353859|2C51374531F4D04B2097F89C4014CF12|h=KJFZ5ZLPKSYT7MYL6HP3ZXNX3DU3PGU4|/";
		System.out.println(url.lastIndexOf("http"));
	}
}
