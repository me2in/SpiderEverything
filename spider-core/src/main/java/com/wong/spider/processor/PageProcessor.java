package com.wong.spider.processor;

import com.wong.spider.common.Page;



public interface PageProcessor {
	
	boolean canProcess(Page page);
	
	void process(Page page);
	
	void serializer(Page page);
	
}
