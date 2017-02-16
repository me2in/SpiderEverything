package com.wong.spider.proxy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 非常简单的线程池实现
 * @author weien
 *
 */
public class SimpleProxyPool implements ProxyPool {
	
	private BlockingQueue<Proxy> queue = new LinkedBlockingQueue<Proxy>();
	private Set<String> proxys = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	private boolean isEnable = false;
	private final static String config_file_path = "F:/proxy.txt";
	
	public SimpleProxyPool(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(config_file_path));
			String line;
			while((line=br.readLine())!=null){
				if(!line.trim().isEmpty()){
					if(proxys.add(line)){//添加成功则说明不重复
						String ip = line.substring(0, line.indexOf(":"));
						String port = line.substring(line.indexOf(":")+1);
						Proxy p = new Proxy(ip,Integer.valueOf(port));
						queue.add(p);
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.isEnable = !queue.isEmpty();
	}

	@Override
	public void pushNewProxy(String ip,Integer port) {
		isEnable = true;
		Proxy p = new Proxy(ip,port);
		queue.add(p);
	}

	@Override
	public Proxy getProxy() {
		return queue.poll();
	}

	@Override
	public boolean isEnable() {
		return isEnable;
	}

	@Override
	public void returnProxyToPool(Proxy proxy, int statusCode) {
		switch(statusCode){
			case Proxy.SUCCESS:
			case Proxy.ERROR_404:
				queue.add(proxy);
				break;
			default:
				proxy.addRetryCount();
				if(proxy.isAvailable()){
					queue.add(proxy);
				}
				break;
		}
	}
}
