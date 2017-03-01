package com.wong.spider.movie.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wong.spider.movie.model.MovieType;

public interface MovieTypeDao extends JpaRepository<MovieType, Integer> {

}
