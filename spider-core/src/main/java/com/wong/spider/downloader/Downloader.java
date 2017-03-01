package com.wong.spider.downloader;

import com.wong.spider.common.Page;
import com.wong.spider.common.Request;
import com.wong.spider.task.Task;

public interface Downloader {
	/**
	 * 下载对应请求的结果，封装为page
	 * @param request
	 * @param task
	 * @return
	 */
	Page download(Request request,Task task);
	/**
	 * 设置请求的线程个数
	 * @param threadNum
	 */
	void setThread(int threadNum);

}
