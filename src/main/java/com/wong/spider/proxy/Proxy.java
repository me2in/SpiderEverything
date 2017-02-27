package com.wong.spider.proxy;

public class Proxy {
	
	private String ip;
	private String username;
	private String password;
	private Integer port;
	private boolean isAvailable = false;
	private int retryCount = 0;
	
	public static final int ERROR_403 = 403;
	public static final int ERROR_404 = 404;
	public static final int ERROR_BANNED = 10000;// banned by website
	public static final int ERROR_Proxy = 10001;// the proxy itself failed
	public static final int SUCCESS = 200;
	private final static int MAX_RETRY = 5;
	
	
	public Proxy(String ip,int port){
		this.ip = ip;
		this.port= port;
		this.isAvailable = true;
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
		this.retryCount ++;
		if(retryCount> MAX_RETRY){
			setAvailable(false);
		}
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
}
