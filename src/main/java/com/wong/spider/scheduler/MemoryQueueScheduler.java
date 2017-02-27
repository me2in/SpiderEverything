package com.wong.spider.scheduler;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import com.wong.spider.Request;

public class MemoryQueueScheduler implements Scheduler {
	
	private BlockingQueue<Request> queue = new PriorityBlockingQueue<Request>();
	
	//TODO 加个delayQueue  实现对网页再请求，进行更新？
	
	//内存url去重
	private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	
	protected boolean isDuplicate(Request request){
		return !urls.add(request.getUrl());
	}
	
	protected boolean isRetryRequest(Request request){
		return request.getParams().get(Request.IS_NEED_RETRY)==null?false:(boolean)request.getParams().get(Request.IS_NEED_RETRY);
	}

	@Override
	public synchronized void push(Request request) {
		if(!isDuplicate(request) || isRetryRequest(request)){
			queue.add(request);
		}
	}

	@Override
	public synchronized void push(List<Request> requestList) {
		for(Request request : requestList){
			if(isDuplicate(request) || isRetryRequest(request)){
				queue.add(request);
			}
		}
	}

	@Override
	public synchronized Request poll() {
		return queue.poll();
	}

	@Override
	public synchronized boolean isComplete() {
		return queue.isEmpty();
	}

}
