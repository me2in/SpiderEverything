package com.wong.spider.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Request implements Comparable<Request>,Serializable{
	
	private static final long serialVersionUID = 6287877104691225748L;
	public static final int HTML = 1;
	public static final int IMAGE = 2;
	public static final int FILE = 3;
	public static final int BIG_FILE = 4;
	public static final String IS_NEED_RETRY = "isNeedRetry"; //是否需要重复请求，用于标记失败的请求
	public static final String STATUS_CODE = "statusCode";//上一次请求的状态码
	
	
	
	private String url;
	private int type = Request.HTML;
	private String fileSavePath;// 非html时可设置文件的保存路径
	private String method; //请求类型，只支持get/post
	private int retryCount = 0;
	private Map<String,Object> params = new HashMap<String,Object>();
	private int priority =  0;//优先级
	private boolean isUseProxy = true;
	public static Request RequestImage(String url){
		Request request = new Request();
		return request.setUrl(url).setType(Request.IMAGE);
	}
	
	public static Request RequestHtml(String url){
		Request request = new Request();
		return request.setUrl(url).setType(Request.HTML);
	}
	
	public static Request RequestFile(String url){
		Request request = new Request();
		return request.setUrl(url).setType(Request.FILE);
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public Request setUrl(String url) {
		this.url = url;
		return this;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public Request setType(int type) {
		this.type = type;
		return this;
	}
	/**
	 * @return the fileSavePath
	 */
	public String getFileSavePath() {
		return fileSavePath;
	}
	/**
	 * @param fileSavePath the fileSavePath to set
	 */
	public Request setFileSavePath(String fileSavePath) {
		this.fileSavePath = fileSavePath;
		return this;
	}
	
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public Request setMethod(String method) {
		this.method = method;
		return this;
	}
	/**
	 * @return the params
	 */
	public Map<String,Object> getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public Request setParams(Map<String,Object> params) {
		this.params = params;
		return this;
	}
	
	public Request addParam(String key,Object value){
		this.params.put(key, value);
		return this;
	}
	/**
	 * @return the retryCount
	 */
	public int getRetryCount() {
		return retryCount;
	}
	/**
	 * @param retryCount the retryCount to set
	 */
	public void addRetryCount() {
		this.retryCount += 1;
	}
	public Object getParam(String key) {
		return this.params.get(key);
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public Request setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	@Override
	public int compareTo(Request o) {
		return o.priority - this.priority;
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
	public Request setUseProxy(boolean isUseProxy) {
		this.isUseProxy = isUseProxy;
		return this;
	}

}
