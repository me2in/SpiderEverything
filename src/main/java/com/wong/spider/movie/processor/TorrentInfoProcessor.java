package com.wong.spider.movie.processor;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import us.codecraft.xsoup.Xsoup;

import com.wong.spider.Page;
import com.wong.spider.Request;
import com.wong.spider.annotation.Processor;
import com.wong.spider.movie.PianyuanUtils;
import com.wong.spider.movie.model.Movie;
import com.wong.spider.movie.model.Torrent;
import com.wong.spider.movie.service.IMovieService;
import com.wong.spider.processor.PageProcessor;
import com.wong.spider.util.MyFileUtils;
import com.wong.spider.util.MyStringUtils;
@Component
@Processor(domain="http://pianyuan.net")
public class TorrentInfoProcessor implements PageProcessor {
	
	@Resource
	private IMovieService movieService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean canProcess(Page page) {
		String pattren = "http://pianyuan.net/r_(\\w)*.html";
		return page.getRequest().getUrl().matches(pattren);
	}

	@Override
	public void process(Page page) {
		
		Torrent torrent = new Torrent();
		
		Document doc = Jsoup.parse(page.getRawText());
		String movieName = MyStringUtils.converunctuationEng2Chinese(Xsoup.select(doc,"/html/body/div[2]/div/div/div[2]/h2/a/text()").get().trim());
		String fileName = Xsoup.select(doc,"/html/body/div[2]/div/div/div[2]/h1/text()").get();
		String type = Xsoup.select(doc,"//*[@id='main-container']/div/div[2]/div/ul/li[1]/text()").get();
		String fileSize = Xsoup.select(doc, "//*[@id='main-container']/div/div[2]/div/ul/li[2]/text()").get();
		String createTime = Xsoup.select(doc,"//*[@id='main-container']/div/div[2]/div/ul/li[3]/text()").get();
		String magnet = Xsoup.select(doc,"//*[@id='main-container']/div/div[2]/div/div[1]/a[2]/@href").get();
		String fileUrl = Xsoup.select(doc,"//*[@id='main-container']/div/div[2]/div/div[1]/a[1]/@href").get();
		String zimu = Xsoup.select(doc,"//*[@id='main-container']/div/div[2]/div/div[1]/a[3]/@href").get();
		
		torrent.setCreateTime(createTime);
		torrent.setFileSize(fileSize);
		torrent.setMagnet(magnet);
		torrent.setZimu(zimu);
		torrent.setUrl("http://pianyuan.net"+fileUrl);
		torrent.setType(type);
		torrent.setFileName(fileName);
		
		page.putField("movieName", movieName);
		page.putField("torrent", torrent);
		
		logger.info("torrent :{}",torrent.getFileName());
		
		String fileSavePath = PianyuanUtils.FILE_SAVE_BASEDIR+File.separator+movieName+File.separator+torrent.getType()+File.separator+torrent.getFileName()+".torrent";
		page.addTargetRequest(Request.RequestFile(torrent.getUrl()).setFileSavePath(fileSavePath).setPriority(3));
	}

	@Override
	public void serializer(Page page) {
		String movieName = page.getResultItems().get("movieName");
		Torrent torrent = page.getResultItems().get("torrent");
		
		List<Movie> dbLM = movieService.findMovieByName(movieName);
		if(dbLM!=null && !dbLM.isEmpty()){
			torrent.setMovieId(dbLM.get(0).getMovieId());
		}
		List<Torrent> dbLT = movieService.findTorrentByName(torrent.getFileName());
		if(dbLT == null || dbLT.isEmpty()){
			movieService.saveTorrent(torrent);
		}
		
		String dir = PianyuanUtils.FILE_SAVE_BASEDIR+File.separator+movieName;
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("    [种子名]:[%s]\r\n", torrent.getFileName()));
		sb.append(String.format("    [类型]:[%s]\r\n", torrent.getType()));
		sb.append(String.format("    [文件大小]:[%s]\r\n", torrent.getFileSize()));
		sb.append(String.format("    [创建时间]:[%s]\r\n", torrent.getCreateTime()));
		sb.append(String.format("    [magnet]:[%s]\r\n", torrent.getMagnet()));
		sb.append(String.format("    [字幕]:[%s]\r\n\r\n", torrent.getZimu()));
		MyFileUtils.writeStringAppend(dir+File.separator+"movie_info.txt", sb.toString());
//		MyFileUtils.writeFile(dir+File.separator+torrent.getType()+File.separator+torrent.getFileName()+".torrent", HttpClientUtils.doGetFile(torrent.getUrl()));
	}

}
