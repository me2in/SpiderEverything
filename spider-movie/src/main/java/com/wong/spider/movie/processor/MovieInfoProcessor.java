package com.wong.spider.movie.processor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import us.codecraft.xsoup.Xsoup;

import com.wong.spider.common.Page;
import com.wong.spider.common.Request;
import com.wong.spider.annotation.Processor;
import com.wong.spider.movie.PianyuanUtils;
import com.wong.spider.movie.model.Movie;
import com.wong.spider.movie.service.IMovieService;
import com.wong.spider.processor.PageProcessor;
import com.wong.spider.util.MyFileUtils;
import com.wong.spider.util.MyStringUtils;

@Component
@Processor(domain="http://pianyuan.net")
public class MovieInfoProcessor implements PageProcessor {

	@Resource
	private IMovieService movieService;
	
	private static String pattern = "http://pianyuan.net/m_(\\w)*.html";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean canProcess(Page page) {
		
		return page.getRequest().getUrl().matches(pattern);
	}

	@Override
	public void process(Page page) {
		
		Movie  movie = new Movie();
		
		movie.setDetailUrl(page.getRequest().getUrl());
		
		Document doc = Jsoup.parse(page.getRawText());
		String name = MyStringUtils.converunctuationEng2Chinese(Xsoup.select(doc, "/html/body/div[2]/div/div/div/h1/text()").get().trim());
		String year = name.substring(name.lastIndexOf("(")+1, name.lastIndexOf(")"));
		String score = Xsoup.select(doc,"//*[@id='main-container']/div/div[1]/div[1]/div[2]/i/tidyText()").get();
		String poster = Xsoup.select(doc,"//*[@id='main-container']/div/div[1]/div[1]/div[1]/a/img/@src").get();
		
		movie.setName(name);
		movie.setScore(score);
		movie.setPoster("http:"+poster);
		movie.setYear(year);
		
		page.putField("posterName", poster.substring(poster.lastIndexOf("/")));
		
		Elements einfos = Xsoup.select(doc,"//*[@id='main-container']/div/div[1]/div[1]/ul/li").getElements();
		for(Element ei :einfos){
			String key = Xsoup.select(ei,"//strong/tidyText()").get().replace(":", "");
			String value = "";
			if(key.equals("豆瓣")){
				value = Xsoup.select(ei,"//div/a/@href").get();
			}else{
				value = MyStringUtils.tidyText(Xsoup.select(ei, "//div/tidyText()").get());
			}
			switch(key){
				case "豆瓣" :movie.setDouban(value);break;
				case "imdb":movie.setImdb(value);
				case "类型":movie.setType(Arrays.asList(value.split(",")));break;
				case "又名" :movie.setAnotherName(value);break;
				case "地区":movie.setArea(Arrays.asList(value.split(",")));break;
				case "导演":movie.setDirector(Arrays.asList(value.split(",")));break;
				case "编剧":movie.setWriter(Arrays.asList(value.split(",")));break;
				case "主演":movie.setStarring(Arrays.asList(value.split(",")));break;
			}
		}
		
		page.putField("movie", movie);
		//种子信息开始,不再区分种子的类型，也不再解析种子的类型
		List<String> realTorrentPath = Xsoup.select(doc, "//*[@id='main-container']/div/div[1]/div[2]/table/tbody/tr/td[1]/a/@href").list();
		page.addTargetRequest(PianyuanUtils.fixRequest(realTorrentPath));
		String fileSavePath = PianyuanUtils.getPosterSavePath(movie.getName(),(String) page.getResultItems().get("posterName"));
		page.addTargetRequest(Request.RequestImage(movie.getPoster()).setFileSavePath(fileSavePath));//将海报的请求申请放入到请求队列
		
		logger.info("电影:{}",movie.getName());
	}

	@Override
	public void serializer(Page page) {
		
		Movie movie = page.getResultItems().get("movie");
		List<Movie> dbM = movieService.findMovieByName(movie.getName());
		if(dbM==null || dbM.isEmpty()){
			movieService.saveMovie(movie);
		}
		
		String dir = PianyuanUtils.getMovieBaseDir(movie.getName());
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n[电影信息]\r\n");
		sb.append(String.format("    [名字]:[%s]\r\n", movie.getName()));
		sb.append(String.format("    [年份]:[%s]\r\n", movie.getYear()));
		sb.append(String.format("    [分数]:[%s]\r\n", movie.getScore()));
		sb.append(String.format("    [地区]:[%s]\r\n", movie.getArea()));
		sb.append(String.format("    [类型]:[%s]\r\n", movie.getType()));
		sb.append(String.format("    [导演]:[%s]\r\n", movie.getDirector()));
		sb.append(String.format("    [编剧]:[%s]\r\n", movie.getWriter()));
		sb.append(String.format("    [主演]:[%s]\r\n", movie.getStarring()));
		sb.append(String.format("    [imdb]:[%s]\r\n", movie.getImdb()));
		sb.append(String.format("    [豆瓣]:[%s]\r\n\r\n\r\n", movie.getDouban()));
		sb.append("[种子列表]\r\n\r\n");
		MyFileUtils.writeString(dir+File.separator+"movie_info.txt", sb.toString());
	}

}
