package com.wong.spider.movie.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="movie_starring")
public class MovieStarring {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer MovieStarringId;
	private Integer movieId;
	private String starring;
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
	 * @return the starring
	 */
	public String getStarring() {
		return starring;
	}
	/**
	 * @param starring the starring to set
	 */
	public void setStarring(String starring) {
		this.starring = starring;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MovieStarring [MovieStarringId=" + MovieStarringId
				+ ", movieId=" + movieId + ", starring=" + starring + "]";
	}
	/**
	 * @return the movieStarringId
	 */
	public Integer getMovieStarringId() {
		return MovieStarringId;
	}
	/**
	 * @param movieStarringId the movieStarringId to set
	 */
	public void setMovieStarringId(Integer movieStarringId) {
		MovieStarringId = movieStarringId;
	}

}
