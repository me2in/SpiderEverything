package com.wong.spider.movie.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.springframework.stereotype.Component;

import com.wong.spider.annotation.Processor;
import com.wong.spider.processor.AbstractProxyProcessor;
import com.wong.spider.util.MyStringUtils;

import us.codecraft.xsoup.Xsoup;
@Processor(domain="http://www.youdaili.net")
@Component
public class YouDailiProcessor extends AbstractProxyProcessor {

	@Override
	protected boolean canProcessor(String url) {
		return url
				.matches("http://www.youdaili.net/Daili/(guonei|http)/(([0-9]+).html)?")
				|| url.equals("http://www.youdaili.net/");
	}

	@Override
	protected List<String> getLinks(String html) {
		List<String> allLinks = Xsoup.select(html, "//a/@href").list();
		return MyStringUtils
				.selectStr(allLinks,
						"http://www.youdaili.net/Daili/(guonei|http)/(([0-9_]+).html)?");
	}

	@Override
	protected List<String[]> getProxys(String html) {
		List<String> plist = Xsoup.select(html,
				"/html/body/div[5]/div[1]/div[1]/div[3]/div[3]/p/text()")
				.list();
		if (plist != null && !plist.isEmpty()) {
			List<String[]> proxyList = new ArrayList<String[]>();
			for (String pp : plist) {
				if (pp.indexOf(":") < 0) {
					continue;
				}
				String ip = pp.substring(0, pp.indexOf(":"));
				String port = pp
						.substring(pp.indexOf(":") + 1, pp.indexOf("@"));
				String[] proxy = { ip, port };
				proxyList.add(proxy);
			}
			return proxyList;
		}
		return null;
	}

	@Override
	protected String getUrl4Test() {
		return "http://pianyuan.net/r_ZkvCD9pg0.html";
	}

	@Override
	protected boolean extraVerify(HttpHost proxy, String result) {
		try{
			Xsoup.select(result,"/html/body/div[2]/div/div/div[2]/h1/text()").get().trim();//如果解析不报错则可用
			return true;
		}catch(Exception e){
			return false;
		}
	}

}
