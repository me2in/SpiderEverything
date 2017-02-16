package com.wong.spider.movie.processor;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import us.codecraft.xsoup.Xsoup;

import com.wong.spider.Page;
import com.wong.spider.downloader.Downloader;
import com.wong.spider.movie.PianyuanUtils;
import com.wong.spider.processor.PageProcessor;
@Component
public class MovieListProcessor implements PageProcessor {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public boolean canProcess(Page page) {
		return page.getUrl().matches("http://pianyuan.net/mv(\\?p=\\d{1,})?");
	}

	@Override
	public void process(Page page,Downloader downloader) {
		Document doc = Jsoup.parse(page.getRawText());
		List<String> movieDetails = Xsoup.select(doc, "//*[@id='main-container']/div/div[1]/div/div/div[1]/a/@href").list();
		List<String> indexlist = Xsoup.select(doc, "//*[@id='main-container']/div/div[1]/div[38]/ul/li/a/@href").list();	
//		System.out.println(movieDetails);
//		System.out.println(indexlist);
		log.info("获取到电影列表：{}",movieDetails);
		log.info("底部导航链接：{}",indexlist);
		page.addTargetUrl(PianyuanUtils.fixUrl(movieDetails));
		page.addTargetUrl(PianyuanUtils.fixUrl(indexlist));
		
	}

	@Override
	public void serializer(Page page,Downloader downloader) {
		//nothing to do...
	}

}
