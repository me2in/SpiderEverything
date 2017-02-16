package com.wong.spider.movie.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="movie")
public class Movie {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer movieId;
	private String anotherName;
	private String name;
	private String score;
	private String imdb;
	private String douban;
	private String year;
	@Column(length=500,nullable=true)
	private String poster;
	private String detailUrl;
	
	@Transient
	private List<String> area;
	@Transient
	private List<String> director;
	@Transient
	private List<String> starring;
	@Transient
	private List<String> writer;
	@Transient
	private List<String> type;
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the area
	 */
	public List<String> getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(List<String> area) {
		this.area = area;
	}
	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}
	/**
	 * @return the imdb
	 */
	public String getImdb() {
		return imdb;
	}
	/**
	 * @param imdb the imdb to set
	 */
	public void setImdb(String imdb) {
		this.imdb = imdb;
	}
	/**
	 * @return the douban
	 */
	public String getDouban() {
		return douban;
	}
	/**
	 * @param douban the douban to set
	 */
	public void setDouban(String douban) {
		this.douban = douban;
	}
	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", anotherName=" + anotherName
				+ ", name=" + name + ", area=" + area + ", score=" + score
				+ ", imdb=" + imdb + ", douban=" + douban + ", year=" + year
				+ ", poster=" + poster + ", director=" + director
				+ ", starring=" + starring + ", writer=" + writer + ", type="
				+ type + "]";
	}
	/**
	 * @return the poster
	 */
	public String getPoster() {
		return poster;
	}
	/**
	 * @param poster the poster to set
	 */
	public void setPoster(String poster) {
		this.poster = poster;
	}
	/**
	 * @return the anotherName
	 */
	public String getAnotherName() {
		return anotherName;
	}
	/**
	 * @param anotherName the anotherName to set
	 */
	public void setAnotherName(String anotherName) {
		this.anotherName = anotherName;
	}
	/**
	 * @return the director
	 */
	public List<String> getDirector() {
		return director;
	}
	/**
	 * @param director the director to set
	 */
	public void setDirector(List<String> director) {
		this.director = director;
	}
	/**
	 * @return the starring
	 */
	public List<String> getStarring() {
		return starring;
	}
	/**
	 * @param starring the starring to set
	 */
	public void setStarring(List<String> starring) {
		this.starring = starring;
	}
	/**
	 * @return the writer
	 */
	public List<String> getWriter() {
		return writer;
	}
	/**
	 * @param writer the writer to set
	 */
	public void setWriter(List<String> writer) {
		this.writer = writer;
	}
	/**
	 * @return the type
	 */
	public List<String> getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(List<String> type) {
		this.type = type;
	}
	/**
	 * @return the detailUrl
	 */
	public String getDetailUrl() {
		return detailUrl;
	}
	/**
	 * @param detailUrl the detailUrl to set
	 */
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

}
