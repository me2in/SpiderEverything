package com.wong.spider.util;

import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

/**
 * HttpClient Http请求工具类
 * 
 * @author Gentle
 */
public class HttpClientUtils {

	protected static CloseableHttpClient client = HttpClients.createDefault();
	protected static Header UserAgentHead = new BasicHeader(
			"User-Agent",
			"Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
	protected static Header accept = new BasicHeader("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	protected static Header accept_encoding = new BasicHeader(
			"Accept-Encoding", "gzip, deflate, sdch, br");
	protected static Header accept_Language = new BasicHeader(
			"Accept-Language", "zh-CN,zh;q=0.8");
	protected static Header connection = new BasicHeader("Connection",
			"keep-alive");
	protected static Header[] headers = { UserAgentHead, accept,
			accept_encoding, accept_Language, connection };

	protected static HttpUriRequest getUriRequest(String url, String method,
			Map<String, String> params, HttpHost proxy) {
		RequestBuilder requestBuilder = RequestBuilder.create(method);
		requestBuilder.addHeader(UserAgentHead).addHeader(accept)
				.addHeader(accept_encoding).addHeader(accept_Language)
				.addHeader(connection);

		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
				.setConnectionRequestTimeout(2000).setSocketTimeout(2000)
				.setConnectTimeout(2000).setRedirectsEnabled(true)
				.setCircularRedirectsAllowed(false).setMaxRedirects(0)
				.setCookieSpec(CookieSpecs.STANDARD);
		if (proxy != null) {
			requestConfigBuilder.setProxy(proxy);
		}

		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entity : params.entrySet()) {
				requestBuilder.addParameter(entity.getKey(), entity.getValue());
			}
		}

		requestBuilder.setConfig(requestConfigBuilder.build());
		requestBuilder.setUri(url);
		return requestBuilder.build();
	}

	public static String doPost(String url) {
		return doRequest(url, HttpPost.METHOD_NAME, null, null);
	}

	public static String doGet(String url) {
		return doRequest(url, HttpGet.METHOD_NAME, null, null);
	}

	public static String doPost(String url, HttpHost proxy) {
		return doRequest(url, HttpPost.METHOD_NAME, null, proxy);
	}

	public static String doGet(String url, HttpHost proxy) {
		return doRequest(url, HttpGet.METHOD_NAME, null, proxy);
	}

	public static String doPost(String url, Map<String, String> params) {
		return doRequest(url, HttpPost.METHOD_NAME, params, null);
	}

	public static String doGet(String url, Map<String, String> params) {
		return doRequest(url, HttpGet.METHOD_NAME, params, null);
	}

	public static String doPost(String url, Map<String, String> params,
			HttpHost proxy) {
		return doRequest(url, HttpPost.METHOD_NAME, params, proxy);
	}

	public static String doGet(String url, Map<String, String> params,
			HttpHost proxy) {
		return doRequest(url, HttpGet.METHOD_NAME, params, proxy);
	}

	public static String doRequest(String url, String method,
			Map<String, String> params, HttpHost proxy) {
		HttpUriRequest httpRequest = getUriRequest(url, method, params, proxy);

		try {
			CloseableHttpResponse response = client.execute(httpRequest);
			try {
				HttpEntity entity = response.getEntity();
				if (response.getStatusLine().getStatusCode() != 200) {
					EntityUtils.consume(entity);
					return null;
				}
				return EntityUtils.toString(entity);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void finalize() throws Throwable {
		client.close();
		super.finalize();
	}
}
