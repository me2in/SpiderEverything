package com.wong.spider.movie.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wong.spider.movie.dao.MovieAreaDao;
import com.wong.spider.movie.dao.MovieDao;
import com.wong.spider.movie.dao.MovieDirectorDao;
import com.wong.spider.movie.dao.MovieStarringDao;
import com.wong.spider.movie.dao.MovieTypeDao;
import com.wong.spider.movie.dao.MovieWriterDao;
import com.wong.spider.movie.dao.TorrentDao;
import com.wong.spider.movie.model.Movie;
import com.wong.spider.movie.model.MovieArea;
import com.wong.spider.movie.model.MovieDirector;
import com.wong.spider.movie.model.MovieStarring;
import com.wong.spider.movie.model.MovieType;
import com.wong.spider.movie.model.MovieWriter;
import com.wong.spider.movie.model.Torrent;
import com.wong.spider.movie.service.IMovieService;
@Service("movieService")
public class MovieServiceImpl implements IMovieService {
	
	@Resource
	private MovieDao movieDao;
	@Resource
	private MovieTypeDao movieTypeDao;
	@Resource
	private MovieStarringDao movieStarringDao;
	@Resource
	private MovieDirectorDao movieDirectorDao;
	@Resource
	private TorrentDao torrentDao;
	@Resource
	private MovieWriterDao movieWriterDao;
	@Resource
	private MovieAreaDao movieAreaDao;
	
	@Override
	public void saveMovie(Movie movie, List<String> types,
			List<String> starrings, List<String> directors, List<String> writters,List<String> areas) {
		
		movieDao.save(movie);
		
		
		if(types!=null && types.size()>0){
			
			List<MovieType> movieTypes = new ArrayList<MovieType>();
			for(String t : types){
				MovieType mt = new MovieType();
				mt.setType(t);
				mt.setMovieId(movie.getMovieId());
				movieTypes.add(mt);
			}
			movieTypeDao.save(movieTypes);
		}
		
		if(directors!=null && directors.size()>0){
			
			List<MovieDirector> movieDirectors = new ArrayList<MovieDirector>();
			for(String d:directors){
				MovieDirector md = new MovieDirector();
				md.setDirector(d);
				md.setMovieId(movie.getMovieId());
				movieDirectors.add(md);
			}
			movieDirectorDao.save(movieDirectors);
		}
		
		if(writters!=null && writters.size()>0){
			
			List<MovieWriter> movieWriters = new ArrayList<MovieWriter>();
			for(String w:writters){
				MovieWriter mr = new MovieWriter();
				mr.setWriter(w);
				mr.setMovieId(movie.getMovieId());
				movieWriters.add(mr);
			}
			movieWriterDao.save(movieWriters);
		}
		
		if(starrings!=null && starrings.size()>0){
			
			List<MovieStarring> movieStarrings = new ArrayList<MovieStarring>();
			for(String st:starrings){
				MovieStarring ms = new MovieStarring();
				ms.setStarring(st);
				ms.setMovieId(movie.getMovieId());
				movieStarrings.add(ms);
			}
			movieStarringDao.save(movieStarrings);
		}
		
		if(areas!=null && areas.size()>0){
			List<MovieArea> movieAreas = new ArrayList<MovieArea>();
			for(String a: areas){
				MovieArea ma = new MovieArea();
				ma.setArea(a);
				ma.setMovieId(movie.getMovieId());
				movieAreas.add(ma);
			}
			movieAreaDao.save(movieAreas);
		}
		
	}
	@Override
	public void saveTorrent(Torrent torrent) {
		torrentDao.saveAndFlush(torrent);
	}
	@Override
	public List<Torrent> findTorrentByName(String name) {
		return torrentDao.findByFileName(name);
	}
	@Override
	public List<Movie> findMovieByName(String name) {
		return movieDao.findByName(name);
	}
	@Override
	public void saveMovie(Movie movie) {
		saveMovie(movie,movie.getType(), movie.getStarring(), movie.getDirector(), movie.getWriter(), movie.getArea());
	}
}
