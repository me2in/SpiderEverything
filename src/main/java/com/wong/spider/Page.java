package com.wong.spider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wong.spider.movie.ResultItems;

public class Page {

	private Request request;
	private String rawText;
	private byte[] data;// 文件对象的byte数组
	private ResultItems resultItems = new ResultItems();
	private int statusCode;
	private List<Request> targetRequest = new ArrayList<Request>();
	private boolean needCycleRequest = false;
	private long contentLength;// 当请求对象为一个文件时，需要设置此字段，以确定请求的文件是完整的
	private boolean isSkip = false;

	/**
	 * @return the rawText
	 */
	public String getRawText() {
		return rawText;
	}

	/**
	 * @param rawText
	 *            the rawText to set
	 */
	public Page setRawText(String rawText) {
		this.rawText = rawText;
		return this;
	}

	/**
	 * @return the resultItems
	 */
	public ResultItems getResultItems() {
		return resultItems;
	}

	/**
	 * @param resultItems
	 *            the resultItems to set
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
	 * @param statusCode
	 *            the statusCode to set
	 */
	public Page setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	public Page putField(String key, Object value) {
		resultItems.put(key, value);
		return this;
	}

	/**
	 * @return the needCycleRequest
	 */
	public boolean isNeedCycleRequest() {
		return needCycleRequest;
	}

	/**
	 * @param needCycleRequest
	 *            the needCycleRequest to set
	 */
	public Page setNeedCycleRequest(boolean needCycleRequest) {
		this.needCycleRequest = needCycleRequest;
		return this;
	}

	/**
	 * @return the contentLength
	 */
	public long getContentLength() {
		return contentLength;
	}

	/**
	 * @param contentLength
	 *            the contentLength to set
	 */
	public Page setContentLength(long contentLength) {
		this.contentLength = contentLength;
		return this;
	}

	/**
	 * @return the isSkip
	 */
	public boolean isSkip() {
		return isSkip;
	}

	/**
	 * @param isSkip
	 *            the isSkip to set
	 */
	public Page setSkip(boolean isSkip) {
		this.isSkip = isSkip;
		return this;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public Page setData(byte[] data) {
		this.data = data;
		return this;
	}

	/**
	 * @return the request
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setRequest(Request request) {
		this.request = request;
	}

	/**
	 * @return the targetRequest
	 */
	public List<Request> getTargetRequest() {
		return targetRequest;
	}

	/**
	 * @param targetRequest
	 *            the targetRequest to set
	 */
	public Page addTargetRequest(List<Request> targetRequest) {
		this.targetRequest.addAll(targetRequest);
		return this;
	}

	public Page addTargetRequest(Request targetRequest) {
		this.targetRequest.add(targetRequest);
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Page [request=" + request + ", rawText=" + rawText + ", data="
				+ Arrays.toString(data) + ", resultItems=" + resultItems
				+ ", statusCode=" + statusCode + ", targetRequest="
				+ targetRequest + ", needCycleRequest=" + needCycleRequest
				+ ", contentLength=" + contentLength + ", isSkip=" + isSkip
				+ "]";
	}

}
