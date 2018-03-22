package com.cn.lingrui.sellPersonnel.pojos.check;

import java.util.ArrayList;
import java.util.List;

import com.cn.lingrui.sellPersonnel.db.dbpojos.check.Province;

public class CurrentAreas_provinces {

	private String REGION_ID;
	private List<Province> provinces = new ArrayList<>();
	public String getREGION_ID() {
		return REGION_ID;
	}
	public void setREGION_ID(String rEGION_ID) {
		REGION_ID = rEGION_ID;
	}
	public List<Province> getProvinces() {
		return provinces;
	}
	public void setProvinces(List<Province> provinces) {
		this.provinces = provinces;
	}
	
}
