package com.wong.spider.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wong.spider.common.Page;
import com.wong.spider.common.Request;
import com.wong.spider.annotation.Processor;
import com.wong.spider.processor.PageProcessor;
import com.wong.spider.util.MyFileUtils;
import com.wong.spider.util.MyStringUtils;

@Processor
public class FileProcessor implements PageProcessor {

	protected Logger logger = LoggerFactory.getLogger("FileProcessor");

	@Override
	public boolean canProcess(Page page) {
		return page.getRequest().getType() == Request.FILE;
	}

	@Override
	public void process(Page page) {
		if (page.getData() == null || page.getData().length ==0 ) {
			page.setSkip(true);// 如果获取到的文件大小 小于 原大小，则跳过，且将此请求重新发送
			page.addTargetRequest(page.getRequest());
			logger.warn("file not complete! (recevice {}) skip",
					page.getContentLength());
		}
	}

	@Override
	public void serializer(Page page) {
		String filepath = page.getRequest().getFileSavePath();
		if (!MyStringUtils.isEmpty(filepath)) {
			byte[] data = page.getData();
			MyFileUtils.writeFile(filepath, data);
		} else {
			logger.info(
					" file request {} not config path where file to save, can not save file!",
					page.getRequest().getUrl());
		}

	}

}
