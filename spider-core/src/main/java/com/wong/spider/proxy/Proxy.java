package com.wong.spider.proxy;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Proxy implements Delayed{
	
	private String ip;
	private String username;
	private String password;
	private Integer port;
	private boolean isAvailable = false;
	private int retryCount = 0;
	private long nanoTimeOut = 0L;
	
	/**
	 * @return the nanoTimeOut
	 */
	public long getNanoTimeOut() {
		return nanoTimeOut;
	}
	/**
	 * @param nanoTimeOut the nanoTimeOut to set
	 */
	public void setNanoTimeOut(long nanoTimeOut) {
		this.nanoTimeOut = nanoTimeOut;
	}
	public static final int ERROR_403 = 403;
	public static final int ERROR_404 = 404;
	public static final int ERROR_BANNED = 10000;// banned by website
	public static final int ERROR_Proxy = 10001;// the proxy itself failed
	public static final int SUCCESS = 200;
	private final static int MAX_RETRY = 7;
	
	public Proxy(String ip,int port){
		this.ip = ip;
		this.port= port;
		this.isAvailable = true;
		this.nanoTimeOut = System.nanoTime() + TimeUnit.NANOSECONDS.convert(0, TimeUnit.MILLISECONDS);
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
	/**
	 * @return the isAvailable
	 */
	public boolean isAvailable() {
		return isAvailable;
	}
	/**
	 * @param isAvailable the isAvailable to set
	 */
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	/**
	 * @return the retryCount
	 */
	public int getRetryCount() {
		return retryCount;
	}
	/**
	 * @param retryCount the retryCount to set
	 */
	public synchronized void addRetryCount() {
		this.retryCount += 1;
		if(retryCount> MAX_RETRY){
			setAvailable(false);
		}
	}
	
	public synchronized void resetRetryCount() {
		this.retryCount = 0;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public int compareTo(Delayed o) {
		Proxy proxy = (Proxy) o;
		return this.nanoTimeOut>proxy.nanoTimeOut?1:(this.nanoTimeOut<proxy.nanoTimeOut?-1:0);
	}
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(nanoTimeOut - System.nanoTime(), TimeUnit.NANOSECONDS);
	}
}
