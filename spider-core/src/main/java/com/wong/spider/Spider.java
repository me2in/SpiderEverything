package com.wong.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wong.spider.common.Page;
import com.wong.spider.common.Request;
import com.wong.spider.common.Website;
import com.wong.spider.downloader.Downloader;
import com.wong.spider.downloader.HttpClientDownloader;
import com.wong.spider.processor.FileProcessor;
import com.wong.spider.processor.ImageProcessor;
import com.wong.spider.processor.PageProcessor;
import com.wong.spider.proxy.SimpleProxyPool;
import com.wong.spider.scheduler.MemoryQueueScheduler;
import com.wong.spider.scheduler.Scheduler;
import com.wong.spider.task.Task;
import com.wong.spider.thread.CountableThreadPool;

public class Spider implements Task{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected Scheduler scheduler = new MemoryQueueScheduler(); 
	protected Downloader downloader = new HttpClientDownloader();
	protected CountableThreadPool threadPool;
	protected ExecutorService executorService;
	protected ReentrantLock newUrlLock = new ReentrantLock();
	protected Condition newUrlCondition = newUrlLock.newCondition();
	protected List<PageProcessor> processors = new ArrayList<PageProcessor>();
	protected boolean exitWhenComplete = true;
	protected int threadNum = 1;
	/**
	 * @param threadNum the threadNum to set
	 */
	public Spider setThreadNum(int threadNum) {
		this.threadNum = threadNum;
		return this;
	}

	protected int emptySleepTime = 30000;
	protected Website site;
	
	

	protected void init(){
		
		if(processors.isEmpty()){
			logger.warn("PageProcessor not setting yet!");
			throw new RuntimeException("PageProcessor not setting yet!");
		}
		
		//图片和文件下载的processor默认导入
		processors.add(new ImageProcessor());
		processors.add(new FileProcessor());
		
		if(scheduler.isComplete()){
			logger.warn("please set start url first!");
			throw new RuntimeException("start url not setting yet !");
		}
		if(site == null){
			logger.warn("website not setting yet !");
			throw new RuntimeException("website not setting yet !");
		}
		
		if(site.isUseProxy() && site.getProxyPool() == null){
			site.setProxyPool(new SimpleProxyPool());
		}
		
		if (threadPool == null || threadPool.isShutdown()) {
			if (executorService != null && !executorService.isShutdown()) {
				threadPool = new CountableThreadPool(threadNum, executorService);
			} else {
				threadPool = new CountableThreadPool(threadNum);
			}
		}
	}
	
	public void run(){
		init();
		while(!Thread.currentThread().isInterrupted()){
			Request request = scheduler.poll();
			if(request == null){
				if(threadPool.getThreadAlive() == 0 && exitWhenComplete){
					break;
				}
				waitNewUrl();
			}else{
				final Request requestFinal = request;
				threadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						try{
							processRequest(requestFinal);
						}catch(Exception e){
							logger.error("process request {} error",requestFinal,e);
							//将请求重新加入请求队列
							if(requestFinal.getRetryCount()<site.getCycleRetry()){
								requestFinal.addRetryCount();
								requestFinal.addParam(Request.IS_NEED_RETRY, true);
								addRequest(requestFinal);
							}
						}finally{
							signalNewUrl();
						}
						
					}

					
				});
				
			}
		}
	}
	
	protected void processRequest(Request requestFinal) {
		Page page = downloader.download(requestFinal, this);
		if(page == null){
			 sleep(site.getSleepTime());
			 return;
		}
		if(page.isNeedCycleRequest()){
			extractAndAddRequests(page);
			sleep(site.getSleepTime());
			return;
		}
		for(PageProcessor processor : processors){
			if(processor.canProcess(page)){
				processor.process(page);
				if(!page.isSkip()){
					processor.serializer(page);
				}
			}
		}
		extractAndAddRequests(page);
	}
	
	protected void extractAndAddRequests(Page page) {
		for (Request request : page.getTargetRequest()) {
			addRequest(request);
		}
	}
	
	protected void addRequest(Request request){
		if (request != null && request.getUrl() != null) {
			scheduler.push(request);
		}
	}
	
	protected void sleep(long sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void waitNewUrl() {
        newUrlLock.lock();
        try {
            //double check
            if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
                return;
            }
            newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("waitNewUrl - interrupted, error {}", e);
        } finally {
            newUrlLock.unlock();
        }
    }
	
	protected void signalNewUrl() {
        try {
            newUrlLock.lock();
            newUrlCondition.signalAll();
        } finally {
            newUrlLock.unlock();
        }
    }
	
	
	public Spider(){
		
	}
	
	/**
	 * @param site the site to set
	 */
	public Spider setSite(Website site) {
		this.site = site;
		return this;
	}

	@Override
	public Website getSite() {
		return site;
	}
	
	public static Spider instance(){
		return new Spider();
	}
	
	public Spider addUrl(String url){
		scheduler.push(Request.RequestHtml(url));
		signalNewUrl();
		return this;
	}
	
	public Spider addProcessor(PageProcessor processor){
		this.processors.add(processor);
		return this;
	}
	
	public Spider addProcessor(List<PageProcessor> processor){
		this.processors.addAll(processor);
		return this;
	}

}
