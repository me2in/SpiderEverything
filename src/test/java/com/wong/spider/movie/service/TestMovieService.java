package com.wong.spider.movie.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.wong.spider.movie.BaseTest;
import com.wong.spider.movie.dao.MovieDao;
import com.wong.spider.movie.model.Movie;

public class TestMovieService extends BaseTest{
	
	@Resource
	private MovieDao dao;
	
	@Test
	public void testMovieInsert(){
		Movie movie = new Movie();
		movie.setDouban("fsfsddfsdf");
		movie.setName("fsdfasdfasf");
		dao.save(movie);
		System.out.println(movie);
	}

}
