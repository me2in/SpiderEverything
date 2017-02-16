package com.wong.spider.movie.enums;

public enum AreaEnum {
	MEIGUO(7  ,"美国"),
	YINGGUO(13 ,"英国"),
	FAGUO(15 ,"法国"),
	RIBEN(16 ,"日本"),
	ZHONGGUANDALU(54 ,"中国大陆"),
	XIANGANG(26 ,"香港"),
	JIANADA(24 ,"加拿大"),
	DEGUO(34 ,"德国"),
	YINGDU(59 ,"印度"),
	HANGUO(3  ,"韩国"),
	YIDALI(22 ,"意大利"),
	AODALIYA(64 ,"澳大利亚"),
	SIBANYA(37 ,"西班牙"),
	TAIWAN(39 ,"台湾"),
	BILISHI(43 ,"比利时"),
	RUIDIAN(32 ,"瑞典"),
	HELAN(53 ,"荷兰"),
	DANMAI(31 ,"丹麦"),
	ELUOSI(51 ,"俄罗斯"),
	XIDE(103,"西德");
	
	private int code;
	private String name;
	
	private AreaEnum(int code,String name){
		this.code = code;
		this.name = name;
	}
	
	public static Integer getCodeByName(String name){
		for(AreaEnum e :AreaEnum.values()){
			if(e.getName().equals(name)){
				return e.getCode();
			}
		}
		return null;
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
}
