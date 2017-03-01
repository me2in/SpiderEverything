package com.wong.spider.scheduler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import com.wong.spider.common.Request;
import com.wong.spider.util.MyFileUtils;

public class MemoryQueueScheduler implements Scheduler,Serializable {

	private static final long serialVersionUID = -2275843985292621852L;

	protected final String LAST_SCHEDULER = "data/config/last.scheduler";

	protected BlockingQueue<Request> queue = new PriorityBlockingQueue<Request>();

	// TODO 加个delayQueue 实现对网页再请求，进行更新？

	// 内存url去重
	protected Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

	protected Timer timer = new Timer();

	protected TimerTask task;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MemoryQueueScheduler() {

		if (MyFileUtils.isExists(LAST_SCHEDULER)) {
			try {
				ObjectInputStream oin = new ObjectInputStream(
						new FileInputStream(LAST_SCHEDULER));
				List<Collection> lastScheduler = (List<Collection>) oin
						.readObject();
				this.queue = (PriorityBlockingQueue<Request>) lastScheduler.get(0);
				this.urls = (Set<String>) lastScheduler.get(1);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			MyFileUtils.makeParentdir(LAST_SCHEDULER);
		}
		task = new TimerTask() {
			@Override
			public void run() {
				try {
					ObjectOutputStream oout = new ObjectOutputStream(
							new FileOutputStream(LAST_SCHEDULER));
					oout.writeObject(prepare4Store());
					oout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		timer.schedule(task, 0,  10 * 60 * 1000);
	}
	
	@SuppressWarnings("rawtypes")
	protected List<Collection> prepare4Store(){
		List<Collection> store= new ArrayList<Collection>();
		store.add(getQueue());
		store.add(getUrls());
		return store;
	}

	protected boolean isDuplicate(Request request) {
		return !getUrls().add(request.getUrl());
	}

	protected boolean isRetryRequest(Request request) {
		return request.getParams().get(Request.IS_NEED_RETRY) == null ? false
				: (boolean) request.getParams().get(Request.IS_NEED_RETRY);
	}

	@Override
	public synchronized void push(Request request) {
		if (!isDuplicate(request) || isRetryRequest(request)) {
			getQueue().add(request);
		}
	}

	@Override
	public synchronized void push(List<Request> requestList) {
		for (Request request : requestList) {
			if (isDuplicate(request) || isRetryRequest(request)) {
				getQueue().add(request);
			}
		}
	}

	@Override
	public synchronized Request poll() {
		return getQueue().poll();
	}

	@Override
	public synchronized boolean isComplete() {
		return getQueue().isEmpty();
	}
	
	protected synchronized BlockingQueue<Request> getQueue(){
		return this.queue;
	}
	
	protected synchronized Set<String> getUrls(){
		return this.urls;
	}

}
