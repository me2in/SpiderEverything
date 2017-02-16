package com.wong.spider.proxy;

public interface ProxyPool {
	
	/**
	 * 添加一个新的代理
	 * @param ip
	 * @param port
	 */
	void pushNewProxy(String ip,Integer port);
	
	Proxy getProxy();
	/**
	 * 代理池是否启用
	 * @return
	 */
	boolean isEnable();
	/**
	 * 将代理请求的结果反馈给代理池，由代理池对代理进行处理
	 * @param proxy
	 * @param statusCode
	 */
	void returnProxyToPool(Proxy proxy,int statusCode);
	
	

}
