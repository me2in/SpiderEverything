package com.wong.spider.downloader;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.wong.spider.common.Page;
import com.wong.spider.common.Request;
import com.wong.spider.common.Website;
import com.wong.spider.proxy.Proxy;
import com.wong.spider.task.Task;
import com.wong.spider.util.MyStringUtils;
/**
 * 基本代码来自webmagic,增加了request的类型，原先只支持html 
 * 对于看不懂的代码，暂时去掉，已观察其作用
 *
 * @author wangjuntao
 * @date 2017-2-22
 * @since
 */
public class HttpClientDownloader implements Downloader {

	protected final ConcurrentMap<String, CloseableHttpClient> httpClients = new ConcurrentHashMap<String, CloseableHttpClient>();
	protected final ConcurrentMap<String, HttpContext> httpContexts = new ConcurrentHashMap<String, HttpContext>();
	protected PoolingHttpClientConnectionManager connectionManager;

	protected static Logger logger = LoggerFactory
			.getLogger(HttpClientDownloader.class.getName());

	public HttpClientDownloader() {
		SSLConnectionSocketFactory sslConnectionSocketFactory = null;
		try {
			sslConnectionSocketFactory = new SSLConnectionSocketFactory(
					createIgnoreVerifySSL(), NoopHostnameVerifier.INSTANCE);//NoopHostnameVerifier 部分网站未设置SSL,此时检查会导致报错
		} catch (Exception e) {
			e.printStackTrace();
		}
		Registry<ConnectionSocketFactory> reg = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", sslConnectionSocketFactory).build();
		connectionManager = new PoolingHttpClientConnectionManager(reg);
		connectionManager.setDefaultMaxPerRoute(100);
	}

	protected CloseableHttpClient getHttpClient(Website site) {//创建client时设置代理是为了什么？代理的用户名密码是在此处设置
		if (MyStringUtils.isEmpty(site.getDomain())) {
			throw new RuntimeException("websit domain must be declare!");
		}
		CloseableHttpClient httpClient = httpClients.get(site.getDomain());
		if(httpClient == null){
			synchronized (this) {
				HttpClientBuilder httpClientBuilder = HttpClients.custom();
				httpClientBuilder.setConnectionManager(connectionManager);
				
				CookieStore cookieStore = new BasicCookieStore();
				for(String domain : site.getCookies().keySet()){
					for(Map.Entry<String, String> cookieEntity : site.getCookies().get(domain).entrySet()){
						BasicClientCookie cookie = new BasicClientCookie(cookieEntity.getKey(), cookieEntity.getValue());
						cookie.setDomain(domain);
						cookieStore.addCookie(cookie);
					}
				}
				
				httpClientBuilder.setDefaultCookieStore(cookieStore);
				httpClients.put(site.getDomain(), httpClientBuilder.build());
			}
		}
		return httpClients.get(site.getDomain());
	}

	protected HttpUriRequest getHttpUriRequest(Website site, Request request,
			Proxy proxy) {
		RequestBuilder requestBuilder = selectRequestMethod(request);
		requestBuilder.setUri(request.getUrl());
		for(Map.Entry<String, String> headEntity : site.getHeaders().entrySet()){//设置头部信息
			requestBuilder.addHeader(headEntity.getKey(), headEntity.getValue());
		}
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(site.getTimeOut())
                .setSocketTimeout(site.getTimeOut())
                .setConnectTimeout(site.getTimeOut())
                .setRedirectsEnabled(site.isRedirectsEnabled())
                .setCircularRedirectsAllowed(false)
                .setMaxRedirects(0)
                .setCookieSpec(CookieSpecs.STANDARD);
		if(proxy != null){
			requestConfigBuilder.setProxy(new HttpHost(proxy.getIp(),proxy.getPort()));
		}
		requestBuilder.setConfig(requestConfigBuilder.build());
		return requestBuilder.build();
	}
	
