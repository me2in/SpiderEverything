package com.wong.spider.movie.service;

import java.util.List;

import com.wong.spider.movie.model.Movie;
import com.wong.spider.movie.model.Torrent;

public interface IMovieService {
	
	void saveMovie(Movie movie);
	
	void saveMovie(Movie movie,List<String> types,List<String> starrings,List<String> director,List<String> writters,List<String> areas);
	
	void saveTorrent(Torrent torrent);
	
	List<Torrent> findTorrentByName(String name);
	
	List<Movie> findMovieByName(String name);

}
