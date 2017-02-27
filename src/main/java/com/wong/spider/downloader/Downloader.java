package com.wong.spider.downloader;

import com.wong.spider.Page;
import com.wong.spider.Request;
import com.wong.spider.Task;

public interface Downloader {
	
	Page download(Request request,Task task);
	
	void setThread(int threadNum);

}
