package com.cn.lingrui.sellPersonnel.pojos.region;

public class RegionsPojo {

	private String regionName;
	
	private String typeRadio;
	
	private String regionId;
	
	// 提交添加地区下辖行政区县
	private String addAreaContain_cityValue;	
	private String addAreaContain_contyValue;
	private String addAreaContain_regionId;
	
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getTypeRadio() {
		return typeRadio;
	}
	public void setTypeRadio(String typeRadio) {
		this.typeRadio = typeRadio;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getAddAreaContain_cityValue() {
		return addAreaContain_cityValue;
	}
	public void setAddAreaContain_cityValue(String addAreaContain_cityValue) {
		this.addAreaContain_cityValue = addAreaContain_cityValue;
	}
	public String getAddAreaContain_regionId() {
		return addAreaContain_regionId;
	}
	public void setAddAreaContain_regionId(String addAreaContain_regionId) {
		this.addAreaContain_regionId = addAreaContain_regionId;
	}
	public String getAddAreaContain_contyValue() {
		return addAreaContain_contyValue;
	}
	public void setAddAreaContain_contyValue(String addAreaContain_contyValue) {
		this.addAreaContain_contyValue = addAreaContain_contyValue;
	}
	
	
}
