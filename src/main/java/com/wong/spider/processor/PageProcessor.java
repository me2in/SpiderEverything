package com.wong.spider.processor;


import com.wong.spider.Page;
import com.wong.spider.downloader.Downloader;

public interface PageProcessor {
	
	boolean canProcess(Page page);
	
	void process(Page page,Downloader downloader);
	
	void serializer(Page page,Downloader downloader);
	
}
