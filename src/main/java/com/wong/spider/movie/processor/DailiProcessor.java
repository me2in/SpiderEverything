package com.wong.spider.movie.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import us.codecraft.xsoup.Xsoup;

import com.wong.spider.Page;
import com.wong.spider.Request;
import com.wong.spider.annotation.Processor;
import com.wong.spider.processor.PageProcessor;
import com.wong.spider.util.MyFileUtils;
@Processor(domain="http://www.youdaili.net")
@Component
public class DailiProcessor implements PageProcessor {
	
	private  CloseableHttpClient client = HttpClients.createDefault();
	private  Header UserAgentHead = new BasicHeader("User-Agent", "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
	private  Header accept = new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	private  Header accept_encoding = new BasicHeader("Accept-Encoding","gzip, deflate, sdch, br");
	private  Header accept_Language = new BasicHeader("Accept-Language","zh-CN,zh;q=0.8");
	private  Header connection = new BasicHeader("Connection","keep-alive");
	private ExecutorService es = Executors.newFixedThreadPool(30);//开启10个线程校验

	@Override
	public boolean canProcess(Page page) {
		return page.getRequest().getUrl().matches("http://www.youdaili.net/Daili/(guonei|http)/(([0-9]+).html)?") 
				|| page.getRequest().getUrl().equals("http://www.youdaili.net/");
	}

	@Override
	public void process(Page page) {
		Document doc = Jsoup.parse(page.getRawText());
		
		List<String> links = Xsoup.select(doc,"//a/@href").list();
		page.addTargetRequest(selectList("http://www.youdaili.net/Daili/(guonei|http)/(([0-9_]+).html)?", links));
		
		List<String> plist = Xsoup.select(doc, "/html/body/div[5]/div[1]/div[1]/div[3]/div[3]/p/text()").list();
		
		if(plist !=null && !plist.isEmpty()){
			Map<String[],Future<Boolean>> allProxys = new HashMap<String[],Future<Boolean>>();
			for(String pp : plist){
				if(pp.indexOf(":")<0){
					continue;
				}
				final String ip = pp.substring(0, pp.indexOf(":"));
				final String port = pp.substring(pp.indexOf(":")+1, pp.indexOf("@"));
				String[] proxy = {ip,port};
				allProxys.put(proxy,es.submit(new Callable<Boolean>() {
					
					@Override
					public Boolean call() throws Exception {
						return vaildProxy(ip,Integer.valueOf(port)) && vaildProxy(ip,Integer.valueOf(port));
					}
				}));
			}
			
			List<String[]> proxyList = new ArrayList<String[]>();
			for(Map.Entry<String[],Future<Boolean>> entity : allProxys.entrySet()){
				try {
					if(entity.getValue().get()){
						proxyList.add(entity.getKey());
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			page.putField("proxyList", proxyList);
		}

	}

	@Override
	public void serializer(Page page) {
		
		List<String[]> proxyList = page.getResultItems().get("proxyList");
		
		StringBuilder sb = new StringBuilder();
		if(proxyList !=null){
			for(String[] proxy : proxyList){
				sb.append(String.format("%s:%s\r\n", proxy[0],proxy[1]));
			}
			MyFileUtils.writeStringAppend("data/proxy.txt", sb.toString());
		}
		
//		if(proxyList !=null){
//			List<Proxy> proxys = new ArrayList<Proxy>();
//			for(String[] proxy : proxyList){
//				Proxy newProxy = new Proxy(proxy[0],Integer.valueOf(proxy[1]));
//				proxys.add(newProxy);
//			}
//			ProxyCache.produce(proxys);
//		}
	}
	
	private boolean vaildProxy(String ip,int port){
		RequestConfig rc = RequestConfig.custom()
	            .setCircularRedirectsAllowed(false)
	            .setMaxRedirects(0)
	            .setSocketTimeout(3000)
	            .setProxy(new HttpHost(ip,port))
	            .setConnectionRequestTimeout(3000)
	            .setConnectTimeout(3000)
	            .setRedirectsEnabled(true)
	            .setCookieSpec(CookieSpecs.STANDARD).build();
		HttpGet httpGet = new HttpGet("http://pianyuan.net/r_ZkvCD9pg0.html");
		httpGet.setHeader(UserAgentHead);
		httpGet.setHeader(accept);
		httpGet.setHeader(accept_Language);
		httpGet.setHeader(accept_encoding);
		httpGet.setHeader(connection);
		httpGet.setConfig(rc);
		
		int statusCode = 0;
		
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			
			statusCode = response.getStatusLine().getStatusCode();
			
			if(statusCode == 200){
				Header type = response.getFirstHeader("Content-Type");
				String chartSet = null;
				if(type.getValue().indexOf("charset")>0){
					String value = type.getValue();
					chartSet = value.substring(value.indexOf("charset")+8);
				}
				String html = EntityUtils.toString(entity,chartSet);
				Xsoup.select(Jsoup.parse(html),"/html/body/div[2]/div/div/div[2]/h2/a/text()").get().trim();
			}
			
			
			EntityUtils.consume(entity);
			
			response.close();
			
		} catch (Exception e) {
			statusCode = 500;
			httpGet.abort();
		}
		
		return statusCode==200;
	}
	
	private List<Request> selectList(String regex, List<String> strings) {
        List<Request> results = new ArrayList<Request>();
        for (String string : strings) {
        	if(string.matches(regex)){
        		results.add(Request.RequestHtml(string).setUseProxy(false));
        	}
        }
        return results;
	}
	
	public static void main(String[] args) {
		String pp = "125.89.198.81:8888@HTTP#广东省韶关市 电信";
		String ip = pp.substring(0, pp.indexOf(":"));
		String port = pp.substring(pp.indexOf(":")+1, pp.indexOf("@"));
		System.out.println(String.format("[%s]:[%s]", ip,port));
	}

}
