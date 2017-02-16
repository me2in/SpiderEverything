package com.wong.spider.movie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class TestPrase {
	
	@Test
	public void parse() throws IOException{
		Document doc = Jsoup.parse(readFromFile("C:/Users/weien/Desktop/movie_info.html"));
		Elements eh1 = doc.select(".col-sm-12.col-md-12.col-lg-12.text-left.nopd>h1");
		System.out.println(eh1.text());
	}
	
	public static String readFromFile(String filePath) throws IOException{
		File file = new File(filePath);
		if(file.exists() && file.canRead()){
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			StringBuffer result = new StringBuffer();
			while((line=br.readLine())!=null){
				result.append(line);
			}
			return result.toString();
		}
		return "";
	}

}
