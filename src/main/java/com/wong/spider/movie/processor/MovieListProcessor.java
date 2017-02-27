package com.wong.spider.movie.processor;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import us.codecraft.xsoup.Xsoup;

import com.wong.spider.Page;
import com.wong.spider.annotation.Processor;
import com.wong.spider.movie.PianyuanUtils;
import com.wong.spider.processor.PageProcessor;
@Component
@Processor(domain="http://pianyuan.net")
public class MovieListProcessor implements PageProcessor {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public boolean canProcess(Page page) {
		return page.getRequest().getUrl().matches("http://pianyuan.net/mv(\\?p=\\d{1,})?");
	}

	@Override
	public void process(Page page) {
		Document doc = Jsoup.parse(page.getRawText());
		List<String> movieDetails = Xsoup.select(doc, "//*[@id='main-container']/div/div[1]/div/div/div[1]/a/@href").list();
		List<String> indexlist = Xsoup.select(doc, "//*[@id='main-container']/div/div[1]/div[38]/ul/li/a/@href").list();	
		
		page.addTargetRequest(PianyuanUtils.fixRequest(movieDetails));
		page.addTargetRequest(PianyuanUtils.fixRequest(indexlist));
		
	}

	@Override
	public void serializer(Page page) {
		//nothing to do...
	}

}
