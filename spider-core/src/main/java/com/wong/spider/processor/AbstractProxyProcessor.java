package com.wong.spider.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpHost;

import com.wong.spider.common.Page;
import com.wong.spider.common.Request;
import com.wong.spider.processor.PageProcessor;
import com.wong.spider.util.HttpClientUtils;
import com.wong.spider.util.MyFileUtils;
import com.wong.spider.util.MyStringUtils;
/**
 * 
 * 对代理采集的processor做了抽象，抽出了绝大多数的重复代码，正常情况下，只需要写解析网页部分的代码就可以了
 * @author wangjuntao
 * @date 2017-2-28
 * @since
 */
public abstract class AbstractProxyProcessor implements PageProcessor {

	protected ExecutorService es = Executors.newFixedThreadPool(30);// 开启30个线程校验

	@Override
	public boolean canProcess(Page page){
		return canProcessor(page.getRequest().getUrl());
	}
	
	protected abstract boolean canProcessor(String url);

	/**
	 * 获取页面的链接
	 * @param html
	 * @return
	 */
	protected abstract List<String> getLinks(String html);
	/**
	 * 从页面抓取代理
	 * @param html
	 * @return
	 */
	protected abstract List<String[]> getProxys(String html);
	/**
	 * 有效的代理保存位置
	 * @return
	 */
	protected  String getPath2SaveProxy(){
		return "data/proxy.txt";
	}
	/**
	 * 用来测试代理是否有效的链接
	 * @return
	 */
	protected abstract String getUrl4Test();
	/**
	 * 对代理额外的校验，如是proxy是否是具有其他待测试功能。
	 * 特别是有些代理并不会真正去请求目标网站，所以即使返回statusCode=200也有可能是不可用的代理
	 * @param proxy
	 * @param result
	 * @return
	 */
	protected abstract boolean extraVerify(HttpHost proxy, String result);

	@Override
	public void process(Page page) {
		String html = page.getRawText();
		List<String> links = getLinks(html);
		if (links != null && !links.isEmpty()) {
			page.addTargetRequest(wrapperUrl2Request(links));
		}
		page.putField("proxy_list", getProxys(html));
	}

	@Override
	public void serializer(Page page) {
		List<String[]> proxyList = page.getResultItems().get("proxy_list");

		Map<String[], Future<Boolean>> allProxyResult = new HashMap<String[], Future<Boolean>>();

		if (proxyList != null && !proxyList.isEmpty()) {

			StringBuilder sb = new StringBuilder();

			for (String[] proxy : proxyList) {
				final String ip = proxy[0];
				final Integer port = Integer.valueOf(proxy[1]);
				allProxyResult.put(proxy, es.submit(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return isProxyAvailable(ip, port)
								&& isProxyAvailable(ip, port);// double check
					}
				}));
			}

			for (String[] proxy : proxyList) {
				try {
					if (allProxyResult.get(proxy).get()) {
						sb.append(String
								.format("%s:%s\r\n", proxy[0], proxy[1]));
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}

			if (sb.length() > 0) {
				MyFileUtils.writeStringAppend(getPath2SaveProxy(),
						sb.toString());
			}

		}
	}

	protected List<Request> wrapperUrl2Request(List<String> urls) {
		List<Request> allRequest = new ArrayList<Request>();
		for (String url : urls) {
			Request request = Request.RequestHtml(url).setUseProxy(false);
			allRequest.add(request);
		}
		return allRequest;
	}
	
	protected boolean isProxyAvailable(String ip, int port) {
		HttpHost proxy = new HttpHost(ip, port);
		String result = HttpClientUtils.doGet(getUrl4Test(), proxy);
		return !MyStringUtils.isEmpty(result) && extraVerify(proxy, result);
	}

}
