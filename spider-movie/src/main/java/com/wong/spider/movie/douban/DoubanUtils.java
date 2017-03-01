package com.wong.spider.movie.douban;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.wong.spider.util.HttpClientUtils;

/**
 * 从豆瓣抓取信息更新到数据库
 * 
 * @author weien
 * 
 */
public class DoubanUtils {

	private  static Logger logger = LoggerFactory.getLogger(DoubanUtils.class);

	private final static String api = "https://api.douban.com/v2/movie/subject/";

	public static String getMovieScore(String mid) {
		
		
		String result = HttpClientUtils.doGet(api+mid);
		
		JSONObject json = JSONObject.parseObject(result);
		
		System.out.println(json);
		
		return json.getJSONObject("rating").getString("average");
	}
	
	public static void main(String[] args) {
		System.out.println(getMovieScore("4019265"));
	}

}
