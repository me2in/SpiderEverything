package com.wong.spider.scheduler;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class QueueScheduler {

	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	//内存url去重
	private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	
	protected boolean isDuplicate(String url){
		return !urls.add(url);
	}
	
	public synchronized void pushURLWhichIsError(String url){
		queue.add(url);
	}
	
	public synchronized void push(String url){
		if(!isDuplicate(url)){
			queue.add(url);
		}
	}
	
	public synchronized void push(List<String> urlList){
		for(String url : urlList){
			if(!isDuplicate(url)){
				queue.add(url);
			}
		}
	}
	
	public synchronized String poll(){
		return queue.poll();
	}
	
}
