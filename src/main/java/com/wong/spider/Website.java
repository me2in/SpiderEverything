package com.wong.spider;

import java.util.Map;
import java.util.HashMap;

import com.wong.spider.proxy.Proxy;
import com.wong.spider.proxy.ProxyPool;

public class Website {
	
	private Map<String,String> headers = new HashMap<String,String>();
	private Map<String,Map<String,String>> cookies = new HashMap<String,Map<String,String>>();
	private ProxyPool proxyPool;
	private String defaultCharset;
	private int sleepTime = 5000;//没有链接时的休眠时间 毫秒 
	private int timeOut = 3000;//超时时间 毫秒
	private boolean isRedirectsEnabled = true;
	private int cycleRetry = 3;
	private boolean isGzip = true;
	private String domain;
	private boolean isUseProxy = true;
	
	public Website setUserAgent(String ua){
		headers.put("User-Agent", ua);
		return this;
	}
	
	public Website addHeader(String key,String value){
		headers.put(key, value);
		return this;
	}
	
	public Map<String,String> getHeaders(){
		return this.headers;
	}
	
	public Map<String,Map<String,String>> getCookies(){
		return this.cookies;
	}
	
	public Website addCookie(String domain,String key,String value){
		if(cookies.containsKey(domain)){
			Map<String,String> domainCookie = new HashMap<String,String>();
			cookies.put(domain, domainCookie);
		}
		cookies.get(domain).put(key, value);
		return this;
	}
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public Website setDomain(String domain) {
		this.domain = domain;
		return this;
	}
	/**
	 * @return the proxyPool
	 */
	public ProxyPool getProxyPool() {
		return proxyPool;
	}
	/**
	 * @param proxyPool the proxyPool to set
	 */
	public Website setProxyPool(ProxyPool proxyPool) {
		this.proxyPool = proxyPool;
		return this;
	}
	/**
	 * @return the defaultCharset
	 */
	public String getDefaultCharset() {
		return defaultCharset;
	}
	/**
	 * @param defaultCharset the defaultCharset to set
	 */
	public Website setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
		return this;
	}
	/**
	 * @return the sleepTime
	 */
	public int getSleepTime() {
		return sleepTime;
	}
	/**
	 * @param sleepTime the sleepTime to set
	 */
	public Website setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
		return this;
	}
	/**
	 * @return the timeOut
	 */
	public int getTimeOut() {
		return timeOut;
	}
	/**
	 * @param timeOut the timeOut to set
	 */
	public Website setTimeOut(int timeOut) {
		this.timeOut = timeOut;
		return this;
	}
	/**
	 * @return the isRedirectsEnabled
	 */
	public boolean isRedirectsEnabled() {
		return isRedirectsEnabled;
	}
	/**
	 * @param isRedirectsEnabled the isRedirectsEnabled to set
	 */
	public Website setRedirectsEnabled(boolean isRedirectsEnabled) {
		this.isRedirectsEnabled = isRedirectsEnabled;
		return this;
	}
	/**
	 * @return the cycleRetry
	 */
	public int getCycleRetry() {
		return cycleRetry;
	}
	/**
	 * @param cycleRetry the cycleRetry to set
	 */
	public Website setCycleRetry(int cycleRetry) {
		this.cycleRetry = cycleRetry;
		return this;
	}
	/**
	 * @return the isGzip
	 */
	public boolean isGzip() {
		return isGzip;
	}
	/**
	 * @param isGzip the isGzip to set
	 */
	public Website setGzip(boolean isGzip) {
		this.isGzip = isGzip;
		return this;
	}
	
	public Proxy getProxy(){
		return proxyPool.getProxy();
	}
	
	public void returnProxy2Pool(Proxy proxy,int statusCode){
		proxyPool.returnProxyToPool(proxy, statusCode);
	}
	
	public boolean isEnableProxy(){
		return proxyPool!=null && proxyPool.isEnable();
	}

	/**
	 * @return the isUseProxy
	 */
	public boolean isUseProxy() {
		return isUseProxy;
	}

	/**
	 * @param isUseProxy the isUseProxy to set
	 */
	public Website setUseProxy(boolean isUseProxy) {
		this.isUseProxy = isUseProxy;
		return this;
	}
}
