package com.wong.spider.movie;

import com.wong.spider.Spider;
import com.wong.spider.common.Website;
import com.wong.spider.movie.spring.SpringHolder;

public class PianyuanSpiderV3 {

	public static void main(String[] args) {

		Website dailiSite = new Website()
				.setDomain("http://www.youdaili.net")
				.setUseProxy(false);
		Spider dailiSpider = Spider.instance()
				.setSite(dailiSite)
				.addUrl("http://www.youdaili.net/")
				.addProcessor(SpringHolder.getProcessor4Site(dailiSite.getDomain()))
				.setThreadNum(5);

		Website site = new Website()
				.addHeader("User-Agent","Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)")
				.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.addHeader("Accept-Encoding", "gzip, deflate, sdch, br")
				.addHeader("Accept-Language", "zh-CN,zh;q=0.8")
				.addHeader("Connection", "keep-alive").setTimeOut(50000)
				.setDomain("http://pianyuan.net");
		Spider pianyuanSpider = Spider.instance()
				.setSite(site)
				.addUrl("http://pianyuan.net/mv")
				.addProcessor(SpringHolder.getProcessor4Site(site.getDomain()))
				.setThreadNum(20);
		
		//暂时未实现自动获取proxy，目前必须先执行dailiSpider，此爬虫会将爬到的代理保存到data/proxy.txt文件。
		
		//第一步，执行代理的爬虫,爬到差不多的代理即可关闭 。
//		dailiSpider.run();
		
		//第二步,执行需要代理的爬虫
		pianyuanSpider.run();
		
	}

}
