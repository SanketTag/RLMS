package com.rlms.constants;

public enum RLMSConstants {

	ACTIVE(1,"Active"),
	INACTIVE(0,"In Active");
	private Integer id;
	private String name;
	
	RLMSConstants(Integer id, String name){
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
