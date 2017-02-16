package com.wong.spider.movie.enums;

public enum MovieTypeEnum {
	
	JUQING(1,"剧情"  ),
	XIJU(9,"喜剧"  ),
	JINGSONG(18,"惊悚"  ),
	DONGZUO(4,"动作"  ),
	AIQING(2,"爱情"  ),
	KONGBU(11,"恐怖"  ),
	FANZUI(19,"犯罪"  ),
	MAOXIAN(6,"冒险"  ),
	XUANYI(17,"悬疑"  ),
	KEHUAN(12,"科幻"  ),
	JIATING(21,"家庭"  ),
	QIHUAN(5,"奇幻"  ),
	JILUPIAN(14,"纪录片"),
	DONGHUA(20,"动画"  ),
	ZHANZHENG(41,"战争"  ),
	ZHUANJI(28,"传记"  ),
	LISHI(49,"历史"  ),
	YINYUE(23,"音乐"  ),
	GEWU(58,"歌舞"  ),
	XIBU(10,"西部"  );

	private String name;
	private int code;
	
	private MovieTypeEnum(int code,String name){
		this.code = code;
		this.name = name;
	}
	
	public static Integer getCodeByName(String name){
		for(MovieTypeEnum t : MovieTypeEnum.values()){
			if(t.getName().equals(name)){
				return t.getCode();
			}
		}
		return null;
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
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
}
