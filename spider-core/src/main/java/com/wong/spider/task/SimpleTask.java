package com.wong.spider.task;

import com.wong.spider.common.Website;


/**
 * 
 *
 * @author wangjuntao
 * @date 2017-2-28
 * @since
 */
public class SimpleTask implements Task {
	
	protected Website site;

	@Override
	public Website getSite() {
		return site;
	}
	
	public SimpleTask setSite(Website site){
		this.site = site;
		return this;
	}

}
