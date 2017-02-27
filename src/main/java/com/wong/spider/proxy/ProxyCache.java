package com.wong.spider.proxy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用生产者消费者模型（主要是为了练习多线程，其实有更简单的设计），爬虫自动抓取代理放到缓存中，代理池自动从这里拿到新的代理
 * 
 * 
 * @author wangjuntao
 * @date 2017-2-27
 * @since
 */
public class ProxyCache {

	private static final int MAX_SIZE = 100;

	private static final Logger logger = LoggerFactory.getLogger("ProxyCache");

	private static LinkedList<Proxy> cache = new LinkedList<Proxy>();

	private final static Lock lock = new ReentrantLock();

	private final static Condition full = lock.newCondition();

	private final static Condition empty = lock.newCondition();

	public static void produce(List<Proxy> proxys) {
		lock.lock();
		try {
			if (proxys != null && !proxys.isEmpty()) {
				while (cache.size() + proxys.size() > MAX_SIZE) {// 大于最大容量
					try {
						full.await();
						logger.info(String.format("目前容量%d,待加入%d,已超过最大容量%d,暂时不能加入", cache.size(),
								proxys.size(), MAX_SIZE));
					} catch (InterruptedException e) {
						logger.warn("代理工厂中断失败");
					}
				}
				cache.addAll(proxys);
				empty.signalAll();
				logger.debug(String.format("成功加入%d个,目前共%d", proxys.size(),
						cache.size()));
			}
		} finally {
			lock.unlock();//不将解锁操作放在finally代码块中，则会出现取出0个的现象，原因不解！
		}
	}

	public static List<Proxy> consume() {
		lock.lock();
		try {
			while (cache.isEmpty()) {//在多线程下，这里必须用while 而不能用if，为什么？
				try {
					empty.await();
					logger.info(String.format("目前空，等待生产"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.info("目前还有:{}", cache.size());
			List<Proxy> proxys = new ArrayList<Proxy>();
			int size = cache.size();
			for (int i = 0; i < size; i++) {
				proxys.add(cache.remove());
			}
			cache.clear();
			full.signalAll();
			logger.info(String.format("成功取出%d", proxys.size()));
			return proxys;

		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		// final ProxyCache proxyCache = new ProxyCache();
		List<Thread> producers = new ArrayList<Thread>();
		List<Thread> consumers = new ArrayList<Thread>();
		for (int i = 0; i < 10; i++) {
			producers.add(new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						List<Proxy> proxys = new ArrayList<Proxy>();
						for (int j = 0; j <= (new Random().nextInt(10)); j++) {
							Proxy proxy = new Proxy("8.8.8.8", j);
							proxys.add(proxy);
						}
						ProxyCache.produce(proxys);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}));

			consumers.add(new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						ProxyCache.consume();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}));
		}

		for (int k = 0; k < 10; k++) {
			producers.get(k).start();
			consumers.get(k).start();
		}
	}
}
