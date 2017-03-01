package com.wong.spider.movie.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wong.spider.movie.model.Movie;

public interface MovieDao extends JpaRepository<Movie, Integer> {
	
	List<Movie> findByName (String fileName);

}
