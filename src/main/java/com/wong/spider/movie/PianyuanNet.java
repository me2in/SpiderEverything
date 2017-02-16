package com.wong.spider.movie;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.wong.spider.movie.config.ApplicationConfig;
import com.wong.spider.movie.model.Movie;
import com.wong.spider.movie.model.Torrent;
import com.wong.spider.movie.service.IMovieService;
import com.wong.spider.util.HttpClientUtils;
import com.wong.spider.util.MyDateUtils;
import com.wong.spider.util.MyFileUtils;
import com.wong.spider.util.MyStringUtils;

/**
 * 抓取片源网 pianyuan.net上的种子文件 
 * 
 * @author weien
 *
 */
public class PianyuanNet {
	
	//TODO  1.防重复  --Bloomfilter+Redis 但是电影数据有可能存在更新，
	// 目前可以肯定种子的下载可以爬完之后不用再访问，可以放到过滤器中(r_*),电影目录可能有更新，假定新电影只更新在头10页，
	//TODO 2.索引数据持久化 -- 已做完
	//TODO 3.多线程 
	//TODO 4.对采集到的电影分数进行更新，链接到豆瓣重新更新分数
	//TODO 5.对已有数据做出分析
	//TODO 6.如何能够持续更新？
	//TODO 7.每运行30分钟以上就卡住 --没有设置好其他两个超时  socket 和 request
	
	private final Logger log = LoggerFactory.getLogger(PianyuanNet.class);
	
	private static final String URL_INDEX = "http://pianyuan.net";
	
	private static final String URL_MOVIE_ALL = "http://pianyuan.net/mv";
	
	private static final String FILEPATH_INDEX = "d:/movie_torrent";
	
	private static ExecutorService step2ThreadPool = Executors.newFixedThreadPool(20);
	
//	private static ExecutorService step3ThreadPool = Executors.newFixedThreadPool(10);
	
	@Resource
	private IMovieService movieService;
	
	public void step1(String url){
		log.info("开始请求并解析电影大全页. [url:{}]",url);
		String html = HttpClientUtils.doGet(url);
		Document doc = Jsoup.parse(html);
		Elements mvElemnts = doc.select(".col-sm-3.col-md-3.col-xs-4.col-lg-2.nopl"); //col-sm-3 col-md-3 col-xs-4 col-lg-2 nopl
		for(Element e: mvElemnts){
			log.info("-----------------------已解析到电影----------------------");
			Element urlE = e.select("a[class='thumbnail']").get(0);
			log.info("***********电影简介**************");
			log.info("[详情链接:{}]" , urlE.attr("href"));
			log.info("[电影名:{}]" , urlE.attr("title"));
			log.info("[海报链接:{}]",urlE.child(0).attr("data-original"));
			log.info("***********电影简介***************");
			
			
			final String href = URL_INDEX + urlE.attr("href");
			String mvName = MyStringUtils.converunctuationEng2Chinese(urlE.attr("title"));
			String picUrl = urlE.child(0).attr("data-original").replace("//", "http://");
			
			try{
				final String baseDir = FILEPATH_INDEX+File.separator+mvName;
				MyFileUtils.makedir(baseDir);
				String picName = picUrl.substring(picUrl.lastIndexOf("/"));
				MyFileUtils.writeFile(baseDir+File.separator+picName, picUrl);
//				step2ThreadPool.execute(new Runnable() {
//					@Override
//					public void run() {
						step2(href,baseDir);
//					}
//				});
				
			}catch(Exception ex){
				log.error("建立文件时出错",ex);
			}
			log.info("-----------------------完成电影解析-----------------------");
		}
		log.info("当前页解析完毕！. [url:{}]",url);
		
	}
	
