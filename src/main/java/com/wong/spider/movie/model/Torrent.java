package com.wong.spider.movie.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name="torrent")
public class Torrent {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer torrentId;
	private Integer movieId;
	@Column(length=800,nullable=true)
	private String magnet;
	private String zimu;
	private String fileName;
	private String fileSize;
	private String type;
	private String createTime;
	@Column(length=500)
	private String url;
	@Temporal(TemporalType.TIMESTAMP)
	private Date time = new Date();
	/**
	 * @return the torrentId
	 */
	public Integer getTorrentId() {
		return torrentId;
	}
	/**
	 * @param torrentId the torrentId to set
	 */
	public void setTorrentId(Integer torrentId) {
		this.torrentId = torrentId;
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
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the fileSize
	 */
	public String getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	/**
	 * @return the magnet
	 */
	public String getMagnet() {
		return magnet;
	}
	/**
	 * @param magnet the magnet to set
	 */
	public void setMagnet(String magnet) {
		this.magnet = magnet;
	}
	/**
	 * @return the zimu
	 */
	public String getZimu() {
		return zimu;
	}
	/**
	 * @param zimu the zimu to set
	 */
	public void setZimu(String zimu) {
		this.zimu = zimu;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Torrent [torrentId=" + torrentId + ", movieId=" + movieId
				+ ", magnet=" + magnet + ", zimu=" + zimu + ", fileName="
				+ fileName + ", fileSize=" + fileSize + ", type=" + type
				+ ", createTime=" + createTime + ", url=" + url + ", time="
				+ time + "]";
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
	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
