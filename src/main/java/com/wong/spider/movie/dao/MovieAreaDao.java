package com.wong.spider.movie.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wong.spider.movie.model.MovieArea;

public interface MovieAreaDao extends JpaRepository<MovieArea, Integer> {

}
