package com.wong.spider.movie.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="movie_writer")
public class MovieWriter {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer movieWriterId;
	private Integer movieId;
	private String writer;
	/**
	 * @return the movieWriterId
	 */
	public Integer getMovieWriterId() {
		return movieWriterId;
	}
	/**
	 * @param movieWriterId the movieWriterId to set
	 */
	public void setMovieWriterId(Integer movieWriterId) {
		this.movieWriterId = movieWriterId;
	}
	/**
	 * @return the movieId
	 */
	public Integer getMovieId() {
		return movieId;
	}
	/**
	 * @param movieId the movieId to set
	 */
	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}
	/**
	 * @return the writer
	 */
	public String getWriter() {
		return writer;
	}
	/**
	 * @param writer the writer to set
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MovieWriter [movieWriterId=" + movieWriterId + ", movieId="
				+ movieId + ", writer=" + writer + "]";
	}
	
	

}