	public void step2(String url,String baseDir){
		
		log.info("开始解析电影详情页。。【url:{}】",url);
		
		Movie movie = new Movie();
		
		movie.setDetailUrl(url);//保存url用于后期更新信息
		
		String html = HttpClientUtils.doGet(url);
		Document doc = Jsoup.parse(html);
		StringBuilder sb = new StringBuilder();
		
		Element ename = doc.select(".col-sm-12.col-md-12.col-lg-12.text-left.nopd>h1").get(0);
		if(ename.children().size()>0){
			ename.children().remove();
		}
		String movieName = ename.html();
		String year = movieName.substring(movieName.lastIndexOf("(")+1, movieName.lastIndexOf(")"));
		
		Element eposter = doc.select(".litpic").get(0);
		String poster = eposter.child(0).child(0).attr("src");
		
		Element detail = doc.select(".detail").get(0);
		sb.append("\r\n\r\n[电影信息]\r\n");
		sb.append(String.format("    [名字]:[%s]\r\n", movieName));
		sb.append(String.format("    [年份]:[%s]\r\n", year));
		
		Element escore = doc.select(".score").get(0);
		String score = escore.child(1).html().replaceAll("<b>|</b>", "");
		sb.append(String.format("    [分数]:[%s]\r\n", score));
		
		log.info("***********电影详情开始**************");
		
		log.info("[电影名字:{}]",movieName);
		log.info("[年份:{}]",year);
		log.info("[海报url:{}]",poster);
		log.info("[分数:{}]",score);
		
		
		movie.setName(movieName);
		movie.setYear(year);
		movie.setScore(score);
		movie.setPoster(poster);
		
		for(int id=0;id<detail.children().size();id++){
			String key = detail.child(id).child(0).html().replace(":", "");
			String text = detail.child(id).child(1).html();
			
			List<String> someList = new ArrayList<String>();
			
			if(key.equals("豆瓣")){
				text = detail.child(id).child(1).getElementsByTag("a").get(0).attr("href");
			}else if(detail.child(id).child(1).getElementsByTag("a").size()>0){
				Elements desc = detail.child(id).child(1).getElementsByTag("a");
				for(int ii=0 ;ii<desc.size();ii++){
					someList.add(desc.get(ii).html());
				}
				text = someList.toString();
			}
			log.info(String.format("[%s]:[%s]", key,text));  
			
			
			sb.append(String.format("    [%s]:[%s]\r\n", key,text));
			
			
			
			switch(key){
				case "豆瓣" :movie.setDouban(text);break;
				case "imdb":movie.setImdb(text);
				case "类型":{
					String[] typeStrList = text.split(",");
//					List<Integer> type = new ArrayList<Integer>();
//					for(String t : typeStrList){
//						type.add(MovieTypeEnum.getCodeByName(t));
//					}
					movie.setType(Arrays.asList(typeStrList));
				}break;
				case "又名" :movie.setAnotherName(text);break;
				case "地区":{
					String[] areaStrs = text.split(",");
					movie.setArea(Arrays.asList(areaStrs));
					}break;
				case "导演":movie.setDirector(someList);break;
				case "编剧":movie.setWriter(someList);break;
				case "主演":movie.setStarring(someList);break;
			}
		}
		log.info("***********电影详情结束**************");
		try{
			//到这里电影信息已经全部获取到了,开始保存
			List<Movie> dbm = movieService.findMovieByName(movie.getName());
			if (dbm == null || dbm.size() == 0) {
				log.info("开始保存电影[{}]信息到数据库",movie.getName());
				movieService.saveMovie(movie, movie.getType(), movie.getStarring(), movie.getDirector(), movie.getWriter(),movie.getArea());    
				log.info("电影[{}]信息保存成功",movie.getName());
			}else{
				log.info("数据库中已存在{}的数据，不再重复保存",movie.getName());
			}
		}catch(Exception ex){
			log.error("保存电影信息到数据库失败",ex);
		}
		
		sb.append("\r\n\r\n\r\n");
		sb.append("[种子列表]\r\n");
		Elements tableEs = doc.getElementsByTag("tbody");
		log.info("开始解析电影[{}]种子列表。。。",movie.getName());
		for(int i=0; i<tableEs.size();i++){
			Elements childs = tableEs.get(i).children();
			Element etype = childs.get(0).getElementsByTag("span").get(0);
			
			String type = etype.html();
			
			log.info("*****以下是类型为{}的种子*******",type);
			
			sb.append(String.format("\r\n\r\n  类型：[%s]\r\n", type));
			
			for(int j=1;j<childs.size();j++){
				
				try {
					Thread.sleep(5000); //程序暂停5秒钟，防止请求过快
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				log.info("--------种子信息开始----------");
				Element ee = childs.get(j);
				
				log.info("[文件名:{}]",ee.child(0).child(0).html());
				log.info("[链接:{}]:",ee.child(0).child(0).attr("href"));
				log.info("[文件大小:{}]",ee.child(1).html());
				log.info("[创建时间:{}]",ee.child(2).html());
				
				log.info("--------种子信息结束----------");
				
				
				String fileName = ee.child(0).child(0).html();
				String href = URL_INDEX +"/"+ee.child(0).child(0).attr("href")+"?"+MyDateUtils.getNowTime();
				String fileSize = ee.child(1).html();
				String createTime = ee.child(2).html();
				
				sb.append(String.format("\r\n    [种子名]:[%s]\r\n", fileName));
				sb.append(String.format("    [文件大小]:[%s]\r\n", fileSize));
				sb.append(String.format("    [创建时间]:[%s]\r\n", createTime));
				
				String torrentBaseDir = baseDir + File.separator+ type;
				MyFileUtils.makedir(torrentBaseDir);
				try{
					Torrent torrent = step3(href,torrentBaseDir);
					torrent.setMovieId(movie.getMovieId());
					torrent.setFileSize(fileSize);
					torrent.setCreateTime(createTime);
					torrent.setType(type);
					try{
						List<Torrent> dbt = movieService.findTorrentByName(torrent.getFileName());
						if (dbt == null || dbt.size() == 0) {
							log.info("开始保存种子{}信息到数据库。。",torrent.getFileName());
							movieService.saveTorrent(torrent);
							log.info("保存种子{}信息成功！",torrent.getFileName());
						}else{
							log.info("种子{}信息在数据库中已存在,放弃保存",torrent.getFileName());
						}
					}catch(Exception ex){
						log.error("保存种子信息失败", ex);
					}
				}catch(Exception ex){
					log.error("下载种子文件出错",ex);
				}
				
				
			}
		}
		log.info("解析完毕并开始写入电影[{}]信息到文件。。【url:{}】",movie.getName(),url);
		MyFileUtils.writeString(baseDir+File.separator+"movie_info.txt", sb.toString());
		log.info("保存电影[{}]信息到文件成功！",movie.getName());
	}
	
	public Torrent step3(String url,String baseDir){
		log.info("开始解析网页并下载种子文件[url:{}]",url);
		
		String html = HttpClientUtils.doGet(url);
		StringBuilder sb = new StringBuilder();
		
		Torrent torrent = new Torrent();
		
		Document doc = Jsoup.parse(html);
		Element etitle = doc.select(".col-sm-10.col-md-10.col-lg-10.text-left").get(0);
		String title = etitle.child(0).html();
		sb.append(String.format("\r\n[种子%s的信息]\r\n", title));
		sb.append(String.format("\r\n  [种子名]:[%s]", title));
		
		
		Element tdown = doc.select(".tdown").get(0);
		log.info("*******种子信息开始*********");
		log.info("[种子文件名:{}]",title);
		log.info("[种子文件链接:{}]:",tdown.child(1).attr("href"));
		log.info("[磁力链接:{}]",tdown.child(2).attr("href"));
		log.info("[字幕链接:{}]",tdown.child(3).attr("href"));
		log.info("*******种子信息结束*********");
		String href = URL_INDEX + tdown.child(1).attr("href");
		String magnet = tdown.child(2).attr("href");
		String zimuUrl = tdown.child(3).attr("href");
		sb.append(String.format("  [magnet]:[%s]", magnet));
		sb.append(String.format("  [字幕地址]:[%s]", zimuUrl));
		
		MyFileUtils.writeStringAppend(baseDir+File.separator+"info.txt", sb.toString());
		MyFileUtils.writeFile(baseDir+File.separator+title+".torrent", href);
		
		
		torrent.setMagnet(magnet);
		torrent.setZimu(zimuUrl);
		torrent.setFileName(title);
		torrent.setUrl(href);
		
		log.info("已成功保存种子[name:{}]",torrent.getFileName());
		
		return torrent;
	}
	
	public static void main(String[] args) {
//		PianyuanNet net = new PianyuanNet();
//		net.step1();
//		net.step2("http://pianyuan.net/m_DtmzWLMc0.html");
//		net.step3("http://pianyuan.net/r_EkZkFe050.html",null);
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		PianyuanNet net = context.getBean(PianyuanNet.class);
		for(int i=236;i<=582;i++){
			try{
				net.step1(URL_MOVIE_ALL+"?p="+i);
//				Thread.sleep(30000);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
//		net.step1(URL_MOVIE_ALL);
		
	}

}
