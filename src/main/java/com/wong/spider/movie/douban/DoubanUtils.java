package com.wong.spider.movie.douban;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.wong.spider.movie.config.ApplicationConfig;
import com.wong.spider.movie.model.Movie;
import com.wong.spider.movie.service.IMovieService;
import com.wong.spider.util.HttpClientUtils;

/**
 * 从豆瓣抓取信息更新到数据库
 * @author weien
 *
 */
@Component
public class DoubanUtils {
	
	@Resource
	private IMovieService movieService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private final String api = "https://api.douban.com/v2/movie/subject/";
	
	public void updateScore(){
		Page<Movie> pageData = null;
		int index = 0;
		do{
			logger.info("第{}页数据",index+1);
			pageData = movieService.findAllMoviePage(index, 40);
			for(Movie mv :pageData.getContent()){
				
				//
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(mv.getDouban()!=null && !mv.getDouban().trim().isEmpty()){
					String mid = mv.getDouban().substring(mv.getDouban().lastIndexOf("/"));
					String realPath = api+"/"+mid;
					String response = HttpClientUtils.doGet(realPath);
					JSONObject json = JSONObject.parseObject(response);
					String score = json.getJSONObject("rating").getString("average");
					logger.info("电影评分[{}]:[{}]",json.getString("aka"),score);
					mv.setScore(score);
					movieService.updateMovie(mv);
				}
			}
			
			index ++;
		}while(pageData.hasNext());
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		DoubanUtils du = context.getBean(DoubanUtils.class);
		du.updateScore();
	}
	
	

}