	protected RequestBuilder selectRequestMethod(Request request) {
        String method = request.getMethod();
        if (method == null || method.equalsIgnoreCase("GET")) {
            return RequestBuilder.get();
        } else if (method.equalsIgnoreCase("POST")) {
            RequestBuilder requestBuilder = RequestBuilder.post();
            NameValuePair[] nameValuePair = (NameValuePair[]) request.getParam("nameValuePair");
            if (nameValuePair != null && nameValuePair.length > 0) {
                requestBuilder.addParameters(nameValuePair);
            }
            return requestBuilder;
        } 
        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }

	protected HttpContext getHttpContext(Website site) {
		if (MyStringUtils.isEmpty(site.getDomain())) {
			throw new RuntimeException("websit domain must be declare!");
		}
		if (httpContexts.get(site.getDomain()) == null) {
			synchronized (this) {
				HttpContext context = HttpClientContext.create();
				httpContexts.put(site.getDomain(), context);
			}
		}
		return httpContexts.get(site.getDomain());
	}

	@Override
	public Page download(Request request, Task task) {
		
		if(!request.getUrl().startsWith("http") && !request.getUrl().startsWith("https")){//如果链接地址不是http或者https开头则直接返回null
			return null;
		}
		
		Website site = task.getSite();
		Proxy proxy = request.isUseProxy()&&site.isEnableProxy() ? site.getProxy() : null;//request 可设置不使用代理
		CloseableHttpClient httpClient = getHttpClient(site);
		HttpUriRequest httpRequest = getHttpUriRequest(site, request, proxy);
		int statusCode = 0;
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpClient
					.execute(httpRequest);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			request.addParam(Request.STATUS_CODE, statusCode);
			if (statusCode == 200) {// 为200时才包装返回内容
				logger.info("download page {}", request.getUrl());
				Page page = handleResponse(request, site.getDefaultCharset(),
						httpResponse);
				return page;
			} else {
				logger.warn("download page {} error,status code {}",
						request.getUrl(), statusCode);
				return null;
			}
		} catch (Exception e) {
			logger.error("download page {} error! message: {}", request.getUrl(), e.getMessage());
			return addToCycleRetry(request, site);
		} finally {
			request.addParam(Request.STATUS_CODE, statusCode);
			if (request.isUseProxy() && site.isEnableProxy() && proxy != null) {
				site.returnProxy2Pool(proxy, statusCode);
			}
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					logger.error("close response fail error", e);
				}
			}
		}
	}

	protected Page addToCycleRetry(Request request, Website site) {
		if (site.getCycleRetry() > 0
				&& (site.getCycleRetry() > request.getRetryCount())) {
			Page page = new Page();
			request.addRetryCount();
			request.addParam(Request.IS_NEED_RETRY, true);
			page.addTargetRequest(request);
			page.setNeedCycleRequest(true);
			return page;
		}
		return null;
	}

	protected Page handleResponse(Request request, String defaultCharset,
			CloseableHttpResponse response) throws ParseException, IOException {
		Page page = new Page();
		page.setRequest(request);
		page.setStatusCode(response.getStatusLine().getStatusCode());
		switch (request.getType()) {
		case Request.HTML:
			//这里没有必要从header的Content-Type获得编码，EntityUtils内部已经做了这样的操作，只需要传递一个默认的编码给EntityUtils就可以了
			String content = EntityUtils.toString(response.getEntity(),
					defaultCharset);
			page.setRawText(content);
			break;
		case Request.IMAGE:
		case Request.FILE:
			String mimeType = ContentType.get(response.getEntity())
					.getMimeType();
			if (mimeType.indexOf("text") < 0) {// 返回的必须是文件类型
				long contentLength = Long.valueOf(response.getLastHeader("Content-Length").getValue());
				long realLength = response.getEntity().getContentLength();//这个是实际拿到的长度？还是头部声明的长度？
				page.setContentLength(realLength);
				logger.debug("get file ({} recevice {})",contentLength,realLength);
				
				byte[] data = EntityUtils.toByteArray(response.getEntity());
				page.setData(data);
			} else {
				page.setStatusCode(403);
				request.addParam(Request.STATUS_CODE, 403);
			}
			break;
		default:
			logger.debug("不支持的文件类型： {}", request.getType());
			break;
		}
		return page;
	}

	@Override
	public void setThread(int threadNum) {
		connectionManager.setMaxTotal(threadNum);
	}

	protected SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException,
			KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}
}
