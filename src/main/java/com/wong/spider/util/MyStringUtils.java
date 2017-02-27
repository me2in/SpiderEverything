package com.wong.spider.util;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStringUtils {
	
	private static final Pattern patternForCharset = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)");
	
	public static String converunctuationEng2Chinese(String str){
		str = str.replace(":", "：");
		str = str.replace("<", "《");
		str = str.replace(">", "》");
		str = str.replace("?", "？");
		str = str.replace("\"", "“");
		str = str.replace("\\\\", "");
		return str;
	}
	
	public static String tidyText(String badStr){
		if(badStr == null || badStr.equals("")){
			return badStr;
		}
		badStr = badStr.trim();
		badStr = badStr.replaceAll(" <>", ",").replace("\n", "");
		if(badStr.lastIndexOf(",")>0){
			badStr = badStr.substring(0, badStr.lastIndexOf(","));
		}
		return badStr;
	}

	public static boolean isEmpty(String domain) {
		return domain == null || "".equals(domain.trim());
	}
	
	public static String getCharset(String contentType){
		Matcher matcher = patternForCharset.matcher(contentType);
        if (matcher.find()) {
            String charset = matcher.group(1);
            if (Charset.isSupported(charset)) {
                return charset;
            }
        }
        return null;
	}
}
