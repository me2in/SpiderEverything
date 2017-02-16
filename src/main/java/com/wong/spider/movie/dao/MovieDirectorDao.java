package com.wong.spider.movie.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wong.spider.movie.model.MovieDirector;

public interface MovieDirectorDao extends JpaRepository<MovieDirector, Integer> {

}
