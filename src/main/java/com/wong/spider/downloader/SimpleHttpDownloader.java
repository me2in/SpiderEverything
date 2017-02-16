package com.wong.spider.downloader;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.wong.spider.Page;
import com.wong.spider.proxy.Proxy;
import com.wong.spider.proxy.ProxyPool;
import com.wong.spider.proxy.SimpleProxyPool;
import com.wong.spider.util.MyFileUtils;

public class SimpleHttpDownloader implements Downloader {
	
	private static CloseableHttpClient client = HttpClients.createDefault();
//	private static HttpClientContext context = HttpClientContext.create();
	private static Header UserAgentHead = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
	private static Header accept = new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	private static Header accept_encoding = new BasicHeader("Accept-Encoding","gzip, deflate, sdch, br");
	private static Header accept_Language = new BasicHeader("Accept-Language","zh-CN,zh;q=0.8");
	private static Header connection = new BasicHeader("Connection","keep-alive");
	
	
	
	private ProxyPool proxyPool = new SimpleProxyPool();
	

	@Override
	public Page download(String url) {
		
		Page page = new Page();
		page.setUrl(url);
		
		Proxy proxy = null;
		
		if(proxyPool.isEnable()){
			proxy = proxyPool.getProxy();
		}
		
		HttpGet httpGet = getHttpGet(url, proxy);
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			page.setStatusCode(response.getStatusLine().getStatusCode());
			
			if(response.getStatusLine().getStatusCode()!=200){
				EntityUtils.consume(entity);
				page.setRawText("");
			}else{
				Header type = response.getFirstHeader("Content-Type");
				String chartSet = null;
				if(type.getValue().indexOf("charset")>0){
					String value = type.getValue();
					chartSet = value.substring(value.indexOf("charset")+8);
				}
				page.setRawText(EntityUtils.toString(entity,chartSet));
			}
			
			response.close();
			
		} catch (Exception e) {
			httpGet.abort();
			page.setStatusCode(500);
		}
		
		if(proxy != null){
			proxyPool.returnProxyToPool(proxy, page.getStatusCode());
		}
		
		return page;
	}

	@Override
	public boolean dowmloadFile(String url, String filepath, boolean isRetry) {
		
		Proxy proxy = null;
		
		if(proxyPool.isEnable()){
			proxy = proxyPool.getProxy();
		}
		
		HttpGet httpGet = getHttpGet(url, proxy);
		
		int statusCode = 0;
		
		try {
			CloseableHttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			
			statusCode = response.getStatusLine().getStatusCode();
			
			if(statusCode!=200){
				EntityUtils.consume(entity);
				return false;
			}else{
				
				Header type = response.getFirstHeader("Content-Type");
				if(type.getValue().indexOf("text")>0){ //如果是文本则直接抛弃
					return false;
				}
				byte[] byts = EntityUtils.toByteArray(response.getEntity());
				response.close();
				try{
					MyFileUtils.writeFile(filepath, byts);
				}catch(Exception e){
					
				}
				return true;
			}
			
			
		} catch (Exception e) {
			statusCode = 500;
			httpGet.abort();
		}
		
		if(proxy != null){
			proxyPool.returnProxyToPool(proxy, statusCode);
		}
		
		
		return true;
	}
	
	private HttpGet getHttpGet(String url,Proxy proxy){
		RequestConfig.Builder rcb = RequestConfig.custom()
	            .setCircularRedirectsAllowed(false)
	            .setMaxRedirects(0)
	            .setSocketTimeout(20000)
	            .setConnectionRequestTimeout(20000)
	            .setConnectTimeout(20000)
	            .setRedirectsEnabled(true)
	            .setCookieSpec(CookieSpecs.STANDARD);
		if(proxy != null){
			rcb.setProxy(new HttpHost(proxy.getIp(),proxy.getPort()));
		}
		HttpGet get = new HttpGet(url);
		get.setHeader(UserAgentHead);
		get.setHeader(accept);
		get.setHeader(accept_Language);
		get.setHeader(accept_encoding);
		get.setHeader(connection);
		get.setConfig(rcb.build());
		
		return get;
	}
}
