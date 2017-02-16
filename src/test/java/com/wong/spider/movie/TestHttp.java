package com.wong.spider.movie;


import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.wong.spider.util.HttpClientUtils;

public class TestHttp {
	/**
	 * 测试httpclient在无效链接下的情况
	 */
	@Test
	public void testBadUrl(){
		byte[] result = HttpClientUtils.doGetFile("http://pianyuan.net/dlbt/RWtrekR5MDUwfGUwMDEyMWRiOWVmMmIwMjQ4YWFkY2U1YXwxNDg0NTMzODMzfDM4MTYxNDVl");
		System.out.println(new String(result));
//		MyFileUtils.writeFile("d:/1.torrent",HttpClientUtils.doGetFile("http://pianyuan.net/dlbt/RW1reEZlcDUwfDkxN2I0ZDlhOGMwYzgzMzM5MmU2MWI5MHwxNDg0NTI5ODQzfGJmNDdiZGNj"));

	}
	
	@Test
	public void testProxy(){
		Map<String,String> params = new HashMap<String,String>();
		params.put("p", "104");
		String result = HttpClientUtils.doGet("http://pianyuan.net/mv",params);
		System.out.println(new String(result));
	}
	
	

}
