package com.wong.spider.movie.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="movie_type")
public class MovieType {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer movieTypeId;
	private Integer movieId;
	private String type;
	/**
	 * @return the movieTypeId
	 */
	public Integer getMovieTypeId() {
		return movieTypeId;
	}
	/**
	 * @param movieTypeId the movieTypeId to set
	 */
	public void setMovieTypeId(Integer movieTypeId) {
		this.movieTypeId = movieTypeId;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MovieType [movieTypeId=" + movieTypeId + ", movieId=" + movieId
				+ ", type=" + type + "]";
	}

}
