package com.wong.spider.movie.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wong.spider.movie.model.MovieStarring;

public interface MovieStarringDao extends JpaRepository< MovieStarring, Integer> {

}
