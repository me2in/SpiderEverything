package com.wong.spider.movie.enums;

public enum MovieInfoEnum {
	
	ANOTHERNAME("又名",1),
	AREA("地区",2),
	TYPE("类型",3),
	DIRECTOR("导演",4),
	WRITER("编剧",5),
	STAR("主演",6),
	IMDB("IMDB",7),
	DOUBAN("豆瓣",8);
	
	private String name;
	private int code;
	
	private MovieInfoEnum(String name,int code){
		this.name = name;
		this.code = code;
	}

}
