package com.wong.spider.common;

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
	
	/**
	 * 设置网站请求的UA
	 * @param ua
	 * @return
	 */
	public Website setUserAgent(String ua){
		headers.put("User-Agent", ua);
		return this;
	}
	/**
	 * 添加请求头
	 * @param key
	 * @param value
	 * @return
	 */
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
	/**
	 * 添加cookie
	 * @param domain 有效域
	 * @param key 键
	 * @param value 值
	 * @return
	 */
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
	 * @param 网站域名
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
	 * @param 代理池
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
	 * @param 网站默认编码
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
	 * @param 请求之间的休眠时间
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
	 * @param 请求超时
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
	 * @param 是否允许重定向
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
	 * @param 请求重试次数
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
	 * @param 是否使用Gzip压缩，暂未生效
	 */
	public Website setGzip(boolean isGzip) {
		this.isGzip = isGzip;
		return this;
	}
	
	public Proxy getProxy(){
		return proxyPool.getProxy();
	}
	/**
	 * 将使用完的代理返回到代理池中
	 * @param proxy
	 * @param statusCode
	 */
	public void returnProxy2Pool(Proxy proxy,int statusCode){
		proxyPool.returnProxyToPool(proxy, statusCode);
	}
	/**
	 * 代理池是否可用
	 * @return
	 */
	public boolean isEnableProxy(){
		return isUseProxy && proxyPool!=null && proxyPool.isEnable();
	}

	/**
	 * @return the isUseProxy
	 */
	public boolean isUseProxy() {
		return isUseProxy;
	}

	/**
	 * @param 设置是否使用代理池
	 */
	public Website setUseProxy(boolean isUseProxy) {
		this.isUseProxy = isUseProxy;
		return this;
	}
}
