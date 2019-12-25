package com.dji.comm.og.service.dto;

public class Param {
	
	private String name;
	private String minVal;
	private String maxVal;
	private String defaultVal;
	private String actVal;
	
	public Param(String name) {
		this.name=name;
	}
	
	public Param(String name, String minVal, String maxVal, String defaultVal, String actVal) {
		super();
		this.name = name;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.defaultVal = defaultVal;
		this.actVal = actVal;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMinVal() {
		return minVal;
	}
	public void setMinVal(String minVal) {
		this.minVal = minVal;
	}
	public String getMaxVal() {
		return maxVal;
	}
	public void setMaxVal(String maxVal) {
		this.maxVal = maxVal;
	}
	public String getDefaultVal() {
		return defaultVal;
	}
	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}
	public String getActVal() {
		return actVal;
	}
	public void setActVal(String actVal) {
		this.actVal = actVal;
	}

	@Override
	public String toString() {
		return ""+this.name;
	}
	
	
	
}
