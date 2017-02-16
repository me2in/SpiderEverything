package com.wong.spider.movie;

import java.io.IOException;

import org.junit.Test;

import com.wong.spider.Page;
import com.wong.spider.movie.processor.MovieInfoProcessor;
import com.wong.spider.movie.processor.MovieListProcessor;
import com.wong.spider.movie.processor.TorrentInfoProcessor;
import com.wong.spider.util.MyFileUtils;

public class TestProcessor {
	
	@Test
	public void testMovieInfoProcessor() throws IOException{
		String rawText = MyFileUtils.readFromFile("C:/Users/weien/Desktop/spider/info2.html");
		Page page = new Page();
		page.setRawText(rawText);
		MovieInfoProcessor mp = new MovieInfoProcessor();
//		mp.process(page);
	}
	
	@Test
	public void TestTorrentInfoProcessor() throws IOException{
		String rawText = MyFileUtils.readFromFile("C:/Users/weien/Desktop/spider/torrent_info.html");
		Page page = new Page();
		page.setRawText(rawText);
		TorrentInfoProcessor tp = new TorrentInfoProcessor();
//		tp.process(page);
	}
	
	@Test
	public void TestMovieListProcessor() throws IOException{
		String rawText = MyFileUtils.readFromFile("C:/Users/weien/Desktop/spider/movie_list.html");
		Page page = new Page();
		page.setRawText(rawText);
		MovieListProcessor mp = new MovieListProcessor();
//		mp.process(page);
	}

}
