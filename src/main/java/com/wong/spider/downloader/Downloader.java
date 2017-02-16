package com.wong.spider.downloader;

import com.wong.spider.Page;

public interface Downloader {
	
	 Page download(String url);
	 
	 boolean dowmloadFile(String url,String filepath,boolean isRetry);

}
