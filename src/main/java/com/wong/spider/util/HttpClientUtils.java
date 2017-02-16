package com.wong.spider.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
/**
 * HttpClient Http请求工具类
 * @author Gentle
 */
public class HttpClientUtils {
	
	private static CloseableHttpClient client = HttpClients.createDefault();
	private static HttpClientContext context = HttpClientContext.create();
	private static Header UserAgentHead = new BasicHeader("User-Agent", "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
	private static Header accept = new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	private static Header accept_encoding = new BasicHeader("Accept-Encoding","gzip, deflate, sdch, br");
	private static Header accept_Language = new BasicHeader("Accept-Language","zh-CN,zh;q=0.8");
	private static Header connection = new BasicHeader("Connection","keep-alive");
	
	private static HttpHost proxy = new HttpHost("121.8.98.202",8080);
	private static RequestConfig rc = RequestConfig.custom()
            .setCircularRedirectsAllowed(false)
            .setMaxRedirects(0)
            .setSocketTimeout(20000)
            .setProxy(proxy)
            .setConnectionRequestTimeout(20000)
            .setConnectTimeout(20000)
            .setRedirectsEnabled(true)
            .setCookieSpec(CookieSpecs.STANDARD).build();
	//多线程模式，待实现
//	private static ThreadLocal<CloseableHttpClient> clientStore = new ThreadLocal<CloseableHttpClient>();
//	private static ThreadLocal<HttpClientContext> contextStore = new ThreadLocal<HttpClientContext>();
//	private static CloseableHttpClient getClient(){
//		CloseableHttpClient client2 = clientStore.get();
//		if(client2==null){
//			client2 = HttpClients.createDefault();
//			clientStore.set(client2);
//		}
//		return client2;
//	}
//	private static HttpClientContext getContext(){
//		HttpClientContext context2 = contextStore.get();
//		if(context2==null){
//			context2 = HttpClientContext.create();
//			contextStore.set(context2);
//		}
//		return context2;
//	}
	static{
		
		setContent();
	}
	private static void setContent(){
		CookieStore store = new BasicCookieStore();
//		BasicClientCookie phpSessionId = new BasicClientCookie("PHPSESSID","8kuph1p4114j5p07s88mh08p06");
//		phpSessionId.setDomain("pianyuan.net");
//		phpSessionId.setPath("/");
		BasicClientCookie py_loginauth = new BasicClientCookie("py_loginauth", "WyJNZXppMDQiLDE0ODQxODQyMTcsIjlhNTQ2MmEwMjBlZjY5OTAiXQ");
		py_loginauth.setDomain("pianyuan.net");
		py_loginauth.setPath("/");
//		py_loginauth.setExpiryDate(expiryDate)
//		BasicClientCookie yunsuo_session_verify = new BasicClientCookie("yunsuo_session_verify", "0ac468550a6a388c02286d17582ef76c");
//		yunsuo_session_verify.setDomain(".pianyuan.net");
//		yunsuo_session_verify.setPath("/");
		
//		store.addCookie(yunsuo_session_verify);
		store.addCookie(py_loginauth);
//		store.addCookie(phpSessionId);
		context.setCookieStore(store);
	}
	
	private static HttpGet getHttpGet(String url){
		HttpGet get = new HttpGet(url);
		get.setConfig(rc);
		
//		Header host = new BasicHeader("Host", "www.kuaidaili.com");
//		Header reffer = new BasicHeader("Referer","http://www.kuaidaili.com/proxylist/");
//		Header insecure = new BasicHeader("Upgrade-Insecure-Requests","1");
//		get.setHeader(host);
//		get.setHeader(reffer);
//		get.setHeader(insecure);
		
		
		get.setHeader(UserAgentHead);
		get.setHeader(accept);
		get.setHeader(accept_Language);
		get.setHeader(accept_encoding);
		get.setHeader(connection);
		return get;
	}
	
	private static HttpPost getHttpPost(String url){
		HttpPost post = new HttpPost(url);
		post.setConfig(rc);
		post.setHeader(UserAgentHead);
		post.setHeader(UserAgentHead);
		post.setHeader(accept);
		post.setHeader(accept_Language);
		post.setHeader(accept_encoding);
		post.setHeader(connection);
		return post;
	}
	
	public static String doPost(String url){
		return doPost(url,null);
	}
	
	public static String doGet(String url){
		return doGet(url,null);
	}
	
	public static byte[] doGetImage(String url){
		return doGetImage(url, null);
	}
	
	public static String doPost(String url,Map<String,String> params){
		HttpPost post = getHttpPost(url);
		if(params!=null && !params.isEmpty()){
			List<NameValuePair> data = new ArrayList<NameValuePair>();
			for(String key:params.keySet()){
				String value = params.get(key);
				if(value!=null){
					data.add(new BasicNameValuePair(key, value));
				}
			}
			UrlEncodedFormEntity formParams = new UrlEncodedFormEntity(data, Consts.UTF_8);
			post.setEntity(formParams);
		}
		try{
			CloseableHttpResponse response = client.execute(post, context);
			try {
				HttpEntity entity = response.getEntity();
				if(response.getStatusLine().getStatusCode()!=200){
					EntityUtils.consume(entity);
					return null;
				}
				Header type = response.getFirstHeader("Content-Type");
				String chartSet = null;
				if(type.getValue().indexOf("charset")>0){
					String value = type.getValue();
					chartSet = value.substring(value.indexOf("charset")+8);
				}
				return EntityUtils.toString(entity,chartSet);
			}finally{
				response.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String doGet(String url,Map<String,String> params){
		try {
			URIBuilder builder = new URIBuilder(url);
			if(params!=null && !params.isEmpty()){
				for(String key:params.keySet()){
					String value = params.get(key);
					if(value!=null){
						builder.addParameter(key, value);
					}
				}
			}
			HttpGet get = getHttpGet(builder.toString());
			CloseableHttpResponse response = client.execute(get, context);
			try{
				HttpEntity entity = response.getEntity();
				System.out.println("statusCode:"+response.getStatusLine().getStatusCode());
				if(response.getStatusLine().getStatusCode()!=200){
					EntityUtils.consume(entity);
					return null;
				}
				Header type = response.getFirstHeader("Content-Type");
				String chartSet = null;
				if(type.getValue().indexOf("charset")>0){
					String value = type.getValue();
					chartSet = value.substring(value.indexOf("charset")+8);
				}
				return EntityUtils.toString(entity,chartSet);
			}catch(Exception e){
				get.abort(); 
			}finally{
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] doGetImage(String url,Map<String,String> params){
		try {
			URIBuilder builder = new URIBuilder(url);
			if(params!=null && !params.isEmpty()){
				for(String key:params.keySet()){
					String value = params.get(key);
					if(value!=null){
						builder.addParameter(key, value);
					}
				}
			}
			HttpGet get = getHttpGet(builder.toString());
			CloseableHttpResponse response = client.execute(get, context);
			try{
				Header type = response.getFirstHeader("Content-Type");
				if(type.getValue().indexOf("text")>0){ //如果是文本则直接抛弃
					return null;
				}
				return EntityUtils.toByteArray(response.getEntity());
			}catch(Exception e){
				get.abort(); 
			}finally{
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] doGetFile(String url){
		return doGetFile(url,null);
	}
	
	public static byte[] doGetFile(String url,Map<String,String> params){
		try {
			URIBuilder builder = new URIBuilder(url);
			if(params!=null && !params.isEmpty()){
				for(String key:params.keySet()){
					String value = params.get(key);
					if(value!=null){
						builder.addParameter(key, value);
					}
				}
			}
			HttpGet get = getHttpGet(builder.toString());
			CloseableHttpResponse response = client.execute(get, context);
			try{
				return EntityUtils.toByteArray(response.getEntity());
			}finally{
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void finalize() throws Throwable {
			client.close();
			super.finalize();
	}
}
