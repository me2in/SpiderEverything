package com.wong.spider.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.Xsoup;

import com.wong.spider.util.HttpClientUtils;

public class FindProxy {

	// private static CloseableHttpClient client = HttpClients.createDefault();
	private static RequestConfig rc = RequestConfig.custom()
			.setCircularRedirectsAllowed(false)
			.setMaxRedirects(0)
			.setSocketTimeout(20000)
			// .setProxy(proxy)
			.setConnectionRequestTimeout(20000).setConnectTimeout(20000)
			.setRedirectsEnabled(true).setCookieSpec(CookieSpecs.STANDARD)
			.build();

	@Test
	public void findProxy() throws IOException {
		for (int i = 1; i <= 10; i++) {

			String html = HttpClientUtils.doGet(String.format("http://www.kuaidaili.com/proxylist/%d/", i));
			System.out.println("html:"+html);
			if(html==null || html.isEmpty()){
				continue;
			}
//			System.out.println(html);
			// String html
			// =readFromFile("C:/Users/weien/Desktop/com_proxylist_1_.html");
			Document doc = Jsoup.parse(html);
			Element etbody = doc.select("tbody").get(0);
			for (Element etr : etbody.children()) {
				String ip = etr.child(0).html();
				String port = etr.child(1).html();
				if (checkProxyAvailable(ip, Integer.valueOf(port))) {
					System.out.println(String.format("[ip:port]  = [%s:%s]",
							ip, port));
				}
			}

		}
	}

	public boolean checkProxyAvailable(String host, Integer port) {

		try {
			HttpHost proxy = new HttpHost(host, port);
			CloseableHttpClient localClient = HttpClients.custom()
					.setRoutePlanner(new DefaultProxyRoutePlanner(proxy))
					.build();
			HttpGet get = new HttpGet("http://pianyuan.net");
			get.setConfig(rc);

			CloseableHttpResponse response = localClient.execute(get);
			EntityUtils.consume(response.getEntity());
			response.close();

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String readFromFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists() && file.canRead()) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			StringBuffer result = new StringBuffer();
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		}
		return "";
	}

}
