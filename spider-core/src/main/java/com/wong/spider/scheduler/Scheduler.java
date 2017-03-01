package com.wong.spider.scheduler;

import java.util.List;

import com.wong.spider.common.Request;


public interface Scheduler {
	
	void push(Request request);
	
	void push(List<Request> requestList);
	
	Request poll();
	
	boolean isComplete();

}
