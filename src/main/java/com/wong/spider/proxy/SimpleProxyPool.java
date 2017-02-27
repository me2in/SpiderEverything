package com.wong.spider.proxy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.wong.spider.util.MyFileUtils;

/**
 * 非常简单的线程池实现
 * @author weien
 *
 */
public class SimpleProxyPool implements ProxyPool {
	
	private BlockingQueue<Proxy> queue = new LinkedBlockingQueue<Proxy>();
	private Set<String> proxys = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	private final static String config_file_path = "data/proxy.txt";
	
	public SimpleProxyPool(){
		
		if(!MyFileUtils.isExists(config_file_path)){
			MyFileUtils.makedir(config_file_path);
		}else{
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
		}
	}

	@Override
	public synchronized void pushNewProxy(String ip,Integer port) {
		if(proxys.add(ip)){
			Proxy p = new Proxy(ip,port);
			queue.add(p);
		}
	}

	@Override
	public synchronized Proxy getProxy() {
		return queue.poll();
	}

	@Override
	public synchronized boolean isEnable() {
		return !queue.isEmpty();
	}

	@Override
	public synchronized void returnProxyToPool(Proxy proxy, int statusCode) {
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
