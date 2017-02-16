package com.wong.spider;

import java.util.ArrayList;
import java.util.List;


import com.wong.spider.movie.ResultItems;

public class Page {
	
	private String url;
	private String rawText;
	private ResultItems resultItems = new ResultItems();
	private int statusCode;
	private List<String> targetRequests = new ArrayList<String>();
	private boolean needCycleRequest = false;
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the rawText
	 */
	public String getRawText() {
		return rawText;
	}
	/**
	 * @param rawText the rawText to set
	 */
	public void setRawText(String rawText) {
		this.rawText = rawText;
	}
	/**
	 * @return the resultItems
	 */
	public ResultItems getResultItems() {
		return resultItems;
	}
	/**
	 * @param resultItems the resultItems to set
	 */
	public void setResultItems(ResultItems resultItems) {
		this.resultItems = resultItems;
	}
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the targetRequests
	 */
	public List<String> getTargetRequests() {
		return targetRequests;
	}
	/**
	 * @param targetRequests the targetRequests to set
	 */
	public void setTargetRequests(List<String> targetRequests) {
		this.targetRequests = targetRequests;
	}
	
	public void putField(String key,Object value){
		resultItems.put(key, value);
	}
	
	public void addTargetUrl(String targetUrl){
		targetRequests.add(targetUrl);
	}
	
	public void addTargetUrl(List<String> urls){
		targetRequests.addAll(urls);
	}
	/**
	 * @return the needCycleRequest
	 */
	public boolean isNeedCycleRequest() {
		return needCycleRequest;
	}
	/**
	 * @param needCycleRequest the needCycleRequest to set
	 */
	public void setNeedCycleRequest(boolean needCycleRequest) {
		this.needCycleRequest = needCycleRequest;
	}
}
