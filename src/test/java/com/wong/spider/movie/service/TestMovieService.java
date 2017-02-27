package com.wong.spider.movie.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.wong.spider.movie.BaseTest;
import com.wong.spider.movie.dao.MovieDao;
import com.wong.spider.movie.model.Movie;

public class TestMovieService extends BaseTest{
	
	@Resource
	private MovieDao dao;
	
	@Resource
	private IMovieService movieService;
	
	@Test
	public void testMovieInsert(){
		Movie movie = new Movie();
		movie.setDouban("fsfsddfsdf");
		movie.setName("fsdfasdfasf");
		dao.save(movie);
		System.out.println(movie);
	}
	
	@Test
	public void testMoviePage(){
		Page<Movie> pageData = movieService.findAllMoviePage(1, 40);
		
		System.out.println(String.format("totalPage:%d total:%d  size:%d  index:%d", pageData.getTotalPages(),pageData.getTotalElements(),pageData.getSize(),pageData.getNumber()));
		System.out.println(pageData);
		System.out.println(pageData.nextPageable());
	}

}
