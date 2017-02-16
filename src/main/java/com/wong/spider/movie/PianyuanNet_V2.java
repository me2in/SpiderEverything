package com.wong.spider.movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;


import com.wong.spider.Page;
import com.wong.spider.downloader.Downloader;
import com.wong.spider.downloader.SimpleHttpDownloader;
import com.wong.spider.movie.config.ApplicationConfig;
import com.wong.spider.movie.processor.DailiProcessor;
import com.wong.spider.movie.processor.MovieInfoProcessor;
import com.wong.spider.movie.processor.MovieListProcessor;
import com.wong.spider.movie.processor.TorrentInfoProcessor;
import com.wong.spider.processor.PageProcessor;
import com.wong.spider.scheduler.QueueScheduler;
import com.wong.spider.thread.CountableThreadPool;


/**
 * 抓取片源网 pianyuan.net上的种子文件 
 * 
 * V2版更新：
 * 1.将网页爬虫和页面处理功能分开解耦
 * 2.元素定位使用xpath语法，更方便（使用xsoup）
 * TODO 3.加入代理 
 * TODO 4.实现保存当前请求状态，下次开始时能够热启动 
 * 
 * @author weien
 *
 */
@Component
public class PianyuanNet_V2 {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private QueueScheduler scheduler = new QueueScheduler(); 
	private List<PageProcessor> processors = new ArrayList<PageProcessor>();
	private CountableThreadPool threadPool;
	private ExecutorService executorService;
	private ReentrantLock newUrlLock = new ReentrantLock();
	private Condition newUrlCondition = newUrlLock.newCondition();
	private Downloader downloader = new SimpleHttpDownloader();
	protected boolean exitWhenComplete = true;
	private int threadNum = 5;
	private int emptySleepTime = 30000;
	
	private void init(){
//		getProcessors().add(new MovieListProcessor());
//		getProcessors().add(new MovieInfoProcessor());
//		getProcessors().add(new TorrentInfoProcessor());
		scheduler.push("http://pianyuan.net/mv");
//		scheduler.push("http://www.youdaili.net/Daili/http/31769.html");
		if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(getThreadNum(), executorService);
            } else {
                threadPool = new CountableThreadPool(getThreadNum());
            }
        }
	}
	
	//真正的执行
	public void run(){
		init();
		while(!Thread.currentThread().isInterrupted()){
			String url = scheduler.poll();
			if(url == null){
				if(threadPool.getThreadAlive() == 0 && exitWhenComplete){
					break;
				}
				waitNewUrl();
			}else{
				final String urlFinal = url;
				threadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						log.info("request url:{}",urlFinal);
						try{
							Page page = downloader.download(urlFinal);
							if(page.getStatusCode()==200){
								processRequest(page);
							}else{
								scheduler.pushURLWhichIsError(urlFinal);
							}
						}catch(Exception e){
							log.error("请求异常url:{}",urlFinal,e);
							scheduler.pushURLWhichIsError(urlFinal);
						}finally{
							signalNewUrl();
						}
					}
				});
			}
		}
		
	}
	
	private void processRequest(Page page){
		for (PageProcessor processor : getProcessors()) {
			if(processor.canProcess(page)){
				processor.process(page,downloader);
				processor.serializer(page,downloader);
				if(page.getTargetRequests()!=null && !page.getTargetRequests().isEmpty()){
					scheduler.push(page.getTargetRequests());
				}
			}
		}
	}
	
	private void waitNewUrl() {
        newUrlLock.lock();
        try {
            //double check
            if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
                return;
            }
            newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.warn("waitNewUrl - interrupted, error {}", e);
        } finally {
            newUrlLock.unlock();
        }
    }
	
	private void signalNewUrl() {
        try {
            newUrlLock.lock();
            newUrlCondition.signalAll();
        } finally {
            newUrlLock.unlock();
        }
    }

	/**
	 * @return the threadNum
	 */
	public int getThreadNum() {
		return threadNum;
	}

	/**
	 * @param threadNum the threadNum to set
	 */
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	

	/**
	 * @return the processors
	 */
	public List<PageProcessor> getProcessors() {
		return processors;
	}

	/**
	 * @param processors the processors to set
	 */
	public void setProcessors(List<PageProcessor> processors) {
		this.processors = processors;
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		PianyuanNet_V2 v2 = context.getBean(PianyuanNet_V2.class);
		v2.getProcessors().add(context.getBean(MovieInfoProcessor.class));
		v2.getProcessors().add(context.getBean(MovieListProcessor.class));
		v2.getProcessors().add(context.getBean(TorrentInfoProcessor.class));
//		v2.getProcessors().add(context.getBean(DailiProcessor.class));
		v2.run();
	}
	
	
	
	
	

}
