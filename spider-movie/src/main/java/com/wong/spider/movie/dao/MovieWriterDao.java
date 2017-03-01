package com.wong.spider.movie.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wong.spider.movie.model.MovieWriter;

public interface MovieWriterDao extends JpaRepository<MovieWriter, Integer> {

}
