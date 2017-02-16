package com.wong.spider.movie.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="movie_area")
public class MovieArea {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer movieAreaId;
	private String  area;
	private Integer movieId;
	/**
	 * @return the movieAreaId
	 */
	public Integer getMovieAreaId() {
		return movieAreaId;
	}
	/**
	 * @param movieAreaId the movieAreaId to set
	 */
	public void setMovieAreaId(Integer movieAreaId) {
		this.movieAreaId = movieAreaId;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MovieArea [movieAreaId=" + movieAreaId + ", area=" + area
				+ ", movieId=" + movieId + "]";
	}
	
}
