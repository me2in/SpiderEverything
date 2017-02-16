package com.wong.spider.movie.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wong.spider.movie.model.Torrent;

public interface TorrentDao extends JpaRepository<Torrent, Integer> {
	
	List<Torrent> findByFileName(String name);

}
