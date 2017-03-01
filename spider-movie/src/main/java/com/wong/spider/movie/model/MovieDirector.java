package com.wong.spider.movie.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="movie_director")
public class MovieDirector {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer MovieDirectorId;
	private Integer movieId;
	private String director;
	/**
	 * @return the movieDirectorId
	 */
	public Integer getMovieDirectorId() {
		return MovieDirectorId;
	}
	/**
	 * @param movieDirectorId the movieDirectorId to set
	 */
	public void setMovieDirectorId(Integer movieDirectorId) {
		MovieDirectorId = movieDirectorId;
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
	 * @return the director
	 */
	public String getDirector() {
		return director;
	}
	/**
	 * @param director the director to set
	 */
	public void setDirector(String director) {
		this.director = director;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MovieDirector [MovieDirectorId=" + MovieDirectorId
				+ ", movieId=" + movieId + ", director=" + director + "]";
	}

}
