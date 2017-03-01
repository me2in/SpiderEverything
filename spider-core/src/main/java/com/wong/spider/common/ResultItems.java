package com.wong.spider.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResultItems {
	
	private Map<String,Object> fields = new LinkedHashMap<String,Object>();
	
	private boolean skip;
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key){
		Object o = fields.get(key);
		if(o ==  null){
			return null;
		}
		return (T) o;
	}
	
	public Map<String,Object> getAll(){
		return fields;
	}
	
	public <T> ResultItems put(String key,T value){
		fields.put(key, value);
		return this;
	}

	/**
	 * @return the skip
	 */
	public boolean isSkip() {
		return skip;
	}

	/**
	 * @param skip the skip to set
	 */
	public void setSkip(boolean skip) {
		this.skip = skip;
	}
}
